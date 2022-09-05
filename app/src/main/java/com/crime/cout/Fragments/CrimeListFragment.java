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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crime.cout.Adapter.CrimeAdapter;
import com.crime.cout.Database.SQLiteDatabaseHelper;
import com.crime.cout.Models.CrimeModel;
import com.crime.cout.R;
import com.crime.cout.Web.HttpRequestHelper;

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
    HttpRequestHelper requestHelper;//9-3-22
    RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //TODO: 9-3-22 Edit or Remove
        requestQueue = Volley.newRequestQueue(getContext());

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_crime_list, container, false);
        sqLiteDatabaseHelper = new SQLiteDatabaseHelper(getContext());
        //TODO: 9-3-22 Add similar object for HTTP-Helper?
        requestHelper = new HttpRequestHelper();//Added 9-3-22

        initViews();
        initRecyclerView();
        getAllCrime("");
        return  view;

    }
    //TODO: Revisit/Refactor for External DB
    private void getAllCrime(String zipCode) {

        crimeModels.addAll(sqLiteDatabaseHelper.getAllCrime());
        //TODO: added 9/3/22 - create same func for HTTP-Helper?
        //String zipCode = "91950";//added 9-3-22
        System.out.println("Look here "+ requestHelper.MakeHttpGetRequest(zipCode, requestQueue));

        //crimeModelsNO.addAll(requestHelper.MakeHttpGetRequest(zipCode, requestQueue));
        requestHelper.MakeHttpGetRequest(zipCode, requestQueue);
        System.out.println("Sample List "+requestHelper.getMyList());
        crimeModelsNO.addAll(requestHelper.getMyList());
        //crimeModelsNO.addAll(crimeModels);

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
                String zip = editTextSearch.getText().toString();
                crimeModels.clear();
                if(zip.equals("")){
                    crimeModels.addAll(crimeModelsNO);
                }else {
                    for(CrimeModel crimeModel:crimeModelsNO){
                        if(crimeModel.getZipCode().contains(zip)){
                            crimeModels.add(crimeModel);
                        }

                    }
                }


               crimeAdapter.notifyDataSetChanged();
            }

        });


    }
}