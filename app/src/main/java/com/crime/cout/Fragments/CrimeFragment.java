package com.crime.cout.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crime.cout.Database.SQLiteDatabaseHelper;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crime.cout.GPS.GpsManager;
import com.crime.cout.GPS.GpsUpdate;
import com.crime.cout.Models.CrimeModel;
import com.crime.cout.R;
import com.crime.cout.Web.HttpRequestHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



@SuppressWarnings("deprecation")
public class CrimeFragment extends Fragment implements OnMapReadyCallback, GpsUpdate {
    View view;
    PowerSpinnerView categorySpinner;
    EditText textViewZipCode;
    String category="Hviolent crime";
    Button buttonSearch;
    GoogleMap mMap=null;
    private GpsManager gpsManager = null;
    LocationManager locationManager;
    String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    boolean isLocationDraw=false;
    LatLng currentLocation=null;
    SQLiteDatabaseHelper sqLiteDatabaseHelper;
    List<CrimeModel> crimeModels;
    HttpRequestHelper requestHelper = new HttpRequestHelper(); //Added 8/26/22

    RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_crime, container, false);
        sqLiteDatabaseHelper=new SQLiteDatabaseHelper(getContext());
        crimeModels=new ArrayList<>();
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(getContext(), "Error : Map Fragment null", Toast.LENGTH_SHORT).show();
        }
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        initView();
        checkGPS();
        return view;
    }

    private void initView() {
        categorySpinner=view.findViewById(R.id.categorySpinner);
        textViewZipCode=view.findViewById(R.id.textViewZipCode);
        buttonSearch=view.findViewById(R.id.buttonSearch);

        //Search button
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String zipCode=textViewZipCode.getText().toString();
                if(mMap==null){
                    Toast.makeText(getContext(), "Please wait map loading", Toast.LENGTH_SHORT).show();
                }else if(zipCode.equals("")){
                    Toast.makeText(getContext(), "Please provide zip code", Toast.LENGTH_SHORT).show();
                }else {
                    crimeModels.clear();
                    //This is the line that renders the view based on DB retrieval
                    crimeModels.addAll(requestHelper.MakeHttpGetRequest(zipCode, requestQueue));
                    for(int i=0;i<crimeModels.size();i++){
                        drawMarker(i+"","crime",crimeModels.get(i).getCrimeName(),
                                new LatLng(Double.parseDouble(crimeModels.get(i).getLatitude()),Double.parseDouble(crimeModels.get(i).getLongitude())),
                                crimeModels.get(i).getCrimeType());
                    }
                    if(crimeModels.size()<1){
                        Toast.makeText(getContext(), "Nothing found", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        categorySpinner.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                categorySpinner.dismiss();
            }
        });
        categorySpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override public void onItemSelected(int oldIndex, @Nullable String oldItem, int newIndex, String newItem) {
                category=newItem;
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap=googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                //Toast.makeText(getContext(), "Marker Clicked", Toast.LENGTH_SHORT).show();
                if(marker.getTag().toString().equals("-1")){
                    marker.showInfoWindow();
                }else {
                    markerClicked(marker);
                }



                return true;
            }
        });
        checkGPS();
    }

    private void markerClicked(Marker marker) {
        Dialog dialog=new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.details_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        TextView textViewName
                ,textViewType,textViewZipCode
                ,textViewLocation;
        Button buttonClose;
        textViewName=dialog.findViewById(R.id.textViewName);
        textViewType=dialog.findViewById(R.id.textViewType);
        textViewZipCode=dialog.findViewById(R.id.textViewZipCode);
        textViewLocation=dialog.findViewById(R.id.textViewLocation);
        buttonClose=dialog.findViewById(R.id.buttonClose);

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        int index= Integer.parseInt(marker.getTag().toString());
        textViewName.setText(crimeModels.get(index).getCrimeName());
        textViewType.setText(crimeModels.get(index).getCrimeType());
        textViewZipCode.setText(crimeModels.get(index).getZipCode());
        textViewLocation.setText(crimeModels.get(index).getLatitude()+","+crimeModels.get(index).getLongitude());

    }

    private void checkGPS() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkLocationPermissions();
            return;
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            navigateUser();
        } else {
            showSettingsAlert();
        }

    }

    private void navigateUser() {
        Toast.makeText(getContext(), "Please wait for a while app getting your current location", Toast.LENGTH_LONG).show();
        gpsManager = new GpsManager(getContext());
        gpsManager.startGPS(getContext());
        gpsManager.setGPSCallback(this);
    }

    private boolean checkLocationPermissions() {
        int permissionResult;
        List<String> permissionsList = new ArrayList<>();
        for (String p : permissions) {
            permissionResult = ContextCompat.checkSelfPermission(getContext(), p);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(p);
            }
        }
        if (!permissionsList.isEmpty()) { //this is okay yes
            ActivityCompat.requestPermissions(getActivity(), permissionsList.toArray(new String[permissionsList.size()]), 202);
            return false;
        }else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 202) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), getString(R.string.permission_d), Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(getContext(), getString(R.string.permission_success), Toast.LENGTH_SHORT).show();
                checkGPS();
            }
        }
    }
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(getString(R.string.gps_n));
        alertDialog.setMessage(getString(R.string.gps_instruction));
        alertDialog.setPositiveButton(getString(R.string.setting), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent,2021);
            }
        });
        alertDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2021) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(getContext(), getString(R.string.gps_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), getString(R.string.gps_need), Toast.LENGTH_SHORT).show();

                checkGPS();
            }
        }
    }


    @Override
    public void onGPSUpdate(Location location) {
        if(!isLocationDraw){
            if(location!=null){
                //currentLocation=new LatLng(location.getLatitude(),location.getLongitude());
                currentLocation = new LatLng(32.71674, -117.16294);
                drawMarker(-1+"","current","You are here",currentLocation,"user");
                isLocationDraw=true;
            }
        }

    }

    private void drawMarker(String tag,String current, String you_are_here,LatLng latLng,String cat) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(you_are_here);
        markerOptions.snippet(cat);
        markerOptions.position(latLng);
        if(current.equals("current")){
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }else {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }

        Marker marker=   mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        marker.setTag(tag);
    }

}