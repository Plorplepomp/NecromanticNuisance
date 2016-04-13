package com.scottdennis.necromanticnuisance.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.scottdennis.necromanticnuisance.NecromanticNuisance;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new NecromanticNuisance(), config);
                config.height = 768;
                config.width = 1024;
	}
}
