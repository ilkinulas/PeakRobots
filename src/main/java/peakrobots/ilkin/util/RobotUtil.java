package peakrobots.ilkin.util;

import robocode.AdvancedRobot;

public class RobotUtil {


    public static boolean isHeadingAtMe(Enemy enemy, AdvancedRobot me) {
        return absoluteBearing(me, enemy.x, enemy.y) <= 20;
    }

    public static double absoluteBearing(AdvancedRobot me, double x, double y) {
        return ((y - me.getY()) < 0 ? Math.PI : 0) +
                Math.atan((x - me.getX()) / (y - me.getY()));
    }

    public static double lerp(double start, double end, float percent)
    {
        return (start + percent*(end - start));
    }
}
