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


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.twistral.tempest.assetsorter.*;
import com.twistral.entities.player.Player;
import com.twistral.utils.Consts;

import java.util.LinkedList;

public class Laser {

    private static final int LASER_WIDTH = 8, LASER_HEIGHT = 4;

    private Texture texture;
    private Sprite sprite;
    private boolean isInMap;
    private float speed;

    // garbage vars
    private float x,y;

    public Laser(AssetSorter assetSorter, Sentry sentry){
        Vector2 sentryPos = sentry.getBody().getPosition();

        this.texture = assetSorter.getResource("GAME_SCREEN", "laserTexture", Texture.class);
        this.sprite = new Sprite(this.texture);
        this.sprite.setRotation(sentry.getCurrentAngle());
        this.sprite.setPosition(
                sentryPos.x * Consts.PPM + sentry.getSprite().getWidth()/4,
                sentryPos.y * Consts.PPM + sentry.getSprite().getHeight()/4
        );

        this.isInMap = true;
        this.speed = 32 * sentry.secondsForTile;
    }

    private void moveLaser(float delta){

        x = this.sprite.getX() + (float) ( this.speed / 60 * Math.cos(this.sprite.getRotation() * MathUtils.degreesToRadians) );
        y = this.sprite.getY() + (float) ( this.speed / 60 * Math.sin(this.sprite.getRotation() * MathUtils.degreesToRadians) );

        this.sprite.setPosition(x,y);
    }


    private void removeIfOutside() {
        x = this.sprite.getX();
        y = this.sprite.getY();
        if(x < 0 || y < 0 || x > 29 * 32 || y > 23 * 32)
            isInMap = false;
    }


    public void update(LinkedList<Laser> lasers, final com.twistral.entities.player.Player player, float delta){
        moveLaser(delta);

        checkIfPlayerIsDead(lasers, player);

        removeIfOutside();
        removeIfInsideStartingArea();
    }


    private void removeIfInsideStartingArea() {
        if(com.twistral.entities.player.Player.isInStartingArea(this.sprite.getBoundingRectangle()))
            isInMap = false;
    }


    private void checkIfPlayerIsDead(LinkedList<Laser> lasers, Player player) {
        if(this.sprite.getBoundingRectangle().overlaps(player.getSprite().getBoundingRectangle())){
            if( player.getDyingTime() == 0.0f ) player.die(lasers);
        }
    }


    public void draw(SpriteBatch batch){
        this.sprite.draw(batch);
    }

    public boolean shouldBeRemoved(){
        return !isInMap;
    }

}

