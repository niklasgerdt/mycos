package mycos;

public interface Publisher {

    <V> void push(V v);
}
