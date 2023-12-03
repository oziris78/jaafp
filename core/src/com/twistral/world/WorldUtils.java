// Copyright 2023 Oğuzhan Topaloğlu
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


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
