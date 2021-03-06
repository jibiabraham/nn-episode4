package pg.pagalguy.app;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pg.pagalguy.R;
import pg.pagalguy.adapter.DiscussionsAdapter;
import pg.pagalguy.models.DiscussionItem;
import pg.pagalguy.utils.AsyncGet;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class DiscussionsViewFragment extends Fragment {
    private static final String DISCUSSIONS = "discussions";
    private static final String DISCUSSIONURL = "url";

    public ArrayList<DiscussionItem> discussions;
    private DiscussionsAdapter adapter;
    private ListView mListView;

    public DiscussionsViewFragment() {
        // Required empty public constructor
    }

    public static DiscussionsViewFragment newInstance(String discussionUrl) {
        DiscussionsViewFragment fragment = new DiscussionsViewFragment();
        /*
        * Fetch the json from the URL
        * Parse JSON
        * Create an ArrayList of posts from the data
        * Replace the loader fragment with the posts fragment
        */
        Bundle args = new Bundle();
        args.putParcelableArrayList(DISCUSSIONS, new ArrayList<DiscussionItem>());
        args.putString(DISCUSSIONURL, discussionUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String url = getArguments().getString(DISCUSSIONURL);
            discussions = getArguments().getParcelableArrayList(DISCUSSIONS);
            if(discussions != null && discussions.size() == 0){
                new AsyncGet(url, jsonHandler);
            }
        }
    }

    private Handler jsonHandler = new Handler() {
        public void handleMessage(Message msg) {
            JSONObject json = (JSONObject) msg.obj;
            if (json != null) {
                try {
                    JSONObject payload = json.getJSONObject("payload");
                    JSONArray discussions = payload.getJSONArray("all_threads");
                    for (int i = 0; i < discussions.length(); i++) {
                        JSONArray discussionTuple = discussions.getJSONArray(i);
                        JSONObject discussion = discussionTuple.getJSONObject(0);
                        String title = discussion.getString("content");
                        String lastUpdated = discussion.getString("updated");
                        int following = 0;
                        DiscussionItem dItem = new DiscussionItem(title, lastUpdated, following);
                        DiscussionsViewFragment.this.discussions.add(dItem);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discussions_view, container, false);
        if(adapter == null){
            adapter = new DiscussionsAdapter(getActivity().getApplicationContext(), this.discussions);
        }
        mListView = (ListView) view.findViewById(R.id.list_discussions);
        mListView.setAdapter(adapter);
        return view;
    }


}
