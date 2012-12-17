package org.dyndns.tdfpro.maize;

import java.util.Iterator;
import java.util.LinkedList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

class Game extends View {
    private static final int MAP_X = 17;
    private static final int MAP_Y = 10;

    private Vec2 tileSize;

    private Paint textPaint;
    private Entities entities = new Entities();
    public final Vec2 screenDimension;
    private Map map;
    private LinkedList<Tile> path = new LinkedList<Tile>();
    private Tile start;
    private Tile finish;

    private GameThread gt;
    private boolean acceptingInput = true;

    public Game(Context context) {
        super(context);
        textPaint = new Paint();
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(28);
        textPaint.setTypeface(Typeface.MONOSPACE);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        screenDimension = new Vec2(metrics.widthPixels, metrics.heightPixels);

        tileSize = new Vec2(screenDimension.x / (MAP_X - 1), screenDimension.y / (MAP_Y - 1));
        map = new Map(MAP_X, MAP_Y, tileSize.mul(-0.5f), tileSize);

        markStartAndGoal();

        gt = new GameThread(25, this);
        gt.start(); // MUST BE LAST IN CONSTRUCTOR
    }

    private void markStartAndGoal() {
        int numExits = 0;
        do {
            start = map.getRandomTile();
            numExits = start.numExits();
        } while (numExits != 1);

        do {
            finish = map.getRandomTile();
            numExits = finish.numExits();
        } while (numExits != 1);

        start.setColor(Paints.red);
        finish.setColor(Paints.yellow);
    }

    public void update(int delta) {
        entities.update(this, delta);
        map.update(this, delta);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawARGB(255, 0, 0, 0);

        map.render(this, canvas);
        entities.render(this, canvas);

        renderPath(canvas);

        // canvas.drawRect(screenDimension.x - screenDimension.y / 2,
        // screenDimension.y / 2,
        // screenDimension.x, screenDimension.y, Paints.red);
        // canvas.drawText("" + screenDimension + ": " + entities.size(), 50,
        // 50, textPaint);

    }

    private void renderPath(Canvas c) {
        Iterator<Tile> it = path.iterator();
        if (it.hasNext()) {
            Tile prev = it.next();
            Vec2 end = prev.center();
            while (it.hasNext()) {
                Tile cur = it.next();
                Vec2 prevc = prev.center();
                Vec2 curc = cur.center();
                prev = cur;
                c.drawLine(prevc.x, prevc.y, curc.x, curc.y, Paints.red);
            }
            c.drawCircle(end.x, end.y, 15, Paints.red);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // int lim = e.getPointerCount();
        // for (int i = 0; i < lim; i++) {
        // entities.add(new FadingCircle(e.getX(i), e.getY(i), 12,
        // textPaint));
        // Tile t = map.tileAt(new Vec2(e.getX(i), e.getY(i)));
        // if (t != null) {
        // t.tap();
        // }
        // }
        Tile t = map.tileAt(new Vec2(e.getX(), e.getY()));
        if (t != null && !path.isEmpty()) {
            Tile top = path.peek();
            Tile prev = path.size() > 1 ? path.get(1) : null;
            if (t == prev) {
                path.removeFirst();
            } else if (t != top) {
                if (map.canGoBetween(t, top) && !t.isEdge()) {
                    path.addFirst(t);
                    // t.tap();
                } else if (!map.isClose(t, top)) {
                    path.clear();
                }
            }
        } else if (t != null && !t.isEdge()) {
            path.add(t);
        }

        // Tile t = map.tileAt(new Vec2(e.getX(), e.getY()));
        // if (t != null) {
        // Iterator<Tile> it = path.iterator();
        // loop: while (it.hasNext()) {
        // Tile p = it.next();
        // if (map.canGoBetween(t, p)) {
        // path.add(t);
        // }
        // }
        // }

        return true;
    }
}

class GameThread extends Thread {
    private int delay;
    private Game game;
    private volatile boolean cont = true;

    private long lastTime;

    public GameThread(int fps, Game g) {
        delay = 1000 / fps;
        game = g;
    }

    public void stopGame() {
        cont = false;
    }

    @Override
    public void run() {
        while (cont) {
            long startTime = System.currentTimeMillis();
            game.update(delay);
            game.postInvalidate();
            long delta = startTime - System.currentTimeMillis();
            try {
                Thread.sleep(delay - delta);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}