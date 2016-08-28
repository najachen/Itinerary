package ${daoPackageName};

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

<#list tables as table>
import ${javabeanPackageName}.${table.getKey()?cap_first};
</#list>
/**
 * ${genDate} by freemarker template
 */
public class ${functionName?cap_first}DaoImpl implements ${functionName?cap_first}Dao {

    /**
     * 獨體模式
     */
    private static ${functionName?cap_first}Dao ${functionName}Dao;

    private ${functionName?cap_first}DaoImpl(Context context) {
        SQLiteOpenHelper dbHelper = ${moduleName?cap_first}DatabaseHelper.getInstance(context);
        readableDB = dbHelper.getReadableDatabase();
        writableDB = dbHelper.getWritableDatabase();
    }

    public static ${functionName?cap_first}Dao getInstance(Context context) {
        if (${functionName}Dao == null) {
            ${functionName}Dao = new ${functionName?cap_first}DaoImpl(context);
        }
        return ${functionName}Dao;
    }

    private SQLiteDatabase readableDB;
    private SQLiteDatabase writableDB;

    /*
        方法寫在下面 ======================================================
     */
	<#list tables as table>
    @Override
    public long insert${table.getKey()?cap_first}(${table.getKey()?cap_first} ${table.getKey()}) {
		
        ContentValues contentValues = new ContentValues();
        <#list table.getValue()?split("_") as fieldInfo>
        	<#if fieldInfo?is_even_item>
		        <#if fieldName != 'id'>
		contentValues.put("${fieldName}", ${table.getKey()}.get${fieldName?cap_first}());
				</#if>
			<#else>
				<#assign fieldName = fieldInfo>
			</#if>
        </#list>
        long rowId = this.writableDB.insert("${table.getKey()}", null, contentValues);
        return rowId;
    }

    @Override
    public void insert${table.getKey()?cap_first}List(List<${table.getKey()?cap_first}> ${table.getKey()}List) {

        this.writableDB.beginTransaction();

        for (${table.getKey()?cap_first} ${table.getKey()} : ${table.getKey()}List) {
            this.insert${table.getKey()?cap_first}(${table.getKey()});
        }
        this.writableDB.setTransactionSuccessful();
        this.writableDB.endTransaction();
    }

    @Override
    public int update${table.getKey()?cap_first}(${table.getKey()?cap_first} ${table.getKey()}) {

        //這裡是要修改的資料
        ContentValues contentValues = new ContentValues();
        <#list table.getValue()?split("_") as fieldInfo>
        	<#if fieldInfo?is_even_item>
		        <#if fieldName != 'id'>
		contentValues.put("${fieldName}", ${table.getKey()}.get${fieldName?cap_first}());
		        </#if>
	        <#else>
	        	<#assign fieldName = fieldInfo>
	        </#if>
        </#list>
        int updateDataAmount = this.writableDB.updateWithOnConflict("${table.getKey()}", contentValues, "id = ?", new String[]{${table.getKey()}.getId().toString()}, SQLiteDatabase.CONFLICT_ROLLBACK);
        return updateDataAmount;
    }

    @Override
    public void update${table.getKey()?cap_first}List(List<${table.getKey()?cap_first}> ${table.getKey()}List) {

        this.writableDB.beginTransaction();

        for (${table.getKey()?cap_first} ${table.getKey()} : ${table.getKey()}List) {
            this.update${table.getKey()?cap_first}(${table.getKey()});
        }
        this.writableDB.setTransactionSuccessful();
        this.writableDB.endTransaction();
    }

    @Override
    public void delete${table.getKey()?cap_first}(Integer id) {

        int deleteDataAmount = this.writableDB.delete("${table.getKey()}", "id = ?", new String[]{id.toString()});
        if (deleteDataAmount > 1) {
            Log.d("移除 ${table.getKey()} 異常!! 移除了 ", deleteDataAmount + " 筆");
        }
    }

    @Override
    public void delete${table.getKey()?cap_first}List(List<Integer> ${table.getKey()}IdList) {

        this.writableDB.beginTransaction();

        for (Integer ${table.getKey()}Id : ${table.getKey()}IdList) {
            this.delete${table.getKey()?cap_first}(${table.getKey()}Id);
        }
        this.writableDB.setTransactionSuccessful();
        this.writableDB.endTransaction();
    }

    @Override
    public ${table.getKey()?cap_first} select${table.getKey()?cap_first}ById(Integer id) {

        Cursor ${table.getKey()}Cursor = this.readableDB.rawQuery("select * from ${table.getKey()} where id = ?", new String[]{id.toString()});

        ${table.getKey()?cap_first} result = new ${table.getKey()?cap_first}();

        String[] columnNames = ${table.getKey()}Cursor.getColumnNames();
        for (String columnName : columnNames) {
            int columnIndex = ${table.getKey()}Cursor.getColumnIndexOrThrow(columnName);

            switch (columnName) {
                <#list table.getValue()?split("_") as fieldInfo>
                	<#if fieldInfo?is_even_item>
                case "${fieldName}":
	                	<#if fieldInfo == 'Integer'>
	                result.set${fieldName?cap_first}(${table.getKey()}Cursor.getInt(columnIndex));
	                    <#elseif fieldInfo == 'String'>
	                result.set${fieldName?cap_first}(${table.getKey()}Cursor.getString(columnIndex));
	                    </#if>
	                break;
                	<#else>
                		<#assign fieldName = fieldInfo>
                	</#if>
                </#list>
                default:
                    Log.d("出現欄位名稱錯誤 => ", columnName + columnIndex);
            }
        }
        return result;
    }

    @Override
    public Cursor select${table.getKey()?cap_first}List(${table.getKey()?cap_first} ${table.getKey()}) {

        StringBuffer sqlWhere = new StringBuffer();
        sqlWhere.append(" 1=1 ");

        List<String> whereArgs = new ArrayList<String>();

		<#list table.getValue()?split("_") as fieldInfo>
			<#if fieldInfo?is_even_item>
		${fieldInfo} ${fieldName} = ${table.getKey()}.get${fieldName?cap_first}();
        if (${fieldName} != null && ${fieldName}.toString().length() > 0) {
            sqlWhere.append(" and ${fieldName} = ? ");
            whereArgs.add(${fieldName}.toString());
        }
			<#else>
				<#assign fieldName = fieldInfo>
			</#if>
        </#list>
        String[] whereArgsStringArray = new String[whereArgs.size()];
        whereArgsStringArray = whereArgs.toArray(whereArgsStringArray);

        Cursor result = this.readableDB.query("${table.getKey()}", null, sqlWhere.toString(), whereArgsStringArray, null, null, "id");
        return result;
    }
    
    </#list>
}
