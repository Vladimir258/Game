package ru.gb.game;

import ru.gb.game.helpers.ObjectPool;
import ru.gb.game.helpers.Poolable;

public class EnemyController extends ObjectPool<EnemyController.Asteroid> {

    class Asteroid implements Poolable {

        @Override
        public boolean isActive() {
            return false;
        }
    }

    @Override
    protected Asteroid newObject() {
        return null;
    }
}
