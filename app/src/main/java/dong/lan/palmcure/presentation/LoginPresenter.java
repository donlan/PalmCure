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

package dong.lan.palmcure.presentation;

import android.text.TextUtils;

import dong.lan.base.ui.base.SPHelper;
import dong.lan.palmcure.api.Client;
import dong.lan.palmcure.api.LoginApi;
import dong.lan.palmcure.feature.presenter.ILoginPresenter;
import dong.lan.palmcure.feature.view.ILoginView;
import dong.lan.palmcure.model.BaseData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 梁桂栋 on 2017/4/12.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */

public class LoginPresenter implements ILoginPresenter {

    private ILoginView view;

    public LoginPresenter(ILoginView view) {
        this.view = view;
    }

    @Override
    public void login(String username, String password, final int type) {
        if (TextUtils.isEmpty(username)) {
            view.toast("用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(username)) {
            view.toast("密码长度为6到16为数字字母组合");
            return;
        }

        LoginApi loginApi = Client.get().retrofit().create(LoginApi.class);
        Call<BaseData> callback = loginApi.login(username, password, type);
        callback.enqueue(new Callback<BaseData>() {
            @Override
            public void onResponse(Call<BaseData> call, Response<BaseData> response) {
                BaseData data = response.body();
                if(response.code()==200) {
                    if (data.code == 0) {
                        SPHelper.instance().putString("user", data.data.toString());
                        SPHelper.instance().putInt("type", type);
                        view.toHome(type);
                    } else {
                        view.dialog("登录失败：" + data.data);
                    }
                }else{
                    view.toast("网络异常："+response.code());
                }
            }

            @Override
            public void onFailure(Call<BaseData> call, Throwable t) {
                view.dialog("登录异常：" +t.getMessage());
            }
        });

    }
}
