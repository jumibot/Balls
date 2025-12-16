# Balls

**[English](README.md)** | **Español**

Un proyecto educativo en Java que demuestra simulación de física 2D en tiempo real a través de un entorno dinámico de bolas rebotantes con elementos de juego interactivos.

Este proyecto sirve como una plataforma de aprendizaje integral para comprender patrones de arquitectura de software, programación concurrente, fundamentos de motores de juego y principios de diseño orientado a objetos.

## Qué Hace el Programa

**Balls** es una simulación de física 2D en tiempo real que presenta entidades dinámicas (bolas/asteroides) que interactúan dentro de un espacio mundial configurable. El programa crea un entorno animado donde:

- **Cuerpos Dinámicos**: Las entidades se mueven, rotan y colisionan según las reglas de física gobernadas por motores de física intercambiables
- **Interacción del Jugador**: Los usuarios pueden controlar una entidad jugador con capacidades de empuje, rotación y disparo usando entradas de teclado
- **Múltiples Motores de Física**: Elige entre diferentes implementaciones de física incluyendo física básica, física de giro y física nula para comportamientos de simulación variados
- **Generación de Mundos**: Mundos generados proceduralmente con fondos personalizables, cuerpos estáticos y elementos decorativos
- **Sistemas de Armas**: Dispara proyectiles con propiedades y comportamientos configurables
- **Generación de Vida**: Sistema automático de generación de entidades que mantiene la actividad en la simulación
- **Renderizado Visual**: Renderizado de gráficos en tiempo real usando Java Swing con gestión de assets para sprites y efectos visuales

La simulación se ejecuta continuamente con una arquitectura multihilo, separando el renderizado (Vista), la lógica del juego (Modelo) y el manejo de entrada del usuario (Controlador) para un rendimiento y mantenibilidad óptimos.

## Valor Educativo para Aprender Programación

Este proyecto sirve como un excelente recurso educativo para aprender conceptos fundamentales y avanzados de programación:

### Conceptos Fundamentales de Programación

1. **Programación Orientada a Objetos (POO)**
   - Jerarquías de herencia (AbstractEntity, DynamicBody, StaticBody, PlayerBody)
   - Polimorfismo a través de implementaciones de interfaces (PhysicsEngine, ActionExecutor)
   - Encapsulación del estado y comportamiento de las entidades
   - Clases abstractas e implementaciones concretas

2. **Patrones de Diseño**
   - **Model-View-Controller (MVC)**: Separación clara de responsabilidades con el Modelo manejando la simulación, la Vista gestionando el renderizado y el Controlador coordinando la comunicación
   - **Patrón Factory**: WorldDefinitionProvider para crear diferentes configuraciones de mundo
   - **Patrón Strategy**: Implementaciones intercambiables de PhysicsEngine
   - **Objetos de Transferencia de Datos (DTOs)**: ActionDTO, EventDTO, EntityInfoDTO para transferencia segura de datos entre capas

3. **Programación Concurrente**
   - Multihilo para ejecución paralela de bucles de renderizado y simulación
   - Estructuras de datos seguras para hilos (ConcurrentHashMap para gestión de entidades)
   - Variables volátiles para visibilidad entre hilos (ModelState, EngineState)
   - Sincronización entre hilo de renderizado y hilo de simulación

4. **Arquitectura de Software**
   - Diseño basado en UML con diagramas de clases definiendo la estructura antes de la implementación
   - Patrón de inyección de dependencias (cableado de Controller, Model, View)
   - Separación clara de responsabilidades entre paquetes (model, view, controller, assets, generators, world)
   - Arquitectura basada en eventos para comportamientos de entidades

5. **Estructuras de Datos y Algoritmos**
   - Gestión eficiente de entidades usando mapas hash para búsquedas O(1)
   - Sistema de ejecución de acciones basado en prioridades
   - Algoritmos de detección de colisiones
   - Cálculos espaciales para simulación de física

6. **Organización del Código**
   - Estructura de paquetes reflejando capas arquitectónicas
   - Convenciones claras de nomenclatura y documentación
   - Diseño modular permitiendo adición de características sin cambios en el núcleo

## Valor Educativo para Aprender Motores de Juego

Este proyecto proporciona experiencia práctica con conceptos fundamentales de motores de juego:

### Arquitectura de Motores de Juego

1. **Implementación del Bucle de Juego**
   - Consideraciones de paso de tiempo fijo vs. variable
   - Separación de lógica de actualización del renderizado
   - Gestión de tasa de fotogramas y optimización del rendimiento

2. **Sistema de Entidad-Componente**
   - Gestión del estado de entidades (enumeración EntityState)
   - Atributos de entidad basados en componentes (posición, velocidad, rotación, masa)
   - Gestión del ciclo de vida de entidades (creación, actualizaciones, destrucción)

3. **Simulación de Física**
   - **Múltiples Motores de Física**: Demuestra cómo diferentes implementaciones de física pueden intercambiarse
   - **Valores de Física**: Constantes de física centralizadas (fricción, elasticidad, gravedad)
   - **Cinemática**: Cálculos de posición, velocidad, aceleración
   - **Dinámica de Rotación**: Cálculos de velocidad angular y giro
   - **Manejo de Límites**: Detección y respuesta de colisiones con bordes del mundo

4. **Pipeline de Renderizado**
   - Separación del estado del juego de la representación visual
   - Renderizado basado en sprites con gestión de assets
   - Renderizado por capas (fondos, entidades, decoradores, UI)
   - Transformaciones del espacio de pantalla
   - Doble buffering para animación suave

5. **Manejo de Entrada**
   - Procesamiento de eventos de teclado (implementación KeyListener)
   - Patrón de comando para acciones del jugador (empuje, rotar, disparar)
   - Gestión del estado de entrada para acciones continuas vs. discretas

6. **Gestión de Assets**
   - Sistema de carga y caché de assets
   - Organización del catálogo de assets
   - Gestión del ciclo de vida de recursos
   - Soporte para diferentes tipos de assets (imágenes, sprites)

7. **Construcción de Mundos**
   - Generación procedural de mundos
   - Colocación de entidades estáticas y dinámicas
   - Sistemas de fondo y decoradores
   - Definiciones de mundo configurables

8. **Mecánicas de Juego**
   - Sistemas de armas con física de proyectiles
   - Generación y ciclo de vida de entidades (LifeGenerator)
   - Sistema de comportamiento basado en eventos
   - Lógica de juego basada en reglas (reglas de límites, reglas de colisión)

### Conceptos Avanzados

- **Máquinas de Estado**: ModelState y EngineState para gestión del ciclo de vida
- **Cámara/Viewport**: Conceptos de dimensión del mundo vs. dimensión de pantalla
- **Optimización del Rendimiento**: Búsquedas eficientes de entidades, asignación mínima de objetos en rutas críticas
- **Extensibilidad**: Clases base abstractas permitiendo nuevos tipos de entidades, motores de física y armas sin modificar código existente

## Aspectos Técnicos Destacados

- **Lenguaje**: Java (aprovechando características POO robustas)
- **Framework GUI**: Java Swing para renderizado multiplataforma
- **Arquitectura**: Separación estricta MVC
- **Concurrencia**: Arquitectura multihilo con colecciones seguras para hilos
- **Diseño**: Diagramas de clases UML guían la implementación
- **Escalabilidad**: Diseñado para soportar más de 1000 entidades dinámicas con límites configurables

## Estructura del Proyecto

La base de código está organizada en paquetes bien definidos, cada uno con responsabilidades claras:

### Paquetes MVC Principales

- **`main`**: Punto de entrada de la aplicación que inicia el motor, conecta dependencias e inicia la simulación
- **`model`**: Estado del juego y lógica de simulación (entidades, física, armas, eventos, acciones)
  - **`model.entities`**: Implementaciones de entidades (DynamicBody, StaticBody, PlayerBody, AbstractEntity, DecoEntity)
  - **`model.physics`**: Implementaciones de motor de física (BasicPhysicsEngine, SpinPhysicsEngine, NullPhysicsEngine, AbstractPhysicsEngine)
  - **`model.weapons`**: Implementaciones del sistema de armas (BasicWeapon, WeaponDto)
- **`view`**: Capa de presentación manejando renderizado y visualización (View, Renderer, ControlPanel)
  - **`view.renderables`**: Objetos de representación visual para entidades (DBodyRenderable, EntityRenderable, EntityInfoDTO)
- **`controller`**: Mediador que coordina Modelo y Vista, procesando entrada del usuario y gestionando el estado del motor

### Paquetes de Soporte

- **`assets`**: Sistema de gestión de assets para cargar y organizar recursos visuales (Assets, AssetCatalog, AssetInfo, AssetType)
- **`world`**: Definición y configuración de mundo (WorldDefinition, BackgroundDef, DynamicBodyDef, StaticBodyDef, DecoratorDef)
  - **`world.providers`**: Implementaciones factory para generar diferentes configuraciones de mundo (RandomWorldDefinitionProvider)
- **`generators`**: Generadores de contenido procedural (WorldGenerator para configuración inicial, LifeGenerator para generación dinámica de entidades)
- **`fx`**: Sistema de efectos visuales para animaciones y efectos de partículas (Fx, FxImage, Spin)
- **`_helpers`**: Clases de utilidad para operaciones comunes (DoubleVector para matemáticas vectoriales 2D, RandomArrayList)
- **`_images`**: Infraestructura de carga y caché de imágenes (Images, ImageCache, ImageDTO, CachedImageKeyDTO)
- **`resources`**: Recursos estáticos incluyendo imágenes de sprites organizadas por tipo (backgrounds, gravity_bodies, solid_bodies, space_decors, spaceship, ui_decors, weapons)

Esta estructura de paquetes sigue una separación arquitectónica clara, facilitando localizar funcionalidad y entender la organización del sistema.

## Documentación

Para documentación arquitectónica detallada de las clases principales, incluyendo patrones de diseño, modelos de hilos y directrices de implementación, consulta:

**[ARCHITECTURE.md](ARCHITECTURE.md)** - Documentación integral extraída de encabezados de código fuente cubriendo:
- Componentes MVC Principales (Controller, Model, View, Renderer)
- Sistema de Entidades (DynamicBody, StaticBody)
- Sistema de Armas (AbstractWeapon)

Esta documentación proporciona explicaciones profundas de estrategias de concurrencia, gestión del ciclo de vida y filosofías de diseño usadas en toda la base de código.

### Glosario

Para definiciones detalladas de conceptos, términos técnicos y componentes del sistema, consulta:

**[GLOSSARY.md](GLOSSARY.md)** - Glosario completo en español que cubre:
- Arquitectura y Patrones de Diseño
- Entidades (Entities)
- Motor de Física (Physics Engine)
- Sistema de Eventos y Acciones
- Estados del Sistema
- Mundo y Generación
- Sistema de Armas
- Assets y Renderizado
- Utilidades
- Conceptos de Física
- Programación Concurrente

También disponible en inglés: **[GLOSSARY_EN.md](GLOSSARY_EN.md)**

## Comenzar

Para ejecutar la simulación:

1. Compila todos los archivos fuente Java en el directorio `src`
2. Ejecuta la clase `Main` ubicada en `src/main/Main.java`
3. Usa los controles de teclado para interactuar con la entidad jugador
4. Observa la simulación de física y los comportamientos de las entidades

## Ruta de Aprendizaje

Para estudiantes y aprendices, recomendamos explorar la base de código en este orden:

1. **Comienza con la Arquitectura**: Examina `Main.java` para entender la secuencia de inicio
2. **Estudia los Componentes MVC**: Lee `Model.java`, `View.java` y `Controller.java` para entender la arquitectura
3. **Explora las Entidades**: Investiga las clases `DynamicBody`, `StaticBody` y `PlayerBody`
4. **Entiende la Física**: Compara diferentes implementaciones de `PhysicsEngine`
5. **Analiza el Multihilo**: Rastrea el flujo de ejecución entre bucle de renderizado y bucle de simulación
6. **Examina el Sistema de Eventos**: Estudia cómo `EventDTO` y `ActionDTO` habilitan comportamientos
7. **Revisa la Gestión de Assets**: Entiende el sistema de `Assets` y carga de assets

Este proyecto demuestra que los motores de juego no son magia—son sistemas de software bien estructurados construidos sobre fundamentos sólidos de programación. Al estudiar y modificar esta base de código, los aprendices obtienen experiencia práctica con prácticas profesionales de ingeniería de software mientras exploran el emocionante dominio del desarrollo de juegos.

## Licencia

Este proyecto se publica bajo la licencia Creative Commons CC0 1.0 Universal, haciéndolo libremente disponible para uso educativo, modificación y distribución.
