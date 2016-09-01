package silver.reminder.itinerary.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import silver.reminder.itinerary.util.TableSchemaSet;
import silver.reminder.itinerary.util.GenJavaCodeAndSqliteCreateTables_Itinerary;

/**
  * Wed Aug 31 22:02:29 CST 2016 by freemarker template
  */
public class ItineraryDatabaseHelper extends SQLiteOpenHelper {

    /**
     * 獨體模式
     */
    private static ItineraryDatabaseHelper dbHelper;

    private ItineraryDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);

        // 資料表規格
        GenJavaCodeAndSqliteCreateTables_Itinerary genTables = new GenJavaCodeAndSqliteCreateTables_Itinerary();
        tables = genTables.prepareTable().getFunctionTableMap();
    }

    public static ItineraryDatabaseHelper getInstance(Context context){
        if(dbHelper == null){
            dbHelper = new ItineraryDatabaseHelper(context, "ItineraryDatabase", null, 1);
        }
        return dbHelper;
    }

    private Map<String, Map<String, TableSchemaSet>> tables;

    @Override
    public void onCreate(SQLiteDatabase db) {
        Set<Entry<String, Map<String, TableSchemaSet>>> entrySet = this.tables.entrySet();

        for(Entry<String, Map<String, TableSchemaSet>> entry : entrySet){
        	Map<String, TableSchemaSet> tableSchemaSets = entry.getValue();

        	for(Entry<String, TableSchemaSet> e : tableSchemaSets.entrySet()){
        		db.execSQL(e.getValue().getCreateSql());
        	}
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
