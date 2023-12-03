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


package com.twistral.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.*;
import com.twistral.tempest.assetsorter.*;
import com.twistral.tempest.box2d.BodyFactory;
import com.twistral.entities.EntityType;
import com.twistral.utils.Consts;


public class DamageBall {

    private static final int THRESHOLD = 4;
    private float MAX_SPEED;
    private float SPEED_INC;
    public com.twistral.entities.EntityType entityType;

    private Texture texture;
    private Sprite sprite;
    private Body body;
    private int currentPoint;
    private final Vector2[] path;

    // for checking collision
    Rectangle bodyRec = new Rectangle(0,0,2*THRESHOLD,2*THRESHOLD);
    Rectangle nextPointRec = new Rectangle(0,0,2*THRESHOLD,2*THRESHOLD);

    // garbage vars
    Vector2 currentDirection = Vector2.Zero;
    Vector2 firstPoint = Vector2.Zero;
    Vector2 secondPoint = Vector2.Zero;
    Vector2 velTemp = Vector2.Zero;
    Vector2 posTemp = Vector2.Zero;
    int nextPoint = 0;

    public DamageBall(AssetSorter assetSorter, World world, final Vector2[] path){
        if(path.length < 2) Gdx.app.log("WARNING", "path.length in DamageBall constructor can't be less than 2");
        MAX_SPEED = 8f;
        SPEED_INC = 1f;
        this.texture = assetSorter.getResource("GAME_SCREEN", "damageBallTexture", Texture.class);
        this.sprite = new Sprite( this.texture );
        this.currentPoint = 0;
        this.path = path;

        // im copy pasting this bit because some change in Tempest-gdx
        // makes this code incompatible with the new versions...
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set((int) path[0].x / Consts.PPM, (int) path[0].y / Consts.PPM);
        body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(8 / Consts.PPM, 8 / Consts.PPM); // 16 / 2 / PPM
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.5f;
        body.createFixture(fixtureDef);
        body.setFixedRotation(true);
        shape.dispose();
        // end of copy paste

        entityType = EntityType.DAMAGE_BALL;
        this.body.setUserData(entityType);
    }


    public static DamageBall getDamageBall(AssetSorter assetSorter, World world, int... coords) {
        Array<Vector2> arrVectors = new Array<>();
        for(int i = 0; i < coords.length; i+=2) arrVectors.add(new Vector2(coords[i] * 32 + 8, (23 - coords[i+1]) * 32 + 8));
        Vector2[] path = arrVectors.toArray(Vector2.class);
        return new DamageBall(assetSorter, world, path);
    }


    /* METHODS */

    public void draw(SpriteBatch batch){
        this.sprite.draw(batch);
    }

    public void update(float delta){
        move(delta);
        posTemp = this.body.getPosition();
        this.sprite.setPosition(posTemp.x * Consts.PPM, posTemp.y * Consts.PPM);
    }

    public void move(float delta){
        // get body vectors
        posTemp = this.getBody().getPosition();
        velTemp = this.getBody().getLinearVelocity();
        // get the direction vector
        nextPoint = (currentPoint + 1) % path.length;
        firstPoint = path[currentPoint];
        secondPoint = path[nextPoint];
        // get rectangles to check if it passed the next point
        bodyRec.x = (posTemp.x * Consts.PPM) - THRESHOLD;
        bodyRec.y = (posTemp.y * Consts.PPM) - THRESHOLD;
        nextPointRec.x = secondPoint.x - THRESHOLD;
        nextPointRec.y = secondPoint.y - THRESHOLD;
        // divide by PPM?!?!?
        currentDirection.x = (secondPoint.x - firstPoint.x) / Consts.PPM;
        currentDirection.y = (secondPoint.y - firstPoint.y) / Consts.PPM;
        // apply force
        if(bodyRec.overlaps(nextPointRec)){ // check if it is in or passed the next point
            // it passed it so increment
            currentPoint += 1;
            currentPoint %= path.length;
            // stop movement, reset speed
            this.getBody().setLinearVelocity(0,0);
        }
        else{
            // it didn't pass it so use direction vector to move it
            this.getBody().setLinearVelocity(currentDirection);
        }
    }




    /* GETTERS AND SETTERS */

    public Body getBody() { return body; }


}
