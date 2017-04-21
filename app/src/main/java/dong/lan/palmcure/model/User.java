package dong.lan.palmcure.model;

import android.text.TextUtils;

/**
 */

public class User {

    public String id;
    public String username;
    public String password;
    public String nickname;
    public String phone;
    public int sex;
    public int type;
    public int verify;
    public String updatedAt;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phone='" + phone + '\'' +
                ", sex=" + sex +
                ", type=" + type +
                ", verify=" + verify +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }

    public String getDisplayName() {
        return TextUtils.isEmpty(nickname) ? username : nickname;
    }

    public String getTelDisplay() {
        return TextUtils.isEmpty(phone) ? "请设置联系电话" : phone;
    }

    public String getSexDisplay() {
        if(sex == 0)
            return "男";
        if(sex == 1)
            return "女";
        return "未知";
    }
}
