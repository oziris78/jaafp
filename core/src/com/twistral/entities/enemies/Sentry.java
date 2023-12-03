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

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.twistral.tempest.assetsorter.*;
import com.twistral.tempest.box2d.BodyFactory;
import com.twistral.entities.EntityType;
import com.twistral.entities.player.Player;
import com.twistral.utils.Consts;
import com.twistral.utils.TSound;

import java.util.LinkedList;


public class Sentry {

    private static final int SENTRY_WIDTH = 16;
    private static final int SENTRY_HEIGHT = 16;

    private AssetSorter assetSorter;
    private Texture texture;
    private Sprite sprite;
    private Sound laserSound;
    private com.twistral.utils.TSound laserTSound;
    private Body body;
    private com.twistral.entities.EntityType entityType;
    protected float secondsForTile;
    private float timer;
    private float firingDelay;
    private float currentAngle;

    // garbage variables
    Vector2 laserStart, sentryPos, playerPos = Vector2.Zero, direction = Vector2.Zero;


    public Sentry(AssetSorter assetSorter, World world, int mapRownum, int mapColNum, float firingDelay, float secondsForTile){
        this.assetSorter = assetSorter;
        this.texture = assetSorter.getResource("GAME_SCREEN", "sentryTexture", Texture.class);
        this.sprite = new Sprite(this.texture);
        this.laserSound = assetSorter.getResource("GAME_SCREEN", "laserSound", Sound.class);
        this.laserTSound = new TSound(this.laserSound);
        this.firingDelay = firingDelay;
        this.secondsForTile = secondsForTile;
        this.timer = 0;
        this.entityType = EntityType.SENTRY;
        this.body = BodyFactory.createBodyAsBox(world, BodyDef.BodyType.StaticBody, false, SENTRY_WIDTH, SENTRY_HEIGHT,
                mapRownum * 32 + SENTRY_WIDTH/2, (23-mapColNum) * 32 + SENTRY_HEIGHT/2, 0.5f, 0.5f, 0.5f, 0f, 0f,
                false, (short) 0, (short) 0, (short) 0, entityType, false);

        this.sprite.setPosition(this.body.getPosition().x * Consts.PPM, this.body.getPosition().y * Consts.PPM);
        this.laserStart = new Vector2(this.sprite.getX() + this.sprite.getWidth()/2, this.sprite.getY() + this.sprite.getHeight()/2);
    }


    /*  METHODS  */

    public void update(final LinkedList<Laser> lasers, Player player, float delta){
        this.timer += delta;

        // getting the positions
        sentryPos = this.body.getPosition();
        playerPos = player.getBody().getPosition();

        // setting the position
        this.sprite.setPosition(sentryPos.x * Consts.PPM, sentryPos.y * Consts.PPM);

        // getting the angle
        direction = playerPos.cpy().sub(sentryPos);
        currentAngle = MathUtils.radDeg * MathUtils.atan2(direction.y, direction.x);

        // setting the angle
        this.sprite.setRotation(currentAngle + 90f);

        // firing a laser
        if( this.canFire() && !player.isInStartingArea() && player.getDyingTime() == 0.0f){
            laserTSound.play(this.firingDelay, 0.2f);
            lasers.add(new Laser(assetSorter, this));
            this.timer = 0;
        }
    }


    private boolean canFire(){
        return this.timer >= this.firingDelay;
    }

    public void draw(SpriteBatch batch){
        this.sprite.draw(batch);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Body getBody() {
        return body;
    }

    public float getCurrentAngle() { return currentAngle; }


}

