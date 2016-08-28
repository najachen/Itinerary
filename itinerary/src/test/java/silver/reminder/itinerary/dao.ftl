package ${daoPackageName};

import android.database.Cursor;

import java.util.List;

<#list tables as table>
import ${javabeanPackageName}.${table.getKey()?cap_first};
</#list>
/**
 * ${genDate} by freemarker template
 */
public interface ${functionName?cap_first}Dao {

	<#list tables as table>
    long insert${table.getKey()?cap_first}(${table.getKey()?cap_first} ${table.getKey()});

    void insert${table.getKey()?cap_first}List(List<${table.getKey()?cap_first}> ${table.getKey()}List);

    int update${table.getKey()?cap_first}(${table.getKey()?cap_first} ${table.getKey()});

    void update${table.getKey()?cap_first}List(List<${table.getKey()?cap_first}> ${table.getKey()}List);

    void delete${table.getKey()?cap_first}(Integer id);

    void delete${table.getKey()?cap_first}List(List<Integer> ${table.getKey()}IdList);

    ${table.getKey()?cap_first} select${table.getKey()?cap_first}ById(Integer id);

    Cursor select${table.getKey()?cap_first}List(${table.getKey()?cap_first} ${table.getKey()});
    
    </#list>
}
