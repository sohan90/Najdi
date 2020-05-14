package com.najdi.android.najdiapp.checkout;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;

import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.utitility.LocaleUtitlity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FetchAddressIntentService extends JobIntentService {
    public static final String TAG = FetchAddressIntentService.class.getSimpleName();
    private static final int JOB_ID = 1001;
    private ResultReceiver resultReceiver;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, FetchAddressIntentService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@Nullable Intent intent) {
        if (intent == null) return;
        Geocoder geocoder = new Geocoder(this, LocaleUtitlity.getLocale());
        // Get the location passed to this service through an extra.
        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
        resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);
        if (isStopped()) return;
        handleAddress(geocoder, location);
    }

    private void handleAddress(Geocoder geocoder, Location location) {
        String errorMessage = "";
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),
                    // In this sample, get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);

        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long);

        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, null);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();
            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            deliverResultToReceiver(Constants.SUCCESS_RESULT, address);
        }
    }

    private void deliverResultToReceiver(int resultCode, Address address) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RESULT_DATA_KEY, address);
        resultReceiver.send(resultCode, bundle);
    }
}
