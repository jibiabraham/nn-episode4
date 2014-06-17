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
import pg.pagalguy.models.DiscussionItem;

/**
 * Created by jibi on 17/6/14.
 */
public class DiscussionsAdapter extends BaseAdapter {
    public Context context;
    public ArrayList<DiscussionItem> discussions;

    public DiscussionsAdapter(Context context, ArrayList<DiscussionItem> discussions) {
        this.context = context;
        this.discussions = discussions;
    }

    @Override
    public int getCount() {
        if(this.discussions != null)
            return this.discussions.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(this.discussions != null)
            return this.discussions.get(position);
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
            convertView = mInflater.inflate(R.layout.discussion_item, null);
        }

        DiscussionItem dItem = (DiscussionItem) getItem(position);

        TextView lsUpdated = (TextView) convertView.findViewById(R.id.ls_updated);
        TextView title = (TextView) convertView.findViewById(R.id.title);

        CharSequence htmlTitle = Html.fromHtml(dItem.getTitle());
        title.setText(htmlTitle);

        lsUpdated.setText(dItem.getLastUpdated());

        return convertView;
    }
}
