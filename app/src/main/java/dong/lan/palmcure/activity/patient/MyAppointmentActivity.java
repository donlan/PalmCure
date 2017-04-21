package dong.lan.palmcure.activity.patient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import dong.lan.base.BaseItemClickListener;
import dong.lan.base.ui.BaseActivity;
import dong.lan.palmcure.R;
import dong.lan.palmcure.UserManager;
import dong.lan.palmcure.adapter.PatientAppointmentAdapter;
import dong.lan.palmcure.api.AppointmentsApi;
import dong.lan.palmcure.api.Client;
import dong.lan.palmcure.model.Appointment;
import dong.lan.palmcure.model.BaseData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 */

public class MyAppointmentActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseItemClickListener<Appointment> {

    private RecyclerView appointmentList;
    private SwipeRefreshLayout refreshLayout;

    private PatientAppointmentAdapter adapter;

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);


        appointmentList = (RecyclerView) findViewById(R.id.appointment_list);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresher);
        appointmentList.setLayoutManager(new GridLayoutManager(this, 1));

        refreshLayout.setOnRefreshListener(this);

        adapter = new PatientAppointmentAdapter(null, this);

        appointmentList.setAdapter(adapter);

        getData();
    }




    @Override
    public void onRefresh() {
        getData();
    }

    /**
     * 获取绑定的所有医生信息
     */
    private void getData() {
        AppointmentsApi api = Client.get().retrofit().create(AppointmentsApi.class);
        Call<BaseData> call = api.query(AppointmentsApi.PATIENT,UserManager.get().currentUser().id);
        call.enqueue(new Callback<BaseData>() {
            @Override
            public void onResponse(Call<BaseData> call, Response<BaseData> response) {
                if (response.code() == 200) {
                    BaseData baseData = response.body();
                    if (baseData.code == 0) {
                        List<Appointment> contracts = baseData.stringToArray(Appointment[].class);
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
    public void onClick(Appointment data, int action, int position) {

    }

}
