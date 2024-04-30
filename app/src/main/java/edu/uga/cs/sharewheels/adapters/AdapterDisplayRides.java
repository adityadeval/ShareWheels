package edu.uga.cs.sharewheels.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.uga.cs.sharewheels.R;
import edu.uga.cs.sharewheels.activities.DriverActivity;
import edu.uga.cs.sharewheels.activities.RiderActivity;
import edu.uga.cs.sharewheels.datamodels.Ride;
import edu.uga.cs.sharewheels.firebaseutils.CreateRideInDBCallback;
import edu.uga.cs.sharewheels.firebaseutils.FirebaseOps;


public class AdapterDisplayRides extends RecyclerView.Adapter{

    private Context context;

    // arrayList_ride is going to contain an array of rides that need to be displayed.
    // To be more specific, it'll contain all rides that have a riderID, but not a driverID.
    // It is not dependent on whether we're displaying ride requests or ride offers. This arrayList will simply contain data
    // from the JSON node "Ride" of our database.
    private ArrayList<Ride> arrayList_rides;
    private FirebaseOps m_firebaseops_instance;
    String loggedInUserId = "";

    public AdapterDisplayRides(Context context, ArrayList<Ride> arrayList_rides) {
        this.context = context;
        this.arrayList_rides = arrayList_rides;
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

        if (context.getClass().getSimpleName().equals("RiderActivity")) {
            itemView = inflater.inflate(R.layout.item_ride_offers, parent, false);
            return new RideOffersViewHolder(itemView);
        } else if (context.getClass().getSimpleName().equals("DriverActivity")) {
            itemView = inflater.inflate(R.layout.item_ride_requests, parent, false);
            return new RideRequestsViewHolder(itemView);
        } else {
            // Default case, or log error
            return null; // This line can be handled better depending on your error handling strategy
        }
    }

    // Below method would be called by Android each time a Holder is created by the adapter for displaying data.
    // 'position' would keep getting auto incremented by Android till all visible views in the Recycler View are filled.
    // 'position' would also be used by us to fetch data from the arrayList_rides one by one.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Ride ride = arrayList_rides.get(position);

        // The same adapter is going to be used for displaying Ride Requests and Ride Offers.
        // Below line checks if the ViewHolder is of type RideRequestsViewHolder which is specifically used for displaying
        // ride requests for the Driver.
        if (holder instanceof RideRequestsViewHolder) {
            RideRequestsViewHolder rideRequestsViewHolder = (RideRequestsViewHolder) holder;
            /*
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
            String formattedDate = ride.getDate().format(formatter);
             */
            rideRequestsViewHolder.tv_DateValue.setText(ride.getDate());
            rideRequestsViewHolder.tv_OriginValue.setText(ride.getOrigin());
            rideRequestsViewHolder.tv_DestinationValue.setText(ride.getDestination());
            rideRequestsViewHolder.tv_RideRequestorValue.setText(ride.getRiderID());

            rideRequestsViewHolder.button_acceptRideRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 1. First fetch the userID of the user who's logge
                    //TODO 2. In the ride that is fetched, (in the ride variable), set the driver_id to currently logged in user.
                    //TODO 3. Call the ride_request_accepted_success() method of the DriverActivity to display green snack bar message saying ride accepted.
                    //TODO 4. Then Delete that particular inflater from the screen

                    int adapterPosition = rideRequestsViewHolder.getAdapterPosition();

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

                    m_firebaseops_instance.updateUserRidesList(loggedInUserId, ride.getRideId(), new CreateRideInDBCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure(String errorMessage) {

                        }
                    });

                }
            });

        }


        if(holder instanceof RideOffersViewHolder){
            RideOffersViewHolder rideOffersViewHolder = (RideOffersViewHolder) holder;

            rideOffersViewHolder.tv_DateValue_offer.setText(ride.getDate());
            rideOffersViewHolder.tv_OriginValue_offer.setText(ride.getOrigin());
            rideOffersViewHolder.tv_DestinationValue_offer.setText(ride.getDestination());
            rideOffersViewHolder.tv_RideOfferorValue.setText(ride.getDriverID());

            rideOffersViewHolder.button_acceptRideOffer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 1. First fetch the userID of the user who's logged in.
                    //TODO 2. In the ride that is fetched, (variable 'ride'), set the rider_id to currently logged in user's ID.
                    //TODO 3. Call the ride_offer_accepted_success() method of the RiderActivity to display green snack bar message saying ride accepted.
                    //TODO 4. Then Delete that particular inflater from the screen
                    ride.setRiderID(loggedInUserId);
                    ((RiderActivity) context).ride_offer_accepted_success();
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
                    m_firebaseops_instance.updateUserRidesList(loggedInUserId, ride.getRideId(), new CreateRideInDBCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure(String errorMessage) {

                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        return arrayList_rides.size();
    }


    public static class RideRequestsViewHolder extends RecyclerView.ViewHolder{

        TextView tv_DateValue;
        TextView tv_OriginValue;
        TextView tv_DestinationValue;
        TextView tv_RideRequestorValue;
        Button button_acceptRideRequest;
        public RideRequestsViewHolder(View itemView) {
            super(itemView);

            tv_DateValue = itemView.findViewById(R.id.tv_DateValue);
            tv_OriginValue = itemView.findViewById(R.id.tv_OriginValue);
            tv_DestinationValue = itemView.findViewById(R.id.tv_DestinationValue);
            tv_RideRequestorValue = itemView.findViewById(R.id.tv_RideRequestorValue);
            button_acceptRideRequest = itemView.findViewById(R.id.button_acceptRideRequest);
        }

    }


    public static class RideOffersViewHolder extends RecyclerView.ViewHolder{

        TextView tv_DateValue_offer;
        TextView tv_OriginValue_offer;
        TextView tv_DestinationValue_offer;
        TextView tv_RideOfferorValue;
        Button button_acceptRideOffer;
        public RideOffersViewHolder(View itemView) {
            super(itemView);

            tv_DateValue_offer = itemView.findViewById(R.id.tv_DateValue);
            tv_OriginValue_offer = itemView.findViewById(R.id.tv_OriginValue);
            tv_DestinationValue_offer = itemView.findViewById(R.id.tv_DestinationValue);
            tv_RideOfferorValue = itemView.findViewById(R.id.tv_RideOfferorValue);
            button_acceptRideOffer = itemView.findViewById(R.id.button_delete_ride);
        }

    }
}
