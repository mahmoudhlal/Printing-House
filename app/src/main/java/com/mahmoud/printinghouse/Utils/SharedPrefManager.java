package com.mahmoud.printinghouse.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mahmoud.printinghouse.models.authResponse.Data;


public class SharedPrefManager {
    final static String SHARED_PREF_NAME = "efforts";
    final static String LOGIN_STATUS = "login_status";
    final static String IS_USER = "is_user";
    final static String FIRST_TIME = "shared_first_time";
    final static String TOKEN = "token";

    private static Context mContext;
    private static SharedPrefManager mInstance ;



    public static synchronized SharedPrefManager getInstance(Context context) {
        mContext = context ;
        if (mInstance == null) {
            mInstance = new SharedPrefManager();
        }
        return mInstance;
    }




    public Boolean getLoginStatus() {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                SHARED_PREF_NAME, 0);
        Boolean value = sharedPreferences.getBoolean(LOGIN_STATUS, false);
        return value;
    }

    public void setLoginStatus(Boolean status) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME,
                0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOGIN_STATUS, status);
        editor.apply();
    }

    public void setIsUser(Boolean isUser) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME,
                0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_USER, isUser);
        editor.apply();
    }

    public Boolean getIsUser() {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                SHARED_PREF_NAME, 0);
        return sharedPreferences.getBoolean(IS_USER, false);
    }

    public Boolean isFirstTime() {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                SHARED_PREF_NAME, 0);
        return sharedPreferences.getBoolean(FIRST_TIME, true);
    }

    public void setFirstTime(Boolean status) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME,
                0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FIRST_TIME, status);
        editor.apply();
    }



    /**
     * this method is responsible for user logout and clearing cache
     */
    public void Logout() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        setFirstTime(false);
        setLoginStatus(false);
    }


    public Data getUserData(){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREF_NAME, mContext.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("data", "");
        return gson.fromJson(json, Data.class) ;
    }

    public void setUserData(Data data){
        setToken(data.getToken());
        SharedPreferences.Editor editor = mContext.getSharedPreferences(SHARED_PREF_NAME, mContext.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString("data", json);
        editor.apply();
    }
    private void setToken(String token) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME,
                0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN, "Bearer "+token);
        editor.apply();
    }

    public String getToken() {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                SHARED_PREF_NAME, 0);
        return sharedPreferences.getString(TOKEN, "");
    }


}
