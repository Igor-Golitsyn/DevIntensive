package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.CustomLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<User>> {
    private static final String TAG = ConstantManager.TAG_PREFIX + "UserListActivity";
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private RecyclerView mRecyclerView;
    private DataManager mDataManager;
    private UsersAdapter mUsersAdapter;
    List<User> mUsers;
    private MenuItem mSearchItem;
    private String mQuery;
    private Handler mHandler;
    private CircularImageView mCircularDrawerHeaderAvatar;
    private TextView mUserEmailDrawerHeader;
    private Loader<List<User>> mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        mDataManager = DataManager.getInstance();
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_conteiner);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mRecyclerView = (RecyclerView) findViewById(R.id.user_list);

        mHandler = new Handler();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        setupToolbar();
        setupDrawer();
        mLoader = getSupportLoaderManager().initLoader(1, new Bundle(), this);
        //loadUsersFromDb();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSnackBar(String message) {
        Log.d(TAG, "showSnackBar");
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void loadUsersFromDb() {
        Log.d(TAG, "loadUsersFromDb");
        if (mDataManager.getUserListFromDb().size() == 0) {
            showSnackBar("Список пользователей не может быть загружен");
        } else {
            showUsers(mDataManager.getUserListFromDb());
        }
    }

    private void setupToolbar() {
        Log.d(TAG, "setupToolbar");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawer() {
        Log.d(TAG, "setupDrawer");
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.getMenu().getItem(1).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                if (item == navigationView.getMenu().getItem(0)) {
                    finish();
                }
                return false;
            }
        });
        mCircularDrawerHeaderAvatar = (CircularImageView) navigationView.getHeaderView(0).findViewById(R.id.drawer_header_avatar);
        mUserEmailDrawerHeader = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawer_header_user_email_txt);
        mUserEmailDrawerHeader.setText(mDataManager.getPreferenceManager().loadUserProfileData().get(1));
        Picasso.with(this)
                .load(mDataManager.getPreferenceManager().loadUserAvatar())
                .placeholder(R.drawable.camaro_yellow)
                .into(mCircularDrawerHeaderAvatar);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "onPrepareOptionsMenu");
        getMenuInflater().inflate(R.menu.search_menu, menu);
        mSearchItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        searchView.setQueryHint("Введите имя пользователя");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                showUsersByQuery(newText);
                return false;
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    private void showUsers(List<User> users) {
        Log.d(TAG, "showUsers");
        mUsers = users;
        mUsersAdapter = new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
            @Override
            public void onUserItemClickListener(int position) {
                UserDTO userDTO = new UserDTO(mUsers.get(position));
                Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                profileIntent.putExtra(ConstantManager.PARCELABLER_KEY, userDTO);
                startActivity(profileIntent);
            }
        });
        mRecyclerView.swapAdapter(mUsersAdapter, false);
    }

    private void showUsersByQuery(String query) {
        Log.d(TAG, "showUsersByQuery");
        mQuery = query;
        Runnable seachUser = new Runnable() {
            @Override
            public void run() {
                showUsers(mDataManager.getUserListByName(mQuery));
            }
        };
        mHandler.removeCallbacks(seachUser);
        long delay = mQuery.isEmpty() ? 0L : ConstantManager.SEARCH_DELAY;
        mHandler.postDelayed(seachUser, delay);
    }


    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader");
        return new CustomLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> data) {
        Log.d(TAG, "onLoadFinished");
        mUsers = data;
        showUsers(mUsers);
    }

    @Override
    public void onLoaderReset(Loader<List<User>> loader) {
        Log.d(TAG, "onLoaderReset");
    }

    /**
     * Обработка нажатия кнопки "back". Убирает открытую NavigationDrawer
     */
    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
