package ${boPackageName};

import android.database.Cursor;

import java.util.List;

<#list tables as table>
import ${javabeanPackageName}.${table.getKey()?cap_first};
</#list>
/**
 * ${genDate} by freemarker template
 */
public interface ${functionName?cap_first}Bo {

	<#list tables as table>
    long create${table.getKey()?cap_first}(${table.getKey()?cap_first} ${table.getKey()});

    void create${table.getKey()?cap_first}List(List<${table.getKey()?cap_first}> ${table.getKey()}List);

    int modify${table.getKey()?cap_first}(${table.getKey()?cap_first} ${table.getKey()});

    void modify${table.getKey()?cap_first}List(List<${table.getKey()?cap_first}> ${table.getKey()}List);

    void remove${table.getKey()?cap_first}(Integer id);

    void remove${table.getKey()?cap_first}List(List<Integer> ${table.getKey()}IdList);

    ${table.getKey()?cap_first} find${table.getKey()?cap_first}ById(Integer id);

    Cursor find${table.getKey()?cap_first}List(${table.getKey()?cap_first} ${table.getKey()});
    
    </#list>
}
