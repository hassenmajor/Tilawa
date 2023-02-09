package com.major.mahdara.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.major.mahdara.MyListAdapter;
import com.major.mahdara.R;

import static com.major.mahdara.CentreActivity.*;

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

        MyListAdapter adapter = new MyListAdapter(this.getContext());
        listView.setAdapter(adapter);
        listView.setSelectionFromTop(chapitre-1, 0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackground(listView.getBackground());
                chapitre = position+1;
                if (chapitre!=chapitre0) mediaPlayer.pause();

                //getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
                getFragmentManager().popBackStack();
            }
        });

    }
}
