package v0id.api.exp.util;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by V0idWa1k3r on 28-Jun-17.
 */
public class Holder<T>
{
    private T t;

    public Holder(T t)
    {
        this.t = t;
    }

    private Holder()
    {

    }

    public T get()
    {
        return this.t;
    }

    public void set(T t)
    {
        this.t = t;
    }

    public static <T>Holder<T> of(T t)
    {
        return new Holder<>(t);
    }

    public void compute(Function<T, T> func)
    {
        this.set(func.apply(this.get()));
    }

    public boolean match(Predicate<T> matcher)
    {
        return matcher.test(this.get());
    }

    public <R>Holder<R> map(Function<T, R> mapper)
    {
        return Holder.of(mapper.apply(this.get()));
    }

    public <R>R mapAndGet(Function<T, R> mapper)
    {
        return mapper.apply(this.get());
    }

    @Override
    public int hashCode()
    {
        return this.t != null ? this.t.hashCode() : super.hashCode();
    }

    @Override
    public String toString()
    {
        return this.t != null ? this.t.toString() : super.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        return this.t != null ? this.t.equals(obj) : super.equals(obj);
    }
}
