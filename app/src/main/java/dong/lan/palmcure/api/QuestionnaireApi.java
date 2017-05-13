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

public interface QuestionnaireApi {

    @FormUrlEncoded
    @POST("user/doctor/createQuestionnaire.php")
    Call<BaseData> createQuestionnaire(@Field("doctor") String doctor,
                                       @Field("patient") String patient,
                                       @Field("level") int level,
                                       @Field("intro") String intro,
                                       @Field("questions") String questions);



    @GET("user/patient/myQuestionnaire.php")
    Call<BaseData> query(@Query("doctor") String doctorId, @Query("patient") String patientId);

    @GET("user/patient/myQuestionnaire.php")
    Call<BaseData> query(@Query("id") String id,@Query("doctor") String doctorId,@Query("patient") String patientId);

    @GET("user/patient/myQuestionnaire.php")
    Call<BaseData> query(@Query("id") String id);

    @FormUrlEncoded
    @POST("user/doctor/updateQuestionnaire.php")
    Call<BaseData> update(@Field("questionnaire") String questionnaire,@Field("questions") String questions);
}
