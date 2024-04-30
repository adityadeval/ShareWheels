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

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.sharewheels.R;
import edu.uga.cs.sharewheels.adapters.AdapterDisplayMyRidesFrag;
import edu.uga.cs.sharewheels.databinding.FragmentActiveRidesBinding;
import edu.uga.cs.sharewheels.databinding.FragmentPendingRidesBinding;
import edu.uga.cs.sharewheels.datamodels.Ride;
import edu.uga.cs.sharewheels.datamodels.User;
import edu.uga.cs.sharewheels.firebaseutils.FirebaseOps;
import edu.uga.cs.sharewheels.firebaseutils.GetAllRidesFromDBCallback;
import edu.uga.cs.sharewheels.firebaseutils.UserDetailsCallback;

public class ActiveRidesFragment extends Fragment {
    private FragmentActiveRidesBinding binding;
    private FirebaseOps m_firebaseops_instance;
    private AdapterDisplayMyRidesFrag adapter;
    private RecyclerView rv_active_rides;
    private ArrayList<Ride> rides;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_firebaseops_instance = new FirebaseOps();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentActiveRidesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("ActiveRidesFragment","Entering");

        // Initialize RecyclerView
        rv_active_rides = view.findViewById(R.id.rv_active_rides);
        // Setup RecyclerView - Adapter, LayoutManager, etc.
        rv_active_rides.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AdapterDisplayMyRidesFrag(getActivity(), new ArrayList<>(), this);
        rv_active_rides.setAdapter(adapter);

        m_firebaseops_instance.get_user_as_obj(getActivity(), new UserDetailsCallback(){

            @Override
            public void onUserDetailsFetched(User user) {
                Log.d("User ID fetched", user.getUserID());
                showActiveRides(user.getRidesList());
            }

            @Override
            public void onUserDetailsFailed(String errorMessage) {
                Toast.makeText(getContext(), "Failed to fetch user details: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });

    }

    public void ride_complete_success() {
        Snackbar.make(getView(), "Ride is completed Successfully, Your points is updated", Snackbar.LENGTH_SHORT).show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onResume() {
        super.onResume();

    }
    public void showActiveRides(List<String> ridesList){
        Log.d("ActiveRidesFragment","Entering showActiveRides");

        m_firebaseops_instance.fetchUserRides(ridesList, new GetAllRidesFromDBCallback() {

            @Override
            public void onRideDataReceived(ArrayList<Ride> rideList) {
                Log.d("ActiveRidesFragment.showActiveRides()", "rideList = "+rideList);
                adapter.updateData(rideList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRideDataFailed(String error) {
                Toast.makeText(getContext(), "Error fetching rides: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}