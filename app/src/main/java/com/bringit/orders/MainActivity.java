package com.bringit.orders;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bringit.orders.fragments.ContactUsFragment;
import com.bringit.orders.fragments.CurrentFragment;
import com.bringit.orders.fragments.LogInFragment;
import com.bringit.orders.fragments.ProfileFragment;
import com.bringit.orders.fragments.TimerFragment;
import com.bringit.orders.utils.Constants;
import com.bringit.orders.utils.SharePref;
import com.crashlytics.android.Crashlytics;
import com.ss.bottomnavigation.BottomNavigation;
import com.ss.bottomnavigation.events.OnSelectedItemChangeListener;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity {

    LinearLayout menuContent;
    ImageView toggl;
    private RelativeLayout profile_menu, logout_menu, contact_menu;
    TextView title;
    LinearLayout bottom_navigation;
     CurrentFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuContent = (LinearLayout) findViewById(R.id.menu_content);
        toggl = (ImageView) findViewById(R.id.toggl);
        profile_menu = (RelativeLayout) findViewById(R.id.profile_menu);
        logout_menu = (RelativeLayout) findViewById(R.id.logout_menu);
        contact_menu = (RelativeLayout) findViewById(R.id.contact_menu);
        title = (TextView) findViewById(R.id.title);
        bottom_navigation = (LinearLayout) findViewById(R.id.bottom_navigation);




        // initMenu();
        if(SharePref.getInstance(this).getData(Constants.IS_LOGGED_PREF).equals("true")){
           // openNewFragment(new MainFragment());
            openNewFragment(new TimerFragment(), "TimerFragment");
        }else {
            openNewFragment(new LogInFragment(),"LogInFragment");
        }
        Constants.initMenu(toggl,menuContent);
        profile_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              openNewFragment(new ProfileFragment(),"ProfileFragment");
            }
        });

        logout_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.logOut(MainActivity.this);
            }
        });

        contact_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewFragment(new ContactUsFragment(), "ContactUsFragment");
            }
        });
        Fabric.with(this, new Crashlytics());
        // TODO: Move this to where you establish a user session
        //logUser();

        initBottomMenu();

    }

    private void initBottomMenu() {

        LinearLayout tab_timer = findViewById(R.id.tab_timer);
        tab_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewFragment(new TimerFragment(), "TimerFragment");
            }
        });

        LinearLayout tab_orders = findViewById(R.id.tab_orders);
        tab_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentFragment = CurrentFragment.newInstance("active", "");
                openNewFragment(currentFragment, "currentFragment");
            }
        });
        LinearLayout tab_history = findViewById(R.id.tab_history);
        tab_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentFragment = CurrentFragment.newInstance("finished", "");
                openNewFragment(currentFragment, "currentFragment");
            }
        });
    }

    public void openNewFragment(final Fragment fragment,final String tag){
        FragmentManager manager = getFragmentManager();
//        for(int i = 0; i < manager.getBackStackEntryCount(); ++i) {
//            manager.popBackStack();
//        }
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container,fragment, tag);
       // transaction.addToBackStack(null);
        transaction.commit();

        Constants.closeMenu(menuContent);
    }

    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }

    public void popFragment(String pageType){
        FragmentManager manager = getFragmentManager();
//        for(int i = 0; i < manager.getBackStackEntryCount(); ++i) {
            manager.popBackStack();
        CurrentFragment currentFragment = CurrentFragment.newInstance(pageType, "");
        openNewFragment(currentFragment, "currentFragment");
//        }
    }

    public void setBottomNavigationVisibility(int visibility){
        bottom_navigation.setVisibility(visibility);
        toggl.setVisibility(visibility);
    }

    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier("12345");
        Crashlytics.setUserEmail("user@fabric.io");
        Crashlytics.setUserName("Test User");
    }


//    private void initMenu() {
//        bottom_navigation.setOnSelectedItemChangeListener(new  OnSelectedItemChangeListener() {
//            @Override
//            public void onSelectedItemChanged(int itemId) {
//                CurrentFragment currentFragment;
//                switch (itemId){
//                    case R.id.tab_timer:
//                        openNewFragment(new TimerFragment(), "TimerFragment");
//
//                        break;
//                    case R.id.tab_orders:
//                         currentFragment = CurrentFragment.newInstance("active", "");
//                        openNewFragment(currentFragment, "currentFragment");
//                       // MyFragment myFragment = currentFragment.getFragmentManager().findFragmentByTag("MY_FRAGMENT");
//
//                      //  ((LinearLayout)currentFragment.getView().findViewById(R.id.main_layout)).setVisibility(View.GONE);
//                      //  CurrentFragment.newInstance("active","");
//                        break;
//                    case R.id.tab_history:
//                         currentFragment = CurrentFragment.newInstance("finished", "");
//                        openNewFragment(currentFragment, "currentFragment");
//                        bottom_navigation.setSelectedItem(0);
//                      //  ((LinearLayout)currentFragment.getView().findViewById(R.id.main_layout)).setVisibility(View.GONE);
//                        break;
//                    case R.id.tab_statistic:
//
//                        break;
//
//                }
//
//            }
//        });
//    }

//    public void fragText(String string){
//        String tagName = "tpTag";
//        Fragment fr =  CurrentFragment.newInstance(string, "");
//        FragmentManager manager = getFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.replace(R.id.fragment_container,fr);
//       // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fr).commit();
//       // CurrentFragment tp = (CurrentFragment) getFragmentManager().findFragmentByTag(tagName);
//
//        transaction.addToBackStack(null);
//        transaction.commit();
//        Constants.closeMenu(menuContent);
//
//    }

    public void setTitle(String titleTxt){
        title.setText(titleTxt);
    }


//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 1) {
//            Bitmap image = (Bitmap) data.getExtras().get("data");
//         //   ImageView imageview = (ImageView) findViewById(R.id.ImageView01); //sets imageview as the bitmap
//            Log.d("CAMERA", "SUCCSES");
//            //imageview.setImageBitmap(image);
//        }
//    }
}
