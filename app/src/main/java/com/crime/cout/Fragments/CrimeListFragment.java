package com.crime.cout.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crime.cout.Adapter.CrimeAdapter;
import com.crime.cout.Database.SQLiteDatabaseHelper;
import com.crime.cout.Models.CrimeModel;
import com.crime.cout.R;

import java.util.ArrayList;
import java.util.List;


public class CrimeListFragment extends Fragment {
     View view;
    List<CrimeModel> crimeModelsNO;
    List<CrimeModel> crimeModels;
    CrimeAdapter crimeAdapter;

    RecyclerView recyclerView;
    LinearLayout layoutEmpty;
    EditText editTextSearch;
   SQLiteDatabaseHelper sqLiteDatabaseHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_crime_list, container, false);
        sqLiteDatabaseHelper=new SQLiteDatabaseHelper(getContext());
        initViews();
        initRecyclerView();
        getAllCrime();
        return  view;

    }
    //TODO: Revisit/Refactor for External DB
    private void getAllCrime() {
        crimeModels.addAll(sqLiteDatabaseHelper.getAllCrime());
        crimeModelsNO.addAll(crimeModels);

        if(crimeModels.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
        }else {
            recyclerView.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        }

        crimeAdapter.notifyDataSetChanged();



    }

    private void initRecyclerView() {
     crimeModels=new ArrayList<>();
     crimeModelsNO=new ArrayList<>();
        crimeAdapter = new CrimeAdapter(crimeModels ,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(crimeAdapter);


    }

    private void initViews() {
        recyclerView=view.findViewById(R.id.recyclerView);
        layoutEmpty=view.findViewById(R.id.layoutEmpty);
        editTextSearch=view.findViewById(R.id.editTextSearch);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String tag=editTextSearch.getText().toString();
                crimeModels.clear();
                if(tag.equals("")){
                    crimeModels.addAll(crimeModelsNO);
                }else {
                    for(CrimeModel crimeModel:crimeModelsNO){
                        if(crimeModel.getZipCode().contains(tag)){
                            crimeModels.add(crimeModel);
                        }

                    }
                }


               crimeAdapter.notifyDataSetChanged();
            }

        });


    }
}