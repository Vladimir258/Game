package ru.gb.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class Weapon {
    private BulletController bc;
    private Hero hero;
    private String title;
    private float firePeriod;
    private int damage;
    private float bulletSpeed;
    private int curBullets;
    private int maxBullets;
    private Vector3[] slots;
    private Sound shootSound;
    public float getFirePeriod() {
        return firePeriod;
    }

    public int getCurBullets() {
        return curBullets;
    }

    public int getMaxBullets() {
        return maxBullets;
    }

    public void setCurBullets(int bullet) {
        curBullets += bullet;
    }

    public Weapon(Hero hero, String title,
                  float firePeriod, int damage, float bulletSpeed,
                  int maxBullets, Vector3[] slots) {
        this.hero = hero;
        this.title = title;
        this.firePeriod = firePeriod;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.maxBullets = maxBullets;
        this.curBullets = maxBullets;
        this.slots = slots;
        this.bc = new BulletController(hero.getPc());
    }

    public BulletController getBc() {
        return bc;
    }


    public void fire() {
        if (curBullets > 0) {
            curBullets--;
            for (int i = 0; i < slots.length; i++) {
                float x, y, vx, vy;

                x = hero.getPosition().x + MathUtils.cosDeg(hero.getAngle() + slots[i].y) * slots[i].x;//25; // Место вылета пули x
                y = hero.getPosition().y + MathUtils.sinDeg(hero.getAngle() + slots[i].y) * slots[i].x;//25;

                vx = hero.getVelocity().x + bulletSpeed * MathUtils.cosDeg(hero.getAngle() + slots[i].z);
                vy = hero.getVelocity().y + bulletSpeed * MathUtils.sinDeg(hero.getAngle() + slots[i].z);

                bc.setup(x, y, vx, vy);
            }
        }
    }
}
