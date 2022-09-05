package com.crime.cout.Web;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.crime.cout.MainActivity;
import com.crime.cout.Models.CrimeModel;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/*
Modified files:
    1. MainActivity.java
    2. AndroidManifest.xml
    3. CrimeReport.java
    4. build.gradle (:app)
    5. layout_web.xml
    6. network_security_config.xml
 */

public class HttpRequestHelper {
    private static final String TAG = "HttpRequestHelper";
    private final String IP = "10.0.2.2"; // this IP is AVD specific, setup by Google
    //private final String IP = "127.0.0.1"; //
    private final String DEFAULT_PORT = "8000";
    private final String SLUG = "c";
    private static List<CrimeModel> crimeModels = new ArrayList<>();


    public  List<CrimeModel> MakeHttpGetRequest(String zipCode, RequestQueue queue) {



          //TODO: Fix
//        RequestFuture<JSONObject> future = RequestFuture.newFuture();
//        JsonObjectRequest request = new JsonObjectRequest(URL, new JSONObject(), future, future);
//        requestQueue.add(request);
//        try {
//            JSONObject response = future.get(); // this will block
//        } catch (InterruptedException e) {
//            // exception handling
//        } catch (ExecutionException e) {
//            // exception handling
//        }
//
        final Gson gson = new Gson();
        final Map[] responseJsonObject = new Map[1];
        final Map[] crimeJsonObject = new Map[1];

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://" +
                        IP +
                        ":" +
                        DEFAULT_PORT +
                        "/" +
                        SLUG +
                        "/?zip=" +
                        zipCode,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("RAW resp: " + response.toString());
                        CrimeModel crimeModel;

                        // Map all objects from JSON into usable format:
                        responseJsonObject[0] = gson.fromJson(response.toString(), Map.class);
                        ArrayList list;
                        list = (ArrayList)  responseJsonObject[0].get(zipCode);


                        /*
                            JSON obj:
                                {
                                  "92105": [
                                         {
                                            "name" : string,
                                            "type" : string,
                                            "lat" : string,
                                            "lng" : string
                                         },
                                         {
                                            "name" : string,
                                            "type" : string,
                                            "lat" : string,
                                            "lng" : string
                                         },
                                        ...
                                       ]
                                   }
                        */

                        if (list != null && !list.isEmpty()) {

                            System.out.println("Crime List: " + list);
                            //Toast.makeText(context, "JSON response received (# crimes = " + list.size() + ")", Toast.LENGTH_SHORT).show();

                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i) != null) {
                                    Map map = (Map) list.get(i);
                                    crimeModel = new CrimeModel(

                                            (String) map.get("name"),
                                            (String) map.get("type"),
                                            zipCode,
                                            (String) map.get("lat"),
                                            (String) map.get("lng")
                                    );
                                   //TODO: Shove this into a list and pass it back
                                    System.out.println(crimeModel.toString());
                                    crimeModels.add(crimeModel);


                                } else {
                                    System.out.println("An error occurred with the Map objects in the response.");
                                }
                            }//end for loop
                            //System.out.println("Check right here BEFORE?: " + crimeModels.size());
//                            fillList(crimeModels);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "HTTP Response Error: " + error.toString());
                    }
                }
        );

        queue.add(objectRequest);
//        System.out.println("Check right here FIRST?: " + crimeModels.size());
//        try
//        {
//            Thread.sleep(1000);
//        }
//        catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
//        System.out.println("Check right here NEXT?: " + crimeModels.size());

        return crimeModels;
    }

    private void fillList(List<CrimeModel> l)
    {
        crimeModels.addAll(l);
    }

    public static List<CrimeModel> getMyList(){
        return crimeModels;
    }


}
