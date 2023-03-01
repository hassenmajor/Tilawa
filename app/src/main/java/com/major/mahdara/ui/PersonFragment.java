package com.major.mahdara.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.major.mahdara.R;

import static com.major.mahdara.CentreActivity.*;

public class PersonFragment extends Fragment {

    View[] views = new View[7];

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i=0; i<views.length; i++)
                views[i].setBackground(null);
            v.setBackgroundColor(getResources().getColor(R.color.couleurArriere, null));
            récitateur = Integer.valueOf(v.getTag().toString());
            if (récitateur!=récitateur0) mediaPlayer.pause();
            getFragmentManager().popBackStack();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_person, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        views[0] = getView().findViewById(R.id.tableLecteur1);
        views[1] = getView().findViewById(R.id.tableLecteur2);
        views[2] = getView().findViewById(R.id.tableLecteur3);
        views[3] = getView().findViewById(R.id.tableLecteur4);
        views[4] = getView().findViewById(R.id.tableLecteur5);
        views[5] = getView().findViewById(R.id.tableLecteur6);
        views[6] = getView().findViewById(R.id.tableLecteur7);

        views[récitateur-1].setBackgroundColor(getResources().getColor(R.color.couleurArriere, null));

        for (int i=0; i<views.length; i++)
            views[i].setOnClickListener(onClickListener);

        super.onViewCreated(view, savedInstanceState);
    }

}
