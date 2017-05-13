package dong.lan.palmcure.api;

import dong.lan.palmcure.model.BaseData;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 */

public interface QuestionApi {

    @FormUrlEncoded
    @POST("user/doctor/createQuestion.php")
    Call<BaseData> createQuestion(@Field("uid") String uid, @Field("question") String question);

    @GET("user/doctor/optionQuestions.php")
    Call<BaseData> queryAllQuestion(@Query("uid") String uid);
}
