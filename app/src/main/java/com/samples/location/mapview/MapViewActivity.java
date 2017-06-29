package com.samples.location.mapview;

import android.location.*;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import java.util.List;
import java.util.Locale;

public class MapViewActivity extends MapActivity {

    private MapController controller;
    private TextView text;
    private MapView map;

    private final LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            printLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {
            //printLocation(null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        text = (TextView)findViewById(R.id.text);
        map = (MapView)findViewById(R.id.map);

        controller = map.getController();

        map.setSatellite(true);
        map.setStreetView(true);

        LocationManager manager = (LocationManager)getSystemService(LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        printLocation(manager.getLastKnownLocation(LocationManager.GPS_PROVIDER));

    }

    public void printLocation(Location location){
        if (location !=null){

            double lat = location.getLatitude();
            double lon = location.getLongitude();

            GeoPoint point = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));

            // Перемещаем карту в заданую точку
            controller.animateTo(point);

            text.setText(String.format("Lat: %4.2f, Long: %4.2f", lat, lon));

            try {
                // Используем Geocoder для получения информации
                // о местоположении и выводим ее на экран
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                int size = addresses.size();
                if (size > 0){
                    Address address = addresses.get(0);
                    for (int i = 0; i < size; i++){
                        text.append(", " + address.getAddressLine(i));
                    }
                    text.append(", " + address.getCountryName());
                }
            }catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Location unavalable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

}
