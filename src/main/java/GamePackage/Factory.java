package GamePackage;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.scene3d.Cone;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

//There is no methods to implement, it just marks your factory as the entity factory.
public class Factory implements EntityFactory {

    @Spawns("target")
    public Entity newTarget(SpawnData data){

        var view = new Cone();
        view.setBottomRadius(1);
        view.setTopRadius(0.2);
        view.setHeight(1);

        //Box3D it's used to wrap the view and check collisions.
        //Collidable is a flag.

        var e =  FXGL.entityBuilder(data)
                .type(EntityType.TARGET)
                .bbox(BoundingShape.box3D(2, 1, 2))
                .view(view)
                .collidable()
                .with(new ProjectileComponent(new Point2D(0,1), 1.5).allowRotation(false))
                .build();

        e.setScaleUniform(0);

        FXGL.animationBuilder()
                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                .scale(e)
                .from(new Point3D(0,0,0))
                .to(new Point3D(1,1,1))
                .buildAndPlay();

        return e;
    }

    @Spawns("ground")
    public Entity newGround(SpawnData data){

        var mat = new PhongMaterial(Color.BROWN);

        var view = new Box(20, 0.4, 20);
        view.setMaterial(mat);

        return FXGL.entityBuilder(data)
                .type(EntityType.GROUND)
                .bbox(BoundingShape.box3D(20, 0.4, 20))
                .view(view)
                .collidable()
                .build();
    }

    @Spawns("platform")
    public Entity newPlatform(SpawnData data){

        var mat = new PhongMaterial(Color.BLUE);

        var view = new Box(2, 0.2, 2);
        view.setMaterial(mat);

        return FXGL.entityBuilder(data)
                .type(EntityType.PLATFORM)
                .bbox(BoundingShape.box3D(2, 0.2, 2))
                .view(view)
                .collidable()
                .build();
    }
}
