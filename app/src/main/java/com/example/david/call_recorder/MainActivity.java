package com.example.david.call_recorder;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aykuttasil.callrecord.CallRecord;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        tryToRequestMarshmallowPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        tryToRequestMarshmallowPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED);
        tryToRequestMarshmallowPermission(Manifest.permission.RECORD_AUDIO);
        tryToRequestMarshmallowPermission(Manifest.permission.PROCESS_OUTGOING_CALLS);
        tryToRequestMarshmallowPermission(Manifest.permission.READ_PHONE_STATE);
        tryToRequestMarshmallowPermission(Manifest.permission.CAPTURE_AUDIO_OUTPUT);


        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this, MainActivity.class);
        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Set", Toast.LENGTH_SHORT).show();
            startService(new Intent(this, MyService.class));
            finish();
        }
    }

    public void tryToRequestMarshmallowPermission(String permissionName) {

        if (Build.VERSION.SDK_INT > 22) {

            final Method checkSelfPermissionMethod = getCheckSelfPermissionMethod();

            if (checkSelfPermissionMethod != null) {

                try {

                    final Integer permissionCheckResult = (Integer) checkSelfPermissionMethod.invoke(this, permissionName);
                    if (permissionCheckResult != PackageManager.PERMISSION_GRANTED) {
                        final Method requestPermissionsMethod = getRequestPermissionsMethod();
                        if (requestPermissionsMethod != null) {
                            requestPermissionsMethod.invoke(this, new String[]{permissionName}, 1);
                            return;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        startService(new Intent(this, MyService.class));
        finish();
    }

    private Method getCheckSelfPermissionMethod() {
        try {
            return Activity.class.getMethod("checkSelfPermission", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    private Method getRequestPermissionsMethod() {
        try {
            final Class[] parameterTypes = {String[].class, int.class};

            return Activity.class.getMethod("requestPermissions", parameterTypes);

        } catch (Exception e) {
            return null;
        }
    }

}
