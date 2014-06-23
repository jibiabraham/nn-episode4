package pg.pagalguy.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jibi on 17/6/14.
 */
public class PostItem implements Parcelable {
    private static final String BASEURL = "http://www.pagalguy.com";

    private long id;
    private String content;
    private String lastUpdated;
    private String url;
    private String author;
    private int likeCount;
    private int commentCount;

    public PostItem() {}

    public PostItem(Long id, String content, String lastUpdated, String url, String author, int likeCount, int commentCount) {
        this.id = id;
        this.lastUpdated = lastUpdated;
        this.url = url;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.content = content;
        this.author = author;
    }

    public PostItem(Parcel source) {
        id = source.readLong();
        content = source.readString();
        lastUpdated = source.readString();
        url= source.readString();
        author = source.readString();
        likeCount = source.readInt();
        commentCount = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(content);
        dest.writeString(lastUpdated);
        dest.writeString(url);
        dest.writeString(author);
        dest.writeInt(likeCount);
        dest.writeInt(commentCount);
    }

    public static final Creator<PostItem> CREATOR = new Creator<PostItem>(){
        @Override
        public PostItem createFromParcel(Parcel source) {
            return new PostItem(source);
        }

        @Override
        public PostItem[] newArray(int size) {
            return new PostItem[size];
        }
    };

    public String getAuthor() {
        return "@" + author;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return BASEURL + url;
    }

    public long getId() {
        return id;
    }
}
