package com.bringit.orders.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import static com.bringit.orders.utils.SharedPrefs.saveData;

public class Utils {

    public static void openAlertDialog(Context context, String msg, String title) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void initPref(JSONObject user) {
        try {
            saveData(Constants.F_NAME_PREF, user.getString("name"));
//            saveData(Constants.L_NAME_PREF, lName.getText().toString());
            saveData(Constants.PHONE_PREF, user.getString("phone"));
            saveData(Constants.EMAIL_PREF, user.getString("email"));
//            saveData(Constants.PASS_PREF, password.getText().toString());

            saveData(Constants.STREET, user.getJSONObject("address").getString("street"));
            saveData(Constants.CITY, user.getJSONObject("address").getString("city"));
            saveData(Constants.HOME, user.getJSONObject("address").getString("houseNumber"));
            saveData(Constants.ENTER, user.getJSONObject("address").getString("apartment"));
            saveData(Constants.T_Z, user.getString("id"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
