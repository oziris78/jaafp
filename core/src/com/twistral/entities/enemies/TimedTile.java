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


package com.twistral.entities.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.twistral.tempest.assetsorter.*;
import com.twistral.entities.player.Player;
import com.twistral.tempest.utils.TextureUtils;
import com.twistral.utils.Consts;

import java.util.LinkedList;


public class TimedTile {

    private static final int TILE_WIDTH = 32, TILE_HEIGHT = 32;
    private Sound timedTileSwitchSound;
    private boolean isHarmless;
    private Texture textureForDamage,textureForHarmless;
    private TextureRegion textureRegionForDamage, textureRegionForHarmless;
    private Rectangle boundingBox;
    private int drawWidth, drawHeight;
    private float timer = 0, harmlessTime, damageTime;

    // garbage
    private Rectangle playerRec = new Rectangle(0,0,32,32);


    public TimedTile(AssetSorter assetSorter, int mapRowNum, int mapColNum, int horizontalTileNum, int verticalTileNum, float harmlessTime, float damageTime){

        this.textureForDamage = assetSorter.getResource("GAME_SCREEN", "trapTileTexture", Texture.class);
        this.textureForHarmless = assetSorter.getResource("GAME_SCREEN", "timedTileTexture", Texture.class);

        this.textureRegionForDamage = TextureUtils.getRepeatedTexture(this.textureForDamage, horizontalTileNum, verticalTileNum);
        this.textureRegionForHarmless = TextureUtils.getRepeatedTexture(this.textureForHarmless, horizontalTileNum, verticalTileNum);

        this.timedTileSwitchSound = assetSorter.getResource("GAME_SCREEN", "timedTileSwitchSound", Sound.class);

        this.harmlessTime = harmlessTime;
        this.damageTime = harmlessTime + damageTime;

        drawWidth = TILE_WIDTH * horizontalTileNum;
        drawHeight = TILE_HEIGHT * verticalTileNum;
        this.boundingBox = new Rectangle(
                mapRowNum * TILE_WIDTH + TILE_WIDTH / 2,
                (23-mapColNum) * TILE_HEIGHT + TILE_HEIGHT / 2,
                drawWidth - TILE_WIDTH / 2,
                drawHeight - TILE_HEIGHT / 2
        );
    }

    public TimedTile(AssetSorter assetSorter, int mapRowNum, int mapColNum, int horizontalTileNum, int verticalTileNum){
        this(assetSorter, mapRowNum, mapColNum, horizontalTileNum, verticalTileNum, 2f, 2f);
    }

    public TimedTile(AssetSorter assetSorter, int mapRowNum, int mapColNum){
        this(assetSorter, mapRowNum, mapColNum, 1, 1, 2f, 2f);
    }

    /* METHODS */

    public void update(LinkedList<Laser> lasers, Player player, float delta){
        timer += delta;
        if(timer <= harmlessTime) {
            // is harmless here
            if(!isHarmless) changeState();
            return;
        }
        if(timer > harmlessTime && timer <= damageTime){
            // is damage here
            if(isHarmless) changeState();
            playerRec.x = player.getBody().getPosition().x * Consts.PPM;
            playerRec.y = player.getBody().getPosition().y * Consts.PPM;
            if(playerRec.overlaps(boundingBox)) player.die(lasers);
        }
        if(timer > damageTime) timer = 0f;
    }

    public void draw(SpriteBatch batch){
        if(timer <= harmlessTime)
            batch.draw(textureRegionForHarmless, boundingBox.x - TILE_WIDTH / 2, boundingBox.y - TILE_HEIGHT / 2, drawWidth, drawHeight);
        else if(timer > harmlessTime && timer <= damageTime)
            batch.draw(textureRegionForDamage, boundingBox.x - TILE_WIDTH / 2, boundingBox.y - TILE_HEIGHT / 2, drawWidth, drawHeight);
    }

    private void changeState(){
        timedTileSwitchSound.play(0.5f);
        isHarmless = !isHarmless;
    }

}
