package assets;


public class Assets {

    public final AssetCatalog backgrounds;
    public final AssetCatalog uiDecors;
    public final AssetCatalog spaceDecors;
    public final AssetCatalog overlayDecors;

    public final AssetCatalog gravityBodies;
    public final AssetCatalog solidBodies;   // asteroids, meteors, labs...
    public final AssetCatalog weapons;

    public final AssetCatalog spaceship;


    public Assets() {
        this.overlayDecors = new AssetCatalog("src/resources/images/overlay_decors");
        this.overlayDecors.register("cosmic_portal_1", "cosmic-portal-1.png", AssetType.COSMIC_PORTAL, AssetIntensity.HIGH);
        this.overlayDecors.register("cracks_1", "cracks-1.png", AssetType.CRACKS, AssetIntensity.HIGH);
        this.overlayDecors.register("cracks_2", "cracks-2.png", AssetType.CRACKS, AssetIntensity.HIGH);
        this.overlayDecors.register("cracks_3", "cracks-3.png", AssetType.CRACKS, AssetIntensity.HIGH);
        this.overlayDecors.register("cracks_4", "cracks-4.png", AssetType.CRACKS, AssetIntensity.HIGH);
        this.overlayDecors.register("cracks_5", "cracks-5.png", AssetType.CRACKS, AssetIntensity.HIGH);
        this.overlayDecors.register("halo_1", "halo-1.png", AssetType.HALO, AssetIntensity.HIGH);
        this.overlayDecors.register("halo_2", "halo-2.png", AssetType.HALO, AssetIntensity.HIGH);
        this.overlayDecors.register("halo_3", "halo-3.png", AssetType.HALO, AssetIntensity.HIGH);
        this.overlayDecors.register("halo_4", "halo-4.png", AssetType.HALO, AssetIntensity.HIGH);
        this.overlayDecors.register("halo_5", "halo-5.png", AssetType.HALO, AssetIntensity.HIGH);
        this.overlayDecors.register("halo_6", "halo-6.png", AssetType.HALO, AssetIntensity.HIGH);
        this.overlayDecors.register("halo_7", "halo-7.png", AssetType.HALO, AssetIntensity.HIGH);
        this.overlayDecors.register("halo_8", "halo-8.png", AssetType.HALO, AssetIntensity.HIGH);
        this.overlayDecors.register("halo_9", "halo-9.png", AssetType.HALO, AssetIntensity.HIGH);
        this.overlayDecors.register("halo_10", "halo-10.png", AssetType.HALO, AssetIntensity.HIGH);
        this.overlayDecors.register("halo_11", "halo-11.png", AssetType.HALO, AssetIntensity.HIGH);
        this.overlayDecors.register("halo_12", "halo-12.png", AssetType.HALO, AssetIntensity.HIGH);
        this.overlayDecors.register("halo_13", "halo-13.png", AssetType.HALO, AssetIntensity.HIGH);
        this.overlayDecors.register("light_1", "light-1.png", AssetType.LIGHT, AssetIntensity.HIGH);
        this.overlayDecors.register("light_2", "light-21.png", AssetType.LIGHT, AssetIntensity.HIGH);
        this.overlayDecors.register("shot_hole_1", "shot-hole-1.png", AssetType.SHOT_HOLE, AssetIntensity.HIGH);
        this.overlayDecors.register("shot_hole_2", "shot-hole-2.png", AssetType.SHOT_HOLE, AssetIntensity.HIGH);
        this.overlayDecors.register("shot_hole_3", "shot-hole-3.png", AssetType.SHOT_HOLE, AssetIntensity.HIGH);

        this.backgrounds = new AssetCatalog("src/resources/images/backgrounds/");
        this.backgrounds.register("back_1", "space-high-intensity-1.png", AssetType.SPACE_BACKGROUND, AssetIntensity.HIGH);
        this.backgrounds.register("back_2", "space-low-intensity-1.jpeg", AssetType.SPACE_BACKGROUND, AssetIntensity.LOW);
        this.backgrounds.register("back_3", "space-low-intensity-2.jpg", AssetType.SPACE_BACKGROUND, AssetIntensity.LOW);
        this.backgrounds.register("back_4", "space-medium-intensity-1.jpeg", AssetType.SPACE_BACKGROUND, AssetIntensity.MEDIUM);
        this.backgrounds.register("back_5", "space-medium-intensity-2.jpeg", AssetType.SPACE_BACKGROUND, AssetIntensity.MEDIUM);

        this.gravityBodies = new AssetCatalog("src/resources/images/gravity_bodies/");
        this.gravityBodies.register("black_hole_1", "black-hole-1.png", AssetType.BLACK_HOLE, AssetIntensity.HIGH);
        this.gravityBodies.register("black_hole_2", "black-hole-2.png", AssetType.BLACK_HOLE, AssetIntensity.HIGH);
        this.gravityBodies.register("moon_1", "moon-1.png", AssetType.MOON, AssetIntensity.HIGH);
        this.gravityBodies.register("moon_2", "moon-2.png", AssetType.MOON, AssetIntensity.HIGH);
        this.gravityBodies.register("planet_1", "planet-1.png", AssetType.PLANET, AssetIntensity.HIGH);
        this.gravityBodies.register("planet_2", "planet-2.png", AssetType.PLANET, AssetIntensity.HIGH);
        this.gravityBodies.register("planet_3", "planet-3.png", AssetType.PLANET, AssetIntensity.HIGH);
        this.gravityBodies.register("planet_4", "planet-4.png", AssetType.PLANET, AssetIntensity.HIGH);
        this.gravityBodies.register("planet_5", "planet-5.png", AssetType.PLANET, AssetIntensity.HIGH);
        this.gravityBodies.register("planet_6", "planet-6.png", AssetType.PLANET, AssetIntensity.HIGH);
        this.gravityBodies.register("sun_1", "sun-1.png", AssetType.SUN, AssetIntensity.HIGH);
        this.gravityBodies.register("sun_2", "sun-2.png", AssetType.SUN, AssetIntensity.HIGH);
        this.gravityBodies.register("sun_3", "sun-3.png", AssetType.SUN, AssetIntensity.HIGH);
        this.gravityBodies.register("sun_4", "sun-4.png", AssetType.SUN, AssetIntensity.HIGH);

        this.solidBodies = new AssetCatalog("src/resources/images/solid_bodies/");
        this.solidBodies.register("asteroid_1", "asteroid-1-mini.png", AssetType.ASTEROID, AssetIntensity.HIGH);
        this.solidBodies.register("asteroid_2", "asteroid-2-mini.png", AssetType.ASTEROID, AssetIntensity.HIGH);
        this.solidBodies.register("asteroid_3", "asteroid-3-mini.png", AssetType.ASTEROID, AssetIntensity.HIGH);
        this.solidBodies.register("asteroid_4", "asteroid-4-mini.png", AssetType.ASTEROID, AssetIntensity.HIGH);
        this.solidBodies.register("asteroid_5", "asteroid-5-mini.png", AssetType.ASTEROID, AssetIntensity.HIGH);
        this.solidBodies.register("asteroid_6", "asteroid-6-mini.png", AssetType.ASTEROID, AssetIntensity.HIGH);
        this.solidBodies.register("lab_1", "lab-1.png", AssetType.LAB, AssetIntensity.HIGH);
        this.solidBodies.register("lab_2", "lab-2.png", AssetType.LAB, AssetIntensity.HIGH);
        this.solidBodies.register("lab_3", "lab-3.png", AssetType.LAB, AssetIntensity.HIGH);
        this.solidBodies.register("lab_5", "lab-5.png", AssetType.LAB, AssetIntensity.HIGH);
        this.solidBodies.register("meteor_1", "meteor-1.png", AssetType.METEOR, AssetIntensity.HIGH);
        this.solidBodies.register("meteor_2", "meteor-2.png", AssetType.METEOR, AssetIntensity.HIGH);
        this.solidBodies.register("meteor_3", "meteor-3.png", AssetType.METEOR, AssetIntensity.HIGH);
        this.solidBodies.register("meteor_4", "meteor-4.png", AssetType.METEOR, AssetIntensity.HIGH);
        this.solidBodies.register("meteor_4", "meteor-4.png", AssetType.METEOR, AssetIntensity.HIGH);

        this.spaceDecors = new AssetCatalog("src/resources/images/space_decors/");
        this.spaceDecors.register("bubbles_1", "bubbles-1.png", AssetType.BUBBLES, AssetIntensity.HIGH);
        this.spaceDecors.register("bubbles_2", "bubbles-2.png", AssetType.BUBBLES, AssetIntensity.HIGH);
        this.spaceDecors.register("bubbles_3", "bubbles-3.png", AssetType.BUBBLES, AssetIntensity.HIGH);
        this.spaceDecors.register("bubbles_4", "bubbles-4.png", AssetType.BUBBLES, AssetIntensity.HIGH);
        this.spaceDecors.register("galaxy_1", "galaxy-1.png", AssetType.GALAXY, AssetIntensity.HIGH);
        this.spaceDecors.register("galaxy_2", "galaxy-2.png", AssetType.GALAXY, AssetIntensity.HIGH);
        this.spaceDecors.register("galaxy_3", "galaxy-3.png", AssetType.GALAXY, AssetIntensity.HIGH);
        this.spaceDecors.register("rainbow_1", "rainbow-1.png", AssetType.RAINBOW, AssetIntensity.HIGH);
        this.spaceDecors.register("stars_1", "stars-1.png", AssetType.STARS, AssetIntensity.HIGH);
        this.spaceDecors.register("stars_2", "stars-2.png", AssetType.STARS, AssetIntensity.HIGH);
        this.spaceDecors.register("stars_3", "stars-3.png", AssetType.STARS, AssetIntensity.HIGH);
        this.spaceDecors.register("stars_4", "stars-4.png", AssetType.STARS, AssetIntensity.HIGH);
        this.spaceDecors.register("stars_5", "stars-5.png", AssetType.STARS, AssetIntensity.HIGH);
        this.spaceDecors.register("stars_6", "stars-6.png", AssetType.STARS, AssetIntensity.HIGH);
        this.spaceDecors.register("stars_7", "stars-7.png", AssetType.STARS, AssetIntensity.HIGH);
        this.spaceDecors.register("stars_8", "stars-8.png", AssetType.STARS, AssetIntensity.HIGH);

        this.spaceship = new AssetCatalog("src/resources/images/spaceship/");
        this.spaceship.register("rocket_1", "rocket-1.png", AssetType.ROCKET, AssetIntensity.HIGH);
        this.spaceship.register("rocket_2", "rocket-2.png", AssetType.ROCKET, AssetIntensity.HIGH);
        this.spaceship.register("rocket_3", "rocket-3.png", AssetType.ROCKET, AssetIntensity.HIGH);
        this.spaceship.register("rocket_4", "rocket-4.png", AssetType.ROCKET, AssetIntensity.HIGH);
        this.spaceship.register("rocket_5", "rocket-5.png", AssetType.ROCKET, AssetIntensity.HIGH);
        this.spaceship.register("rocket_6", "rocket-6.png", AssetType.ROCKET, AssetIntensity.HIGH);
        this.spaceship.register("rocket_7", "rocket-7.png", AssetType.ROCKET, AssetIntensity.HIGH);
        this.spaceship.register("spaceship_1", "spaceship-1.png", AssetType.SPACESHIP, AssetIntensity.HIGH);
        this.spaceship.register("spaceship_2", "spaceship-2.png", AssetType.SPACESHIP, AssetIntensity.HIGH);
        this.spaceship.register("spaceship_3", "spaceship-3.png", AssetType.SPACESHIP, AssetIntensity.HIGH);
        this.spaceship.register("spaceship_4", "spaceship-4.png", AssetType.SPACESHIP, AssetIntensity.HIGH);
        this.spaceship.register("spaceship_5", "spaceship-5.png", AssetType.SPACESHIP, AssetIntensity.HIGH);
        this.spaceship.register("spaceship_6", "spaceship-6.png", AssetType.SPACESHIP, AssetIntensity.HIGH);
        this.spaceship.register("spaceship_7", "spaceship-7.png", AssetType.SPACESHIP, AssetIntensity.HIGH);

        this.uiDecors = new AssetCatalog("src/resources/images/ui_decors/");
        this.uiDecors.register("signs_1", "signs-1.png", AssetType.SIGN, AssetIntensity.HIGH);

        this.weapons = new AssetCatalog("src/resources/images/weapons/");
        this.weapons.register("bullet_1", "bullet-1.png", AssetType.BULLET, AssetIntensity.LOW);
        this.weapons.register("bomb_1", "grenade-1.png", AssetType.BOMB, AssetIntensity.MEDIUM);
        this.weapons.register("bomb_2", "grenade-2.png", AssetType.BOMB, AssetIntensity.MEDIUM);
        this.weapons.register("misil_1", "misil-1.png", AssetType.MISIL, AssetIntensity.HIGH);
        this.weapons.register("misil_2", "misil-2.png", AssetType.MISIL, AssetIntensity.HIGH);
        this.weapons.register("misil_3", "misil-3.png", AssetType.MISIL, AssetIntensity.HIGH);
        this.weapons.register("misil_4", "misil-4.png", AssetType.MISIL, AssetIntensity.HIGH);
        this.weapons.register("misil_5", "misil-5.png", AssetType.MISIL, AssetIntensity.HIGH);

    }
}
