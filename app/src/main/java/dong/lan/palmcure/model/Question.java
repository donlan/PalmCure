package dong.lan.palmcure.model;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import dong.lan.base.utils.DateUtils;
import dong.lan.base.utils.GsonHelper;
import dong.lan.palmcure.UserManager;

/**
 * Created by 梁桂栋 on 2017/5/10.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */

public class Question {
    public String id;
    public String type;
    public String qdescribe;
    public Map<String, Integer> qkey;
    public Map<String, String> options;
    public String answer;
    public String creator;
    public int score;
    public String createdAt;


    public Question() {
    }

    public Question(String desc, String optionA, String optionB, String optionC) {
        qkey = new HashMap<>(3);
        options = new HashMap<>(3);
        qkey.put("A", 5);
        qkey.put("B", 2);
        qkey.put("C", 0);
        options.put("A", optionA);
        options.put("B", optionB);
        options.put("C", optionC);
        qdescribe = desc;
        score = 0;
        type = "1";
        answer = "";
        createdAt = DateUtils.getTime(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss");
        creator = UserManager.get().currentUser().id;
        id = String.valueOf(System.currentTimeMillis());

    }

    public String getOptionString() {
        return "A: " + options.get("A") + " B: " + options.get("B") + " C:" + options.get("C") + (TextUtils.isEmpty(answer) ? "" : "  得分：" + qkey.get(answer));
    }

    public String toJson() {
        return GsonHelper.getInstance().toJson(this);
    }

    public int getScore() {
        if(TextUtils.isEmpty(answer))
            return 0;
        return qkey.get(answer);
    }
}
