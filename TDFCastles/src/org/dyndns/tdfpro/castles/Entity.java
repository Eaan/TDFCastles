package org.dyndns.tdfpro.castles;

import android.graphics.Canvas;
import android.graphics.Paint;

public interface Entity {
    public boolean update(Game game, int delta);

    public void render(Game game, Canvas c);
}

class FadingCircle implements Entity {
    private float x;
    private float y;
    private float radius;
    private final Paint paint;
    private int lifetime = 0;

    public FadingCircle(float x, float y, float radius, Paint paint) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.paint = paint;
    }

    @Override
    public boolean update(Game game, int delta) {
        lifetime += delta;
        return lifetime < 3000;
    }

    @Override
    public void render(Game game, Canvas c) {
        c.drawCircle(x, y, radius, paint);
    }
}