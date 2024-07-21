package GamePackage;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.scene3d.Model3D;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import com.almasb.fxgl.entity.Entity;
import java.util.Map;
import javafx.scene.transform.Rotate;


public class GameApp extends GameApplication {

    private Entity platform;
    private javafx.scene.text.Text fpsText;

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

        Model3D model = FXGL.getAssetLoader().loadModel3D("uploads_files_4176475_Polina-final.obj");

        model.setScaleX(100);
        model.setScaleY(100);
        model.setScaleZ(100);

        var camera = FXGL.getGameScene().getCamera3D();
        camera.getTransform().translateX(5);
        camera.getTransform().translateY(-10);

        FXGL.getGameWorld().addEntityFactory(new Factory());

        var ground = FXGL.spawn("ground", 0, 5, 0);

        camera.getTransform().lookAt(ground.getPosition3D());

        platform = FXGL.spawn("platform", 0, 1, 0);

        Rotate rotateY = new Rotate();
        rotateY.setAxis(Rotate.Y_AXIS);
        rotateY.setAngle(180);

        SpawnData model_data = new SpawnData(0,1,+4);
        Entity model_entity = FXGL.entityBuilder(model_data)
                .view(model)
                .rotate(180)
                .build();

        model_entity.getViewComponent().getParent().getTransforms().add(rotateY);
        FXGL.getGameWorld().addEntity(model_entity);

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

        fpsText = FXGL.getUIFactoryService().newText("", Color.DARKRED, 14);
        FXGL.addUINode(fpsText, 50, 65);
    }

    @Override
    protected void onUpdate(double tpf){
        var number = (1.0 / FXGL.tpf());
        fpsText.setText(String.format("FPS: %d", (int) number));
    }

    public static void main(String[] args){
        launch(args);
    }
}
