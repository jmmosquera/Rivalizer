package iezv.jmm.rivalizer.SQLite;

import android.provider.BaseColumns;

public class Contract {

    private Contract(){}

    public static abstract class GameTable implements BaseColumns{
        public static final String TABLE = "games";
        public static final String CLOUDID = "cloud_id";
        public static final String NAME = "name";
        public static final String URLPHOTO = "url_photo";
        public static final String DESCRIPTION = "description";
        public static final String RULES = "rules";
    }

    public static final String SQL_CREATE_GAMES =
        "create table "+GameTable.TABLE+" ("+
        GameTable._ID+" integer primary key autoincrement, "+
        GameTable.CLOUDID+" text, "+
        GameTable.NAME+" text, "+
        GameTable.URLPHOTO+" text, "+
        GameTable.DESCRIPTION+" text, "+
        GameTable.RULES+" text)";

    public static final String SQL_DROP_GAMES = " drop table if exists " + GameTable.TABLE;

    public static abstract class PlaceTable implements BaseColumns{
        public static final String TABLE = "places";
        public static final String CLOUDID = "cloud_id";
        public static final String NAME = "name";
        public static final String ADDRESS = "address";
        public static final String COORDINATES = "coordinates";
        public static final String URLPHOTO = "url_photo";
        public static final String REVIEW = "review";
    }

    public static final String SQL_CREATE_PLACES =
            "create table "+PlaceTable.TABLE+" ("+
            PlaceTable._ID+" integer primary key autoincrement, "+
            PlaceTable.CLOUDID+" text, "+
            PlaceTable.NAME+" text, "+
            PlaceTable.ADDRESS+" text, "+
            PlaceTable.COORDINATES+" text, "+
            PlaceTable.URLPHOTO+" text"+
            PlaceTable.REVIEW+" text)";
    public static final String SQL_DROP_PLACES = " drop table if exists " + PlaceTable.TABLE;

}
