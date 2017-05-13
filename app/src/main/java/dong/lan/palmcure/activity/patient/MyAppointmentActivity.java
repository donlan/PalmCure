package dong.lan.palmcure.activity.patient;

import android.os.Bundle;
import android.support.annotation.Nullable;

import dong.lan.base.ui.BaseActivity;
import dong.lan.palmcure.R;
import dong.lan.palmcure.UserManager;
import dong.lan.palmcure.api.AppointmentsApi;

/**
 */

public class MyAppointmentActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment);
        AppointmentFragment fragment = AppointmentFragment.newInstance(AppointmentsApi.PATIENT, UserManager.get().currentUser().id, "");
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).show(fragment).commit();
    }

}
