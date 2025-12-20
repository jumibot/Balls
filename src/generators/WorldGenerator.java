package generators;

import controller.Controller;
import java.util.ArrayList;
import java.util.Random;
import world.WorldDefItemDto;
import world.WorldDefPositionItemDto;
import world.WorldDefWeaponDto;
import world.WorldDefinition;

public class WorldGenerator {

    private final Random rnd = new Random();

    private final Controller controller;
    WorldDefinition worldDefinition;

    public WorldGenerator(Controller controller, WorldDefinition worldDef) {
        this.controller = controller;
        this.worldDefinition = worldDef;

        this.createWorld();
    }

    private void createWorld() {
        this.controller.loadAssets(this.worldDefinition.gameAssets);

        this.createSpaceDecorators(this.worldDefinition.spaceDecoratorsDef);
        this.createSBodies(this.worldDefinition.gravityBodiesDef);
        this.createPlayers(this.worldDefinition.spaceshipsDef);
    }

    private void createSBodies(ArrayList<WorldDefPositionItemDto> sBodies) {
        for (WorldDefPositionItemDto body : sBodies) {
            this.controller.addSBody(body.assetId, body.size, body.posX, body.posY, body.angle);
        }
    }

    private void createSpaceDecorators(ArrayList<WorldDefPositionItemDto> decorators) {

        for (WorldDefPositionItemDto deco : decorators) {
            this.controller.addDecorator(deco.assetId, deco.size, deco.posX, deco.posY, deco.angle);
        }
    }

    private void createPlayers(ArrayList<WorldDefItemDto> dBodies) {
        String playerId = null;

        for (WorldDefItemDto body : dBodies) {
            playerId = this.controller.addPlayer(
                    body.assetId, body.size, 500, 200, 0, 0, 0, 0, 0,
                    this.randomAngularSpeed(270), 0, 0);

            
            WorldDefWeaponDto gun = this.worldDefinition.gunsDef.get(0);
            this.controller.addWeaponToPlayer(
                    playerId, gun.assetId, gun.size,
                    gun.firingSpeed, gun.acc, gun.accTime,
                    0, gun.burstSize, gun.fireRate);

            WorldDefWeaponDto machineGun = this.worldDefinition.machineGunsDef.get(0);
            this.controller.addWeaponToPlayer(
                    playerId, machineGun.assetId, machineGun.size,
                    machineGun.firingSpeed, machineGun.acc, machineGun.accTime,
                    0, machineGun.burstSize, machineGun.fireRate);

            WorldDefWeaponDto missilLauncher = this.worldDefinition.missilLaunchersDef.get(0);
            this.controller.addWeaponToPlayer(
                    playerId, missilLauncher.assetId, missilLauncher.size,
                    missilLauncher.firingSpeed, missilLauncher.acc, missilLauncher.accTime,
                    -body.size * 0.5, missilLauncher.burstSize, missilLauncher.fireRate);

            WorldDefWeaponDto mineLauncher = this.worldDefinition.mineLaunchersDef.get(0);
            this.controller.addWeaponToPlayer(
                    playerId, mineLauncher.assetId, mineLauncher.size,
                    mineLauncher.firingSpeed, mineLauncher.acc, mineLauncher.accTime,
                    body.size * 0.5, mineLauncher.burstSize, mineLauncher.fireRate);
        }

        if (playerId != null) {
            this.controller.setLocalPlayer(playerId);
        }
    }

    private double randomAngularSpeed(double maxAngularSpeed) {
        return this.rnd.nextFloat() * maxAngularSpeed;
    }
}
