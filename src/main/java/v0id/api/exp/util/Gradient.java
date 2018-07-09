package v0id.api.exp.util;

import com.google.common.collect.Lists;

import javax.vecmath.Vector3f;
import java.util.Comparator;
import java.util.List;

public class Gradient
{
    private List<GradientPoint> keys = Lists.newArrayList();

    public Function4<Gradient, List<GradientPoint>, Float, Vector3f> gradientFunc;

    public static Function4<Gradient, List<GradientPoint>, Float, Vector3f> Linear = (grad, collection, a) ->
    {
        int closestStart = grad.getClosest(a);
        GradientPoint point = collection.get(closestStart);
        GradientPoint next = collection.get((closestStart + 1) % collection.size());
        float aRel = Math.abs((a - point.key) / (next.key - point.key));
        return add(scale(point.color, (1 - aRel)), scale(next.color, aRel));
    };

    public static Function4<Gradient, List<GradientPoint>, Float, Vector3f> cosine = (grad, collection, a) ->
    {
        int closestStart = grad.getClosest(a);
        GradientPoint point = collection.get(closestStart);
        GradientPoint next = collection.get((closestStart + 1) % collection.size());
        float aRelC = (float)(1 - Math.cos(Math.abs((a - point.key) / (next.key - point.key)) * Math.PI)) / 2;
        return add(scale(point.color, (1 - aRelC)), scale(next.color, aRelC));
    };

    public static Function4<Gradient, List<GradientPoint>, Float, Vector3f> cubic = (grad, collection, a) ->
    {
        int closestStart = grad.getClosest(a);
        int preStart = closestStart == 0 ? collection.size() - 1 : closestStart - 1;
        int nextStart = (closestStart + 1) % collection.size();
        int lastStart = (nextStart + 1) % collection.size();
        GradientPoint point = collection.get(closestStart);
        GradientPoint next = collection.get(nextStart);
        float aRel = Math.abs((a - point.key) / (next.key - point.key));
        return grad.cubicInterpolation(new Vector3f[] { collection.get(preStart).color, point.color, next.color, collection.get(lastStart).color }, 1, 2, aRel);
    };

    public Vector3f get(float key)
    {
        return this.keys.get(this.getClosest(key)).color;
    }

    public Vector3f interpolate(float value, Function4<Gradient, List<GradientPoint>, Float, Vector3f> func)
    {
        return func.apply(this, this.keys, value);
    }

    public int getClosest(float value)
    {
        for (int i = 0; i < this.keys.size(); ++i)
        {
            GradientPoint current = this.keys.get(i);
            if (i == this.keys.size() - 1 && i == 0)
            {
                return i;
            }

            GradientPoint next = this.keys.get((i + 1) % this.keys.size());
            if (value >= current.key && value <= next.key)
            {
                return i;
            }
        }

        return this.keys.size() - 1;
    }

    public void add(float key, Vector3f val)
    {
        GradientPoint point = new GradientPoint(key, val);
        this.keys.add(point);
        this.keys.sort(Comparator.naturalOrder());
    }

    public Vector3f cubicInterpolation(Vector3f[] colors, int start, int end, float a)
    {
        Vector3f clr_start = colors[start];
        Vector3f clr_end = colors[end];
        Vector3f clr_before_start = colors[start == 0 ? 3 : start - 1];
        Vector3f clr_after_end = colors[(end + 1) % 4];
        float aSq = a * a;
        Vector3f a0 = add(sub(add(scale(clr_before_start, -0.5F), scale(clr_start, 1.5F)), scale(clr_end, 1.5F)), scale(clr_after_end, 0.5F));
        Vector3f a1 = sub(add(sub(clr_before_start, scale(clr_start, 2.5F)), scale(clr_end, 2)), scale(clr_after_end, 0.5F));
        Vector3f a2 = add(scale(clr_before_start, -0.5F), scale(clr_end, 0.5F));
        return add(add(add(scale(scale(a0, a), aSq), scale(a1, aSq)), scale(a2, a)), clr_start);
    }

    private static Vector3f scale(Vector3f vec, Vector3f val)
    {
        return new Vector3f(vec.getX() * val.getX(), vec.getY() * val.getY(), vec.getZ() * val.getZ());
    }

    private static Vector3f scale(Vector3f vec, float val)
    {
        Vector3f ret = new Vector3f(vec);
        ret.scale(val);
        return ret;
    }

    private static Vector3f add(Vector3f l, Vector3f r)
    {
        Vector3f ret = new Vector3f(l);
        ret.add(r);
        return ret;
    }

    private static Vector3f sub(Vector3f l, Vector3f r)
    {
        Vector3f ret = new Vector3f(l);
        ret.sub(r);
        return ret;
    }

    public static class GradientPoint implements Comparable<GradientPoint>
    {
        public GradientPoint(float key, Vector3f color)
        {
            this.key = key;
            this.color = color;
        }

        public Vector3f color;
        public float key;

        public int compareTo(GradientPoint other)
        {
            float diff = this.key - other.key;
            return diff < 0 ? -1 : diff > 0 ? 1 : 0;
        }
    }
}
