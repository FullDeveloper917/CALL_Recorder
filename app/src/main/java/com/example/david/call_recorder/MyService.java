package com.example.david.call_recorder;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.aykuttasil.callrecord.CallRecord;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        startApp();

    }

    private void startApp() {
        CallRecord callRecord = new CallRecord.Builder(this)
                .setRecordFileName("testRecordFile")
                .setRecordDirName("testRecordDir")
                .setRecordDirPath(Environment.getExternalStorageDirectory().getPath()) // optional & default value
                .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB) // optional & default value
                .setOutputFormat(MediaRecorder.OutputFormat.AMR_NB) // optional & default value
                .setAudioSource(MediaRecorder.AudioSource.VOICE_CALL) // optional & default value
                .setShowSeed(true) // optional & default value ->Ex: RecordFileName_incoming.amr || RecordFileName_outgoing.amr
                .build();

        callRecord.startCallReceiver();
        Log.d("startApp~~~~", "Record ready");
        Toast.makeText(this, "Record ready", Toast.LENGTH_SHORT).show();
    }
}
