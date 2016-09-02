package silver.reminder.itinerary;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/8/23.
 */
public class ListSoundFileActivity extends AppCompatActivity {

    private Button backCreateDingDong;
    private ListView soundFileList;
    private FloatingActionButton nextPageSoundFile;
    private FloatingActionButton prePageSoundFile;

    /*
        分頁相關
     */
    private static final int PAGE_SIZE = 5;
    private int currentPage = 1;

    /*
        table name and field name
     */
    private static final String SOUND_FILE_TABLE_NAME = "soundFile";
    private static final String SOUND_FILE_FIELD_ID = "id";
    private static final String SOUND_FILE_FIELD_FILE_NAME = "fileName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_sound_file_activity);

        findViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        listSoundFile(null);
    }

    private void findViews() {
        nextPageSoundFile = (FloatingActionButton) findViewById(R.id.nextPageSoundFile);
        nextPageSoundFile.setOnClickListener(this::nextPageSoundFile);
        prePageSoundFile = (FloatingActionButton) findViewById(R.id.prePageSoundFile);
        prePageSoundFile.setOnClickListener(this::prePageSoundFile);
        backCreateDingDong = (Button) findViewById(R.id.backCreateDingDong);
        backCreateDingDong.setOnClickListener(this::backCreateDingDong);

        soundFileList = (ListView) findViewById(R.id.soundFileList);
        soundFileList.setOnItemClickListener(this::onSoundFileClick);
    }

    /**
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    private void onSoundFileClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView soundFileId = (TextView) view.findViewById(R.id.soundFileId);
        TextView soundFileName = (TextView) view.findViewById(R.id.soundFileName);

        Toast.makeText(this, "您選的是 " + soundFileName.getText().toString(), Toast.LENGTH_LONG).show();

        Intent intent = getIntent();
        intent.putExtra(GlobalNaming.SOUND_FILE_ID, soundFileId.getText().toString());
        intent.putExtra(GlobalNaming.SOUND_FILE_NAME, soundFileName.getText().toString());
        setResult(RESULT_OK, intent);

        this.backCreateDingDong(view);
    }

    /**
     * @param view
     */
    private void backCreateDingDong(View view) {
        finish();
    }

    /**
     * @param view
     */
    private void prePageSoundFile(View view) {
        listSoundFile(Pager.GO_BACKWARD);
    }

    /**
     * @param view
     */
    private void nextPageSoundFile(View view) {
        listSoundFile(Pager.GO_FORWARD);
    }

    private void listSoundFile(Boolean isGoForward) {

        Pager pager = new Pager(this, PAGE_SIZE);
        Cursor cursor = pager.getPagedCursorBySearchingCondition(SOUND_FILE_TABLE_NAME
                , SOUND_FILE_FIELD_ID
                , String.valueOf(0)
                , String.valueOf(Integer.MAX_VALUE)
                , this.currentPage
                , isGoForward);
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this
                , R.layout.embedding_sound_file_list_item
                , cursor
                , new String[]{SOUND_FILE_FIELD_ID, SOUND_FILE_FIELD_FILE_NAME}
                , new int[]{R.id.soundFileId, R.id.soundFileName}
                , 0);
        soundFileList.setAdapter(simpleCursorAdapter);
    }
}
