package ru.gb;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StartGame extends ApplicationAdapter {
	SpriteBatch batch;
	Unit hero;
//	private final List<Unit> enemies = new ArrayList<>();
	private KeyboardAdapter inptuProcessor = new KeyboardAdapter();

	@Override
	public void create () {
		Gdx.input.setInputProcessor(inptuProcessor);
		batch = new SpriteBatch();
		hero = new Unit(100, 200);

//		List<Unit> newEnemies = IntStream.range(0,15)
//				.mapToObj(i -> {
//					int x = MathUtils.random(Gdx.graphics.getWidth());
//					int y = MathUtils.random(Gdx.graphics.getHeight());
//					return new Unit(x,y, "enemy.png");
//				})
//				.collect(Collectors.toList());
//		enemies.addAll(newEnemies);
	}

	@Override
	public void render () {
		hero.moveTo(inptuProcessor.getDirection()); // Передача движения герою
		hero.rotateTo(inptuProcessor.getMousePos());
		ScreenUtils.clear(1, 1, 1, 1);
		batch.begin();
		hero.render(batch);
//		enemies.forEach(enemy -> {
//			enemy.render(batch);
//			enemy.rotateTo(hero.getPosition());
//		});
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		hero.dispose();
	}
}