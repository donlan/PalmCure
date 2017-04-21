package dong.lan.palmcure.activity.doctor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import dong.lan.base.ui.BaseActivity;
import dong.lan.base.ui.base.SPHelper;
import dong.lan.palmcure.R;
import dong.lan.palmcure.UserManager;
import dong.lan.palmcure.api.Client;
import dong.lan.palmcure.api.VerifyApi;
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

public class VerifyActivity extends BaseActivity {


    private EditText idInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        idInput = (EditText) findViewById(R.id.idcard_input);

        findViewById(R.id.verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });
    }



    private boolean ok = false;

    private void verify() {
        VerifyApi api = Client.get().retrofit().create(VerifyApi.class);
        api.verify(UserManager.get().currentUser().id, idInput.getText().toString())
                .enqueue(new Callback<BaseData>() {
                    @Override
                    public void onResponse(Call<BaseData> call, Response<BaseData> response) {
                        if (response.code() == 200) {
                            BaseData baseData = response.body();
                            if (baseData.code == 0) {
                                toast("认证成功");
                                User user = UserManager.get().currentUser();
                                user.verify = 1;
                                SPHelper.instance().putString("user", Client.get().gson().toJson(user));
                                ok = true;
                                finish();
                            } else {
                                dialog(baseData.data.toString());
                            }
                        } else {
                            toast("网络异常");
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseData> call, Throwable t) {
                        toast("网络异常");
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public void finish() {
        setResult(ok?1:-1);
        super.finish();
    }
}
