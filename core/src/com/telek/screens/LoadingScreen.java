package com.telek.screens;


import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.*;
import com.telek.*;
import com.telek.telekgdx.screens.TScreen;
import com.telek.telekgdx.screens.TScreenUtils;


public class LoadingScreen extends ScreenAdapter implements TScreen {


    // MAIN GAME OBJECT
    private final Jaafp game;

    // Menu objects
    private Stage stage;
    private Table table;
    private Label loadingPercent;
    private TextButton btnStart;

    private boolean wasAlreadyShown = false;

    public LoadingScreen(final Jaafp jaafp) {
        this.game = jaafp;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table(this.game.assetSorter.getResource("skin", Skin.class));
        table.setFillParent(true);
        stage.addActor(table);

        loadingPercent = new Label("0 %", this.game.assetSorter.getResource("skin", Skin.class));
        loadingPercent.setStyle(new Label.LabelStyle(this.game.assetSorter.getResource("font60", BitmapFont.class), Color.WHITE));
        loadingPercent.setWrap(true);
        loadingPercent.setAlignment(Align.center);
        table.add(loadingPercent).growX().height(Value.percentWidth(0.3f));
        table.row();

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                startLoading();
            }
        }, 0.1f);

    }

    private void startLoading() {
        while(!game.assetSorter.update("GAME_SCREEN")){
            loadingPercent.setText(String.valueOf(game.assetSorter.getPercentage() * 100f));
        }
        loadingPercent.setText("100 %");
        createButtonAndAddIt();
        wasAlreadyShown = true;
    }


    private void createButtonAndAddIt(){
        btnStart = new TextButton("START", this.game.assetSorter.getResource("skin", Skin.class));
        float scale = 3f;
        table.add(btnStart).width(btnStart.getWidth() * scale).height(btnStart.getHeight() * scale);
        btnStart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.input.setInputProcessor(null);
                game.setScreen(game.screenSorter.getScreen("gameScreen"));
            }
        });
    }


    @Override
    public void configure() {
        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        TScreenUtils.clearScreen();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        game.updateMainCameras(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
    }

    public boolean isWasAlreadyShown() {
        return wasAlreadyShown;
    }

}




