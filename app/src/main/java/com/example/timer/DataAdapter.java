package com.example.timer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataHolder>
        implements ItemTouchHelperAdapter{

    Context context;
    ArrayList<TimerSequence> timerList;

    private final OnTimerListener mOnTimerListener;
    private final OnItemDeleteListener mOnDeleteListener;
    private final SQLiteHelper helper;

    public void notifyData(ArrayList<TimerSequence> list){
        timerList = list;
        this.notifyDataSetChanged();
    }

    public DataAdapter(Context context, ArrayList<TimerSequence> timerList,
                       OnTimerListener onTimerListener, OnItemDeleteListener mOnDeleteListener) {
        this.context = context;
        this.timerList = timerList;
        this.mOnTimerListener = onTimerListener;
        this.helper = new SQLiteHelper(context);
        this.mOnDeleteListener = mOnDeleteListener;
    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new DataHolder(view, mOnTimerListener, mOnDeleteListener);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final DataHolder holder, final int position) {
        TimerSequence timerSequence = timerList.get(position);
        Resources resources = context.getResources();
        holder.title.setText(resources.getString(R.string.training_name) + ": " + timerSequence.getTitle());
        holder.preparationTime.setText(resources.getString(R.string.preparing_time) + ": " + timerSequence.getPreparationTime());
        holder.workingTime.setText(resources.getString(R.string.working_time) + ": " + timerSequence.getWorkingTime());
        holder.restTime.setText(resources.getString(R.string.rest_time) + ": " + timerSequence.getRestTime());
        holder.cyclesAmount.setText(resources.getString(R.string.cycles_amount) + ": " + timerSequence.getCyclesAmount());
        holder.setsAmount.setText(resources.getString(R.string.sets_amount) + ": " + timerSequence.getSetsAmount());
        holder.betweenSetsRest.setText(resources.getString(R.string.rest_between_sets) + ": " + timerSequence.getBetweenSetsRest());
        holder.cooldownTime.setText(resources.getString(R.string.cooldown_time) + ": " + timerSequence.getCooldownTime());
        holder.layout.setBackgroundColor(timerSequence.getColor());
        holder.cardView.setCardBackgroundColor(timerSequence.getColor());
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


    public class DataHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView title, preparationTime, workingTime, restTime, cyclesAmount, setsAmount, betweenSetsRest,
                cooldownTime;

        LinearLayout layout;
        CardView cardView;
        OnTimerListener onTimerListener;
        OnLongTimerListener onLongClickListener;
        OnItemDeleteListener onItemDeleteListener;
        ImageButton editButton;
        ImageButton playButton;

        public DataHolder(View itemView, OnTimerListener mOnTimerListener, OnItemDeleteListener mOnDeleteListener) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            layout = itemView.findViewById(R.id.linearLayout);
            editButton = itemView.findViewById(R.id.editButton);
            playButton = itemView.findViewById(R.id.playButton);

            title = itemView.findViewById(R.id.timerTitleText);
            preparationTime = itemView.findViewById(R.id.preparationTimeText);
            workingTime = itemView.findViewById(R.id.workingTimeText);
            restTime = itemView.findViewById(R.id.restTimeText);
            cyclesAmount = itemView.findViewById(R.id.cyclesAmountText);
            setsAmount = itemView.findViewById(R.id.setsAmountText);
            betweenSetsRest = itemView.findViewById(R.id.betweenSetsRestText);
            cooldownTime = itemView.findViewById(R.id.cooldownTimeText);
            onTimerListener = mOnTimerListener;
            onItemDeleteListener = mOnDeleteListener;
            itemView.setOnClickListener(this);
            //itemView.setOnLongClickListener((View.OnLongClickListener)onLongClickListener);

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ActiveActivity.class);

                   intent.putExtra("id", timerList.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });

            final PopupMenu.OnMenuItemClickListener onMenuListener = new PopupMenu.OnMenuItemClickListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.deleteItem: {
                            int position = getAdapterPosition();
                            onItemDeleteListener.onItemDelete(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());
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
            };

            View.OnClickListener onEditClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popupMenu = new PopupMenu(editButton.getContext(), v);
                    popupMenu.setOnMenuItemClickListener(onMenuListener);
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                    showIcons(popupMenu);
                    popupMenu.show();

                }
            };

            editButton.setOnClickListener(onEditClick);
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

    private void showIcons(PopupMenu popupMenu){
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon",
                            boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    interface OnTimerListener {
        void onTimerClick(int position);
    }

    interface OnLongTimerListener {
        void onLongClick(int position);
    }

    interface OnItemDeleteListener {
        void onItemDelete(int position);
    }

}
