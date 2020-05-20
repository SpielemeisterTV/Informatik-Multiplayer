package tv.spielemeister.mpg.engine.world;

public class Location extends Vector{

    private int world;
    private float rotation;

    public Location(int world, int x, int y, float rotation){
        super(x, y);
        this.world = world;
    }

    public int getWorld() {
        return world;
    }

    public void setWorld(int world) {
        this.world = world;
    }

    public float getRotation() { // Use getAngle for vector angle
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
