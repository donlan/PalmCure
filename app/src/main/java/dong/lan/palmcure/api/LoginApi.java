package dong.lan.palmcure.api;

import dong.lan.palmcure.model.BaseData;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 登录的网络请求Api
 */

public interface LoginApi {

    @FormUrlEncoded
    @POST("user/login/") //声明请求方式用POST，请求的地址是 Client中配置的baseUrl+user/login/
    Call<BaseData> login(@Field("username") String username, @Field("passwd") String passwd, @Field("type") int type);
    //注意的是，POST请求参数都用(@Field("xxx")声明
}
