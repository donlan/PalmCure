package dong.lan.palmcure.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 */

public class Client {
    private Retrofit retrofit;
    private Gson gson;
    private static Client client = null;

    private Client() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS) //网络请求超时设置
                .writeTimeout(5,TimeUnit.SECONDS)
                .connectTimeout(5,TimeUnit.SECONDS)
                .build();
        //配置Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.191.1:8088/rsl/") //服务器地址
                .client(okHttpClient) //网络访问控制
                .addConverterFactory(GsonConverterFactory.create()) //服务器返回结果自动通过GSON解析
                .build();
        //配置GSON用来做json数据的解析
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
