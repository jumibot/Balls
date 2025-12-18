# Glossary - MVC Game Engine

This glossary provides definitions for key terms and concepts used throughout the MVC Game Engine project.

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
The data layer of the MVC pattern. Represents the game's state, business logic, and data structures. Manages game entities, world state, and rules independently of the user interface.

### **View**
The presentation layer of the MVC pattern. Responsible for rendering the game's visual representation to the screen. Displays the current state of the model without containing game logic.

### **Controller**
The input and logic coordinator of the MVC pattern. Processes user input, updates the model based on game rules, and determines which view updates are needed.

### **Separation of Concerns**
A design principle where different aspects of the application (data, presentation, logic) are kept separate to improve maintainability and testability.

### **Observer Pattern**
A design pattern where the View observes the Model for changes and updates automatically when the Model's state changes.

### **Data Binding**
The process of connecting the Model's data to the View's display, allowing automatic updates when data changes.

---

## Game Engine Terms

### **Entity**
A game object that exists within the game world. Can represent characters, items, obstacles, or any interactive element.

### **Component**
A modular piece of functionality that can be attached to entities to give them specific behaviors or properties (e.g., physics, rendering, input handling).

### **Scene**
A contained environment or level in the game. Manages a collection of entities and their interactions within a specific context.

### **Game Loop**
The main cycle that runs continuously while the game is active. Typically processes input, updates game state, and renders graphics in sequence.

### **Update Cycle**
The portion of the game loop where game logic is executed, entity states are updated, and game rules are applied.

### **Render Cycle**
The portion of the game loop where the current game state is drawn to the screen.

### **Delta Time (dt)**
The time elapsed since the last frame update. Used to ensure consistent game behavior regardless of frame rate.

### **Frame Rate (FPS)**
The number of frames (complete update and render cycles) displayed per second. Common targets are 30 or 60 FPS.

### **Game State**
The current condition of all game data, including entity positions, player scores, level progress, and system status.

### **State Machine**
A behavioral pattern that allows an entity or system to transition between different states (e.g., menu, playing, paused, game over).

### **Lifecycle**
The sequence of events an object goes through from creation (initialization) to destruction (cleanup).

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

## Audio Terms

### **Sound Effect (SFX)**
A short audio clip played in response to game events (e.g., jump sound, explosion).

### **Background Music (BGM)**
Longer audio tracks that play continuously in the background to set mood and atmosphere.

### **Audio Channel**
An independent audio output stream. Multiple channels allow simultaneous sound playback.

### **Volume**
The loudness level of an audio source.

### **Audio Manager**
A system component that handles loading, playing, and controlling all game audio.

### **Sample Rate**
The number of audio samples per second, determining audio quality.

### **Audio Buffer**
A temporary storage area for audio data before playback.

---

## Physics & Collision

### **Physics Engine**
A system that simulates physical interactions like gravity, velocity, acceleration, and collisions.

### **Collision Detection**
The process of determining when two or more entities intersect or touch.

### **Bounding Box**
A rectangular area surrounding an entity used for collision detection.

### **Hit Box**
The specific area of an entity that can participate in collisions, which may be smaller than the visual sprite.

### **Collision Response**
The action taken when a collision is detected (e.g., bounce, stop, trigger event).

### **Rigid Body**
An object that follows physics simulation rules for movement and collision.

### **Velocity**
The speed and direction of an entity's movement.

### **Acceleration**
The rate of change of velocity over time.

### **Gravity**
A constant downward force applied to entities with physics properties.

### **Friction**
Resistance that slows down moving objects.

### **Impulse**
A sudden force applied to an object, changing its velocity instantly.

### **Raycast**
A technique that projects an invisible line to detect what it intersects with, useful for line-of-sight and shooting mechanics.

---

## Additional Concepts

### **Prefab**
A pre-configured entity template that can be instantiated multiple times in the game.

### **Serialization**
The process of converting game state or objects into a format that can be saved to disk or transmitted.

### **Deserialization**
The process of reconstructing objects or game state from saved data.

### **Hot Reload**
The ability to update code or assets while the game is running without restarting.

### **Profiler**
A tool that measures performance metrics like frame rate, memory usage, and execution time of different systems.

### **Memory Leak**
A bug where allocated memory is not properly released, causing increasing memory consumption over time.

### **Garbage Collection**
An automatic memory management process that frees memory no longer in use.

### **Thread**
An independent sequence of execution within a program, allowing parallel processing.

### **Asynchronous Operation**
A task that runs independently without blocking the main program flow.

---

## Document Information

**Version:** 1.0  
**Last Updated:** 2025-12-18  
**Maintained By:** MVCGameEngine Team  

For additional information, please refer to the main documentation or visit the project repository.
