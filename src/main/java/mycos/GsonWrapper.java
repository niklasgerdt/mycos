package mycos;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

final class GsonWrapper {
    private final Gson gson;

    GsonWrapper(final Gson gson) {
	this.gson = gson;
    }

    String toJson(Object object) {
	return gson.toJson(object);
    }

    <V> Optional<V> fromJson(String reply) {
	if (Objects.isNull(reply))
	    return Optional.empty();
	else {
	    Type type = new TypeToken<V>() {}.getType();
	    return Optional.of(gson.fromJson(reply, type));
	}
    }
}
