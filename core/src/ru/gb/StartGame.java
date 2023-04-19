package ru.gb;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class StartGame extends ApplicationAdapter {
	SpriteBatch batch;
	BitmapFont font;
	ParticleController pc;
	Background bg;
	Hero hero;
	EnemyController ec;
	BonusController bc;

	@Override
	public void create () {
		batch = new SpriteBatch();
		bg = new Background();
		pc = new ParticleController();
		hero = new Hero(pc);
		font = new BitmapFont();
		bc = new BonusController();
		ec = new EnemyController(bc);
		generateBigAsteroids(3);
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
		// Столкновение корабля с бонусом
		for (int i = 0; i < bc.getActiveList().size(); i++) {
			BonusController.Bonus a = bc.getActiveList().get(i);
			// TODO непойму почему hero переодически скачет от бобнуса к бонусу, получилось забавно
			// TODO но где-то ошибка, может метод dst() заменить в comeToHero. Подумаю
			if(hero.comeToHero(a)) {
				// a.setPosition(hero.getPosition());
				// Слежение за мышкой делается подобно
				Vector2 tmpVec = new Vector2().set(hero.getPosition()).sub(a.getPosition()).nor();
				a.getVelocity().mulAdd(tmpVec, 200.0f);
			};

			if(a.getHitArea().contains(hero.getPosition())) { // При столкновении с астероидом
				hero.useBonus(a.getSize(),a.getType()); // При столкновении с бонусом кораблю перепадают бонусы)))

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
				a.deactivate(); //
				break;
			}
		}

		// Столкновение астероида с пулей
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
					break; // Выходим чтоб не сбить еще астероиды
				}
			}
		}
		// Столкновение корабля с астероидом
		for (int i = 0; i < ec.getActiveList().size(); i++) {  // Урон для коробля
			EnemyController.Asteroid a = ec.getActiveList().get(i);
			if(a.getHitArea().contains(hero.getPosition())) { // При столкновении с астероидом
				hero.takeDamage( 2 + a.getHpMax()); // корабль получает урон равный жизньАстероида
				hero.push(a.getVelocity()); // при столкновении с астероидом нас отбрасывает на силу равную ускорению астероида
				a.takeDamage(a.getHpMax()); // астероид уничтожается
				break;
			}
		}
	}

}

