# Progress Bar Feature - Hud Class

## Overview
The `Hud` class has been extended with a new `drawProgressBar` method that allows you to display visual progress bars in the game's HUD.

## Method Signature

```java
public void drawProgressBar(Graphics2D g, int row, String label, double progress, int barWidth)
```

## Parameters

- **`g`** (Graphics2D): The graphics context to draw on
- **`row`** (int): The row number where the progress bar should be drawn (follows the same row system as text lines)
- **`label`** (String): The text label to display before the progress bar
- **`progress`** (double): The progress value, ranging from 0.0 (0%) to 1.0 (100%)
- **`barWidth`** (int): The width of the progress bar in pixels (e.g., 200)

## Features

1. **Color-coded Progress**: The bar automatically changes color based on progress:
   - **Red**: progress < 33%
   - **Yellow**: 33% ≤ progress < 66%
   - **Green**: progress ≥ 66%

2. **Percentage Display**: Shows the progress percentage as text next to the bar

3. **Automatic Clamping**: Progress values are automatically clamped to the valid range [0.0, 1.0]

4. **Consistent Styling**: Integrates seamlessly with the existing HUD system (font, positioning, colors)

## Usage Examples

### Basic Usage

```java
// Create a HUD instance
Hud hud = new Hud(Color.GRAY, 10, 12, 35);
hud.maxLenLabel = 7; // Set label width for proper alignment

// In your rendering method
public void render(Graphics2D g) {
    // Draw a health bar at 75% (green)
    hud.drawProgressBar(g, 1, "Health", 0.75, 200);
    
    // Draw an energy bar at 50% (yellow)
    hud.drawProgressBar(g, 2, "Energy", 0.50, 200);
    
    // Draw a shield bar at 25% (red)
    hud.drawProgressBar(g, 3, "Shield", 0.25, 200);
}
```

### Using the Example Class

The `ProgressBarHud` class provides a ready-to-use example:

```java
// Create the progress bar HUD
ProgressBarHud progressHud = new ProgressBarHud();

// In your rendering loop
public void render(Graphics2D g) {
    double health = 0.85;  // 85% health
    double energy = 0.40;  // 40% energy
    double shield = 0.95;  // 95% shield
    
    progressHud.drawWithProgressBars(g, health, energy, shield);
}
```

### Custom HUD with Progress Bars

You can create your own HUD class extending `Hud`:

```java
public class MyCustomHud extends Hud {
    public MyCustomHud() {
        super(Color.CYAN, 10, 12, 35);
        this.maxLenLabel = 10; // Adjust based on your longest label
    }
    
    public void drawStats(Graphics2D g, PlayerStats stats) {
        // Draw multiple progress bars with dynamic values
        drawProgressBar(g, 1, "HP", stats.getHealthRatio(), 250);
        drawProgressBar(g, 2, "MP", stats.getManaRatio(), 250);
        drawProgressBar(g, 3, "XP", stats.getExperienceRatio(), 250);
        drawProgressBar(g, 4, "Stamina", stats.getStaminaRatio(), 250);
    }
}
```

## Visual Appearance

The progress bar consists of:
- A dark gray outline
- A dark background fill
- A colored fill representing the current progress
- White percentage text (e.g., "75%") displayed to the right of the bar

Example visualization:
```
Health  [████████████░░░░░░░░] 60%
Energy  [██████░░░░░░░░░░░░░░] 30%
Shield  [████████████████████] 100%
```

## Notes

- The progress bar height is automatically calculated based on the font size
- The bar is vertically aligned with the text baseline
- Graphics state (colors) is automatically preserved and restored after drawing
- The method works with any existing HUD positioning configuration
