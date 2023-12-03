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


package com.twistral;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.utils.viewport.*;
import com.twistral.screens.EndingScreen;
import com.twistral.screens.GameScreen;
import com.twistral.screens.MainMenuScreen;
import com.twistral.screens.OptionsScreen;
import com.twistral.tempest.assetsorter.AssetSorter;
import com.twistral.tempest.screensorter.*;
import com.twistral.screens.LoadingScreen;
import com.twistral.tempest.utils.ScreenUtils;
import com.twistral.utils.AssetSetter;


public class Jaafp extends Game {

	// saving
	public static boolean WILL_BE_FULLSCREEN;
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public Preferences preferences;

	// assetsorter
	public AssetSorter assetSorter;
	public ScreenSorter screenSorter;

	// rendering objects
	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;
	public OrthographicCamera camera;
	public Viewport viewport;

	// others
	public boolean musicShouldPlay;


	@Override
	public void create() {
		// saving
		preferences = Gdx.app.getPreferences("jaafpData");
		refleshPreferences();
		ScreenUtils.resizeWindow(SCREEN_WIDTH, SCREEN_HEIGHT);
		if(WILL_BE_FULLSCREEN)
			ScreenUtils.fullscreenWindow();

		// rendering objects
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera();
		viewport = new FitViewport(1024 / 2, 768 / 2, this.camera);
		viewport.apply();
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

		// assetsorter
		assetSorter = new AssetSorter();
		new AssetSetter(assetSorter);

		// screensorter
		this.screenSorter = new ScreenSorter();
		this.screenSorter.putScreen("gameScreen", GameScreen.class, Jaafp.class);
		this.screenSorter.putScreen("endingScreen", EndingScreen.class, Jaafp.class);
		this.screenSorter.putScreen("loadingScreen", LoadingScreen.class, Jaafp.class);
		this.screenSorter.putScreen("mainMenuScreen", MainMenuScreen.class, Jaafp.class);
		this.screenSorter.putScreen("optionsScreen", OptionsScreen.class, Jaafp.class);



		// start the game
		setScreen(this.screenSorter.getScreen("mainMenuScreen", this));
	}




	@Override
	public void dispose() {
		super.dispose();
		// DISPOSE ALL RENDERING OBJECTS
		batch.dispose();
		shapeRenderer.dispose();
		// DISPOSE ASSETSORTER
		assetSorter.disposeAll();
		screenSorter.disposeAll();
	}


	public void updateMainCameras(int width, int height){
		viewport.update(width, height);
		this.camera.update();
		this.camera.position.set(this.camera.viewportWidth / 2, this.camera.viewportHeight / 2, 0);

		this.batch.setProjectionMatrix(this.camera.combined);
		this.shapeRenderer.setProjectionMatrix(this.camera.combined);
	}

	public void refleshPreferences() {
		SCREEN_WIDTH = preferences.getInteger("SCREEN_WIDTH", 1024);
		SCREEN_HEIGHT = preferences.getInteger("SCREEN_HEIGHT", 768);
		WILL_BE_FULLSCREEN = preferences.getBoolean("IS_FULLSCREEN", false);
		musicShouldPlay = preferences.getBoolean("MUSIC_SHOULD_PLAY", true);
	}

}
