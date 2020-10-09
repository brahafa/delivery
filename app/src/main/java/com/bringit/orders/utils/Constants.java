package com.bringit.orders.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.bringit.orders.activities.MainActivity;
import com.bringit.orders.fragments.LogInFragment;
import com.bringit.orders.models.UserDetails;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.bringit.orders.utils.SharedPrefs.saveData;

/**
 * Created by User on 22/03/2018.
 */

public class Constants {

    public static String F_NAME_PREF = "f_name";
    public static String L_NAME_PREF = "l_name";
    public static String PHONE_PREF = "phone";
    public static String EMAIL_PREF = "email";
    public static String PASS_PREF = "pass";
    public static String TOKEN_PREF = "token";
    public static String ID_PREF = "id";
    // public static String TOKEN_PREF= "token";
    public static String IS_LOGGED_PREF = "isLogged";
    public static String T_Z = "tz";

    public static String TIME_PREF = "timePref";

    public static String STREET = "street";
    public static String CITY = "city";
    public static String HOME = "home";
    public static String ENTER = "enter";


    public static String FINISHED = "finished";
    public static String ACTIVE = "active";

    //    pizza topping locations
    public static final String PIZZA_TYPE_FULL = "full";
    public static final String PIZZA_TYPE_RH = "rightHalfPizza";
    public static final String PIZZA_TYPE_LH = "leftHalfPizza";
    public static final String PIZZA_TYPE_TL = "tl";
    public static final String PIZZA_TYPE_TR = "tr";
    public static final String PIZZA_TYPE_BL = "bl";
    public static final String PIZZA_TYPE_BR = "br";

    //    pizza shape
    public static final String PIZZA_TYPE_CIRCLE = "circle";
    public static final String PIZZA_TYPE_RECTANGLE = "rectangle";
    public static final String PIZZA_TYPE_ONE_SLICE = "slice";

    //    item types
    public static final String ITEM_TYPE_PIZZA = "pizza";
    public static final String ITEM_TYPE_TOPPING = "topping";
    public static final String ITEM_TYPE_DRINK = "drink";
    public static final String ITEM_TYPE_ADDITIONAL_OFFER = "additionalOffer";
    public static final String ITEM_TYPE_SPECIAL = "special";
    public static final String ITEM_TYPE_DEAL = "deal";

    public static void openAlertDialog(Context context, String title, String msg) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public static void saveLoggedIn(String token, String id) {
        UserDetails.getInstance().setToken(token);
        saveData(Constants.TOKEN_PREF, token);
        saveData(Constants.ID_PREF, id);
        saveData(Constants.IS_LOGGED_PREF, true);
    }

    public static void logOut(Context context) {
        saveData(Constants.TOKEN_PREF, "");
        saveData(Constants.IS_LOGGED_PREF, false);
        saveData(Constants.F_NAME_PREF, "");
        saveData(Constants.L_NAME_PREF, "");
        saveData(Constants.PHONE_PREF, "");
        saveData(Constants.EMAIL_PREF, "");
        saveData(Constants.PASS_PREF, "");

        saveData(Constants.STREET, "");
        saveData(Constants.CITY, "");
        saveData(Constants.HOME, "");
        saveData(Constants.ENTER, "");
        saveData(Constants.T_Z, "");
        ((MainActivity) context).openNewFragment(new LogInFragment(), "");
    }

    public static void openOrder(View orderContent) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, -orderContent.getHeight(), 0);

        animate.setDuration(500);
        animate.setFillAfter(true);
        orderContent.startAnimation(animate);
        orderContent.setVisibility(View.VISIBLE);
    }

    public static void closeOrder(View orderContent) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, -orderContent.getHeight());

        animate.setDuration(500);
        animate.setFillAfter(true);
        orderContent.startAnimation(animate);
        orderContent.setVisibility(View.GONE);
    }

    public static Bitmap calculateSampleSize(Context context, Bitmap originalImage) {
        Bitmap background = Bitmap.createBitmap((int) originalImage.getWidth(), (int) originalImage.getHeight(), Bitmap.Config.ARGB_8888);

        float originalWidth = originalImage.getWidth();
        float originalHeight = originalImage.getHeight();

        Canvas canvas = new Canvas(originalImage);

        float scale = getScreenWidth(context) / originalWidth;

        float xTranslation = 0.0f;
        float yTranslation = 0.0f;// (height - originalHeight * scale) / 2.0f;

        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);

        Paint paint = new Paint();
        paint.setFilterBitmap(true);

        canvas.drawBitmap(originalImage, transformation, paint);

        return background;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = (maxSize - 50) / 2;
            height = (int) (height / bitmapRatio);
        } else {
            height = (maxSize - 50) / 2;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();

        ((MainActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //     int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return width;
    }

    public static void openMenu(View menuContent) {
        TranslateAnimation animate = new TranslateAnimation(menuContent.getWidth(), 0, 0, 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        menuContent.startAnimation(animate);
        menuContent.setVisibility(View.VISIBLE);
    }

    public static void closeMenu(View menuContent) {
        if (isMenuOpen(menuContent)) {
            TranslateAnimation animate = new TranslateAnimation(0, menuContent.getWidth(), 0, 0);
            animate.setDuration(500);
            menuContent.startAnimation(animate);
            menuContent.setVisibility(View.GONE);
        }
        menuContent.setVisibility(View.GONE);
    }

    private static boolean isMenuOpen(View menuContent) {
        return menuContent.getVisibility() == View.VISIBLE;
    }

    public static void initMenu(ImageView toggle, final View menuContent) {

        menuContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu(menuContent);
            }
        });

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenuOpen(menuContent)) {
                    closeMenu(menuContent);
                } else {
                    openMenu(menuContent);
                }
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    public static String dateFormatToDisplay(String strDate) {

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
        Date dateNew = new Date();
        String formatedDate = "";
        try {
            dateNew = format1.parse(strDate);
            formatedDate = format2.format(dateNew);
            System.out.println(formatedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatedDate;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date stringToDate(String strDate) {

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
        Date dateNew = new Date();
        String formatedDate = "";
        try {
            dateNew = format1.parse(strDate);
            formatedDate = format2.format(dateNew);
            System.out.println(formatedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date(formatedDate);
    }

    public static String dateToString(Date date) {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        return df.format(date);
    }


    public static void openAlertDialog(Activity activity, final DateCallback dateCallback) {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateCallback.onDateChoose(dateToString((newDate.getTime())));
                //  date.setText(dateToString((newDate.getTime())));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }


    public interface DateCallback {
        void onDateChoose(String dateStr);
    }
}
