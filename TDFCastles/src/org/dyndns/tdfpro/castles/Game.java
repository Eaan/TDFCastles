package org.dyndns.tdfpro.castles;

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

        gt = new GameThread(25, this);
        gt.start(); // MUST BE LAST IN CONSTRUCTOR
    }

    public void update(int delta) {
        entities.update(this, delta);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawARGB(255, 0, 0, 0);

        entities.render(this, canvas);

        // canvas.drawRect(screenDimension.x - screenDimension.y / 2,
        // screenDimension.y / 2,
        // screenDimension.x, screenDimension.y, Paints.red);
        // canvas.drawText("" + screenDimension + ": " + entities.size(), 50,
        // 50, textPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
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