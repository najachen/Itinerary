package silver.reminder.itinerary;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/8/23.
 */
public class ListSoundFileActivity extends AppCompatActivity {
    int page = 0;   //設定一開始頁數
    int count = 0 ; //計算頁數，未完成如何計算更換soundDataX

    String [] soundData1 = {"sound1", "sound2", "sound3", "sound4"};
    String [] soundData2 = {"sound5", "sound6", "sound7", "sound8"};
    String [] soundData3 = {"sound9", "soundA", "soundB", "soundC"};
    private Button backpage;
    private ListView soundfilelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_sound_file_activity);

        FloatingActionButton fabright = (FloatingActionButton) findViewById(R.id.fabRight);
        FloatingActionButton fableft = (FloatingActionButton) findViewById(R.id.fabLeft);

        backpage = (Button) findViewById(R.id.backPage);
        soundfilelist = (ListView) findViewById(R.id.soundFileList);

        soundfilelist.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, soundData1));
        soundfilelist.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, soundData2));
        soundfilelist.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, soundData3));

        backpage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); ;
            }
        });

        fableft.setOnClickListener(new FloatingActionButton.OnClickListener() {    //清單下一頁
            @Override
            public void onClick(View view) {
                page--; //按上一頁到第一張時，繼續在按，未完成 Listview string陣列 ，參考吳老師的CH6圖片相關介面，更換圖片是更換陣列裡的檔案名稱???
                if (page < 0) ;
                page = soundData2.length - 1;
//                Intent intent = new Intent(ListSoundFileActivity.this, ListSoundFileContent.class);
//                startActivity(intent);
            }
        });
        fabright.setOnClickListener(new FloatingActionButton.OnClickListener() { //清單下一頁
            @Override
            public void onClick(View view) {
                page++;
                if (page == count) {
                    page = 0;
                }
            }
        });
    }





}
