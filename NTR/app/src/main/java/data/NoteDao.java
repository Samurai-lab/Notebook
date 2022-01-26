/** Обьект, через который осуществляется доступ к таблице базы данных (запись, чтение, выборка и тому подобное)
Описываем интерфейс
 *
 *
 *
 **/
package data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import model.Note;

@Dao
public interface NoteDao {
    //Запрос, который возвращает список
    @Query("SELECT * FROM Note")
    List<Note> getAll();
    //Обьект на который будет подписываться пользовательский интерфейс (если что-то меняется, то LiveData будет сообщать, что данные обновились)
    @Query("SELECT * FROM Note")
    LiveData<List<Note>> getAllLiveData();
    //Загрузить все заметки по id из списка(с автоподстановкой (:userIds))
    @Query("SELECT * FROM Note WHERE uid IN (:userIds)")
    List<Note> loadAllByIds(int[] userIds);
    //Выборка по id
    @Query("SELECT * FROM Note WHERE uid = :uid LIMIT 1")
    Note findById(int uid);
    //Функция для вставки (если мы вставляем заметку в базу данных с id , который уже существует, то будет преизведена замена со старой на новую)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Note note);
    //Функция для обновления
    @Update
    void update(Note note);
    //Функция для удаления
    @Delete
    void delete(Note note);

}
