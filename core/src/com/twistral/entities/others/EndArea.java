package com.twistral.entities.others;


import com.badlogic.gdx.math.Rectangle;
import com.twistral.entities.player.Player;

public class EndArea {

    private Rectangle area;

    public EndArea(float leftBottomTileCountX, float leftBottomTileCountY, float horizontalTileCount, float verticalTileCount){
        area = new Rectangle(leftBottomTileCountX * 32, leftBottomTileCountY * 32, horizontalTileCount * 32, verticalTileCount * 32);
    }

    public boolean playerEnteredArea(Player player){
        return this.area.contains(player.getSprite().getBoundingRectangle());
    }

}
