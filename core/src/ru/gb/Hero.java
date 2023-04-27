package ru.gb;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.StringBuilder;

public class Hero {

    private boolean pause = false;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float enginePower;
    public float fireTimer;
    private int score;
    private int scoreView;
    private int hp;
    private int hpMax;
    private Circle hitArea;
    private StringBuilder sbScore;
    private StringBuilder sbHP;
    private StringBuilder sbGameStatus;
    private StringBuilder sbAmmo;
    private StringBuilder sbMoney;
    private StringBuilder sbGameOver;
    private Weapon weapon;
    private ParticleController pc;

    private StartGame sg;
    private int halfX;
    private int halfY;
    private int money;
    private final float BASE_SIZE = 64;
    private final float BASE_RADIUS = BASE_SIZE / 2;
    public float getAngle() {
        return angle;
    }
    public void addScore(int amount) {
        this.score += amount;
    }
    public Vector2 getPosition() {
        return position;
    }
    public Vector2 getVelocity() {
        return velocity;
    }

    public int getScore() {
        return score;
    }

    public StringBuilder getSbGameStatus() {
        return sbGameStatus;
    }

    public void setSbGameStatus(StringBuilder sbGameStatus) {
        this.sbGameStatus = sbGameStatus;
    }

    public Hero(ParticleController pc, StartGame sg) {
        this.sg = sg;
        this.texture = new TextureRegion(new Texture("ship.png"));
        this.position = new Vector2(100, 100);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;
        this.enginePower = 500.0f;
        this.hitArea = new Circle();
        this.hitArea.setPosition(position);
        this.hitArea.setRadius(BASE_RADIUS);
        this.pc = pc;
        this.hpMax = 100;
        this.hp = hpMax;
        this.sbScore = new StringBuilder();
        this.sbHP = new StringBuilder();
        this.sbGameStatus = new StringBuilder();
        this.sbGameOver = new StringBuilder();
        this.sbAmmo = new StringBuilder();
        this.sbMoney = new StringBuilder();
        createWeapons();
    }

    public ParticleController getPc() {
        return pc;
    }

    public Weapon getWeapon() {
        return weapon;
    }

        public boolean comeToHero(BonusController.Bonus bb) {
        if(this.position.dst(bb.getPosition()) < 60) {
           return true;
        }
        return false;
    }

    public boolean takeDamage(int amout) {
        hp -= amout;
        if(hp <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, 1, 1, angle);


        pc.render(batch);
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        sbScore.clear();
        sbHP.clear();
        sbGameStatus.clear();
        sbAmmo.clear();
        sbMoney.clear();
        sbGameOver.clear();
        sbScore.append("SCORE: ").append(scoreView);
        font.draw(batch, sbScore, 10, 180);
        sbHP.append("HP: ").append(hp).append(" / ").append(hpMax);
        font.draw(batch, sbHP, 10, 160);
        sbAmmo.append("Ammo: ").append(weapon.getCurBullets()).append(" / ").append(weapon.getMaxBullets());
        font.draw(batch, sbAmmo, 10, 140);
        sbMoney.append("Money: ").append(money);
        font.draw(batch, sbMoney, 10,  120);

        sbGameStatus.append("Level: ").append(sg.getLevel());
        font.draw(batch, sbGameStatus, Gdx.graphics.getWidth() - 100,  Gdx.graphics.getHeight() - 100);

        halfX = Gdx.graphics.getWidth() / 2;
        halfY = Gdx.graphics.getHeight() / 2;

        if(hp <= 0) {
            sbGameOver.append("Game Over");
            font.draw(batch, sbGameOver, halfX, halfY); //
        }
    }

    public void update(float dt) {
        if(hp > 100) {
            hp = 100;
        }
        pc.update(dt);


        this.hitArea.setPosition(position);
        updateScore(dt);
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

    public void updateScore(float dt) {
        if(scoreView < score) {
            scoreView += 1000 * dt;
            if(scoreView > score) {
                scoreView = score;
            }
        }
    }

    public void push(Vector2 pushVector) {
        velocity.set(pushVector);
    }

    public void checkSpaceBorders() {
        if(position.x < 32.0f) {
            position.x = 32.0f;
            velocity.x *= -1;
        }
        if(position.x >  Gdx.graphics.getWidth() - 32.0f) {
            position.x =  Gdx.graphics.getWidth()- 32.0f;
            velocity.x *= -1;
        }
        if(position.y < 32.0f) {
            position.y = 32.0f;
            velocity.y *= -1;
        }
        if(position.y >  Gdx.graphics.getHeight() - 32.0f) {
            position.y = Gdx.graphics.getHeight() - 32.0f;
            velocity.y *= -1;
        }
    }

    public void controlShip(float dt) {
        fireTimer += dt;
        if(Gdx.input.isKeyPressed(Input.Keys.P)) {

            if(fireTimer > 0.2f) {
                fireTimer = 0.0f;
               weapon.fire();
            }
        }

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

        position.mulAdd(velocity,dt);

        float stopKoef = 1.0f - dt;
        if (stopKoef < 0) {
            stopKoef = 0;
        }
        velocity.scl(stopKoef);
    }

    public void useBonus(int size, int type) {
        if(type == 0 || type == 1 ) {
            money += size;
        }
        if(type == 2 || type == 3 ) {
            hp += size;
        }
        if(type == 4 || type == 5 ) {
            weapon.setCurBullets(size);
        }
    }

    private void createWeapons() {
        weapon = new Weapon(
                         this, "Energy", 0.2f, 4,
                        2000, 1000,
                        new Vector3[]{
                                // new Vector3(0, 0, 0),
                                //  new Vector3(0, 0, 0),
                                new Vector3(28, 0, 0)
                        });
    }
}

