package pg.pagalguy.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jibi on 17/6/14.
 */
public class DiscussionItem implements Parcelable {
    private String title;
    private String lastUpdated;
    private int following;

    public DiscussionItem() {}

    public DiscussionItem(String title, String lastUpdated, int following) {
        this.title = title;
        this.lastUpdated = lastUpdated;
        this.following = following;
    }

    public DiscussionItem(Parcel source) {
        title = source.readString();
        lastUpdated = source.readString();
        following = source.readInt();
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

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(lastUpdated);
        dest.writeInt(following);
    }

    public static final Creator<DiscussionItem> CREATOR = new Creator<DiscussionItem>(){
        @Override
        public DiscussionItem createFromParcel(Parcel source) {
            return new DiscussionItem(source);
        }

        @Override
        public DiscussionItem[] newArray(int size) {
            return new DiscussionItem[size];
        }
    };
}
