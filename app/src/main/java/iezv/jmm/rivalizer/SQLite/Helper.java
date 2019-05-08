package iezv.jmm.rivalizer.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Helper  extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "rivalizer.db";
    public static final int DATABASE_VERSION = 1;

    public Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(Contract.SQL_CREATE_GAMES);
        db.execSQL(Contract.SQL_CREATE_PLACES);
        db.execSQL(Contract.SQL_CREATE_RIVALS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersiun, int newVersion){
        db.execSQL(Contract.SQL_DROP_GAMES);
        db.execSQL(Contract.SQL_DROP_PLACES);
        db.execSQL(Contract.SQL_DROP_RIVALS);

        onCreate(db);
    }

}
