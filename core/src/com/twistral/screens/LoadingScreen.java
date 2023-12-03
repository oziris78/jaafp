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
import com.twistral.tempest.screensorter.TScreen;
import com.twistral.Jaafp;
import com.twistral.tempest.utils.ScreenUtils;


public class LoadingScreen extends ScreenAdapter implements TScreen {


    // MAIN GAME OBJECT
    private final com.twistral.Jaafp game;

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
        btnStart.getLabel().setStyle(new Label.LabelStyle(this.game.assetSorter.getResource("font30", BitmapFont.class), Color.WHITE));
        float scale = 3f;
        table.add(btnStart).width(btnStart.getWidth() * scale).height(btnStart.getHeight() * scale);
        btnStart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.input.setInputProcessor(null);
                game.setScreen(game.screenSorter.getScreen("gameScreen", game));
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

    public boolean isWasAlreadyShown() {
        return wasAlreadyShown;
    }

}




