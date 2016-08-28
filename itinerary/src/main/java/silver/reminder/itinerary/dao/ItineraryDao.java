package silver.reminder.itinerary.dao;

import android.database.Cursor;

import java.util.List;

import silver.reminder.itinerary.javabean.Note;
import silver.reminder.itinerary.javabean.Task;
import silver.reminder.itinerary.javabean.Shopping;
/**
 * Sun Aug 28 22:11:47 CST 2016 by freemarker template
 */
public interface ItineraryDao {

    long insertNote(Note note);

    void insertNoteList(List<Note> noteList);

    int updateNote(Note note);

    void updateNoteList(List<Note> noteList);

    void deleteNote(Integer id);

    void deleteNoteList(List<Integer> noteIdList);

    Note selectNoteById(Integer id);

    Cursor selectNoteList(Note note);
    
    long insertTask(Task task);

    void insertTaskList(List<Task> taskList);

    int updateTask(Task task);

    void updateTaskList(List<Task> taskList);

    void deleteTask(Integer id);

    void deleteTaskList(List<Integer> taskIdList);

    Task selectTaskById(Integer id);

    Cursor selectTaskList(Task task);
    
    long insertShopping(Shopping shopping);

    void insertShoppingList(List<Shopping> shoppingList);

    int updateShopping(Shopping shopping);

    void updateShoppingList(List<Shopping> shoppingList);

    void deleteShopping(Integer id);

    void deleteShoppingList(List<Integer> shoppingIdList);

    Shopping selectShoppingById(Integer id);

    Cursor selectShoppingList(Shopping shopping);
    
}
