package ch.heigvd.iict.sym.sym_labo4;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import ch.heigvd.iict.sym.wearcommon.Constants;

public class WearSynchronizedActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, DataClient.OnDataChangedListener {

    private static final String TAG = WearSynchronizedActivity.class.getSimpleName();

    private SeekBar redSlider = null;
    private SeekBar greenSlider = null;
    private SeekBar blueSlider = null;
    private SeekBar greySlider = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearsynchronized);

        redSlider = findViewById(R.id.redSeekBar);
        greenSlider = findViewById(R.id.greenSeekBar);
        blueSlider = findViewById(R.id.blueSeekBar);
        greySlider = findViewById(R.id.greySeekBar);

        redSlider.setOnSeekBarChangeListener(this);
        greenSlider.setOnSeekBarChangeListener(this);
        blueSlider.setOnSeekBarChangeListener(this);
        greySlider.setOnSeekBarChangeListener(this);

        updateBackgroundColor();
    }

    /**
     * Method used to update the background color of the activity
     */
    private void updateBackgroundColor() {

        int r = redSlider.getProgress();
        int g = greenSlider.getProgress();
        int b = blueSlider.getProgress();

        View rootView = findViewById(android.R.id.content);
        rootView.setBackgroundColor(Color.argb(255, r, g, b));
    }

    private void sendPayload() {

        int r = redSlider.getProgress();
        int g = greenSlider.getProgress();
        int b = blueSlider.getProgress();

        //Send payload to the layer API
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(Constants.DATA);
        putDataMapRequest.getDataMap().putInt(Constants.RED, r);
        putDataMapRequest.getDataMap().putInt(Constants.GREEN, g);
        putDataMapRequest.getDataMap().putInt(Constants.BLUE, b);

        PutDataRequest request = putDataMapRequest.asPutDataRequest();
        request.setUrgent();

        Log.w(TAG, "DataItem saved: " + request.toString());

        Wearable.getDataClient(getApplicationContext()).putDataItem(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Wearable.getDataClient(this).addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.getDataClient(this).removeListener(this);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (seekBar.getId() == greySlider.getId()) {
            int cursor = greySlider.getProgress();

            redSlider.setProgress(cursor);
            greenSlider.setProgress(cursor);
            blueSlider.setProgress(cursor);
        }

        updateBackgroundColor();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        /* NOTHING TO DO */
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        sendPayload();
    }

    /*
     * Sends data to proper WearableRecyclerView logger row or if the item passed is an asset, sends
     * to row displaying Bitmaps.
     */
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "onDataChanged(): " + dataEvents);

        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (Constants.DATA.equals(path)) {

                    DataItem dataItem = event.getDataItem();
                    DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();

                    int r = dataMap.getInt(Constants.RED);
                    int g = dataMap.getInt(Constants.GREEN);
                    int b = dataMap.getInt(Constants.BLUE);

                    redSlider.setProgress(r);
                    greenSlider.setProgress(g);
                    blueSlider.setProgress(b);

                    updateBackgroundColor();
                }
            }
        }
    }

}
