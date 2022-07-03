package com.twistral.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.*;
import com.twistral.toriagdx.screens.TScreen;
import com.twistral.toriagdx.screens.TScreenUtils;
import com.twistral.Jaafp;


public class EndingScreen extends ScreenAdapter implements TScreen {

    // MAIN GAME OBJECT
    private final com.twistral.Jaafp game;

    // Menu objects
    private Stage stage;
    private Table table;
    private Label title;
    private TextButton goToMainMenu;


    public EndingScreen(final Jaafp jaafp) {
        this.game = jaafp;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table(this.game.assetSorter.getResource("skin", Skin.class));
        table.setFillParent(true);
        stage.addActor(table);

        title = new Label("Congratulations, You have just finished JAAFP!", this.game.assetSorter.getResource("skin", Skin.class));
        title.setStyle(new Label.LabelStyle(this.game.assetSorter.getResource("font60", BitmapFont.class), Color.WHITE));
        title.setWrap(true);
        title.setAlignment(Align.center);
        table.add(title).growX().height(Value.percentWidth(0.3f));
        table.row();

        goToMainMenu = new TextButton("GO TO MAIN MENU", this.game.assetSorter.getResource("skin", Skin.class));
        goToMainMenu.getLabel().setStyle(new Label.LabelStyle(
                this.game.assetSorter.getResource("font30", BitmapFont.class), Color.WHITE)
        );

        table.add(goToMainMenu).growX().pad(Value.percentWidth(0f), Value.percentWidth(0.4f), Value.percentWidth(0f),
                Value.percentWidth(0.4f)).height(Value.percentWidth(0.1f)).row();

        goToMainMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.input.setInputProcessor(null);
                game.setScreen(game.screenSorter.getScreen("mainMenuScreen", game));
            }
        });

    }

    @Override
    public void update(float delta) {
        // empty for now
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        TScreenUtils.clearScreen();
        stage.act(delta);
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

    @Override
    public void configure() {
        Gdx.input.setInputProcessor(stage);
    }



}



