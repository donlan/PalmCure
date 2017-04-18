/*
 *   Copyright 2016, donlan(梁桂栋)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *   Email me: stonelavender@hotmail.com
 */

package dong.lan.palmcure.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.OnClick;
import dong.lan.base.ui.BaseActivity;
import dong.lan.base.ui.base.Config;
import dong.lan.palmcure.MainActivity;
import dong.lan.palmcure.R;
import dong.lan.palmcure.feature.presenter.ILoginPresenter;
import dong.lan.palmcure.feature.view.ILoginView;
import dong.lan.palmcure.presentation.LoginPresenter;

public class LoginActivity extends BaseActivity implements ILoginView {

    @BindView(R.id.login_password)
    EditText passwordET;
    @BindView(R.id.login_username)
    EditText usernameEt;

    @OnClick(R.id.login)
    void login() {
        String username = usernameEt.getText().toString();
        String password = passwordET.getText().toString();
        int type = typeDoctor.isChecked() ? Config.TYPE_DOCTOR: Config.TYPE_PATIENT;
        presenter.login(username, password,type);
    }

    RadioButton typeDoctor;
    RadioButton typePatient;

    @OnClick(R.id.login_to_register)
    void toRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent,1);
    }

    private ILoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        bindView();
        typeDoctor = (RadioButton) findViewById(R.id.login_type_doctor);
        typePatient = (RadioButton) findViewById(R.id.login_type_patient);
        usernameEt = (EditText) findViewById(R.id.login_username);
        passwordET = (EditText) findViewById(R.id.login_password);
        findViewById(R.id.login_to_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRegister();
            }
        });
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        presenter = new LoginPresenter(this);
    }

    @Override
    public Activity activity() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode==1){ //从注册页面返回结果
            String username  = data.getStringExtra("username");
            String password = data.getStringExtra("password");
            int type = data.getIntExtra("type",-1);
            presenter.login(username,password,type);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void toHome() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
