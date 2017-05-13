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

import java.util.List;

import dong.lan.base.BaseItemClickListener;
import dong.lan.base.ui.BaseActivity;
import dong.lan.base.ui.Dialog;
import dong.lan.base.ui.base.Config;
import dong.lan.palmcure.R;
import dong.lan.palmcure.UserManager;
import dong.lan.palmcure.adapter.PatientContractAdapter;
import dong.lan.palmcure.api.AllPatientsApi;
import dong.lan.palmcure.api.BindDoctorApi;
import dong.lan.palmcure.api.Client;
import dong.lan.palmcure.model.BaseData;
import dong.lan.palmcure.model.Contract;
import dong.lan.palmcure.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorHomeActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener, BaseItemClickListener<Contract> {

    private RecyclerView doctorList;
    private SwipeRefreshLayout refreshLayout;
    private Toolbar toolbar;
    private PatientContractAdapter adapter;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = UserManager.get().currentUser();
        if (user.verify != 1) {
            startActivityForResult(new Intent(this, VerifyActivity.class), 1);
        }

        setContentView(R.layout.activity_doctor_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(this);

        doctorList = (RecyclerView) findViewById(R.id.patient_list);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresher);
        doctorList.setLayoutManager(new GridLayoutManager(this, 1));

        refreshLayout.setOnRefreshListener(this);

        adapter = new PatientContractAdapter(null, this);

        doctorList.setAdapter(adapter);

        getData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.doctor_home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return onMenuItemClick(item);
    }

    @Override
    public void onRefresh() {
        getData();
    }

    private void getData() {
        AllPatientsApi api = Client.get().retrofit().create(AllPatientsApi.class);
        Call<BaseData> call = api.queryAllDoctor(UserManager.get().currentUser().id);
        call.enqueue(new Callback<BaseData>() {
            @Override
            public void onResponse(Call<BaseData> call, Response<BaseData> response) {
                if (response.code() == 200) {
                    BaseData baseData = response.body();
                    if (baseData.code == 0) {
                        List<Contract> contracts = baseData.stringToArray(Contract[].class);
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
    public void onClick(final Contract data, int action, int position) {
        if (data.status == Config.CONTRACT_STATUS_ADD) {
            new Dialog(this)
                    .setMessageText("同意该患者签约请求？")
                    .setClickListener(new Dialog.DialogClickListener() {
                        @Override
                        public boolean onDialogClick(int which) {
                            if (which == Dialog.CLICK_RIGHT) {
                                agreeContract(data);
                            }
                            return true;
                        }
                    }).show();
        } else if (data.status == Config.CONTRACT_STATUS_VERIFY) {
            Intent intent = new Intent(this, DoctorPatientActivity.class);
            intent.putExtra("id", data.patient);
            startActivity(intent);
        }
    }

    private void agreeContract(Contract data) {
        BindDoctorApi api = Client.get().retrofit().create(BindDoctorApi.class);
        api.bind(data.id, data.doctor, data.patient, Config.CONTRACT_STATUS_VERIFY)
                .enqueue(new Callback<BaseData>() {
                    @Override
                    public void onResponse(Call<BaseData> call, Response<BaseData> response) {
                        if (response.code() == 200) {
                            BaseData baseData = response.body();
                            if (baseData.code == 0) {
                                toast("回复绑定成功");
                            } else {
                                dialog(baseData.data.toString());
                            }
                        } else {
                            dialog("回复失败，网络连接错误");
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseData> call, Throwable t) {
                        toast("回复失败");
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.doctor_logout:
                UserManager.get().logout();
                finish();
                break;
            case R.id.doctor_me:
                startActivity(new Intent(this, DoctorCenter.class));
                break;
            case R.id.doctor_questions:
                startActivity(new Intent(this, QuestionListActivity.class));
                break;
        }

        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1)
            finish();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
