package com.telek.entities.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.telek.telekgdx.assets.*;

public class DyingAnimation {


    public static Animation<TextureRegion> getAnimation(AssetSorter assetSorter){
        Texture walkSheet = assetSorter.getResource("GAME_SCREEN", "playerDeathTexture", Texture.class);

        int FRAME_COLS = walkSheet.getWidth() / Player.PLAYER_WIDTH;
        int FRAME_ROWS = walkSheet.getHeight() / Player.PLAYER_HEIGHT;
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
