package acevedor.codinggame.podrace;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import kotlin.Unit;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.onKeyUp;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

public class BasicGameApp extends GameApplication {

    Player playerCodingGame = new Player();
    List<Entity> checkpoints = new ArrayList<>();
    Entity currentCheckpoint;
    Pod simulation;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Basic Game App");
    }

    @Override
    protected void initInput() {
        onKeyUp(KeyCode.SPACE, () -> {
            System.out.println("\n ================================================= ");
            Entity player = getGameWorld().getSingleton(EntityType.PLAYER);

            simulation.play(new Point(currentCheckpoint.getX(), currentCheckpoint.getY()), 5);
            player.setPosition(new Point2D(simulation.position.x, simulation.position.y));
            player.setRotation(minus90(simulation.angle));
            System.err.println("x: " + player.getX() + " y:" + player.getY());

            double distance = new Point(player.getX(), player.getY()).distance(new Point(currentCheckpoint.getX(), currentCheckpoint.getY()));
            System.out.println("distance to checkpoint: "+distance);
            if(distance < 20 ){
                currentCheckpoint = getNextCheckpoint();
                System.out.println("checkpoint reached, new one: "+currentCheckpoint.toString());
            }

            return Unit.INSTANCE;
        });
    }
    public Entity getNextCheckpoint() {
        int i = checkpoints.indexOf(currentCheckpoint);
        if (i + 1 < checkpoints.size()) {
            return checkpoints.get(i + 1);
        } else {
            return checkpoints.get(0);
        }
    }
    @Override
    protected void initGame(){

        getGameScene().setBackgroundColor(Color.BLACK);

        getGameWorld().addEntityFactory(new SpaceRangerFactory());

        spawn("player", 200, 200);
        checkpoints.add(spawn("checkpoint1", 400, 400));
        checkpoints.add(spawn("checkpoint2", 250, 120));
        checkpoints.add(spawn("checkpoint3", 500, 300));
        currentCheckpoint = checkpoints.get(0);
        Entity player = getGameWorld().getSingleton(EntityType.PLAYER);
        player.setRotation(0);
        simulation = Pod.of(player.getX(), player.getY(), 90);
    }
    public static class SpaceRangerFactory implements EntityFactory {
        @Spawns("player")
        public Entity newPlayer(SpawnData data) {
            var top = new Circle(5, Color.RED);
            top.setStroke(Color.GRAY);
            var bot = new Rectangle(1, 30, Color.BLUE);
            bot.setStroke(Color.GRAY);

            return entityBuilder(data)
                    .type(EntityType.PLAYER)
                    .view(top)
                    .view(bot)
                    .build();
        }
        @Spawns("checkpoint1")
        public Entity newCheckpoint(SpawnData data) {
            var top = new Circle(10, Color.GREEN);
            top.setStroke(Color.GRAY);

            return entityBuilder(data)
                    .type(EntityType.CP1)
                    .view(top)
                    .build();
        }
        @Spawns("checkpoint2")
        public Entity newCheckpoint2(SpawnData data) {
            var top = new Circle(10, Color.YELLOW);
            top.setStroke(Color.GRAY);

            return entityBuilder(data)
                    .type(EntityType.CP2)
                    .view(top)
                    .build();
        }
        @Spawns("checkpoint3")
        public Entity newCheckpoint3(SpawnData data) {
            var top = new Circle(10, Color.MAGENTA);
            top.setStroke(Color.GRAY);

            return entityBuilder(data)
                    .type(EntityType.CP3)
                    .view(top)
                    .build();
        }
    }

    public enum EntityType {
        PLAYER,
        CP1,
        CP2,
        CP3
    }
    public static void main(String[] args) {
        launch(args);
    }

    double minus90(double angle){
        angle = angle - 90;
        if (angle >= 360.0) {
            angle = angle - 360.0;
        } else if (angle < 0.0) {
            angle += 360.0;
        }
        return angle;
    }
    double plus90(double angle){
        angle = angle + 90;
        if (angle >= 360.0) {
            angle = angle - 360.0;
        } else if (angle < 0.0) {
            angle += 360.0;
        }
        return angle;
    }

}
