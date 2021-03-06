package silver.reminder.itinerary.bo;

import android.content.Context;
import android.database.Cursor;

import java.util.List;

import silver.reminder.itinerary.dao.ItineraryDao;
import silver.reminder.itinerary.dao.ItineraryDaoImpl;
import silver.reminder.itinerary.model.Note;
import silver.reminder.itinerary.model.Task;
import silver.reminder.itinerary.model.Shopping;
/**
 * Mon Sep 05 23:52:20 CST 2016 by freemarker template
 */
public class ItineraryBoImpl implements ItineraryBo {

    /**
     * 獨體模式
     */
    private static ItineraryBo itineraryBo;

    private ItineraryBoImpl(Context context){
        itineraryDao = ItineraryDaoImpl.getInstance(context);
    }

    public static ItineraryBo getInstance(Context context){
        if(itineraryBo == null){
            itineraryBo = new ItineraryBoImpl(context);
        }
        return itineraryBo;
    }

    private ItineraryDao itineraryDao;

    /*
        方法寫在下面 ======================================================
     */

    @Override
    public long createNote(Note note) {
        long rowId = this.itineraryDao.insertNote(note);
        return rowId;
    }

    @Override
    public void createNoteList(List<Note> noteList) {
        this.itineraryDao.insertNoteList(noteList);
    }

    @Override
    public int modifyNote(Note note) {
        int modifyDataAmount = this.itineraryDao.updateNote(note);
        return modifyDataAmount;
    }

    @Override
    public void modifyNoteList(List<Note> noteList) {
        this.itineraryDao.updateNoteList(noteList);
    }

    @Override
    public void removeNote(Integer _id) {
        this.itineraryDao.deleteNote(_id);
    }

    @Override
    public void removeNoteList(List<Integer> noteIdList) {
        this.itineraryDao.deleteNoteList(noteIdList);
    }

    @Override
    public Note findNoteById(Integer _id) {
        Note result = this.itineraryDao.selectNoteById(_id);
        return result;
    }

    @Override
    public Cursor findNoteList(Note note) {
        Cursor result = this.itineraryDao.selectNoteList(note);
        return result;
    }

    @Override
    public long createTask(Task task) {
        long rowId = this.itineraryDao.insertTask(task);
        return rowId;
    }

    @Override
    public void createTaskList(List<Task> taskList) {
        this.itineraryDao.insertTaskList(taskList);
    }

    @Override
    public int modifyTask(Task task) {
        int modifyDataAmount = this.itineraryDao.updateTask(task);
        return modifyDataAmount;
    }

    @Override
    public void modifyTaskList(List<Task> taskList) {
        this.itineraryDao.updateTaskList(taskList);
    }

    @Override
    public void removeTask(Integer _id) {
        this.itineraryDao.deleteTask(_id);
    }

    @Override
    public void removeTaskList(List<Integer> taskIdList) {
        this.itineraryDao.deleteTaskList(taskIdList);
    }

    @Override
    public Task findTaskById(Integer _id) {
        Task result = this.itineraryDao.selectTaskById(_id);
        return result;
    }

    @Override
    public Cursor findTaskList(Task task) {
        Cursor result = this.itineraryDao.selectTaskList(task);
        return result;
    }

    @Override
    public long createShopping(Shopping shopping) {
        long rowId = this.itineraryDao.insertShopping(shopping);
        return rowId;
    }

    @Override
    public void createShoppingList(List<Shopping> shoppingList) {
        this.itineraryDao.insertShoppingList(shoppingList);
    }

    @Override
    public int modifyShopping(Shopping shopping) {
        int modifyDataAmount = this.itineraryDao.updateShopping(shopping);
        return modifyDataAmount;
    }

    @Override
    public void modifyShoppingList(List<Shopping> shoppingList) {
        this.itineraryDao.updateShoppingList(shoppingList);
    }

    @Override
    public void removeShopping(Integer _id) {
        this.itineraryDao.deleteShopping(_id);
    }

    @Override
    public void removeShoppingList(List<Integer> shoppingIdList) {
        this.itineraryDao.deleteShoppingList(shoppingIdList);
    }

    @Override
    public Shopping findShoppingById(Integer _id) {
        Shopping result = this.itineraryDao.selectShoppingById(_id);
        return result;
    }

    @Override
    public Cursor findShoppingList(Shopping shopping) {
        Cursor result = this.itineraryDao.selectShoppingList(shopping);
        return result;
    }

}
