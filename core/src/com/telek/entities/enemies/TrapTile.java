package com.telek.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.telek.entities.player.*;
import com.telek.telekgdx.TelekGDX;
import com.telek.telekgdx.assets.*;

import java.util.*;

import static com.telek.utils.Consts.PPM;


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
        this.textureRegion = TelekGDX.getRepeatedTexture(this.texture, horizontalTileNum, verticalTileNum);

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
        playerRec.x = player.getBody().getPosition().x * PPM;
        playerRec.y = player.getBody().getPosition().y * PPM;
        if(playerRec.overlaps(boundingBox)) player.die(lasers);
    }

    public void draw(SpriteBatch batch){
        batch.draw(textureRegion, boundingBox.x - TILE_WIDTH / 2, boundingBox.y - TILE_HEIGHT / 2, drawWidth, drawHeight);
    }

}
