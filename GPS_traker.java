package com.example.velocity_carbon.code;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import static android.location.LocationManager.*;

public class GPS_traker implements LocationListener {
    Context context;



    public GPS_traker(Context c) {
        context = c;
    }

    public GPS_traker() {
        super();
    }

    public Location getLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "permission den", Toast.LENGTH_SHORT).show();
            return null;
        }
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabeled = lm.isProviderEnabled(GPS_PROVIDER);
        if (isGPSEnabeled) {
            lm.removeUpdates(this);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0,this);
            Location l = lm.getLastKnownLocation(GPS_PROVIDER);
            //Toast.makeText(context, "vitore", Toast.LENGTH_SHORT).show();
            return l;
        } else {
            Toast.makeText(context, "enable koro", Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
