package com.scottdennis.necromanticnuisance;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import screens.PlayScreen;

public class NecromanticNuisance extends Game {
	SpriteBatch batch;
        public PlayScreen playScreen;
	
	@Override
	public void create () 
        {
		batch = new SpriteBatch();
                playScreen = new PlayScreen(this);
                setScreen(playScreen);
                
	}

	@Override
	public void render () 
        {
            super.render();
	}
}
