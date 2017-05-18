package dong.lan.palmcure.api;

import dong.lan.palmcure.model.BaseData;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 */

public interface RegisterApi {

    @FormUrlEncoded
    @POST("user/register/")
    Call<BaseData> register(@Field("username") String username, @Field("passwd") String passwd, @Field("type") int type);
}
