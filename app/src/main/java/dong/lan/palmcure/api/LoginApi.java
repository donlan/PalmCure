package dong.lan.palmcure.api;

import dong.lan.palmcure.model.BaseData;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 梁桂栋 on 2017/4/18.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */

public interface LoginApi {

    @FormUrlEncoded
    @POST("user/login/")
    Call<BaseData> login(@Field("username") String username, @Field("passwd") String passwd, @Field("type") int type);
}
