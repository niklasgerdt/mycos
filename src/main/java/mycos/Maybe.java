package mycos;

import java.util.*;

public class Maybe<T> {
    private final Optional<Later<T>> optional;

    Maybe(Optional<Later<T>> t) {
	this.optional = t;
    }

    public boolean has() {
	return optional.isPresent();
    }

    public Later<T> get() {
	return optional.get();
    }
}
