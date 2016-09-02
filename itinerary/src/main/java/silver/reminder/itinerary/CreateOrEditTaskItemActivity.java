package silver.reminder.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateOrEditTaskItemActivity extends AppCompatActivity {

    private Button cancelEditTaskItem;
    private Button createOrUpdateTaskItem;
    private Button switchFunctionNote;
    private Button switchFunctionShopping;
    private EditText noteExplain;
    private EditText noteContent;
    private EditText name;
    private EditText quantity;
    private EditText unitPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_or_edit_task_detail_list_item);

        findViews();

        Intent intent = getIntent();
        int taskItemId = intent.getIntExtra(GlobalNaming.TASK_ITEM_ID, GlobalNaming.ERROR_CODE);
        int taskItemViewType = intent.getIntExtra(GlobalNaming.TASK_ITEM_VIEW_TYPE, GlobalNaming.ERROR_CODE);


    }

    private void findViews() {
        cancelEditTaskItem = (Button) findViewById(R.id.cancelEditTaskItem);
        cancelEditTaskItem.setOnClickListener(this::cancelEditTaskItem);
        createOrUpdateTaskItem = (Button) findViewById(R.id.createOrUpdateTaskItem);
        createOrUpdateTaskItem.setOnClickListener(this::createOrUpdateTaskItem);
        switchFunctionNote = (Button) findViewById(R.id.switchFunctionNote);
        switchFunctionNote.setOnClickListener(this::switchFunctionNote);
        switchFunctionShopping = (Button) findViewById(R.id.switchFunctionShopping);
        switchFunctionShopping.setOnClickListener(this::switchFunctionShopping);

        noteExplain = (EditText) findViewById(R.id.noteExplain);
        noteContent = (EditText) findViewById(R.id.noteContent);
        name = (EditText) findViewById(R.id.name);
        quantity = (EditText) findViewById(R.id.quantity);
        unitPrice = (EditText) findViewById(R.id.unitPrice);
    }

    /**
     * 切換成編輯購物清單
     *
     * @param view
     */
    private void switchFunctionShopping(View view) {

    }

    /**
     * 切換成編輯備忘錄
     *
     * @param view
     */
    private void switchFunctionNote(View view) {


    }

    /**
     * 新增或編輯
     *
     * @param view
     */
    private void createOrUpdateTaskItem(View view) {


    }

    /**
     * 取消編輯
     *
     * @param view
     */
    private void cancelEditTaskItem(View view) {
        finish();
    }
}
