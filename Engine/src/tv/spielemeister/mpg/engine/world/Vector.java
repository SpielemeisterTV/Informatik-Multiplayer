package tv.spielemeister.mpg.engine.world;

public class Vector {

    private double x, y;

    public Vector(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getLength(){
        return Math.sqrt(x*x + y*y);
    }

    public void normalize(){
        double fac = 1 / getLength();
        x *= fac;
        y *= fac;
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

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
