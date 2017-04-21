package dong.lan.palmcure.activity.patient;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import dong.lan.base.ui.BaseActivity;
import dong.lan.base.ui.Dialog;
import dong.lan.base.ui.base.SPHelper;
import dong.lan.library.LabelTextView;
import dong.lan.palmcure.R;
import dong.lan.palmcure.UserManager;
import dong.lan.palmcure.api.Client;
import dong.lan.palmcure.api.UpdateUserApi;
import dong.lan.palmcure.model.BaseData;
import dong.lan.palmcure.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 梁桂栋 on 2017/4/20.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */

public class PatientCenter extends BaseActivity implements View.OnClickListener, Dialog.DialogClickListener {

    private LabelTextView username;
    private LabelTextView tel;
    private LabelTextView sex;
    private LabelTextView appointment;
    private LabelTextView questionnaire;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_patient_center);

        username = (LabelTextView) findViewById(R.id.username);
        tel = (LabelTextView) findViewById(R.id.tel);
        sex = (LabelTextView) findViewById(R.id.sex);
        appointment = (LabelTextView) findViewById(R.id.appointment_record);
        questionnaire = (LabelTextView) findViewById(R.id.questionnaire_record);

        username.setOnClickListener(this);

        tel.setOnClickListener(this);

        sex.setOnClickListener(this);

        appointment.setOnClickListener(this);

        questionnaire.setOnClickListener(this);


        initView();
    }

    private void initView() {
        user = UserManager.get().currentUser();

        username.setText(user.getDisplayName());

        tel.setText(user.getTelDisplay());

        sex.setText(user.getSexDisplay());
        if (user.sex == 0 || user.sex == 1)
            sex.setEnabled(false);
        dialog = new Dialog(this);
        dialog.setView(R.layout.dialog_one_edit_input);
        input = (EditText) dialog.findView(R.id.dialog_input);
        dialog.setClickListener(this);
        dialog.setRemoveViews(false);
    }


    private Dialog dialog;
    private EditText input;

    private int tag = -1;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.username:
                tag = 1;
                input.setText("");
                dialog.setMessageText("输入姓名");
                input.setHint("输入姓名");
                dialog.show();
                break;
            case R.id.tel:
                tag = 2;
                input.setText("");
                dialog.setMessageText("联系电话");
                input.setHint("联系电话");
                dialog.show();
                break;
            case R.id.sex:
                new AlertDialog.Builder(this)
                        .setTitle("选择性别")
                        .setSingleChoiceItems(new CharSequence[]{"男", "女"}, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sex.setText(which == 0 ? "男" : "女");
                                user.sex = which;
                            }
                        }).show();
                break;
            case R.id.appointment_record:
                startActivity(new Intent(this, MyAppointmentActivity.class));
                break;
            case R.id.questionnaire_record:

                break;

        }
    }

    @Override
    public boolean onDialogClick(int which) {
        if (which == Dialog.CLICK_RIGHT) {
            if (tag == 1) {
                username.setText(input.getText().toString());
                user.nickname = input.getText().toString();
            } else if (tag == 2) {
                tel.setText(input.getText().toString());
                user.phone = input.getText().toString();
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String userJson = Client.get().gson().toJson(user);
        SPHelper.instance().putString("user", userJson);
        UpdateUserApi api = Client.get().retrofit().create(UpdateUserApi.class);
        Log.d("TAG", "" + SPHelper.instance().getString("user"));
        api.update(user).enqueue(new Callback<BaseData>() {
            @Override
            public void onResponse(Call<BaseData> call, Response<BaseData> response) {
                Log.d("TAG", "" + response.body());
            }

            @Override
            public void onFailure(Call<BaseData> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }
}
