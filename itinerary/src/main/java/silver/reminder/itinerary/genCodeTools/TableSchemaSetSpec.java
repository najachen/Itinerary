package silver.reminder.itinerary.genCodeTools;

import java.util.Map;

public interface TableSchemaSetSpec {

	Map getFieldMetaData();

	String SPACE = " ";

	// 基數
	Integer RADIX = 2;

	/*
	 * 欄位資料型別
	 */
	Integer INTEGER = (int) Math.pow(RADIX, 5);
	Integer REAL = (int) Math.pow(RADIX, 6);
	Integer TEXT = (int) Math.pow(RADIX, 7);
	Integer LONG = (int) Math.pow(RADIX, 8);

	/*
	 * 欄位資料
	 */
	Integer PRIMARY_KEY_AUTOINCREMENT = (int) Math.pow(RADIX, 1);
	Integer NOT_NULL = (int) Math.pow(RADIX, 2);
}
