package acevedor.codinggame.podrace;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

import java.util.ArrayList;
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

    final static int r = 1;
    public static final int TICK_DURATION = 20;
    boolean autoplay = true;
    Player playerCodingGame = new Player();
    List<Entity> checkpoints = new ArrayList<>();
    Entity currentCheckpoint;
    Pod simulation;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(16000*r);
        settings.setHeight(9000*r);
        settings.setTitle("Basic Game App");
    }

    @Override
    protected void initInput() {
        onKeyUp(KeyCode.SPACE, () -> {
            return play();
        });
        onKeyUp(KeyCode.ENTER, () -> {
            autoplay = !autoplay;
            return null;
        });
    }

    @Override
    protected void onUpdate(final double tpf) {
        super.onUpdate(tpf);
        if(autoplay){
            try {
                Thread.sleep(TICK_DURATION);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            play();
        }
    }

    private Unit play() {
        playerCodingGame.debug = true;
        playerCodingGame.isTesting = true;
        playerCodingGame.init();
        playerCodingGame.startTime = System.currentTimeMillis()+50;
        System.out.println("\n ================================================= ");
        Entity player = getGameWorld().getSingleton(EntityType.PLAYER);

        playerCodingGame.applyInput(
                (int) player.getX(),
                (int) player.getY(),
                simulation != null ? (int) simulation.vx : 0,
                simulation != null ? (int) simulation.vy : 0,
                (int) plus90(player.getRotation()),
                checkpoints.indexOf(currentCheckpoint),
                0,0,0,0,0,0 // pod 2
        );
        playerCodingGame.play();
        simulation = playerCodingGame.pod1;
        player.setPosition(new Point2D(simulation.position.x, simulation.position.y));
        player.setRotation(minus90(simulation.angle));

        double distance = new Point(player.getX(), player.getY()).distance(new Point(currentCheckpoint.getX(), currentCheckpoint.getY()));
        System.out.println("distance to checkpoint: "+distance + " position: "+currentCheckpoint.getPosition()+ " name:" + currentCheckpoint.getType());
        if(distance < 600*r ){
            currentCheckpoint = getNextCheckpoint();
            System.out.println("checkpoint reached, new one: "+currentCheckpoint.toString());
        }

        return Unit.INSTANCE;
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

        spawn("player", 2000*r, 2000*r);
        checkpoints.add(spawn("checkpoint1", 4000*r, 8000*r));
        checkpoints.add(spawn("checkpoint2", 13000*r, 4000*r));
        checkpoints.add(spawn("checkpoint3", 2500*r, 1200*r));
        checkpoints.add(spawn("checkpoint4", 14000*r, 7000*r));
        currentCheckpoint = checkpoints.get(0);
        for (int i = 0; i < checkpoints.size(); i++) {
            GameCache.checkpointsList.add(new Checkpoint(i, new Point(checkpoints.get(i).getX(), checkpoints.get(i).getY())));
        }

        Entity player = getGameWorld().getSingleton(EntityType.PLAYER);
        player.setRotation(90);

        playerCodingGame.init();
        simulation = playerCodingGame.pod1;
    }
    public static class SpaceRangerFactory implements EntityFactory {
        @Spawns("player")
        public Entity newPlayer(SpawnData data) {
            var top = new Circle(200 *r, Color.WHITE);
            top.setStroke(Color.GRAY);
            var bot = new Rectangle(12*r, 3000*r, Color.CYAN);
            bot.setStroke(Color.GRAY);

            return entityBuilder(data)
                    .type(EntityType.PLAYER)
                    .view(top)
                    .view(bot)
                    .zIndex(1)
                    .build();
        }
        @Spawns("checkpoint1")
        public Entity newCheckpoint(SpawnData data) {
            var top = new Circle(600*r, Color.GREEN);
            top.setStroke(Color.GRAY);

            return entityBuilder(data)
                    .type(EntityType.CP1)
                    .view(top)
                    .build();
        }
        @Spawns("checkpoint2")
        public Entity newCheckpoint2(SpawnData data) {
            var top = new Circle(600*r, Color.YELLOW);
            top.setStroke(Color.GRAY);

            return entityBuilder(data)
                    .type(EntityType.CP2)
                    .view(top)
                    .build();
        }
        @Spawns("checkpoint3")
        public Entity newCheckpoint3(SpawnData data) {
            var top = new Circle(600*r, Color.MAGENTA);
            top.setStroke(Color.GRAY);

            return entityBuilder(data)
                    .type(EntityType.CP3)
                    .view(top)
                    .build();
        }
        @Spawns("checkpoint4")
        public Entity newCheckpoint4(SpawnData data) {
            var top = new Circle(600*r, Color.RED);
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
        CP3,
        CP4,
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
