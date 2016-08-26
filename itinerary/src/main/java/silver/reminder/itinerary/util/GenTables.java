package silver.reminder.itinerary.util;

import java.util.HashMap;
import java.util.Map;

public class GenTables {

	private Map<String, GenTableTools> tables = new HashMap<>();

	public Map<String, GenTableTools> getTables() {
		return tables;
	}

	public void prepareTable(){

		/*
		 * table - task
		 */
		GenTableTools taskTableConfigSet = new GenTableTools("task") {

			@Override
			public Map getFieldMetaData() {
				return new HashMap() {
					{
						put("id", GenTableSpec.INTEGER | GenTableSpec.PRIMARY_KEY_AUTOINCREMENT);
						put("name", GenTableSpec.TEXT);
						put("tm", GenTableSpec.TEXT | GenTableSpec.NOT_NULL);
						put("site", GenTableSpec.TEXT);
					}
				};
			}
		};
		tables.put(taskTableConfigSet.getTableName(), taskTableConfigSet);

		/*
		 * table - schedule
		 */
		GenTableTools scheduleTableConfigSet = new GenTableTools("schedule") {

			@Override
			public Map getFieldMetaData() {
				return new HashMap() {
					{
						put("id", GenTableSpec.INTEGER | GenTableSpec.PRIMARY_KEY_AUTOINCREMENT);
						put("tm", GenTableSpec.TEXT | GenTableSpec.NOT_NULL);
						put("taskId", GenTableSpec.INTEGER | GenTableSpec.NOT_NULL);
						put("soundFileId", GenTableSpec.INTEGER | GenTableSpec.NOT_NULL);
					}
				};
			}
		};
		tables.put(scheduleTableConfigSet.getTableName(), scheduleTableConfigSet);

		/*
		 * table - soundFile
		 */
		GenTableTools soundFileTableConfigSet = new GenTableTools("soundFile") {

			@Override
			public Map getFieldMetaData() {
				return new HashMap() {
					{
						put("id", GenTableSpec.INTEGER | GenTableSpec.PRIMARY_KEY_AUTOINCREMENT);
						put("fileName", GenTableSpec.TEXT | GenTableSpec.NOT_NULL);
					}
				};
			}
		};
		tables.put(soundFileTableConfigSet.getTableName(), soundFileTableConfigSet);

		/*
		 * table - shopping
		 */
		GenTableTools shoppingTableConfigSet = new GenTableTools("shopping") {

			@Override
			public Map getFieldMetaData() {
				return new HashMap() {
					{
						put("id", GenTableSpec.INTEGER | GenTableSpec.PRIMARY_KEY_AUTOINCREMENT);
						put("taskId", GenTableSpec.INTEGER | GenTableSpec.NOT_NULL);
						put("name", GenTableSpec.TEXT | GenTableSpec.NOT_NULL);
						put("quantity", GenTableSpec.INTEGER);
						put("unitPrice", GenTableSpec.REAL);
					}
				};
			}
		};
		tables.put(shoppingTableConfigSet.getTableName(), shoppingTableConfigSet);

		/*
		 * table - note
		 */
		GenTableTools noteTableConfigSet = new GenTableTools("note") {

			@Override
			public Map getFieldMetaData() {
				return new HashMap() {
					{
						put("id", GenTableSpec.INTEGER | GenTableSpec.PRIMARY_KEY_AUTOINCREMENT);
						put("taskId", GenTableSpec.INTEGER | GenTableSpec.NOT_NULL);
						put("noteContent", GenTableSpec.TEXT | GenTableSpec.NOT_NULL);
						put("noteExplain", GenTableSpec.TEXT);
					}
				};
			}
		};
		tables.put(noteTableConfigSet.getTableName(), noteTableConfigSet);
	}
}
