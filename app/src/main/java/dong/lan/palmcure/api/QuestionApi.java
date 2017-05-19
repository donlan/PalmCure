package dong.lan.palmcure.api;

import dong.lan.palmcure.model.BaseData;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 处理问题的网络请求api
 * createQuestion:创建一个新问题
 * queryAllQuestion：查询用户创建的所有问题
 */

public interface QuestionApi {

    //声明请求方式用POST，请求的地址是 Client中配置的baseUrl+user/doctor/createQuestion.php
    //也就是请求地址是：http://192.168.191.1:8088/rsl/user/doctor/createQuestion.php
    //注意的是，POST请求参数都用(@Field("xxx")声明
    @FormUrlEncoded
    @POST("user/doctor/createQuestion.php")
    Call<BaseData> createQuestion(@Field("uid") String uid, @Field("question") String question);

    //下面是用GET发送网络请求，此时请求参数用@Query("xxx")声明
    @GET("user/doctor/optionQuestions.php")
    Call<BaseData> queryAllQuestion(@Query("uid") String uid);
}
