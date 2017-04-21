/*
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

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;

import dong.lan.base.ui.base.Config;
import dong.lan.base.ui.base.SPHelper;
import dong.lan.palmcure.R;
import dong.lan.palmcure.activity.doctor.DoctorHomeActivity;
import dong.lan.palmcure.activity.patient.PatientHomeActivity;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SPHelper.instance().init(getApplicationContext(),"cure");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (TextUtils.isEmpty(SPHelper.instance().getString("user"))) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            setContentView(R.layout.activity_splash);
            new Handler().sendEmptyMessageDelayed(0, 1000);
        }
    }

    private class Handler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int type = SPHelper.instance().getInt("type");
            if(type == Config.TYPE_DOCTOR) {
                startActivity(new Intent(SplashActivity.this, DoctorHomeActivity.class));
            }else
                startActivity(new Intent(SplashActivity.this, PatientHomeActivity.class));
            finish();
        }
    }
}
