
package dong.lan.palmcure.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;

import com.blankj.ALog;

import java.util.List;

import dong.lan.base.ui.base.Config;
import dong.lan.base.ui.base.SPHelper;
import dong.lan.palmcure.R;
import dong.lan.palmcure.activity.doctor.DoctorHomeActivity;
import dong.lan.palmcure.activity.patient.PatientHomeActivity;
import pub.devrel.easypermissions.EasyPermissions;


public class SplashActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            run();
        } else {
            EasyPermissions.requestPermissions(this, "需要读取内存卡信息的权限", 1, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private void run() {
        ALog.d("run");
        if (TextUtils.isEmpty(SPHelper.instance().getString("user"))) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            setContentView(R.layout.activity_splash);
            new Handler().sendEmptyMessageDelayed(0, 2500);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //成功
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Some permissions have been granted
        // ...
        run();
    }

    //失败
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Some permissions have been denied
        // ...
    }

    private class Handler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int type = SPHelper.instance().getInt("type");
            if (type == Config.TYPE_DOCTOR) {
                startActivity(new Intent(SplashActivity.this, DoctorHomeActivity.class));
            } else
                startActivity(new Intent(SplashActivity.this, PatientHomeActivity.class));
            finish();
        }
    }
}
