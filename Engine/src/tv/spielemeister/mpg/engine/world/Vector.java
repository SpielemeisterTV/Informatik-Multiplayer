package tv.spielemeister.mpg.engine.world;

public class Vector {

    private int x, y;

    public Vector(int x, int y){
        this.x = x;
        this.y = y;
    }

    public double getLength(){
        return Math.sqrt(x*x + y*y);
    }

    public int getBlockX(){
        return this.getX()/16;
    }

    public int getBlockY(){
        return this.getY()/16;
    }

    public double distance(Vector vector){
        double x = Math.abs(this.x - vector.x);
        double y = Math.abs(this.y - vector.y);
        return Math.sqrt(x*x + y*y);
    }

    public void add(Vector vector){
        x += vector.getX();
        y += vector.getY();
    }

    public void subtract(Vector vector){
        x -= vector.getX();
        y -= vector.getY();
    }

    public void multiply(int fac){
        x *= fac;
        y *= fac;
    }

    public float getAngle(){ // Angle of the vector
        return (float) Math.atan2(y, x);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
