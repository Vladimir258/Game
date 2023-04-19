package ru.gb;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Background {
    // Звезды часть bg, поэтому создадим внутренний класс звезда в классе bg
    private class Star{
        private Vector2 position;
        private Vector2 velocity;
        float scale;

        public Star() {
            //  В конструкторе рандомно создаем звезду, координаты и скорость с направлением
            this.position = new Vector2(MathUtils.random(-200, Gdx.graphics.getWidth() + 200), MathUtils.random(-200, Gdx.graphics.getHeight() + 200));
            this.velocity = new Vector2(MathUtils.random(-40, -5), 0);
            this.scale = Math.abs(velocity.x) / 80.0f * 0.8f;// Задаем размер звезд
        }
        // Движение звезд
        public void update(float dt) {
            // звезды двигаются относительно игрока
//            if(sg != null) {
////                position.x += (velocity.x - sg.getHero().getVelocity().x * 0.1) * dt;
////                position.y += (velocity.y - sg.getHero().getVelocity().y * 0.1) * dt;
//            } else {
                // относительно dt
                position.mulAdd(velocity, dt);
        //    }
            // Если звезда зашла за экран слева, перемещаем ее за экран справа
            // чтоб массив звезд циклично бегал от начала к концу, бесконечное звездное пространство
            if(position.x < -200) {
                position.x = Gdx.graphics.getHeight() + 1000;
                position.y = MathUtils.random(-200, Gdx.graphics.getHeight() + 200);
                scale = Math.abs(velocity.x) / 40.0f * 0.8f; // Задаем размер звезд
            }
        }
    }

    private final int STAR_COUNT = 1000; // Максимальное количество звезд
   // private StartGame sg;
    private Texture textureCosmos;
    private TextureRegion textureStar;
    private Star[] stars;

   // public Background() {
       public Background() {
        this.textureCosmos = new Texture("bg.png");
        this.textureStar = new TextureRegion(new Texture("star.jpg"));
        //this.textureStar = Assets.getInstance().getAtlas().findRegion("star16");
      //  this.sg = sg;
        this.stars = new Star[STAR_COUNT]; // заполняем массив звезд 34 - 37
        for (int i = 0; i < stars.length; i++) {
            stars[i] =  new Star();
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(textureCosmos, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Отрисовываем звезды
        for (Star star : stars) {
            batch.draw(textureStar, star.position.x - 8, star.position.y - 8, 0, 0,
                    16, 16, star.scale, star.scale, 0);

//            // Мерцание звезд
//            if (MathUtils.random(0, 1000) < 1) {
//                batch.draw(textureStar, stars[i].position.x - 8, stars[i].position.y - 8, 8, 8,
//                        16, 16, stars[i].scale * 2, stars[i].scale * 2, 0, 0, 0, 16, 16, false, false);
//            }
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
