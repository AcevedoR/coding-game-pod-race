package acevedor.codinggame.podrace;

import com.almasb.fxgl.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;

public class SimulateEngine {
    Player playerCodingGame = new Player();
    List<Entity> checkpoints = new ArrayList<>();
    Entity currentCheckpoint;
    int r;
    Entity player;

    public SimulateEngine(final int r, Entity player, List<Entity> checkpoints) {
        this.r = r;
        this.player = player;

        this.currentCheckpoint = checkpoints.get(0);
        this.checkpoints.addAll(checkpoints);
        for (int i = 0; i < this.checkpoints.size(); i++) {
            GameCache.checkpointsList.add(new Checkpoint(i, new Point(this.checkpoints.get(i).getX(), this.checkpoints.get(i).getY())));
        }
        playerCodingGame.init();
    }


    public Solutionn play() {
        playerCodingGame.debug = true;
        playerCodingGame.isTesting = true;
        playerCodingGame.init();
        playerCodingGame.startTime = System.currentTimeMillis();
        System.out.println("\n ================================================= ");

        playerCodingGame.applyInput(
                (int) player.getX(),
                (int) player.getY(),
                playerCodingGame.pod1 != null ? (int) playerCodingGame.pod1.vx : 0,
                playerCodingGame.pod1 != null ? (int) playerCodingGame.pod1.vy : 0,
                (int) plus90(player.getRotation()),
                checkpoints.indexOf(currentCheckpoint),
                0, 0, 0, 0, 0, 0 // pod 2
        );
        playerCodingGame.play();
        player.setPosition(new Point2D(playerCodingGame.pod1.position.x, playerCodingGame.pod1.position.y));
        player.setRotation(minus90(playerCodingGame.pod1.angle));

        double distance = new Point(player.getX(), player.getY()).distance(new Point(currentCheckpoint.getX(), currentCheckpoint.getY()));
        System.out.println("distance to checkpoint: " + distance + " position: " + currentCheckpoint.getPosition() + " name:" + currentCheckpoint.getType());
        if (distance < 600 * r) {
            currentCheckpoint = getNextCheckpoint();
            System.out.println("checkpoint reached, new one: " + currentCheckpoint.toString());
        }

        return playerCodingGame.lastSolution;
    }

    public Entity getNextCheckpoint() {
        int i = checkpoints.indexOf(currentCheckpoint);
        if (i + 1 < checkpoints.size()) {
            return checkpoints.get(i + 1);
        } else {
            return checkpoints.get(0);
        }
    }

    double minus90(double angle) {
        angle = angle - 90;
        if (angle >= 360.0) {
            angle = angle - 360.0;
        } else if (angle < 0.0) {
            angle += 360.0;
        }
        return angle;
    }

    double plus90(double angle) {
        angle = angle + 90;
        if (angle >= 360.0) {
            angle = angle - 360.0;
        } else if (angle < 0.0) {
            angle += 360.0;
        }
        return angle;
    }
}
