# CacheImage revisión y opinión

## Cómo funciona `ImageCache`
- Construye un `Map<CachedImageKeyDTO, BufferedImage>` inmutable en tiempo de inserción y lo usa como caché en memoria para cada combinación `(angle, assetId, size)`.
- Recibe un `GraphicsConfiguration` para crear imágenes compatibles con el hardware y un catálogo `Images` que aporta las texturas base por `assetId`.
- Si no encuentra el `assetId`, genera un círculo rojo como respaldo y siempre devuelve la imagen insertada en el mapa.

## Cómo se usa en el renderizado
- `Renderer` crea **tres instancias** de `ImageCache`, una por cada catálogo `Images` (dinámicos, estáticos y decoradores) en `setImages(...)` y las pasa a los `Renderable` correspondientes.
- Los `Renderable` (`EntityRenderable` y `DBodyRenderable`) siempre piden la imagen por `assetId`, tamaño y ángulo, delegando en la caché para reutilizar el `BufferedImage` compatible.

## Opinión final: ¿caché global o por tipo?
- La caché actual no tiene políticas de caducidad ni límites por tipo; funciona como un diccionario infinito basado en `assetId+size+angle`.
- Separar por tipo **solo aporta aislamiento lógico** y evitar que cada catálogo dependa de imágenes que no carga. No hay optimizaciones específicas por categoría ni presupuestos de memoria diferenciados.
- Si los `assetId` son globalmente únicos y estás dispuesto a unificar los catálogos `Images`, **una caché global reduciría duplicación** (tres mapas y tres configuraciones) y eliminaría la decisión de qué instancia usar.
- Mantener caches separadas solo tendría sentido si planeas: (a) políticas/budgets distintos por categoría, (b) precarga/validaciones independientes o (c) métricas diferenciadas. Hoy el código no usa nada de eso, así que el beneficio práctico es mínimo.
