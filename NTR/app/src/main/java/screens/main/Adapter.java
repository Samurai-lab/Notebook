package screens.main;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.ntr.App;
import com.example.ntr.R;

import java.util.List;

import model.Note;
import screens.details.NoteDetailsActivity;

public class Adapter extends RecyclerView.Adapter<Adapter.NoteViewHolder> {

    private SortedList<Note> sortedList;

    //Все свойства, которыми обладает наш лист
    public Adapter() {
        //Для работы списка и всех анимаций используем класс sortedList
        //sortedList работает на уровне рефлексии и сверяет, какие поля обновились, какие создались, а какие обновились и вызывает методы у Callback
        sortedList = new SortedList<>(Note.class, new SortedList.Callback<Note>() {
            @Override
            //Сравнивает между совой два обьект и нужен, чтобы sortedList знал, какой элемент больше (для сортироваки)
            public int compare(Note o1, Note o2) {
                if (!o2.done && o1.done) {
                    return 1;
                }
                if (o2.done && !o1.done) {
                    return -1;
                }
                //]сравниваем по времени создания
                return (int)(o2.timestamp - o1.timestamp);
            }

            //Остатки адаптера
            //Нужно сообщить нашему адаптеру, что элементы были изменены в таком-то диапозоне и произведет их обновление, не затрагивая остальные элементы
            @Override
            public void onChanged(int position, int count) {
                notifyItemChanged(position, count);
            }

            //Возвращает true, если обьекты равны не только по  id, но и по содержимому
            @Override
            public boolean areContentsTheSame(Note oldItem, Note newItem) {
                return oldItem.equals(newItem);
            }

            //Когда содержимое двух элемнтов может быть разное, но у них одинаковые id-шники
            @Override
            public boolean areItemsTheSame(Note item1, Note item2) {
                return item1.uid == item2.uid;
            }

            //Функция, сообщающая адаптеру об изменениях
            @Override
            public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
            }

            //Функция, сообщающая адаптеру об изменениях
            @Override
            public void onRemoved(int position, int count) {
               notifyItemRangeRemoved(position, count);
            }

            //Функция, сообщающая адаптеру об изменениях
            @Override
            public void onMoved(int fromPosition, int toPosition) {
               notifyItemMoved(fromPosition, toPosition);
            }

//            @Override
//            public void onChanged(int position, int count, Object payload) {
//                mBatchingListUpdateCallback.onChanged(position, count, payload);
//            }
//
//
//
//            @Override
//            public boolean areItemsTheSame(T2 item1, T2 item2) {
//                return mWrappedCallback.areItemsTheSame(item1, item2);
//            }
//
//            @Nullable
//            @Override
//            public Object getChangePayload(T2 item1, T2 item2) {
//                return mWrappedCallback.getChangePayload(item1, item2);
//            }
        });
    }

    //Создание нового ViewHolder
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_list, parent, false));
    }

    //Привязка к заметке
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
      holder.bind(sortedList.get(position));
    }

    //Сколько элементво в листе
    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    //Функция, позволяющая обновить список(содержимое адаптера)
    public void setItems(List<Note> notes) {

        //sortedList сравнит свое содержимое с новым списком, найдет разницу и выработает обновление, чтобы обновить только те элементы, которые были затронуты
        sortedList.replaceAll(notes);
    }
    //Создаем класс для отдельного элемента нашего адаптера(нужен, чтобы хранить все id-шники и ссылки, чтобы можно было их переиспользовать и каждый раз не доставать их)
    static class NoteViewHolder extends RecyclerView.ViewHolder {

        //Текст заметки
        TextView noteText;

        //Чекбокс
        CheckBox completed;

        //Конпка удаления
        View delete;

        //Заметка, которая в данный момент отображается данным элементом
        Note note;

        //Дополнительная переменная (для небольшого хака, чтобы при первоначльнем создании строки она не будет зачеркунтой)
        boolean silentUpdate;

        //Обработчик с проверками
        public NoteViewHolder (@NonNull final View itemView) {
            super(itemView);


            noteText = itemView.findViewById(R.id.note_text);
            completed = itemView.findViewById(R.id.completed);
            delete = itemView.findViewById(R.id.delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                    App.getInstance().getNoteDao().delete(note);
                    NoteDetailsActivity.start((Activity) itemView.getContext(), note);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                   App.getInstance().getNoteDao().delete(note);
                }
            });

            //Если не silentUpdate, то обновляем заметку
            completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
                    if (!silentUpdate) {
                        note.done = checked;
                        //Нужно сохранить изменения в заметки
                        App.getInstance().getNoteDao().update(note);
                    }
                    //Обновляем зачеркивание строки
                    updateStrokeOut();
                }
            });
        }
        //Функция, отображающая функции полей заметки на наши View(View находяться выше)
        public void bind(Note note) {

            //Запоминаем заметку, в которой работаем (пригодиться для обработки событий)
            this.note = note;

            //В поле для текста записываем текст заметки
            noteText.setText((note.text));

            //Обновляем функцию
            updateStrokeOut();
            silentUpdate = true;
            completed.setChecked(note.done);
            silentUpdate = false;
        }

        //Проверка дел на выполненное
        private void updateStrokeOut() {

            //Если дело сделано, то зачеркиваем
            if (note.done) {
                completed.setText("Выполнено");
                noteText.setPaintFlags(noteText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                completed.setText("Выполняется");
                noteText.setPaintFlags(noteText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
    }
}
