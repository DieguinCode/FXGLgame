package GamePackage;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import com.almasb.fxgl.entity.Entity;

import java.util.Map;


public class GameApp extends GameApplication {

    private Entity platform;

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle("Our Beautiful Game");
        gameSettings.setVersion("1.0");
        gameSettings.set3D(true);
        gameSettings.setWidth(1280);
        gameSettings.setHeight(720);
    }

    @Override
    protected void initInput(){

        var speed = 0.25;

        FXGL.onKey(KeyCode.W, () -> {
           platform.translateZ(speed);
        });
        FXGL.onKey(KeyCode.S, () -> {
            platform.translateZ(-speed);
        });
        FXGL.onKey(KeyCode.A, () -> {
            platform.translateX(-speed);
        });
        FXGL.onKey(KeyCode.D, () -> {
            platform.translateX(speed);
        });
        FXGL.onKey(KeyCode.R, () -> {
            platform.setPosition3D(new Point3D(0, 1, 0));
        });
    }

    @Override
    protected void initGameVars(Map<String, Object> vars){
        vars.put("score", 0);
    }

    @Override
    protected void initGame(){

        var camera = FXGL.getGameScene().getCamera3D();
        camera.getTransform().translateX(5);
        camera.getTransform().translateY(-10);

        FXGL.getGameWorld().addEntityFactory(new Factory());

        var ground = FXGL.spawn("ground", 0, 5, 0);

        camera.getTransform().lookAt(ground.getPosition3D());

        platform = FXGL.spawn("platform", 0, 1, 0);

        //Spawn: x and y are in pixels at our screen (Default: Middle of the Screen)
        FXGL.run(() -> {
            var point = FXGLMath.randomPoint(new Rectangle2D(-5, -5, 10, 10));

            FXGL.spawn("target", point.getX(), -5, point.getY());
        }, Duration.seconds(2));

    }

    //Collision Handler
    @Override
    protected void initPhysics(){
        FXGL.onCollisionBegin(EntityType.TARGET, EntityType.GROUND, (target, ground) -> {
            target.removeFromWorld();
            FXGL.inc("score", -1);
        });

        FXGL.onCollisionBegin(EntityType.TARGET, EntityType.PLATFORM, (target, platform) -> {
            target.removeFromWorld();
            FXGL.inc("score", +1);
        });
    }

    @Override
    protected void initUI(){
        var scoreText = FXGL.getUIFactoryService().newText("", Color.BLACK, 14);
        scoreText.textProperty().bind(FXGL.getip("score").asString("Score: %d"));

        FXGL.addUINode(scoreText, 50, 50);
    }

    public static void main(String[] args){
        launch(args);
    }
}
