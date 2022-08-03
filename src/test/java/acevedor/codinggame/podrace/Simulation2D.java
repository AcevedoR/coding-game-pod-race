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

public class Simulation2D extends GameApplication {

    final static int ration2D = 1;
    public static final int TICK_DURATION = 20;
    boolean autoplay = true;
    List<Entity> simulationPoints = new ArrayList<>();
    Entity checkpointIcon;
    SimulateEngine simulationEngine;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(16000* ration2D);
        settings.setHeight(9000* ration2D);
        settings.setTitle("Basic Game App");
    }

    @Override
    protected void initInput() {
        onKeyUp(KeyCode.SPACE, () -> {
            clearSimulation();
            Solutionn solution = simulationEngine.play();
            drawSimulation(solution);
            return Unit.INSTANCE;
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
            clearSimulation();
            Solutionn solution = simulationEngine.play();
            drawSimulation(solution);
        }
    }

    private void drawSimulation(final Solutionn solution) {
        for (int i = 0; i < solution.moves1.size(); i++) {
            Pod pod = solution.simulateMove(i);
            Entity p = spawn("simulationPoint", new Point2D(pod.position.x, pod.position.y));
            simulationPoints.add(p);
            if(i == solution.moves1.size() - 1){
                drawCheckpoinPassedIcon(pod.currentCheckpoint);
            }
        }
    }
    private void clearSimulation() {
        for (Entity p : simulationPoints) {
            p.removeFromWorld();
        }
        simulationPoints.clear();
    }
    private void drawCheckpoinPassedIcon(final Checkpoint c){
        if(checkpointIcon != null){
            checkpointIcon.removeFromWorld();
        }
        checkpointIcon = spawn("checkpointPassedIcon", new Point2D(c.position.x, c.position.y));
    }

    @Override
    protected void initGame(){

        getGameScene().setBackgroundColor(Color.BLACK);

        getGameWorld().addEntityFactory(new SpaceRangerFactory());

        spawn("player", 2000* ration2D, 2000* ration2D);
        Entity player = getGameWorld().getSingleton(EntityType.PLAYER);
        player.setRotation(90);

        simulationEngine = new SimulateEngine(
                ration2D,
                player,
                List.of(
                        spawn("checkpoint1", 4000 * ration2D, 8000 * ration2D),
                        spawn("checkpoint2", 13000 * ration2D, 4000 * ration2D),
                        spawn("checkpoint3", 2500 * ration2D, 1200 * ration2D),
                        spawn("checkpoint4", 14000 * ration2D, 7000 * ration2D)
                )
        );

        GameParameters.depth = 10;
        simulationEngine.playerCodingGame.startTime = System.currentTimeMillis()+50;
    }
    public static class SpaceRangerFactory implements EntityFactory {
        @Spawns("player")
        public Entity newPlayer(SpawnData data) {
            var top = new Circle(200 * ration2D, Color.WHITE);
            top.setStroke(Color.GRAY);
            var bot = new Rectangle(12* ration2D, 3000* ration2D, Color.CYAN);
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
            var top = new Circle(600* ration2D, Color.GREEN);
            top.setStroke(Color.GRAY);

            return entityBuilder(data)
                    .type(EntityType.CP1)
                    .view(top)
                    .build();
        }
        @Spawns("checkpoint2")
        public Entity newCheckpoint2(SpawnData data) {
            var top = new Circle(600* ration2D, Color.YELLOW);
            top.setStroke(Color.GRAY);

            return entityBuilder(data)
                    .type(EntityType.CP2)
                    .view(top)
                    .build();
        }
        @Spawns("checkpoint3")
        public Entity newCheckpoint3(SpawnData data) {
            var top = new Circle(600* ration2D, Color.MAGENTA);
            top.setStroke(Color.GRAY);

            return entityBuilder(data)
                    .type(EntityType.CP3)
                    .view(top)
                    .build();
        }
        @Spawns("checkpoint4")
        public Entity newCheckpoint4(SpawnData data) {
            var top = new Circle(600* ration2D, Color.RED);
            top.setStroke(Color.GRAY);

            return entityBuilder(data)
                    .type(EntityType.CP3)
                    .view(top)
                    .build();
        }
        @Spawns("simulationPoint")
        public Entity simulationPoint(SpawnData data) {
            var top = new Circle(100 * ration2D, Color.IVORY);
            top.setStroke(Color.GRAY);
            return entityBuilder(data)
                    .type(EntityType.PLAYER)
                    .view(top)
                    .zIndex(1)
                    .build();
        }
        @Spawns("checkpointPassedIcon")
        public Entity checkpointPassedIcon(SpawnData data) {
            var top = new Circle(200 * ration2D, Color.NAVY);
            top.setStroke(Color.GRAY);
            return entityBuilder(data)
                    .type(EntityType.PLAYER)
                    .view(top)
                    .zIndex(1)
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
}
