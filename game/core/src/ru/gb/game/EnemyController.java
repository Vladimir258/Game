package ru.gb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.gb.game.helpers.ObjectPool;
import ru.gb.game.helpers.Poolable;

public class EnemyController extends ObjectPool<EnemyController.Asteroid> {

    private BonusController bc;

    public EnemyController(BonusController bc) {
        this.bc = bc;
    }

    class Asteroid implements Poolable {
        private TextureRegion texture;
        private Vector2 position;
        private Vector2 velocity;
        private int hp;
        private int hpMax;
        private float angle; // Угол показа изображения
        private float rotationSpeed; // Скорость вращения
        private float scale; // Масштаб
        private boolean active;
        private Circle hitArea;

        private EnemyController ec;
        private final float BASE_SIZE = 256.0f;
        private final float BASE_RADIUS = BASE_SIZE / 2;

        public Circle getHitArea() {
            return hitArea;
        }

        public int getHpMax() {
            return hpMax;
        }

        public Vector2 getVelocity() {
            return velocity;
        }

        public Vector2 getPosition() {
            return position;
        }


        @Override
        public boolean isActive() {
            return false;
        }


    public Asteroid(EnemyController ec) {
        this.ec = ec;
        this.position = new Vector2(0,0);
        this.velocity = new Vector2(0,0);
        this.hitArea = new Circle();
        active = false;
        this.texture = new TextureRegion(new Texture("asteroid.png"));
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 128, position.y - 128, 128, 128,256,256,scale,scale,angle);
    }

    public void deactivate() {
        active = false;
    }

    public void activate(float x, float y, float vx, float vy, float scale) {
        this.position.set(x,y);
        this.velocity.set(vx,vy);
        this.hpMax = (int) ((5 * 2)* scale);
        this.hp = hpMax;
        this.angle = MathUtils.random(0.0f,360.0f);
        this.rotationSpeed = MathUtils.random(-180.0f,180.0f);
        this.hitArea.setPosition(position);

        this.scale = scale;
        this.active = true;
        this.hitArea.setRadius(BASE_RADIUS * scale * 0.9f);
    }

    public boolean takeDamage(int amout) {
        hp -= amout;
        if(hp <= 0) {
            deactivate();
            if(scale > 0.3f) {
                for (int i = 0; i < 3; i++) {
                    ec.setup(position.x, position.y,
                            MathUtils.random(-200, 200), MathUtils.random(-200, 200), scale - 0.2f);
                }
            }
            int bonusSize = this.hpMax * 2;         // Вычисляем максимальный бонус
            int bonusType = MathUtils.random(0,9);  // Вычисляем шанс выпадения
            bc.setup(position.x, position.y,
                    50, 50, 1.0f, bonusSize, bonusType); // Создаем бонус
            return true;
        } else {
            return false;
        }
    }

    // Движение
    public void update(float dt) {
        position.mulAdd(velocity, dt);
        angle += rotationSpeed * dt;

        if(position.x < -hitArea.radius) {
            position.x = Gdx.graphics.getWidth() + hitArea.radius;
        }
        if(position.x > Gdx.graphics.getWidth() + hitArea.radius) {
            position.x = -hitArea.radius;
        }
        if(position.y < -hitArea.radius) {
            position.y = Gdx.graphics.getHeight() + hitArea.radius;
        }
        if(position.y > Gdx.graphics.getHeight() + hitArea.radius) {
            position.y = -hitArea.radius;
        }
        hitArea.setPosition(position);
    }
}

    @Override
    protected Asteroid newObject() {
        return new Asteroid(this);
    }


    public void render(SpriteBatch batch) {
        // Отрисовываем астероиды
        for (int i = 0; i < activeList.size(); i ++) {
            Asteroid a = activeList.get(i);
            a.render(batch);
        }
    }

    public void setup(float x, float y, float vx, float vy, float scale) {
        getActiveElement().activate(x, y, vx, vy, scale);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i ++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
