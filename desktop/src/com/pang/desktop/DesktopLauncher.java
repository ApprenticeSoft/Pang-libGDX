package com.pang.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pang.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Pang";
	    config.width = 960;
	    config.height = 540;
	    config.samples = 8;
	    
	    //config.width = 1920;
	    //config.height = 1080;
	    
	    //config.width = 500;
	    //config.height = 720;
	    
		new LwjglApplication(new MyGdxGame(), config);
	}
}
