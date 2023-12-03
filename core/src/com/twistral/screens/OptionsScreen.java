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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.twistral.Jaafp;
import com.twistral.tempest.screensorter.TScreen;
import com.twistral.tempest.utils.ScreenUtils;

public class OptionsScreen extends ScreenAdapter implements TScreen {

    // MAIN GAME OBJECT
    private final Jaafp game;

    // settings
    private String currentRes;
    private boolean currentFullscreen;
    private boolean increaseCloseMusicFont = true;

    // Menu objects
    private Stage stage;
    private Table table, resTable;
    private Label label;
    private TextButton btnCloseMusic, btnSave, btn1024_768, btn1280_1024, btn1366_768, btn1600_900, btn1920_1080, btn1920_1200, btnFullscreen, btnBack;

    public OptionsScreen(final Jaafp jaafp) {
        this.game = jaafp;
        currentRes = String.format("%dx%d", Jaafp.SCREEN_WIDTH, Jaafp.SCREEN_HEIGHT);
        currentFullscreen = Jaafp.WILL_BE_FULLSCREEN;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table(this.game.assetSorter.getResource("skin", Skin.class));
        table.setFillParent(true);
        resTable = new Table(this.game.assetSorter.getResource("skin", Skin.class));
        stage.addActor(table);

        label = new Label("", this.game.assetSorter.getResource("skin", Skin.class));
        setTextToLabel();
        label.setStyle(new Label.LabelStyle(this.game.assetSorter.getResource("font60", BitmapFont.class), Color.WHITE));
        label.setWrap(true);
        label.setAlignment(Align.center);

        btn1024_768 = new TextButton("1024 x 768", this.game.assetSorter.getResource("skin", Skin.class));
        btn1280_1024 = new TextButton("1280 x 1024", this.game.assetSorter.getResource("skin", Skin.class));
        btn1366_768 = new TextButton("1366 x 768", this.game.assetSorter.getResource("skin", Skin.class));
        btn1600_900 = new TextButton("1600 x 900", this.game.assetSorter.getResource("skin", Skin.class));
        btn1920_1080 = new TextButton("1920 x 1080", this.game.assetSorter.getResource("skin", Skin.class));
        btn1920_1200 = new TextButton("1920 x 1200", this.game.assetSorter.getResource("skin", Skin.class));
        btnFullscreen = new TextButton(currentFullscreen ? "Fullscreen is ON" : "Fullscreen is OFF", this.game.assetSorter.getResource("skin", Skin.class));
        btnSave = new TextButton("APPLY OPTIONS", this.game.assetSorter.getResource("skin", Skin.class));
        btnBack = new TextButton("BACK", this.game.assetSorter.getResource("skin", Skin.class));
        btnCloseMusic = new TextButton(game.musicShouldPlay ? "MUSIC IS ON" : "MUSIC IS OFF", this.game.assetSorter.getResource("skin", Skin.class));

        table.top().padTop(Value.percentHeight(0.2f));
        table.add(label).padBottom(Value.percentHeight(0.5f)).row();
        resTable.add(btn1024_768);
        resTable.add(btn1280_1024);
        resTable.add(btn1366_768).row();
        resTable.add(btn1600_900);
        resTable.add(btn1920_1080);
        resTable.add(btn1920_1200);
        table.add(resTable).padBottom(Value.percentHeight(0.5f)).row();
        table.add(btnFullscreen).padBottom(Value.percentHeight(0.5f)).row();
        table.add(btnCloseMusic).padBottom(Value.percentHeight(0.5f)).row();
        table.add(btnSave).padBottom(Value.percentHeight(0.5f)).row();
        table.add(btnBack).padBottom(Value.percentHeight(0.5f)).row();

        BitmapFont font30 = this.game.assetSorter.getResource("font30", BitmapFont.class);
        label.getStyle().font = font30;

        TextButton[] buttons = new TextButton[]{ btnCloseMusic, btnSave, btn1024_768, btn1280_1024, btn1366_768,
                btn1600_900, btn1920_1080, btn1920_1200, btnFullscreen, btnBack };
        for(TextButton btn : buttons)
            btn.getLabel().setStyle(new Label.LabelStyle(this.game.assetSorter.getResource("font20", BitmapFont.class), Color.WHITE));


        setupListeners();
    }


    private void setupListeners(){
        btnSave.addListener(new ChangeListener() { @Override public void changed(ChangeEvent event, Actor actor) {
            String whString = currentRes.replaceAll(" ", "");
            int indexOfX = whString.indexOf("x");
            int newWidth = Integer.parseInt( whString.substring(0, indexOfX) );
            int newHeight = Integer.parseInt( whString.substring(indexOfX+1) );

            int oldWidth = game.preferences.getInteger("SCREEN_WIDTH", 1024);
            int oldHeight = game.preferences.getInteger("SCREEN_HEIGHT", 768);
            boolean isFullscreen = game.preferences.getBoolean("IS_FULLSCREEN", false);

            game.preferences.putInteger("SCREEN_WIDTH", newWidth);
            game.preferences.putInteger("SCREEN_HEIGHT", newHeight);
            game.preferences.putBoolean("IS_FULLSCREEN", currentFullscreen);
            game.preferences.putBoolean("MUSIC_SHOULD_PLAY", btnCloseMusic.getText().toString().contentEquals("MUSIC IS ON"));
            game.preferences.flush();
            game.refleshPreferences();

            if(currentFullscreen != isFullscreen || oldHeight != newHeight || oldWidth != newWidth){
                ScreenUtils.resizeWindow(newWidth, newHeight);
                if(currentFullscreen)
                    ScreenUtils.fullscreenWindow();
            }
        }});

        btn1024_768.addListener(new ChangeListener() { @Override public void changed(ChangeEvent event, Actor actor) {
            setCurrentResToContent(btn1024_768);
        }});

        btn1280_1024.addListener(new ChangeListener() { @Override public void changed(ChangeEvent event, Actor actor) {
            setCurrentResToContent(btn1280_1024);
        }});

        btn1366_768.addListener(new ChangeListener() { @Override public void changed(ChangeEvent event, Actor actor) {
            setCurrentResToContent(btn1366_768);
        }});

        btn1600_900.addListener(new ChangeListener() { @Override public void changed(ChangeEvent event, Actor actor) {
            setCurrentResToContent(btn1600_900);
        }});

        btn1920_1080.addListener(new ChangeListener() { @Override public void changed(ChangeEvent event, Actor actor) {
            setCurrentResToContent(btn1920_1080);
        }});

        btn1920_1200.addListener(new ChangeListener() { @Override public void changed(ChangeEvent event, Actor actor) {
            setCurrentResToContent(btn1920_1200);
        }});

        btnFullscreen.addListener(new ChangeListener() { @Override public void changed(ChangeEvent event, Actor actor) {
            currentFullscreen = !currentFullscreen;
            btnFullscreen.setText(currentFullscreen ? "Fullscreen is ON" : "Fullscreen is OFF");
            setTextToLabel();
        }});

        btnBack.addListener(new ChangeListener() { @Override public void changed(ChangeEvent event, Actor actor) {
            Gdx.input.setInputProcessor(null);
            game.setScreen(game.screenSorter.getScreen("mainMenuScreen", game));
        }});

        btnCloseMusic.addListener(new ChangeListener() {@Override public void changed(ChangeEvent event, Actor actor) {
            if(increaseCloseMusicFont)
                btnCloseMusic.getLabel().getStyle().font = game.assetSorter.getResource("font20", BitmapFont.class);
            game.musicShouldPlay = !game.musicShouldPlay;
            if(game.musicShouldPlay) btnCloseMusic.setText("MUSIC IS ON");
            else btnCloseMusic.setText("MUSIC IS OFF");
        }});

    }

    private void setCurrentResToContent(TextButton btn){
        currentRes = btn.getText().toString();
        setTextToLabel();
    }

    private void setTextToLabel(){
        label.setText(currentRes + " " + (currentFullscreen ? "Fullscreen:ON" : "Fullscreen:OFF") );
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

}




