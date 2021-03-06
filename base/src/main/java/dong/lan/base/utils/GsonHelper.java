package dong.lan.base.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.util.List;

/**
 * Created by 梁桂栋 on 17-1-31 ： 下午8:22.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class GsonHelper {
    private GsonHelper() {
        gson = new GsonBuilder().serializeNulls().create();
    }

    private static GsonHelper instance;
    private Gson gson;

    public static GsonHelper getInstance() {
        if (instance == null) {
            instance = new GsonHelper();
        }
        return instance;
    }

    public String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public <T> T toTarget(String json, Class<T> tClass) {
        return gson.fromJson(json, tClass);
    }

    public <T> List<T> toTargetList(String json,Class<T> tClass){
        return gson.fromJson(json,new TypeToken<List<T>>(){}.getType());
    }
    public <T> T toTarget(Reader reader, Class<T> tClass) {
        return gson.fromJson(reader, tClass);
    }
}
