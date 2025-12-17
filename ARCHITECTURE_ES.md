# Balls - Documentación de Arquitectura

**[English](ARCHITECTURE.md)** | **Español**

Este documento proporciona documentación arquitectónica detallada para las clases principales del motor de juego Balls. Estas descripciones están extraídas de los comentarios de encabezado en el código fuente y sirven como referencia para entender los patrones de diseño, modelos de hilos y responsabilidades de cada componente.

## Tabla de Contenidos

1. [Componentes MVC Principales](#componentes-mvc-principales)
   - [Controller](#controller)
   - [Model](#model)
   - [View](#view)
   - [Renderer](#renderer)
2. [Sistema de Entidades](#sistema-de-entidades)
   - [DynamicBody](#dynamicbody)
   - [StaticBody](#staticbody)
3. [Sistema de Armas](#sistema-de-armas)
   - [AbstractWeapon](#abstractweapon)

---

## Controller

**Fuente:** `src/controller/Controller.java`

Controller
----------

Coordinador central de la tríada MVC:
  - Posee referencias a Model y View.
  - Realiza el cableado de inicio del motor (assets, definición del mundo, dimensiones, límites).
  - Convierte entrada del usuario (View) en comandos del Model.
  - Proporciona getters de instantáneas usados por el Renderer (a través de la View).

Responsabilidades (alto nivel)
------------------------------

1) Secuencia de arranque / activación
   - Valida que todas las dependencias requeridas estén presentes (assets, mundo,
     dimensiones, máximo de cuerpos, model, view).
   - Carga recursos visuales en la View (View.loadAssets).
   - Configura la View e inicia el bucle del Renderer (View.activate).
   - Configura el Model (dimensión, máximo de cuerpos) e inicia la simulación
     (Model.activate).
   - Cambia el estado del controller a ALIVE cuando todo está listo.

2) Construcción del mundo / creación de entidades
   - addDBody / addSBody / addDecorator / addPlayer delegan la creación de entidades
     al Model.
   - Importante: los cuerpos estáticos y decoradores son "actualizados por empuje" a la View:
     después de agregar una entidad estática/decoradora, el controller obtiene una
     instantánea fresca estática/decoradora del Model y la empuja a la View
     (View.updateSBodyInfo / View.updateDecoratorsInfo). Esto coincide con el diseño
     donde las visuales estáticas/decoradoras usualmente no cambian cada fotograma, por lo que
     evitas actualizaciones innecesarias por fotograma.

3) Despacho de comandos en tiempo de ejecución
   - Expone comandos de alto nivel del jugador que la View llama en respuesta a la entrada:
     playerThrustOn / playerThrustOff / playerReverseThrust
     playerRotateLeftOn / playerRotateRightOn / playerRotateOff
     playerFire
   - Todos estos son simples delegaciones al Model, manteniendo la View libre
     de lógica de simulación.

4) Acceso a instantáneas para renderizado
   - getDBodyInfo(): devuelve datos de instantánea dinámica del Model. Esto está
     destinado a ser extraído frecuentemente (típicamente una vez por fotograma por el
     hilo del Renderer).
   - getSBodyInfo() / getDecoratorInfo(): usado para empujar instantáneas cuando
     el contenido estático/decorador cambia.

5) Reglas de juego / capa de decisión (acciones basadas en reglas)
   - decideActions(entity, events) toma eventos del Model (EventDTO) y produce
     una lista de acciones (ActionDTO).
   - applyGameRules(...) mapea eventos -> acciones:
     * Límite del mundo alcanzado => DIE (alta prioridad)
     * MUST_FIRE => FIRE (alta prioridad)
     * COLLIDED / NONE => sin acción adicional
   - Si no hay una acción "similar a muerte" presente, MOVE se agrega por defecto.
     Esto crea una línea base determinística: las entidades siempre se mueven a menos que
     sean explícitamente eliminadas/explotadas.

Estado del motor
---------------
engineState es volátil y representa la vista del Controller del ciclo de vida
del motor:
  - STARTING: estado inicial después de la construcción
  - ALIVE: establecido después de que activate() termina exitosamente
  - PAUSED: establecido vía enginePause()
  - STOPPED: establecido vía engineStop()

Reglas de inyección de dependencias
-----------------------------------
  - setModel(model): almacena el model e inyecta el controller de vuelta en
    el model (model.setController(this)). Esto habilita callbacks / decisiones de reglas
    si el Model consulta al Controller.
  - setView(view): almacena la view e inyecta el controller en la view
    (view.setController(this)). Esto habilita a la View para enviar comandos del jugador
    y extraer instantáneas.

Notas de hilos
-------------
  - El Controller en sí actúa principalmente como una fachada. El punto clave de concurrencia
    es el acceso a instantáneas: el hilo del Renderer extrae getDBodyInfo() frecuentemente.
    Las instantáneas estáticas/decoradoras se empujan ocasionalmente desde el "lado lógico"
    (model->controller->view).
  - Mantener los métodos del Controller pequeños y con efectos secundarios ligeros reduce la contención
    y hace más fácil razonar sobre dónde ocurren las interacciones entre hilos.

---

## Model

**Fuente:** `src/model/Model.java`

Model
-----

Capa de simulación central de la tríada MVC. El Model posee y gestiona todas
las entidades (cuerpos dinámicos, cuerpos estáticos, jugadores, decoradores) y orquesta
su ciclo de vida, actualizaciones de física e interacciones.

Responsabilidades
----------------
  - Gestión de entidades: crear, activar y rastrear todas las entidades de simulación
  - Proporcionar datos de instantánea seguros para hilos (EntityInfoDTO / DBodyInfoDTO) al
    Controller para renderizado
  - Delegar actualizaciones de física a hilos individuales de entidad
  - Mantener colecciones de entidades con estrategias de concurrencia apropiadas
  - Hacer cumplir límites del mundo y límites de entidades

Tipos de entidades
-----------------
El Model gestiona varias categorías distintas de entidades:

1) Cuerpos Dinámicos (dBodies)
   - Entidades con simulación de física activa (naves, asteroides, proyectiles)
   - Cada uno se ejecuta en su propio hilo, actualizando continuamente posición/velocidad
   - Almacenados en ConcurrentHashMap para acceso seguro entre hilos

2) Cuerpos de Jugador (pBodies)
   - Cuerpos dinámicos especiales con controles de jugador y armas
   - Indexados por cadena de ID de jugador
   - Soportan comandos de empuje, rotación y disparo

3) Cuerpos Estáticos (sBodies)
   - Entidades no móviles con posiciones fijas (obstáculos, plataformas)
   - Sin hilo de física
   - Actualizados por empuje a la View cuando se crean/modifican

4) Cuerpos de Gravedad (gravityBodies)
   - Cuerpos estáticos que ejercen influencia gravitacional
   - Usados para cuerpos planetarios o agujeros negros

5) Decoradores (decorators)
   - Entidades solo visuales sin impacto en el juego (elementos de fondo)
   - Actualizados por empuje a la View cuando se crean/modifican

Ciclo de vida
------------
Construcción:
  - Model se crea en estado STARTING
  - Los mapas de entidades se pre-asignan con capacidades esperadas

Activación (activate()):
  - Valida que Controller, dimensiones del mundo y máximo de entidades estén establecidos
  - Transiciona a estado ALIVE
  - Después de la activación, las entidades pueden ser creadas y activadas

Generación de instantáneas
--------------------------
El Model proporciona métodos de instantánea que devuelven DTOs inmutables:
  - getDBodyInfo(): devuelve List<DBodyInfoDTO> para todos los cuerpos dinámicos activos
  - getSBodyInfo(): devuelve List<EntityInfoDTO> para todos los cuerpos estáticos activos
  - getDecoratorInfo(): devuelve List<EntityInfoDTO> para todos los decoradores

Estas instantáneas son extraídas por el Controller y empujadas a la View/Renderer.
El patrón asegura una separación limpia: el renderizado nunca accede al estado de
entidad mutable directamente.

Estrategia de concurrencia
--------------------------
  - Todos los mapas de entidades usan ConcurrentHashMap para acceso seguro entre hilos
  - Las entidades individuales gestionan su propia sincronización de hilos
  - Las transiciones de estado del Model están protegidas por campos volátiles
  - Los métodos de instantánea crean listas DTO independientes para evitar
    modificación concurrente durante el renderizado

Objetivos de diseño
-------------------
  - Mantener la lógica de simulación aislada de las preocupaciones de la view
  - Proporcionar gestión de entidades determinística y segura para hilos
  - Soportar altos conteos de entidades (hasta MAX_ENTITIES = 5000)
  - Habilitar actualizaciones de física paralelas eficientes vía hilos por entidad

---

## View

**Fuente:** `src/view/View.java`

View
----

Ventana de nivel superior Swing que representa la capa de presentación del motor.
Esta clase conecta:
  - La superficie de renderizado (Renderer)
  - Carga de assets y catálogos de imágenes (Images)
  - Entrada del usuario (KeyListener) y despacho de comandos al Controller

Rol arquitectónico
------------------
View es una fachada delgada sobre renderizado + entrada:
  - No simula nada.
  - No posee estado del mundo.
  - Se comunica con el model exclusivamente a través del Controller.

El Renderer extrae instantáneas dinámicas cada fotograma (vía View -> Controller),
mientras que las instantáneas estáticas/decoradoras se empujan a la View/Renderer solo cuando
cambian (para evitar actualizaciones redundantes por fotograma para entidades que no se
mueven).

Ciclo de vida
------------
Construcción:
  - Crea el ControlPanel (controles de UI, si los hay).
  - Crea el Renderer (Canvas).
  - Construye el layout del JFrame y adjunta el key listener.

Activación (activate()):
  - Valida dependencias obligatorias (dimensiones, fondo, catálogos de imágenes).
  - Inyecta dimensiones de la view e imágenes en el Renderer.
  - Inicia el hilo del Renderer (bucle de renderizado activo).

Gestión de assets
----------------
loadAssets(...) carga y registra todos los recursos visuales requeridos por el mundo:
  - Imagen de fondo (single BufferedImage).
  - Sprites de cuerpos dinámicos (naves, asteroides, misiles, etc.).
  - Sprites de cuerpos estáticos (cuerpos de gravedad, bombas, etc.).
  - Sprites de decoradores (parallax / decoración espacial).

La View almacena catálogos como colecciones Images, que luego se convierten
en cachés GPU/compatibles dentro del Renderer (ImageCache).

Delegación de estado del motor
------------------------------
View expone getEngineState() como un puente de conveniencia para el Renderer.
El bucle de renderizado puede detenerse o pausarse basado en el estado del motor propiedad del Controller.

Manejo de entrada
----------------
La entrada del teclado se captura a nivel del Canvas de renderizado (Renderer es
enfocable y recibe el KeyListener) y se traduce en comandos de alto nivel
del Controller:
  - Empuje on/off (adelante usa empuje positivo; empuje inverso se maneja
    como empuje negativo, y ambos se detienen vía el mismo comando thrustOff).
  - Rotación izquierda/derecha y rotación off.
  - Fire: manejado como una acción activada por borde usando fireKeyDown para prevenir
    que la repetición de tecla genere disparos continuos mientras se mantiene presionada ESPACIO.

Enfoque y consideraciones de Swing
----------------------------------
El Renderer es el propietario del enfoque para entrada. El enfoque se solicita después de que el marco
se vuelve visible usando SwingUtilities.invokeLater(...) para mejorar la confiabilidad
con el timing de despacho de eventos de Swing.

Consideraciones de hilos
------------------------
Swing es de un solo hilo (EDT), mientras que el renderizado se ejecuta en su propio hilo.
Esta clase mantiene sus responsabilidades mínimas:
  - Solo empuja actualizaciones estáticas/decoradoras cuando es necesario.
  - La extracción de instantáneas dinámicas se hace dentro del hilo del Renderer a través de
    getters View -> Controller.

Objetivos de diseño
-------------------
  - Mantener la View como coordinador, no como contenedor de estado.
  - Mantener el renderizado independiente y en tiempo real (renderizado activo).
  - Traducir la entrada del usuario en comandos del controller de forma limpia y predecible.

---

## Renderer

**Fuente:** `src/view/Renderer.java`

Renderer
--------

Bucle de renderizado activo responsable de dibujar el fotograma actual en la
pantalla. Esta clase posee el hilo de renderizado y realiza todo el dibujo usando
un buffer trasero basado en BufferStrategy.

Rol arquitectónico
------------------
El Renderer es un consumidor basado en extracción de instantáneas visuales proporcionadas por la View.
Nunca consulta o muta el model directamente.

El renderizado está desacoplado de la simulación a través de DTOs de instantánea inmutables
(EntityInfoDTO / DBodyInfoDTO), asegurando que el renderizado permanezca determinístico
y libre de condiciones de carrera del lado del model.

Modelo de hilos
--------------
  - Un hilo de renderizado dedicado impulsa el bucle de renderizado (Runnable).
  - El renderizado está activo solo mientras el estado del motor es ALIVE.
  - El bucle termina limpiamente cuando el motor alcanza STOPPED.

Patrones de acceso a datos
--------------------------
Se usan tres colecciones renderizables diferentes, cada una con una estrategia de concurrencia
conscientemente elegida basada en la frecuencia de actualización y propiedad del hilo:

1) Cuerpos dinámicos (DBodies)
   - Almacenados en un HashMap simple.
   - Actualizados y renderizados exclusivamente por el hilo de renderizado.
   - Sin acceso concurrente → no se requiere sincronización.

2) Cuerpos estáticos (SBodies)
   - Raramente actualizados, potencialmente desde hilos no de renderizado
     (model → controller → view).
   - Almacenados usando una estrategia de copiar-al-escribir:
     * Las actualizaciones crean una nueva instancia de Map.
     * La referencia se intercambia atómicamente vía un campo volátil.
   - El hilo de renderizado solo lee instantáneas estables.

3) Decoradores
   - Mismo patrón de acceso que los cuerpos estáticos.
   - Usa la misma estrategia de copiar-al-escribir + intercambio atómico.

Este diseño evita bloqueos, minimiza la contención y garantiza que el
hilo de renderizado siempre itera sobre una instantánea completamente consistente.

Seguimiento de fotogramas
-------------------------
Se usa un contador de fotogramas monotónicamente creciente (currentFrame) para:
  - Rastrear la vivacidad de renderizables.
  - Eliminar renderizables obsoletos de forma determinística.

Cada método de actualización captura una instantánea de fotograma local para asegurar
consistencia interna, incluso si el contador de fotograma global avanza más tarde.

Pipeline de renderizado
-----------------------
Por fotograma:
  1) El fondo se renderiza a un VolatileImage para blitting rápido.
  2) Los decoradores se dibujan.
  3) Los cuerpos estáticos se dibujan.
  4) Los cuerpos dinámicos se actualizan y se dibujan.
  5) Los elementos HUD (FPS) se renderizan al final.

Se usa composición alfa para separar el renderizado de fondo opaco de
entidades transparentes.

Consideraciones de rendimiento
------------------------------
  - Triple buffering vía BufferStrategy.
  - VolatileImage usado para caché de fondo.
  - Tasa de fotogramas objetivo ~60 FPS (16 ms de retraso).
  - FPS se mide usando una ventana móvil de un segundo.

Objetivos de diseño
-------------------
  - Renderizado determinístico.
  - Cero bloqueo en el bucle de renderizado.
  - Propiedad clara de estado mutable.
  - Decisiones de concurrencia explícitas y documentadas.

Esta clase está diseñada para comportarse como un componente de renderizado de bajo nivel adecuado
para un motor de juego pequeño en lugar de un renderizador Swing centrado en UI.

---

## DynamicBody

**Fuente:** `src/model/entities/DynamicBody.java`

DynamicBody
-----------

Representa una sola entidad dinámica en el modelo de simulación.

Cada DynamicBody mantiene:
  - Un identificador único y atributos visuales (assetId, size)
  - Su propia instancia de PhysicsEngine, que almacena y actualiza la
    instantánea inmutable de PhysicsValues (posición, velocidad, aceleración, ángulo, etc.)
  - Un hilo dedicado responsable de avanzar su estado de física a lo largo del tiempo

Los cuerpos dinámicos interactúan exclusivamente con el Model, reportando actualizaciones de física
y solicitando procesamiento de eventos (colisiones, rebotes, etc.). La capa de view
nunca lee el estado mutable directamente; en su lugar, DynamicBody produce una
instantánea DBodyInfoDTO encapsulando todos los datos visuales y físicos requeridos
para el renderizado.

El control del ciclo de vida (STARTING → ALIVE → DEAD) se gestiona internamente, y los
contadores estáticos (heredados de AbstractEntity) rastrean cantidades globales de entidades creadas,
activas y muertas.

Modelo de hilos
--------------
Cada DynamicBody se ejecuta en su propio hilo (implementa Runnable). El motor de
física se actualiza continuamente en el bucle run(), con la entidad verificando
eventos y procesando acciones basadas en reglas de juego determinadas por el
Controller.

El objetivo de esta clase es aislar el comportamiento por objeto y la evolución de física
mientras se mantiene la simulación segura para hilos a través de instantáneas inmutables y un
pipeline de renderizado claramente separado.

---

## StaticBody

**Fuente:** `src/model/entities/StaticBody.java`

StaticBody
----------

Representa una sola entidad estática en el modelo de simulación.

Cada StaticBody mantiene:
  - Un identificador único y atributos visuales (assetId, size)
  - Una instancia de NullPhysicsEngine con posición y ángulo fijos
  - Sin hilo dedicado (los cuerpos estáticos no se mueven ni actualizan)

Los cuerpos estáticos se usan para elementos del mundo no móviles como obstáculos,
plataformas o elementos decorativos que tienen presencia física pero sin
comportamiento dinámico.

La capa de view accede a los cuerpos estáticos a través de instantáneas EntityInfoDTO,
siguiendo el mismo patrón que los cuerpos dinámicos pero sin los datos de
física que varían con el tiempo.

El control del ciclo de vida (STARTING → ALIVE → DEAD) se gestiona internamente, y los
contadores estáticos (heredados de AbstractEntity) rastrean cantidades globales de entidades creadas,
activas y muertas.

Estático vs. Dinámico
--------------------
A diferencia de DynamicBody, StaticBody:
  - Usa NullPhysicsEngine (sin actualizaciones de física)
  - No tiene hilo (sin bucle run())
  - Devuelve EntityInfoDTO en lugar de DBodyInfoDTO (sin velocidad/aceleración)
  - Está destinado para elementos del mundo de posición fija

Esta separación mantiene la base de código limpia y previene sobrecarga innecesaria
para entidades que nunca se mueven.

---

## AbstractWeapon

**Fuente:** `src/model/weapons/AbstractWeapon.java`

AbstractWeapon
--------------

Clase base para todas las implementaciones de armas en la simulación.

Esta clase proporciona:
  - Identificación inmutable (weapon id)
  - Un objeto de configuración estático (WeaponDto)
  - Un mecanismo de solicitud de disparo monotónico y seguro para hilos
  - Una API unificada para actualizaciones de armas de tick discreto

MODELO DE CONCURRENCIA
----------------------
  - El método registerFireRequest() puede ser invocado desde *cualquier hilo*
    (típicamente desde el Controller o Model en respuesta a entrada del jugador).
    Almacena una marca de tiempo (System.nanoTime) en un AtomicLong.

  - El arma es un *componente pasivo*: no posee hilo y no realiza
    trabajo asíncrono. Toda la lógica de disparo debe ocurrir cuando update(dtSeconds) es
    ejecutada por el hilo de simulación del PlayerBody.

MODELO DE SOLICITUD DE DISPARO
------------------------------
  - lastFireRequest contiene la marca de tiempo de la solicitud de disparo *más reciente*.
  - lastHandledRequest contiene la marca de tiempo de la solicitud más reciente ya
    procesada (disparo producido o conscientemente ignorado).

  - Existe una nueva solicitud si:
      lastFireRequest > lastHandledRequest

  - Llamar a hasNewRequest() consume la solicitud avanzando
    lastHandledRequest a lastFireRequest.

  - No se realiza buffering o encolamiento de solicitudes. Solo la intención de disparo más reciente
    importa. Las solicitudes que ocurren durante el enfriamiento o secuencias de disparo en curso
    se consumen y descartan. Esto produce un comportamiento simple y predecible
    alineado con la simulación de tick discreto.

FILOSOFÍA DE DISEÑO
------------------
  - Las armas no ejecutan código fuera de update(): determinístico, seguro para hilos,
    y fácil de razonar en una simulación fuertemente multi-hilo.

  - La intención de disparo es monotónica y activada por borde. El sistema nunca almacena
    múltiples disparos pendientes; las armas deciden inmediatamente si disparar o
    ignorar una solicitud.

  - AbstractWeapon no impone ningún comportamiento de disparo específico; solo estandariza
    las reglas de consumo de solicitudes. Las armas concretas (BasicWeapon, BurstWeapon,
    MissileWeapon, etc.) implementan toda la lógica de disparo real.

DIRECTRICES PARA NUEVAS IMPLEMENTACIONES DE ARMAS
-------------------------------------------------
Si estás implementando un nuevo tipo de arma, sigue estos principios:

1. **Nunca bloquees el hilo update().** Toda la lógica debe ser rápida y estrictamente
   local a un tick.

2. **Usa hasNewRequest() solo para detectar y consumir intención.** No intentes
   re-interpretar o almacenar solicitudes pasadas.

3. **Devuelve como máximo un disparo por tick.** Las armas de múltiples disparos o ráfaga deben
   distribuir disparos a través de ticks a menos que el juego soporte explícitamente
   emisión de múltiples proyectiles simultáneos.

4. **Gestiona tu propio timing interno.** Usa dtSeconds para reducir enfriamientos,
   temporizadores de ráfaga o ventanas de aceleración.

5. **No crees proyectiles dentro de update().** update() solo debe señalar
   intención (true/false). El Model o el llamador es responsable de instanciar
   proyectiles DynamicBody.

6. **Sé explícito sobre la política de manejo de solicitudes.** Decide si un arma
   ignora solicitudes durante el enfriamiento, colapsa múltiples solicitudes en una, o
   solo inicia ráfagas cuando está inactiva.

7. **Mantén el arma sin estado con respecto a hilos.** Sin hilos en segundo plano,
   sin temporizadores, sin sleeps; todo ocurre durante el ciclo de tick del PlayerBody.

Estas reglas aseguran que todas las armas se comporten de forma consistente dentro del motor,
permanezcan determinísticas y no introduzcan peligros de concurrencia.

---


## Contribuir

Al modificar cualquiera de estas clases, por favor asegúrate de que:

1. **Los comentarios de encabezado permanezcan sincronizados** con la implementación real
2. **Los patrones de diseño** descritos en los encabezados se sigan
3. **Las garantías de hilos** se mantengan
4. **Los modelos de concurrencia** se respeten

Para actualizar esta documentación, regenerala de los encabezados de la fuente o actualiza manualmente tanto los archivos fuente como este documento.

## Documentación Relacionada

Para definiciones detalladas de conceptos, términos técnicos y componentes del sistema, consulta:

**[GLOSSARY.md](GLOSSARY.md)** - Glosario completo en español

## Versión

Esta documentación fue generada a partir de los encabezados del código fuente. Última actualización: 2025-12-16

---

*Para detalles de implementación y ejemplos de código, consulta los archivos fuente directamente.*
