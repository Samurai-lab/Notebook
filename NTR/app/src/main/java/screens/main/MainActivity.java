/** Создаем интерфейс для главного активити
 *
 *
 *
 * **/
package screens.main;

import android.os.Bundle;
import com.example.ntr.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import model.Note;
import screens.details.NoteDetailsActivity;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    //Действие по умолчанию
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.list);
        //Отображаем список сверху вниз (как и задумано)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //Привязываем ViewModel к нашему View
        final Adapter adapter = new Adapter();
        recyclerView.setAdapter(adapter);

        //Кнопка в левом нижнем углу (отвечает за создание новой заметки)
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteDetailsActivity.start(MainActivity.this, null);
            }
        });

        //Подкючаем  ViewModel
        MainviewModel mainviewModel = ViewModelProviders.of(this).get(MainviewModel.class);
        mainviewModel.getNoteLiveData().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setItems(notes);
            }
        });

    }
}