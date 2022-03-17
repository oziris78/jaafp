package com.telek;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.*;
import com.telek.telekgdx.assets.*;
import com.telek.screens.*;
import com.telek.telekgdx.screens.*;
import com.telek.utils.AssetSetter;


public class Jaafp extends Game {

	// saving
	public static boolean WILL_BE_FULLSCREEN;
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public Preferences preferences;

    // screens
	private MainMenuScreen mainMenuScreen = null;
	private OptionsScreen optionsScreen = null;
	private LoadingScreen loadingScreen = null;
	private GameScreen gameScreen = null;
	private EndingScreen endingScreen = null;

	// assetsorter
	public AssetSorter assetSorter;
	public ScreenSorter<Jaafp> screenSorter;

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
		SCREEN_WIDTH = preferences.getInteger("SCREEN_WIDTH", 1024);
		SCREEN_HEIGHT = preferences.getInteger("SCREEN_HEIGHT", 768);
		WILL_BE_FULLSCREEN = preferences.getBoolean("IS_FULLSCREEN", false);
		musicShouldPlay = preferences.getBoolean("MUSIC_SHOULD_PLAY", true);
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
		this.screenSorter = new ScreenSorter<>(this);
		this.screenSorter.putScreen("gameScreen", GameScreen.class);
		this.screenSorter.putScreen("endingScreen", EndingScreen.class);
		this.screenSorter.putScreen("loadingScreen", LoadingScreen.class);
		this.screenSorter.putScreen("mainMenuScreen", MainMenuScreen.class);
		this.screenSorter.putScreen("optionsScreen", OptionsScreen.class);



		// start the game
		setScreen(this.screenSorter.getScreen("mainMenuScreen"));
	}




	@Override
	public void dispose() {
		super.dispose();
		// DISPOSE ALL SCREENS
		mainMenuScreen.dispose();
		gameScreen.dispose();
		endingScreen.dispose();
		// DISPOSE ALL RENDERING OBJECTS
		batch.dispose();
		shapeRenderer.dispose();
		// DISPOSE ASSETSORTER
		assetSorter.disposeAll();
	}


	public void updateMainCameras(int width, int height){
		viewport.update(width, height);
		this.camera.update();
		this.camera.position.set(this.camera.viewportWidth / 2, this.camera.viewportHeight / 2, 0);

		this.batch.setProjectionMatrix(this.camera.combined);
		this.shapeRenderer.setProjectionMatrix(this.camera.combined);
	}

}
