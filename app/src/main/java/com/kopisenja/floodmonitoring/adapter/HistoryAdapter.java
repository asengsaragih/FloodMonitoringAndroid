package com.kopisenja.floodmonitoring.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kopisenja.floodmonitoring.R;
import com.kopisenja.floodmonitoring.base.Flood;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private ClickHandler mClickHandler;
    private Context mContext;
    private ArrayList<Flood> mData;
    private ArrayList<String> mDataId;
    private ArrayList<String> mSelectedId;
    private View mEmptyView;

    public HistoryAdapter(Context context, ArrayList<Flood> data, ArrayList<String> dataId, View emptyView, ClickHandler handler) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_history, parent, false);
        return new HistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Flood flood = mData.get(position);

        final int category = flood.getCategory();

        if (category == 1) {
            holder.categoryTextView.setText(mContext.getString(R.string.category_normal));
            holder.categoryTextView.setTextColor(mContext.getResources().getColor(R.color.colorTextCategoryNormal));
        } else if (category == 2) {
            holder.categoryTextView.setText(mContext.getString(R.string.category_standby));
            holder.categoryTextView.setTextColor(mContext.getResources().getColor(R.color.colorTextCategoryStandBy));
        } else {
            holder.categoryTextView.setText(mContext.getString(R.string.category_danger));
            holder.categoryTextView.setTextColor(mContext.getResources().getColor(R.color.colorTextCategoryDanger));
        }

        holder.locationTextView.setText(flood.getLocation());
        holder.dateTextView.setText(flood.getDate() + " " + flood.getTime());
        holder.detailTextView.setText(flood.getLevel() + " " + flood.getDebit());

        holder.boxConstraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.TransparentDialogCustom);
                View detailView = View.inflate(mContext, R.layout.custom_dialog_history, null);

                TextView statusTextview = detailView.findViewById(R.id.textView_dialog_history_status);
                TextView lokasiTextview = detailView.findViewById(R.id.textView_dialog_history_lokasi);
                TextView tanggalTextview = detailView.findViewById(R.id.textView_dialog_history_tanggal);
                TextView waktuTextview = detailView.findViewById(R.id.textView_dialog_history_waktu);
                TextView tinggiTextview = detailView.findViewById(R.id.textView_dialog_history_tinggi);
                TextView debitTextview = detailView.findViewById(R.id.textView_dialog_history_debit);

                String text = "";

                if (category == 1) {
                    text = "<font color='#4F4F4F'>" + "Status \t\t\t\t: " + "</font>" + "<font color='#27AE60'><b>" + mContext.getString(R.string.category_normal) + "</font></b>";
                } else if (category == 2) {
                    text = "<font color='#4F4F4F'>" + "Status \t\t\t\t: " + "</font>" + "<font color='#F2C94C'><b>" + mContext.getString(R.string.category_standby) + "</font></b>";
                } else {
                    text = "<font color='#4F4F4F'>" + "Status \t\t\t\t: " + "</font>" + "<font color='#EB5757'><b>" + mContext.getString(R.string.category_danger) + "</font></b>";
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    statusTextview.setText(Html.fromHtml(text,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
                } else {
                    statusTextview.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                }


                lokasiTextview.setText("Lokasi \t\t\t\t: " + flood.getLocation());
                tanggalTextview.setText("Tanggal \t\t\t: " + flood.getDate());
                waktuTextview.setText("Waktu \t\t\t\t: " + flood.getTime());
                tinggiTextview.setText("Tinggi \t\t\t\t\t: " + flood.getLevel());

                String[] debit = flood.getDebit().split(" ");
                debitTextview.setText("Debit \t\t\t\t\t: " + debit[0] + " Liter/menit");


                builder.setView(detailView);
                builder.show();
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

        final TextView categoryTextView;
        final TextView locationTextView;
        final TextView dateTextView;
        final TextView detailTextView;
        final ConstraintLayout boxConstraint;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryTextView = itemView.findViewById(R.id.textView_history_category);
            locationTextView = itemView.findViewById(R.id.textView_history_location);
            dateTextView = itemView.findViewById(R.id.textView_history_date);
            detailTextView = itemView.findViewById(R.id.textView_history_detail);
            boxConstraint = itemView.findViewById(R.id.constraint_list_item_history);

            // focusable sengaja di false biar ngga bisa di klik
            itemView.setFocusable(false);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
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
