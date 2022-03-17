package com.telek.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.telek.Jaafp;


public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "JAAFP v1.0";
		config.resizable = false;
		config.fullscreen = false;
		config.width = 600;
		config.height = 450;
		new LwjglApplication(new Jaafp(), config);
	}

}
