package com.training.startandroid.trapp.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;

import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;

import com.training.startandroid.trapp.R;
import com.training.startandroid.trapp.ui.fragments.catalogs.CatalogsViewFragment;
import com.training.startandroid.trapp.util.FragmentHelper;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            CatalogsViewFragment catalogsViewFragment = new CatalogsViewFragment();

            final String tag = CatalogsViewFragment.class.getName();
            transaction.add(R.id.fragment_parent_layout, catalogsViewFragment, tag);

            transaction.addToBackStack(tag);
            transaction.commit();
        }

    }

    @Override
    public void onBackPressed() {

        Log.d(LOG_TAG, "back press");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        int countFragmentsInParentLevel = fragmentManager.getBackStackEntryCount();

        Fragment higherFragmentInParentLevel = null;

        if (countFragmentsInParentLevel == 1) {
            String higherFragmentTagInLevel = fragmentManager.getBackStackEntryAt(0).getName();
            higherFragmentInParentLevel = fragmentManager.findFragmentByTag(higherFragmentTagInLevel);
        }

        Fragment higherFragmentInActivity = FragmentHelper.getHigherFragmentInStack(fragmentManager);

        if (countFragmentsInParentLevel > 1 || higherFragmentInActivity != higherFragmentInParentLevel) {

            Fragment previousFragment = FragmentHelper.getPreviousFragmentInBackStack(fragmentManager);
            FragmentHelper.comeBack2PreviousFragment(higherFragmentInActivity, previousFragment, fragmentManager);

        } else {
            super.onBackPressed();
            finish();
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

        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                return true;

            case R.id.action_setting:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_dictionary:
                break;
            case R.id.nav_tests:
                break;

            case R.id.nav_search:
                break;

            case R.id.nav_settings:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override

    protected void onPause() {
        super.onPause();
    }

}
