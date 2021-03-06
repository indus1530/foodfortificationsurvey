package edu.aku.hassannaqvi.foodfortificationsurvey.core;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.view.View;

import org.json.JSONArray;

import java.io.File;
import java.util.List;

import edu.aku.hassannaqvi.foodfortificationsurvey.BuildConfig;
import edu.aku.hassannaqvi.foodfortificationsurvey.models.FamilyMembers;
import edu.aku.hassannaqvi.foodfortificationsurvey.models.FoodConsumption;
import edu.aku.hassannaqvi.foodfortificationsurvey.models.Form;
import edu.aku.hassannaqvi.foodfortificationsurvey.models.RandomHH;
import edu.aku.hassannaqvi.foodfortificationsurvey.models.Users;


public class MainApp extends Application {

    public static final String PROJECT_NAME = "foodfortificationsurvey";
    public static final String DIST_ID = null;
    public static final String SYNC_LOGIN = "sync_login";
    public static final String _IP = "https://vcoe1.aku.edu";// .LIVE server
    // public static final String _IP = "http://cls-pae-fp51764";// .TEST server
    // public static final String _IP = "http://f49461:8080/prosystem";// .TEST server
    //public static final String _IP = "http://43.245.131.159:8080";// .TEST server
    public static final String _HOST_URL = MainApp._IP + "/biofort/api/";// .TEST server;
    public static final String _SERVER_URL = "sync.php";
    public static final String _SERVER_GET_URL = "getData.php";
    public static final String _PHOTO_UPLOAD_URL = _HOST_URL + "uploads.php";
    public static final String _UPDATE_URL = MainApp._IP + "/biofort/app/survey";

    //COUNTRIES
    public static int PAKISTAN = 1;
    public static int URDU = 3;

    public static File sdDir;
    public static String[] downloadData;
    public static Form form;
    public static FamilyMembers familyMember;

    //public static Samples samples;

    public static String DeviceURL = "devices.php";
    public static AppInfo appInfo;
    public static Users user;
    public static Boolean admin = false;
    public static Boolean superuser = false;
    public static List<JSONArray> uploadData;
    public static SharedPreferences.Editor editor;
    public static SharedPreferences sharedPref;
    public static String deviceid;
    public static int versionCode = BuildConfig.VERSION_CODE;
    public static String versionName = BuildConfig.VERSION_NAME;
    public static int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 2;
    public static long TWO_MINUTES = 1000 * 60 * 2;
    public static boolean permissionCheck = false;
    public static int idType = 0;

    public static List<String> subjectNames;
    public static List<FamilyMembers> familyList;
    public static List<Integer> mwraList;
    public static List<Integer> childOfSelectedMWRAList;
    public static List<FamilyMembers> fatherList;
    public static List<FamilyMembers> motherList;
    //public static FamilyMembers familyMember;
    public static int memberCount = 0;
    public static String selectedMWRA;
    public static String selectedChild;
    public static int memberCountComplete;
    public static boolean memberComplete;
    //public static int mwraCount;
    public static FamilyMembers mwra;
    public static RandomHH currentHousehold;
    public static List<FoodConsumption> foodConsumption;
    public static int foodIndex;
    public static boolean hhheadSelected = false;


    public static void hideSystemUI(View decorView) {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public static String getDeviceId(Context context) {
        String deviceId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
           /* final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }*/
        }
        return "deviceId";
    }

    public static String kishGrid(int Hh_Sno, int totalMwra) {

        // GENERATE RANDOM
        // int household  = 1 + (int)(Math.random() * ((10 - 1) + 1));
        // int eligibles = 1 + (int)(Math.random() * ((8 - 1) + 1));

        int household = Math.min(Hh_Sno, 10);
        int eligibles = Math.min(totalMwra, 8);

        int[][] grid = {
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 2, 2, 2, 2, 2, 2, 2},
                {1, 1, 3, 3, 3, 3, 3, 3},
                {1, 2, 1, 4, 4, 4, 4, 4},
                {1, 1, 2, 1, 5, 5, 5, 5},
                {1, 2, 3, 2, 1, 6, 6, 6},
                {1, 1, 1, 3, 2, 1, 7, 7},
                {1, 2, 2, 4, 3, 2, 1, 8},
                {1, 1, 3, 1, 4, 3, 2, 1},
                {1, 2, 1, 2, 5, 4, 3, 2},
        };

        // System.out.println(household+"-"+eligibles);
        // System.out.println(grid[household-1][eligibles-1]);

        // return Index of MWRAList (total-1)
        return String.valueOf((grid[household - 1][eligibles - 1]) - 1);
    }

    public static Boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network nw = connectivityManager.getActiveNetwork();
            if (nw == null) return false;
            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
            return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
        } else {
            NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
            return nwInfo != null && nwInfo.isConnected();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Initiate DateTime
        //Initializ App info
        appInfo = new AppInfo(this);
        sharedPref = getSharedPreferences(PROJECT_NAME, MODE_PRIVATE);
        editor = sharedPref.edit();
        deviceid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

    }

}
