package ru.gb.game;

import com.badlogic.gdx.math.Vector2;
import ru.gb.game.helpers.ObjectPool;
import ru.gb.game.helpers.Poolable;

public class BulletController extends ObjectPool<BulletController.Bullet> {

    class Bullet implements Poolable {

        @Override
        public boolean isActive() {
            return false;
        }
    }

    @Override
    protected Bullet newObject() {
        return null;
    }
}
