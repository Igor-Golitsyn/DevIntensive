package com.softdesign.devintensive.data.storage.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Игорь on 15.07.2016.
 */
public class UserDTO implements Parcelable {
    private static final String TAG = ConstantManager.TAG_PREFIX + "UserDTO";
    private String mPhoto;
    private String mFullName;
    private String mRaiting;
    private String mCodeLines;
    private String mProjects;
    private String mBio;
    private List<String> mRepositories;

    public List<String> getRepositories() {
        Log.d(TAG, "getRepositories");
        return mRepositories;
    }

    public String getPhoto() {
        Log.d(TAG, "getPhoto");
        return mPhoto;
    }

    public String getRaiting() {
        Log.d(TAG, "getRaiting");
        return mRaiting;
    }

    public String getCodeLines() {
        Log.d(TAG, "getCodeLines");
        return mCodeLines;
    }

    public String getProjects() {
        Log.d(TAG, "getProjects");
        return mProjects;
    }

    public String getBio() {
        Log.d(TAG, "getBio");
        return mBio;
    }

    public String getFullName() {
        Log.d(TAG, "getFullName");
        return mFullName;
    }

    public UserDTO(User userData) {
        Log.d(TAG, "UserDTO");
        List<String> repoLink = new ArrayList<>();
        mPhoto = userData.getPhoto();
        mFullName = userData.getFullName();
        mRaiting = String.valueOf(userData.getRating());
        mCodeLines = String.valueOf(userData.getCodeLines());
        mProjects = String.valueOf(userData.getProjects());
        mBio = userData.getBio();

        for (Repository gitLink : userData.getRepositories()) {
            repoLink.add(gitLink.getRepositoryName());
        }
        mRepositories = repoLink;
    }

    protected UserDTO(Parcel in) {
        Log.d(TAG, "UserDTO");
        mPhoto = in.readString();
        mFullName = in.readString();
        mRaiting = in.readString();
        mCodeLines = in.readString();
        mProjects = in.readString();
        mBio = in.readString();
        if (in.readByte() == 0x01) {
            mRepositories = new ArrayList<String>();
            in.readList(mRepositories, String.class.getClassLoader());
        } else {
            mRepositories = null;
        }
    }

    @Override
    public int describeContents() {
        Log.d(TAG, "describeContents");
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.d(TAG, "writeToParcel");
        dest.writeString(mPhoto);
        dest.writeString(mFullName);
        dest.writeString(mRaiting);
        dest.writeString(mCodeLines);
        dest.writeString(mProjects);
        dest.writeString(mBio);
        if (mRepositories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mRepositories);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserDTO> CREATOR = new Parcelable.Creator<UserDTO>() {
        @Override
        public UserDTO createFromParcel(Parcel in) {
            return new UserDTO(in);
        }

        @Override
        public UserDTO[] newArray(int size) {
            return new UserDTO[size];
        }
    };
}
