package dong.lan.palmcure.model;

import dong.lan.base.ui.base.Config;

/**
 * Created by 梁桂栋 on 2017/4/19.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */

public class Contract {

    public String id;
    public String doctor;
    public String patient;
    public int status;
    public String createdAt;
    public String updatedAt;
    public User user;

    @Override
    public String toString() {
        return "Contract{" +
                "id='" + id + '\'' +
                ", doctor='" + doctor + '\'' +
                ", patient='" + patient + '\'' +
                ", status=" + status +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", user=" + user +
                '}';
    }

    public String getStatusDisplay() {
        if(status == Config.CONTRACT_STATUS_ADD){
            return "待签约";
        }
        if(status == Config.CONTRACT_STATUS_VERIFY){
            return "已签约";
        }
        if(status == Config.CONTRACT_STATUS_REJECT){
            return "拒绝";
        }
        return "未知";
    }
}
