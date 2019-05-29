package iezv.jmm.rivalizer.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import iezv.jmm.rivalizer.POJO.Place;

public class PlaceManager {

    private Helper dbh;
    private SQLiteDatabase db;

    public PlaceManager (Context c) { dbh = new Helper(c); };

    public void open() { db = dbh.getWritableDatabase(); }

    public void openRead() { db = dbh.getReadableDatabase(); }

    public void close() { dbh.close(); }

    public long insert(Place place){
        ContentValues values = new ContentValues();
        values.put(Contract.PlaceTable.NAME, place.getName());
        values.put(Contract.PlaceTable.ADDRESS, place.getAddress());
        values.put(Contract.PlaceTable.COORDINATES, place.getCoordinates());
        if(place.getUrlPhoto()!=null) {
            values.put(Contract.PlaceTable.URLPHOTO, place.getUrlPhoto());
        }
        values.put(Contract.PlaceTable.REVIEW, place.getReview());

        long id = db.insert(Contract.PlaceTable.TABLE, null, values);

        return id;
    }

    public int delete(Place place){
        String condition = Contract.PlaceTable._ID + " = ?";
        String[] args = { place.getIdPlace() + ""};
        int count = db.delete(Contract.PlaceTable.TABLE, condition, args);
        return count;
    }

    public int update(Place place) {
        ContentValues values = new ContentValues();
        values.put(Contract.PlaceTable.NAME, place.getName());
        values.put(Contract.PlaceTable.ADDRESS, place.getAddress());
        values.put(Contract.PlaceTable.COORDINATES, place.getCoordinates());
        if(place.getUrlPhoto()!=null) {
            values.put(Contract.PlaceTable.URLPHOTO, place.getUrlPhoto());
        }
        values.put(Contract.PlaceTable.REVIEW, place.getReview());

        String condition = Contract.PlaceTable._ID + " = ?";
        String[] args = { place.getIdPlace() + "" };

        int count = db.update(Contract.PlaceTable.TABLE, values, condition, args);

        return count;
    }

    public Cursor getCursor(){
        Cursor cursor = db.query(Contract.PlaceTable.TABLE, null, null, null, null, null, null);
        return cursor;
    }

    public Place getRow(Cursor c){
        Place place = new Place();
        place.setIdPlace(c.getString(0));
        place.setName(c.getString(1));
        place.setAddress(c.getString(2));
        place.setCoordinates(c.getString(3));
        place.setUrlPhoto(c.getString(4));
        place.setReview(c.getString(5));

        return place;
    }

    public Place get(long id){
        String[] projection = {
                Contract.PlaceTable._ID,
                Contract.PlaceTable.CLOUDID,
                Contract.PlaceTable.NAME,
                Contract.PlaceTable.ADDRESS,
                Contract.PlaceTable.COORDINATES,
                Contract.PlaceTable.URLPHOTO,
                Contract.PlaceTable.URLPHOTO,
        };
        String where = Contract.PlaceTable._ID+" = ?";
        String[] parameters = new String[] {id+""};
        String groupby = null;
        String having = null;
        String orderby = null;

        Cursor c = db.query(Contract.PlaceTable.TABLE, projection, where, parameters, groupby, having, orderby);

        Place place = getRow(c);

        c.close();

        return place;
    }

    public List<Place> select(String condition){
        List<Place> lp = new ArrayList<Place>();
        Cursor c = db.query(Contract.PlaceTable.TABLE, null, condition, null, null, null, null);
        Place place;
        while (c.moveToNext()){
            place = getRow(c);
            lp.add(place);
        }

        c.close();
        return lp;
    }


}
