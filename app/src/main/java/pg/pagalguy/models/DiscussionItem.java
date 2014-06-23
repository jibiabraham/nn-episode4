package pg.pagalguy.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jibi on 17/6/14.
 */
public class DiscussionItem implements Parcelable {
    private static final String BASEURL = "http://www.pagalguy.com";

    private String title;
    private String lastUpdated;
    private String url;
    private int following;

    public DiscussionItem() {}

    public DiscussionItem(String title, String lastUpdated, String url) {
        this.title = title;
        this.lastUpdated = lastUpdated;
        this.url = url;
    }

    public DiscussionItem(Parcel source) {
        title = source.readString();
        lastUpdated = source.readString();
        url = source.readString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public String getUrl() {
        return BASEURL + url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(lastUpdated);
        dest.writeString(url);
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
}
