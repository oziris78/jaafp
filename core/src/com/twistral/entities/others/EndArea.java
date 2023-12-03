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
