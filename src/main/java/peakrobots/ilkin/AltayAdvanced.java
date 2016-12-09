package peakrobots.ilkin;

import robocode.*;
import robocode.util.Utils;

import java.awt.*;
import java.awt.geom.Point2D;

public class AltayAdvanced extends AdvancedRobot {

    private boolean move = true;
    private boolean initialHeadingAdjustInProgress = false;
    private static final String EVENT_WALL_DETECTED = "WALL_DETECTED";
    private double oldEnemyHeading;

    @Override
    public void run() {
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        setBodyColor(Color.red);
        setGunColor(Color.yellow);

        addCustomEvent(new Condition(EVENT_WALL_DETECTED) {
            @Override
            public boolean test() {
                if (initialHeadingAdjustInProgress) {
                    double heading = getHeading();
                    return heading == 0 ||
                            heading == 90 ||
                            heading == 180 ||
                            heading == 270 ||
                            heading == 360;
                }
                return false;
            }
        });

        while (true) {
            setTurnRadarLeft(360);
            if (move) {
                setAhead(10000);
            }
            execute();
        }
    }

    @Override
    public void onCustomEvent(CustomEvent event) {
        if (event.getCondition().getName().equals(EVENT_WALL_DETECTED)) {
            initialHeadingAdjustInProgress = false;
            setAhead(10000);
        }
    }

    @Override
    public void onHitWall(HitWallEvent event) {
        move = false;
        setTurnLeft(90 - event.getBearing());
        setTurnGunLeft(180 - event.getBearing());
        initialHeadingAdjustInProgress = true;
    }


    @Override
    public void onScannedRobot(ScannedRobotEvent e) {

        double bulletPower = Math.min(3.0,getEnergy());
        double myX = getX();
        double myY = getY();
        double absoluteBearing = getHeadingRadians() + e.getBearingRadians();
        double enemyX = getX() + e.getDistance() * Math.sin(absoluteBearing);
        double enemyY = getY() + e.getDistance() * Math.cos(absoluteBearing);
        double enemyHeading = e.getHeadingRadians();
        double enemyHeadingChange = enemyHeading - oldEnemyHeading;
        double enemyVelocity = e.getVelocity();
        oldEnemyHeading = enemyHeading;

        double deltaTime = 0;
        double battleFieldHeight = getBattleFieldHeight(),
                battleFieldWidth = getBattleFieldWidth();
        double predictedX = enemyX, predictedY = enemyY;
        while((++deltaTime) * (20.0 - 3.0 * bulletPower) <
                Point2D.Double.distance(myX, myY, predictedX, predictedY)){
            predictedX += Math.sin(enemyHeading) * enemyVelocity;
            predictedY += Math.cos(enemyHeading) * enemyVelocity;
            enemyHeading += enemyHeadingChange;
            if(	predictedX < 18.0
                    || predictedY < 18.0
                    || predictedX > battleFieldWidth - 18.0
                    || predictedY > battleFieldHeight - 18.0){

                predictedX = Math.min(Math.max(18.0, predictedX),
                        battleFieldWidth - 18.0);
                predictedY = Math.min(Math.max(18.0, predictedY),
                        battleFieldHeight - 18.0);
                break;
            }
        }
        double theta = Utils.normalAbsoluteAngle(Math.atan2(
                predictedX - getX(), predictedY - getY()));

        setTurnRadarRightRadians(Utils.normalRelativeAngle(
                absoluteBearing - getRadarHeadingRadians()));
        setTurnGunRightRadians(Utils.normalRelativeAngle(
                theta - getGunHeadingRadians()));

        fire(bulletPower);
    }

}
