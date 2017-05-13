package dong.lan.palmcure.activity.patient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import dong.lan.base.ui.BaseActivity;
import dong.lan.base.utils.DateUtils;
import dong.lan.library.LabelTextView;
import dong.lan.palmcure.R;
import dong.lan.palmcure.UserManager;
import dong.lan.palmcure.api.AppointmentsApi;
import dong.lan.palmcure.api.Client;
import dong.lan.palmcure.model.Appointment;
import dong.lan.palmcure.model.BaseData;
import dong.lan.palmcure.utils.DateTimePicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 */

public class CreateNewAppointment extends BaseActivity implements DateTimePicker.CallBack {

    private EditText reason;
    private TextView time;
    private LabelTextView commit;
    private long timeMills;
    private String tid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);
        tid = getIntent().getStringExtra("tid");
        initView();
    }

    private DateTimePicker dateTimePicker;
    private void initView() {
        reason  = (EditText) findViewById(R.id.appoint_reason);
        time = (TextView) findViewById(R.id.appoint_time);
        commit = (LabelTextView) findViewById(R.id.appoint_commit);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAppointment();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    if(dateTimePicker == null)
                        dateTimePicker = new DateTimePicker(CreateNewAppointment.this,CreateNewAppointment.this);
                    dateTimePicker.show();
                }
        });
    }

    private void newAppointment() {
        final String reasonStr = reason.getText().toString();
        Appointment appointment = new Appointment();
        appointment.status = Appointment.STATUS_NEW ;
        appointment.booktime = DateUtils.getTime(timeMills,"yyyy-MM-dd HH:mm:ss");
        appointment.doctor = tid;
        appointment.reason = reasonStr;
        appointment.patient = UserManager.get().currentUser().id;
        Client.get().retrofit().create(AppointmentsApi.class)
                .create(tid,UserManager.get().currentUser().id,reasonStr,
                        DateUtils.getTime(timeMills,"yyyy-MM-dd HH:mm:ss"),Appointment.STATUS_NEW)
                .enqueue(new Callback<BaseData>() {
                    @Override
                    public void onResponse(Call<BaseData> call, Response<BaseData> response) {
                        if (response.code() == 200) {
                            BaseData baseData = response.body();
                            if (baseData.code == 0) {
                                toast("提交预约成功");
                            } else {
                                dialog(baseData.data.toString());
                            }
                        } else {
                            toast("网络出错："+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseData> call, Throwable t) {
                        t.printStackTrace();
                        toast("网络异常："+t.getMessage());
                    }
                });
    }

    @Override
    public void onClose(long time) {
        timeMills = time;
        this.time.setText(DateUtils.getTime(time,"预约时间：yyyy年MM月dd日 HH:mm"));
    }
}
