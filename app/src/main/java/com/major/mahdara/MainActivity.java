package com.major.mahdara;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.material.snackbar.Snackbar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    TextView textView, textStart, textEnd, textVerset, textPartie;
    Spinner spinner;
    ImageButton buttonStart, buttonPause;
    ScrollView scrollView;
    AdView adView;
    SeekBar seekBar;
    Timer timer;
    public static MediaPlayer mediaPlayer = new MediaPlayer();
    static String[] quran;
    static String[] parties;
    static String[] durées;
    static String[] titres;
    static String[][] liens;

    private int tilawa = -1;
    private int sourate() {
        return spinner.getSelectedItemPosition() + 1;
    }
    private int tajwid = -1;
    private int moujawid() {
        return moujawid;
    }
    InputStream stream;
    String string = "";
    public static Handler handler = new Handler();
    AlertDialog.Builder helpBox;
    private String arkam;
/*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tilawa", tilawa);
        outState.putInt("sourate", sourate());
        outState.putInt("tajwid", tajwid);
        outState.putInt("moujawid", moujawid());
        outState.putInt("seek", mediaPlayer.getCurrentPosition());
    }*/

    @Override
    protected void onDestroy() {
        pauseSound(null);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        getWindow().getDecorView().setTextDirection(View.TEXT_DIRECTION_RTL);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // Mahdara Banner : ca-app-pub-7799975842206135/9634454119
        // Test Banner : ca-app-pub-3940256099942544/6300978111

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                //Toast.makeText(MainActivity.this, "Ad failed: "+loadAdError.toString(), Toast.LENGTH_LONG).show();
                super.onAdFailedToLoad(loadAdError);
            }
        });

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

        scrollView = (ScrollView)findViewById(R.id.scrollView);
        textView = (TextView)findViewById(R.id.textView);
        textPartie = (TextView)findViewById(R.id.textPartie);
        textVerset = (TextView)findViewById(R.id.textVerset);
        textStart = (TextView)findViewById(R.id.textStart);
        textEnd = (TextView)findViewById(R.id.textEnd);
        spinner = (Spinner)findViewById(R.id.spinner);
        buttonStart = (ImageButton)findViewById(R.id.buttonStart);
        buttonPause = (ImageButton)findViewById(R.id.buttonPause);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
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
        ArrayAdapter<String> a=new ArrayAdapter<String>(this,
                R.layout.my_spinner, titres) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(18);
                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView v = (TextView) super.getView(position, convertView, parent);
                v.setGravity(Gravity.RIGHT);
                v.setPadding(20, 10, 20, 10);
                //w.setCompoundDrawables(null, null, null, null);
                return v;
            }
        };
        a.setDropDownViewResource( R.layout.my_spinner );
        spinner.setAdapter(a);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Afficher();
                scrollView.setScrollY(0);
                textVerset.setText(getString(R.string.ayat)+"\n"+durées[position]);
                textPartie.setText(getString(R.string.hizb)+"\n"+parties[position]);
                pauseSound(null);
                // Relancer la publicité
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textStart.setText((int) (progress / (1000 * 60))+":"+(int) ((progress / 1000) % 60));
                if ( !fromUser && (progress>=0.999*seekBar.getMax()) )
                {
                    buttonStart.setVisibility(View.VISIBLE);
                    buttonPause.setVisibility(View.INVISIBLE);
                    endOperation();
                }
                if (action_checked) {
                    double a = seekBar.getProgress()/1.0/seekBar.getMax();
                    double h = scrollView.getHeight()-scrollView.getPaddingTop();
                    double H = (textView.getHeight()*a)-h/2;
                    scrollView.setScrollY( (int)H );
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    mediaPlayer.seekTo(seekBar.getProgress());
                } catch (Exception e) { }
            }
        });

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Synchroniser();
            }
        }, 1000, 1000);

    }

    private void Synchroniser() {
        try {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
        } catch (Exception e) { }
    }

    private void Afficher() {
        string = null;
        if (sourate()==1) {
            string = getString(R.string.fatiha);
            textView.setText(string);
            return;
        }
        else if (sourate()==9)
            string = "\n" + quran[sourate()-1];
        else
            string = getString(R.string.basmala) + "\n" + quran[sourate()-1];
        for (int n=2; n<290; n++)
            if (string.contains("("+n+")"))
                string = string.replace("("+n+")", "("+(n-1)+")");
            else break;
        string = string + " " + durées[sourate()-1];
        for (int n=100; n<300; n++)
        {
            if (string.contains("" + n))
                string = string.replace(String.valueOf(n), arkam.charAt(Integer.valueOf(String.valueOf((""+n).charAt(2))))+""
                    +arkam.charAt(Integer.valueOf(String.valueOf((""+n).charAt(1))))+""
                    +arkam.charAt(Integer.valueOf(String.valueOf((""+n).charAt(0)))));
            else break;
        }
        for (int n=10; n<100; n++)
        {
            if (string.contains("" + n))
                string = string.replace(String.valueOf(n), arkam.charAt(Integer.valueOf(String.valueOf((""+n).charAt(1))))+""
                    +arkam.charAt(Integer.valueOf(String.valueOf((""+n).charAt(0)))));
            else break;
        }
        for (int i=0; i<10; i++)
            string = string.replace(i+"", arkam.charAt(i)+"");
        string = string.replace(getString(R.string.cercle_bug), "");
        string = string.replace("(", "").replace(")", "");
        string = string.replace("[", "﴿").replace("]", "﴾");
        textView.setText(string);
    }

    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id");
    private void Notifier() {
        final Intent intent = new Intent(this, FullActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        PendingIntent pendingIntent0 = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(this, MyBroadcastReceiver.class), 0);
        builder = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle(titres[sourate()-1])
                .setContentText(getString(R.string.note_text_prim))
                .setSmallIcon(R.drawable.ic_mahdara)
                .setStyle(new NotificationCompat.BigTextStyle())
                //.addAction(new NotificationCompat.Action(0, "Stop", intent0))
                .setContentIntent(pendingIntent)
                .setDeleteIntent(pendingIntent0)
                .setOngoing(false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id",
                    "New channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            assert manager != null;
            manager.createNotificationChannel(channel);
        }

        assert manager != null;
        manager.notify(0, builder.build());
    }

    boolean action_checked = false;
    Switch action_switch;
    Menu main_menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        main_menu = menu;
        action_switch = (Switch) menu.findItem(R.id.action_switch).getActionView();
        action_switch.setChecked(false);
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

        return true;
    }

    int repeatNumber = 0;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_repeat:
                repeatNumber++;
                switch (repeatNumber%4) {
                    case 0:
                        item.setIcon(getResources().getDrawable(R.drawable.ic_repeat_grey));
                        return true;
                    case 1:
                        item.setIcon(getResources().getDrawable(R.drawable.ic_repeat_black));
                        return true;
                    case 2:
                        item.setIcon(getResources().getDrawable(R.drawable.ic_repeat_one_black));
                        return true;
                    case 3:
                        item.setIcon(getResources().getDrawable(R.drawable.ic_shuffle_black));
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

    private int moujawid = 1;
    public void choisirRécitateur(MenuItem item) {
        item.setChecked(true);
        moujawid = item.getOrder();
        if (item.getOrder() != tajwid) pauseSound(null);
    }

    Thread thread = new Thread();
    public void startSound(View view) {
        buttonPause.setVisibility(View.VISIBLE);
        buttonStart.setVisibility(View.INVISIBLE);
        if (!thread.isAlive()) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        handler.post(new Runnable() { public void run() {
                            try {
                                Notifier();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }});
                        if (tilawa!=sourate() || tajwid!=moujawid())
                        {
                            if (sourate()==1)
                                switch (moujawid())
                                {
                                    case 1:
                                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.fatiha_1);
                                        break;
                                    case 2:
                                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.fatiha_2);
                                        break;
                                    case 3:
                                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.fatiha_3);
                                        break;
                                    case 4:
                                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.fatiha_4);
                                        break;
                                    case 5:
                                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.fatiha_5);
                                        break;
                                    case 6:
                                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.fatiha_6);
                                        break;
                                    case 7:
                                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.fatiha_7);
                                        break;
                                }
                            else
                            {
                                mediaPlayer.reset();
                                mediaPlayer.setDataSource(liens[moujawid()-1][sourate()-1]);
                                mediaPlayer.prepare();
                            }
                            tilawa = sourate(); tajwid = moujawid();
                        }
                        handler.post(new Runnable() { public void run() {
                            try {
                                seekBar.setMax(mediaPlayer.getDuration());
                                textEnd.setText((int) (seekBar.getMax() / (1000 * 60))+":"+(int) ((seekBar.getMax() / 1000) % 60));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }});
                        mediaPlayer.start();
                    } catch (IOException e) {
                        Snackbar.make(buttonStart, R.string.connexion_failed, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        handler.post(new Runnable() { public void run() {
                            buttonStart.setVisibility(View.VISIBLE);
                            buttonPause.setVisibility(View.INVISIBLE);
                        }});
                        tilawa = -1; tajwid = -1;
                    } catch (Exception e) {
                        Snackbar.make(buttonStart, R.string.connexion_cancelled, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        handler.post(new Runnable() { public void run() {
                            buttonStart.setVisibility(View.VISIBLE);
                            buttonPause.setVisibility(View.INVISIBLE);
                        }});
                        tilawa = -1; tajwid = -1;
                    }
                }
            } ) ;
            thread.start();
        }
    }
    public void pauseSound(View view) {
        thread.interrupt();
        mediaPlayer.pause();
        handler.post(new Runnable() { public void run() {
            try {
                NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                if (manager != null) manager.cancelAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }});
        buttonStart.setVisibility(View.VISIBLE);
        buttonPause.setVisibility(View.INVISIBLE);
    }
    public void previousElement(View view) {
        if (sourate()==1)
            spinner.setSelection(113, true);
        else
            spinner.setSelection(spinner.getSelectedItemPosition()-1, true);
    }
    public void nextElement(View view) {
        if (sourate()==114)
            spinner.setSelection(0, true);
        else
            spinner.setSelection(spinner.getSelectedItemPosition()+1, true);
    }

    public void replaySound(View view) {
        try {
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-10000);
            Synchroniser();
        } catch (Exception e) { }
    }

    public void forwardSound(View view) {
        try {
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+10000);
            Synchroniser();
        } catch (Exception e) { }
    }

    private void endOperation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    switch (repeatNumber%4) {
                        case 0:
                            NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                            if (manager != null) manager.cancelAll();
                            break;
                        case 1:
                            nextElement(null);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startSound(null);
                                }
                            }, 250);
                            break;
                        case 2:
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startSound(null);
                                }
                            }, 250);
                            break;
                        case 3:
                            spinner.setSelection((int)(Math.random()*114), true);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startSound(null);
                                }
                            }, 250);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, (int)(0.001*seekBar.getMax()));
    }
}
