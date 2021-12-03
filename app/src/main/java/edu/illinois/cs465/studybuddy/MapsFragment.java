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

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

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
        private String hoursText;

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
            TextView titleTextView = v.findViewById(R.id.info_window_title);
            titleTextView.setText(marker.getTitle());

            if (hoursText != null) {
                TextView hoursTextView = v.findViewById(R.id.hours_text);
                hoursTextView.setText(hoursText);
            }
        }

        public void setHoursText(String hoursText) {
            this.hoursText = hoursText;
        }
    }

    private OnMapReadyCallback callback = googleMap -> {
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        CustomInfoWindowAdapter ciwa = new CustomInfoWindowAdapter();
        googleMap.setInfoWindowAdapter(ciwa);

        OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
        Request request = new Request.Builder()
                .url(String.format("https://maps.googleapis.com/maps/api/place/details/json?place_id=%s&fields=geometry%%2Copening_hours&key=%s",
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
                    LatLng latLng = null;
                    String hoursText = null;

                    try {
                        String resp = response.body().string();
                        Log.d("response", resp);
                        JSONObject jsonRes = new JSONObject(resp);
                        JSONObject geometryLocation = jsonRes.getJSONObject("result").getJSONObject("geometry").getJSONObject("location");
                        latLng = new LatLng(geometryLocation.getDouble("lat"), geometryLocation.getDouble("lng"));

                        Calendar c = Calendar.getInstance();
                        int numericalDOW = (c.get(Calendar.DAY_OF_WEEK) + 5) % 7;
                        hoursText = (String)jsonRes.getJSONObject("result").getJSONObject("opening_hours").getJSONArray("weekday_text").get(numericalDOW);

                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();
                    }

                    if (latLng != null) {
                        LatLng finalLatLng = latLng;
                        String finalHoursText = hoursText;
                        getActivity().runOnUiThread(() -> {
                            Marker locationMarker = googleMap.addMarker(new MarkerOptions()
                                    .position(finalLatLng)
                                    .title(locName));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(finalLatLng, 16f));
                            if (finalHoursText != null) {
                                ciwa.setHoursText(finalHoursText);
                            }

                            locationMarker.showInfoWindow();
                        });
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