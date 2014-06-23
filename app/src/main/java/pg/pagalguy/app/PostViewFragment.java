package pg.pagalguy.app;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import pg.pagalguy.R;
import pg.pagalguy.adapter.CommentsListAdapter;
import pg.pagalguy.adapter.PostsListAdapter;
import pg.pagalguy.models.CommentItem;
import pg.pagalguy.models.PostItem;
import pg.pagalguy.utils.AsyncGet;
import pg.pagalguy.utils.YFractionProperty;

/**
 * Created by jibi on 19/6/14.
 */
public class PostViewFragment extends Fragment {
    private static final String BASEURL = "http://www.pagalguy.com";
    private static final String POSTURL = "postUrl";
    private static final String POSTID = "id";

    private PostItem post;
    private ArrayList<CommentItem> comments;
    private CommentsListAdapter adapter;
    private View mHeader;
    private ListView mListView;
    private View mPlaceHolderView;
    private int mActionBarHeight;
    private int mMinHeaderTranslation;
    private TypedValue mTypedValue = new TypedValue();

    private Long postId;
    private String postUrl;
    private String commentsUrl;
    public int startYPosition = 0;

    public PostViewFragment() {}

    public static PostViewFragment newInstance(Long id, String url){
        PostViewFragment postFragment = new PostViewFragment();

        Bundle args = new Bundle();
        args.putString(POSTURL, url);
        args.putLong(POSTID, id);
        postFragment.setArguments(args);

        return postFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            postUrl = getArguments().getString(POSTURL);
            postId = getArguments().getLong(POSTID);
            commentsUrl = BASEURL + "/comments/" + Long.toString(postId) + "?pages=1";
            comments = new ArrayList<CommentItem>();

            Log.v("COMMENTSURL::", commentsUrl);
            new AsyncGet(postUrl, fetchPost);
            new AsyncGet(commentsUrl, fetchComments);
        }
    }

    private Handler fetchPost = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            JSONObject json = (JSONObject) msg.obj;
            if (json != null) {
                try {
                    JSONObject payload = json.getJSONObject("payload");
                    JSONObject post = payload.getJSONObject("post");
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
                    PostViewFragment.this.post = new PostItem(id, content, lsUpdated, postUrl, author, likeCount, commentCount);

                    View view = PostViewFragment.this.getView();
                    TextView contentTv = (TextView) view.findViewById(R.id.content);
                    TextView nick = (TextView) view.findViewById(R.id.nick);

                    CharSequence htmlContent = Html.fromHtml(PostViewFragment.this.post.getContent());

                    contentTv.setText(htmlContent);
                    nick.setText(PostViewFragment.this.post.getAuthor());

                    int mHeaderHeight = PostViewFragment.this.mHeader.getMeasuredHeight();
                    PostViewFragment.this.mPlaceHolderView.setMinimumHeight(mHeaderHeight);
                    PostViewFragment.this.mMinHeaderTranslation = -mHeaderHeight + getActionBarHeight();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    private Handler fetchComments = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            JSONObject json = (JSONObject) msg.obj;
            Log.v("comments::", json.toString());
            if (json != null) {
                try {
                    JSONObject payload = json.getJSONObject("payload");
                    Log.v("PAYLOAD::", payload.toString());
                    JSONObject pages = payload.getJSONObject("pages");
                    Iterator keys = pages.keys();
                    while(keys.hasNext()){
                        String key = (String) keys.next();
                        JSONArray comments = pages.getJSONArray(key);
                        for (int i = 0; i < comments.length(); i++){
                            JSONObject comment = comments.getJSONObject(i);
                            Long id = comment.getLong("id");
                            String author = comment.getJSONObject("author").getString("nick");
                            String content = comment.getString("content");
                            String lsUpdated = comment.getString("updated");
                            CommentItem cItem = new CommentItem(id, content, lsUpdated, author);
                            PostViewFragment.this.comments.add(cItem);
                        }
                    }
                    int mHeaderHeight = PostViewFragment.this.mHeader.getMeasuredHeight();
                    PostViewFragment.this.mPlaceHolderView.setMinimumHeight(mHeaderHeight);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_view, container, false);
        if (adapter == null) {
            adapter = new CommentsListAdapter(getActivity().getApplicationContext(), this.comments);
        }
        mListView = (ListView) view.findViewById(R.id.list_comments);
        mHeader = view.findViewById(R.id.header);
        mPlaceHolderView = getActivity().getLayoutInflater().inflate(R.layout.view_header_placeholder, mListView, false);
        mMinHeaderTranslation = -mHeader.getMeasuredHeight() + getActionBarHeight();

        mListView.addHeaderView(mPlaceHolderView);
        mListView.setAdapter(adapter);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int scrollY = getScrollY();
                mHeader.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));
            }
        });
        return view;
    }

    public int getScrollY() {
        View c = mListView.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = mListView.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mPlaceHolderView.getHeight();
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    public int getActionBarHeight() {
        if (mActionBarHeight != 0) {
            return mActionBarHeight;
        }
        getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
        mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, getResources().getDisplayMetrics());
        return mActionBarHeight;
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
