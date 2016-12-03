package peakrobots.ilkin;

import robocode.Robot;
import robocode.Rules;

public class Altay extends Robot {

    @Override
    public void run() {
        while (true) {
            turnRight(60);
            fire(Rules.MAX_BULLET_POWER);
            ahead(50);
        }
    }

}
