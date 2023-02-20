package com.major.mahdara;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    public static String[][] simpleQuran;
    public static String[] quran;
    public static String[] parties;
    public static String[] versets;
    public static String[] titres;
    public static String[][] liens;

    public static int chapitre0 = -1;
    public static int récitateur0 = -1;
    public static int verset = 0;
    public static int chapitre = 1;
    public static int récitateur = 1;
    public static AlertDialog.Builder helpBox;
    public static String nombreIndien;
    public static String nombreArabe;
    public static String nombreArabe(String string)
    {
        for (int n=100; n<300; n++)
        {
            if (string.contains("" + n))
                string = string.replace(String.valueOf(n), nombreArabe.charAt(Integer.valueOf(String.valueOf((""+n).charAt(2))))+""
                        +nombreArabe.charAt(Integer.valueOf(String.valueOf((""+n).charAt(1))))+""
                        +nombreArabe.charAt(Integer.valueOf(String.valueOf((""+n).charAt(0)))));
            else break;
        }
        for (int n=10; n<100; n++)
        {
            if (string.contains("" + n))
                string = string.replace(String.valueOf(n), nombreArabe.charAt(Integer.valueOf(String.valueOf((""+n).charAt(1))))+""
                        +nombreArabe.charAt(Integer.valueOf(String.valueOf((""+n).charAt(0)))));
            else break;
        }
        for (int i=0; i<10; i++)
            string = string.replace(i+"", nombreArabe.charAt(i)+"");
        return string;
    }

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
                R.id.navigation_home, R.id.navigation_list, R.id.navigation_person, R.id.navigation_search)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        nombreArabe = getString(R.string.arkam);
        nombreIndien = "0123456789";
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

        String string = "";
        InputStream stream;
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
            stream = getAssets().open("quran-affichage.txt");
            string = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
            quran = string.split("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        versets = new String[titres.length];
        parties = new String[titres.length];
        for (int i=0; i<titres.length; i++) {
            String x[] = titres[i].split(";");
            parties[i] = x[2];
            versets[i] = x[1];
            titres[i] = x[0];
        }

    }

    public static boolean action_checked = false;
    Switch action_switch;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.centre_menu, menu);
        action_switch = (Switch) menu.findItem(R.id.action_switch).getActionView();
        action_switch.setChecked(action_checked);
        action_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                action_checked = isChecked;
                if (isChecked)
                    Toast.makeText(getApplicationContext(), R.string.yes_lawh, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), R.string.no_lawh, Toast.LENGTH_SHORT).show();
            }
        });
        switch (repeatNumber%4) {
            case 0:
                menu.findItem(R.id.action_repeat).setIcon(R.drawable.ic_repeat_grey);
                return true;
            case 1:
                menu.findItem(R.id.action_repeat).setIcon(R.drawable.ic_repeat_black);
                return true;
            case 2:
                menu.findItem(R.id.action_repeat).setIcon(R.drawable.ic_repeat_one_black);
                return true;
            case 3:
                menu.findItem(R.id.action_repeat).setIcon(R.drawable.ic_shuffle_black);
                return true;
        }
        return true;
    }

    public static int repeatNumber = 0;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_repeat:
                repeatNumber++;
                switch (repeatNumber%4) {
                    case 0:
                        item.setIcon(R.drawable.ic_repeat_grey);
                        return true;
                    case 1:
                        item.setIcon(R.drawable.ic_repeat_black);
                        return true;
                    case 2:
                        item.setIcon(R.drawable.ic_repeat_one_black);
                        return true;
                    case 3:
                        item.setIcon(R.drawable.ic_shuffle_black);
                        return true;
                }
                return super.onOptionsItemSelected(item);
            case R.id.action_help:
                helpBox.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void choisirRécitateur(MenuItem item) {
        item.setChecked(true);
        récitateur = item.getOrder();
        if (récitateur!=récitateur0) mediaPlayer.pause();
    }

}
