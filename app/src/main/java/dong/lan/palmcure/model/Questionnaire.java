package dong.lan.palmcure.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 */

public class Questionnaire implements Serializable{

    public static final int STATUS_NEW = 0;
    public static final int STATUS_TESING = 1;
    public static final int STATUS_DONE = 2;

    public String id;
    public String doctor;
    public String patient;
    public String intro;
    public int status;
    public int level;
    public String result;
    public String createdAt;
    public String updatedAt;

    public String getResultString() {
        if(TextUtils.isEmpty(result))
            return "";
        return "医生建议："+result;
    }

    public String getStatus() {
        if (status == 0)
            return "新建";
        else if(status ==1){
            return "测试中";
        }else if(status == 2){
            return "完毕";
        }
        return "异常";
    }

}
