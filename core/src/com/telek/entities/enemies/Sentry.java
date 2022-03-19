package com.telek.entities.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.telek.entities.*;
import com.telek.entities.player.*;
import com.telek.telekgdx.assets.*;
import com.telek.telekgdx.box2d.BodyFactory;
import com.telek.utils.TSound;

import java.util.LinkedList;

import static com.telek.utils.Consts.PPM;



public class Sentry {

    private static final int SENTRY_WIDTH = 16;
    private static final int SENTRY_HEIGHT = 16;

    private static boolean OTHER_SENTRY_HAS_SHOT;

    private AssetSorter assetSorter;
    private Texture texture;
    private Sprite sprite;
    private Sound laserSound;
    private TSound laserTSound;
    private Body body;
    private EntityType entityType;
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
        OTHER_SENTRY_HAS_SHOT = false;
        this.firingDelay = firingDelay;
        this.secondsForTile = secondsForTile;
        this.timer = 0;
        this.entityType = EntityType.SENTRY;
        this.body = BodyFactory.createBodyAsBox(world, BodyDef.BodyType.StaticBody, false, SENTRY_WIDTH, SENTRY_HEIGHT,
                mapRownum * 32 + SENTRY_WIDTH/2, (23-mapColNum) * 32 + SENTRY_HEIGHT/2, 0.5f, 0.5f, 0.5f, 0f, 0f,
                false, (short) 0, (short) 0, (short) 0, entityType, false);

        this.sprite.setPosition(this.body.getPosition().x * PPM, this.body.getPosition().y * PPM);
        this.laserStart = new Vector2(this.sprite.getX() + this.sprite.getWidth()/2, this.sprite.getY() + this.sprite.getHeight()/2);
    }


    /*  METHODS  */

    public void update(final LinkedList<Laser> lasers, Player player, float delta){
        this.timer += delta;

        // getting the positions
        sentryPos = this.body.getPosition();
        playerPos = player.getBody().getPosition();

        // setting the position
        this.sprite.setPosition(sentryPos.x * PPM, sentryPos.y * PPM);

        // getting the angle
        direction = playerPos.cpy().sub(sentryPos);
        currentAngle = MathUtils.radDeg * MathUtils.atan2(direction.y, direction.x);

        // setting the angle
        this.sprite.setRotation(currentAngle + 90f);

        // firing a laser
        if( this.canFire() && !player.isInStartingArea() && player.getDyingTime() == 0.0f){
            playLaserSound();
            lasers.add(new Laser(assetSorter, this));
            this.timer = 0;
        }
    }

    private void playLaserSound(){
        if(OTHER_SENTRY_HAS_SHOT) return;
        laserTSound.play(this.firingDelay, 0.2f);
        OTHER_SENTRY_HAS_SHOT = true;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                OTHER_SENTRY_HAS_SHOT = !OTHER_SENTRY_HAS_SHOT;
            }
        }, this.firingDelay/2);
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


