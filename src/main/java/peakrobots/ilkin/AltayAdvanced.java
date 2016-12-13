package peakrobots.ilkin;

import peakrobots.ilkin.util.Enemy;
import peakrobots.ilkin.util.EnemyCache;
import peakrobots.ilkin.util.PaintUtil;
import peakrobots.ilkin.util.RobotUtil;
import robocode.*;
import robocode.util.Utils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Map;

public class AltayAdvanced extends AdvancedRobot {

    private static final String EVENT_WALL_DETECTED = "WALL_DETECTED";
    private EnemyCache enemyCache = new EnemyCache();

    private int moveDirection = 1;
    private int turnCounter = 0;

    @Override
    public void run() {
        initialize();
        while (true) {
            setTurnRadarLeft(360);
            turnCounter++;
            execute();
        }
    }

    private void initialize() {
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        setAdjustRadarForRobotTurn(true);

        decorateRobot();
    }

    @Override
    public void onCustomEvent(CustomEvent event) {
        if (event.getCondition().getName().equals(EVENT_WALL_DETECTED)) {

        }
    }

    @Override
    public void onHitWall(HitWallEvent event) {
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        enemyCache.enemyScanened(this, e);
        Enemy enemy = enemyCache.getClosestEnemy();

        moveGun(enemy);
        moveRobot(enemy);
    }

    private void moveRobot(Enemy enemy) {
        if (enemy.distance > 250) {
            moveDirection = 1;
            setTurnRightRadians(enemy.bearing);
            setAhead(100 * moveDirection);
        } else {
            moveDirection = (turnCounter / 40) % 2 == 0 ? 1 : -1;
            setTurnRightRadians(enemy.bearing + Math.PI/2);
            setAhead(100 * moveDirection);
        }
    }
    private void moveGun(Enemy enemy) {
        double bulletPower = 1;
        int time = (int) (enemy.distance / Rules.getBulletSpeed(bulletPower));
        double enemyAbsoluteBearing = RobotUtil.absoluteBearing(
                this,
                enemy.getFutureX(time),
                enemy.getFutureY(time));

        setTurnGunRightRadians(
                Utils.normalRelativeAngle(enemyAbsoluteBearing - getGunHeadingRadians()));

        if (getGunTurnRemainingRadians() < Math.PI/20) {
            fire(3);
        }
    }

    @Override
    public void onRobotDeath(RobotDeathEvent event) {
        enemyCache.enemyDead(event.getName());
    }

    @Override
    public void onHitByBullet(HitByBulletEvent event) {
        super.onHitByBullet(event);
    }

    @Override
    public void onHitRobot(HitRobotEvent event) {
        setTurnRightRadians(Math.PI / 2);
    }

    @Override
    public void onBulletHitBullet(BulletHitBulletEvent event) {
        super.onBulletHitBullet(event);
    }

    @Override
    public void onBulletMissed(BulletMissedEvent event) {
        super.onBulletMissed(event);
    }

    @Override
    public void onBulletHit(BulletHitEvent event) {
        super.onBulletHit(event);
    }

    @Override
    public void onPaint(Graphics2D g) {

        for (Map.Entry<String, Enemy> entry : enemyCache.getAllEnemies()) {
            Enemy enemy = entry.getValue();
            PaintUtil.drawEnemy(enemy, g, PaintUtil.RED);
        }
        Enemy closestEnemy = enemyCache.getClosestEnemy();
        if (closestEnemy != null) {
            PaintUtil.drawEnemy(closestEnemy, g, PaintUtil.BLUE);
        }

        drawGunHeading(g);
    }

    private void drawGunHeading(Graphics2D g) {
        double length = getBattleFieldWidth();
        double alpha = getGunHeadingRadians();
        double x = getX();
        double targetX = x + length * Math.sin(alpha);
        double y = getY();
        double targetY = y + length * Math.cos(alpha);
        g.drawLine((int)x, (int)y, (int)targetX, (int)targetY);
    }

    private void decorateRobot() {
        setBodyColor(new Color(0xab, 0xcd, 0xef));
        setGunColor(new Color(0x00, 0x00, 0xdd));
        setRadarColor(new Color(0xab, 0xcd, 0xef));
        setBulletColor(new Color(0xaa, 0x00, 0x00));
    }

}
