package com.example.leonardosoares.moedausabilidade;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.hardware.Camera;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.leonardosoares.moedausabilidade.databinding.ActivityConversaoBinding;

public class ConversaoActivity extends AppCompatActivity {

    public Camera mCamera;
    public Preview mPreview;

    public static final int REAL = 0;
    public static final int DOLAR = 1;
    public static final int EURO = 2;
    public static final int PESO_ARGENTINO = 3;

    private int moedaDe;
    private int moedaPara;

    ActivityConversaoBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mBinding.preview.setCamera(Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK));
    }

}
