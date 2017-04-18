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

import dong.lan.palmcure.api.Client;
import dong.lan.palmcure.api.RegisterApi;
import dong.lan.palmcure.feature.presenter.IRegisterPresenter;
import dong.lan.palmcure.feature.view.IRegisterView;
import dong.lan.palmcure.model.BaseData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 */

public class RegisterPresenter implements IRegisterPresenter {

    private IRegisterView view;

    public RegisterPresenter(IRegisterView view) {
        this.view = view;
    }

    @Override
    public void register(final String username, final String password,int type) {
        if (TextUtils.isEmpty(username)) {
            view.toast("用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(username)) {
            view.toast("密码长度为6到16为数字字母组合");
            return;
        }
        RegisterApi loginApi = Client.get().retrofit().create(RegisterApi.class);
        Call<BaseData> callback = loginApi.register(username, password, type);
        callback.enqueue(new Callback<BaseData>() {
            @Override
            public void onResponse(Call<BaseData> call, Response<BaseData> response) {
                BaseData data = response.body();
                if (data.code == 0) {
                    view.autoLogin();
                } else {
                    view.dialog("注册失败：" + data.data);
                }
            }

            @Override
            public void onFailure(Call<BaseData> call, Throwable t) {
                view.dialog("注册失败：" +t.getMessage());
            }
        });

    }
}
