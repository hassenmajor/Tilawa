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
import android.widget.SearchView;

import com.major.mahdara.MySearchAdapter;
import com.major.mahdara.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.major.mahdara.CentreActivity.*;

public class SearchFragment extends Fragment {

    SearchView searchView;
    ListView searchList;
    MySearchAdapter adapter;
    static ArrayList<String> list = new ArrayList<>();
    static int position = 0;
    public static String query = "";

    String string = "";
    InputStream stream;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        searchView = (SearchView)view.findViewById(R.id.searchView);
        searchView.setQuery(query, false);
        searchList = (ListView)view.findViewById(R.id.searchList);
        adapter = new MySearchAdapter(this.getContext(), list);
        searchList.setAdapter(adapter);
        searchList.setSelectionFromTop(position, 0);

        try {
            stream = getActivity().getAssets().open("quran-recherche.txt");
            string = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
            String[] tableau = string.split("\n\n");
            simpleQuran = new String[114][];
            for (int i=0; i<simpleQuran.length; i++)
                simpleQuran[i] = tableau[i].split("\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (query.length()>1)
                    {
                        SearchFragment.query = query;
                        searchText();
                        return true;
                    }
                    else
                        return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackground(searchList.getBackground());
                chapitre = Integer.valueOf(list.get(position).split(";")[0]);
                verset = Integer.valueOf(list.get(position).split(";")[1]);
                SearchFragment.position = position;
                if (chapitre!=chapitre0) mediaPlayer.pause();
                getFragmentManager().popBackStack();
            }
        });
    }

    private void searchText() {
        adapter.clearItem();
        for (int i=0; i<simpleQuran.length; i++)
        for (int j=0; j<simpleQuran[i].length; j++)
        {
            if (simpleQuran[i][j].contains(query))
                adapter.addItem(i+1, j+1);
        }
        list = adapter.getList();
    }

}
