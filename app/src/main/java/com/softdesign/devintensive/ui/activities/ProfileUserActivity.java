package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileUserActivity extends BaseActivity {
    private static final String TAG = ConstantManager.TAG_PREFIX + "ProfileUserActivity";
    private Toolbar mToolbar;
    private ImageView mProfileImage;
    private TextView mUserBio;
    private TextView mUserRaiting, mUserCodeLines, mUserProjects;
    private ListView mRepoListView;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private CoordinatorLayout mCoordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mProfileImage = (ImageView) findViewById(R.id.user_photo_img);
        mUserBio = (TextView) findViewById(R.id.bio_et);
        mUserRaiting = (TextView) findViewById(R.id.user_value_raiting);
        mUserCodeLines = (TextView) findViewById(R.id.user_value_code_lines);
        mUserProjects = (TextView) findViewById(R.id.user_value_projects);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mRepoListView = (ListView) findViewById(R.id.repositories_list);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_conteiner);
        setupToolBar();
        initProfileDate();
    }

    private void setupToolBar() {
        Log.d(TAG, "setupToolBar");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initProfileDate() {
        Log.d(TAG, "initProfileDate");
        UserDTO userDTO = getIntent().getParcelableExtra(ConstantManager.PARCELABLER_KEY);
        final List<String> repositories = userDTO.getRepositories();
        final RepositoriesAdapter repositoriesAdapter = new RepositoriesAdapter(this, repositories);

        mRepoListView.setAdapter(repositoriesAdapter);
        mRepoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + repositories.get(position)));
                startActivity(intent);
            }
        });

        mUserBio.setText(userDTO.getBio());
        mUserProjects.setText(userDTO.getProjects());
        mUserCodeLines.setText(userDTO.getCodeLines());
        mUserRaiting.setText(userDTO.getRaiting());
        mCollapsingToolbarLayout.setTitle(userDTO.getFullName());

        Picasso.with(this)
                .load(userDTO.getPhoto())
                .placeholder(R.drawable.user_bg)
                .error(R.drawable.user_bg)
                .fit()
                .centerCrop()
                .into(mProfileImage);
    }
}
