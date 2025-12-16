# Glossary of Concepts - Balls

**English** | **[Español](GLOSSARY.md)**

This document provides a glossary of the most important concepts used in the Balls project, a real-time 2D physics engine implemented in Java.

## Architecture and Design Patterns

### MVC (Model-View-Controller)
Architectural pattern that separates the application into three main components:
- **Model**: Manages game state and simulation logic
- **View**: Handles visual presentation and rendering
- **Controller**: Coordinates communication between Model and View, processing user input

### Model
Component representing the game logic layer. Manages all entities, physics simulation, and world state. Runs in its own thread to keep the simulation independent from rendering.

### View
Presentation layer that handles visual rendering using Java Swing. Requests snapshots of game state through the Controller and renders them on screen. Contains no simulation logic.

### Controller
Central coordinator of the MVC architecture. Connects Model and View, manages engine initialization, processes user commands, and provides access to state snapshots for rendering.

## Entities

### Entity (AbstractEntity)
Abstract base class for all entities in the simulation. Defines:
- Unique identifier (`entityId`)
- Lifecycle state (`EntityState`: STARTING, ALIVE, DEAD)
- Visual asset reference (`assetId`)
- Size (`size`)
- Static counters for tracking created, alive, and dead entities

### DynamicBody
Dynamic entity that moves and rotates according to physics laws. Features:
- Runs in its own dedicated thread
- Has a `PhysicsEngine` that calculates its movement
- Continuously updates its position, velocity, and acceleration
- Interacts with the world (collisions, boundary bounces)
- Examples: asteroids, projectiles

### StaticBody
Static entity that doesn't move during simulation. Features:
- No dedicated thread
- Uses `NullPhysicsEngine` (no physics calculations)
- Fixed position, rotation, and size
- Used for obstacles, static visual decoration
- Examples: planets, decorative obstacles

### PlayerBody
Special entity that extends `DynamicBody` and represents the player. Features:
- Keyboard control (thrust, rotation, fire)
- Weapon system with multiple slots
- Configurable parameters: max thrust force, angular acceleration
- Unique player identifier (`playerId`)

### DecoEntity
Purely decorative entity without physics or game logic. Used for visual elements that don't interact with the world.

### PhysicsBody
Interface that marks entities that have physical behavior. Provides default methods for:
- Getting physics values (`PhysicsValues`)
- Applying movement
- Managing boundary bounces

### EntityState
Enumeration defining entity lifecycle states:
- **STARTING**: Entity created but not activated
- **ALIVE**: Entity active in simulation
- **DEAD**: Entity marked for removal

## Physics Engine

### PhysicsEngine
Interface defining the contract for physics engines. Responsibilities:
- Calculate new physics values based on elapsed time
- Manage bounces at world boundaries
- Update thrust and angular acceleration values

### BasicPhysicsEngine
Concrete implementation of `PhysicsEngine` that applies basic physics:
- MRUA integration (Uniformly Accelerated Rectilinear Motion)
- Velocity calculation: v₁ = v₀ + a·Δt
- Position calculation: x₁ = x₀ + v_avg·Δt
- Thrust application according to rotation angle
- Friction and elasticity management in bounces

### NullPhysicsEngine
"Null" physics engine used by `StaticBody`. Performs no physics calculations, maintaining constant values.

### AbstractPhysicsEngine
Abstract base class providing common implementation for physics engines, including physics values management and bounces.

### PhysicsValues
Immutable object encapsulating the complete physical state of an entity at a specific moment:
- **timeStamp**: Timestamp in nanoseconds
- **posX, posY**: Position in 2D space
- **speedX, speedY**: Velocity (x, y components)
- **accX, accY**: Acceleration (x, y components)
- **angle**: Rotation angle in degrees
- **angularSpeed**: Angular velocity (degrees/second)
- **angularAcc**: Angular acceleration (degrees/second²)
- **thrust**: Applied thrust force

## Events and Actions System

### ActionDTO (Data Transfer Object)
Data transfer object encapsulating an action to execute:
- **type**: Action type (`ActionType`)
- **executor**: Executor that will process the action (`ActionExecutor`)
- **priority**: Execution priority (`ActionPriority`)

### EventDTO (Data Transfer Object)
Object representing an event in the simulation:
- **entity**: Entity generating the event
- **eventType**: Event type (`EventType`)

### ActionType
Enumeration of possible action types in the system (e.g., move, rotate, fire).

### EventType
Enumeration of event types that can occur in the simulation (e.g., collision, out of bounds).

### ActionExecutor
Interface defining objects capable of executing actions on the model.

### ActionPriority
Enumeration defining priority levels for action execution.

## System States

### ModelState
Enumeration defining Model lifecycle states:
- **STARTING**: Initializing
- **ALIVE**: Running simulation
- **STOPPED**: Stopped

### EngineState
Enumeration defining complete engine states (Controller).

## World and Generation

### WorldDefinition
Object defining the complete configuration of a world:
- World dimensions (`worldWidth`, `worldHeight`)
- Visual asset catalog (`AssetCatalog`)
- Background, decorator, and gravitational body definitions
- Asteroid, spaceship, and weapon configurations

### WorldGenerator
Class responsible for generating the initial world based on a `WorldDefinition`. Creates and places all initial entities.

### LifeGenerator
Automatic generator of dynamic entities that maintains activity in the simulation, creating new entities when necessary.

## Weapon System

### Weapon
Interface/base class for weapon systems that can fire projectiles.

### BasicWeapon
Basic weapon implementation with:
- Projectile configuration (`WeaponDto`)
- Fire rate control
- Fire request system

### WeaponDto
Weapon configuration object defining projectile properties and weapon behavior.

## Assets and Rendering

### AssetCatalog
Catalog that organizes and manages all visual resources (sprites, images) used in the game.

### AssetType
Enumeration of asset types (backgrounds, solid_bodies, space_decors, spaceship, weapons, etc.).

### EntityInfoDTO
Data transfer object containing information for rendering a static entity:
- Entity ID
- Asset ID
- Size
- Position (x, y)
- Rotation angle

### DBodyInfoDTO
Extension of `EntityInfoDTO` for dynamic entities, adding:
- Timestamp
- Velocity (speedX, speedY)
- Acceleration (accX, accY)

### Renderer
Component handling the rendering loop, drawing the current game state on screen using snapshots provided by the Controller.

## Utilities

### DoubleVector
Utility class for 2D vector mathematics, providing common vector operations.

### Images
Image loading and caching system for efficient graphic resource management.

### Fx (Effects)
Visual effects system for animations and particles.

## Physics Concepts

### Thrust
Force applied to an entity in the direction of its current angle. Used to propel spaceships and other dynamic objects.

### Angular Velocity
Rotation speed of an entity, measured in degrees per second.

### Angular Acceleration
Change in angular velocity per unit of time, measured in degrees per second squared.

### Elasticity
Property determining how much kinetic energy is conserved during a collision or bounce (value between 0 and 1).

### Friction
Force opposing movement, gradually reducing the velocity of entities.

## Concurrent Programming

### Thread-Safe Collections
Thread-safe collections (such as `ConcurrentHashMap`) used to manage entities in a multithreaded environment.

### Volatile Variables
Variables marked as `volatile` to guarantee visibility between threads (e.g., `ModelState`, `EntityState`).

### Immutable Objects
Immutable objects like `PhysicsValues` that guarantee concurrency safety by not allowing modification after creation.

---

## Execution Flow

1. **Initialization**: `Main` creates Controller, Model, and View
2. **Asset Loading**: Controller loads visual resources into View
3. **World Generation**: WorldGenerator creates initial entities
4. **Activation**: Model and View activate, starting their execution loops
5. **Simulation Loop**: DynamicBody entities calculate physics in separate threads
6. **Rendering Loop**: View requests snapshots and renders current state
7. **Input Processing**: Controller translates keyboard input into Model actions
8. **Event Processing**: Model manages events (collisions, bounces) and executes actions

This glossary provides a solid foundation for understanding the architecture and fundamental concepts of the Balls project.
