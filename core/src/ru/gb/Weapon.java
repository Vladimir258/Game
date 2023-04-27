package ru.gb;

import com.badlogic.gdx.math.*;

public class Weapon {
    private BulletController bc;
    private Hero hero;
    private String title;
    private float firePeriod;
    private int damage;
    private float bulletSpeed;
    private int maxBullets;
    private int curBullets;
    private Vector3[] slots;

    public float getFirePeriod() {
        return firePeriod;
    }

    public int getMaxBullets() {
        return maxBullets;
    }

    public int getCurBullets() {
        return curBullets;
    }

    public void setCurBullets(int bullet) {
        curBullets += bullet;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
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
