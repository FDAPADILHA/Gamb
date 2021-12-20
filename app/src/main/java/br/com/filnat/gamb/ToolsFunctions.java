package br.com.filnat.gamb;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class ToolsFunctions extends AppCompatActivity implements View.OnClickListener {

    private Button buttonPower, buttonPowerOn, buttonPowerOff, btn;
    public static final int RESULT_ENABLE = 11;
    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName compName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funcoes);
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        compName = new ComponentName(this, AdminPower.class);

        buttonPower = findViewById(R.id.buttonPower);
        buttonPowerOn = findViewById(R.id.buttonPowerOn);
        buttonPowerOff = findViewById(R.id.buttonPowerOff);
        buttonPower.setOnClickListener(this);
        buttonPowerOn.setOnClickListener(this);
        buttonPowerOff.setOnClickListener(this);

        verifyStoragePermission(this);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenShot(getWindow().getDecorView().getRootView(), "print");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isActive = devicePolicyManager.isAdminActive(compName);
        buttonPowerOff.setVisibility(isActive ? View.VISIBLE : View.GONE);
        buttonPowerOn.setVisibility(isActive ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonPower) {
            boolean active = devicePolicyManager.isAdminActive(compName);

            if (active) {
                devicePolicyManager.lockNow();
            } else {
                Toast.makeText(this, R.string.permission1, Toast.LENGTH_SHORT).show();
            }

        } else if (view == buttonPowerOn) {

            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, R.string.permission2);
            startActivityIfNeeded(intent, RESULT_ENABLE);
            /*startActivityIfNeeded*/
        } else if (view == buttonPowerOff) {
            devicePolicyManager.removeActiveAdmin(compName);
            buttonPowerOff.setVisibility(View.GONE);
            buttonPowerOn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch(requestCode) {
            case RESULT_ENABLE:
                if(resultCode == AppCompatActivity.RESULT_OK){
                    Toast.makeText(ToolsFunctions.this, R.string.permission1, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ToolsFunctions.this, R.string.permissionalert, Toast.LENGTH_LONG).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //ScreenShot

    private static File takeScreenShot(View view, String fileName) {

        //Date date = new Date();
        //CharSequence format = DateFormat.getDateInstance(DateFormat.SHORT).format(date);


            String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
            File fileDir = new File(dirPath);
            if (!fileDir.exists())
                 fileDir.mkdir();

            //String path = dirPath + "/" + fileName + "-" + format + ".jpeg";
            String path = dirPath + "/" + fileName + ".jpeg";

            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);

            File imageFile = new File(path);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return imageFile;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSION_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermission(AppCompatActivity activity) {

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSION_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

}