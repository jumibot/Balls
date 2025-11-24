package worlds;


public interface WorldDefinitionProvider {

    WorldDefinition provide(int worldWidth, int worldHeight);
}