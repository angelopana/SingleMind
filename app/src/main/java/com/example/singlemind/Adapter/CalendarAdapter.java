package com.example.singlemind.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.singlemind.Controllers.DBManager;
import com.example.singlemind.Model.Event;
import com.example.singlemind.R;
import com.example.singlemind.UI.IUpdatable;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.EventViewHolder>{

    private List<Event> mRecyclerEvents;

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

    public CalendarAdapter(List<Event> events) {
        mRecyclerEvents = events;
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
        holder.mEventName.setText(mRecyclerEvents.get(position).getmEventName());
        //holder.mEventType.setText(mRecyclerEvents.get(position).getmEventType());
        holder.mEventTime.setText(mRecyclerEvents.get(position).getmEventTime());
        holder.mEventContent.setText(mRecyclerEvents.get(position).getmEventDescription());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Delete Event");
                builder.setMessage("You will not be able to recover this event");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Ok",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete event
                        Event e = mRecyclerEvents.get(position);
                        DBManager.getInstance().deleteEvent(e, new IUpdatable() {
                            @Override
                            public void onUpdateSuccess() {
                                //successfully deleted - remove from view
                                mRecyclerEvents.remove(position);
                                notifyItemRemoved(position);
                                Snackbar.make(view, "Item Removed", Snackbar.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onUpdateFailed() {
                                //successfully destroyed
                                Snackbar.make(view, "Deletion Failed- Check Network Connection", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mRecyclerEvents.size();
    }
}
