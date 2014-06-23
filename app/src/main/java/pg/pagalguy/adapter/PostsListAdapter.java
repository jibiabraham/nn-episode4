package pg.pagalguy.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pg.pagalguy.R;
import pg.pagalguy.models.PostItem;

/**
 * Created by jibi on 17/6/14.
 */
public class PostsListAdapter extends BaseAdapter {
    public Context context;
    public ArrayList<PostItem> posts;

    public PostsListAdapter(Context context, ArrayList<PostItem> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public int getCount() {
        if(this.posts != null)
            return this.posts.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(this.posts != null)
            return this.posts.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the item view if null
        if(convertView == null){
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.post_list_item, null);
        }

        PostItem dItem = (PostItem) getItem(position);

        TextView content = (TextView) convertView.findViewById(R.id.content);
        TextView nick = (TextView) convertView.findViewById(R.id.nick);
        TextView likes = (TextView) convertView.findViewById(R.id.likes);
        TextView comments = (TextView) convertView.findViewById(R.id.comments);

        CharSequence htmlContent = Html.fromHtml(dItem.getContent());

        content.setText(htmlContent);
        nick.setText(dItem.getAuthor());
        likes.setText(Integer.toString(dItem.getLikeCount()) + " likes");
        comments.setText(Integer.toString(dItem.getCommentCount()) + " comments");

        return convertView;
    }
}
