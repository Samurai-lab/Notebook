/** Получаем доступ к приложению, а через него и к интересующим нас обьектам работы с данными из любой части приложения
 *
 *
 *
 * **/
package com.example.ntr;

import android.app.Application;

import androidx.room.Room;

import data.AppDatabase;
import data.NoteDao;

public class App extends Application {

    private AppDatabase database;
    private NoteDao noteDao;
    private static App instance;

    public static App getInstance() {
        return instance;
    }
    //Будет вызванно до момента включения всего приложения в целом (эта инициализация выполнится при любом старте приложения или же процесса)
    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        //После выполнение следующего кода поулчаем уже первый функционал нашей базы данных
        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "app-db-name")
                .allowMainThreadQueries()
                //Запросы базы данных в основной поток (не рекомендуют, но иначе пока не реализуемо :))
                .build();
        //Получаем полностью рабочий обьект даты
        noteDao = database.noteDao();
    }
    //Методы для доступа к интересующим нас обьектам (называют также геттеры (get(извлечь)))
    public AppDatabase getDatabase() {
        return database;
    }

    public void setDatabase(AppDatabase database) {
        this.database = database;
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }
}
