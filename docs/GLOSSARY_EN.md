# Glossary - Balls

**English** | **[Español](GLOSSARY.md)**

This glossary provides definitions for key terms and concepts used throughout the Balls project, a real-time 2D physics simulation engine built in Java.

## Table of Contents

- [General Terms](#general-terms)
- [MVC Pattern Terms](#mvc-pattern-terms)
- [Game Engine Terms](#game-engine-terms)
- [Programming Concepts](#programming-concepts)
- [Graphics & Rendering](#graphics--rendering)
- [Input & Controls](#input--controls)
- [Audio Terms](#audio-terms)
- [Physics & Collision](#physics--collision)

---

## General Terms

### **API (Application Programming Interface)**
A set of functions, classes, and protocols that allow developers to interact with the game engine and create games.

### **Asset**
Any resource used in a game, including images, sounds, music, fonts, scripts, and data files.

### **Build**
The process of compiling and packaging the game code and assets into an executable format.

### **Debug Mode**
A development mode that provides additional logging, error checking, and diagnostic tools to help identify and fix issues.

### **Dependency**
An external library or module that the engine relies on to provide specific functionality.

### **Framework**
A structured foundation that provides common functionality and enforces architectural patterns for building applications.

### **Library**
A collection of pre-written code that developers can use to perform common tasks without writing code from scratch.

### **Module**
A self-contained unit of code that encapsulates specific functionality and can be used independently or combined with other modules.

### **Plugin**
An add-on component that extends the engine's functionality without modifying the core codebase.

---

## MVC Pattern Terms

### **MVC (Model-View-Controller)**
An architectural pattern that separates an application into three interconnected components to organize code and improve maintainability.

### **Model**
The data and simulation layer of the MVC pattern. In Balls, the Model manages the complete game state, physics simulation, and all bodies (entities). It runs on its own thread(s), updating physics continuously and providing snapshots (DTOs) to the Controller for rendering. The Model never directly accesses the View.

### **View**
The presentation layer of the MVC pattern. In Balls, the View is a Swing-based window that handles rendering and input capture. It requests visual resources, manages the Renderer (rendering loop), and translates keyboard input into Controller commands. The View accesses Model data only through Controller-provided snapshots.

### **Controller**
The coordination layer of the MVC pattern. In Balls, the Controller bridges user input from the View to Model commands, manages the engine lifecycle (initialization, activation, shutdown), and provides snapshot getters that the Renderer uses to pull dynamic body data. It also implements game rules by mapping events to actions.

### **Separation of Concerns**
A design principle where different aspects of the application (data, presentation, logic) are kept separate to improve maintainability and testability. In Balls, the Model handles simulation, the View handles rendering/input, and the Controller coordinates between them.

### **Observer Pattern**
A design pattern where the View observes the Model for changes and updates automatically when the Model's state changes.

### **Data Binding**
The process of connecting the Model's data to the View's display, allowing automatic updates when data changes.

---

## Body System Terms

### **Body**
A game object that exists within the simulation world. In Balls, bodies are the core entities managed by the Model. See AbstractBody, DynamicBody, StaticBody, PlayerBody, and DecoBody.

### **AbstractBody**
The base abstract class for all bodies in the simulation. Defines common properties like unique entityId, BodyState lifecycle (STARTING → ALIVE → DEAD), physics engine reference, and lifecycle management methods (activate(), die()). Maintains static counters for tracking created, alive, and dead bodies globally.

### **DynamicBody**
A body with active physics simulation that moves and rotates according to physics rules. Each DynamicBody runs on its own dedicated thread, continuously updating position, velocity, and acceleration using its BasicPhysicsEngine. Examples: asteroids, projectiles, missiles.

### **StaticBody**
A body with a fixed position that does not move during simulation. Uses NullPhysicsEngine (no physics calculations). Static bodies have no thread and are used for obstacles, decorative elements, and non-moving world elements. Examples: planets, space stations, decorative objects.

### **PlayerBody**
A special DynamicBody that represents a player-controlled entity. Extends DynamicBody with player control capabilities (thrust, rotation, firing), a weapon system with multiple weapon slots, and configurable parameters like max thrust force and angular acceleration.

### **DecoBody**
A purely decorative body (like TemporaryDecoBody) used for visual-only elements with no gameplay impact or physics simulation. Used for background elements and temporary visual effects.

### **PhysicsBody**
An interface that marks bodies capable of physics simulation. Provides default methods for obtaining physics values, applying movement, and handling world boundary bounces.

### **BodyState**
An enumeration defining the lifecycle states of a body:
- **STARTING**: Body created but not yet activated
- **ALIVE**: Body is active in the simulation  
- **DEAD**: Body marked for removal

### **BodyDTO**
A Data Transfer Object containing immutable snapshot data about a body for safe transfer between Model and View layers. Contains entityId, assetId, size, position (x, y), and angle.

## Physics Engine Terms

### **PhysicsEngine**
An interface defining the contract for physics calculations. Responsible for computing new physics values based on elapsed time, managing boundary bounces, and updating thrust and angular acceleration. Different implementations provide different physics behaviors.

### **BasicPhysicsEngine**
A concrete physics engine that applies realistic 2D kinematics using MRUA (Uniformly Accelerated Rectilinear Motion) integration. Calculates velocity (v₁ = v₀ + a·Δt), position (x₁ = x₀ + v_avg·Δt), and rotation based on angular velocity. Applies thrust according to the body's current angle.

### **NullPhysicsEngine**
A "null object" physics engine used by StaticBody. Performs no calculations and maintains constant physics values, ensuring static bodies remain immobile without computational overhead.

### **AbstractPhysicsEngine**
An abstract base class providing common implementation for physics engines, including physics value management and boundary bounce handling.

### **PhysicsValuesDTO**
An immutable Data Transfer Object encapsulating complete physics state at a specific moment:
- **timeStamp**: Timestamp in nanoseconds
- **posX, posY**: Position in 2D space
- **speedX, speedY**: Velocity components  
- **accX, accY**: Acceleration components
- **angle**: Rotation angle in degrees
- **angularSpeed**: Rotational velocity (degrees/second)
- **angularAcc**: Angular acceleration (degrees/second²)  
- **thrust**: Applied thrust force
- **size**: Body size

### **Thrust**
A force applied to a body in the direction of its current angle. Used to propel spaceships and other dynamic objects. In Balls, thrust is applied by PlayerBody when the user presses movement keys.

### **Angular Velocity**
The rate of rotation of a body, measured in degrees per second. Determines how fast a body spins.

### **Angular Acceleration**  
The rate of change of angular velocity over time, measured in degrees per second squared. Controls how quickly rotation speed changes.

---

## Programming Concepts

### **Class**
A blueprint for creating objects that defines properties (data) and methods (behavior).

### **Object**
An instance of a class with specific data values.

### **Inheritance**
A mechanism where a class can inherit properties and methods from a parent class, promoting code reuse.

### **Interface**
A contract that defines a set of methods that implementing classes must provide.

### **Polymorphism**
The ability of different classes to be treated as instances of the same class through inheritance or interfaces.

### **Encapsulation**
The practice of bundling data and methods that operate on that data within a single unit (class) and restricting direct access to some components.

### **Callback**
A function passed as an argument to another function to be executed at a later time, often in response to an event.

### **Event**
A notification that something has occurred in the system (e.g., key press, collision, timer expiration).

### **Event Handler**
A function that responds to a specific event when it occurs.

### **Exception**
An error or unexpected condition that occurs during program execution.

### **Singleton**
A design pattern that ensures only one instance of a class exists throughout the application.

---

## Graphics & Rendering

### **Sprite**
A 2D image or animation that represents a visual element in the game.

### **Texture**
An image applied to a surface or sprite to give it visual detail.

### **Canvas**
The drawing surface where graphics are rendered.

### **Buffer**
A temporary storage area for graphics data before it's displayed on screen. Double buffering prevents flickering.

### **Rendering Pipeline**
The sequence of steps the engine takes to transform game data into pixels on the screen.

### **Layer**
A rendering level that determines the draw order of visual elements. Higher layers appear in front of lower layers.

### **Z-Index**
A value that determines the rendering order of elements on the same layer. Higher values are drawn on top.

### **Viewport**
The visible portion of the game world displayed on screen.

### **Camera**
An entity that defines what portion of the game world is visible in the viewport.

### **Transform**
The position, rotation, and scale of an entity in the game world.

### **Animation**
A sequence of images or transformations displayed over time to create the illusion of movement.

### **Frame**
A single image in an animation sequence.

### **Sprite Sheet**
A single image file containing multiple sprites or animation frames arranged in a grid.

### **Tile**
A small, reusable graphic element used to build larger game environments efficiently.

### **Tilemap**
A grid-based level layout constructed from tiles.

---

## Input & Controls

### **Input Manager**
A system component that captures and processes user input from various devices.

### **Key Binding**
The association between a specific key or button and a game action.

### **Input Event**
A notification that an input device state has changed (key pressed, mouse moved, etc.).

### **Polling**
Checking the state of input devices at regular intervals during the game loop.

### **Event-Driven Input**
Responding to input changes through callback functions triggered when input events occur.

### **Gesture**
A complex input pattern recognized by the system (e.g., swipe, pinch, double-tap).

---

## Weapon System Terms

### **Weapon**
An interface/component that can fire projectiles. In Balls, weapons are attached to PlayerBody and managed through the weapon system.

### **AbstractWeapon**
The base abstract class for all weapon implementations. Provides:
- Immutable weapon identification
- Thread-safe firing request mechanism using AtomicLong  
- Unified API for discrete-tick weapon updates
- Fire rate control and ammunition management
Weapons are passive components with no threads; they only fire when update(dt) is called by the owning body.

### **BasicWeapon**
A standard weapon implementation with cooldown-based fire rate limiting and ammunition/reload mechanics. Fires single projectiles when requested, respecting fire rate (shots per second) and ammo constraints.

### **BurstWeapon**
A weapon that fires multiple projectiles in quick succession (a burst) when triggered. Manages burst timing and projectile count internally.

### **MineLauncher**
A specialized weapon that deploys stationary mines that persist in the world.

### **MissileLauncher**
A weapon that fires guided or unguided missile projectiles with different flight characteristics than standard bullets.

### **WeaponDto**
A Data Transfer Object containing weapon configuration:
- Fire rate (shots per second)
- Maximum ammunition capacity
- Reload time (seconds)
- Projectile properties (size, speed, lifetime, asset)

### **WeaponFactory**
A factory class for creating weapon instances with predefined configurations.

### **WeaponType**
An enumeration of available weapon types in the game.

---

## Event and Action System Terms

### **ActionDTO**
A Data Transfer Object that encapsulates an action to be executed:
- **type**: The type of action (ActionType enum)
- **executor**: The component that will execute the action (ActionExecutor)
- **priority**: Execution priority (ActionPriority enum)

### **EventDTO**
A Data Transfer Object representing an event in the simulation:
- **entity**: The body that generated the event  
- **eventType**: The type of event (EventType enum)

### **ActionType**
An enumeration of action types that can be performed in the system (e.g., MOVE, DIE, FIRE).

### **EventType**  
An enumeration of event types that can occur during simulation (e.g., COLLIDED, WORLD_BOUNDARY_REACHED, MUST_FIRE).

### **ActionExecutor**
An interface for objects capable of executing actions on the Model.

### **ActionPriority**
An enumeration defining priority levels for action execution, ensuring critical actions (like entity death) are processed before others.

---

## State Management Terms

### **ModelState**
An enumeration defining the lifecycle states of the Model:
- **STARTING**: Model is initializing
- **ALIVE**: Model is running the simulation
- **STOPPED**: Model has been stopped

### **EngineState**  
An enumeration defining the lifecycle states of the entire engine (managed by Controller):
- **STARTING**: Engine is initializing
- **ALIVE**: Engine is running
- **PAUSED**: Engine is paused
- **STOPPED**: Engine has been stopped

## World Generation Terms

### **WorldDefinition**
A configuration object that defines a complete world setup:
- World dimensions (width, height)
- Asset catalog for visual resources
- Background definitions
- Decorator definitions  
- Body definitions (dynamic and static)
- Weapon configurations

### **WorldGenerator**
A class responsible for generating the initial world based on a WorldDefinition. Creates and places all initial bodies.

### **LifeGenerator**
An automated body generator that maintains simulation activity by creating new dynamic bodies when needed. Helps keep the simulation populated with entities.

## Rendering Terms

### **Renderer**
The component that manages the rendering loop. It runs on its own thread, continuously pulling dynamic body snapshots from the Controller and drawing them using Java Swing graphics. Handles layered rendering (backgrounds, static bodies, dynamic bodies, decorations, UI).

### **RenderDTO**
A Data Transfer Object containing immutable rendering information for static bodies. Includes entityId, assetId, size, position, and angle.

### **DynamicRenderDTO**
An extension of RenderDTO for dynamic bodies, adding timestamp and velocity/acceleration data for potential motion interpolation or effects.

## Asset Management Terms

### **AssetCatalog**
A catalog that organizes and manages all visual resources (sprites, images) used in the simulation. Groups assets by type and provides lookup functionality.

### **AssetType**
An enumeration of asset categories: backgrounds, gravity_bodies, solid_bodies, space_decors, spaceship, ui_decors, weapons.

### **Images**
A system for loading and caching images to support efficient resource management.

## Threading and Concurrency Terms

### **Thread-Safe Collections**
Collections designed for safe concurrent access from multiple threads. Balls uses ConcurrentHashMap for managing bodies in the Model.

### **Volatile Variables**
Variables marked with the `volatile` keyword to ensure visibility across threads. Used for ModelState, EngineState, and BodyState to guarantee all threads see the latest values.

### **Immutable Objects**
Objects that cannot be modified after creation, guaranteeing thread safety. PhysicsValuesDTO and DTOs are immutable, allowing safe sharing between threads without synchronization.

### **Dedicated Thread**
Each DynamicBody runs on its own dedicated thread, continuously updating its physics state independently.

## Utility Terms

### **DoubleVector**
A utility class for 2D vector mathematics, providing common vector operations used in physics calculations.

### **RandomArrayList**
A specialized ArrayList that provides random element selection, used for procedural content generation.

---

## Document Information

**Version:** 1.0  
**Last Updated:** 2026-01-01  
**Maintained By:** Balls Development Team  

For additional information, please refer to the main documentation or visit the project repository.
