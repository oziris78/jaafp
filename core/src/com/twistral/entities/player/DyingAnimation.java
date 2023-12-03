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


package com.twistral.entities.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.twistral.tempest.assetsorter.AssetSorter;

public class DyingAnimation {


    public static Animation<TextureRegion> getAnimation(AssetSorter assetSorter){
        Texture walkSheet = assetSorter.getResource("GAME_SCREEN", "playerDeathTexture", Texture.class);

        int FRAME_COLS = walkSheet.getWidth() / com.twistral.entities.player.Player.PLAYER_WIDTH;
        int FRAME_ROWS = walkSheet.getHeight() / com.twistral.entities.player.Player.PLAYER_HEIGHT;
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);

        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        return new Animation<>(Player.DYING_ANIM_TOTAL_TIME / walkFrames.length, walkFrames);
    }

}
