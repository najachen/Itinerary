package silver.reminder.itinerary.bo;

import android.database.Cursor;

import java.util.List;

import silver.reminder.itinerary.model.Note;
import silver.reminder.itinerary.model.Task;
import silver.reminder.itinerary.model.Shopping;
/**
 * Mon Sep 05 23:52:20 CST 2016 by freemarker template
 */
public interface ItineraryBo {

    long createNote(Note note);

    void createNoteList(List<Note> noteList);

    int modifyNote(Note note);

    void modifyNoteList(List<Note> noteList);

    void removeNote(Integer _id);

    void removeNoteList(List<Integer> noteIdList);

    Note findNoteById(Integer _id);

    Cursor findNoteList(Note note);

    long createTask(Task task);

    void createTaskList(List<Task> taskList);

    int modifyTask(Task task);

    void modifyTaskList(List<Task> taskList);

    void removeTask(Integer _id);

    void removeTaskList(List<Integer> taskIdList);

    Task findTaskById(Integer _id);

    Cursor findTaskList(Task task);

    long createShopping(Shopping shopping);

    void createShoppingList(List<Shopping> shoppingList);

    int modifyShopping(Shopping shopping);

    void modifyShoppingList(List<Shopping> shoppingList);

    void removeShopping(Integer _id);

    void removeShoppingList(List<Integer> shoppingIdList);

    Shopping findShoppingById(Integer _id);

    Cursor findShoppingList(Shopping shopping);

}
