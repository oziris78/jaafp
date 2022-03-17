package com.telek.world;

import com.badlogic.gdx.physics.box2d.*;
import com.telek.entities.*;
import com.telek.entities.enemies.*;
import com.telek.entities.player.*;
import com.telek.telekgdx.TelekGDX;

import java.util.LinkedList;

public class WorldListener implements ContactListener {

    Player player;
    LinkedList<Laser> lasers;
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
        if(TelekGDX.twoSidedEquals(entA, entB, EntityType.PLAYER, EntityType.DAMAGE_BALL)) {
            player.die(lasers);
            return;
        }

        // PLAYER AND ANY WALL
        if(TelekGDX.twoSidedEquals(entA, entB, EntityType.PLAYER, EntityType.WALL)) {
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
