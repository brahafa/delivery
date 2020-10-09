package com.bringit.orders.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;

import com.bringit.orders.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.bringit.orders.utils.Constants.PIZZA_TYPE_BL;
import static com.bringit.orders.utils.Constants.PIZZA_TYPE_BR;
import static com.bringit.orders.utils.Constants.PIZZA_TYPE_FULL;
import static com.bringit.orders.utils.Constants.PIZZA_TYPE_LH;
import static com.bringit.orders.utils.Constants.PIZZA_TYPE_RH;
import static com.bringit.orders.utils.Constants.PIZZA_TYPE_TL;
import static com.bringit.orders.utils.Constants.PIZZA_TYPE_TR;
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
    public static int getImageRes(String viewType) {
        int imageRes = R.drawable.ic_pizza_full_active;
        switch (viewType) {
            case PIZZA_TYPE_FULL:
                imageRes = R.drawable.ic_pizza_full_active;
                break;
            case PIZZA_TYPE_RH:
                imageRes = R.drawable.ic_pizza_rh_active;
                break;
            case PIZZA_TYPE_LH:
                imageRes = R.drawable.ic_pizza_lh_active;
                break;
            case PIZZA_TYPE_TR:
                imageRes = R.drawable.ic_pizza_tr_cart;
                break;
            case PIZZA_TYPE_TL:
                imageRes = R.drawable.ic_pizza_tl_cart;
                break;
            case PIZZA_TYPE_BR:
                imageRes = R.drawable.ic_pizza_br_cart;
                break;
            case PIZZA_TYPE_BL:
                imageRes = R.drawable.ic_pizza_bl_cart;
                break;
        }
        return imageRes;
    }

    public static int getImageResRect(String viewType) {
        int imageRes = R.drawable.ic_pizza_full_rect_active;
        switch (viewType) {
            case PIZZA_TYPE_FULL:
                imageRes = R.drawable.ic_pizza_full_rect_active;
                break;
            case PIZZA_TYPE_RH:
                imageRes = R.drawable.ic_pizza_rh_rect_cart;
                break;
            case PIZZA_TYPE_LH:
                imageRes = R.drawable.ic_pizza_lh_rect_cart;
                break;
            case PIZZA_TYPE_TR:
                imageRes = R.drawable.ic_pizza_tr_rect_cart;
                break;
            case PIZZA_TYPE_TL:
                imageRes = R.drawable.ic_pizza_tl_rect_cart;
                break;
            case PIZZA_TYPE_BR:
                imageRes = R.drawable.ic_pizza_br_rect_cart;
                break;
            case PIZZA_TYPE_BL:
                imageRes = R.drawable.ic_pizza_bl_rect_cart;
                break;
        }
        return imageRes;
    }
}
