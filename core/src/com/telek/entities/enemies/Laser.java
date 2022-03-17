package com.telek.entities.enemies;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.telek.entities.player.Player;
import com.telek.telekgdx.assets.*;

import java.util.LinkedList;

import static com.telek.utils.Consts.PPM;

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
        this.sprite.setRotation(sentry.getSprite().getRotation() - 45);
        this.sprite.setPosition(
                sentryPos.x * PPM + sentry.getSprite().getWidth()/4,
                sentryPos.y * PPM + sentry.getSprite().getHeight()/4
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


    public void update(LinkedList<Laser> lasers, final Player player, float delta){
        moveLaser(delta);

        checkIfPlayerIsDead(lasers, player);

        removeIfOutside();
        removeIfInsideStartingArea();
    }


    private void removeIfInsideStartingArea() {
        if(Player.isInStartingArea(this.sprite.getBoundingRectangle()))
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

