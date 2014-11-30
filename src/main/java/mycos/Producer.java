package mycos;

import com.google.gson.Gson;

public class Producer {

    <T> void send(T t) {
        Gson gson = new Gson();
        String json = gson.toJson(t);

    }

}
