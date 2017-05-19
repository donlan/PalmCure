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
 * 获取医生创建的所有问题的页面
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

    //初始化页面空间，通关过布局文件找到指定view的id
    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        questionList = (RecyclerView) findViewById(R.id.list);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresher);

        //设置列表
        questionList.setLayoutManager(new GridLayoutManager(this, 1));
        //设置列表下拉刷新
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

    //下来刷新开始的方法
    @Override
    public void onRefresh() {
        getData();
    }

    //请求数据
    public void getData() {
        //有Client生成一个Api请求实例
        QuestionApi questionApi = Client.get().retrofit().create(QuestionApi.class);
        //根据实例的具体Api方法发起请求，此处是请求医生创建的所有问题
        questionApi.queryAllQuestion(UserManager.get().currentUser().id)
                .enqueue(new Callback<BaseData>() {
                    //网络放回结果
                    @Override
                    public void onResponse(Call<BaseData> call, Response<BaseData> response) {
                        if (response.code() == 200) { //服务器连接返回码：200表示与与服务器通信成功
                            BaseData baseData = response.body();//服务器返回给我们的数据：客户端会自动将json格式解析成一个类实例
                            if (baseData.code == 0) { //这是我们PHP代码返回的结果码，0表示操作成功
                                ALog.d(baseData);
                                //这里是将返回数据，他是一个json格式的数组，我们通过GSON解析成java的数组
                                List<Question> questions = baseData.stringToArray(Question[].class);
                                //解析成java数组成功
                                if (questions == null)
                                    questions = new ArrayList<Question>();
                                if (questions.isEmpty()) {
                                    toast("无问题");
                                }

                                if (adapter == null) {
                                    //将解析得到的java数组生成一个适配器，适配器的作用就是将数组中的每个数据绑定到一个列表中
                                    //就是你使用APP的过程中看到的列表
                                    adapter = new QuestionAdapter(questions);
                                    adapter.setClickListener(QuestionListActivity.this);
                                    //将适配器设置到列表控件中，界面机会根据适配的绑定规则显示到页面中
                                    questionList.setAdapter(adapter);
                                } else {
                                    adapter.reset(questions);
                                }

                            } else { //网络操作失败，弹出一个对话框提示
                                dialog(baseData.data.toString());
                            }
                        } else { //网络连接失败，提示用户
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
