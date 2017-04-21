package dong.lan.palmcure.api;

import dong.lan.palmcure.model.BaseData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by 梁桂栋 on 2017/4/19.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */

public interface AppointmentsApi {

    String DOCTOR ="doctor";
    String PATIENT ="patient";

    @GET("user/{type}/myAppointment.php")
    Call<BaseData> query(@Path("type") String type,@Query("uid") String uid);
}
