package edu.uga.cs.sharewheels.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.uga.cs.sharewheels.R;
import edu.uga.cs.sharewheels.activities.DriverActivity;
import edu.uga.cs.sharewheels.activities.MyRidesActivity;
import edu.uga.cs.sharewheels.datamodels.Ride;
import edu.uga.cs.sharewheels.firebaseutils.CreateRideInDBCallback;
import edu.uga.cs.sharewheels.firebaseutils.DeleteRideCallBack;
import edu.uga.cs.sharewheels.firebaseutils.FirebaseOps;
import edu.uga.cs.sharewheels.fragments.PendingRidesFragment;

public class AdapterDisplayMyRidesFrag extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<Ride> arrayList_rides;
    private Fragment fragment;  // Reference to Fragment
    private FirebaseOps m_firebaseops_instance;
    String loggedInUserId = "";

    public AdapterDisplayMyRidesFrag(Context context, ArrayList<Ride> arrayList_rides, Fragment fragment) {
        this.context = context;
        this.arrayList_rides = arrayList_rides;
        this.fragment = fragment;
    }

    // Method to update the data in the adapter
    public void updateData(ArrayList<Ride> newRides) {
        arrayList_rides.clear(); // Clear the existing data
        arrayList_rides.addAll(newRides); // Add all new data
        Log.d("AdapterDisplayRides", "Contents of arrayList_rides: " + arrayList_rides);
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Get the Logged in user id
        m_firebaseops_instance = new FirebaseOps();
        loggedInUserId = m_firebaseops_instance.getLoggedInUserID();

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;

        if (fragment.getClass().getSimpleName().equals("PendingRidesFragment")) {
            itemView = inflater.inflate(R.layout.item_pending_rides, parent, false);
            return new PendingRidesViewHolder(itemView);
        }else {
            // Default case, or log error
            return null;  // This line can be handled better depending on your error handling strategy
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Ride ride = arrayList_rides.get(position);

        if (holder instanceof PendingRidesViewHolder) {
            PendingRidesViewHolder pendingRidesHolder = (PendingRidesViewHolder) holder;
            /*
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
            String formattedDate = ride.getDate().format(formatter);
             */
            pendingRidesHolder.tv_DateValue.setText(ride.getDate());
            pendingRidesHolder.tv_OriginValue.setText(ride.getOrigin());
            pendingRidesHolder.tv_DestinationValue.setText(ride.getDestination());

            pendingRidesHolder.button_delete_ride.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    m_firebaseops_instance.deleteRide(ride.getRideId(), new DeleteRideCallBack() {
                        @Override
                        public void onSuccess() {
                            // Remove the ride from the list and notify the adapter
                            arrayList_rides.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, arrayList_rides.size());
                            Log.d("AdapterDisplayMyRidesFrag", "Ride deleted successfully");
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            // Log or display the error
                            Log.e("AdapterDisplayMyRidesFrag", "Failed to delete ride: " + errorMessage);
                        }
                    });
                }
                });

            /*
            pendingRidesHolder.button_delete_ride.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int adapterPosition = pendingRidesHolder.getAdapterPosition();

                    ride.setDriverID(loggedInUserId);
                    ((DriverActivity) context).ride_request_accepted_success();
                    m_firebaseops_instance.acceptRide(ride, new CreateRideInDBCallback() {
                        @Override
                        public void onSuccess() {
                            // Remove the accepted ride from the list and update the UI
                            arrayList_rides.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, arrayList_rides.size());
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            // Show an error message
                        }
                    });


                }
            });
             */


        }

    }

    @Override
    public int getItemCount() {
        return arrayList_rides.size();
    }

    public static class PendingRidesViewHolder extends RecyclerView.ViewHolder{
        TextView tv_DateValue;
        TextView tv_OriginValue;
        TextView tv_DestinationValue;
        Button button_edit_ride;
        Button button_delete_ride;

        public PendingRidesViewHolder(View itemView) {
            super(itemView);

            tv_DateValue = itemView.findViewById(R.id.tv_DateValue);
            tv_OriginValue = itemView.findViewById(R.id.tv_OriginValue);
            tv_DestinationValue = itemView.findViewById(R.id.tv_DestinationValue);
            button_edit_ride = itemView.findViewById(R.id.button_edit_ride);
            button_delete_ride = itemView.findViewById(R.id.button_delete_ride);
        }
    }



}
