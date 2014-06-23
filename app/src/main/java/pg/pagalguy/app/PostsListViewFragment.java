package pg.pagalguy.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pg.pagalguy.R;
import pg.pagalguy.adapter.PostsListAdapter;
import pg.pagalguy.models.DiscussionItem;
import pg.pagalguy.models.PostItem;
import pg.pagalguy.utils.AsyncGet;

/**
 * Created by jibi on 18/6/14.
 */
public class PostsListViewFragment extends Fragment {

    private static final String DISCUSSIONURL = "url";
    private static final String DISCUSSIONPOSTS = "posts";

    public ArrayList<PostItem> posts;
    private PostsListAdapter adapter;
    private ListView mListView;
    public int startYPosition = 0;

    // Required empty constructor
    public PostsListViewFragment() {}

    public static PostsListViewFragment newInstance(String discussionUrl){
        PostsListViewFragment fragment = new PostsListViewFragment();

        Bundle args = new Bundle();
        args.putString(DISCUSSIONURL, discussionUrl);
        fragment.setArguments(args);

        return fragment;
    }


    public static PostsListViewFragment newInstance(ArrayList<PostItem> posts){
        PostsListViewFragment fragment = new PostsListViewFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList(DISCUSSIONPOSTS, posts);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            posts = getArguments().getParcelableArrayList(DISCUSSIONPOSTS);
        }
    }

    public static ArrayList<PostItem> parsePostsJson (JSONObject data){
        ArrayList<PostItem> postsArray = new ArrayList<PostItem>();
        try {
            JSONObject payload = data.getJSONObject("payload");
            JSONArray posts = payload.getJSONArray("posts");
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.getJSONObject(i);
                Long id = post.getLong("id");
                String author = post.getJSONObject("author").getString("nick");
                String content = post.getString("content");
                String postUrl = post.getString("url");
                String lsUpdated = post.getString("updated");
                int commentCount = 0;
                int likeCount = 0;
                if (post.has("cmc")){
                    commentCount = post.getInt("cmc");
                }
                if (post.has("lc")){
                    likeCount = post.getInt("lc");
                }
                PostItem pItem = new PostItem(id, content, lsUpdated, postUrl, author, likeCount, commentCount);
                postsArray.add(pItem);
            }
            Log.v("PostsListViewOnDataFetched:::", "Yes we have data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postsArray;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts_list_view, container, false);
        if(adapter == null){
            adapter = new PostsListAdapter(getActivity().getApplicationContext(), this.posts);
        }
        mListView = (ListView) view .findViewById(R.id.list_posts);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PostItem card = posts.get(position);
                String text = Integer.toString(view.getTop()) + ":::" + card.getUrl();
                Toast.makeText(getActivity().getApplicationContext(), text, 100).show();
                /*
                * Initiate the new view
                * */
                Fragment postViewFragment = PostViewFragment.newInstance(card.getId(), card.getUrl());
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();


                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.frame_container, postViewFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });
        return view;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Context context = getActivity().getApplicationContext();
        Animation anim;
        if(!enter || context == null){
            return null;
        }
        anim = new TranslateAnimation(0f, 0f, startYPosition, 0f);
        anim.setDuration(150);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                getView().setTranslationY(0);
            }
        });
        return anim;
    }
}
