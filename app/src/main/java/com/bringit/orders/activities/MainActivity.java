package com.bringit.orders.activities;

import android.os.Bundle;
import android.view.View;

import com.bringit.orders.R;
import com.bringit.orders.databinding.ActivityMainBinding;
import com.bringit.orders.utils.Constants;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import static com.bringit.orders.utils.SharedPrefs.getBooleanData;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNav();

        navController.addOnDestinationChangedListener((controller, destination, arguments) ->
                binding.tvTitle.setText(destination.getLabel()));

        // initMenu();
        Constants.initMenu(binding.ivMenu, binding.lMenu.menuContent);
        binding.lMenu.profileMenu.setOnClickListener(v -> {
            navController.navigate(R.id.profileFragment);
            Constants.closeMenu(binding.lMenu.menuContent);
        });
        binding.lMenu.logoutMenu.setOnClickListener(view -> {
            navController.navigate(R.id.loginFragment, null,
                    new NavOptions.Builder()
                            .setLaunchSingleTop(true)
                            .setPopUpTo(R.id.nav_graph_main, true)
                            .build());
            Constants.logOut(MainActivity.this);
            Constants.closeMenu(binding.lMenu.menuContent);
        });
        binding.lMenu.contactMenu.setOnClickListener(view -> {
            navController.navigate(R.id.contactUsFragment);
            Constants.closeMenu(binding.lMenu.menuContent);
        });

        //        Fabric.with(this, new Crashlytics());
        // TODO: Move this to where you establish a user session
        //logUser();

    }

    private void setupBottomNav() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavGraph graph = navController.getGraph();

        if (getBooleanData(Constants.IS_LOGGED_PREF)) graph.setStartDestination(R.id.timerFragment);
        else graph.setStartDestination(R.id.loginFragment);

        navController.setGraph(graph);

        NavigationUI.setupWithNavController(binding.bottomNavView, navController);
    }

    public void openNewFragment(final Fragment fragment, final String tag) {
        androidx.fragment.app.FragmentManager manager = getSupportFragmentManager();
//        for(int i = 0; i < manager.getBackStackEntryCount(); ++i) {
//            manager.popBackStack();
//        }
        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.replace(R.id.fragment_container, fragment, tag);
        // transaction.addToBackStack(null);
        transaction.commit();

        Constants.closeMenu(binding.lMenu.menuContent);
    }

    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }

    public void setBottomNavigationVisibility(int visibility) {
        binding.bottomNavView.setVisibility(visibility);
        binding.ivMenu.setVisibility(visibility);
    }

    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
//        Crashlytics.setUserIdentifier("12345");
//        Crashlytics.setUserEmail("user@fabric.io");
//        Crashlytics.setUserName("Test User");
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

    public void setTitle(String titleTxt) {
//        title.setText(titleTxt);
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
