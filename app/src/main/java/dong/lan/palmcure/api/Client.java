package dong.lan.palmcure.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 */

public class Client {
    private Retrofit retrofit;
    private Gson gson;
    private static Client client = null;

    private Client() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.165.204.54:8088/rsl/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        gson = new GsonBuilder()
                .serializeNulls()
                .create();
    }

    public static Client get() {
        if (client == null)
            client = new Client();
        return client;
    }

    public Gson gson() {
        return gson;
    }

    public Retrofit retrofit() {
        return retrofit;
    }
}
