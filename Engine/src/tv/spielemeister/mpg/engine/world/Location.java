package tv.spielemeister.mpg.engine.world;

public class Location extends Vector{

    private int world, rotation;

    public Location(int world, int x, int y, int rotation){
        super(x, y);
        this.world = world;
    }

    public int getWorld() {
        return world;
    }

    public void setWorld(int world) {
        this.world = world;
    }

    public int getRotation() { // Use getAngle for vector angle
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }
}
