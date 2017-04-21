package dong.lan.palmcure.activity.patient;

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
import dong.lan.base.ui.base.Config;
import dong.lan.palmcure.R;
import dong.lan.palmcure.UserManager;
import dong.lan.palmcure.adapter.PatientContractAdapter;
import dong.lan.palmcure.api.AllPatientContractApi;
import dong.lan.palmcure.api.Client;
import dong.lan.palmcure.model.BaseData;
import dong.lan.palmcure.model.Contract;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 */

public class PatientHomeActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseItemClickListener<Contract>, Toolbar.OnMenuItemClickListener {

    private RecyclerView doctorList;
    private SwipeRefreshLayout refreshLayout;

    private PatientContractAdapter adapter;

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.inflateMenu(R.menu.patient_home_menu);

        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(this);

        doctorList = (RecyclerView) findViewById(R.id.doctor_list);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresher);
        doctorList.setLayoutManager(new GridLayoutManager(this, 1));

        refreshLayout.setOnRefreshListener(this);

        adapter = new PatientContractAdapter(null, this);

        doctorList.setAdapter(adapter);

        getData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.patient_home_menu, menu);
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

    /**
     * 获取绑定的所有医生信息
     */
    private void getData() {
        AllPatientContractApi api = Client.get().retrofit().create(AllPatientContractApi.class);
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
    public void onClick(Contract data, int action, int position) {
        if(data.status ==Config.CONTRACT_STATUS_VERIFY ){

        }else{
            toast("医生未回复签约");
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.patient_all_doctor:
                startActivity(new Intent(this, AllDoctorActivity.class));
                break;
            case R.id.patient_logout:
                UserManager.get().logout();
                finish();
                break;
            case R.id.patient_me:
                startActivity(new Intent(this, PatientCenter.class));
                break;
        }

        return false;
    }
}
