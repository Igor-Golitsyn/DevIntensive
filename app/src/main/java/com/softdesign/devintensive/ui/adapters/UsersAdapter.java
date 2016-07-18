package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.ui.views.AspectRatioImageView;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Игорь on 14.07.2016.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private static final String TAG = ConstantManager.TAG_PREFIX + "UsersAdapter";
    private List<UserListRes.UserData> mUsers;
    private Context mContext;
    private UserViewHolder.CustomClickListener mCustomClickListener;

    public UsersAdapter(List<UserListRes.UserData> users, UserViewHolder.CustomClickListener listener) {
        Log.d(TAG, "UsersAdapter");
        mUsers = users;
        mCustomClickListener = listener;
    }

    @Override
    public UsersAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);
        return new UserViewHolder(convertView,mCustomClickListener);
    }

    @Override
    public void onBindViewHolder(UsersAdapter.UserViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        UserListRes.UserData user = mUsers.get(position);
        Picasso.with(mContext)
                .load(user.getPublicInfo().getPhoto())
                .placeholder(mContext.getResources().getDrawable(R.drawable.user_bg))
                .error(mContext.getResources().getDrawable(R.drawable.user_bg))
                .into(holder.userPhoto);
        holder.mFullName.setText(user.getFullName());
        holder.mRating.setText(String.valueOf(user.getProfileValues().getRaiting()));
        holder.mCodeLines.setText(String.valueOf(user.getProfileValues().getLinesCode()));
        holder.mProjects.setText(String.valueOf(user.getProfileValues().getProjects()));
        if (user.getPublicInfo().getBio() == null || user.getPublicInfo().getBio().isEmpty()) {
            holder.mBio.setVisibility(View.GONE);
        } else {
            holder.mBio.setVisibility(View.VISIBLE);
            holder.mBio.setText(user.getPublicInfo().getBio());
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount");
        return mUsers.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final String TAG = ConstantManager.TAG_PREFIX + "UserViewHolder";
        protected AspectRatioImageView userPhoto;
        protected TextView mFullName, mRating, mCodeLines, mProjects, mBio;
        protected Button mShowMore;
        private CustomClickListener mListener;

        public UserViewHolder(View itemView, CustomClickListener listener) {
            super(itemView);
            Log.d(TAG, "UserViewHolder");
            this.mListener = listener;
            userPhoto = (AspectRatioImageView) itemView.findViewById(R.id.user_photo);
            mFullName = (TextView) itemView.findViewById(R.id.user_full_name_txt);
            mRating = (TextView) itemView.findViewById(R.id.rating_txt);
            mCodeLines = (TextView) itemView.findViewById(R.id.code_lines_txt);
            mProjects = (TextView) itemView.findViewById(R.id.projects_txt);
            mBio = (TextView) itemView.findViewById(R.id.bio_txt);
            mShowMore = (Button) itemView.findViewById(R.id.more_info_btn);

            mShowMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener!=null){
                mListener.onUserItemClickListener(getAdapterPosition());
            }
        }

        public interface CustomClickListener {
            void onUserItemClickListener(int position);
        }
    }
}
