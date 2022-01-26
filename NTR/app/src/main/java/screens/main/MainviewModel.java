/** Нужно предоставит ьдоступ к нашим заметкам для того, чтобы этот самый список отразился на главном экране
 *
 *
 *
 * **/
package screens.main;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ntr.App;

import java.util.List;

import model.Note;

public class MainviewModel extends ViewModel {
    //Реализуем список заметок
    private LiveData<List<Note>> noteLiveData = App.getInstance().getNoteDao().getAllLiveData();
    //Ну и геттер собственно
    public LiveData<List<Note>> getNoteLiveData() {
        return noteLiveData;
    }
}
