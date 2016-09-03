package silver.reminder.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import silver.reminder.itinerary.ListTaskItemActivity.MyViewHolderAdapter;
import silver.reminder.itinerary.bo.ItineraryBo;
import silver.reminder.itinerary.bo.ItineraryBoImpl;
import silver.reminder.itinerary.model.Note;
import silver.reminder.itinerary.model.Shopping;

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
    private TextView noteContentLabel;
    private TextView noteExplainLabel;
    private TextView shoppingItemTitle;
    private TextView shoppingNameLabel;
    private TextView quantityLabel;
    private TextView unitPriceLabel;

    /*
        頁面物件
     */
    private int taskId;
    private Note note;
    private Shopping shopping;

    /*
        bo
     */
    private ItineraryBo itineraryBo;

    /*
        統一管理頁面元件
     */
    private List<View> noteViewList;
    private List<View> shoppingViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_or_edit_task_detail_list_item);

        findViews();

        itineraryBo = ItineraryBoImpl.getInstance(this);

        noteViewList = new ArrayList<View>();
        shoppingViewList = new ArrayList<View>();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
            準備頁面物件
         */
        //新增 帶 taskId 過來
        //編輯 帶 itemId viewType過來
        Intent intent = getIntent();

        taskId = intent.getIntExtra(GlobalNaming.TASK_ID, GlobalNaming.ERROR_CODE);

        int taskItemId = intent.getIntExtra(GlobalNaming.TASK_ITEM_ID, GlobalNaming.ERROR_CODE);
        int taskItemViewType = intent.getIntExtra(GlobalNaming.TASK_ITEM_VIEW_TYPE, GlobalNaming.ERROR_CODE);

        if (taskItemViewType == MyViewHolderAdapter.VIEW_NOTE) {
            note = itineraryBo.findNoteById(taskItemId);
            shopping = null;
        } else if (taskItemViewType == MyViewHolderAdapter.VIEW_SHOPPING) {
            shopping = itineraryBo.findShoppingById(taskItemId);
            note = null;
        }

        /*
            顯示內容
         */
        //先決定要不要顯示底下的切換鈕
        if (taskId == 0 && taskItemId != 0 && taskItemViewType != 0) { //修改
            switchFunctionShopping.setVisibility(View.GONE);
            switchFunctionNote.setVisibility(View.GONE);
        } else { //新增
            switchFunctionShopping.setVisibility(View.VISIBLE);
            switchFunctionNote.setVisibility(View.VISIBLE);
        }
        //切換其他欄位與label
        if (taskItemViewType == MyViewHolderAdapter.VIEW_NOTE) {
            switchView(shoppingViewList, noteViewList);
        } else if (taskItemViewType == MyViewHolderAdapter.VIEW_SHOPPING) {
            switchView(noteViewList, shoppingViewList);
        } else if (taskId != 0 && taskItemId == 0 && taskItemViewType == 0) {
            switchView(null, noteViewList);
            switchView(null, shoppingViewList);
        }
        //填值
        if (taskItemViewType == MyViewHolderAdapter.VIEW_NOTE) {
            noteContent.setText(note.getNoteContent());
            noteExplain.setText(note.getNoteExplain());
        } else if (taskItemViewType == MyViewHolderAdapter.VIEW_SHOPPING) {
            name.setText(shopping.getName());
            quantity.setText(shopping.getQuantity());
            unitPrice.setText(shopping.getUnitPrice().toString());
        }
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

        /*
            購物
         */
        shoppingItemTitle = (TextView) findViewById(R.id.shoppingItemTitle);
        shoppingNameLabel = (TextView) findViewById(R.id.shoppingNameLabel);
        quantityLabel = (TextView) findViewById(R.id.quantityLabel);
        unitPriceLabel = (TextView) findViewById(R.id.unitPriceLabel);

        name = (EditText) findViewById(R.id.name);
        quantity = (EditText) findViewById(R.id.quantity);
        unitPrice = (EditText) findViewById(R.id.unitPrice);

        //集中管理
        shoppingViewList.add(shoppingItemTitle);
        shoppingViewList.add(shoppingNameLabel);
        shoppingViewList.add(quantityLabel);
        shoppingViewList.add(unitPriceLabel);
        shoppingViewList.add(name);
        shoppingViewList.add(quantity);
        shoppingViewList.add(unitPrice);

        /*
            備忘
         */
        noteContentLabel = (TextView) findViewById(R.id.noteContentLabel);
        noteExplainLabel = (TextView) findViewById(R.id.noteExplainLabel);

        noteExplain = (EditText) findViewById(R.id.noteExplain);
        noteContent = (EditText) findViewById(R.id.noteContent);

        //集中管理
        noteViewList.add(noteContentLabel);
        noteViewList.add(noteExplainLabel);
        noteViewList.add(noteExplain);
        noteViewList.add(noteContent);
    }

    /**
     * 切換成編輯購物清單
     *
     * @param view
     */
    private void switchFunctionShopping(View view) {
        switchView(noteViewList, shoppingViewList);
    }

    /**
     * 切換成編輯備忘錄
     *
     * @param view
     */
    private void switchFunctionNote(View view) {
        switchView(shoppingViewList, noteViewList);
    }

    /**
     * 新增或更新(存檔)
     *
     * @param view
     */
    private void createOrUpdateTaskItem(View view) {

        if (taskId != 0 && note == null && shopping == null) { //新增

            if (isAllViewsEnable(noteViewList)) { //note使用中

                Note noteSave = new Note();
                noteSave.setTaskId(taskId);
                noteSave.setNoteContent(noteContent.getText().toString());
                noteSave.setNoteExplain(noteExplain.getText().toString());

                long rowId = itineraryBo.createNote(noteSave);

            } else if (isAllViewsEnable(shoppingViewList)) { //shopping使用中

                Shopping shoppingSave = new Shopping();
                shoppingSave.setTaskId(taskId);
                shoppingSave.setName(name.getText().toString());
                shoppingSave.setQuantity(Integer.parseInt(quantity.getText().toString()));
                shoppingSave.setUnitPrice(Float.parseFloat(unitPrice.getText().toString()));

                long rowId = itineraryBo.createShopping(shoppingSave);
            }

        } else if (taskId == 0 && (note != null || shopping != null)) { //修改

            if(note!=null){
                note.setNoteContent(noteContent.getText().toString());
                note.setNoteExplain(noteExplain.getText().toString());

                int modRowCount = itineraryBo.modifyNote(note);
            }else if(shopping!=null){

                shopping.setName(name.getText().toString());
                shopping.setQuantity(Integer.parseInt(quantity.getText().toString()));
                shopping.setUnitPrice(Float.parseFloat(unitPrice.getText().toString()));

                int modRowCount = itineraryBo.modifyShopping(shopping);
            }
        }

        //存檔完成 返回
        finish();
    }

    /**
     * 取消編輯
     *
     * @param view
     */
    private void cancelEditTaskItem(View view) {
        finish();
    }

    /**
     * 依 viewType 切換畫面元件
     *
     * @param viewListToDisable 要消失的頁面物件清單
     * @param viewListToEnable  要顯示的頁面物件清單
     */
    private void switchView(List<View> viewListToDisable, List<View> viewListToEnable) {

        //容許空list
        viewListToDisable = viewListToDisable == null ? new ArrayList<View>() : viewListToDisable;
        viewListToEnable = viewListToEnable == null ? new ArrayList<View>() : viewListToEnable;

        for (View view : viewListToDisable) {
            if (view instanceof EditText) {
                ((EditText) view).setText(GlobalNaming.SPACE);
            }
            view.setVisibility(View.GONE);
            view.setFocusable(false);
            view.setEnabled(false);
        }

        for (View view : viewListToEnable) {
            view.setVisibility(View.VISIBLE);
            view.setFocusable(true);
            view.setEnabled(true);
        }
    }

    /**
     * 判斷View清單中的元件是否都在使用中
     *
     * @param viewList
     * @return
     */
    private boolean isAllViewsEnable(List<View> viewList) {
        boolean isAllVisible = false;
        boolean isAllFocusable = false;
        boolean isAllEnable = false;

        for (View view : viewList) {
            isAllVisible &= view.getVisibility() == View.VISIBLE;
            isAllFocusable &= view.isFocusable();
            isAllEnable &= view.isEnabled();
        }

        return isAllVisible & isAllFocusable & isAllEnable;
    }
}
