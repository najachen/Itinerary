package silver.reminder.itinerary.genCodeTools;

import java.util.HashMap;
import java.util.Map;

public class GenJavaCodeAndSqliteCreateTables_Itinerary {

	/*
	 * 功能分割
	 */
	private static final String funNmItinerary = "itinerary";
	private static final String funNmSoundDingDong = "soundDingDong";
	
	/*
	 * 資料表名稱對映資料表結構
	 */
	private static final Map<String, TableSchemaSet> tablesItinerary = new HashMap<>();
	private static final Map<String, TableSchemaSet> tablesSoundDingDong = new HashMap<>();
	
	/*
	 * 功能對映資料表
	 */
	private static final Map<String, Map<String, TableSchemaSet>> functionTableMap = new HashMap<String, Map<String, TableSchemaSet>>(){
		{
			put(funNmItinerary, tablesItinerary);
			put(funNmSoundDingDong, tablesSoundDingDong);
		}
	};

	/*
	 * getter
	 */
	public Map<String, Map<String, TableSchemaSet>> getFunctionTableMap() {
		return functionTableMap;
	}

	/*
	 * 這裡設定資料表結構
	 */
	public GenJavaCodeAndSqliteCreateTables_Itinerary prepareTable(){

		/*
		 * table - task
		 */
		TableSchemaSet taskTableSchemaSet = new TableSchemaSet("task") {

			@Override
			public Map getFieldMetaData() {
				return new HashMap() {
					{
						put("id", TableSchemaSetSpec.INTEGER | TableSchemaSetSpec.PRIMARY_KEY_AUTOINCREMENT);
						put("name", TableSchemaSetSpec.TEXT);
						put("tm", TableSchemaSetSpec.TEXT | TableSchemaSetSpec.NOT_NULL);
						put("site", TableSchemaSetSpec.TEXT);
					}
				};
			}
		};
		functionTableMap.get(funNmItinerary).put(taskTableSchemaSet.getTableName(), taskTableSchemaSet);
		
		/*
		 * table - schedule
		 */
		TableSchemaSet scheduleTableSchemaSet = new TableSchemaSet("schedule") {

			@Override
			public Map getFieldMetaData() {
				return new HashMap() {
					{
						put("id", TableSchemaSetSpec.INTEGER | TableSchemaSetSpec.PRIMARY_KEY_AUTOINCREMENT);
						put("tm", TableSchemaSetSpec.TEXT | TableSchemaSetSpec.NOT_NULL);
						put("taskId", TableSchemaSetSpec.INTEGER | TableSchemaSetSpec.NOT_NULL);
						put("soundFileId", TableSchemaSetSpec.INTEGER | TableSchemaSetSpec.NOT_NULL);
					}
				};
			}
		};
		functionTableMap.get(funNmSoundDingDong).put(scheduleTableSchemaSet.getTableName(), scheduleTableSchemaSet);

		/*
		 * table - soundFile
		 */
		TableSchemaSet soundFileTableSchemaSet = new TableSchemaSet("soundFile") {

			@Override
			public Map getFieldMetaData() {
				return new HashMap() {
					{
						put("id", TableSchemaSetSpec.INTEGER | TableSchemaSetSpec.PRIMARY_KEY_AUTOINCREMENT);
						put("fileName", TableSchemaSetSpec.TEXT | TableSchemaSetSpec.NOT_NULL);
					}
				};
			}
		};
		functionTableMap.get(funNmSoundDingDong).put(soundFileTableSchemaSet.getTableName(), soundFileTableSchemaSet);

		/*
		 * table - shopping
		 */
		TableSchemaSet shoppingTableSchemaSet = new TableSchemaSet("shopping") {

			@Override
			public Map getFieldMetaData() {
				return new HashMap() {
					{
						put("id", TableSchemaSetSpec.INTEGER | TableSchemaSetSpec.PRIMARY_KEY_AUTOINCREMENT);
						put("taskId", TableSchemaSetSpec.INTEGER | TableSchemaSetSpec.NOT_NULL);
						put("name", TableSchemaSetSpec.TEXT | TableSchemaSetSpec.NOT_NULL);
						put("quantity", TableSchemaSetSpec.INTEGER);
						put("unitPrice", TableSchemaSetSpec.REAL);
					}
				};
			}
		};
		functionTableMap.get(funNmItinerary).put(shoppingTableSchemaSet.getTableName(), shoppingTableSchemaSet);

		/*
		 * table - note
		 */
		TableSchemaSet noteTableSchemaSet = new TableSchemaSet("note") {

			@Override
			public Map getFieldMetaData() {
				return new HashMap() {
					{
						put("id", TableSchemaSetSpec.INTEGER | TableSchemaSetSpec.PRIMARY_KEY_AUTOINCREMENT);
						put("taskId", TableSchemaSetSpec.INTEGER | TableSchemaSetSpec.NOT_NULL);
						put("noteContent", TableSchemaSetSpec.TEXT | TableSchemaSetSpec.NOT_NULL);
						put("noteExplain", TableSchemaSetSpec.TEXT);
					}
				};
			}
		};
		functionTableMap.get(funNmItinerary).put(noteTableSchemaSet.getTableName(), noteTableSchemaSet);
		
		return this;
	}
}
