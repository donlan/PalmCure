package dong.lan.palmcure;

import android.util.Log;

import dong.lan.base.ui.base.SPHelper;
import dong.lan.palmcure.api.Client;
import dong.lan.palmcure.model.User;

/**
 */

public class UserManager {

    private static UserManager manager;

    private UserManager(){}


    public static UserManager get(){
        if(manager == null){
            manager = new UserManager();
        }
        return manager;
    }




    private User user;

    public User currentUser(){
        if(user == null){
           user =  Client.get().gson().fromJson(SPHelper.instance().getString("user"),User.class);
        }
        Log.d("TAG",user.id);
        return user;
    }


    public void logout(){
        SPHelper.instance().putString("user","");
    }
}
