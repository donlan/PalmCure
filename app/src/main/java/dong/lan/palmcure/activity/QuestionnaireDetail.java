package dong.lan.palmcure.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.ALog;

import java.util.List;

import dong.lan.base.BaseItemClickListener;
import dong.lan.base.ui.BaseActivity;
import dong.lan.base.ui.base.Config;
import dong.lan.base.utils.GsonHelper;
import dong.lan.palmcure.R;
import dong.lan.palmcure.UserManager;
import dong.lan.palmcure.adapter.QuestionHandlerAdapter;
import dong.lan.palmcure.api.Client;
import dong.lan.palmcure.api.QuestionnaireApi;
import dong.lan.palmcure.model.BaseData;
import dong.lan.palmcure.model.Questionnaire;
import dong.lan.palmcure.model.Record;
import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;
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


        GalleryLayoutManager layoutManager = new GalleryLayoutManager(GalleryLayoutManager.HORIZONTAL);
        layoutManager.attach(container);

        layoutManager.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View item, int position) {

            }
        });

        layoutManager.setItemTransformer(new GalleryLayoutManager.ItemTransformer() {
            @Override
            public void transformItem(GalleryLayoutManager layoutManager, View item, float fraction) {

            }
        });

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
        if (data == null) {
            if (UserManager.get().currentUser().type == Config.TYPE_DOCTOR)
                commit(questionnaire, null);
            else
                container.scrollToPosition(1);
        } else {
            if (position == adapter.getItemCount() - 1) {
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
