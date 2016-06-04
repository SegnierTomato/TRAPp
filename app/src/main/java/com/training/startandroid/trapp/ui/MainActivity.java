package com.training.startandroid.trapp.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import android.support.v7.view.ActionMode;
//import android.view.ActionMode;

import android.support.v7.widget.Toolbar;
//import android.widget.Toolbar;

import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.training.startandroid.trapp.R;
import com.training.startandroid.trapp.database.DatabaseConnection;
import com.training.startandroid.trapp.database.dao.DAOFactory;
import com.training.startandroid.trapp.model.Catalog;
import com.training.startandroid.trapp.ui.selection.SelectionHelper;
import com.training.startandroid.trapp.ui.selection.SelectionObserver;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final ActionModeCallback mActionModeCallback = new ActionModeCallback();
    private SelectableRecyclerViewAdapter mAdapter;
    private ActionMode mActionMode;

    public static boolean isTablet(Context context) {

        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE);

        return (xlarge || large);
    }

    public static int getDisplayColumns(Context context) {

        int columnCount = 1;
        if (isTablet(context)) {
            columnCount = 2;
        }

        return columnCount;
    }

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

        DatabaseConnection.initializeConnection(getApplicationContext());
        DatabaseConnection.openConnection();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.catalog_recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(getDisplayColumns(this), StaggeredGridLayoutManager.VERTICAL));

        RecyclerView.ItemAnimator itemAnimatior = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimatior);


//        List<Catalog> listCatalogs = DAOFactory.getCatalogsDAO().getAllCatalogs();

        List<Catalog> listCatalogs = initializeListCatalogs(50);

        mAdapter = new SelectableRecyclerViewAdapter(this, listCatalogs);
        recyclerView.setAdapter(mAdapter);

    }

    private List<Catalog> initializeListCatalogs(int count) {
        List<Catalog> listCatalogs = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Catalog newCatalog = new Catalog(i + 1, "Test Catalog " + (i+1), new Date());
            listCatalogs.add(newCatalog);
        }
        return listCatalogs;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
        DatabaseConnection.openConnection();
        super.onResume();
    }

    @Override
    protected void onPause() {
        DatabaseConnection.closeConnection();
        super.onPause();
    }

    public void startActionMode() {
        mActionMode = startSupportActionMode(mActionModeCallback);
    }

    public ActionMode getActionMode(){
        return mActionMode;
    }

    private class ActionModeCallback implements ActionMode.Callback, SelectionObserver {

        private ActionMode mActionMode;

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            SelectionHelper selectionHelper = mAdapter.getSelectionHelper();
            selectionHelper.unregisterSelectionObserver(this);
            mActionMode = null;
            selectionHelper.setSelectable(false);
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            mActionMode = actionMode;

            mActionMode.getMenuInflater().inflate(R.menu.catalog_selection, menu);
            mAdapter.getSelectionHelper().registerSelectionObserver(this);
            return true;

        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

            SelectionHelper selectionHelper = mAdapter.getSelectionHelper();

            switch (menuItem.getItemId()) {
                case R.id.action_remove:

                    HashSet<Integer> selectedPositions = selectionHelper.getSelectedItemsPositions();
                    for (Integer position : selectedPositions) {
//                        DAOFactory.getCatalogsDAO().removeCatalogById(position);
                    }
                    mAdapter.removeSelectedItems();

                    Toast.makeText(MainActivity.this,
                            "Remove selected catalogs", Toast.LENGTH_SHORT).show();

                    break;

                case R.id.action_edit:

                    break;

                case R.id.action_select_all:

                    mAdapter.selectAllItems();
                    break;

                case R.id.action_unselected_all:
                    mAdapter.clearSelectionsItems();
                    break;
            }

            return true;
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder holder, boolean isSelected) {
            if (mActionMode != null) {
                int checkedCatalogsCount = mAdapter.getSelectionHelper().getSelectedItemsCount();
                mActionMode.setTitle(String.valueOf(checkedCatalogsCount));
            }
        }

        @Override
        public void onSelectableChanged(boolean isSelectable) {

            if (!isSelectable) {
                mActionMode.finish();
            }
        }
    }
}
