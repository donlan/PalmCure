package dong.lan.palmcure.api;

import dong.lan.palmcure.model.BaseData;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by 梁桂栋 on 2017/4/19.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */

public interface AllDoctorApi {

    @GET("user/patient/allDoctor.php")
    Call<BaseData> queryAllDoctor();
}
