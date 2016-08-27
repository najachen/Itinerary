package silver.reminder.itinerary.util;

import java.util.HashMap;
import java.util.Map;

public class GenTables {

	private Map<String, TableSchemaSet> tables = new HashMap<>();

	public Map<String, TableSchemaSet> getTables() {
		return tables;
	}

	public void prepareTable(){

		/*
		 * table - task
		 */
		TableSchemaSet taskTableConfigSet = new TableSchemaSet("task") {

			@Override
			public Map getFieldMetaData() {
				return new HashMap() {
					{
						put("id", TableSchemaGenSpec.INTEGER | TableSchemaGenSpec.PRIMARY_KEY_AUTOINCREMENT);
						put("name", TableSchemaGenSpec.TEXT);
						put("tm", TableSchemaGenSpec.TEXT | TableSchemaGenSpec.NOT_NULL);
						put("site", TableSchemaGenSpec.TEXT);
					}
				};
			}
		};
		tables.put(taskTableConfigSet.getTableName(), taskTableConfigSet);

		/*
		 * table - schedule
		 */
		TableSchemaSet scheduleTableConfigSet = new TableSchemaSet("schedule") {

			@Override
			public Map getFieldMetaData() {
				return new HashMap() {
					{
						put("id", TableSchemaGenSpec.INTEGER | TableSchemaGenSpec.PRIMARY_KEY_AUTOINCREMENT);
						put("tm", TableSchemaGenSpec.TEXT | TableSchemaGenSpec.NOT_NULL);
						put("taskId", TableSchemaGenSpec.INTEGER | TableSchemaGenSpec.NOT_NULL);
						put("soundFileId", TableSchemaGenSpec.INTEGER | TableSchemaGenSpec.NOT_NULL);
					}
				};
			}
		};
		tables.put(scheduleTableConfigSet.getTableName(), scheduleTableConfigSet);

		/*
		 * table - soundFile
		 */
		TableSchemaSet soundFileTableConfigSet = new TableSchemaSet("soundFile") {

			@Override
			public Map getFieldMetaData() {
				return new HashMap() {
					{
						put("id", TableSchemaGenSpec.INTEGER | TableSchemaGenSpec.PRIMARY_KEY_AUTOINCREMENT);
						put("fileName", TableSchemaGenSpec.TEXT | TableSchemaGenSpec.NOT_NULL);
					}
				};
			}
		};
		tables.put(soundFileTableConfigSet.getTableName(), soundFileTableConfigSet);

		/*
		 * table - shopping
		 */
		TableSchemaSet shoppingTableConfigSet = new TableSchemaSet("shopping") {

			@Override
			public Map getFieldMetaData() {
				return new HashMap() {
					{
						put("id", TableSchemaGenSpec.INTEGER | TableSchemaGenSpec.PRIMARY_KEY_AUTOINCREMENT);
						put("taskId", TableSchemaGenSpec.INTEGER | TableSchemaGenSpec.NOT_NULL);
						put("name", TableSchemaGenSpec.TEXT | TableSchemaGenSpec.NOT_NULL);
						put("quantity", TableSchemaGenSpec.INTEGER);
						put("unitPrice", TableSchemaGenSpec.REAL);
					}
				};
			}
		};
		tables.put(shoppingTableConfigSet.getTableName(), shoppingTableConfigSet);

		/*
		 * table - note
		 */
		TableSchemaSet noteTableConfigSet = new TableSchemaSet("note") {

			@Override
			public Map getFieldMetaData() {
				return new HashMap() {
					{
						put("id", TableSchemaGenSpec.INTEGER | TableSchemaGenSpec.PRIMARY_KEY_AUTOINCREMENT);
						put("taskId", TableSchemaGenSpec.INTEGER | TableSchemaGenSpec.NOT_NULL);
						put("noteContent", TableSchemaGenSpec.TEXT | TableSchemaGenSpec.NOT_NULL);
						put("noteExplain", TableSchemaGenSpec.TEXT);
					}
				};
			}
		};
		tables.put(noteTableConfigSet.getTableName(), noteTableConfigSet);
	}
}
