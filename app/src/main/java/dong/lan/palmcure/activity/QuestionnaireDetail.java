package dong.lan.palmcure.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.ALog;

import java.util.List;

import dong.lan.base.BaseItemClickListener;
import dong.lan.base.ui.BaseActivity;
import dong.lan.base.ui.base.Config;
import dong.lan.base.utils.GsonHelper;
import dong.lan.palmcure.R;
import dong.lan.palmcure.UserManager;
import dong.lan.palmcure.activity.patient.CreateNewAppointment;
import dong.lan.palmcure.adapter.QuestionHandlerAdapter;
import dong.lan.palmcure.api.Client;
import dong.lan.palmcure.api.QuestionnaireApi;
import dong.lan.palmcure.model.BaseData;
import dong.lan.palmcure.model.Questionnaire;
import dong.lan.palmcure.model.Record;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 */

public class QuestionnaireDetail extends BaseActivity implements BaseItemClickListener<Record> {

    private RecyclerView container;
    private QuestionHandlerAdapter adapter;
    private List<Record> questions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire_detail);
        container = (RecyclerView) findViewById(R.id.container);
        container.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getData();
    }

    private Questionnaire questionnaire;

    private void getData() {
        questionnaire = (Questionnaire) getIntent().getSerializableExtra("questionnaire");
        if (questionnaire == null)
            return;
        final QuestionnaireApi api = Client.get().retrofit().create(QuestionnaireApi.class);
        Call<BaseData> call = api.query(questionnaire.id);
        call.enqueue(new Callback<BaseData>() {
            @Override
            public void onResponse(Call<BaseData> call, Response<BaseData> response) {
                if (response.code() == 200) {
                    BaseData baseData = response.body();
                    ALog.d(baseData);
                    if (baseData.code == 0) {
                        questions = baseData.stringToArray(Record[].class);
                        adapter = new QuestionHandlerAdapter(questionnaire, questions);
                        adapter.setClickListener(QuestionnaireDetail.this);
                        container.setAdapter(adapter);

                    } else {
                        dialog(baseData.data.toString());
                    }
                } else {
                    toast("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<BaseData> call, Throwable t) {
                toast("网络失败：" + t.getMessage());
            }
        });
    }

    @Override
    public void onClick(Record data, int action, int position) {
        ALog.d(data);
        ALog.d(action);
        ALog.d(position);
        if (action == 1) {
            if (UserManager.get().currentUser().type == Config.TYPE_DOCTOR && questionnaire.status == Questionnaire.STATUS_TESING) {
                questionnaire.status = Questionnaire.STATUS_DONE;
                commit(questionnaire, null);
            } else if (questionnaire.status == Questionnaire.STATUS_TESING && adapter.getLevel() < questionnaire.level) {
                new AlertDialog.Builder(this)
                        .setMessage("患者问卷答题结果不合格，是否向患者发起再诊预约？")
                        .setPositiveButton("是的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(QuestionnaireDetail.this, CreateNewAppointment.class);
                                intent.putExtra("tid", UserManager.get().currentUser().id);
                                intent.putExtra("uid", questionnaire.patient);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("否", null)
                        .show();

            } else
                container.scrollToPosition(1);
        } else if (action == 2) {
            if (position == adapter.getItemCount() - 1 && UserManager.get().currentUser().type == Config.TYPE_PATIENT) {
                questionnaire.status = Questionnaire.STATUS_TESING;
                commit(questionnaire, questions);
            } else {
                container.scrollToPosition(position + 1);
            }
        }
    }

    private void commit(Questionnaire questionnaire, List<Record> questions) {
        final QuestionnaireApi api = Client.get().retrofit().create(QuestionnaireApi.class);
        ALog.d(Client.get().gson().toJson(questionnaire));
        ALog.d(Client.get().gson().toJson(questions));
        Call<BaseData> call = api.update(GsonHelper.getInstance().toJson(questionnaire), questions == null ? "" : GsonHelper.getInstance().toJson(questions));
        call.enqueue(new Callback<BaseData>() {
            @Override
            public void onResponse(Call<BaseData> call, Response<BaseData> response) {
                if (response.code() == 200) {
                    BaseData baseData = response.body();
                    if (baseData.code == 0) {
                        toast("更新成功");
                        adapter.calScore();
                        adapter.notifyItemChanged(0);
                    } else {
                        dialog(baseData.data.toString());
                    }
                } else {
                    toast("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<BaseData> call, Throwable t) {
                toast("网络失败：" + t.getMessage());
            }
        });
    }
}
