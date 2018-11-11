package com.example.leonardosoares.moedausabilidade;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.hardware.Camera;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Surface;

import com.example.leonardosoares.moedausabilidade.databinding.ActivityConversaoBinding;

import static android.hardware.Camera.*;

public class ConversaoActivity extends AppCompatActivity {

    public Camera mCamera;

    public static final int REAL = 0;
    public static final int DOLAR = 1;
    public static final int EURO = 2;
    public static final int PESO_ARGENTINO = 3;

    private int moedaDe;
    private int moedaPara;

    ActivityConversaoBinding mBinding;

    int numberOfCameras;
    int cameraCurrentlyLocked;

    // The first rear facing camera
    int defaultCameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Find the total number of cameras available
        numberOfCameras = getNumberOfCameras();

        // Find the ID of the default camera
        CameraInfo cameraInfo = new CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                defaultCameraId = i;
            }
        }

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_conversao);
        mBinding.deEditField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    mBinding.paraEditField.setText(converte(editable.toString()));
                } catch (Exception e) {
                }
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        moedaDe = sharedPreferences.getInt(MainActivity.MOEDA_DE, 0);
        moedaPara = sharedPreferences.getInt(MainActivity.MOEDA_PARA, 0);

        switch (moedaDe) {
            case ConversaoActivity.REAL:
                mBinding.deLabel.setText(R.string.real);
                break;
            case ConversaoActivity.DOLAR:
                mBinding.deLabel.setText(R.string.dolar);
                break;
            case ConversaoActivity.EURO:
                mBinding.deLabel.setText(R.string.euro);
                break;
            case ConversaoActivity.PESO_ARGENTINO:
                mBinding.deLabel.setText(R.string.peso);
                break;
        }

        switch (moedaPara) {
            case ConversaoActivity.REAL:
                mBinding.paraLabel.setText(R.string.real);
                break;
            case ConversaoActivity.DOLAR:
                mBinding.paraLabel.setText(R.string.dolar);
                break;
            case ConversaoActivity.EURO:
                mBinding.paraLabel.setText(R.string.euro);
                break;
            case ConversaoActivity.PESO_ARGENTINO:
                mBinding.paraLabel.setText(R.string.peso);
                break;
        }

        setContentView(mBinding.getRoot());
    }

    private String converte(String s) throws Exception {
        Double valor = Double.parseDouble(s);
        switch (moedaDe) {
            case REAL:
                if (moedaPara == DOLAR) return String.format("%.2f", valor * 0.267618);
                else if (moedaPara == EURO) return String.format("%.2f", valor * 0.236167);
                else if (moedaPara == PESO_ARGENTINO) return String.format("%.2f", valor * 9.47495);
                break;
            case DOLAR:
                if (moedaPara == REAL) return String.format("%.2f", valor * 3.73659);
                else if (moedaPara == EURO) return String.format("%.2f", valor * 0.882375);
                else if (moedaPara == PESO_ARGENTINO) return String.format("%.2f", valor * 35.4046);
                break;
            case EURO:
                if (moedaPara == DOLAR) return String.format("%.2f", valor * 1.13339);
                else if (moedaPara == REAL) return String.format("%.2f", valor * 4.23416);
                else if (moedaPara == PESO_ARGENTINO) return String.format("%.2f", valor * 40.1298);
                break;
            case PESO_ARGENTINO:
                if (moedaPara == DOLAR) return String.format("%.2f", valor * 1.13339);
                else if (moedaPara == EURO) return String.format("%.2f", valor * 0.0249148);
                else if (moedaPara == REAL) return String.format("%.2f", valor * 0.105551);
                break;
        }
        return String.format("%.2f", valor);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Open the default i.e. the first rear facing camera.
        try {
            mCamera = open();

            CameraInfo info =
                    new CameraInfo();
            getCameraInfo(defaultCameraId, info);
            int rotation = this.getWindowManager().getDefaultDisplay()
                    .getRotation();
            int degrees = 0;
            switch (rotation) {
                case Surface.ROTATION_0: degrees = 0; break;
                case Surface.ROTATION_90: degrees = 90; break;
                case Surface.ROTATION_180: degrees = 180; break;
                case Surface.ROTATION_270: degrees = 270; break;
            }

            int result;
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360;  // compensate the mirror
            } else {  // back-facing
                result = (info.orientation - degrees + 360) % 360;
            }

            mCamera.setDisplayOrientation(result);
            cameraCurrentlyLocked = defaultCameraId;
            mBinding.preview.setCamera(mCamera);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Because the Camera object is a shared resource, it's very
        // important to release it when the activity is paused.
        if (mCamera != null) {
            mBinding.preview.setCamera(null);
            mCamera.release();
            mCamera = null;
        }
    }
}