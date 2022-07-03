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
import com.twistral.toriagdx.assets.*;
import com.twistral.toriagdx.screens.*;
import com.twistral.screens.LoadingScreen;
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
		TScreenUtils.resizeAndSetFullscreen(SCREEN_WIDTH, SCREEN_HEIGHT, WILL_BE_FULLSCREEN);

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
