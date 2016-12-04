package peakrobots.ilkin;

import robocode.*;
import robocode.Robot;

import java.awt.*;

public class Altay extends Robot {

    enum Direction {
        North, East, South, West
    }

    private Direction currentWall;

    @Override
    public void run() {
        setBodyColor(Color.red);
        setGunColor(Color.yellow);

        currentWall = goToClosestWall();
        turnLeft(90);
        turnGunRight(90);
        double moveAmount = Math.max(getBattleFieldHeight(), getBattleFieldWidth());
        while (true) {
            ahead(moveAmount);
            turnRight(90);
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        fire(Rules.MAX_BULLET_POWER);
    }

    private Direction goToClosestWall() {
        Direction closestWall = getClosestWall();
        double targetHeading = getInitialHeading(closestWall);
        double heading = getHeading();
        if (targetHeading > heading) {
            turnLeft(heading - targetHeading);
        } else {
            turnRight(targetHeading - heading);
        }
        double distance = distanceTo(closestWall);
        back(distance);
        return closestWall;
    }

    private double getInitialHeading(Direction closestWall) {
        switch (closestWall) {
            case North:
                return 180;
            case East:
                return 270;
            case South:
                return 0;
            case West:
                return 90;
        }
        return 0;
    }

    private double distanceTo(Direction closestWall) {
        switch (closestWall) {
            case North:
                return getBattleFieldHeight() - getY();
            case East:
                return getBattleFieldWidth() - getX();
            case South:
                return getY();
            case West:
                return getX();
        }
        return 0;
    }

    private Direction getClosestWall() {
        double height = getBattleFieldHeight();
        double width  = getBattleFieldWidth();
        double x = getX();
        double y = getY();
        double diffNorth = height - y;
        double diffEast = width - x;
        double diffSouth = y;
        double diffWest = x;
        if (diffNorth < diffEast && diffNorth < diffSouth && diffNorth < diffWest) {
            return Direction.North;
        }
        if (diffEast < diffNorth && diffEast < diffSouth && diffEast < diffWest) {
            return Direction.East;
        }
        if (diffSouth < diffEast && diffSouth < diffNorth && diffSouth < diffWest) {
            return Direction.South;
        }
        return Direction.West;
    }
}
