package se.tuppload.android.satstrainingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.tuppload.android.satstrainingapp.Holders.OwnViewHolder;
import se.tuppload.android.satstrainingapp.Holders.PreviousViewHolder;
import se.tuppload.android.satstrainingapp.Holders.BookedViewHolder;

public class TrainingListAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private static ArrayList<Activity> activities = new ArrayList<>();
    private HashMap<String, Center> centers = new HashMap<>();
    private LayoutInflater inflater;
    private final String[] swedish_days = {"", "Måndag", "Tisdag", "Onsdag", "Torsdag", "Fredag", "Lördag", "Söndag"};
    private final String[] swedish_months = {"", "Januari", "Februari", "Mars", "April", "Maj", "Juni", "Juli", "Augusti", "September", "Oktober", "November", "December"};
    private DateTime date;
    private DateTime currentDateTime = new DateTime();
    private DateTime activityDate;
    private static final int VIEWTYPE_COUNT = 3;
    private static final int PREVIOUS = 0;
    private static final int BOOKED = 1;
    private static final int OWN = 2;
//    private static final int


    public TrainingListAdapter(MainActivity activity, ArrayList<Activity> activities, HashMap centers) {
        inflater = LayoutInflater.from(activity);
        this.activities = activities;
        this.centers = centers;
    }

    @Override
    public int getViewTypeCount() {
        return VIEWTYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {

        activityDate = DateTime.parse(getItem(position).date);
        if (getItem(position).status.equals("COMPLETE") || activityDate.isBeforeNow()) {
            return PREVIOUS;
        } else {
            if (getItem(position).type.equals("GROUP")) {
                return BOOKED;
            } else {
                return OWN;
            }
        }
    }

    @Override
    public int getCount() {
        return activities.size();
    }

    @Override
    public Activity getItem(int position) {
        return activities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        OwnViewHolder ownHolder;
        PreviousViewHolder previousHolder;
        BookedViewHolder bookedHolder;

        int viewType = getItemViewType(position);
        switch (viewType) {
            case PREVIOUS:
                if (view == null) {
                    view = inflater.inflate(R.layout.activity_previous, parent, false);
                    previousHolder = new PreviousViewHolder();
                    previousHolder.type = (TextView) view.findViewById(R.id.previous_type);
                    previousHolder.date = (TextView) view.findViewById(R.id.previous_date);
//                    switch (type)
                    previousHolder.typeImg = (ImageView) view.findViewById(R.id.previous_type_img);
                    view.setTag(previousHolder);
                } else {
                    previousHolder = (PreviousViewHolder) view.getTag();
                }
                previousHolder.type.setText(activities.get(position).subType);
                previousHolder.date.setText(activities.get(position).date.substring(0,10));
                break;
            case OWN:
                if (view == null) {
                    view = inflater.inflate(R.layout.activity_own, parent, false);
                    ownHolder = new OwnViewHolder();
                    ownHolder.type = (TextView) view.findViewById(R.id.own_type);
                    ownHolder.duration = (TextView) view.findViewById(R.id.own_duration);
                    view.setTag(ownHolder);
                } else {
                    ownHolder = (OwnViewHolder) view.getTag();
                }
                ownHolder.type.setText(activities.get(position).subType);
                ownHolder.duration.setText(Integer.toString(activities.get(position).durationInMinutes));
                break;
            case BOOKED:
                if (view == null) {
                    view = inflater.inflate(R.layout.activity_booked, parent, false);
                    bookedHolder = new BookedViewHolder();
                    bookedHolder.workoutType = (TextView) view.findViewById(R.id.pass);
                    bookedHolder.gymLocation = (TextView) view.findViewById(R.id.center);
                    bookedHolder.instructorsName = (TextView) view.findViewById(R.id.instructor);
                    bookedHolder.positionInQueue = (TextView) view.findViewById(R.id.number_participants);
                    bookedHolder.startTimeHour = (TextView) view.findViewById(R.id.hour);
                    bookedHolder.startTimeMinutes = (TextView) view.findViewById(R.id.minutes);
                    bookedHolder.activityDuration = (TextView) view.findViewById(R.id.class_time);
                    bookedHolder.activityDate = (TextView) view.findViewById(R.id.date_header);
                    bookedHolder.positionInQueueImg = (ImageView) view.findViewById(R.id.img_participants);
                    view.setTag(bookedHolder);
                } else {
                    bookedHolder = (BookedViewHolder) view.getTag();
                }
                bookedHolder.workoutType.setText(activities.get(position).subType);
                bookedHolder.gymLocation.setText(centers.get(activities.get(position).booking.center).name);
//                bookedHolder.gymLocation.setText("CenterName TODO");
                bookedHolder.instructorsName.setText(activities.get(position).booking.aClass.instructorId);
//                bookedHolder.instructorsName.setText("instructorName TODO");
                bookedHolder.positionInQueue.setText(Integer.toString(activities.get(position).booking.positionInQueue));
//                bookedHolder.positionInQueue.setText("PosInQueue TODO");
                bookedHolder.startTimeHour.setText(activities.get(position).date.substring(11, 13));
                bookedHolder.startTimeMinutes.setText(activities.get(position).date.substring(14, 16));
//                bookedHolder.activityDate.setText(activities.get(position).date.substring(0, 10));
                bookedHolder.activityDuration.setText(activities.get(position).durationInMinutes + " min");

                // Hide waiting list count img if no one in queue
            if (activities.get(position).booking.positionInQueue == 0) {
                bookedHolder.positionInQueue.setVisibility(View.GONE);
                bookedHolder.positionInQueueImg.setVisibility(View.GONE);
            }
                break;

        }


        return view;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.date_header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.date_header);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        date = DateTime.parse(getItem(position).date);
        String extra = "";

        if (date.getDayOfYear() == currentDateTime.getDayOfYear()){
            extra = "Idag, ";
        }
        if (date.getDayOfYear() == currentDateTime.getDayOfYear()+1){
            extra = "Imorgon, ";
        }
        String headerText = swedish_days[date.getDayOfWeek()] + " " + date.getDayOfMonth() + " " + swedish_months[date.getMonthOfYear()];

        holder.text.setText(extra + headerText);
        return convertView;
    }

    public static ArrayList<Activity> getList() {
        return activities;
    }

    @Override
    public long getHeaderId(int i) {
        return i;
    }

    private class HeaderViewHolder {
        TextView text;
    }
}