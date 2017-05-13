package dong.lan.palmcure.activity.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.blankj.ALog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import dong.lan.base.BaseItemClickListener;
import dong.lan.base.ui.BaseActivity;
import dong.lan.palmcure.R;
import dong.lan.palmcure.UserManager;
import dong.lan.palmcure.adapter.QuestionAdapter;
import dong.lan.palmcure.api.Client;
import dong.lan.palmcure.api.QuestionApi;
import dong.lan.palmcure.model.BaseData;
import dong.lan.palmcure.model.Question;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 */

public class QuestionListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseItemClickListener<Question> {
    private RecyclerView questionList;
    private SwipeRefreshLayout refreshLayout;
    private Toolbar toolbar;
    private QuestionAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        questionList = (RecyclerView) findViewById(R.id.list);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresher);

        questionList.setLayoutManager(new GridLayoutManager(this, 1));
        refreshLayout.setOnRefreshListener(this);

        int mode = getIntent().getIntExtra("mode", QuestionAdapter.MODE_NONE);
        QuestionAdapter.MODE = mode;
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.question_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.question_add:
                Intent addQuestIntent = new Intent(this, CreateQuestionActivity.class);
                startActivityForResult(addQuestIntent, 1);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        getData();
    }

    public void getData() {
        QuestionApi questionApi = Client.get().retrofit().create(QuestionApi.class);
        questionApi.queryAllQuestion(UserManager.get().currentUser().id)
                .enqueue(new Callback<BaseData>() {
                    @Override
                    public void onResponse(Call<BaseData> call, Response<BaseData> response) {
                        if (response.code() == 200) {
                            BaseData baseData = response.body();
                            if (baseData.code == 0) {
                                ALog.d(baseData);
                                List<Question> questions = baseData.stringToArray(Question[].class);
                                if (questions == null)
                                    questions = new ArrayList<Question>();
                                if (questions.isEmpty()) {
                                    toast("无问题");
                                }
                                if (adapter == null) {
                                    adapter = new QuestionAdapter(questions);
                                    adapter.setClickListener(QuestionListActivity.this);
                                    questionList.setAdapter(adapter);
                                } else {
                                    adapter.reset(questions);
                                }

                            } else {
                                dialog(baseData.data.toString());
                            }
                        } else {
                            toast("网络失败：" + response.code());
                        }
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<BaseData> call, Throwable t) {
                        t.printStackTrace();
                        toast(t.getMessage());
                        refreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onClick(Question data, int action, int position) {
        if (action == 1) { //普通点击

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 1) {
            getData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(QuestionAdapter.MODE == QuestionAdapter.MODE_SELECT && adapter!=null){
            EventBus.getDefault().post(adapter.getSelect());
        }
        QuestionAdapter.MODE = QuestionAdapter.MODE_NONE;
    }
}
