package com.twistral.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.twistral.toriagdx.ToriaGDX;
import com.twistral.toriagdx.ToriaGDX;
import com.twistral.toriagdx.assets.*;
import com.twistral.entities.player.Player;
import com.twistral.utils.Consts;

import java.util.*;


public class TrapTile {

    private static final int TILE_WIDTH = 32, TILE_HEIGHT = 32;
    private Texture texture;
    private TextureRegion textureRegion;
    private Rectangle boundingBox;
    private int drawWidth, drawHeight;

    // garbage
    private Rectangle playerRec = new Rectangle(0,0,32,32);


    public TrapTile(AssetSorter assetSorter, int mapRowNum, int mapColNum, int horizontalTileNum, int verticalTileNum){
        this.texture = assetSorter.getResource("GAME_SCREEN", "trapTileTexture", Texture.class);
        this.textureRegion = ToriaGDX.getRepeatedTexture(this.texture, horizontalTileNum, verticalTileNum);

        drawWidth = TILE_WIDTH * horizontalTileNum;
        drawHeight = TILE_HEIGHT * verticalTileNum;

        this.boundingBox = new Rectangle(
                mapRowNum * TILE_WIDTH + TILE_WIDTH / 2,
                (23-mapColNum) * TILE_HEIGHT + TILE_HEIGHT / 2,
                drawWidth - TILE_WIDTH / 2, drawHeight - TILE_HEIGHT / 2
        );
    }

    /* METHODS */

    public void update(LinkedList<Laser> lasers, Player player, float delta){
        playerRec.x = player.getBody().getPosition().x * Consts.PPM;
        playerRec.y = player.getBody().getPosition().y * Consts.PPM;
        if(playerRec.overlaps(boundingBox)) player.die(lasers);
    }

    public void draw(SpriteBatch batch){
        batch.draw(textureRegion, boundingBox.x - TILE_WIDTH / 2, boundingBox.y - TILE_HEIGHT / 2, drawWidth, drawHeight);
    }

}
