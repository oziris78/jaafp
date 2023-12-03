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


package com.twistral.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.Timer;
import com.twistral.entities.enemies.DamageBall;
import com.twistral.entities.enemies.Laser;
import com.twistral.entities.enemies.TimedTile;
import com.twistral.entities.enemies.TrapTile;
import com.twistral.tempest.box2d.Box2DUtils;
import com.twistral.tempest.box2d.TiledParser;
import com.twistral.entities.enemies.Sentry;
import com.twistral.tempest.screensorter.TScreen;
import com.twistral.tempest.utils.ScreenUtils;
import com.twistral.world.EnemyFactory;
import com.twistral.world.OthersFactory;
import com.twistral.Jaafp;
import com.twistral.entities.EntityType;
import com.twistral.entities.others.EndArea;
import com.twistral.entities.player.Player;
import com.twistral.utils.Consts;
import com.twistral.world.WorldListener;

import java.util.*;

public class GameScreen extends ScreenAdapter implements TScreen {



    // MAIN GAME OBJECT FOR SINGLETON
    private final Jaafp game;

    // constants
    public static boolean DEBUG = false;
    public static boolean HACKS = true;

    // scene2d
    Stage stage;
    Table table;
    TextButton goToNext;

    // box2d
    Box2DDebugRenderer b2dr;
    World world;
    Player player;
    DamageBall[] damageBalls;
    TrapTile[] trapTiles;
    TimedTile[] timedTiles;
    Sentry[] sentries;
    EndArea endAreas[];
    LinkedList<Laser> lasers;

    // shaders
    private boolean shadersAreOn = false;
    private final int shakingValue = 1;
    ShaderProgram earthquakeShader;

    // tiled
    OrthogonalTiledMapRenderer tmr;
    TiledMap[] maps;
    TiledMap map1;

    // box2d lights

    // music
    Music gameMusic;


    // garbage vars for performance
    float accumulator = 0;
    Vector3 pos;
    Laser temp = null;


    public GameScreen(final Jaafp game){
        this.game = game;

        // box2d
        Box2D.init();
        Box2DUtils.init(Consts.PPM);
        b2dr = new Box2DDebugRenderer();
        world = new World(new Vector2(0,0), false);
        player = new Player(this.game.assetSorter, world);
        player.setCanMove(true);
        player.setAlive(true);
        world.setContactListener(new WorldListener(lasers, player));
        damageBalls = EnemyFactory.getDamageBalls(this.game.assetSorter, world, player.getCurrentLevel());
        trapTiles = EnemyFactory.getTrapTiles(this.game.assetSorter, player.getCurrentLevel());
        timedTiles = EnemyFactory.getTimedTiles(this.game.assetSorter, player.getCurrentLevel());
        sentries = EnemyFactory.getSentries(this.game.assetSorter, world, player.getCurrentLevel());
        endAreas = OthersFactory.getEndAreas();
        lasers = new LinkedList<>();

        // tiled
        TmxMapLoader mapLoader = new TmxMapLoader();
        map1 = mapLoader.load("tiledMaps/mapLevel1.tmx");
        maps = new TiledMap[]{ map1, map1 , map1 , map1 , map1 };
        tmr = new OrthogonalTiledMapRenderer(map1, this.game.batch);
        for(TiledMap m : maps)
            TiledParser.parseTiledObjectLayer(world, m.getLayers().get("collisionLayer1").getObjects(), EntityType.WALL);


        // scene2d
        stage = new Stage();
        table = new Table(this.game.assetSorter.getResource("skin", Skin.class));
        goToNext = new TextButton("GO TO NEXT LEVEL", this.game.assetSorter.getResource("skin", Skin.class));
        goToNext.getLabel().setStyle(new Label.LabelStyle(
                this.game.assetSorter.getResource("font30", BitmapFont.class), Color.WHITE)
        );
        goToNext.setWidth(goToNext.getWidth() * 3f);
        table.setFillParent(true);
        goToNext.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor){
                deactivateStage();
            }
        });


        // box2d lights


        // music
        this.gameMusic = this.game.assetSorter.getResource("GAME_SCREEN", "pianoMusic", Music.class);
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.2f);
        if(this.game.musicShouldPlay) startOrStopGameMusic();

        // shaders
        ShaderProgram.pedantic = false;
        earthquakeShader = new ShaderProgram(Gdx.files.internal("shaders/earthquake.vsh"), Gdx.files.internal("shaders/earthquake.fsh"));

    }


    private void startOrStopGameMusic() {
        if(!gameMusic.isPlaying()) gameMusic.play();
        else gameMusic.stop();
    }


    private void activateShaders(){
        earthquakeShader.bind();
        earthquakeShader.setUniformf("u_distort", MathUtils.random(shakingValue),MathUtils.random(shakingValue),0);

        if(shadersAreOn) return;

        this.game.batch.setShader(earthquakeShader);
        tmr.getBatch().setShader(earthquakeShader);
        stage.getBatch().setShader(earthquakeShader);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                resetShaders();
            }
        }, player.getDyingAnimation().getAnimationDuration());
        shadersAreOn = !shadersAreOn;
    }

    private void resetShaders(){
        this.game.batch.setShader(null);
        tmr.getBatch().setShader(null);
        stage.getBatch().setShader(null);
        shadersAreOn = !shadersAreOn;
    }


    public void deactivateStage(){
        player.ableItToProgress();
        Gdx.input.setInputProcessor(null);
        stage.clear();
        player.resetPosition();
        player.update(Gdx.graphics.getDeltaTime(),endAreas,stage,table,goToNext,tmr,maps);
        player.setCanMove(true);
        player.setAlive(true);
        player.setIsStageShown(false);
        loadCurrentLevel();
        clearLasers();
    }


    private void loadCurrentLevel() {
        clearLasers();
        damageBalls = com.twistral.world.EnemyFactory.getDamageBalls(this.game.assetSorter, world, player.getCurrentLevel());
        trapTiles = com.twistral.world.EnemyFactory.getTrapTiles(this.game.assetSorter, player.getCurrentLevel());
        timedTiles = com.twistral.world.EnemyFactory.getTimedTiles(this.game.assetSorter, player.getCurrentLevel());
        sentries = com.twistral.world.EnemyFactory.getSentries(this.game.assetSorter, world, player.getCurrentLevel());
    }

    private void clearLasers(){
        if(this.lasers != null) this.lasers.clear();
    }


    @Override
    public void update(float delta) {
        player.move();
        for(com.twistral.entities.enemies.DamageBall ball : damageBalls) ball.update(delta);
        for(com.twistral.entities.enemies.TrapTile trapTile : trapTiles) trapTile.update(lasers, player, delta);
        for(com.twistral.entities.enemies.TimedTile timedTile : timedTiles) timedTile.update(lasers, player, delta);
        for(com.twistral.entities.enemies.Laser laser : lasers) laser.update(lasers, player, delta);
        for(Iterator<com.twistral.entities.enemies.Laser> iterator = lasers.iterator(); iterator.hasNext(); ){
            temp = iterator.next();
            if(temp.shouldBeRemoved()) iterator.remove();
        }
        for(com.twistral.entities.enemies.Sentry sentry : sentries) sentry.update(lasers, player, delta);

        doOneStep(this.world, delta);
        player.update(delta, endAreas, stage, table, goToNext, tmr, maps);
        updateCam();

        if(!player.canMove() && player.isStageIsShown()) stage.act(delta);
    }

    private void updateCam() {
        pos = this.game.camera.position;
        pos.x = this.game.camera.position.x + (player.getSprite().getX() - this.game.camera.position.x) * Consts.LERP;
        pos.y = this.game.camera.position.y + (player.getSprite().getY() - this.game.camera.position.y) * Consts.LERP;
        this.game.camera.position.set(pos);
        this.game.camera.update();
        tmr.setView(this.game.camera);
    }



    @Override
    public void render(float delta) {
        super.render(delta);
        ScreenUtils.clear();

        update(delta);
        if(Gdx.input.isKeyJustPressed(Input.Keys.Y)) DEBUG = !DEBUG;

        if(DEBUG){
            this.game.camera.position.set(0,0,0);
            if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
                this.game.camera.zoom += 0.05f;
            if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
                this.game.camera.zoom -= 0.05f;
            b2dr.render(world, this.game.camera.combined);
            return;
        }



        if(!player.isAlive()) activateShaders();

        tmr.render();

        this.game.batch.begin();

        if(player.canMove()) {
            for(com.twistral.entities.enemies.TimedTile timedTile : timedTiles) timedTile.draw(this.game.batch);
            player.draw(this.game.batch);
            for(com.twistral.entities.enemies.TrapTile trapTile : trapTiles) trapTile.draw(this.game.batch);
            for(com.twistral.entities.enemies.DamageBall ball : damageBalls) ball.draw(this.game.batch);
            for(Sentry sentry : sentries) sentry.draw(this.game.batch);
            for(com.twistral.entities.enemies.Laser laser : lasers) laser.draw(this.game.batch);
        }
        else {
            Gdx.gl20.glEnable(GL20.GL_BLEND);
            Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            this.game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            this.game.shapeRenderer.setColor(new Color(0, 0, 0, 0.8f));
            this.game.shapeRenderer.rect(0, 0, this.game.SCREEN_WIDTH, this.game.SCREEN_HEIGHT);
            this.game.shapeRenderer.end();
            Gdx.gl20.glDisable(GL20.GL_BLEND);
            stage.draw();

        }

        this.game.batch.end();

        checkIfGameHasEnded(player);
    }


    private void checkIfGameHasEnded(Player player) {
        if(!player.isGameHasEnded()) return;
        player.resetPlayer(tmr, maps);
        Gdx.input.setInputProcessor(null);
        Gdx.gl20.glClearColor(0,0,0,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.game.setScreen(game.screenSorter.getScreen("endingScreen", game));
    }




    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        game.updateMainCameras(width, height);
        stage.getViewport().update(width, height, true);
        tmr.setView(this.game.camera);

    }

    @Override
    public void dispose() {
        super.dispose();
         stage.dispose();
         b2dr.dispose();
         world.dispose();
         tmr.dispose();
         map1.dispose();
    }

    @Override public void show() { super.show(); }
    @Override public void hide() { super.hide(); }
    @Override public void pause() { super.pause(); }
    @Override public void resume() { super.resume(); }

    private void doOneStep(World world, float delta) {
        accumulator += Math.min(delta, 0.25f);
        while (accumulator >= Consts.TIME_STEP) {
            world.step(Consts.TIME_STEP, Consts.VELOCITY_ITERATION, Consts.POSITION_ITERATION);
            accumulator -= Consts.TIME_STEP;
        }
    }

    @Override
    public void configure() {
        loadCurrentLevel();
        // game screen doesn't have any stages etc. it only uses isKeyJustPressed() etc. methods
        // so this method is blank
    }



}
