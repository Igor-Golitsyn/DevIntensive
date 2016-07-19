package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";
    private File mPhotoFile = null;
    private Uri mSelectedImage = null;
    private boolean mCurrentEditMode = false;
    private List<EditText> mUserInfoViews;
    private DataManager mDataManager;
    private AppBarLayout.LayoutParams mAppBarParams = null;
    @BindView(R.id.main_coordinator_conteiner)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.user_score)
    LinearLayout mUserScoreLayout;
    @BindView(R.id.navigation_drawer)
    DrawerLayout mNavigationDrawer;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.phone_et)
    EditText mUserPhone;
    @BindView(R.id.email_et)
    EditText mUserMail;
    @BindView(R.id.vk_et)
    EditText mUserVK;
    @BindView(R.id.git_et)
    EditText mUserGit;
    @BindView(R.id.bio_et)
    EditText mUserBio;
    @BindView(R.id.profile_placeholder)
    RelativeLayout mProfilePlaceHolder;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.user_photo_img)
    ImageView mProfileImage;
    @BindView(R.id.call_img)
    ImageView mCallImg;
    @BindView(R.id.send_email_img)
    ImageView mSendEmailImg;
    @BindView(R.id.link_vk_img)
    ImageView mLinkVkImg;
    @BindView(R.id.link_git_img)
    ImageView mLinkGitImg;
    @BindView(R.id.user_profile_content)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.user_value_raiting)
    TextView mUserValueRaiting;
    @BindView(R.id.user_value_code_lines)
    TextView mUserValueCodeLines;
    @BindView(R.id.user_value_projects)
    TextView mUserValueProjects;
    private CircularImageView mCircularDrawerHeaderAvatar;
    private TextView mUserEmailDrawerHeader;
    private TextView mUserNameDrawerHeader;
    private List<TextView> mUserValuesViews;

    /**
     * Старт 1
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDataManager = DataManager.getInstance();

        mUserVK.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith(getString(R.string.vk_com))) {
                    mUserVK.setText(getString(R.string.vk_com));
                    Selection.setSelection(mUserVK.getText(), mUserVK.getText().length());
                }
            }
        });
        mUserGit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith(getString(R.string.github_com))) {
                    mUserGit.setText(getString(R.string.github_com));
                    Selection.setSelection(mUserGit.getText(), mUserGit.getText().length());
                }
            }
        });
        mUserMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!checkEmail()) showToast(getString(R.string.wrong_email));
            }
        });
        mUserPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!checkPhoneNumber()) showToast(getString(R.string.wrong_phone_number));
            }
        });

        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserVK);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserBio);

        mUserValuesViews = new ArrayList<>();
        mUserValuesViews.add(mUserValueRaiting);
        mUserValuesViews.add(mUserValueCodeLines);
        mUserValuesViews.add(mUserValueProjects);

        mFab.setOnClickListener(this);
        mProfilePlaceHolder.setOnClickListener(this);
        mCallImg.setOnClickListener(this);
        mSendEmailImg.setOnClickListener(this);
        mLinkVkImg.setOnClickListener(this);
        mLinkGitImg.setOnClickListener(this);

        setupToolbar();
        setupDrawer();
        initUserInfo();
        initFromServerUserValue();


        Picasso.with(this)
                .load(mDataManager.getPreferenceManager().loadUserPhoto())
                .placeholder(R.drawable.userphoto)
                .into(mProfileImage);

        if (savedInstanceState == null) {
            changeEditMode(mCurrentEditMode);
        } else {
            mCurrentEditMode = savedInstanceState.getBoolean(ConstantManager.EDIT_MODE_KEY, false);
            changeEditMode(mCurrentEditMode);
        }
    }

    /**
     * Открывает Drawer layout
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Старт 2
     */
    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    /**
     * Старт 3
     */
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    /**
     * Выполняется при сворачивании
     */
    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        saveUserInfo();
        super.onPause();
    }

    /**
     * Выполняется при закрытии
     */
    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    /**
     * Выполняется при запуске после onStop
     */
    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart");
        super.onRestart();
    }

    /**
     * Обрабатывает клики
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick");
        switch (v.getId()) {
            case R.id.fab:
                if (isValidUserProfileContent()) {
                    mCurrentEditMode = !mCurrentEditMode;
                    changeEditMode(mCurrentEditMode);
                } else {
                    showToast(getString(R.string.error_input_data));
                }
                break;
            case R.id.profile_placeholder:
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
            case R.id.call_img:
                makeActiv(Uri.parse("tel:" + mUserPhone.getText().toString()));
                break;
            case R.id.send_email_img:
                makeActiv(Uri.parse("mailto:" + mUserMail.getText().toString()));
                break;
            case R.id.link_vk_img:
                makeActiv(Uri.parse("https://" + mUserVK.getText().toString()));
                break;
            case R.id.link_git_img:
                makeActiv(Uri.parse("https://" + mUserGit.getText().toString()));
                break;
        }
    }

    /**
     * Показывает snackBar
     *
     * @param message
     */
    private void showSnackBar(String message) {
        Log.d(TAG, "showSnackBar");
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Устанавливает тулбар
     */
    private void setupToolbar() {
        Log.d(TAG, "setupToolbar");
        setSupportActionBar(mToolbar);
        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbarLayout.getLayoutParams();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Устанавливает Drawer layout
     */
    private void setupDrawer() {
        Log.d(TAG, "setupDrawer");
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item == navigationView.getMenu().getItem(1)) {
                    Intent intent = new Intent(MainActivity.this, UserListActivity.class);
                    startActivity(intent);
                    item.setChecked(false);
                }
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        mCircularDrawerHeaderAvatar = (CircularImageView) navigationView.getHeaderView(0).findViewById(R.id.drawer_header_avatar);
        mUserEmailDrawerHeader = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawer_header_user_email_txt);
        mUserEmailDrawerHeader.setText(mDataManager.getPreferenceManager().loadUserProfileData().get(1));
        Picasso.with(this)
                .load(mDataManager.getPreferenceManager().loadUserAvatar())
                .placeholder(R.drawable.user_bg)
                .into(mCircularDrawerHeaderAvatar);
    }

    /**
     * Переключает режим true-редактирование/просмотр-false
     *
     * @param mode
     */
    private void changeEditMode(boolean mode) {
        Log.d(TAG, "changeEditMode");
        for (View view : mUserInfoViews) {
            view.setEnabled(mode);
            view.setFocusable(mode);
            view.setFocusableInTouchMode(mode);
        }
        if (mode) {
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            showProfilePlaceHolder();
            lockToolbar();
            mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
            mUserPhone.requestFocus();
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0, 0);
        } else {
            mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.color_white));
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            hideProfilePlaceHolder();
            unLockToolbar();
        }
    }

    /**
     * Сохраняет статус
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putBoolean(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    /**
     * Получение результата из другой Activity (фото из камеры или картинки из галереи)
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);
                }
                break;
        }
    }

    /**
     * Загружет фото в профиль
     *
     * @param selectedImage
     */
    private void insertProfileImage(Uri selectedImage) {
        Log.d(TAG, "insertProfileImage");
        Picasso.with(this).load(selectedImage).into(mProfileImage);
        mDataManager.getPreferenceManager().saveUserPhoto(selectedImage);
    }

    /**
     * Загружает данные пользователя
     */
    private void initUserInfo() {
        Log.d(TAG, "initUserInfo");
        List<String> userData = mDataManager.getPreferenceManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));
        }
    }

    /**
     * Сохраняет данные пользователя
     */
    private void saveUserInfo() {
        Log.d(TAG, "saveUserInfo");
        List<String> userData = new ArrayList<>();
        for (EditText editText : mUserInfoViews) {
            userData.add(editText.getText().toString());
        }
        mDataManager.getPreferenceManager().saveUserProfileData(userData);
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

    /**
     * Загружает фото из галереи
     */
    private void loadPhotoFromGallery() {
        Log.d(TAG, "loadPhotoFromGallery");
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.user_profile_chose_message)), ConstantManager.REQUEST_GALLERY_PICTURE);
    }

    /**
     * Записывает данные из камеры в файл
     */
    private void loadPhotoFromCamera() {
        Log.d(TAG, "loadPhotoFromCamera");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                showToast(getString(R.string.error_make_photo));
                loadPhotoFromGallery();
            }
            if (mPhotoFile != null) {
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_REQUEST_PERMITION_CODE);
        }
        Snackbar.make(mCoordinatorLayout, R.string.request_correct_work_programm, Snackbar.LENGTH_LONG).setAction(R.string.solve_answer, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApplicationSettings();
            }
        }).show();
    }

    /**
     * Обрабатывает результат запроса permissions
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");
        if (requestCode == ConstantManager.CAMERA_REQUEST_PERMITION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast(String.format(getString(R.string.permission_to__received), permissions[0]));
            }
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                showToast(String.format(getString(R.string.permission_to__received), permissions[1]));
            }
        }
    }

    /**
     * Скрывает layout для profileUserPhoto
     */
    private void hideProfilePlaceHolder() {
        Log.d(TAG, "hideProfilePlaceHolder");
        mProfilePlaceHolder.setVisibility(View.GONE);
    }

    /**
     * Показывает layout для profileUserPhoto
     */
    private void showProfilePlaceHolder() {
        Log.d(TAG, "showProfilePlaceHolder");
        mProfilePlaceHolder.setVisibility(View.VISIBLE);
    }

    /**
     * Блокирует сворачивание верхнего тулбара в режиме редактирования
     */
    private void lockToolbar() {
        Log.d(TAG, "lockToolbar");
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbarLayout.setLayoutParams(mAppBarParams);
    }

    /**
     * Делает верхний тулбар подвижным
     */
    private void unLockToolbar() {
        Log.d(TAG, "unLockToolbar");
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbarLayout.setLayoutParams(mAppBarParams);
    }

    /**
     * Создает диалоговые окна
     *
     * @param id
     * @return
     */
    @Nullable
    @Override
    protected Dialog onCreateDialog(int id) {
        Log.d(TAG, "onCreateDialog");
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {getString(R.string.user_profile_dialog_gallery), getString(R.string.user_profile_dialog_camera), getString(R.string.user_profile_dialog_cancel)};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_profile_dialog_title));
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choiseItem) {
                        switch (choiseItem) {
                            case 0:
                                loadPhotoFromGallery();
                                break;
                            case 1:
                                loadPhotoFromCamera();
                                break;
                            case 2:
                                dialog.cancel();
                        }
                    }
                });
                return builder.create();
            default:
                return null;
        }
    }

    /**
     * Создает файл для фото
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        Log.d(TAG, "createImageFile");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());
        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        return image;
    }

    /**
     * Открывает свойства приложения
     */
    public void openApplicationSettings() {
        Log.d(TAG, "openApplicationSettings");
        Intent appSettingIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingIntent, ConstantManager.PERMITION_REQUEST_SETTINGS_CODE);
    }

    /**
     * Выполняет действие
     */
    private void makeActiv(Uri uri) {
        Log.d(TAG, "makeActiv");
        if (mCurrentEditMode == false) {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    /**
     * Проверка на корректность телефона и почты
     *
     * @return
     */
    private boolean isValidUserProfileContent() {
        Log.d(TAG, "checkInputUserProfileContent");
        boolean isValidData;
        isValidData = checkPhoneNumber() && checkEmail();
        return isValidData;
    }

    /**
     * Проверяет валидность номера
     *
     * @return
     */
    private boolean checkPhoneNumber() {
        Log.d(TAG, "checkPhoneNumber");
        try {
            String number = Uri.parse(mUserPhone.getText().toString()).toString();
            if (!number.startsWith("+")) return false;
            if (number.lastIndexOf("+") != 0) return false;
            if (number.length() < 11 || number.length() > 20) {         //проверка на число символов
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Проверяет валидность почты
     *
     * @return
     */
    private boolean checkEmail() {
        Log.d(TAG, "checkEmail");
        String email = mUserMail.getText().toString();
        String[] emailss = email.split("@");
        if (emailss.length != 2) {//проверка на симоволы до и после @
            return false;
        }
        if (emailss[0].length() < 3) return false; //проверка на число символов перед @
        if (!emailss[1].contains(".")) return false;//проверка на наличие "."
        int lastT = emailss[1].lastIndexOf(".");
        if ((emailss[1].length() - 1 - lastT) < 2) return false;//на символы после "."
        if (lastT < 2) return false;//на символы перед "."
        return true;
    }

    private void initFromServerUserValue() {
        Log.d(TAG, "initFromServerUserValue");
        List<String> userData = mDataManager.getPreferenceManager().loadUserProfileValues();
        for (int i = 0; i < userData.size(); i++) {
            mUserValuesViews.get(i).setText(userData.get(i));
        }
    }
}
