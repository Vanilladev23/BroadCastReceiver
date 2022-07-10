package com.example.broadcastreceiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;

public class BroadCastGPS extends BroadcastReceiver {

	@SuppressLint("UnsafeProtectedBroadcastReceiver")
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {
			String locationMode = "";
			ContentResolver contentResolver = context.getContentResolver();
			// Find out what the settings say about which providers are enabled
			int mode = Settings.Secure.getInt(
					contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);

			if (mode != Settings.Secure.LOCATION_MODE_OFF) {
				Log.d("BBB", locationMode);
				Intent intentLocation = new Intent(context, MainActivity.class);
				intentLocation.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				intentLocation.putExtra("mode", true);
				context.startActivity(intentLocation);
			} else {
				Log.d("BBB", "Location off");
			}
		}
	}
}

