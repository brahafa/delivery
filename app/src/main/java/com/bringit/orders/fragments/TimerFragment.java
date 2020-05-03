package com.bringit.orders.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bringit.orders.MainActivity;
import com.bringit.orders.R;
import com.bringit.orders.utils.Constants;
import com.bringit.orders.utils.GPSTracker;
import com.bringit.orders.utils.SharePref;
import com.bringit.orders.utils.UtilityLocation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;


public class TimerFragment extends Fragment {

    View view;
    TextView dateTime, date;
    TextView button;
    Timer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_timer, container, false);
        timer=new Timer();
        dateTime = (TextView)view.findViewById(R.id.text3);
        date = (TextView)view.findViewById(R.id.date);
        button = (TextView)view.findViewById(R.id.button);
        Log.d("ORDER ID  ", SharePref.getInstance(getActivity()).getData(Constants.ID_PREF));
        //sendLocation();
        ((MainActivity)getActivity()).setBottomNavigationVisibility(1);

        UtilityLocation.checkPermission(getActivity());

        if(SharePref.getInstance(getActivity()).getLongData(Constants.TIME_PREF)!=-1){
            openTimer();
        }else{
            closeTimer();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SharePref.getInstance(getActivity()).getLongData(Constants.TIME_PREF)!=-1){
                    closeTimer();
                }else{
                    SharePref.getInstance(getActivity()).saveData(Constants.TIME_PREF, System.currentTimeMillis());
                    openTimer();
                }
            }
        });
        ((MainActivity)getActivity()).setTitle("פתיחת יום");
        return  view;

    }

    private void sendLocation() {

        final GPSTracker gps = new GPSTracker(getActivity());
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){

                DatabaseReference myRef;
                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    myRef = database.getReference(SharePref.getInstance(getActivity()).getData(Constants.ID_PREF)).child("latitude");
                  //  test1=test1*2;
                    myRef.setValue(latitude);
                  //  myRef.setValue(test1);
                    Log.d("lat", myRef.toString());
                    myRef = database.getReference(SharePref.getInstance(getActivity()).getData(Constants.ID_PREF)).child("longitude");

                  //  myRef = database.getReference("id").child("longitude");
                  //  test1=test1*3;
                    Log.d("long", myRef.toString());
                    myRef.setValue(longitude);
                   // myRef.setValue(test1);



//                    // Write a message to the database
//                    myRef = database.getReference("latitude");
//                    myRef.setValue(latitude);
//
//
//                    myRef = database.getReference("longitude");
//                    myRef.setValue(longitude);

                }

                sendLocation();
            }
        }, 9000);   //5 seconds

    }
    private void openTimer() {
        Log.i(TAG, "Started service");
        button.setText("סיים שליחויות");
        initDate();
        timer=new Timer();
        timer.schedule(new firstTask(), 0,500);
        h2.postDelayed(run, 0);
    }

    private void closeTimer() {
        SharePref.getInstance(getActivity()).saveData(Constants.TIME_PREF, -1);
        Log.d(TAG,  "stopService");
        button.setText("אני מוכן לשליחות");
        timer.cancel();
        timer.purge();
        h2.removeCallbacks(run);
        dateTime.setText("00:00:00");
        date.setText("00/00/00");
    }

    Handler h2 = new Handler();
    Runnable run = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - SharePref.getInstance(getActivity()).getLongData(Constants.TIME_PREF);
            int seconds = (int) (millis / 1000) % 60 ;
            int minutes = (int) ((millis / (1000*60)) % 60);
            int hours   = (int) ((millis / (1000*60*60)) % 24);

            dateTime.setText(String.format("%02d:%02d:%02d", (hours), minutes, seconds));

            h2.postDelayed(this, 500);
        }
    };
    //tells handler to send a message
    class firstTask extends TimerTask {

        @Override
        public void run() {
            h2.sendEmptyMessage(0);
        }
    }

    private void initDate() {
        long millis = SharePref.getInstance(getActivity()).getLongData(Constants.TIME_PREF);
        java.util.Date dateObj = new java.util.Date(millis);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatMMDDYYYY = new SimpleDateFormat("dd/MM/yy");
        StringBuilder nowddMMYY = new StringBuilder( dateformatMMDDYYYY.format( dateObj ) );
        date.setText(nowddMMYY);
        SharePref.getInstance(getActivity()).saveData(Constants.TIME_PREF, millis);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
