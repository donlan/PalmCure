package dong.lan.palmcure.api;

import dong.lan.palmcure.model.BaseData;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 */

public interface AllDoctorApi {

    @GET("user/patient/allDoctor.php")
    Call<BaseData> queryAllDoctor();
}
