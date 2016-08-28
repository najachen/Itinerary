package ${daoPackageName};

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ${utilPackageName}.TableSchemaSet;
import ${utilPackageName}.GenJavaCodeAndSqliteCreateTables_${moduleName?cap_first};

/**
  * ${genDate} by freemarker template
  */
public class ${moduleName?cap_first}DatabaseHelper extends SQLiteOpenHelper {

    /**
     * 獨體模式
     */
    private static ${moduleName?cap_first}DatabaseHelper dbHelper;

    private ${moduleName?cap_first}DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);

        // 資料表規格
        GenJavaCodeAndSqliteCreateTables_${moduleName?cap_first} genTables = new GenJavaCodeAndSqliteCreateTables_${moduleName?cap_first}();
        tables = genTables.prepareTable().getFunctionTableMap();
    }

    public static ${moduleName?cap_first}DatabaseHelper getInstance(Context context){
        if(dbHelper == null){
            dbHelper = new ${moduleName?cap_first}DatabaseHelper(context, "${moduleName?cap_first}Database", null, 1);
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
