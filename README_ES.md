# MVCGameEngine

**[English](README.md)** | **Español**

Un proyecto educativo en Java que demuestra simulación de física 2D en tiempo real a través de un entorno dinámico de bolas rebotantes con elementos de juego interactivos.

Este proyecto sirve como una plataforma de aprendizaje integral para comprender patrones de arquitectura de software, programación concurrente, fundamentos de motores de juego y principios de diseño orientado a objetos.

## Qué Hace el Programa

**MVCGameEngine** es una simulación de física 2D en tiempo real que presenta entidades dinámicas (bolas/asteroides) que interactúan dentro de un espacio mundial configurable. El programa crea un entorno animado donde:

- **Cuerpos Dinámicos**: Las entidades se mueven, rotan y colisionan según las reglas de física gobernadas por motores de física intercambiables
- **Interacción del Jugador**: Los usuarios pueden controlar una entidad jugador con capacidades de empuje, rotación y disparo usando entradas de teclado
- **Múltiples Motores de Física**: Elige entre diferentes implementaciones de física incluyendo física básica y física nula para comportamientos de simulación variados
- **Generación de Mundos**: Mundos generados proceduralmente con fondos personalizables, cuerpos estáticos y elementos decorativos
- **Sistemas de Armas**: Dispara proyectiles con propiedades y comportamientos configurables
- **Generación de Vida**: Sistema automático de generación de entidades que mantiene la actividad en la simulación
- **Renderizado Visual**: Renderizado de gráficos en tiempo real usando Java Swing con gestión de recursos para sprites y efectos visuales

La simulación se ejecuta continuamente con una arquitectura multihilo, separando el renderizado (Vista), la lógica del juego (Modelo) y el manejo de entrada del usuario (Controlador) para un rendimiento y mantenibilidad óptimos.

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
- **`generators`**: Generadores de contenido procedural
- **`fx`**: Sistema de efectos visuales
- **`_helpers`**: Clases de utilidad (DoubleVector, RandomArrayList)
- **`resources`**: Recursos estáticos (sprites, imágenes)

## Licencia

Este proyecto se publica bajo la licencia Creative Commons CC0 1.0 Universal, haciéndolo libremente disponible para uso educativo, modificación y distribución.
