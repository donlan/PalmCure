package dong.lan.palmcure.activity.doctor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.blankj.ALog;

import dong.lan.base.ui.BaseActivity;
import dong.lan.library.LabelTextView;
import dong.lan.palmcure.R;
import dong.lan.palmcure.UserManager;
import dong.lan.palmcure.api.Client;
import dong.lan.palmcure.api.QuestionApi;
import dong.lan.palmcure.model.BaseData;
import dong.lan.palmcure.model.Question;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 */

public class CreateQuestionActivity extends BaseActivity {


    private EditText descEt;
    private EditText aEt;
    private EditText bEt;
    private EditText cEt;
    private LabelTextView commit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        initView();
    }

    private void initView() {
        descEt = (EditText) findViewById(R.id.question_desc);
        aEt = (EditText) findViewById(R.id.option_a);
        bEt = (EditText) findViewById(R.id.option_b);
        cEt = (EditText) findViewById(R.id.option_c);
        commit = (LabelTextView) findViewById(R.id.commit);

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitQuestion(descEt.getText().toString(),
                        aEt.getText().toString(),
                        bEt.getText().toString(),
                        cEt.getText().toString());
            }
        });
    }

    //提交问题
    private void commitQuestion(String question,String optionA,String optionB,String optionC) {
        if(TextUtils.isEmpty(question)){
            toast("内容不能为空");
            return;
        }
        if(TextUtils.isEmpty(optionA) || TextUtils.isEmpty(optionB) || TextUtils.isEmpty(optionC)){
            toast("选项不能为空");
            return;
        }

        Question quest = new Question(question,optionA,optionB,optionC);
        ALog.d(quest.toJson());
        QuestionApi questionApi = Client.get().retrofit().create(QuestionApi.class);
        questionApi.createQuestion(UserManager.get().currentUser().id,quest.toJson())
                .enqueue(new Callback<BaseData>() {
            @Override
            public void onResponse(Call<BaseData> call, Response<BaseData> response) {
                if(response.code() == 200){
                    BaseData baseData = response.body();
                    if(baseData.code ==0){
                        toast("提交成功");
                        setResult(1);
                        finish();
                    }else{
                        dialog(baseData.data.toString());
                    }
                }else{
                    toast("网络失败："+response.code());
                }
            }

            @Override
            public void onFailure(Call<BaseData> call, Throwable t) {
                t.printStackTrace();
                toast(t.getMessage());
            }
        });
    }
}
