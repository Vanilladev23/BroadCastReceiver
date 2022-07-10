package com.example.broadcastreceiver;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

	private LocationManager locationManager;
	private LocationListener listener;
	private BroadCastGPS broadCastGPS;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		broadCastGPS = new BroadCastGPS();


		listener = new LocationListener() {

			@Override
			public void onLocationChanged(@NonNull Location location) {
				Log.d("BBB","Lat : " +location.getLatitude());
				Log.d("BBB","Long : " +location.getLongitude());
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}

			@Override
			public void onProviderEnabled(@NonNull String provider) {
			}

			@Override
			public void onProviderDisabled(@NonNull String provider) {
//                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(i);
			}
		};

		Intent intent = getIntent();

		if (intent != null){
			boolean mode = intent.getBooleanExtra("mode",false);
			if (mode){
				configure_location();
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
			registerReceiver(broadCastGPS,intentFilter);
			configure_location();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && broadCastGPS != null){
			unregisterReceiver(broadCastGPS);
		}

	}

	@SuppressLint("ObsoleteSdkInt")
	private void configure_location(){
		if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,
				ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				request_permission();
			}
		} else {
			// permission has been granted
			locationManager.requestLocationUpdates("gps", 5000, 0, listener);
		}
	}

	private void request_permission() {
		if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
				ACCESS_COARSE_LOCATION)) {

			Snackbar.make(findViewById(R.id.main_linear_layout), "Location permission is needed because ...",
							Snackbar.LENGTH_LONG)
					.setAction("retry", new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							requestPermissions(new String[]{ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 10);
						}
					})
					.show();
		} else {
			// permission has not been granted yet. Request it directly.
			requestPermissions(new String[]{ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 10);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == 10) {
			configure_location();
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
}