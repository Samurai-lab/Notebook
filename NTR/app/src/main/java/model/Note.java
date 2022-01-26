/** Класс, описывающий отдельную сущьность
 *
 *
 *
 * **/
package model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
//parcelable  нужен для передачи нашего Note(заметка) через активити (в проекте два экрана и они будут активити)
public class Note implements Parcelable {
    //Индивидуальный идентификатор, который будет иметь значение для каждого обьекта (сущьности)
    @PrimaryKey(autoGenerate = true)
    public int uid;
    //Текст заметки
    @ColumnInfo(name = "text")
    public String text;
    //Время создания заметки (на всякий случай long)
    @ColumnInfo(name = "timestamp")
    public long timestamp;
    // Обозначает состояние дела (сделано/не сделано)
    @ColumnInfo(name = "done")
    public boolean done;

    public Note() {
    }

    protected Note(Parcel in) {
        uid = in.readInt();
        text = in.readString();
        timestamp = in.readLong();
        done = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeString(text);
        dest.writeLong(timestamp);
        dest.writeByte((byte) (done ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return uid == note.uid &&
                timestamp == note.timestamp &&
                done == note.done &&
                text.equals(note.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, text, timestamp, done);
    }
}
