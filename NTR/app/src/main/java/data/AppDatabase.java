/** Класс, описывающий всю базу данных
 *
 *
 *
 * **/
package data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import model.Note;
//Указываем, какие сущьности будут в нашей базы данных (у нас одна)
@Database(entities = {Note.class}, version = 1, exportSchema = false)
//Обстрактный метод, при создании которого автаматически генирируется реализация нашего интерфейса (NoteDao) библиотекой room
public abstract class AppDatabase extends RoomDatabase {
    //База данных будет работать все время, пока данная сессия приложения не закончится
    public abstract NoteDao noteDao();
}
