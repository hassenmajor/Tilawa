package com.major.mahdara.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.major.mahdara.CentreActivity;
import com.major.mahdara.R;

import static com.major.mahdara.CentreActivity.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        listView = (ListView)getView().findViewById(R.id.listView);

        ArrayAdapter<String> a=new ArrayAdapter<String>(this.getContext(),
                R.layout.my_spinner, CentreActivity.titres) {
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
        listView.setAdapter(a);

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chapitre = position+1;
                try {
                    CentreActivity.mediaPlayer.pause();
                } catch (Exception e) { }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
