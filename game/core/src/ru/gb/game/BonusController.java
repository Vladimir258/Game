package ru.gb.game;

import com.badlogic.gdx.math.Vector2;
import ru.gb.game.helpers.ObjectPool;
import ru.gb.game.helpers.Poolable;

public class BonusController extends ObjectPool<BonusController.Bonus> {


    class Bonus implements Poolable {
        public Vector2 getPosition() {
            return position;
        }

        private Vector2 position;


        @Override
        public boolean isActive() {
            return false;
        }
    }

    @Override
    protected Bonus newObject() {
        return null;
    }
}
