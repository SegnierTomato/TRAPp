package com.training.startandroid.trapp.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;

import android.support.v7.view.ActionMode;
//import android.view.ActionMode;

import android.support.v7.widget.Toolbar;
//import android.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.training.startandroid.trapp.R;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String LOG_TAG = "class MainActivity";
    private RelativeLayout mFragmentParentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setAnimation();

        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                int countFragment = fragmentManager.getBackStackEntryCount();

                final String tag = fragmentManager.getBackStackEntryAt(countFragment - 1).getName();
                Fragment currentFragment = fragmentManager.findFragmentByTag(tag);

                if (currentFragment != null) {
                    fragmentTransaction.hide(currentFragment);
                }

                Fragment addCatalogFragment = new AddCatalogFragment();

                final String currentOperationTag = AddCatalogFragment.class.getName();
                fragmentTransaction.add(R.id.fragment_parent_layout, addCatalogFragment, currentOperationTag);

                fragmentTransaction.addToBackStack(currentOperationTag);
                fragmentTransaction.commit();

                Log.d("Fragment fab stack coun", fragmentManager.getBackStackEntryCount() + "");

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mFragmentParentLayout = (RelativeLayout) findViewById(R.id.fragment_parent_layout);


        FragmentManager fragmentManager = getFragmentManager();

//        FragmentsOnBackStackListener backStackListener = new FragmentsOnBackStackListener();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        CatalogsViewFragment catalogsViewFragment = new CatalogsViewFragment();
        final String tag = CatalogsViewFragment.class.getName();
//        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        transaction.add(R.id.fragment_parent_layout, catalogsViewFragment,tag);

        transaction.addToBackStack(tag);
        transaction.commit();

    }

    private void getBackPreviousFragment(Fragment currentFragment, Fragment previousFragment) {

        try {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (currentFragment != null) {
//            fragmentTransaction.hide(currentFragment);
                fragmentTransaction.remove(currentFragment);
            }

            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);

            if (previousFragment != null) {
                fragmentTransaction.show(previousFragment);
            }

            int count = fragmentManager.getBackStackEntryCount();
            fragmentManager.popBackStackImmediate();
            count = fragmentManager.getBackStackEntryCount();

//            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.toString());
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        FragmentManager fragmentManager = getFragmentManager();
        int countFragments = fragmentManager.getBackStackEntryCount();

        if (countFragments != 1) {

            final String tagCurrent = fragmentManager.getBackStackEntryAt(countFragments - 1).getName();
            final String tagPrevious = fragmentManager.getBackStackEntryAt(countFragments - 2).getName();
            Fragment currentFragment = fragmentManager.findFragmentByTag(tagCurrent);
            Fragment previousFragment = fragmentManager.findFragmentByTag(tagPrevious);

            getBackPreviousFragment(currentFragment, previousFragment);

        } else {

            super.onBackPressed();
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
            case R.id.action_remove:
                return true;
            case R.id.action_edit:
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

    class FragmentsOnBackStackListener implements FragmentManager.OnBackStackChangedListener {

        @Override
        public void onBackStackChanged() {

            FragmentManager fragmentManager = getFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                int id = fragmentManager.getBackStackEntryAt(i).getId();
                Fragment fragment = fragmentManager.findFragmentById(id);
            }

            fragmentTransaction.commit();
        }
    }
}
