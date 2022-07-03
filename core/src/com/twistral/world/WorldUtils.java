package com.twistral.world;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.twistral.entities.EntityType;

public class WorldUtils {

    public static void removeAllDamageBalls(World world){
        removeAllEntities(world, com.twistral.entities.EntityType.DAMAGE_BALL);
    }

    public static void removeAllSentries(World world){
        removeAllEntities(world, com.twistral.entities.EntityType.SENTRY);
    }

    private static void removeAllEntities(World world, com.twistral.entities.EntityType entityTypeToRemove){
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);
        com.twistral.entities.EntityType entityType;
        for(Body b : bodies)
            if( ((EntityType) b.getUserData()).equals(entityTypeToRemove))
                world.destroyBody(b);
    }

}
