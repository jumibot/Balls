# Característica de Barra de Progreso - Clase Hud

## Descripción General
La clase `Hud` ha sido extendida con un nuevo método `drawProgressBar` que permite mostrar barras de progreso visuales en el HUD del juego.

## Firma del Método

```java
public void drawProgressBar(Graphics2D g, int row, String label, double progress, int barWidth)
```

## Parámetros

- **`g`** (Graphics2D): El contexto gráfico donde dibujar
- **`row`** (int): El número de fila donde se debe dibujar la barra de progreso (sigue el mismo sistema de filas que las líneas de texto)
- **`label`** (String): La etiqueta de texto a mostrar antes de la barra de progreso
- **`progress`** (double): El valor del progreso, entre 0.0 (0%) y 1.0 (100%)
- **`barWidth`** (int): El ancho de la barra de progreso en píxeles (ej., 200)

## Características

1. **Progreso Codificado por Color**: La barra cambia automáticamente de color según el progreso:
   - **Rojo**: progreso < 33%
   - **Amarillo**: 33% ≤ progreso < 66%
   - **Verde**: progreso ≥ 66%

2. **Visualización de Porcentaje**: Muestra el porcentaje de progreso como texto junto a la barra

3. **Limitación Automática**: Los valores de progreso se limitan automáticamente al rango válido [0.0, 1.0]

4. **Estilo Consistente**: Se integra perfectamente con el sistema HUD existente (fuente, posicionamiento, colores)

## Ejemplos de Uso

### Uso Básico

```java
// Crear una instancia de HUD
Hud hud = new Hud(Color.GRAY, 10, 12, 35);
hud.maxLenLabel = 7; // Establecer ancho de etiqueta para alineación correcta

// En tu método de renderizado
public void render(Graphics2D g) {
    // Dibujar una barra de salud al 75% (verde)
    hud.drawProgressBar(g, 1, "Salud", 0.75, 200);
    
    // Dibujar una barra de energía al 50% (amarillo)
    hud.drawProgressBar(g, 2, "Energía", 0.50, 200);
    
    // Dibujar una barra de escudo al 25% (rojo)
    hud.drawProgressBar(g, 3, "Escudo", 0.25, 200);
}
```

### Usando la Clase de Ejemplo

La clase `ProgressBarHud` proporciona un ejemplo listo para usar:

```java
// Crear el HUD de barras de progreso
ProgressBarHud progressHud = new ProgressBarHud();

// En tu bucle de renderizado
public void render(Graphics2D g) {
    double health = 0.85;  // 85% de salud
    double energy = 0.40;  // 40% de energía
    double shield = 0.95;  // 95% de escudo
    
    progressHud.drawWithProgressBars(g, health, energy, shield);
}
```

### HUD Personalizado con Barras de Progreso

Puedes crear tu propia clase HUD extendiendo `Hud`:

```java
public class MiHudPersonalizado extends Hud {
    public MiHudPersonalizado() {
        super(Color.CYAN, 10, 12, 35);
        this.maxLenLabel = 10; // Ajustar según tu etiqueta más larga
    }
    
    public void dibujarEstadisticas(Graphics2D g, EstadisticasJugador stats) {
        // Dibujar múltiples barras de progreso con valores dinámicos
        drawProgressBar(g, 1, "HP", stats.getRatioSalud(), 250);
        drawProgressBar(g, 2, "MP", stats.getRatioMana(), 250);
        drawProgressBar(g, 3, "XP", stats.getRatioExperiencia(), 250);
        drawProgressBar(g, 4, "Resistencia", stats.getRatioResistencia(), 250);
    }
}
```

## Apariencia Visual

La barra de progreso consiste en:
- Un contorno gris oscuro
- Un relleno de fondo oscuro
- Un relleno de color representando el progreso actual
- Texto de porcentaje en blanco (ej., "75%") mostrado a la derecha de la barra

Ejemplo de visualización:
```
Salud    [████████████░░░░░░░░] 60%
Energía  [██████░░░░░░░░░░░░░░] 30%
Escudo   [████████████████████] 100%
```

## Notas

- La altura de la barra de progreso se calcula automáticamente según el tamaño de la fuente
- La barra se alinea verticalmente con la línea base del texto
- El estado gráfico (colores) se preserva y restaura automáticamente después de dibujar
- El método funciona con cualquier configuración de posicionamiento HUD existente
