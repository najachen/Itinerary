package silver.reminder.itinerary.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import silver.reminder.itinerary.util.GenTableTools;
import silver.reminder.itinerary.util.GenTables;


public class ItineraryDatabaseHelper extends SQLiteOpenHelper {

    private static ItineraryDatabaseHelper dbHelper;

    private ItineraryDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);

        GenTables genTables = new GenTables();
        genTables.prepareTable();
        tables = genTables.getTables();
    }

    public static ItineraryDatabaseHelper getInstance(Context context){
        if(dbHelper == null){
            dbHelper = new ItineraryDatabaseHelper(context, "ItineraryDatabase", null, 1);
        }
        return dbHelper;
    }

    private Map<String, GenTableTools> tables;

    @Override
    public void onCreate(SQLiteDatabase db) {

        Set<Entry<String, GenTableTools>> entrySet = this.tables.entrySet();
        for(Entry<String, GenTableTools> entry:entrySet){
            db.execSQL(entry.getValue().getCreateSql());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
