package dong.lan.palmcure.activity.patient;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.blankj.ALog;

import java.util.List;

import dong.lan.base.BaseItemClickListener;
import dong.lan.base.ui.BaseFragment;
import dong.lan.base.ui.Dialog;
import dong.lan.base.ui.base.Config;
import dong.lan.base.utils.DateUtils;
import dong.lan.palmcure.R;
import dong.lan.palmcure.UserManager;
import dong.lan.palmcure.adapter.PatientAppointmentAdapter;
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

public class AppointmentFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseItemClickListener<Appointment>, DateTimePicker.CallBack {

    private RecyclerView list;
    private SwipeRefreshLayout refreshLayout;
    private PatientAppointmentAdapter adapter;
    private Dialog dialog;
    private EditText input;
    private DateTimePicker dateTimePicker;

    public static AppointmentFragment newInstance(String type, String uid, String tid) {
        AppointmentFragment fragment = new AppointmentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        bundle.putString("tid", tid);
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (content == null) {
            content = inflater.inflate(R.layout.base_list, container, false);
            list = (RecyclerView) content.findViewById(R.id.list);
            list.setLayoutManager(new GridLayoutManager(getContext(), 1));
            refreshLayout = (SwipeRefreshLayout) content.findViewById(R.id.refresher);
            refreshLayout.setOnRefreshListener(this);
            adapter = new PatientAppointmentAdapter(null, this);
            list.setAdapter(adapter);
            getData();
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
        String uid = getArguments().getString("uid");
        String tid = getArguments().getString("tid");
        String type = getArguments().getString("type");
        Log.d("TAG", "getData: " + uid + "   \n " + tid);
        AppointmentsApi api = Client.get().retrofit().create(AppointmentsApi.class);
        Call<BaseData> call = api.query(type, uid, tid);
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
                toast("网络失败：" + t.getMessage());
                refreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onClick(final Appointment data, int action, final int position) {
        if (UserManager.get().currentUser().type == Config.TYPE_DOCTOR) {
            if (action == 0) {
                new Dialog(getContext())
                        .setMessageText("同意此预约？")
                        .setRightText("同意")
                        .setLeftText("拒绝")
                        .setClickListener(new Dialog.DialogClickListener() {
                            @Override
                            public boolean onDialogClick(int which) {
                                data.status = which == Dialog.CLICK_RIGHT ? Config.APPOINTMENT_WAIT : Config.APPOINTMENT_REJECT;
                                updateAppointment(data);
                                return true;
                            }
                        }).show();
            } else if (action == 1 && data.status != Config.APPOINTMENT_FINISH) {
                new AlertDialog.Builder(getContext())
                        .setTitle("更改预约时间")
                        .setMessage("当前时间不符合，请求更新预约时间？")
                        .setPositiveButton("更改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setAppointmentTime(data, position);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        }
    }


    private int curIndex = -1;
    private Appointment curAppointment;

    private void setAppointmentTime(Appointment data, int position) {
        if (dateTimePicker == null) {
            dateTimePicker = new DateTimePicker(getContext(), AppointmentFragment.this);
            dateTimePicker.reset(System.currentTimeMillis());
        }
        dateTimePicker.show();
        curIndex = position;
        curAppointment = data;
    }

    private void updateAppointment(Appointment appointment) {

        ALog.d(appointment.id);
        ALog.d( Client.get().gson().toJson(appointment));
        Client.get().retrofit().create(AppointmentsApi.class)
                .update(appointment.id, Client.get().gson().toJson(appointment))
                .enqueue(new Callback<BaseData>() {
                    @Override
                    public void onResponse(Call<BaseData> call, Response<BaseData> response) {
                        if (response.code() == 200) {
                            BaseData baseData = response.body();
                            if (baseData.code == 0) {
                                toast("更新成功");
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
    public void onClose(long time) {
        if (curIndex != -1 && curAppointment != null) {
            curAppointment.booktime = DateUtils.getTime(time, "yyyy-MM-dd HH:mm:ss");
            adapter.notifyItemChanged(curIndex);
            updateAppointment(curAppointment);
        }
    }
}
