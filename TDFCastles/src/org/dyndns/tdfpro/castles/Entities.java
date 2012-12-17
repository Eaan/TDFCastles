package org.dyndns.tdfpro.castles;

import java.util.ArrayList;
import java.util.Iterator;

import android.graphics.Canvas;

public class Entities implements Entity {
    private ArrayList<Entity> entities = new ArrayList<Entity>();

    @Override
    public boolean update(Game game, int delta) {
        synchronized (entities) {
            Iterator<Entity> ite = entities.iterator();
            while (ite.hasNext()) {
                Entity ent = ite.next();
                if (!ent.update(game, delta)) {
                    ite.remove();
                }
            }
        }
        return true;
    }

    public int size() {
        return entities.size();
    }

    @Override
    public void render(Game game, Canvas c) {
        synchronized (entities) {
            for (Entity v : entities) {
                v.render(game, c);
            }
        }
    }

    public void add(Entity ent) {
        synchronized (entities) {
            entities.add(ent);
        }
    }

}
