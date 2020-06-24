package com.kopisenja.floodmonitoring.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.kopisenja.floodmonitoring.R;
import com.kopisenja.floodmonitoring.base.Notification;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private ClickHandler mClickHandler;
    private Context mContext;
    private ArrayList<Notification> mData;
    private ArrayList<String> mDataId;
    private ArrayList<String> mSelectedId;
    private View mEmptyView;

    public NotificationAdapter(Context context, ArrayList<Notification> data, ArrayList<String> dataId, View emptyView, ClickHandler handler) {
        mContext = context;
        mData = data;
        mDataId = dataId;
        mEmptyView = emptyView;
        mClickHandler = handler;
        mSelectedId = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_notification, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = mData.get(position);

        //initial tmp shared pref
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences("NOTIFICATION_SESSION", mContext.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        final String key = mDataId.get(position);

        holder.nameTextView.setText(notification.getName());

        //initial switch before show
        int checkSwitch = sharedPreferences.getInt(key, 0);
        if (checkSwitch == 1) {
            holder.notificationSwitch.setChecked(true);
        } else {
            holder.notificationSwitch.setChecked(false);
        }

        //switch onchange
        holder.notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    // active topic
                    FirebaseMessaging.getInstance().subscribeToTopic("FLOOD_" + key);

                    // put tmp key value
                    editor.putInt(key, 1);
                    editor.commit();
                } else {
                    // deactive topic
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("FLOOD_" + key);

                    // put tmp key value
                    editor.putInt(key, 0);
                    editor.commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void updateEmptyView() {
        if (mData.size() == 0)
            mEmptyView.setVisibility(View.VISIBLE);
        else
            mEmptyView.setVisibility(View.GONE);
    }

    public void toggleSelection(String dataId) {
        if (mSelectedId.contains(dataId))
            mSelectedId.remove(dataId);
        else
            mSelectedId.add(dataId);

        notifyDataSetChanged();
    }

    public int selectionCount() {
        return mSelectedId.size();
    }

    public void resetSelection() {
        mSelectedId = new ArrayList<>();
        notifyDataSetChanged();
    }

    public ArrayList<String> getSelectedId() {
        return mSelectedId;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final TextView nameTextView;
        final Switch notificationSwitch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.textview_title_list_notification);
            notificationSwitch = itemView.findViewById(R.id.switch_notification);

            itemView.setFocusable(false);
            itemView.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public interface ClickHandler {
        void onItemClick(int position);
        boolean onItemLongClick(int position);
    }
}
