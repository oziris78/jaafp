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
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.*;
import com.twistral.tempest.utils.ScreenUtils;
import com.twistral.tempest.screensorter.TScreen;
import com.twistral.Jaafp;


public class MainMenuScreen extends ScreenAdapter implements TScreen {

    // MAIN GAME OBJECT
    private final com.twistral.Jaafp game;

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

        table.setSize(com.twistral.Jaafp.SCREEN_WIDTH, Jaafp.SCREEN_HEIGHT);
        table.top();
        table.add(title).padTop(Value.percentHeight(0.2f)).row();
        table.add(btnStart).padTop(Value.percentHeight(0.2f)).row();
        table.add(btnOptions).padTop(Value.percentHeight(0.2f)).row();
        table.add(btnQuit).padTop(Value.percentHeight(0.2f)).row();

        BitmapFont font = this.game.assetSorter.getResource("font30", BitmapFont.class);
        btnStart.getLabel().setStyle(new Label.LabelStyle(font, Color.WHITE));
        btnOptions.getLabel().setStyle(new Label.LabelStyle(font, Color.WHITE));
        btnQuit.getLabel().setStyle(new Label.LabelStyle(font, Color.WHITE));


        btnStart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.input.setInputProcessor(null);

                LoadingScreen loadingScreen = game.screenSorter.getScreen("loadingScreen", game);
                if(!loadingScreen.isWasAlreadyShown())
                    game.setScreen(game.screenSorter.getScreen("loadingScreen", game));
                else
                    game.setScreen(game.screenSorter.getScreen("gameScreen", game));
            }
        });

        btnOptions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.input.setInputProcessor(null);
                game.setScreen(game.screenSorter.getScreen("optionsScreen", game));
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
        ScreenUtils.clear();
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