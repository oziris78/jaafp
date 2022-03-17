package com.telek.world;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.telek.entities.EntityType;

public class WorldUtils {

    public static void removeAllDamageBalls(World world){
        removeAllEntities(world, EntityType.DAMAGE_BALL);
    }

    public static void removeAllSentries(World world){
        removeAllEntities(world, EntityType.SENTRY);
    }

    private static void removeAllEntities(World world, EntityType entityTypeToRemove){
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);
        EntityType entityType;
        for(Body b : bodies)
            if( ((EntityType) b.getUserData()).equals(entityTypeToRemove))
                world.destroyBody(b);
    }

}
