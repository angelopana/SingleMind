package com.example.singlemind;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.EventViewHolder>{

    private List<Event> recyclerEvents;

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        CardView mEventCard;
        TextView mEventType, mEventName, mEventTime, mEventContent;

        EventViewHolder(View itemView) {
            super(itemView);
            mEventCard = itemView.findViewById(R.id.card_event);
            mEventType = itemView.findViewById(R.id.event_type);
            mEventName = itemView.findViewById(R.id.event_name);
            mEventTime = itemView.findViewById(R.id.event_time);
            mEventContent = itemView.findViewById(R.id.event_content);
        }
    }

    CalendarAdapter(List<Event> events) {
        recyclerEvents = events;
    }

    @NonNull
    @Override
    public CalendarAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        EventViewHolder evh = new EventViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarAdapter.EventViewHolder holder, int position) {
        holder.mEventName.setText(recyclerEvents.get(position).getmEventName());
        holder.mEventType.setText(recyclerEvents.get(position).getmEventType());
        holder.mEventTime.setText(recyclerEvents.get(position).getmEventTime());
        holder.mEventContent.setText(recyclerEvents.get(position).getmEventContent());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return recyclerEvents.size();
    }
}
