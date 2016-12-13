package peakrobots.ilkin.util;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EnemyCache {
    private Map<String, Enemy> cache = new HashMap<String, Enemy>();

    public Enemy enemyScanened(AdvancedRobot me, ScannedRobotEvent event) {
        String name = event.getName();
        Enemy enemy = cache.get(name);
        if (enemy == null) {
            enemy = new Enemy(name);
        }

        double absoluteBearing = me.getHeadingRadians() + event.getBearingRadians();
        enemy.x = me.getX() + event.getDistance() * Math.sin(absoluteBearing);
        enemy.y = me.getY() + event.getDistance() * Math.cos(absoluteBearing);
        enemy.distance = event.getDistance();
        enemy.width = me.getWidth();
        enemy.height = me.getHeight();
        enemy.bearing = event.getBearingRadians();
        enemy.heading = event.getHeadingRadians();
        enemy.velocity = event.getVelocity();

        cache.put(name, enemy);
        return enemy;
    }

    public void enemyDead(String name) {
        cache.remove(name);
    }

    public Set<Map.Entry<String, Enemy>> getAllEnemies() {
        return cache.entrySet();
    }

    public Enemy getClosestEnemy() {
        Enemy result = null;
        for (Map.Entry<String, Enemy> entry : cache.entrySet()) {
            if (result == null || result.distance >= entry.getValue().distance) {
                result = entry.getValue();
            }
        }
        return result;
    }

}
