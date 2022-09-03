package com.crime.cout.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.crime.cout.Models.CrimeModel;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    SQLiteDatabase db=null;
    private static final String DATABASE_NAME = "appDatabase2.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CRIME_TABLE = "crime_table";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String ZIP_CODE = "zip_code";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String TYPE = "type";

    public SQLiteDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + CRIME_TABLE +
                " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT, " +
                ZIP_CODE + " TEXT, " +
                LATITUDE + " TEXT, " +
                LONGITUDE + " TEXT, " +
                TYPE + " TEXT);";
        db.execSQL(query);
        setDBonCreate(db);

        // add test data when application install for the first time
        addNewCrime(new CrimeModel("Battery","violent crime","92129","32.94195", "-117.11120"));
        addNewCrime(new CrimeModel("Assault","violent crime","92127","33.01926", "-117.10222"));
        addNewCrime(new CrimeModel("Embezzlement","white collar crime","92021","32.81281", "-116.95862"));
        addNewCrime(new CrimeModel("Theft","property crime","92027","33.10456"," -116.99678"));

    }

    private void setDBonCreate(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + CRIME_TABLE);
        onCreate(db);
    }

    public boolean addNewCrime(CrimeModel crimeModel ){
        if(db==null){
            db = this.getWritableDatabase();
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME,crimeModel.getCrimeName());
        contentValues.put(ZIP_CODE,crimeModel.getZipCode());
        contentValues.put(TYPE,crimeModel.getCrimeType());
        contentValues.put(LATITUDE,crimeModel.getLatitude());
        contentValues.put(LONGITUDE,crimeModel.getLongitude());
        long result = db.insert(CRIME_TABLE,null, contentValues );
        return result != -1;
    }

    public List<CrimeModel> getAllCrime(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(" select * from "+ CRIME_TABLE ,null );
        List<CrimeModel> crimeModels=new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                CrimeModel crimeModel=new CrimeModel();
                crimeModel.setCrimeName(cursor.getString(1));
                crimeModel.setZipCode(cursor.getString(2));
                crimeModel.setLatitude(cursor.getString(3));
                crimeModel.setLongitude(cursor.getString(4));
                crimeModel.setCrimeType(cursor.getString(5));
                    crimeModels.add(crimeModel);
            }while (cursor.moveToNext()); // move cursor to next row
        }
        return crimeModels;
    }
    public List<CrimeModel> getCrimeByZipCode(String zipCode, String category){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(" select * from "+ CRIME_TABLE +" where " + ZIP_CODE+"='"+ zipCode +"'",null );
        List<CrimeModel> crimeModels=new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                CrimeModel crimeModel=new CrimeModel();
                crimeModel.setCrimeName(cursor.getString(1));
                crimeModel.setZipCode(cursor.getString(2));
                crimeModel.setLatitude(cursor.getString(3));
                crimeModel.setLongitude(cursor.getString(4));
                crimeModel.setCrimeType(cursor.getString(5));
                if(category.trim().toLowerCase().equals(crimeModel.getCrimeType().trim().toLowerCase())){
                    crimeModels.add(crimeModel);
                }
            }while (cursor.moveToNext()); // move cursor to next row
        }
        return crimeModels;
    }
}
