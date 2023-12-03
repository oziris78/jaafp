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


package com.twistral.entities.player;

import com.badlogic.gdx.*;
import static com.badlogic.gdx.Input.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import static com.twistral.utils.Consts.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;
import com.twistral.entities.EntityType;
import com.twistral.entities.enemies.Laser;
import com.twistral.tempest.assetsorter.*;
import com.twistral.tempest.assetsorter.AssetSorter;
import com.twistral.tempest.box2d.BodyFactory;
import com.twistral.entities.others.EndArea;
import com.twistral.screens.GameScreen;
import com.twistral.utils.TSound;


import java.util.LinkedList;

public class Player {

    public static final int PLAYER_WIDTH = 16;
    public static final int PLAYER_HEIGHT = 16;
    public static final float DYING_ANIM_TOTAL_TIME = 1.6f;

    private final float DENSITY = 0.05f;
    private final float FRICTION = 0.05f;
    private final float RESTITUTION = 0.05f;

    private final Vector2 startingPosition, endingPosition;
    private final Rectangle startingArea;
    private static Rectangle STARTING_AREA;

    public com.twistral.entities.EntityType entityType;

    private boolean gameHasEnded;
    private float MAX_SPEED = 8f;
    private float SPEED_INC = 1f;
    private int currentLevel = 1;

    private Body body;
    private Texture texture;
    private Sprite sprite;
    private Animation<TextureRegion> dyingAnimation;
    private boolean canMove = false;
    private boolean stageIsShown = false;
    private boolean isAlive = false;
    private float dyingTime = 0f;

    private Sound dyingSound;
    private com.twistral.utils.TSound dyingTSound;
    private Sound  winningTheLevelSound;
    private com.twistral.utils.TSound winningTheLevelTSound;

    // garbage vars
    private Vector2 vel;
    private Vector2 pos;
    private boolean canProgress = true;


    public Player(AssetSorter assetSorter, World world){
        this.texture = assetSorter.getResource("GAME_SCREEN", "playerTexture", Texture.class);
        this.sprite = new Sprite(this.texture);

        this.dyingSound = assetSorter.getResource("GAME_SCREEN", "dyingSound", Sound.class);
        this.dyingTSound = new com.twistral.utils.TSound(this.dyingSound);

        this.winningTheLevelSound = assetSorter.getResource("GAME_SCREEN", "winningTheLevelSound", Sound.class);
        this.winningTheLevelTSound = new TSound(this.winningTheLevelSound);

        this.startingPosition = new Vector2(10 * 32 - this.PLAYER_WIDTH / 2, 8 * 32 - this.PLAYER_HEIGHT / 2);
        this.startingArea = new Rectangle(9 * 32, (23-16) * 32, 2 * 32, 2 * 32);
        STARTING_AREA = new Rectangle(9 * 32, (23-16) * 32, 2 * 32, 2 * 32);
        this.endingPosition = new Vector2(19 * 32 - this.PLAYER_WIDTH / 2, (23-8) * 32 - this.PLAYER_HEIGHT / 2);
        this.dyingAnimation = DyingAnimation.getAnimation(assetSorter);
        this.gameHasEnded = false;

        this.entityType = EntityType.PLAYER;
        this.body = BodyFactory.createBodyAsBox(world, BodyDef.BodyType.DynamicBody, true, this.PLAYER_WIDTH,
                this.PLAYER_HEIGHT, (int) startingPosition.x, (int) startingPosition.y,
                this.DENSITY, this.FRICTION, this.RESTITUTION, MAX_SPEED, 0f,
                false, (short) 0, (short) 0, (short) 0, entityType, false);
    }

    public void update(float delta, com.twistral.entities.others.EndArea[] endAreas,
                       Stage stage, Table table, TextButton goToNext,
                       OrthogonalTiledMapRenderer tmr, TiledMap[] maps)
    {

        if(!isAlive) {
            dyingTime += delta;
            return;
        }
        pos = this.body.getPosition();
        this.sprite.setPosition(pos.x * PPM, pos.y * PPM);
        this.checkIfInEndingArea(endAreas,stage,table,goToNext, tmr, maps);
    }


    public void draw(SpriteBatch batch){
        if(!this.isAlive){
            batch.draw( dyingAnimation.getKeyFrame(dyingTime, true), this.sprite.getX(), this.sprite.getY() );
            if(dyingTime >= dyingAnimation.getAnimationDuration()) {
                dyingTime = 0f;
                this.setAlive(true);
                this.resetPosition();
            }
            return;
        }
        if(this.canMove) this.sprite.draw(batch);
    }


    /* FUNCTIONS */

    private boolean timeout = false;

    public void checkIfInEndingArea(EndArea[] endAreas, Stage stage, Table table, TextButton goToNext,
                                    OrthogonalTiledMapRenderer tmr, TiledMap[] maps)
    {
        if(timeout) return;
        if(!canProgress) return;
        if(endAreas[this.getCurrentLevel()-1].playerEnteredArea(this) && canProgress){
            canProgress = false;
            winningTheLevelTSound.play(0.1f);
            if(this.getCurrentLevel() == 5){
                gameHasEnded = true;
                return;
            }
            timeout = true;
            this.setCanMove(false);
            this.setIsStageShown(true);
            this.setCurrentLevel(this.getCurrentLevel() + 1);
            activateStage(stage,table,goToNext, tmr, maps);
            Timer.schedule( new Timer.Task() {@Override public void run() { timeout = false; }},  3);
        }
    }

    public void ableItToProgress(){
        canProgress = true;
    }

    public void setIsStageShown(boolean b) {
        this.stageIsShown = b;
    }


    public void activateStage(Stage stage, Table table, TextButton goToNext, OrthogonalTiledMapRenderer tmr, TiledMap[] maps){
        stage.addActor(table);
        table.add(goToNext).width(Value.percentWidth(1f)).height(Value.percentWidth(0.35f));
        Gdx.input.setInputProcessor(stage);
        changeLevel(tmr, maps);
    }

    public void changeLevel(OrthogonalTiledMapRenderer tmr, TiledMap[] maps){
        tmr.setMap( maps[this.getCurrentLevel() - 1] );
    }

    public void move(){
        if(!canMove) return;
        if(Gdx.input.isKeyJustPressed(Keys.H)) { teleportToTheEnd(); return;}

        vel = this.getBody().getLinearVelocity();
        pos = this.getBody().getPosition();

        MAX_SPEED = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) ? 10f : 8f;
        SPEED_INC = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) ? 1.25f : 1f;

        if(Gdx.input.isKeyPressed(Keys.SPACE)) { this.resetSpeed(); return; }

        if(Gdx.input.isKeyPressed(Keys.A) && vel.x > -MAX_SPEED) this.body.applyLinearImpulse(-SPEED_INC, 0, pos.x, pos.y, true);
        if(Gdx.input.isKeyPressed(Keys.D) && vel.x < MAX_SPEED)  this.body.applyLinearImpulse(SPEED_INC, 0,  pos.x, pos.y, true);
        if(Gdx.input.isKeyPressed(Keys.W) && vel.y < MAX_SPEED)  this.body.applyLinearImpulse(0, SPEED_INC,  pos.x, pos.y, true);
        if(Gdx.input.isKeyPressed(Keys.S) && vel.y > -MAX_SPEED) this.body.applyLinearImpulse(0, -SPEED_INC, pos.x, pos.y, true);

    }

    public boolean isInStartingArea(){
        return startingArea.overlaps(this.getSprite().getBoundingRectangle());
    }


    public static boolean isInStartingArea(Rectangle r){
        return r.overlaps(STARTING_AREA);
    }

    public void resetSpeed() { this.body.setLinearVelocity(0, 0); }

    /* GETTERS AND SETTERS */

    public int getCurrentLevel() { return this.currentLevel; }
    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }
    public Body getBody() { return this.body; }
    public Sprite getSprite() { return this.sprite; }
    public boolean isStageIsShown() { return this.stageIsShown; }
    public void setAlive(boolean b) { this.isAlive = b; }
    public boolean isAlive() { return this.isAlive; }
    public boolean canMove() { return canMove; }
    public void setCanMove(boolean canMove) { this.canMove = canMove; }
    public Animation<TextureRegion> getDyingAnimation() { return dyingAnimation; }


    public void resetPosition() {
        this.getBody().setTransform(startingPosition.x / PPM, startingPosition.y / PPM, this.getBody().getAngle());
    }

    public void teleportToTheEnd(){
        if(GameScreen.HACKS) this.getBody().setTransform(endingPosition.x / PPM, endingPosition.y / PPM, this.getBody().getAngle());
    }

    public void die(final LinkedList<Laser> lasers) {
        this.dyingTSound.play(this.dyingAnimation.getAnimationDuration(), 0.35f);
        isAlive = false;
        clearLasersInstantly(lasers);
    }



    public void clearLasersInstantly(LinkedList<Laser> lasers){
        if(lasers != null && lasers.size() != 0)
            lasers.clear();
    }

    public boolean isGameHasEnded() { return gameHasEnded; }
    public void setGameHasEnded(boolean gameHasEnded) { this.gameHasEnded = gameHasEnded; }

    public float getDyingTime() {
        return this.dyingTime;
    }

    public void resetPlayer(OrthogonalTiledMapRenderer tmr, TiledMap[] maps){
        setGameHasEnded(false);
        setCurrentLevel(1);
        resetPosition();
        resetSpeed();
        changeLevel(tmr, maps);
    }


}
