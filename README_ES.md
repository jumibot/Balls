# MVCGameEngine

**[English](README.md)** | **Español**

Un proyecto educativo en Java que demuestra una arquitectura modular de motor de juegos 2D con simulación de física en tiempo real, diseñado para crear juegos estilo arcade como Asteroids, shoot 'em ups espaciales u otros juegos basados en física.

Este proyecto sirve como una plataforma de aprendizaje integral para comprender patrones de arquitectura de software, programación concurrente, fundamentos de motores de juego y principios de diseño orientado a objetos.

## Qué Hace el Motor

**MVCGameEngine** es un motor de juegos 2D flexible que incluye simulación de física en tiempo real, gestión de entidades y capacidades de renderizado. Aunque la implementación de ejemplo demuestra un escenario de disparos espaciales, el motor está diseñado para soportar varios tipos de juegos estilo arcade:

- **Desarrollo de Juegos Flexible**: Crea diferentes tipos de juegos arcade 2D (disparos espaciales como Asteroids, billar, u otros juegos basados en física)
- **Implementación de Ejemplo**: Incluye un ejemplo de disparos espaciales con asteroides, naves espaciales y proyectiles
- **Cuerpos Dinámicos**: Las entidades se mueven, rotan y colisionan según las reglas de física gobernadas por motores de física intercambiables
- **Interacción del Jugador**: Los usuarios pueden controlar entidades jugador con capacidades de empuje, rotación y disparo usando entradas de teclado
- **Múltiples Motores de Física**: Elige entre diferentes implementaciones de física incluyendo física básica y física nula para comportamientos de simulación variados
- **Generación de Escena**: Define mundos con fondos personalizables, cuerpos estáticos y elementos decorativos
- **Sistemas de Armas**: Framework de armas configurable que soporta diferentes tipos de proyectiles y comportamientos
- **Generación de Vida**: Sistema automático de generación de entidades para mantener la actividad del juego
- **Renderizado Visual**: Renderizado de gráficos en tiempo real usando Java Swing con gestión de recursos para sprites y efectos visuales

El motor se ejecuta continuamente con una arquitectura multihilo, separando el renderizado (Vista), la lógica del juego (Modelo) y el manejo de entrada del usuario (Controlador) para un rendimiento y mantenibilidad óptimos.

## Documentación

### Documentación de Arquitectura

Para documentación arquitectónica detallada de las clases principales, incluyendo patrones de diseño, modelos de threading y guías de implementación, consulte:

**[ARCHITECTURE_ES.md](docs/ARCHITECTURE_ES.md)** | **[ARCHITECTURE.md](docs/ARCHITECTURE.md)** - Documentación integral que cubre:
- Componentes MVC (Controller, Model, View, Renderer)
- Sistema de Cuerpos (AbstractBody, DynamicBody, StaticBody, PlayerBody, DecoBody)
- Sistema de Motores de Física (PhysicsEngine, BasicPhysicsEngine, NullPhysicsEngine)
- Sistema de Armas (AbstractWeapon y sus implementaciones)

### Glosario de Conceptos

Para definiciones de términos clave y conceptos utilizados en el proyecto:

**[GLOSSARY.md](docs/GLOSSARY.md)** | **[GLOSSARY_EN.md](docs/GLOSSARY_EN.md)** - Glosario completo que define:
- Términos de arquitectura MVC
- Sistema de cuerpos y tipos de entidades
- Conceptos de motores de física
- Sistema de armas
- Gestión de eventos y acciones
- Conceptos de threading y concurrencia

## Estructura del Proyecto

El código está organizado en paquetes bien definidos:

### Paquetes MVC Principales

- **`main`**: Punto de entrada de la aplicación
- **`model`**: Estado del juego y lógica de simulación
  - `model.bodies`: Cuerpos (DynamicBody, StaticBody, PlayerBody, DecoBody)
  - `model.physics`: Motores de física (BasicPhysicsEngine, NullPhysicsEngine)
  - `model.weapons`: Sistema de armas (BasicWeapon, BurstWeapon, MineLauncher, MissileLauncher)
- **`view`**: Capa de presentación (View, Renderer, ControlPanel)
- **`controller`**: Mediador coordinando Model y View

### Paquetes de Soporte

- **`assets`**: Gestión de recursos visuales
- **`world`**: Definición y configuración de mundos
- **`generators`**: Generadores de contenido procedural (SceneGenerator para escena estática, LifeGenerator para generación dinámica)
- **`fx`**: Sistema de efectos visuales
- **`_helpers`**: Clases de utilidad (DoubleVector, RandomArrayList)
- **`resources`**: Recursos estáticos (sprites, imágenes)

## Licencia

Este proyecto se publica bajo la licencia Creative Commons CC0 1.0 Universal, haciéndolo libremente disponible para uso educativo, modificación y distribución.
