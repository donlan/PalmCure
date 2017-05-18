package dong.lan.palmcure.model;

import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;

import dong.lan.palmcure.api.Client;

/**
 */

public class BaseData {

    public int code;
    public Object data;

    public <T> T toTarget(Class<T> tClass) {
        return Client.get().gson().fromJson(data.toString(), tClass);
    }
    public  <T> List<T> stringToArray( Class<T[]> clazz) {
        T[] arr = Client.get().gson().fromJson(data.toString(), clazz);
        return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }

    public <T> List<T> toTarget() {
        return Client.get().gson().fromJson(data.toString(), new TypeToken<T>(){}.getType());
    }

    @Override
    public String toString() {
        return "BaseData{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
