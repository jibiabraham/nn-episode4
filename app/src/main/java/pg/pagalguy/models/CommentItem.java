package pg.pagalguy.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jibi on 19/6/14.
 */
public class CommentItem implements Parcelable{
    private long id;
    private String content;
    private String updated;
    private String author;

    public CommentItem(long id, String content, String updated, String author) {
        this.id = id;
        this.content = content;
        this.updated = updated;
        this.author = author;
    }

    public CommentItem(Parcel source){
        id = source.readLong();
        content = source.readString();
        updated = source.readString();
        author = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(content);
        dest.writeString(updated);
        dest.writeString(author);
    }

    public static final Creator<CommentItem> CREATOR = new Creator<CommentItem>(){
        @Override
        public CommentItem createFromParcel(Parcel source) {
            return new CommentItem(source);
        }

        @Override
        public CommentItem[] newArray(int size) {
            return new CommentItem[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
