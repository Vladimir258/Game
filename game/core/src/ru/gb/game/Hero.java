package ru.gb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


public class Hero {

    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float enginePower;
    private ParticleController pc;
    private int halfX;
    private int halfY;
    private final float BASE_SIZE = 64;
    private final float BASE_RADIUS = BASE_SIZE / 2;
    public float getAngle() {
        return angle;
    }
    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Hero(ParticleController pc) {
        this.texture = new TextureRegion(new Texture("ship.png"));
        this.position = new Vector2(100, 100);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;
        this.enginePower = 500.0f;
        this.pc = pc;
    }

    public ParticleController getPc() {
        return pc;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, 1, 1, angle);
        pc.render(batch);
    }

    public void update(float dt) {
        pc.update(dt);
        checkSpaceBorders();
        controlShip(dt);

        if(velocity.len() > 50.0f) {
            float bxL = position.x + MathUtils.cosDeg(angle + 140) * 40;
            float byL = position.y + MathUtils.sinDeg(angle + 140) * 40;
            float bxR = position.x + MathUtils.cosDeg(angle - 140) * 40;
            float byR = position.y + MathUtils.sinDeg(angle - 140) * 40;

            for (int i = 0; i < 5; i++) {
                pc.setup(
                        bxL + MathUtils.random(-4,4),
                        byL + MathUtils.random(-4,4),
                        velocity.x * -0.3f + MathUtils.random(-4,4),
                        velocity.y * -0.3f + MathUtils.random(-4,4),
                        0.2f, 1.2f, 0.2f,
                        1.0f, 0.3f, 0.0f, 1.0f,
                        1.0f, 1.0f, 1.0f, 1.0f
                );
                pc.setup(
                        bxR + MathUtils.random(-4,4),
                        byR + MathUtils.random(-4,4),
                        velocity.x * -0.3f + MathUtils.random(-4,4),
                        velocity.y * -0.3f + MathUtils.random(-4,4),
                        0.2f, 1.2f, 0.2f,
                        1.0f, 0.3f, 0.0f, 1.0f,
                        1.0f, 1.0f, 1.0f, 1.0f
                );
            }
        }
    }

    public void push(Vector2 pushVector) {
        velocity.set(pushVector);
    }

    public void checkSpaceBorders() {
        if(position.x < 32.0f) {
            position.x = 32.0f;
            velocity.x *= -1; // Отскакивание от стены
        }
        if(position.x >  Gdx.graphics.getWidth() - 32.0f) {
            position.x =  Gdx.graphics.getWidth()- 32.0f;
            velocity.x *= -1;
        }
        if(position.y < 32.0f) {
            position.y = 32.0f;
            velocity.y *= -1; // Отскакивание от стены
        }
        if(position.y >  Gdx.graphics.getHeight() - 32.0f) {
            position.y = Gdx.graphics.getHeight() - 32.0f;
            velocity.y *= -1;
        }
    }

    public void controlShip(float dt) {

        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180.0f * dt;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 180.0f * dt;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.x += MathUtils.cosDeg(angle) * enginePower * dt;
            velocity.y += MathUtils.sinDeg(angle) * enginePower * dt;
        } else
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.x -= MathUtils.cosDeg(angle) * enginePower / 2 * dt;
            velocity.y -= MathUtils.sinDeg(angle) * enginePower / 2 * dt;
        }

        position.mulAdd(velocity,dt); // Сложить и умножить. Вместо двух строк position.x += velocity.x * dt;

        float stopKoef = 1.0f - dt; // Коэффецинет торможения
        if (stopKoef < 0) {
            stopKoef = 0;
        }
        velocity.scl(stopKoef); // scl() - Умножение вектора на скаляр
    }
}
