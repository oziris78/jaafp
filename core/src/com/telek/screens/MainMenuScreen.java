package com.telek.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.*;
import com.telek.*;
import com.telek.jtelek.scene2dUtils.Scene2DUtils;
import com.telek.telekgdx.screens.TScreen;
import com.telek.telekgdx.screens.TScreenUtils;

public class MainMenuScreen extends ScreenAdapter implements TScreen {

    // MAIN GAME OBJECT
    private final Jaafp game;

    // Menu objects
    private Stage stage;
    private Table table;
    private Label title;
    private TextButton btnStart, btnOptions, btnQuit;

    public MainMenuScreen(final Jaafp jaafp) {
        this.game = jaafp;

        stage = new Stage();

        table = new Table(this.game.assetSorter.getResource("skin", Skin.class));
        table.setFillParent(true);
        stage.addActor(table);

        title = new Label("JUST ANOTHER ANNOYING\nFUCKING PLATFORMER", this.game.assetSorter.getResource("skin", Skin.class));
        title.setStyle(new Label.LabelStyle(this.game.assetSorter.getResource("font60", BitmapFont.class), Color.WHITE));
        title.setWrap(true);
        title.setAlignment(Align.center);

        btnStart = new TextButton("START", this.game.assetSorter.getResource("skin", Skin.class));
        btnOptions = new TextButton("OPTIONS", this.game.assetSorter.getResource("skin", Skin.class));
        btnQuit = new TextButton("QUIT", this.game.assetSorter.getResource("skin", Skin.class));

        table.setSize(Jaafp.SCREEN_WIDTH, Jaafp.SCREEN_HEIGHT);
        table.top();
        table.add(title).padTop(Value.percentHeight(0.2f)).row();
        table.add(btnStart).padTop(Value.percentHeight(0.2f)).row();
        table.add(btnOptions).padTop(Value.percentHeight(0.2f)).row();
        table.add(btnQuit).padTop(Value.percentHeight(0.2f)).row();

        Scene2DUtils.changeFont(btnStart, this.game.assetSorter.getResource("font30", BitmapFont.class));
        Scene2DUtils.changeFont(btnOptions, this.game.assetSorter.getResource("font30", BitmapFont.class));
        Scene2DUtils.changeFont(btnQuit, this.game.assetSorter.getResource("font30", BitmapFont.class));


        btnStart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.input.setInputProcessor(null);

                LoadingScreen loadingScreen = game.screenSorter.getScreen("loadingScreen");
                if(!loadingScreen.isWasAlreadyShown())
                    game.setScreen(game.screenSorter.getScreen("loadingScreen"));
                else
                    game.setScreen(game.screenSorter.getScreen("gameScreen"));
            }
        });

        btnOptions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.input.setInputProcessor(null);
                game.setScreen(game.screenSorter.getScreen("optionsScreen"));
            }
        });


        btnQuit.addListener(new ChangeListener() {@Override public void changed(ChangeEvent event, Actor actor) { Gdx.app.exit(); }});
    }

    @Override
    public void configure(){
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

}