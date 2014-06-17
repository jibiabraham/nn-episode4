package pg.pagalguy.models;

/**
 * Created by jibi on 17/6/14.
 */
public class NavDrawerItem {
    private String title;

    public NavDrawerItem(){}

    public NavDrawerItem(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
