package ${boPackageName};

import android.content.Context;
import android.database.Cursor;

import java.util.List;

import ${daoPackageName}.${functionName?cap_first}Dao;
import ${daoPackageName}.${functionName?cap_first}DaoImpl;
<#list tables as table>
import ${javabeanPackageName}.${table.getKey()?cap_first};
</#list>
/**
 * ${genDate} by freemarker template
 */
public class ${functionName?cap_first}BoImpl implements ${functionName?cap_first}Bo {

    /**
     * 獨體模式
     */
    private static ${functionName?cap_first}Bo ${functionName}Bo;

    private ${functionName?cap_first}BoImpl(Context context){
        ${functionName}Dao = ${functionName?cap_first}DaoImpl.getInstance(context);
    }

    public static ${functionName?cap_first}Bo getInstance(Context context){
        if(${functionName}Bo == null){
            ${functionName}Bo = new ${functionName?cap_first}BoImpl(context);
        }
        return ${functionName}Bo;
    }

    private ${functionName?cap_first}Dao ${functionName}Dao;

    /*
        方法寫在下面 ======================================================
     */

	<#list tables as table>
    @Override
    public long create${table.getKey()?cap_first}(${table.getKey()?cap_first} ${table.getKey()}) {
        long rowId = this.${functionName}Dao.insert${table.getKey()?cap_first}(${table.getKey()});
        return rowId;
    }

    @Override
    public void create${table.getKey()?cap_first}List(List<${table.getKey()?cap_first}> ${table.getKey()}List) {
        this.${functionName}Dao.insert${table.getKey()?cap_first}List(${table.getKey()}List);
    }

    @Override
    public int modify${table.getKey()?cap_first}(${table.getKey()?cap_first} ${table.getKey()}) {
        int modifyDataAmount = this.${functionName}Dao.update${table.getKey()?cap_first}(${table.getKey()});
        return modifyDataAmount;
    }

    @Override
    public void modify${table.getKey()?cap_first}List(List<${table.getKey()?cap_first}> ${table.getKey()}List) {
        this.${functionName}Dao.update${table.getKey()?cap_first}List(${table.getKey()}List);
    }

    @Override
    public void remove${table.getKey()?cap_first}(Integer id) {
        this.${functionName}Dao.delete${table.getKey()?cap_first}(id);
    }

    @Override
    public void remove${table.getKey()?cap_first}List(List<Integer> ${table.getKey()}IdList) {
        this.${functionName}Dao.delete${table.getKey()?cap_first}List(${table.getKey()}IdList);
    }

    @Override
    public ${table.getKey()?cap_first} find${table.getKey()?cap_first}ById(Integer id) {
        ${table.getKey()?cap_first} result = this.${functionName}Dao.select${table.getKey()?cap_first}ById(id);
        return result;
    }

    @Override
    public Cursor find${table.getKey()?cap_first}List(${table.getKey()?cap_first} ${table.getKey()}) {
        Cursor result = this.${functionName}Dao.select${table.getKey()?cap_first}List(${table.getKey()});
        return result;
    }
    
    </#list>
}
