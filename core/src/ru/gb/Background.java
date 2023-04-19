package ru.gb;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;

public class Background {
    private class Star{
        private Vector2 position;
        private Vector2 velocity;
        float scale;

        public Star() {
            this.position = new Vector2(MathUtils.random(-200, Gdx.graphics.getWidth() + 200), MathUtils.random(-200, Gdx.graphics.getHeight() + 200));
            this.velocity = new Vector2(MathUtils.random(-40, -5), 0);
            this.scale = Math.abs(velocity.x) / 80.0f * 0.8f;
        }
        public void update(float dt) {
            if(hero != null) {
                position.x += (velocity.x - hero.getVelocity().x * 0.1) * dt;
                position.y += (velocity.y - hero.getVelocity().y * 0.1) * dt;
            } else {
                position.mulAdd(velocity, dt);
            }
            if(position.x < -200) {
                position.x = Gdx.graphics.getHeight() + 1000;
                position.y = MathUtils.random(-200, Gdx.graphics.getHeight() + 200);
                scale = Math.abs(velocity.x) / 40.0f * 0.8f;
            }
        }
    }
    private Hero hero;

    private final int STAR_COUNT = 1000;
    private Texture textureCosmos;
    private TextureRegion textureStar;
    private Star[] stars;

       public Background(Hero hero) {
        this.textureCosmos = new Texture("bg.png");
        this.textureStar = new TextureRegion(new Texture("star.jpg"));
        this.hero = hero;
        this.stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] =  new Star();
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(textureCosmos, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        for (Star star : stars) {
            batch.draw(textureStar, star.position.x - 8, star.position.y - 8, 0, 0,
                    16, 16, star.scale, star.scale, 0);
        }
    }

    public void update(float dt) {
        // Обновляем координаты звезд
        for (Star star : stars) {
            star.update(dt);
        }
    }

    public void dispose() {
        textureCosmos.dispose();
    }
}
