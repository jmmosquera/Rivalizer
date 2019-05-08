package iezv.jmm.rivalizer.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

import iezv.jmm.rivalizer.POJO.Rival;

public class RivalManager {
    private Helper dbh;
    private SQLiteDatabase db;

    public RivalManager (Context c) { dbh = new Helper(c); };

    public void open() { db = dbh.getWritableDatabase(); }

    public void openRead() { db = dbh.getReadableDatabase(); }

    public void close() { dbh.close(); }

    public long insert(Rival rival){
        ContentValues values = new ContentValues();
        values.put(Contract.RivalTable.CLOUDID, rival.getCloud_id());
        values.put(Contract.RivalTable.NAME, rival.getName());
        if(rival.getUrlPhoto()!=null){
            values.put(Contract.RivalTable.URLPHOTO, rival.getUrlPhoto());
        }
        values.put(Contract.RivalTable.IDGAMES, rival.getIdGames());

        long id = db.insert(Contract.RivalTable.TABLE, null, values);

        return id;
    }

    public int delete(Rival rival){
        String condition = Contract.PlaceTable._ID + " =  ?";
        String[] args = { rival.getId() + "" };
        int count = db.delete(Contract.RivalTable.TABLE, condition, args);
        return count;
    }

    public int update(Rival rival){
        ContentValues values = new ContentValues();
        values.put(Contract.RivalTable.CLOUDID, rival.getCloud_id());
        values.put(Contract.RivalTable.NAME, rival.getName());
        if(rival.getUrlPhoto()!=null){
            values.put(Contract.RivalTable.URLPHOTO, rival.getUrlPhoto());
        }
        values.put(Contract.RivalTable.IDGAMES, rival.getIdGames());

        String condition = Contract.RivalTable._ID + " =  ?";
        String[] args = { rival.getId()+""};

        int count = db.update(Contract.RivalTable.TABLE, values, condition, args);

        return count;
    }

    public Cursor getCursor(){
        Cursor cursor = db.query(Contract.RivalTable.TABLE, null, null, null, null, null, null);
        return cursor;
    }

    public Rival getRow(Cursor c){
        Rival rival = new Rival();
        rival.setId(c.getInt(0));
        rival.setCloud_id(c.getString(1));
        rival.setName(c.getString(2));
        rival.setfPlace(c.getString(3));
        rival.setUrlPhoto((c.getString(4)));
        rival.setIdGames(c.getInt(5));

        return rival;
    }

    public Rival get(long id){
        String [] projection = {
                Contract.RivalTable._ID,
                Contract.RivalTable.CLOUDID,
                Contract.RivalTable.NAME,
                Contract.RivalTable.FPLACE,
                Contract.RivalTable.URLPHOTO,
                Contract.RivalTable.IDGAMES
        };

        String where = Contract.PlaceTable._ID+" = ?";
        String[] parameters = new String[] { id+""};
        String groupby = null;
        String having = null;
        String orderby = null;

        Cursor c = db.query(Contract.RivalTable.TABLE, projection, where, parameters, groupby, having, orderby);

        Rival rival = getRow(c);

        c.close();

        return rival;
    }

    public List<Rival> select(String condition){
        List<Rival> lp = new ArrayList<Rival>();
        Cursor c = db.query(Contract.RivalTable.TABLE, null, condition, null, null, null, null);
        Rival rival;
        while (c.moveToNext()){
            rival = getRow(c);
            lp.add(rival);
        }

        c.close();

        return lp;
    }

}
