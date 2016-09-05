package silver.reminder.itinerary.dao;

import android.database.Cursor;

import java.util.List;

import silver.reminder.itinerary.model.Note;
import silver.reminder.itinerary.model.Task;
import silver.reminder.itinerary.model.Shopping;
/**
 * Mon Sep 05 23:52:20 CST 2016 by freemarker template
 */
public interface ItineraryDao {

    long insertNote(Note note);

    void insertNoteList(List<Note> noteList);

    int updateNote(Note note);

    void updateNoteList(List<Note> noteList);

    void deleteNote(Integer _id);

    void deleteNoteList(List<Integer> noteIdList);

    Note selectNoteById(Integer _id);

    Cursor selectNoteList(Note note);

    long insertTask(Task task);

    void insertTaskList(List<Task> taskList);

    int updateTask(Task task);

    void updateTaskList(List<Task> taskList);

    void deleteTask(Integer _id);

    void deleteTaskList(List<Integer> taskIdList);

    Task selectTaskById(Integer _id);

    Cursor selectTaskList(Task task);

    long insertShopping(Shopping shopping);

    void insertShoppingList(List<Shopping> shoppingList);

    int updateShopping(Shopping shopping);

    void updateShoppingList(List<Shopping> shoppingList);

    void deleteShopping(Integer _id);

    void deleteShoppingList(List<Integer> shoppingIdList);

    Shopping selectShoppingById(Integer _id);

    Cursor selectShoppingList(Shopping shopping);

}
