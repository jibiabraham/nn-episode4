package pg.pagalguy.app;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import pg.pagalguy.R;
import pg.pagalguy.adapter.NavDrawerListAdapter;
import pg.pagalguy.models.NavDrawerItem;

/**
 * Created by jibi on 17/6/14.
 */
public class MainActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // Nav drawer title
    private CharSequence mDrawerTitle;
    // Store app title
    private CharSequence mTitle;

    private String[] navMenuTitles;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the current title
        mTitle = mDrawerTitle = getTitle();

        // Get the nav menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // Set the references to our layout objects
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);


        navDrawerItems = new ArrayList<NavDrawerItem>();
        for (int i = 0; i < navMenuTitles.length; i++){
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[i]));
        }

        // Instantiate and attach the nav drawer list adepter to the nav drawer list view
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new NavMenuClickListener());

        /* The up navigation will allow our application to move to previous activity from the next activity
        * http://www.tutorialspoint.com/android/android_navigation.htm
        *
        * Set whether home should be displayed as an "up" affordance. Set this to true if selecting "home"
        * returns up by a single level in your UI rather than back to the top level or front page.
        * https://developer.android.com/reference/android/support/v7/app/ActionBar.html#setDisplayHomeAsUpEnabled(boolean)
        *
        * getSupportActionBar
        * http://developer.android.com/reference/android/support/v7/app/ActionBarActivity.html#getSupportActionBar%28%29*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Define the drawer toggle actions
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer,   // nav menu toggle item
                R.string.app_name,      // nav drawer open - accessibility
                R.string.app_name       // nav drawer close - accessibility
        ){
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if(savedInstanceState == null){
            displayView(0);
        }
    }

    // Switch between views
    private void displayView(int position) {
        // We'll be replace main content with appropriate fragments
        android.support.v4.app.Fragment fragment = null;
        switch (position){
            case 0:
                fragment = DiscussionsViewFragment.newInstance("http://smaug.pagalguy.com/cat");
                break;
            default:
                break;
        }

        if(fragment != null){
            /* Switch out the fragments
            * http://www.javacodegeeks.com/2013/06/android-fragment-transaction-fragmentmanager-and-backstack.html*/
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().
                    replace(R.id.frame_container, fragment).commit();

            // Update selected item and title and close drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setTitle(CharSequence title){
        mTitle = title;
        getSupportActionBar().setTitle(title);
    }

    private class NavMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            displayView(position);
        }
    }
}
