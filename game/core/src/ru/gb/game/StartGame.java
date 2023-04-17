package ru.gb.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;

public class StartGame extends ApplicationAdapter {
	SpriteBatch batch;
	BitmapFont font;
	ParticleController pc;
	Background bg;
	Hero hero;

	@Override
	public void create () {
		batch = new SpriteBatch();
		bg = new Background();
		pc = new ParticleController();
		hero = new Hero(pc);
		font = new BitmapFont();
	}

	@Override
	public void render () {
		batch.begin();
		float dt = Gdx.graphics.getDeltaTime();
		bg.render(batch);
		bg.update(dt);
		hero.render(batch);
		hero.update(dt);
		pc.render(batch);
		pc.update(dt);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		bg.dispose();
	}
}
