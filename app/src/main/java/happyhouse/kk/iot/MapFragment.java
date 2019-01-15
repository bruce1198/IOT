package happyhouse.kk.iot;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private String lat = "";
    private String lng = "";
    private GoogleMap mMap;
    private MapView mapView;
    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            //if(MachineActivity.json_object.length()!=0) {
                //JSONObject jsonObject = MachineActivity.json_object;
                //lat = jsonObject.getString("lat");
                //lng = jsonObject.getString("lng");
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = root.findViewById(R.id.google_map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng location;
        try {
            location = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        } catch (NumberFormatException|NullPointerException e) {
            location = new LatLng(24.79, 120.99);
        }
        mMap.addMarker(new MarkerOptions().position(location).title("POSITION"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        UiSettings us = mMap.getUiSettings();
        us.setAllGesturesEnabled(true);
        us.setZoomControlsEnabled(true);
    }
}
