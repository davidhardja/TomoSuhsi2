package com.example.david.tomosushi.Common;

import com.example.david.tomosushi.Database.Data.Menus;
import com.example.david.tomosushi.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 27/10/2017.
 */

public class Constant {

    // ============== App Name & Version =========================
    public static String APP_NAME = "TomoSushi";
    public static String APP_VERSION = "0.2.1";

    // ============== APIs =========================
    public static final String API_URL = "https://projek-it.com/tomo/";
    //public static final String API_URL = "http://192.168.137.1/tomo/";

    // ============== Response Code =========================
    public static final String API_SUCCESS = "200";
    public static final String UNAUTHORIZED = "401";

    // ============== Database =====================
    public static String DATABASE_NAME = "Tomo_sushi_database";
    public static int DATABASE_VERSION = 2 ;
    public static String DAO_PACKAGE_NAME = "com.example.david.sushi.database.Data";

    //Time Idle
    public static long START_TIME = 100000*1000;
    public static final long INTERVAL = 1000;
    public static boolean SHOW_SCREENSAVER = true;

    //Id Table
    public static String ID_MEJA = "x";

    //Font
    public static String DEFAULT_FONT = "fonts/varela_round_regular.ttf";

    //Cart
    public static List<Menus> cart = new ArrayList<>();
    public static List<Menus> bill = new ArrayList<>();

    //Status Meja
    public static String AVAILABLE = "1";
    public static String SEATED = "2";
    public static String ORDERED = "3";
    public static String PAYMENT = "4";

    //Main Activity
    public static MainActivity mainActivity;

    //Drawer Item
    public static int PRIMARY = 2131558419;
    public static int SECONDARY = 2131558424;

}
