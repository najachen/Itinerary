package silver.reminder.itinerary.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import silver.reminder.itinerary.genCodeTools.GenJavaCodeAndSqliteCreateTables_Itinerary;
import silver.reminder.itinerary.genCodeTools.TableSchemaSet;


/**
 * Mon Sep 05 23:27:31 CST 2016 by freemarker template
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

    public static ItineraryDatabaseHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new ItineraryDatabaseHelper(context, "ItineraryDatabase.db", null, 1);
        }
        return dbHelper;
    }

    private Map<String, Map<String, TableSchemaSet>> tables;

    @Override
    public void onCreate(SQLiteDatabase db) {
        Set<Entry<String, Map<String, TableSchemaSet>>> entrySet = this.tables.entrySet();

        for (Entry<String, Map<String, TableSchemaSet>> entry : entrySet) {
            Map<String, TableSchemaSet> tableSchemaSets = entry.getValue();

            for (Entry<String, TableSchemaSet> e : tableSchemaSets.entrySet()) {
                db.execSQL(e.getValue().getCreateSql());

                //test-
                Log.v(" create table ---- ", e.getValue().getCreateSql());
            }
        }

        //test- 測試資料
//        for (int i = 0; i < 20; i++) {
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.DATE, i);
//
//            ContentValues task = new ContentValues();
//            task.put("name", "Peter");
//            task.put("site", "marlibe " + i);
//            task.put("time", calendar.getTimeInMillis());
//
//            long rowId = db.insert("task", null, task);
//            Log.v("rowId ====", String.valueOf(rowId));
//
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//            String dateStr = simpleDateFormat.format(calendar.getTime());
//            Log.v("date ====", rowId + "/" + dateStr);
//
//            //============================
//
//            ContentValues soundFile = new ContentValues();
//            soundFile.put("fileName", "sound" + i);
//
//            long rowIdSoundFile = db.insert("soundFile", null, soundFile);
//            Log.v("rowIdSoundFile ====", String.valueOf(rowIdSoundFile));
//
//            //============================
//
//            for (int j = 0; j < 20; j++) {
//
//                ContentValues shopping = new ContentValues();
//                shopping.put("taskId", i);
//                shopping.put("name", "buyOne" + j);
//                shopping.put("quantity", j);
//                shopping.put("unitPrice", (float) j / 10);
//
//                long rowIdShopping = db.insert("shopping", null, shopping);
//                Log.v("rowIdShopping ====", String.valueOf(rowIdShopping));
//
//                //============================
//
//                ContentValues note = new ContentValues();
//                note.put("taskId", i);
//                note.put("noteContent", "noteContent" + j);
//                note.put("noteExplain", "noteExplain" + j);
//
//                long rowIdNote = db.insert("note", null, note);
//                Log.v("rowIdNote ====", String.valueOf(rowIdNote));
//            }
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
