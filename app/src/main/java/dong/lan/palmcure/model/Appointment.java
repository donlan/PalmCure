package dong.lan.palmcure.model;

import dong.lan.base.ui.base.Config;

/**
 */

public class Appointment {
    public String id;
    public String doctor;
    public String patient;
    public String reason;
    public int status;
    public String booktime;
    public String createdAt;
    public String updatedAt;
    public User user;

    @Override
    public String toString() {
        return "Appointment{" +
                "id='" + id + '\'' +
                ", doctor='" + doctor + '\'' +
                ", patient='" + patient + '\'' +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                ", booktime='" + booktime + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", user=" + user +
                '}';
    }

    public String getStatusDisplay() {
        if (status == Config.APPOINTMENT_ADD) {
            return "等待确认";
        }
        if (status == Config.APPOINTMENT_REJECT) {
            return "拒绝";
        }
        if (status == Config.APPOINTMENT_FINISH) {
            return "结束";
        }
        if (status == Config.APPOINTMENT_WAIT) {
            return "等待";
        }
        return "未知";

    }
}
