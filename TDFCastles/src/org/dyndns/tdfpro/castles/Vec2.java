package org.dyndns.tdfpro.castles;

import android.graphics.Point;

public class Vec2 {
    public static final Vec2 ZERO = new Vec2(0, 0);
    public static final Vec2 NORTH = new Vec2(0, 1);
    public static final Vec2 SOUTH = new Vec2(0, -1);
    public static final Vec2 WEST = new Vec2(-1, 0);
    public static final Vec2 EAST = new Vec2(1, 0);

    public final float x;
    public final float y;

    public Vec2(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(final Point p) {
        x = p.x;
        y = p.y;
    }

    public Vec2 add(final Vec2 o) {
        return new Vec2(x + o.x, y + o.y);
    }

    public Vec2 sub(final Vec2 o) {
        return new Vec2(x - o.x, y - o.y);
    }

    public Vec2 set(final float x, final float y) {
        return new Vec2(x, y);
    }

    public Vec2 mul(final float f) {
        return new Vec2(f * x, f * y);
    }

    @Override
    public boolean equals(final Object o) {
        if (o != null && o.getClass() == getClass()) {
            Vec2 v = (Vec2) o;
            return x == v.x && y == v.y;
        }
        return false;
    }

    public float dot(final Vec2 o) {
        return x * o.x + y * o.y;
    }

    public float distance(final Vec2 o) {
        return sub(o).length();
    }

    public float distanceSquared(final Vec2 o) {
        return sub(o).lengthSquared();
    }

    @Override
    public int hashCode() {
        return 31 * Float.floatToIntBits(x) + Float.floatToIntBits(y);
    }

    public Vec2 negate() {
        return new Vec2(-x, -y);
    }

    public Vec2 negateLocal() {
        return negate();
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float lengthSquared() {
        return x * x + y * y;
    }

    public Vec2 normalise() {
        final float l = length();
        return new Vec2(x / l, y / l);
    }

    public Vec2 scale(final float a) {
        final float l = length();
        return new Vec2(x * a / l, y * a / l);
    }

    public Vec2 copy() {
        return this;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
