package silver.reminder.itinerary.util;

import java.util.Map;

public interface GenTableSpec {

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

	/*
	 * 欄位資料
	 */
	Integer PRIMARY_KEY_AUTOINCREMENT = (int) Math.pow(RADIX, 1);
	Integer NOT_NULL = (int) Math.pow(RADIX, 2);
}
