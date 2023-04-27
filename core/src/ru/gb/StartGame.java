package ru.gb;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;

public class StartGame extends ApplicationAdapter {
	SpriteBatch batch;
	BitmapFont font;
	ParticleController pc;
	Background bg;
	Hero hero;
	EnemyController ec;
	BonusController bc;

	int level = 1;

	public int getLevel() {
		return level;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		bg = new Background(hero);
		pc = new ParticleController();
		hero = new Hero(pc, this);
		font = new BitmapFont();
		bc = new BonusController();
		ec = new EnemyController(bc);
		generateBigAsteroids(1);
	}

	@Override
	public void render () {
		batch.begin();

		float dt = Gdx.graphics.getDeltaTime();

		bg.render(batch);
		bg.update(dt);

		hero.render(batch);
		hero.update(dt);

		hero.renderGUI(batch, font);

		hero.getWeapon().getBc().render(batch);
		hero.getWeapon().getBc().update(dt);

		pc.render(batch);
		pc.update(dt);

		ec.render(batch);
		ec.update(dt);

		bc.render(batch);
		bc.update(dt);

		batch.end();

		checkCollisions();

		if (ec.getActiveList().isEmpty()) {
			generateBigAsteroids(++level);
			hero.getWeapon().setDamage(hero.getWeapon().getDamage() + level);
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		bg.dispose();
	}

	private void generateBigAsteroids(int count) {
		for (int i = 0; i < count; i++) {
			ec.setup(MathUtils.random(0, Gdx.graphics.getWidth()),
					MathUtils.random(0, Gdx.graphics.getHeight()),
					MathUtils.random(-200,200),MathUtils.random(-200,200), 1.0f);
		}
	}

	public void checkCollisions() {
		for (int i = 0; i < bc.getActiveList().size(); i++) {
			BonusController.Bonus a = bc.getActiveList().get(i);
			if(hero.comeToHero(a)) {
				Vector2 tmpVec = new Vector2().set(hero.getPosition()).sub(a.getPosition()).nor();
				a.getVelocity().mulAdd(tmpVec, 200.0f);
			};

			if(a.getHitArea().contains(hero.getPosition())) {
				hero.useBonus(a.getSize(),a.getType());

				for (int j = 0; j < 16; j++) {
					float angle = 6.28f / 16.0f * j;
					pc.setup(
							hero.getPosition().x + MathUtils.random(-4, 4),
							hero.getPosition().y + MathUtils.random(-4, 4),
							(float) Math.cos(angle) * 100,
							(float) Math.sin(angle) * 100,
							0.8f, 3.0f, 1.8f,
							0.0f, 1.0f, 0.0f, 1.0f,
							1.0f, 1.0f, 1.0f, 0.5f
					);
				}
				a.deactivate();
				break;
			}
		}

		for (int i = 0; i < hero.getWeapon().getBc().getActiveList().size(); i++) {
			BulletController.Bullet b = hero.getWeapon().getBc().getActiveList().get(i);
			for (int j = 0; j < ec.getActiveList().size(); j++) {
				EnemyController.Asteroid a = ec.getActiveList().get(j);
				if(a.getHitArea().contains(b.getPosition())) {

					pc.setup(
							b.getPosition().x + MathUtils.random(-4, 4),
							b.getPosition().y + MathUtils.random(-4, 4),
							b.getVelocity().x * -0.3f + MathUtils.random(-30, 30),
							b.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
							0.2f, 2.3f, 1.7f,
							1.0f, 1.0f, 1.0f, 1.0f,
							0.0f, 0.0f, 1.0f, 0.0f
					);

					b.deactivate();
					if(a.takeDamage(1)) {
						hero.addScore(a.getHpMax() * 100);
					}
					break;
				}
			}
		}

		for (int i = 0; i < ec.getActiveList().size(); i++) {
			EnemyController.Asteroid a = ec.getActiveList().get(i);
			if(a.getHitArea().contains(hero.getPosition())) {
				hero.takeDamage( 2 + a.getHpMax());
				hero.push(a.getVelocity());
				a.takeDamage(a.getHpMax());
				break;
			}
		}
	}
}

