package acevedor.codinggame.podrace;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

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
            Entity chekpoint1 = getGameWorld().getSingleton(EntityType.CP1);
            Entity chekpoint2 = getGameWorld().getSingleton(EntityType.CP2);

            simulation.play(new Point(chekpoint1.getX(), chekpoint1.getY()), 5);

            System.out.println("old angle: " + plus90(player.getRotation()) + " new:" + simulation.angle);

            player.setPosition(new Point2D(simulation.position.x, simulation.position.y));
            player.setRotation(minus90(simulation.angle));
            System.err.println("x: " + player.getX() + " y:" + player.getY());
            return Unit.INSTANCE;
        });
    }

    @Override
    protected void initGame(){

        getGameScene().setBackgroundColor(Color.BLACK);

        getGameWorld().addEntityFactory(new SpaceRangerFactory());

        spawn("player", 200, 200);
        spawn("checkpoint1", 400, 400);
        spawn("checkpoint2", 250, 120);
        currentCheckpoint = getGameWorld().getSingleton(EntityType.CP1);
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
    }

    public enum EntityType {
        PLAYER,
        CP1,
        CP2
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
