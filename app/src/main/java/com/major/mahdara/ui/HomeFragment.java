package com.major.mahdara.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.major.mahdara.FullActivity;
import com.major.mahdara.MyBroadcastReceiver;
import com.major.mahdara.R;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static com.major.mahdara.CentreActivity.*;

public class HomeFragment extends Fragment {

    TextView textView, textStart, textEnd, textVerset, textPartie;
    Spinner spinner;
    ImageButton buttonStart, buttonPause;
    ScrollView scrollView;
    SeekBar seekBar;
    Timer timer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        scrollView = (ScrollView)getView().findViewById(R.id.scrollView);
        textView = (TextView)getView().findViewById(R.id.textView);
        textPartie = (TextView)getView().findViewById(R.id.textPartie);
        textVerset = (TextView)getView().findViewById(R.id.textVerset);
        textStart = (TextView)getView().findViewById(R.id.textStart);
        textEnd = (TextView)getView().findViewById(R.id.textEnd);
        spinner = (Spinner)getView().findViewById(R.id.spinner);
        buttonStart = (ImageButton)getView().findViewById(R.id.buttonStart);
        buttonPause = (ImageButton)getView().findViewById(R.id.buttonPause);
        seekBar = (SeekBar)getView().findViewById(R.id.seekBar);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSound(v);
            }
        });

        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseSound(v);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                récitateur = position+1;
                Afficher();
                scrollView.setScrollY(0);
                textVerset.setText(getString(R.string.ayat)+"\n"+durées[position]);
                textPartie.setText(getString(R.string.hizb)+"\n"+parties[position]);
                pauseSound(null);
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
        String string;
        if (chapitre==1) {
            string = getString(R.string.fatiha);
            textView.setText(string);
            return;
        }
        else if (chapitre==9)
            string = "\n" + quran[chapitre-1];
        else
            string = getString(R.string.basmala) + "\n" + quran[chapitre-1];
        for (int n=2; n<290; n++)
            if (string.contains("("+n+")"))
                string = string.replace("("+n+")", "("+(n-1)+")");
            else break;
        string = string + " " + durées[chapitre-1];
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

    NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "channel_id");
    private void Notifier() {
        final Intent intent = new Intent(getActivity(), FullActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity().getApplicationContext(), 0, intent, 0);
        PendingIntent pendingIntent0 = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, new Intent(getActivity(), MyBroadcastReceiver.class), 0);
        builder = new NotificationCompat.Builder(getContext(), "channel_id")
                .setContentTitle(titres[chapitre-1])
                .setContentText(getString(R.string.note_text_prim))
                .setSmallIcon(R.drawable.ic_mahdara)
                .setStyle(new NotificationCompat.BigTextStyle())
                //.addAction(new NotificationCompat.Action(0, "Stop", intent0))
                .setContentIntent(pendingIntent)
                .setDeleteIntent(pendingIntent0)
                .setOngoing(false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        NotificationManager manager = (NotificationManager)getActivity().getSystemService(NOTIFICATION_SERVICE);
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
                        if (chapitre0!=chapitre || récitateur0!=récitateur)
                        {
                            if (chapitre==1)
                                switch (récitateur)
                                {
                                    case 1:
                                        mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.fatiha_1);
                                        break;
                                    case 2:
                                        mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.fatiha_2);
                                        break;
                                    case 3:
                                        mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.fatiha_3);
                                        break;
                                    case 4:
                                        mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.fatiha_4);
                                        break;
                                    case 5:
                                        mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.fatiha_5);
                                        break;
                                    case 6:
                                        mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.fatiha_6);
                                        break;
                                    case 7:
                                        mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.fatiha_7);
                                        break;
                                }
                            else
                            {
                                mediaPlayer.reset();
                                mediaPlayer.setDataSource(liens[récitateur-1][chapitre-1]);
                                mediaPlayer.prepare();
                            }
                            chapitre0 = chapitre; récitateur0 = récitateur;
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
                        chapitre0 = -1; récitateur0 = -1;
                    } catch (Exception e) {
                        Snackbar.make(buttonStart, R.string.connexion_cancelled, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        handler.post(new Runnable() { public void run() {
                            buttonStart.setVisibility(View.VISIBLE);
                            buttonPause.setVisibility(View.INVISIBLE);
                        }});
                        chapitre0 = -1; récitateur0 = -1;
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
                NotificationManager manager = (NotificationManager)getActivity().getSystemService(NOTIFICATION_SERVICE);
                if (manager != null) manager.cancelAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }});
        buttonStart.setVisibility(View.VISIBLE);
        buttonPause.setVisibility(View.INVISIBLE);
    }

    public void previousElement(View view) {
        if (chapitre==1)
            spinner.setSelection(113, true);
        else
            spinner.setSelection(spinner.getSelectedItemPosition()-1, true);
    }
    public void nextElement(View view) {
        if (chapitre==114)
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
                            NotificationManager manager = (NotificationManager)getActivity().getSystemService(NOTIFICATION_SERVICE);
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
