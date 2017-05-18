package dong.lan.palmcure.api;

import dong.lan.palmcure.model.BaseData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 */

public interface AllPatientsApi {

    @GET("user/doctor/allContract.php")
    Call<BaseData> queryAllDoctor(@Query("uid")String uid);
}
