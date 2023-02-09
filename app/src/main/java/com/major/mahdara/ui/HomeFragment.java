package com.major.mahdara.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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

    TextView textView, textStart, textEnd, textVerset, textPartie, textChapitre;
    ImageButton buttonStart, buttonPause, imageNext, imagePrevious, imageReplay, imageForward;
    ScrollView scrollView;
    SeekBar seekBar;
    Timer timer;
    Handler handler = new Handler();
    Runnable Synchroniser = new Runnable() { public void run() {
        try {
            seekBar.setMax(mediaPlayer.getDuration());
            textEnd.setText((int) (seekBar.getMax() / (1000 * 60))+":"+(int) ((seekBar.getMax() / 1000) % 60));
        } catch (Exception e) {
            e.printStackTrace();
        }
    } };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.buttonStart:
                    startSound(); break;
                case R.id.buttonPause:
                    pauseSound(); break;
                case R.id.imagePrevious:
                    previousElement(); break;
                case R.id.imageNext:
                    nextElement(); break;
                case R.id.imageReplay:
                    replaySound(); break;
                case R.id.imageForward:
                    forwardSound(); break;
            }
        }
    };

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
        textChapitre = (TextView)getView().findViewById(R.id.textChapitre);
        seekBar = (SeekBar)getView().findViewById(R.id.seekBar);

        buttonStart = (ImageButton)getView().findViewById(R.id.buttonStart);
        buttonPause = (ImageButton)getView().findViewById(R.id.buttonPause);
        getView().findViewById(R.id.imagePrevious).setOnClickListener(onClickListener);
        getView().findViewById(R.id.imageNext).setOnClickListener(onClickListener);
        getView().findViewById(R.id.imageReplay).setOnClickListener(onClickListener);
        getView().findViewById(R.id.imageForward).setOnClickListener(onClickListener);

        buttonStart.setOnClickListener(onClickListener);
        buttonPause.setOnClickListener(onClickListener);

        Afficher();
        Synchroniser.run();
        Synchroniser();
        Synchroniser(mediaPlayer.isPlaying());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textStart.setText((int) (progress / (1000 * 60))+":"+(int) ((progress / 1000) % 60));
                if ( !fromUser && (progress>=0.999*seekBar.getMax()) )
                {
                    Synchroniser(false);
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
    private void Synchroniser(boolean isPlaying) {
        if (isPlaying) {
            buttonPause.setVisibility(View.VISIBLE);
            buttonStart.setVisibility(View.INVISIBLE);
        } else {
            buttonStart.setVisibility(View.VISIBLE);
            buttonPause.setVisibility(View.INVISIBLE);
        }
    }

    private void Afficher() {
        textChapitre.setText("﴿ "+titres[chapitre-1]+" ﴾");
        textVerset.setText(getString(R.string.ayat)+"\n"+versets[chapitre-1]);
        textPartie.setText(getString(R.string.hizb)+"\n"+parties[chapitre-1]);
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
        string = string + " " + versets[chapitre-1];
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
    public void startSound() {
        Synchroniser(true);
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
                        handler.post(Synchroniser);
                        mediaPlayer.start();
                    } catch (IOException e) {
                        Snackbar.make(buttonStart, R.string.connexion_failed, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        handler.post(new Runnable() { public void run() {
                            Synchroniser(false);
                        }});
                        chapitre0 = -1; récitateur0 = -1;
                    } catch (Exception e) {
                        Snackbar.make(buttonStart, R.string.connexion_cancelled, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        handler.post(new Runnable() { public void run() {
                            Synchroniser(false);
                        }});
                        chapitre0 = -1; récitateur0 = -1;
                    }
                }
            } ) ;
            thread.start();
        }
    }
    public void pauseSound() {
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
        Synchroniser(false);
    }

    public void previousElement() {
        if (chapitre==1)
            chapitre=114;
        else
            chapitre--;
        mediaPlayer.pause();
        Synchroniser(false);
        Afficher();
    }
    public void nextElement() {
        if (chapitre==114)
            chapitre=1;
        else
            chapitre++;
        mediaPlayer.pause();
        Synchroniser(false);
        Afficher();
    }
    public void randomElement() {
        chapitre = (int)(Math.random()*114);
        mediaPlayer.pause();
        Synchroniser(false);
        Afficher();
    }

    public void replaySound() {
        try {
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-10000);
            Synchroniser();
        } catch (Exception e) { }
    }

    public void forwardSound() {
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
                            nextElement();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startSound();
                                }
                            }, 250);
                            break;
                        case 2:
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startSound();
                                }
                            }, 250);
                            break;
                        case 3:
                            randomElement();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startSound();
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
