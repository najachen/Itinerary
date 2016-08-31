package silver.reminder.itinerary;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;

public class StartGenCode {

    /*
     * 套件名稱組合
     */
    private static final String packageNamePrefix = "silver.reminder.";

    // private static final String moduleNameInventory = "inventory";
    private static final String moduleName = "itinerary";

    private static final String packageNamePostfixBo = ".bo";
    private static final String packageNamePostfixDao = ".dao";
    private static final String packageNamePostfixJavaBean = ".javabean";
    private static final String packageNamePostfixUtil = ".util";

    /*
     * 樣板檔
     */
    private static final String tmpBo = "bo.ftl";
    private static final String tmpBoImpl = "boImpl.ftl";
    private static final String tmpDao = "dao.ftl";
    private static final String tmpDaoImpl = "daoImpl.ftl";
    private static final String tmpDatabaseHelper = "databaseHelper.ftl";
    private static final String tmpJavaBean = "javaBean.ftl";

    /**
     * 檔案輸出的路徑修飾(會因ide不同而不同 須注意)
     */
    private static final String fileExtName = ".java";
    // eclipse 適用
//    private static final String packagePathPrefix = "src\\";
    // android studio 適用
     private static final String packagePathPrefix = "src\\main\\java\\";

    /**
     * 檔案本身所在位置
     */
    // eclipse
//    private static final String thisFilePath = "D:\\AndroidProjects\\EclipseWorkSpace\\LambdaTest\\src\\gen\\java\\code\\tools\\";
    //android studio
//	private static final String thisFilePath = "D:\\PccuAndroidProj\\Itinerary\\itinerary\\src\\test\\java\\silver\\reminder\\itinerary\\";
	//文化大學電腦路徑
    private static final String thisFilePath = "E:\\android\\projects\\Itinerary\\itinerary\\src\\test\\java\\silver\\reminder\\itinerary\\";

    /**
     * 產生時間
     */
    private static Date dateNow;

    /**
     * start gen code
     */
    // eclipse
//    public static void main(String[] args) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
    // android studio
	@Test
	public void genCode() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {

        // 產生程式碼的時間 以利版本控管
        dateNow = new Date();

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);

        // 第一個參數 com.lambda.test.LambdaTest.class
        // 第二個參數 "" 所以樣板檔放在 com.lambda.test
        // 也就是說 第二個參數是第一個參數類別的相對路徑
        // 另一種寫法
        // cfg.setDirectoryForTemplateLoading(new
        // File("D:\\Download_Station\\ws\\LambdaTest\\src\\com\\lambda\\sql"));
        // 也可以

        // eclipse 適用
//        cfg.setClassForTemplateLoading(StartGenCode.class, "");
        // Android Studio 適用
//		cfg.setDirectoryForTemplateLoading(new File("D:\\PccuAndroidProj\\Itinerary\\itinerary\\src\\test\\java\\silver\\reminder\\itinerary\\"));
		//文化大學電腦路徑
        cfg.setDirectoryForTemplateLoading(new File("E:\\android\\projects\\Itinerary\\itinerary\\src\\test\\java\\silver\\reminder\\itinerary\\"));

        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
        cfg.setLocale(Locale.TAIWAN);

		/*
         * 全部資料
		 */
        GenJavaCodeAndSqliteCreateTables_Itinerary genObj = new GenJavaCodeAndSqliteCreateTables_Itinerary();
        Map<String, Map<String, TableSchemaSet>> functionTableMap = genObj.prepareTable().getFunctionTableMap();

		/*
		 * 開始輸出檔案
		 */
        outputSqliteOpenHelperFile(cfg);

        // 依功能切開
        for (Entry<String, Map<String, TableSchemaSet>> entryFunction : functionTableMap.entrySet()) {
            String functionName = entryFunction.getKey();
            Map<String, TableSchemaSet> tables = entryFunction.getValue();

            outputBoFile(cfg, functionName, tables);
            outputBoImplFile(cfg, functionName, tables);
            outputDaoFile(cfg, functionName, tables);
            outputDaoImplFile(cfg, functionName, tables);

            // 依資料表切開
            for (Entry<String, TableSchemaSet> entryTable : tables.entrySet()) {
                String tableName = entryTable.getKey();
                TableSchemaSet tableSchemaSet = entryTable.getValue();

                outputJavabeanFile(cfg, functionName, tableName, tableSchemaSet);
            }
        }

        // 輸出完畢 傳送公用程式
        moveUtilFile();
    }

    /**
     * 產生 javabean
     */
    private static void outputJavabeanFile(Configuration cfg, String funNm, String tbNm, TableSchemaSet tbShmSet) {

        try {
            Template temp = cfg.getTemplate(tmpJavaBean);
            String packageName = packageNamePrefix + moduleName + packageNamePostfixJavaBean;

            // 準備資料 插入樣板
            Map<String, Object> root = new HashMap<String, Object>();
            root.put("javabeanPackageName", packageName);
            root.put("genDate", dateNow.toString());
            root.put("className", capFirstChar(tbNm));
            root.put("fieldList", tbShmSet.getFieldDataTypeMap().entrySet());

            // 輸出檔案
            File dir = new File(packagePathPrefix + parsePackageNameToFileOutputPath(packageName));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File outputFile = new File(dir.getAbsolutePath(), capFirstChar(tbNm) + fileExtName);
            FileOutputStream fos = new FileOutputStream(outputFile);
            OutputStreamWriter out = new OutputStreamWriter(fos);
            temp.process(root, out);

            fos.flush();
            fos.close();
            out.flush();
            out.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TemplateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 產生 bo
     */
    private static void outputBoFile(Configuration cfg, String funNm, Map<String, TableSchemaSet> tables) {

        try {
            Template temp = cfg.getTemplate(tmpBo);
            String packageName = packageNamePrefix + moduleName + packageNamePostfixBo;

            // 準備資料 插入樣板
            Map<String, Object> root = new HashMap<String, Object>();
            root.put("boPackageName", packageName);
            root.put("tables", tables.entrySet());
            root.put("javabeanPackageName", packageNamePrefix + moduleName + packageNamePostfixJavaBean);
            root.put("genDate", dateNow.toString());
            root.put("functionName", funNm);

            // 輸出檔案
            File dir = new File(packagePathPrefix + parsePackageNameToFileOutputPath(packageName));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File outputFile = new File(dir.getAbsolutePath(), capFirstChar(funNm) + "Bo" + fileExtName);
            FileOutputStream fos = new FileOutputStream(outputFile);
            OutputStreamWriter out = new OutputStreamWriter(fos);
            temp.process(root, out);

            fos.flush();
            fos.close();
            out.flush();
            out.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TemplateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 產生 boImpl
     */
    private static void outputBoImplFile(Configuration cfg, String funNm, Map<String, TableSchemaSet> tables) {

        try {
            Template temp = cfg.getTemplate(tmpBoImpl);
            String packageName = packageNamePrefix + moduleName + packageNamePostfixBo;

            // 準備資料 插入樣板
            Map<String, Object> root = new HashMap<String, Object>();
            root.put("boPackageName", packageName);
            root.put("daoPackageName", packageNamePrefix + moduleName + packageNamePostfixDao);
            root.put("functionName", funNm);
            root.put("tables", tables.entrySet());
            root.put("javabeanPackageName", packageNamePrefix + moduleName + packageNamePostfixJavaBean);
            root.put("genDate", dateNow.toString());

            // 輸出檔案
            File dir = new File(packagePathPrefix + parsePackageNameToFileOutputPath(packageName));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File outputFile = new File(dir.getAbsolutePath(), capFirstChar(funNm) + "BoImpl" + fileExtName);
            FileOutputStream fos = new FileOutputStream(outputFile);
            OutputStreamWriter out = new OutputStreamWriter(fos);
            temp.process(root, out);

            fos.flush();
            fos.close();
            out.flush();
            out.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TemplateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 產生 dao
     */
    private static void outputDaoFile(Configuration cfg, String funNm, Map<String, TableSchemaSet> tables) {

        try {
            Template temp = cfg.getTemplate(tmpDao);
            String packageName = packageNamePrefix + moduleName + packageNamePostfixDao;

            // 準備資料 插入樣板
            Map<String, Object> root = new HashMap<String, Object>();
            root.put("daoPackageName", packageNamePrefix + moduleName + packageNamePostfixDao);
            root.put("tables", tables.entrySet());
            root.put("javabeanPackageName", packageNamePrefix + moduleName + packageNamePostfixJavaBean);
            root.put("genDate", dateNow.toString());
            root.put("functionName", funNm);

            // 輸出檔案
            File dir = new File(packagePathPrefix + parsePackageNameToFileOutputPath(packageName));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File outputFile = new File(dir.getAbsolutePath(), capFirstChar(funNm) + "Dao" + fileExtName);
            FileOutputStream fos = new FileOutputStream(outputFile);
            OutputStreamWriter out = new OutputStreamWriter(fos);
            temp.process(root, out);

            fos.flush();
            fos.close();
            out.flush();
            out.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TemplateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 產生 daoImpl
     */
    private static void outputDaoImplFile(Configuration cfg, String funNm, Map<String, TableSchemaSet> tables) {

        try {
            Template temp = cfg.getTemplate(tmpDaoImpl);
            String packageName = packageNamePrefix + moduleName + packageNamePostfixDao;

            // 準備資料 插入樣板
            Map<String, Object> root = new HashMap<String, Object>();
            root.put("daoPackageName", packageNamePrefix + moduleName + packageNamePostfixDao);
            root.put("javabeanPackageName", packageNamePrefix + moduleName + packageNamePostfixJavaBean);
            root.put("genDate", dateNow.toString());
            root.put("functionName", funNm);
            root.put("moduleName", moduleName);
            root.put("tables", transMultiMapToOne(tables).entrySet());

            // 輸出檔案
            File dir = new File(packagePathPrefix + parsePackageNameToFileOutputPath(packageName));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File outputFile = new File(dir.getAbsolutePath(), capFirstChar(funNm) + "DaoImpl" + fileExtName);
            FileOutputStream fos = new FileOutputStream(outputFile);
            OutputStreamWriter out = new OutputStreamWriter(fos);
            temp.process(root, out);

            fos.flush();
            fos.close();
            out.flush();
            out.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TemplateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 產生 sqlite open helper
     */
    private static void outputSqliteOpenHelperFile(Configuration cfg) {

        try {
            Template temp = cfg.getTemplate(tmpDatabaseHelper);
            String packageName = packageNamePrefix + moduleName + packageNamePostfixDao;

            // 準備資料 插入樣板
            Map<String, Object> root = new HashMap<String, Object>();
            root.put("daoPackageName", packageNamePrefix + moduleName + packageNamePostfixDao);
            root.put("utilPackageName", packageNamePrefix + moduleName + packageNamePostfixUtil);
            root.put("genDate", dateNow.toString());
            root.put("moduleName", moduleName);

            // 輸出檔案
            File dir = new File(packagePathPrefix + parsePackageNameToFileOutputPath(packageName));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File outputFile = new File(dir.getAbsolutePath(), capFirstChar(moduleName) + "DatabaseHelper" + fileExtName);
            FileOutputStream fos = new FileOutputStream(outputFile);
            OutputStreamWriter out = new OutputStreamWriter(fos);
            temp.process(root, out);

            fos.flush();
            fos.close();
            out.flush();
            out.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TemplateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 解析套件名稱為檔案書出路徑
     */
    private static String parsePackageNameToFileOutputPath(String packageName) {
        String[] packageNamePiece = packageName.split("\\.");

        StringBuffer packagePath = new StringBuffer();
        for (String piece : packageNamePiece) {
            packagePath.append(piece).append("\\");
        }
        return packagePath.toString();
    }

    /**
     * 字串開頭第一個字轉大寫
     */
    private static String capFirstChar(String capString) {
        return capString.substring(0, 1).toUpperCase() + capString.substring(1, capString.length());
    }

    /**
     * tables 特別處理 因為freemarker不能顯示參考資料型別內的資料
     */
    private static Map<String, String> transMultiMapToOne(Map<String, TableSchemaSet> tables) {
        Map<String, String> result = new HashMap<String, String>();

        for (Entry<String, TableSchemaSet> table : tables.entrySet()) {
            String tableName = table.getKey();
            Map<String, String> fieldDataTypeMap = table.getValue().getFieldDataTypeMap();

            StringBuffer fieldNameAndDataType = new StringBuffer();

            for (Entry<String, String> fieldDataTypeEntry : fieldDataTypeMap.entrySet()) {
                fieldNameAndDataType.append(fieldDataTypeEntry.getKey()).append("_");
                fieldNameAndDataType.append(fieldDataTypeEntry.getValue()).append("_");
            }
            String strFieldNameAndDataType = fieldNameAndDataType.toString();
            result.put(tableName, strFieldNameAndDataType.substring(0, strFieldNameAndDataType.length() - 1));
        }
        return result;
    }

    /**
     * 打包公用程式
     */
    private static void moveUtilFile() {

        String utilPackageName = packageNamePrefix + moduleName + packageNamePostfixUtil;
        File dir = new File(packagePathPrefix + parsePackageNameToFileOutputPath(utilPackageName));
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String[] fileNames = {GenJavaCodeAndSqliteCreateTables_Itinerary.class.getSimpleName(), TableSchemaSet.class.getSimpleName(), TableSchemaSetSpec.class.getSimpleName()};

        for (String fileName : fileNames) {
            fileIO(dir, fileName);
        }
    }

    private static void fileIO(File dir, String fileName) {

        File originFile = new File(thisFilePath + fileName + fileExtName);
        File targetFile = new File(dir.getAbsolutePath(), fileName + fileExtName);
        try {
            FileInputStream fileInputStream = new FileInputStream(originFile);
            FileOutputStream fileOutputStream = new FileOutputStream(targetFile);

            byte[] buffer = new byte[1024 * 1024];
            int len = 0;

            while ((len = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }

            fileInputStream.close();
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
