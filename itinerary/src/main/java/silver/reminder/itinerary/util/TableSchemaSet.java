package silver.reminder.itinerary.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class TableSchemaSet implements TableSchemaSetSpec {

	private String tableName;

	/*
	 * 建立資料表的欄位描述
	 */
	private Map<String, String> fieldDescMap = new HashMap<String, String>();
	
	/*
	 * 欄位與資料型態對映
	 */
	private Map<String, String> fieldDataTypeMap = new HashMap<String, String>();

	/*
	 * 建構子 建立sql描述與資料型態的map
	 */
	public TableSchemaSet(String tableName) {
		this.tableName = tableName;

		Set<Entry<String, Integer>> entrySet = getFieldMetaData().entrySet();
		for (Entry<String, Integer> entry : entrySet) {
			this.fieldDescMap.put(entry.getKey(), parseDescMaskCodeToSql(entry.getValue()));
			this.fieldDataTypeMap.put(entry.getKey(), parseFieldDataType(entry.getValue()));
		}
	}

	/*
	 * 資料型別對映
	 */
	private static String parseFieldDataType(Integer descMaskCode) {

		if ((descMaskCode & INTEGER) == INTEGER) {
			return "Integer";
		} else if ((descMaskCode & REAL) == REAL) {
			return "Float";
		} else if ((descMaskCode & TEXT) == TEXT) {
			return "String";
		}
		return "Object";
	}
	
	/*
	 * 解析描述遮罩碼 轉成欄位描述sql
	 */
	private static String parseDescMaskCodeToSql(Integer descMaskCode) {

		StringBuffer fieldDesc = new StringBuffer();

		if ((descMaskCode & INTEGER) == INTEGER) {
			fieldDesc.append("integer");
		} else if ((descMaskCode & REAL) == REAL) {
			fieldDesc.append("real");
		} else if ((descMaskCode & TEXT) == TEXT) {
			fieldDesc.append("text");
		}
		fieldDesc.append(SPACE);

		if ((descMaskCode & PRIMARY_KEY_AUTOINCREMENT) == PRIMARY_KEY_AUTOINCREMENT) {
			fieldDesc.append("primary key autoincrement");
		} else if ((descMaskCode & NOT_NULL) == NOT_NULL) {
			fieldDesc.append("not null");
		}
		fieldDesc.append(SPACE);

		return fieldDesc.toString();
	}

	/*
	 * 產生建立資料表sql
	 */
	public String getCreateSql() {

		StringBuffer createSql = new StringBuffer();
		createSql.append("create table").append(SPACE).append(tableName).append("(");

		Set<Entry<String, String>> entrySet = fieldDescMap.entrySet();
		int iter = 0;
		for (Entry<String, String> entry : entrySet) {
			createSql.append(entry.getKey()).append(SPACE).append(entry.getValue());
			if (iter != entrySet.size() - 1) {
				createSql.append(",");
			}
			iter++;
		}
		createSql.append(")");

		return createSql.toString();
	}
	
	/*
	 * getter
	 */
	public String getTableName() {
		return tableName;
	}

	public Map<String, String> getFieldDescMap() {
		return fieldDescMap;
	}

	public Map<String, String> getFieldDataTypeMap() {
		return fieldDataTypeMap;
	}
}
