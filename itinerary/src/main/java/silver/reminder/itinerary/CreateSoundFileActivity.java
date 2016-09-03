package silver.reminder.itinerary;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioEncoder;
import android.media.MediaRecorder.AudioSource;
import android.media.MediaRecorder.OutputFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import silver.reminder.itinerary.bo.SoundDingDongBo;
import silver.reminder.itinerary.bo.SoundDingDongBoImpl;
import silver.reminder.itinerary.model.SoundFile;

/**
 * Created by Administrator on 2016/8/22.
 */
public class CreateSoundFileActivity extends AppCompatActivity {

    private Button cancelRecord;
    private Button soundFileList;
    private Button saveSoundFile;
    private ImageButton startOrStopRecord;
    private EditText soundFilePath;
    private TextView recordDuration;
    private TextView isRecording;

    /**
     * 銀級小幫手 專用音樂資料夾名稱
     */
    static final String DIR_SILVER_REMINDER = "/silverReminderMedia/";
    static final String DIR_SILVER_REMINDER_TEMP = "temp/";
    static final String DIR_SILVER_REMINDER_STORE = "store/";

    private static final String MEDIA_FILE_NAME_EXT = ".mp3";

    /**
     * 錄音機物件
     */
    private MediaRecorder mediaRecorder;

    /*
        多媒體檔案輸出資料夾
     */
    private File dirTemp;
    private File dirStore;

    /**
     * 是否正在錄音
     */
    private boolean isRecordingNow;

    /*
        bo
     */
    private SoundDingDongBo soundDingDongBo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_sound_file);

        findViews();

        /*
            音樂檔 輸出資料夾
         */
        File dirMusic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);

        //暫存資料夾
        dirTemp = new File(dirMusic, DIR_SILVER_REMINDER + DIR_SILVER_REMINDER_TEMP);
        if (!dirTemp.exists()) {
            dirTemp.mkdirs();
        }
        //正式資料夾
        dirStore = new File(dirMusic, DIR_SILVER_REMINDER + DIR_SILVER_REMINDER_STORE);
        if (!dirStore.exists()) {
            dirStore.mkdirs();
        }

        //bo
        soundDingDongBo = SoundDingDongBoImpl.getInstance(this);

        /*
            是否正在錄音??
         */
        isRecordingNow = false;
    }

    private void findViews() {
        cancelRecord = (Button) findViewById(R.id.cancelRecord);
        cancelRecord.setOnClickListener(this::cancelRecord);
        soundFileList = (Button) findViewById(R.id.soundFileList);
        soundFileList.setOnClickListener(this::soundFileList);
        saveSoundFile = (Button) findViewById(R.id.saveSoundFile);
        saveSoundFile.setOnClickListener(this::saveSoundFile);
        startOrStopRecord = (ImageButton) findViewById(R.id.startOrStopRecord);
        startOrStopRecord.setOnClickListener(this::startOrStopRecord);

        soundFilePath = (EditText) findViewById(R.id.soundFilePath);

        recordDuration = (TextView) findViewById(R.id.recordDuration);
        isRecording = (TextView) findViewById(R.id.isRecording);
    }

    /**
     * 開始或停止錄音
     *
     * @param view
     */
    private void startOrStopRecord(View view) {

        if (!isRecordingNow) {

            //檔名處理
            String mediaFileName = GlobalNaming.getTmString(Calendar.getInstance());
            String outputFilePath = new File(dirTemp, mediaFileName + MEDIA_FILE_NAME_EXT).getAbsolutePath();

            //MediaRecorder 前置準備
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(AudioSource.MIC);
            mediaRecorder.setOutputFormat(OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(outputFilePath);

            //開始
            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaRecorder.start();

            //顯示"錄音中..."字樣
            isRecording.setVisibility(View.VISIBLE);

        } else {

            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;

            //隱藏"錄音中..."字樣
            isRecording.setVisibility(View.INVISIBLE);

            //顯示錄音長度
            File theLatestFile = findTheLatestFileInTempDir();
            Uri theLatestFileUri = Uri.parse(theLatestFile.getAbsolutePath());
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(this, theLatestFileUri);
            String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

            long durationLong = Long.parseLong(duration);
            Date dateDuration = new Date(durationLong);

            SimpleDateFormat formatter = new SimpleDateFormat("mm 分 ss 秒");
            recordDuration.setText(formatter.format(dateDuration));
        }
    }

    /**
     * 儲存音效檔
     *
     * @param view
     */
    private void saveSoundFile(View view) {

        //檢查檔名欄位
        String fileNameSave = soundFilePath.getText().toString();

        if (fileNameSave == null || fileNameSave.length() == 0) {
            Snackbar.make(view, "檔案名稱未輸入!!", Snackbar.LENGTH_SHORT).show();
            return;
        }

        //檔案主檔名
        int fileNameLen = fileNameSave.length();
        int extFileNameLen = MEDIA_FILE_NAME_EXT.length();

        int pointIndex = fileNameSave.indexOf(".");
        if (pointIndex == fileNameLen - extFileNameLen && fileNameSave.endsWith(MEDIA_FILE_NAME_EXT)) {
            fileNameSave = fileNameSave.substring(0, fileNameLen - extFileNameLen);
        }

        //檢查正式資料夾 有無相同檔名 若無則搬檔案到正式資料夾
        String[] storeFileNameArray = dirStore.list();
        for (int i = 0; i < storeFileNameArray.length; i++) {
            storeFileNameArray[i] = storeFileNameArray[i].substring(0, extFileNameLen);
        }
        List<String> storeFileNameList = Arrays.asList(storeFileNameArray);

        if (storeFileNameList.contains(fileNameSave)) {
            Snackbar.make(view, "已存在相同檔名的音效檔, 請更改檔名!!", Snackbar.LENGTH_SHORT).show();
            return;
        } else {
            findTheLatestFileInTempDir().renameTo(new File(dirStore, fileNameSave + MEDIA_FILE_NAME_EXT));
        }

        //清空暫存區所有資料
        for(String tempFileName:dirTemp.list()){
            new File(dirTemp, tempFileName).delete();
        }

        //資料庫存檔
        SoundFile soundFileSave = new SoundFile();
        soundFileSave.setFileName(fileNameSave + MEDIA_FILE_NAME_EXT);
        long rowId = soundDingDongBo.createSoundFile(soundFileSave);

        finish();
    }

    /**
     * 進入音效清單頁
     *
     * @param view
     */
    private void soundFileList(View view) {
        Intent intent = new Intent(this, ListSoundFileActivity.class);
        startActivity(intent);
    }

    /**
     * 取消錄音
     *
     * @param view
     */
    private void cancelRecord(View view) {
        finish();
    }

    /**
     * 從暫存的資料夾中 找出最新的檔案
     *
     * @return
     */
    private File findTheLatestFileInTempDir() {
        String[] fileNameArray = dirTemp.list();

        for (int i = 0; i < fileNameArray.length; i++) {
            fileNameArray[i] = fileNameArray[i].replace(MEDIA_FILE_NAME_EXT, "");
        }
        List<String> fileNameList = Arrays.asList(fileNameArray);
        String theLatestFileName = Collections.max(fileNameList);

        return new File(dirTemp, theLatestFileName + MEDIA_FILE_NAME_EXT);
    }
}
