package dong.lan.palmcure.activity.patient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.List;

import dong.lan.base.BaseItemClickListener;
import dong.lan.base.ui.BaseActivity;
import dong.lan.base.ui.Dialog;
import dong.lan.base.ui.base.Config;
import dong.lan.palmcure.R;
import dong.lan.palmcure.UserManager;
import dong.lan.palmcure.adapter.AllDoctorAdapter;
import dong.lan.palmcure.api.AllDoctorApi;
import dong.lan.palmcure.api.BindDoctorApi;
import dong.lan.palmcure.api.Client;
import dong.lan.palmcure.model.BaseData;
import dong.lan.palmcure.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 */

public class AllDoctorActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseItemClickListener<User>, Toolbar.OnMenuItemClickListener {

    private RecyclerView doctorList;
    private SwipeRefreshLayout refreshLayout;

    private AllDoctorAdapter adapter;

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_doctor);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

//        toolbar.inflateMenu(R.menu.patient_home_menu);
//        toolbar.setOnMenuItemClickListener(this);

        doctorList = (RecyclerView) findViewById(R.id.all_doctor_list);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresher);
        doctorList.setLayoutManager(new GridLayoutManager(this, 1));

        refreshLayout.setOnRefreshListener(this);

        adapter = new AllDoctorAdapter(null, this);

        doctorList.setAdapter(adapter);

        getData();
    }

    @Override
    public void onRefresh() {
        getData();
    }

    /**
     * 获取所有医生信息
     */
    private void getData() {
        AllDoctorApi api = Client.get().retrofit().create(AllDoctorApi.class);
        Call<BaseData> call = api.queryAllDoctor();
        call.enqueue(new Callback<BaseData>() {
            @Override
            public void onResponse(Call<BaseData> call, Response<BaseData> response) {
                if (response.code() == 200) {
                    BaseData baseData = response.body();
                    if (baseData.code == 0) {
                        List<User> contracts = baseData.stringToArray(User[].class);
                        adapter.clearAddAll(contracts);
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
                toast("" + t.getMessage());
                refreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onClick(final User data, int action, int position) {
        new Dialog(this).setMessageText("确定与医生：" + data.getDisplayName() + " 签约绑定吗？")
                .setClickListener(new Dialog.DialogClickListener() {
                    @Override
                    public boolean onDialogClick(int which) {
                        if (which == Dialog.CLICK_RIGHT) {
                            addContract(data);
                        }
                        return true;
                    }
                }).show();
    }

    private void addContract(User data) {
        String patient = UserManager.get().currentUser().id;
        String doctor = data.id;
        BindDoctorApi api = Client.get().retrofit().create(BindDoctorApi.class);
        api.bind(null, doctor, patient, Config.CONTRACT_STATUS_ADD).enqueue(new Callback<BaseData>() {
            @Override
            public void onResponse(Call<BaseData> call, Response<BaseData> response) {
                BaseData baseData = response.body();
                if (baseData.code == 0) {
                    toast("请求签约成功，请等待医生回复");
                } else {
                    dialog(baseData.data.toString());
                }
            }

            @Override
            public void onFailure(Call<BaseData> call, Throwable t) {
                toast(t.getMessage());
            }
        });

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        switch (id) {


        }

        return false;
    }
}
