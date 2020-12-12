package tv.spielemeister.mpg.engine.world;

public class Physics {

    private static PhysicsProperties[] properties = new PhysicsProperties[512];

    private static final PhysicsProperties defaultProperties =
            new PhysicsProperties(false, false, false, false, 0);

    public static void updateProperties(int tileId, PhysicsProperties properties){
        Physics.properties[tileId] = properties;
    }

    public static PhysicsProperties getProperties(int tileId){
        if(properties[tileId] != null)
            return properties[tileId];
        else
            return defaultProperties;
    }

    public static class PhysicsProperties {
        /*
        *   Solid: cannot be went trough
        *   Slippery: Players slip to the next tile (if oriented to the tile in which it is oriented)
        *   oriented: Makes other properties behave differently based on their orientation
        *   burning: Damages entities at a constant rate
        *   cooldown: Players are only able to leave the tile after the cooldown (ms) has passed
        */
        public final boolean solid, slippery, oriented, burning;
        public final int cooldown;

        public PhysicsProperties(boolean solid, boolean slippery, boolean oriented, boolean burning, int cooldown){
            this.solid = solid;
            this.slippery = slippery;
            this.oriented = oriented;
            this.burning = burning;
            this.cooldown = cooldown;
        }
    }


}
