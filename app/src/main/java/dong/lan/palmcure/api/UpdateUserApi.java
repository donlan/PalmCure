package dong.lan.palmcure.api;

import dong.lan.palmcure.model.BaseData;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 */

public interface UpdateUserApi {
    @FormUrlEncoded
    @POST("user/update/")
    Call<BaseData> update(@Field("user") String userJson);
}
