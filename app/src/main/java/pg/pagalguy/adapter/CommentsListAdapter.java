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
import pg.pagalguy.models.CommentItem;

/**
 * Created by jibi on 19/6/14.
 */
public class CommentsListAdapter extends BaseAdapter {
    public Context context;
    public ArrayList<CommentItem> comments;

    public CommentsListAdapter(Context context, ArrayList<CommentItem> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        if(this.comments != null){
            return this.comments.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(this.comments != null){
            return this.comments.get(position);
        }
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

        CommentItem dItem = (CommentItem) getItem(position);

        TextView content = (TextView) convertView.findViewById(R.id.content);
        TextView nick = (TextView) convertView.findViewById(R.id.nick);

        CharSequence htmlContent = Html.fromHtml(dItem.getContent());

        content.setText(htmlContent);
        nick.setText(dItem.getAuthor());

        return convertView;
    }
}
