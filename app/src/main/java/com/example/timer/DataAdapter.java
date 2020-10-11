package com.example.timer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataHolder> implements  ItemTouchHelperAdapter {
    Context context;
    ArrayList<TimerSequence> timerList;

    private OnTimerListener mOnTimerListener;
    private OnLongTimerListener mOnLongClickListener;
    private OnItemDelete mOnItemDelete;
    private SQLiteHelper helper;

    public DataAdapter(Context context, ArrayList<TimerSequence> timerList,
                       OnTimerListener onTimerListener,
                       OnLongTimerListener onLongClickListener,
                       OnItemDelete onItemDelete) {
        this.context = context;
        this.timerList = timerList;
        this.mOnTimerListener = onTimerListener;
        this.mOnLongClickListener = onLongClickListener;
        this.mOnItemDelete = onItemDelete;
        this.helper = new SQLiteHelper(context);
    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new DataHolder(view, mOnTimerListener, mOnLongClickListener, mOnItemDelete, context, timerList);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DataHolder holder, final int position) {
        TimerSequence timerSequence = timerList.get(position);
        holder.title.setText("Timer name: " + timerSequence.getTitle());
        holder.preparationTime.setText("Preparation time: " + timerSequence.getPreparationTime());
        holder.workingTime.setText("Working time: " + timerSequence.getWorkingTime());
        holder.restTime.setText("Rest time: " + timerSequence.getRestTime());
        holder.cyclesAmount.setText("Cycles amount: " + timerSequence.getCyclesAmount());
        holder.setsAmount.setText("Sets amount: " + timerSequence.getSetsAmount());
        holder.betweenSetsRest.setText("Between sets rest: " + timerSequence.getBetweenSetsRest());
        holder.cooldownTime.setText("Cooldown time: " + timerSequence.getCooldownTime());
        timerList = helper.getList();
        int color = helper.getColorByTimerID(timerList.get(position).getId());
        holder.layout.setBackgroundColor(color);
        holder.cardView.setCardBackgroundColor(color);

    }

    @Override
    public int getItemCount() {
        return timerList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(timerList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(timerList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);

    }

    @Override
    public void onItemDismiss(int position) {
        timerList.remove(position);
        notifyItemRemoved(position);
    }

    public static class DataHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView title, preparationTime, workingTime, restTime, cyclesAmount, setsAmount, betweenSetsRest,
                cooldownTime;
        LinearLayout layout;
        CardView cardView;
        OnTimerListener onTimerListener;
        OnLongTimerListener onLongClickListener;
        ImageButton deleteButton;

        public DataHolder(View itemView, final OnTimerListener onTimerListener,
                          final OnLongTimerListener onLongClickListener, final OnItemDelete onItemDelete,
                          final Context context, final ArrayList<TimerSequence> timer) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            layout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            deleteButton = (ImageButton) itemView.findViewById(R.id.editButton);
            title = itemView.findViewById(R.id.timerTitleText);
            preparationTime = itemView.findViewById(R.id.preparationTimeText);
            workingTime = itemView.findViewById(R.id.workingTimeText);
            restTime = itemView.findViewById(R.id.restTimeText);
            cyclesAmount = itemView.findViewById(R.id.cyclesAmountText);
            setsAmount = itemView.findViewById(R.id.setsAmountText);
            betweenSetsRest = itemView.findViewById(R.id.betweenSetsRestText);
            cooldownTime = itemView.findViewById(R.id.cooldownTimeText);
            this.onTimerListener = onTimerListener;
            this.onLongClickListener = onLongClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final PopupMenu popupMenu = new PopupMenu(context, deleteButton);
                    try {
                        Field[] fields = popupMenu.getClass().getDeclaredFields();
                        for (Field field : fields) {
                            if ("mPopup".equals(field.getName())) {
                                field.setAccessible(true);
                                Object menuPopupHelper = field.get(popupMenu);

                                Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());

                                Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                                setForceIcons.invoke(menuPopupHelper, true);
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.deleteItem: {
                                    timer.remove(getAdapterPosition());
                                    onItemDelete.onItemDelete(getAdapterPosition());
                                    return true;
                                }
                                case R.id.editItem: {
                                    onTimerListener.onTimerClick(getAdapterPosition());
                                    return true;
                                }
                                default: {
                                    return false;
                                }
                            }
                        }
                    });
                    popupMenu.show();
                }
            });
        }

        @Override
        public void onClick(View view) {
            onTimerListener.onTimerClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            onLongClickListener.onLongClick(getAdapterPosition());
            return true;
        }
    }

    interface OnTimerListener {
        void onTimerClick(int position);
    }

    interface OnItemDelete {
        void onItemDelete(int position);
    }

    interface OnLongTimerListener {
        void onLongClick(int position);
    }
}
