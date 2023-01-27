package com.major.mahdara;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class CentreActivity extends AppCompatActivity {

    public static MediaPlayer mediaPlayer = new MediaPlayer();
    public static String[] quran;
    public static String[] parties;
    public static String[] durées;
    public static String[] titres;
    public static String[][] liens;

    InputStream stream;
    String string = "";
    public static Handler handler = new Handler();
    AlertDialog.Builder helpBox;
    private String arkam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centre);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        getWindow().getDecorView().setTextDirection(View.TEXT_DIRECTION_RTL);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_list, R.id.navigation_person)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        helpBox = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.malumat))
                .setMessage(R.string.help_text)
                .setPositiveButton(R.string.positive,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int sumthin) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:hassenmajor@gmail.com")));
                            }
                        })
                .setNeutralButton(R.string.neutral,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int sumthin) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.major.mahdara")));
                            }
                        })
                .setNegativeButton(R.string.negative, null);

        //
        arkam = getString(R.string.arkam);

        liens = new String[7][];
        try {
            stream = getAssets().open("liens1.txt");
            string = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
            liens[0] = string.split("\n");
            stream = getAssets().open("liens2.txt");
            string = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
            liens[1] = string.split("\n");
            stream = getAssets().open("liens3.txt");
            string = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
            liens[2] = string.split("\n");
            stream = getAssets().open("liens4.txt");
            string = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
            liens[3] = string.split("\n");
            stream = getAssets().open("liens5.txt");
            string = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
            liens[4] = string.split("\n");
            stream = getAssets().open("liens6.txt");
            string = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
            liens[5] = string.split("\n");
            stream = getAssets().open("liens7.txt");
            string = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
            liens[6] = string.split("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        titres = null;
        quran = null;
        try {
            stream = getAssets().open("titres.txt");
            string = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
            titres = string.split("\n");
            stream = getAssets().open("quran.txt");
            string = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
            quran = string.split("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        durées = new String[titres.length];
        parties = new String[titres.length];
        for (int i=0; i<titres.length; i++) {
            String x[] = titres[i].split(";");
            parties[i] = x[2];
            durées[i] = x[1];
            titres[i] = (i+1) + " - " + x[0];
        }

    }

}
