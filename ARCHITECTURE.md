# Balls - Architecture Documentation

**English** | **[Español](ARCHITECTURE_ES.md)**

This document provides detailed architectural documentation for the main classes in the Balls game engine. These descriptions are extracted from the header comments in the source code and serve as a reference for understanding the design patterns, threading models, and responsibilities of each component.

## Table of Contents

1. [MVC Core Components](#mvc-core-components)
   - [Controller](#controller)
   - [Model](#model)
   - [View](#view)
   - [Renderer](#renderer)
2. [Entity System](#entity-system)
   - [DynamicBody](#dynamicbody)
   - [StaticBody](#staticbody)
3. [Weapon System](#weapon-system)
   - [AbstractWeapon](#abstractweapon)

---

## Controller

**Source:** `src/controller/Controller.java`

Controller
----------

Central coordinator of the MVC triad:
  - Owns references to Model and View.
  - Performs engine startup wiring (assets, world definition, dimensions, limits).
  - Bridges user input (View) into Model commands.
  - Provides snapshot getters used by the Renderer (via the View).

Responsibilities (high level)
-----------------------------

1) Bootstrapping / activation sequence
   - Validates that all required dependencies are present (assets, world,
     dimensions, max bodies, model, view).
   - Loads visual resources into the View (View.loadAssets).
   - Configures the View and starts the Renderer loop (View.activate).
   - Configures the Model (dimension, max bodies) and starts simulation
     (Model.activate).
   - Switches controller state to ALIVE when everything is ready.

2) World building / entity creation
   - addDBody / addSBody / addDecorator / addPlayer delegate entity creation
     to the Model.
   - Important: static bodies and decorators are "push-updated" into the View:
     after adding a static/decorator entity, the controller fetches a fresh
     static/decorator snapshot from the Model and pushes it to the View
     (View.updateSBodyInfo / View.updateDecoratorsInfo). This matches the design
     where static/decorator visuals usually do not change every frame, so you
     avoid unnecessary per-frame updates.

3) Runtime command dispatch
   - Exposes high-level player commands that the View calls in response to input:
     playerThrustOn / playerThrustOff / playerReverseThrust
     playerRotateLeftOn / playerRotateRightOn / playerRotateOff
     playerFire
   - All of these are simple delegations to the Model, keeping the View free
     of simulation logic.

4) Snapshot access for rendering
   - getDBodyInfo(): returns dynamic snapshot data from the Model. This is
     intended to be pulled frequently (typically once per frame by the
     Renderer thread).
   - getSBodyInfo() / getDecoratorInfo(): used to push snapshots when
     static/decorator content changes.

5) Game rules / decision layer (rule-based actions)
   - decideActions(entity, events) takes Model events (EventDTO) and produces
     a list of actions (ActionDTO).
   - applyGameRules(...) maps events -> actions:
     * World boundary reached => DIE (high priority)
     * MUST_FIRE => FIRE (high priority)
     * COLLIDED / NONE => no additional action
   - If no "death-like" action is present, MOVE is appended by default.
     This creates a deterministic baseline: entities always move unless
     explicitly killed/exploded.

Engine state
------------
engineState is volatile and represents the Controller's view of the engine
lifecycle:
  - STARTING: initial state after construction
  - ALIVE: set after activate() finishes successfully
  - PAUSED: set via enginePause()
  - STOPPED: set via engineStop()

Dependency injection rules
--------------------------
  - setModel(model): stores the model and injects the controller back into
    the model (model.setController(this)). This enables callbacks / rules
    decisions if the Model consults the Controller.
  - setView(view): stores the view and injects the controller into the view
    (view.setController(this)). This enables the View to send player commands
    and to pull snapshots.

Threading notes
---------------
  - The Controller itself mostly acts as a facade. The key concurrency point
    is snapshot access: Renderer thread pulls getDBodyInfo() frequently.
    Static/decorator snapshots are pushed occasionally from the "logic side"
    (model->controller->view).
  - Keeping Controller methods small and side-effect-light reduces contention
    and makes it easier to reason about where cross-thread interactions happen.

---

## Model

**Source:** `src/model/Model.java`

Model
-----

Core simulation layer of the MVC triad. The Model owns and manages all
entities (dynamic bodies, static bodies, players, decorators) and orchestrates
their lifecycle, physics updates, and interactions.

Responsibilities
----------------
  - Entity management: create, activate, and track all simulation entities
  - Provide thread-safe snapshot data (EntityInfoDTO / DBodyInfoDTO) to the
    Controller for rendering
  - Delegate physics updates to individual entity threads
  - Maintain entity collections with appropriate concurrency strategies
  - Enforce world boundaries and entity limits

Entity types
------------
The Model manages several distinct entity categories:

1) Dynamic Bodies (dBodies)
   - Entities with active physics simulation (ships, asteroids, projectiles)
   - Each runs on its own thread, continuously updating position/velocity
   - Stored in ConcurrentHashMap for thread-safe access

2) Player Bodies (pBodies)
   - Special dynamic bodies with player controls and weapons
   - Keyed by player ID string
   - Support thrust, rotation, and firing commands

3) Static Bodies (sBodies)
   - Non-moving entities with fixed positions (obstacles, platforms)
   - No physics thread
   - Push-updated to View when created/modified

4) Gravity Bodies (gravityBodies)
   - Static bodies that exert gravitational influence
   - Used for planetary bodies or black holes

5) Decorators (decorators)
   - Visual-only entities with no gameplay impact (background elements)
   - Push-updated to View when created/modified

Lifecycle
---------
Construction:
  - Model is created in STARTING state
  - Entity maps are pre-allocated with expected capacities

Activation (activate()):
  - Validates that Controller, world dimensions, and max entities are set
  - Transitions to ALIVE state
  - After activation, entities can be created and activated

Snapshot generation
-------------------
The Model provides snapshot methods that return immutable DTOs:
  - getDBodyInfo(): returns List<DBodyInfoDTO> for all active dynamic bodies
  - getSBodyInfo(): returns List<EntityInfoDTO> for all active static bodies
  - getDecoratorInfo(): returns List<EntityInfoDTO> for all decorators

These snapshots are pulled by the Controller and pushed to the View/Renderer.
The pattern ensures clean separation: rendering never accesses mutable
entity state directly.

Concurrency strategy
--------------------
  - All entity maps use ConcurrentHashMap for thread-safe access
  - Individual entities manage their own thread synchronization
  - Model state transitions are protected by volatile fields
  - Snapshot methods create independent DTO lists to avoid concurrent
    modification during rendering

Design goals
------------
  - Keep simulation logic isolated from view concerns
  - Provide deterministic, thread-safe entity management
  - Support high entity counts (up to MAX_ENTITIES = 5000)
  - Enable efficient parallel physics updates via per-entity threads

---

## View

**Source:** `src/view/View.java`

View
----

Swing top-level window that represents the presentation layer of the engine.
This class wires together:
  - The rendering surface (Renderer)
  - Asset loading and image catalogs (Images)
  - User input (KeyListener) and command dispatch to the Controller

Architectural role
------------------
View is a thin façade over rendering + input:
  - It does not simulate anything.
  - It does not own world state.
  - It communicates with the model exclusively through the Controller.

The Renderer pulls dynamic snapshots every frame (via View -> Controller),
while static/decorator snapshots are pushed into the View/Renderer only when
they change (to avoid redundant per-frame updates for entities that do not
move).

Lifecycle
---------
Construction:
  - Creates the ControlPanel (UI controls, if any).
  - Creates the Renderer (Canvas).
  - Builds the JFrame layout and attaches the key listener.

Activation (activate()):
  - Validates mandatory dependencies (dimensions, background, image catalogs).
  - Injects view dimensions and images into the Renderer.
  - Starts the Renderer thread (active rendering loop).

Asset management
----------------
loadAssets(...) loads and registers all visual resources required by the world:
  - Background image (single BufferedImage).
  - Dynamic body sprites (ships, asteroids, missiles, etc.).
  - Static body sprites (gravity bodies, bombs, etc.).
  - Decorator sprites (parallax / space decor).

The View stores catalogs as Images collections, which are later converted
into GPU/compatible caches inside the Renderer (ImageCache).

Engine state delegation
-----------------------
View exposes getEngineState() as a convenience bridge for the Renderer.
The render loop can stop or pause based on Controller-owned engine state.

Input handling
--------------
Keyboard input is captured at the rendering Canvas level (Renderer is
focusable and receives the KeyListener) and translated into high-level
Controller commands:
  - Thrust on/off (forward uses positive thrust; reverse thrust is handled
    as negative thrust, and both are stopped via the same thrustOff command).
  - Rotation left/right and rotation off.
  - Fire: handled as an edge-triggered action using fireKeyDown to prevent
    key repeat from generating continuous shots while SPACE is held.

Focus and Swing considerations
-------------------------------
The Renderer is the focus owner for input. Focus is requested after the frame
becomes visible using SwingUtilities.invokeLater(...) to improve reliability
with Swing's event dispatch timing.

Threading considerations
------------------------
Swing is single-threaded (EDT), while rendering runs on its own thread.
This class keeps its responsibilities minimal:
  - It only pushes static/decorator updates when needed.
  - Dynamic snapshot pulling is done inside the Renderer thread through
    View -> Controller getters.

Design goals
------------
  - Keep the View as a coordinator, not a state holder.
  - Keep rendering independent and real-time (active rendering).
  - Translate user input into controller commands cleanly and predictably.

---

## Renderer

**Source:** `src/view/Renderer.java`

Renderer
--------

Active rendering loop responsible for drawing the current frame to the
screen. This class owns the rendering thread and performs all drawing using
a BufferStrategy-based back buffer.

Architectural role
------------------
The Renderer is a pull-based consumer of visual snapshots provided by the View.
It never queries or mutates the model directly.

Rendering is decoupled from simulation through immutable snapshot DTOs
(EntityInfoDTO / DBodyInfoDTO), ensuring that rendering remains deterministic
and free of model-side race conditions.

Threading model
---------------
  - A dedicated render thread drives the render loop (Runnable).
  - Rendering is active only while the engine state is ALIVE.
  - The loop terminates cleanly when the engine reaches STOPPED.

Data access patterns
--------------------
Three different renderable collections are used, each with a consciously chosen
concurrency strategy based on update frequency and thread ownership:

1) Dynamic bodies (DBodies)
   - Stored in a plain HashMap.
   - Updated and rendered exclusively by the render thread.
   - No concurrent access → no synchronization required.

2) Static bodies (SBodies)
   - Rarely updated, potentially from non-render threads
     (model → controller → view).
   - Stored using a copy-on-write strategy:
     * Updates create a new Map instance.
     * The reference is swapped atomically via a volatile field.
   - The render thread only reads stable snapshots.

3) Decorators
   - Same access pattern as static bodies.
   - Uses the same copy-on-write + atomic swap strategy.

This design avoids locks, minimizes contention, and guarantees that the
render thread always iterates over a fully consistent snapshot.

Frame tracking
--------------
A monotonically increasing frame counter (currentFrame) is used to:
  - Track renderable liveness.
  - Remove obsolete renderables deterministically.

Each update method captures a local frame snapshot to ensure internal
consistency, even if the global frame counter advances later.

Rendering pipeline
------------------
Per frame:
  1) Background is rendered to a VolatileImage for fast blitting.
  2) Decorators are drawn.
  3) Static bodies are drawn.
  4) Dynamic bodies are updated and drawn.
  5) HUD elements (FPS) are rendered last.

Alpha compositing is used to separate opaque background rendering from
transparent entities.

Performance considerations
--------------------------
  - Triple buffering via BufferStrategy.
  - VolatileImage used for background caching.
  - Target frame rate ~60 FPS (16 ms delay).
  - FPS is measured using a rolling one-second window.

Design goals
------------
  - Deterministic rendering.
  - Zero blocking in the render loop.
  - Clear ownership of mutable state.
  - Explicit, documented concurrency decisions.

This class is intended to behave as a low-level rendering component suitable
for a small game engine rather than a UI-centric Swing renderer.

---

## DynamicBody

**Source:** `src/model/entities/DynamicBody.java`

DynamicBody
-----------

Represents a single dynamic entity in the simulation model.

Each DynamicBody maintains:
  - A unique identifier and visual attributes (assetId, size)
  - Its own PhysicsEngine instance, which stores and updates the immutable
    PhysicsValues snapshot (position, speed, acceleration, angle, etc.)
  - A dedicated thread responsible for advancing its physics state over time

Dynamic bodies interact exclusively with the Model, reporting physics updates
and requesting event processing (collisions, rebounds, etc.). The view layer
never reads mutable state directly; instead, DynamicBody produces a
DBodyInfoDTO snapshot encapsulating all visual and physical data required
for rendering.

Lifecycle control (STARTING → ALIVE → DEAD) is managed internally, and static
counters (inherited from AbstractEntity) track global quantities of created,
active and dead entities.

Threading model
---------------
Each DynamicBody runs on its own thread (implements Runnable). The physics
engine is updated continuously in the run() loop, with the entity checking
for events and processing actions based on game rules determined by the
Controller.

The goal of this class is to isolate per-object behavior and physics evolution
while keeping the simulation thread-safe through immutable snapshots and a
clearly separated rendering pipeline.

---

## StaticBody

**Source:** `src/model/entities/StaticBody.java`

StaticBody
----------

Represents a single static entity in the simulation model.

Each StaticBody maintains:
  - A unique identifier and visual attributes (assetId, size)
  - A NullPhysicsEngine instance with fixed position and angle
  - No dedicated thread (static bodies do not move or update)

Static bodies are used for non-moving world elements such as obstacles,
platforms, or decorative elements that have physical presence but no
dynamic behavior.

The view layer accesses static bodies through EntityInfoDTO snapshots,
following the same pattern as dynamic bodies but without the time-varying
physics data.

Lifecycle control (STARTING → ALIVE → DEAD) is managed internally, and static
counters (inherited from AbstractEntity) track global quantities of created,
active and dead entities.

Static vs. Dynamic
------------------
Unlike DynamicBody, StaticBody:
  - Uses NullPhysicsEngine (no physics updates)
  - Has no thread (no run() loop)
  - Returns EntityInfoDTO instead of DBodyInfoDTO (no velocity/acceleration)
  - Is intended for fixed-position world elements

This separation keeps the codebase clean and prevents unnecessary overhead
for entities that never move.

---

## AbstractWeapon

**Source:** `src/model/weapons/AbstractWeapon.java`

AbstractWeapon
--------------

Base class for all weapon implementations in the simulation.

This class provides:
  - Immutable identification (weapon id)
  - A static configuration object (WeaponDto)
  - A thread-safe monotonic firing-request mechanism
  - A unified API for discrete-tick weapon updates

CONCURRENCY MODEL
-----------------
  - The method registerFireRequest() may be invoked from *any thread*
    (typically from the Controller or Model in response to player input).
    It stores a timestamp (System.nanoTime) in an AtomicLong.

  - The weapon is a *passive component*: it owns no thread and performs no
    asynchronous work. All firing logic must occur when update(dtSeconds) is
    executed by the PlayerBody's simulation thread.

FIRING REQUEST MODEL
--------------------
  - lastFireRequest holds the timestamp of the *latest* firing request.
  - lastHandledRequest holds the timestamp of the most recent request already
    processed (shot produced or consciously ignored).

  - A new request exists if:
      lastFireRequest > lastHandledRequest

  - Calling hasNewRequest() consumes the request by advancing
    lastHandledRequest to lastFireRequest.

  - No request buffering or queuing is performed. Only the most recent firing
    intention matters. Requests that occur during cooldown or ongoing firing
    sequences are consumed and discarded. This yields simple and predictable
    behavior aligned with discrete-tick simulation.

DESIGN PHILOSOPHY
-----------------
  - Weapons do not run code outside update(): deterministic, thread-safe,
    and easy to reason about in a heavily multi-threaded simulation.

  - Firing intent is monotonic and edge-triggered. The system never stores
    multiple pending shots; weapons decide immediately whether to fire or
    ignore a request.

  - AbstractWeapon imposes no specific firing behavior; it standardizes only
    the request-consumption rules. Concrete weapons (BasicWeapon, BurstWeapon,
    MissileWeapon, etc.) implement all actual firing logic.

GUIDELINES FOR NEW WEAPON IMPLEMENTATIONS
------------------------------------------
If you are implementing a new weapon type, follow these principles:

1. **Never block the update() thread.** All logic must be fast and strictly
   local to one tick.

2. **Use hasNewRequest() only to detect and consume intent.** Do not attempt
   to re-interpret or store past requests.

3. **Return at most one shot per tick.** Multishot or burst weapons should
   spread shots across ticks unless the game explicitly supports simultaneous
   multi-projectile emission.

4. **Manage your own internal timing.** Use dtSeconds to reduce cooldowns,
   burst timers, or acceleration windows.

5. **Do not create projectiles inside update().** update() should only signal
   intent (true/false). The Model or caller is responsible for instantiating
   DynamicBody projectiles.

6. **Be explicit about request-handling policy.** Decide whether a weapon
   ignores requests during cooldown, collapses multiple requests into one, or
   only starts bursts when idle.

7. **Keep the weapon stateless with respect to threading.** No background
   threads, no timers, no sleeps; everything happens during the PlayerBody's
   tick cycle.

These rules ensure that all weapons behave consistently inside the engine,
remain deterministic, and do not introduce concurrency hazards.

---


## Contributing

When modifying any of these classes, please ensure that:

1. **Header comments remain synchronized** with the actual implementation
2. **Design patterns** described in the headers are followed
3. **Threading guarantees** are maintained
4. **Concurrency models** are respected

To update this documentation, regenerate it from the source headers or manually update both the source files and this document.

## Related Documentation

For detailed definitions of concepts, technical terms, and system components, see:

**[GLOSSARY_EN.md](GLOSSARY_EN.md)** - Complete glossary in English

## Version

This documentation was generated from the source code headers. Last updated: 2025-12-16

---

*For implementation details and code examples, refer to the source files directly.*
