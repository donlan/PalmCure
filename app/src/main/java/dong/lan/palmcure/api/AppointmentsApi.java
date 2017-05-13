package dong.lan.palmcure.api;

import dong.lan.palmcure.model.BaseData;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 */

public interface AppointmentsApi {

    String DOCTOR ="doctor";
    String PATIENT ="patient";

    @GET("user/{type}/myAppointment.php")
    Call<BaseData> query(@Path("type") String type,@Query("uid") String uid,@Query("tid") String tid);


    @FormUrlEncoded
    @POST("user/patient/appointment.php")
    Call<BaseData> create(@Field("doctorId") String doctorId, @Field("patient") String patient,@Field("reason") String reason,@Field("booktime") String booktime, @Field("status") int status);

    @FormUrlEncoded
    @POST("user/patient/appointment.php")
    Call<BaseData> update(@Field("id") String id,@Field("appointment") String appointment);
}
