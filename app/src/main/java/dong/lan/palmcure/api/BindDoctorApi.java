package dong.lan.palmcure.api;

import dong.lan.palmcure.model.BaseData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 */

public interface BindDoctorApi {

    @GET("user/patient/bindDoctor.php")
    Call<BaseData> bind(@Query("id") String id,@Query("doctorId") String doctor,@Query("patientId") String patient,@Query("status") int status);
}
