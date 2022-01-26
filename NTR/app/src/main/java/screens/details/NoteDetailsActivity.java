/** Активити для редактирования и создания заметки
 *
 *
 *
 * **/
package screens.details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ntr.App;
import com.example.ntr.R;

import model.Note;

public class NoteDetailsActivity extends AppCompatActivity {

    //Передача и возврат заметки (передаем целеком название самой заметки)
    private static final String EXTRA_NOTE = "NoteDetailsActivity.EXTRA_NOTE";

    //Заметка, которую мы в данный момент редактируем или создаем
    private Note note;

    //Наше текстовое поле для ввода
    private EditText editText;


    public static void start(Activity caller, Note note) {
        Intent intent = new Intent(caller, NoteDetailsActivity.class);

        //Если заметка не равна null, то крепим заметку к интенту
        if (note != null) {
            intent.putExtra(EXTRA_NOTE, note);
        }
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Эта функция приводет к тому, что приложение читает опиание нашего layout и создает уже конекретные классы,
         и эти классы автоматически добавляются к активити(после чего их собственно можно истопльзовать) */
        setContentView(R.layout.activity_note_details);

        //Задаем тулбар в качестве екшенбара
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Кнопка назад
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Текст заголовка
        setTitle(getString(R.string.note_details_title));

        //Достаем editText по id
        editText = findViewById(R.id.text);

        //Если интент есть, то задаем текст, который внутри заметки. Если же заметки нету, то создаем новую заметку
        if (getIntent().hasExtra(EXTRA_NOTE)) {
            note = getIntent().getParcelableExtra(EXTRA_NOTE);
            editText.setText(note.text);
        } else {
            note = new Note();
        }
    }


    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        //Извлекаем меню
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //Обработка событий
    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item) {

        switch (item.getItemId()) {
            //Если нажата кнопка домой, то завершаем активити
            case android.R.id.home:
                if (editText.getText().length() > 0) {
                    //Текст заметки
                    note.text = editText.getText().toString();
                    //Дело по умолчанию еще не сделано, поэтому изначально не зачеркиваем (false)
                    note.done = false;
                    note.timestamp = System.currentTimeMillis();
                    //Если новая, то вставить в таблицу, а если старая, то изменить (п.с. с этим пунктом баги)
                    //Проверяем интент (если заметка передовалась, то редактирование)
                    if (getIntent().hasExtra(EXTRA_NOTE)) {
                        App.getInstance().getNoteDao().insert(note);
                    } else {
                        App.getInstance().getNoteDao().update(note);
                    }
                    finish();
                } else if (editText.getText().length() == 0) {
                    //(п.с. проблема при нажатии на кнопку задан (она делает тоже самое, что и сохранить))
                    finish();
                }
                break;
                //Если нажата кнопка сохранить, то сохраняем нашу заметку
            case R.id.action_save:
                //Если пользователь ничего не добавил или не ввел, то и кнопка не работает
                if (editText.getText().length() > 0) {
                    //Текст заметки
                    note.text = editText.getText().toString();
                    //Дело по умолчанию еще не сделано, поэтому изначально не зачеркиваем (false)
                    note.done = false;
                    note.timestamp = System.currentTimeMillis();
                    //Если новая, то вставить в таблицу, а если старая, то изменить (п.с. с этим пунктом баги)
                    //Проверяем интент (если заметка передовалась, то редактирование)
                    if (getIntent().hasExtra(EXTRA_NOTE)) {
                        App.getInstance().getNoteDao().insert(note);
                    } else {
                        App.getInstance().getNoteDao().insert(note);
                    }
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
