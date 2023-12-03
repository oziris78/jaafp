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


package com.twistral.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.twistral.tempest.assetsorter.AssetSorter;

public class AssetSetter {

    private final AssetSorter assetSorter;

    public AssetSetter(AssetSorter assetSorter){
        this.assetSorter = assetSorter;
        this.init();
    }

    private void init(){
        assetSorter.addImmediatelyNeededAsset("font15", createFont(15));
        assetSorter.addImmediatelyNeededAsset("font20", createFont(20));
        assetSorter.addImmediatelyNeededAsset("font30", createFont(30));
        assetSorter.addImmediatelyNeededAsset("font60", createFont(60));
        assetSorter.addImmediatelyNeededAsset("skin", getSkin());

        assetSorter.addAsset("GAME_SCREEN", "damageBallTexture", new AssetDescriptor("images/enemies/damageBall.png", Texture.class));
        assetSorter.addAsset("GAME_SCREEN", "laserTexture", new AssetDescriptor("images/enemies/laser.png", Texture.class));
        assetSorter.addAsset("GAME_SCREEN", "sentryTexture", new AssetDescriptor("images/enemies/sentry.png", Texture.class));
        assetSorter.addAsset("GAME_SCREEN", "trapTileTexture", new AssetDescriptor("images/enemies/trapTile.png", Texture.class));
        assetSorter.addAsset("GAME_SCREEN", "timedTileTexture", new AssetDescriptor("images/enemies/timedTile.png", Texture.class));
        assetSorter.addAsset("GAME_SCREEN", "playerDeathTexture", new AssetDescriptor("images/player/playerDeath.png", Texture.class));
        assetSorter.addAsset("GAME_SCREEN", "playerTexture", new AssetDescriptor("images/player/player2.png", Texture.class));
        assetSorter.addAsset("GAME_SCREEN", "dyingSound", new AssetDescriptor("sounds/sfx/dying.wav", Sound.class));
        assetSorter.addAsset("GAME_SCREEN", "laserSound", new AssetDescriptor("sounds/sfx/laser.wav", Sound.class));
        assetSorter.addAsset("GAME_SCREEN", "timedTileSwitchSound", new AssetDescriptor("sounds/sfx/timedTileSwitch.wav", Sound.class));
        assetSorter.addAsset("GAME_SCREEN", "winningTheLevelSound", new AssetDescriptor("sounds/sfx/winningTheLevel.wav", Sound.class));
        assetSorter.addAsset("GAME_SCREEN", "pianoMusic", new AssetDescriptor("sounds/music/pianoMusic.wav", Music.class));
    }


    private Skin getSkin() {
        return new Skin(Gdx.files.internal("neonSkin/neon-ui.json"));
    }

    private BitmapFont createFont(int size){
        BitmapFont font = null;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/audiowide.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = size;
        parameter.color = Color.WHITE;
        parameter.borderColor = new Color(0.1f, 0.1f, 0.1f, 0.8f);
        parameter.borderWidth = 1f;
        parameter.shadowOffsetX = 2;
        parameter.shadowOffsetY = 2;
        parameter.shadowColor = new Color(0.1f, 0.1f, 0.1f, 0.8f);

        font = generator.generateFont(parameter);
        font.getData().markupEnabled = true;
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        generator.dispose();
        return font;
    }


}
