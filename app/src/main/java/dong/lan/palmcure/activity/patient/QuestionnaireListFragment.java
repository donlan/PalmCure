package dong.lan.palmcure.activity.patient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import dong.lan.base.BaseItemClickListener;
import dong.lan.base.ui.BaseFragment;
import dong.lan.palmcure.R;
import dong.lan.palmcure.activity.QuestionnaireDetail;
import dong.lan.palmcure.adapter.QuestionnaireAdapter;
import dong.lan.palmcure.api.Client;
import dong.lan.palmcure.api.QuestionnaireApi;
import dong.lan.palmcure.model.BaseData;
import dong.lan.palmcure.model.Questionnaire;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 */

public class QuestionnaireListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseItemClickListener<Questionnaire> {

    private RecyclerView list;
    private SwipeRefreshLayout refreshLayout;
    private QuestionnaireAdapter adapter;

    public static QuestionnaireListFragment newInstance(String uid, String tid) {
        QuestionnaireListFragment fragment = new QuestionnaireListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("doctor", uid);
        bundle.putString("patient",tid);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(content == null){
            content = inflater.inflate(R.layout.base_list,container,false);
            list = (RecyclerView) content.findViewById(R.id.list);
            list.setLayoutManager(new GridLayoutManager(getContext(),1));
            refreshLayout = (SwipeRefreshLayout) content.findViewById(R.id.refresher);
            refreshLayout.setOnRefreshListener(this);
            adapter = new QuestionnaireAdapter(null);
            list.setAdapter(adapter);
        }
        return content;
    }

    @Override
    public void onRefresh() {
        getData();
    }

    /**
     * 获取预约记录
     */
    private void getData() {
        String uid = getArguments().getString("doctor");
        String tid = getArguments().getString("patient");
        QuestionnaireApi api = Client.get().retrofit().create(QuestionnaireApi.class);
        Call<BaseData> call = api.query(uid,tid);
        call.enqueue(new Callback<BaseData>() {
            @Override
            public void onResponse(Call<BaseData> call, Response<BaseData> response) {
                if (response.code() == 200) {
                    BaseData baseData = response.body();
                    if (baseData.code == 0) {
                        List<Questionnaire> contracts = baseData.stringToArray(Questionnaire[].class);
                        adapter.setClickListener(QuestionnaireListFragment.this);
                        adapter.reset(contracts);
                    } else {
                        dialog(baseData.data.toString());
                    }
                } else {
                    toast("获取数据失败");
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<BaseData> call, Throwable t) {
                toast("网络失败：" + t.getMessage());
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onClick(Questionnaire data, int action, int position) {
        Intent intent = new Intent(getContext(), QuestionnaireDetail.class);
        intent.putExtra("questionnaire",data);
        startActivity(intent);
    }
}
