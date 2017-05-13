package dong.lan.palmcure.activity.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import dong.lan.base.ui.BaseActivity;
import dong.lan.base.utils.GsonHelper;
import dong.lan.library.LabelTextView;
import dong.lan.palmcure.R;
import dong.lan.palmcure.UserManager;
import dong.lan.palmcure.adapter.QuestionAdapter;
import dong.lan.palmcure.api.Client;
import dong.lan.palmcure.api.QuestionnaireApi;
import dong.lan.palmcure.model.BaseData;
import dong.lan.palmcure.model.Question;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 创建问卷
 */

public class CreateQuestionnaire extends BaseActivity {


    private EditText intro;
    private TextView optionRes;
    private LabelTextView optionSelect;
    private LabelTextView commit;
    private RadioGroup levelGroup;


    private String patientId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_questinnaire);
        patientId = getIntent().getStringExtra("id");
        initView();
    }

    private void initView() {
        intro = (EditText) findViewById(R.id.qn_intro);
        optionSelect = (LabelTextView) findViewById(R.id.option_select);
        optionRes = (TextView) findViewById(R.id.option_res);
        commit = (LabelTextView) findViewById(R.id.commit_qn);
        levelGroup = (RadioGroup) findViewById(R.id.level_group);

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitQuestionnaire();
            }
        });

        optionSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateQuestionnaire.this, QuestionListActivity.class);
                intent.putExtra("mode", QuestionAdapter.MODE_SELECT);
                startActivity(intent);
            }
        });
        EventBus.getDefault().register(this);
    }

    private List<String> qid;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOptionSelect(List<Question> questions) {
        int count = 0;
        if (questions != null) {
            if (qid == null)
                qid = new ArrayList<>();
            qid.clear();
            optionRes.setText("");
            int i = 1;
            for (Question question : questions) {
                optionRes.append(String.valueOf(i++));
                optionRes.append(" : ");
                optionRes.append(question.qdescribe);
                optionRes.append("\n\n");
                count += question.getScore();
                qid.add(question.id);
            }
        }
        optionRes.append("总分数：" + count);
    }

    private void commitQuestionnaire() {
        String introStr = intro.getText().toString();
        if(TextUtils.isEmpty(introStr)){
            dialog("无问卷描述");
            return;
        }
        if (qid.isEmpty()){
            dialog("无问题");
            return;
        }
        int level = 0;
        int id = levelGroup.getCheckedRadioButtonId();
        if (id == R.id.option_a) {
            level = 8;
        } else if (id == R.id.option_b) {
            level = 6;
        } else if (id == R.id.option_c) {
            level = 5;
        }
        Client.get().retrofit().create(QuestionnaireApi.class)
                .createQuestionnaire(UserManager.get().currentUser().id,
                        patientId, level, introStr,
                        GsonHelper.getInstance().toJson(qid))
                .enqueue(new Callback<BaseData>() {
                    @Override
                    public void onResponse(Call<BaseData> call, Response<BaseData> response) {
                        if (response.code() == 200) {
                            BaseData baseData = response.body();
                            if (baseData.code == 0) {
                                toast("创建问卷成功");
                            } else {
                                dialog(baseData.data.toString());
                            }
                        } else {
                            toast("网络失败："+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseData> call, Throwable t) {
                        toast("网络失败：" + t.getMessage());
                        t.printStackTrace();
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
