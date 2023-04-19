package ru.gb;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import ru.gb.helpers.*;

public class ParticleController extends ObjectPool<ParticleController.Particle> {

    class Particle implements Poolable {
        private Vector2 position;
        private Vector2 velocity;
        private float r1, g1, b1, a1;
        private float r2, g2, b2, a2;

        private boolean active;
        private float time;
        private float timeMax;
        private float size1, size2;

        public Vector2 getPosition() {
            return position;
        }
        public Vector2 getVelocity() {
            return velocity;
        }
        public float getR1() {
            return r1;
        }
        public float getG1() {
            return g1;
        }
        public float getB1() {
            return b1;
        }
        public float getA1() {
            return a1;
        }
        public float getR2() {
            return r2;
        }
        public float getG2() {
            return g2;
        }
        public float getB2() {
            return b2;
        }
        public float getA2() {
            return a2;
        }
        public float getTime() {
            return time;
        }
        public float getTimeMax() {
            return timeMax;
        }
        public float getSize1() {
            return size1;
        }
        public float getSize2() {
            return size2;
        }
        @Override
        public boolean isActive() {
            return active;
        }

        public Particle() {
            position = new Vector2(0, 0);
            velocity = new Vector2(0, 0);
            size1 = 1.0f;
            size2 = 1.0f;
        }

        public void init(float x, float y, float vx, float vy,
                         float timeMax, float size1, float size2,
                         float r1, float g1, float b1, float a1,
                         float r2, float g2, float b2, float a2) {
            this.position.x = x;
            this.position.y = y;
            this.velocity.x = vx;
            this.velocity.y = vy;
            this.r1 = r1;
            this.g1 = g1;
            this.b1 = b1;
            this.a1 = a1;
            this.r2 = r2;
            this.g2 = g2;
            this.b2 = b2;
            this.a2 = a2;
            this.time = 0.0f;
            this.timeMax = timeMax;
            this.size1 = size1;
            this.size2 = size2;
            this.active = true;
        }

        public void deactivate() {
            active = false;
        }

        public void update(float dt) {
            time += dt;
            position.mulAdd(velocity, dt);
            if(time > timeMax) {
                deactivate();
            }
        }
    }
    class EffectBuilder {
        public void buildMonsterSplash(float x, float y) {
            for (int i = 0; i < 15; i++) {
                float randomAngle = MathUtils.random(0, 6.28f);
                float randomSpeed = MathUtils.random(0, 50.0f);
                setup(x, y, (float)Math.cos(randomAngle) * randomSpeed, (float) Math.sin(randomAngle) * randomSpeed, 1.2f, 2.0f, 1.8f, 1, 0, 0, 1, 1, 0, 0, 0.2f);
            }
        }
    }

    private TextureRegion oneParticle;
    private EffectBuilder effectBuilder;

    public EffectBuilder getEffectBuilder() {
        return effectBuilder;
    }

    public ParticleController() {
        this.oneParticle = new TextureRegion(new Texture("shadow.jpg"));
        this.effectBuilder = new EffectBuilder();
    }

    @Override
    protected Particle newObject() {
        return new Particle();
    }

    public void render(SpriteBatch batch) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        for (int i = 0; i < activeList.size(); i++) {
            Particle o = activeList.get(i);
            float t = o.getTime() / o.getTimeMax();
            float scale =  lerp(o.getSize1(), o.getSize2(), t);
            batch.setColor(lerp(o.getR1(), o.getR2(), t),
                           lerp(o.getG1(), o.getG2(), t),
                           lerp(o.getB1(), o.getB2(), t),
                           lerp(o.getA1(), o.getA2(), t));
            batch.draw(oneParticle, o.getPosition().x - 8, o.getPosition().y - 8,
                    8, 8, 16, 16, scale, scale, 0);
        }
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        for (int i = 0; i < activeList.size(); i++) {
            Particle o = activeList.get(i);
            float t = o.getTime() / o.getTimeMax();
            float scale = lerp(o.getSize1(), o.getSize2(), t);
            if(MathUtils.random(0, 300) < 3) {
                scale *= 5;
            }
            batch.setColor(lerp(o.getR1(), o.getR2(), t), lerp(o.getG1(), o.getG2(), t),
                    lerp(o.getB1(), o.getB2(), t), lerp(o.getA1(), o.getA2(), t));
            batch.draw(oneParticle, o.getPosition().x - 8, o.getPosition().y - 8,
                    8, 8, 16, 16, scale, scale, 0);

        }
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void setup(float x, float y, float vx, float vy,
                      float timeMax, float size1, float size2,
                      float r1, float g1, float b1, float a1,
                      float r2, float g2, float b2, float a2) {
        Particle item = getActiveElement();
        item.init(x, y, vx, vy, timeMax, size1, size2, r1, g1, b1, a1, r2, g2, b2, a2);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    public float lerp(float value1, float value2, float point) {
        return value1 + (value2 - value1) * point;
    }
}
