package edu.illinois.cs465.studybuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Callback;

import org.json.JSONException;
import org.json.JSONObject;

public class MapsFragment extends Fragment {

    String placeId;
    String locName;

    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View mWindow;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.marker_info_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null; // use the default window
        }

        @Override
        public View getInfoContents(Marker marker) {
            render(marker, mWindow);
            return mWindow;
        }

        private void render(Marker marker, View v) {
            TextView title = v.findViewById(R.id.info_window_title);
            title.setText(marker.getTitle());
        }
    }

    private OnMapReadyCallback callback = googleMap -> {
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
        Request request = new Request.Builder()
                .url(String.format("https://maps.googleapis.com/maps/api/place/details/json?place_id=%s&fields=geometry&key=%s",
                        placeId, getResources().getString(R.string.google_maps_key)))
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        JSONObject geometryLocation = json.getJSONObject("result").getJSONObject("geometry").getJSONObject("location");
                        LatLng locationLatlng = new LatLng(geometryLocation.getDouble("lat"), geometryLocation.getDouble("lng"));
                        Log.d("resp latlng", locationLatlng.toString());
                        getActivity().runOnUiThread(() -> {
                            googleMap.addMarker(new MarkerOptions()
                                    .position(locationLatlng)
                                    .title(locName));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatlng, 16f));
                        });
                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                try {
                    String encodedName = URLEncoder.encode(locName, StandardCharsets.UTF_8.name());
                    String queryUrl = String.format("https://www.google.com/maps/search/?api=1&query=%s&query_place_id=%s?z=16", encodedName, placeId);
                    Log.d("Google Maps Query", queryUrl);
                    Uri gMapsUri = Uri.parse(queryUrl);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gMapsUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                } catch (java.io.UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e("Unsupported Encoding", locName);
                }
            }
        });
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();
        if (activity instanceof LocationPageActivity) {
            LocationPageActivity locationPage = (LocationPageActivity) activity;
            placeId = locationPage.GetGoogleMapsPlaceId();
            locName = locationPage.GetLocationName();
        }

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}