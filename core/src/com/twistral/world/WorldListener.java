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

import com.badlogic.gdx.physics.box2d.*;
import com.twistral.tempest.utils.ScreenUtils;
import com.twistral.entities.EntityType;
import com.twistral.entities.enemies.Laser;
import com.twistral.entities.player.Player;

import java.util.LinkedList;

public class WorldListener implements ContactListener {

    com.twistral.entities.player.Player player;
    LinkedList<com.twistral.entities.enemies.Laser> lasers;
    public WorldListener(LinkedList<Laser> lasers, final Player player){
        this.lasers = lasers;
        this.player = player;
    }
    @Override

    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        EntityType entA = (EntityType) fixA.getBody().getUserData();
        EntityType entB = (EntityType) fixB.getBody().getUserData();

        // CHECK IF ANY OF THEM ARE NULL
        if(fixA == null || fixB == null || entA == null || entB == null) return;

        // PLAYER AND DAMAGE_BALL
        if(twoSidedEquals(entA, entB, EntityType.PLAYER, EntityType.DAMAGE_BALL)) {
            player.die(lasers);
            return;
        }

        // PLAYER AND ANY WALL
        if(twoSidedEquals(entA, entB, EntityType.PLAYER, EntityType.WALL)) {
            Body playerBody = (fixA.getBody().getUserData().equals(EntityType.PLAYER)) ? fixA.getBody() : fixB.getBody();
            playerBody.setLinearVelocity(0, 0);
            return;
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    @Override
    public void endContact(Contact contact) {

    }

    public static boolean twoSidedEquals(Object obj1, Object obj2, Object val1, Object val2) {
        return (obj1.equals(val1) && obj2.equals(val2)) || (obj1.equals(val2) && obj2.equals(val1));
    }


}
