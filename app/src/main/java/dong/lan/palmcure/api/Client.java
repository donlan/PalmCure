package dong.lan.palmcure.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 梁桂栋 on 2017/4/18.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */

public class Client {
    private Retrofit retrofit;
    private Gson gson;
    private static Client client = null;

    private Client() {
        retrofit = new Retrofit.Builder()
                .baseUrl("10.158.192.124:8088/")
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
