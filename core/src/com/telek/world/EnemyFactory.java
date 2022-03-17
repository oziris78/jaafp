package com.telek.world;

import com.badlogic.gdx.physics.box2d.*;
import com.telek.telekgdx.assets.*;
import com.telek.entities.enemies.*;


public class EnemyFactory {


    public static DamageBall[] getDamageBalls(AssetSorter assetSorter, World world, int level) {
        WorldUtils.removeAllDamageBalls(world);
        DamageBall[] arr = null;
        if(level == 1){
            // LEVEL 1
            arr = new DamageBall[]{
                DamageBall.getDamageBall(assetSorter, world, 8, 13, 12, 13),
                DamageBall.getDamageBall(assetSorter, world, 8, 12, 12, 12),
                DamageBall.getDamageBall(assetSorter, world, 8, 11, 12, 11),
                DamageBall.getDamageBall(assetSorter, world, 8, 10, 12, 10),
                DamageBall.getDamageBall(assetSorter, world, 8, 9, 12, 9),

                DamageBall.getDamageBall(assetSorter, world, 13, 10, 13, 16),
                DamageBall.getDamageBall(assetSorter, world, 14, 10, 14, 16),
                DamageBall.getDamageBall(assetSorter, world, 15, 10, 15, 16),
                DamageBall.getDamageBall(assetSorter, world, 16, 10, 16, 16),
                DamageBall.getDamageBall(assetSorter, world, 17, 10, 17, 16),
                DamageBall.getDamageBall(assetSorter, world, 18, 10, 18, 16),
                DamageBall.getDamageBall(assetSorter, world, 19, 10, 19, 16),
            };
        }
        if(level == 2){
            // LEVEL 2
            arr = new DamageBall[]{
                DamageBall.getDamageBall(assetSorter, world, 12, 17, 12, 6),
                DamageBall.getDamageBall(assetSorter, world, 14, 17, 14, 6),
                DamageBall.getDamageBall(assetSorter, world, 16, 17, 16, 6),
                DamageBall.getDamageBall(assetSorter, world, 18, 17, 18, 6),
            };
        }
        if(level == 3){
            // LEVEL 3
            arr = new DamageBall[]{
                DamageBall.getDamageBall(assetSorter, world, 13, 14, 13, 17),
                DamageBall.getDamageBall(assetSorter, world, 17, 14, 17, 17),
                DamageBall.getDamageBall(assetSorter, world, 10, 11, 18, 11, 18, 12, 10, 12)
            };
        }
        if(level == 4){
            // LEVEL 4
            arr = new DamageBall[]{
                DamageBall.getDamageBall(assetSorter, world, 9, 11, 20, 11, 20, 12, 9, 12, 9, 7, 17, 7, 17, 9, 9, 9)
            };
        }
        if(level == 5){
            // LEVEL 5
            arr = new DamageBall[]{};
        }
        return arr;
    }


    public static TrapTile[] getTrapTiles(AssetSorter assetSorter, int level) {
        TrapTile[] arr = null;
        if(level == 1 || level == 5){
            // LEVEL 1
            arr = new TrapTile[]{};
        }
        if(level == 2){
            // LEVEL 2
            arr =  new TrapTile[]{
                new TrapTile(assetSorter, 8, 13, 13, 1),
                new TrapTile(assetSorter, 10, 10, 12, 1),
                new TrapTile(assetSorter, 10, 9, 1, 2)
            };
        }
        if(level == 3){
            // LEVEL 3
            arr = new TrapTile[]{
                new TrapTile(assetSorter, 8, 13, 12, 1),
                new TrapTile(assetSorter, 8, 12, 1, 7),
                new TrapTile(assetSorter, 9, 6, 9, 1),
                new TrapTile(assetSorter, 11, 10, 11, 1)
            };
        }
        if(level == 4){
            // LEVEL 4
            arr = new TrapTile[]{
                new TrapTile(assetSorter, 8, 13, 11, 1),
                new TrapTile(assetSorter, 11, 10, 11, 1)
            };
        }
        return arr;
    }


    public static TimedTile[] getTimedTiles(AssetSorter assetSorter, int level) {
        TimedTile[] arr = null;
        if(level == 1 || level == 2){
            // LEVEL 1 & 2
            arr = new TimedTile[]{};
        }
        if(level == 3){
            // LEVEL 3
            arr = new TimedTile[]{
                    new TimedTile(assetSorter, 15, 17, 1, 4),
                    new TimedTile(assetSorter, 11, 9, 1, 3, 1f, 3.0f),
                    new TimedTile(assetSorter, 13, 9, 1, 3, 1f, 2.5f),
                    new TimedTile(assetSorter, 15, 9, 1, 3, 1f, 2.0f),
                    new TimedTile(assetSorter, 17, 9, 1, 3, 1f, 1.5f),
                    new TimedTile(assetSorter, 12, 12, 1, 1, 7f, 7f),
                    new TimedTile(assetSorter, 13, 11, 1, 1, 7f, 7f),
                    new TimedTile(assetSorter, 14, 12, 1, 1, 7f, 7f),
                    new TimedTile(assetSorter, 15, 11, 1, 1, 7f, 7f),
                    new TimedTile(assetSorter, 16, 12, 1, 1, 7f, 7f)
            };
        }
        if(level == 4){
            // LEVEL 4
            arr = new TimedTile[]{
            };
        }
        if(level == 5){
            // LEVEL 5
            arr = new TimedTile[]{
                new TimedTile(assetSorter, 16, 7),
                new TimedTile(assetSorter, 17, 8),
                new TimedTile(assetSorter, 18, 9),
                new TimedTile(assetSorter, 19, 10)
            };
        }
        return arr;
    }



    public static Sentry[] getSentries(AssetSorter assetSorter, World world, int level) {
        WorldUtils.removeAllSentries(world);
        Sentry[] arr = null;
        if(level == 1 || level == 2 || level == 3){/* LEVEL 1 & 2 & 3 */ arr = new Sentry[]{}; }
        if(level == 4){
            // LEVEL 4
            arr = new Sentry[]{
                new Sentry(assetSorter, world,  8,  6, 2.50f, 0.5f),
                new Sentry(assetSorter, world, 21, 17, 1.25f, 1.5f),
                new Sentry(assetSorter, world, 15, 17, 1.25f, 1.5f),
                new Sentry(assetSorter, world, 17, 17, 1.25f, 1.5f),
                new Sentry(assetSorter, world, 12,  6, 1.25f, 1.5f),
                new Sentry(assetSorter, world, 14,  6, 1.25f, 1.5f)
            };
        }
        if(level == 5){
            // LEVEL 5
            arr = new Sentry[]{
                new Sentry(assetSorter, world, 9, 13,  0.5f, 5f),
                new Sentry(assetSorter, world, 10, 12, 0.5f, 5f),
                new Sentry(assetSorter, world, 11, 11, 0.5f, 5f),
                new Sentry(assetSorter, world, 12, 10, 0.5f, 5f),
                new Sentry(assetSorter, world, 13, 9,  0.5f, 5f),
                new Sentry(assetSorter, world, 14, 8,  0.5f, 5f),
                new Sentry(assetSorter, world, 15, 7,  0.5f, 5f),
                new Sentry(assetSorter, world, 16, 6,  0.5f, 5f),
                new Sentry(assetSorter, world, 14, 16, 0.5f, 5f),
                new Sentry(assetSorter, world, 15, 15, 0.5f, 5f),
                new Sentry(assetSorter, world, 16, 14, 0.5f, 5f),
                new Sentry(assetSorter, world, 17, 13, 0.5f, 5f),
                new Sentry(assetSorter, world, 18, 12, 0.5f, 5f),
                new Sentry(assetSorter, world, 19, 11, 0.5f, 5f),
                new Sentry(assetSorter, world, 20, 10, 0.5f, 5f),
                new Sentry(assetSorter, world, 21, 9,  0.5f, 5f)
            };
        }
        return arr;
    }




}
