package com.retofit.app.localData;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.retofit.app.data_model_class.Articles;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreferencesUtil {
    private static final String PREF_NAME = "MyAppPreferences";
    private static final String LIST_KEY = "bookmarkList";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public SharedPreferencesUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveList(ArrayList<Articles> list) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(list);
        editor.putString(LIST_KEY, json);
        editor.apply();
    }

    public ArrayList<Articles> getList() {
        String json = sharedPreferences.getString(LIST_KEY, null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<Articles>>() {}.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }
}
