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
import com.twistral.Jaafp;
import com.twistral.tempest.screensorter.TScreen;
import com.twistral.tempest.utils.ScreenUtils;


public class EndingScreen extends ScreenAdapter implements TScreen {

    // MAIN GAME OBJECT
    private final Jaafp game;

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
        ScreenUtils.clear();
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



