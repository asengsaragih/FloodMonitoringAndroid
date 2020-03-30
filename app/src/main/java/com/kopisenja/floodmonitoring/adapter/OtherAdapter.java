package com.kopisenja.floodmonitoring.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kopisenja.floodmonitoring.R;
import com.kopisenja.floodmonitoring.activity.HistoryActivity;
import com.kopisenja.floodmonitoring.base.OtherMarker;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class OtherAdapter extends RecyclerView.Adapter<OtherAdapter.ViewHolder> {

    private ClickHandler mClickHandler;
    private Context mContext;
    private ArrayList<OtherMarker> mData;
    private ArrayList<String> mDataId;
    private ArrayList<String> mSelectedId;
    private View mEmptyView;

    public OtherAdapter(Context context, ArrayList<OtherMarker> data, ArrayList<String> dataId, View emptyView, ClickHandler handler) {
        mContext = context;
        mData = data;
        mDataId = dataId;
        mEmptyView = emptyView;
        mClickHandler = handler;
        mSelectedId = new ArrayList<>();
    }

    public void updateEmptyView() {
        if (mData.size() == 0)
            mEmptyView.setVisibility(View.VISIBLE);
        else
            mEmptyView.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_other_location, parent, false);
        return new OtherAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OtherMarker otherMarker = mData.get(position);
        holder.titleTextView.setText(otherMarker.getLocation());
        holder.boxConstraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, HistoryActivity.class);

                if (otherMarker.getLocation().equals("Bojongsoang")) {
                    intent.putExtra("CODE_LOCATION", "1");
                    mContext.startActivity(intent);
                } else if (otherMarker.getLocation().equals("Radio")) {
                    intent.putExtra("CODE_LOCATION", "2");
                    mContext.startActivity(intent);
                }
            }
        });

        holder.itemView.setSelected(mSelectedId.contains(mDataId.get(position)));
    }

    @Override
    public int getItemCount() {
        return mData.size();
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

        final TextView titleTextView;
        final ConstraintLayout boxConstraint;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            boxConstraint = itemView.findViewById(R.id.constraint_list_item_other);
            titleTextView = itemView.findViewById(R.id.textview_title_other_list);

            // focusable sengaja di false biar ngga bisa di klik
            itemView.setFocusable(false);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
    public interface ClickHandler {
        void onItemClick(int position);
        boolean onItemLongClick(int position);
    }
}
