package silver.reminder.itinerary.util;

/**
 * Created by 阿鬼 on 2016/8/25.
 * 這個方法暫時關閉 因為未經過嚴密測試
 */
public class SqlEngine {

//    public static final String space = " ";
//    public static final String and = "and";
//    public static final String whereDescPiecewiseAnd = " and ";
//    public static final String whereDescPiecewiseEq = " = ";
//
//    public static final String limit = "limit";
//    public static final String offset = "offset";

//    public static String editSqlWhereArgs(Object conditionObj, Class clazz) {
//
//        try {
//            StringBuffer sql = new StringBuffer();
//            sql.append(space).append("where 1=1").append(space);
//
//            Map<String, String> whereArgs = new HashMap();
//            Method[] methods = clazz.getDeclaredMethods();
//            for (Method method : methods) {
//
//                String methodName = method.getName();
//                if (methodName.startsWith("get")) {
//                    //這裡可能有問題 未轉型 可能沒有這個方法
//                    Object getValue = method.invoke(conditionObj, null);
//                    if (getValue != null) {
//                        whereArgs.put(methodName.replace("get", ""), String.valueOf(getValue));
//                    }
//                }
//            }
//
//            Set<Entry<String, String>> entrySet = whereArgs.entrySet();
//            for (Entry<String, String> entry : entrySet) {
//                sql.append(and + space + entry.getKey() + "=" + entry.getValue()).append(space);
//
//                //test
//                System.out.println("key = " + entry.getKey() + " value = " + entry.getValue());
//            }
//            return sql.toString();
//
//        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return "";
//    }

//    public static Object cursorToJavaBean(Cursor cursor, Class clazz){
//        return null;
//    }

//    public static List cursorToJavaBeanList(Cursor cursor, Class clazz){
//        return null;
//    }

//    public static ContentValues model2ContentValues(){
//        return null;
//    }
}
