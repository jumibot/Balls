package generators;


import _helpers.DoubleVector;
import controller.Controller;
import java.util.ArrayList;
import java.util.Random;
import world.ItemDto;
import world.PositionItemDto;
import world.WeaponItemDto;
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
        this.createSBodies(this.worldDefinition.bombsDef);
        this.createPlayers(this.worldDefinition.spaceshipsDef);
    }


    private void createSBodies(ArrayList<PositionItemDto> sBodies) {
        for (PositionItemDto body : sBodies) {
            this.controller.addSBody(body.assetId, body.size, body.posX, body.posY, body.angle);
        }
    }


    private void createSpaceDecorators(ArrayList<PositionItemDto> decorators) {

        for (PositionItemDto deco : decorators) {
            this.controller.addDecorator(deco.assetId, deco.size, deco.posX, deco.posY, deco.angle);
        }
    }


    private void createPlayers(ArrayList<ItemDto> dBodies) {
        String playerId = null;

        for (ItemDto body : dBodies) {
            playerId = this.controller.addPlayer(
                    body.assetId, body.size, 500, 200, 0, 0, 0, 0, 0,
                    this.randomAngularSpeed(270), 0, 0);

            // Bullet weapon
            WeaponItemDto bullet = this.worldDefinition.bullets.get(0);
            this.controller.addWeaponToPlayer(
                    playerId, bullet.assetId, bullet.size,
                    bullet.firingSpeed, bullet.acc, bullet.accTime,
                    0, bullet.burstSize, bullet.fireRate);

            WeaponItemDto misil = this.worldDefinition.misilsDef.get(0);
            this.controller.addWeaponToPlayer(
                    playerId, misil.assetId, misil.size,
                    misil.firingSpeed, misil.acc, misil.accTime,
                    -body.size * 0.5, misil.burstSize, misil.fireRate);

            this.controller.addWeaponToPlayer(
                    playerId, misil.assetId, misil.size,
                    misil.firingSpeed, misil.acc, misil.accTime,
                    body.size * 0.5, misil.burstSize, misil.fireRate);
        }

        if (playerId != null) {
            this.controller.setLocalPlayer(playerId);
        }
    }


    private double randomAngularSpeed(double maxAngularSpeed) {
        return this.rnd.nextFloat() * maxAngularSpeed;
    }
}
