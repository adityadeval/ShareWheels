package edu.uga.cs.sharewheels.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.sharewheels.R;
import edu.uga.cs.sharewheels.adapters.AdapterDisplayRides;
import edu.uga.cs.sharewheels.databinding.FragmentPendingRidesBinding;
import edu.uga.cs.sharewheels.datamodels.Ride;
import edu.uga.cs.sharewheels.datamodels.User;
import edu.uga.cs.sharewheels.firebaseutils.FirebaseOps;
import edu.uga.cs.sharewheels.firebaseutils.GetAllRidesFromDBCallback;
import edu.uga.cs.sharewheels.firebaseutils.UserDetailsCallback;

public class PendingRidesFragment extends Fragment {

    private FragmentPendingRidesBinding binding;
    private FirebaseOps m_firebaseops_instance;
    private AdapterDisplayRides adapter;
    private RecyclerView rv_pending_rides;
    private ArrayList<Ride> rides;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_firebaseops_instance = new FirebaseOps();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentPendingRidesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*
        final TextView textView = binding.textHome;
        textView.setText("This is PendingRidesFragment");

         */
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView
        rv_pending_rides = view.findViewById(R.id.rv_pending_rides);
        // Setup RecyclerView - Adapter, LayoutManager, etc.
        rv_pending_rides.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AdapterDisplayRides(getActivity(), new ArrayList<>());
        rv_pending_rides.setAdapter(adapter);

        m_firebaseops_instance.get_user_as_obj(getActivity(), new UserDetailsCallback(){

            @Override
            public void onUserDetailsFetched(User user) {
                Log.d("User ID fetched", user.getUserID());
                Log.d("Rides List of user: ", user.getRidesList().toString());
                showPendingRides(user.getRidesList());
            }

            @Override
            public void onUserDetailsFailed(String errorMessage) {
                Toast.makeText(getContext(), "Failed to fetch user details: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void showPendingRides(List<String> ridesList){

        m_firebaseops_instance.fetchUserRides(ridesList, new GetAllRidesFromDBCallback() {

            @Override
            public void onRideDataReceived(ArrayList<Ride> rideList) {
                Log.d("PendingRidesFragment.showPendingRides()", "rideList = "+rideList);
                adapter.updateData(rideList);

                //adapter = new AdapterDisplayRides(getActivity(), rideList);
//                adapter = new AdapterDisplayRides(getActivity(), new ArrayList<>());
                //rv_pending_rides.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRideDataFailed(String error) {
                Toast.makeText(getContext(), "Error fetching rides: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}