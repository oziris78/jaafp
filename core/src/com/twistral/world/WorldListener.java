package com.twistral.world;

import com.badlogic.gdx.physics.box2d.*;
import com.twistral.toriagdx.ToriaGDX;
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
        com.twistral.entities.EntityType entA = (com.twistral.entities.EntityType) fixA.getBody().getUserData();
        com.twistral.entities.EntityType entB = (com.twistral.entities.EntityType) fixB.getBody().getUserData();

        // CHECK IF ANY OF THEM ARE NULL
        if(fixA == null || fixB == null || entA == null || entB == null) return;

        // PLAYER AND DAMAGE_BALL
        if(ToriaGDX.twoSidedEquals(entA, entB, com.twistral.entities.EntityType.PLAYER, com.twistral.entities.EntityType.DAMAGE_BALL)) {
            player.die(lasers);
            return;
        }

        // PLAYER AND ANY WALL
        if(ToriaGDX.twoSidedEquals(entA, entB, com.twistral.entities.EntityType.PLAYER, com.twistral.entities.EntityType.WALL)) {
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




}
