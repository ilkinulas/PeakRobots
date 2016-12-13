package peakrobots.ilkin.util;

import java.awt.*;

public class PaintUtil {
    public static final Color RED = new Color(0xff, 0x4d, 0x77, 0x80);
    public static final Color GREEN = new Color(0x66, 0xff, 0x66, 0x80);
    public static final Color BLUE = new Color(0x3f, 0xb3, 0xff, 0x80);
    public static final Color YELLOW  = new Color(0xff, 0xff, 0x66, 0x80);

    public static void drawEnemy(Enemy enemy, Graphics2D g, Color color) {
        g.setColor(color);
        g.fillRect(
                (int) enemy.x - (int)enemy.width / 2,
                (int) enemy.y - (int)enemy.height / 2,
                (int) enemy.width,
                (int) enemy.height);
    }
}
