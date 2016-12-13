package peakrobots.ilkin.util;

public class Enemy {
    public final String name;
    public double x;
    public double y;
    public double width;
    public double height;
    public double distance;
    public double bearing;
    public double heading;
    public double velocity;

    public Enemy(String name) {
        this.name = name;
    }

    public double getFutureX(long when){
        return x + Math.sin(heading) * velocity * when;
    }

    public double getFutureY(long when){
        return y + Math.cos(heading) * velocity * when;
    }
}
