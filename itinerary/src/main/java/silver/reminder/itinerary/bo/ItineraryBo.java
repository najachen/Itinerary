package silver.reminder.itinerary.bo;

import android.database.Cursor;

import java.util.List;

import silver.reminder.itinerary.javabean.Note;
import silver.reminder.itinerary.javabean.Task;
import silver.reminder.itinerary.javabean.Shopping;
/**
 * Sun Aug 28 22:11:47 CST 2016 by freemarker template
 */
public interface ItineraryBo {

    long createNote(Note note);

    void createNoteList(List<Note> noteList);

    int modifyNote(Note note);

    void modifyNoteList(List<Note> noteList);

    void removeNote(Integer id);

    void removeNoteList(List<Integer> noteIdList);

    Note findNoteById(Integer id);

    Cursor findNoteList(Note note);
    
    long createTask(Task task);

    void createTaskList(List<Task> taskList);

    int modifyTask(Task task);

    void modifyTaskList(List<Task> taskList);

    void removeTask(Integer id);

    void removeTaskList(List<Integer> taskIdList);

    Task findTaskById(Integer id);

    Cursor findTaskList(Task task);
    
    long createShopping(Shopping shopping);

    void createShoppingList(List<Shopping> shoppingList);

    int modifyShopping(Shopping shopping);

    void modifyShoppingList(List<Shopping> shoppingList);

    void removeShopping(Integer id);

    void removeShoppingList(List<Integer> shoppingIdList);

    Shopping findShoppingById(Integer id);

    Cursor findShoppingList(Shopping shopping);
    
}
