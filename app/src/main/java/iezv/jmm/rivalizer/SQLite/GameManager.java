package iezv.jmm.rivalizer.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import iezv.jmm.rivalizer.POJO.Game;

public class GameManager {

    private Helper dbh;
    private SQLiteDatabase db;

    public GameManager(Context c){ dbh = new Helper(c); }

    public void open() { db = dbh.getWritableDatabase(); }

    public void openRead() { db = dbh.getReadableDatabase(); }

    public void close() { dbh.close(); }

    public long insert(Game game){
        ContentValues values = new ContentValues();
        // values.put(Contract.GameTable.CLOUDID, game.getCloudId());
        values.put(Contract.GameTable.NAME, game.getName());
        if(game.getUrlPhoto()!=null) {
            values.put(Contract.GameTable.URLPHOTO, game.getUrlPhoto());
        }
        values.put(Contract.GameTable.DESCRIPTION, game.getDescription());
        values.put(Contract.GameTable.RULES, game.getRules());

        long id = db.insert(Contract.GameTable.TABLE, null, values);

        return id;
    }

    public int delete(Game game){
        String condition  = Contract.GameTable._ID + " = ?";
        String[] args = { game.getIdGame() + ""};
        int count = db.delete(Contract.GameTable.TABLE, condition, args);
        return count;
    }

    public int update(Game game){
        ContentValues values = new ContentValues();

        // values.put(Contract.GameTable.CLOUDID, game.getCloudId());
        values.put(Contract.GameTable.NAME, game.getName());
        if(game.getUrlPhoto()!=null) {
            values.put(Contract.GameTable.URLPHOTO, game.getUrlPhoto());
        }
        values.put(Contract.GameTable.DESCRIPTION, game.getDescription());
        values.put(Contract.GameTable.RULES, game.getRules());

        String condition = Contract.GameTable._ID + " = ?";
        String[] args = { game.getIdGame() + "" };

        int count = db.update(Contract.GameTable.TABLE, values, condition, args);

        return count;
    }

    public Cursor getCursor(){
        Cursor cursor = db.query(Contract.GameTable.TABLE, null, null, null, null, null, null);
        return cursor;
    }

    public Game getRow(Cursor c){
        Game game = new Game();
        game.setIdGame(c.getString(0));
        // game.setCloudId(c.getString(1));
        game.setName(c.getString(1));
        game.setUrlPhoto(c.getString(2));
        game.setDescription(c.getString(3));
        game.setRules(c.getString(4));

        return game;
    }

    public Game get(int id){
        String[] projection = {
                Contract.GameTable._ID,
                // Contract.GameTable.CLOUDID,
                Contract.GameTable.NAME,
                Contract.GameTable.URLPHOTO,
                Contract.GameTable.DESCRIPTION,
                Contract.GameTable.RULES
        };

        String where = Contract.GameTable._ID+" = ?";
        String[] parameters = new String[] {id+""};
        String groupby = null;
        String having = null;
        String orderby = null;

        Cursor c = db.query(Contract.GameTable.TABLE, projection, where, parameters, groupby, having, orderby);

        Game game = getRow(c);

        c.close();

        return game;
    }

    // Get específicos

    public List<Game> select (String condition){
        List<Game> lg = new ArrayList<Game>();
        Cursor c = db.query(Contract.GameTable.TABLE, null, condition, null, null, null, null);
        Game game;
        while(c.moveToNext()){
            game = getRow(c);
            lg.add(game);
        }

        c.close();
        return lg;
    }

}
