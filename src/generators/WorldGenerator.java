package generators;

import controller.Controller;
import java.util.ArrayList;
import java.util.Random;
import world.WorldDefItemDto;
import world.WorldDefPositionItemDto;
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

        this.createSpaceDecorators();
        this.createSBodies();
        this.createPlayers();
    }

    private void createSBodies() {
        ArrayList<WorldDefPositionItemDto> sBodies = this.worldDefinition.gravityBodies;

        for (WorldDefPositionItemDto body : sBodies) {
            this.controller.addSBody(body.assetId, body.size, body.posX, body.posY, body.angle);
        }
    }

    private void createSpaceDecorators() {
        ArrayList<WorldDefPositionItemDto> decorators = this.worldDefinition.spaceDecorators;

        for (WorldDefPositionItemDto deco : decorators) {
            this.controller.addDecorator(deco.assetId, deco.size, deco.posX, deco.posY, deco.angle);
        }
    }

    private void createPlayers() {
        ArrayList<WorldDefItemDto> dBodies = this.worldDefinition.spaceshipsDef;
        String playerId = null;

        for (WorldDefItemDto body : dBodies) {
            playerId = this.controller.addPlayer(
                    body.assetId, body.size, 500, 200, 0, 0, 0, 0, 0,
                    this.randomAngularSpeed(270), 0, 0);

            this.controller.addWeaponToPlayer(
                playerId, this.worldDefinition.primaryWeapon.get(0), 0);

            this.controller.addWeaponToPlayer(
                playerId, this.worldDefinition.secondaryWeapon.get(0), 0);

            this.controller.addWeaponToPlayer(
                playerId, this.worldDefinition.missilLaunchers.get(0), -10);

            this.controller.addWeaponToPlayer(
                playerId, this.worldDefinition.mineLaunchers.get(0), 10);
        }

        if (playerId != null) {
            this.controller.setLocalPlayer(playerId);
        } else {
            System.err.println("[DEBUG] No valid playerId to set as local player.");
        }
    }

    private double randomAngularSpeed(double maxAngularSpeed) {
        return this.rnd.nextFloat() * maxAngularSpeed;
    }
}
