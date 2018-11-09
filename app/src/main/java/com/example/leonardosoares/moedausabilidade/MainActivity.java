package com.example.leonardosoares.moedausabilidade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.leonardosoares.moedausabilidade.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mBinding;
    public static final String MOEDA_DE = "MOEDA_DE";
    public static final String MOEDA_PARA = "MOEDA_PARA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.moedas_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.moedaDeSpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.moedas_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.moedaParaSpinner.setAdapter(adapter2);

        setContentView(mBinding.getRoot());
    }

    public void proximoClicked(View view) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(MOEDA_DE, mBinding.moedaDeSpinner.getSelectedItemPosition());
        editor.putInt(MOEDA_PARA, mBinding.moedaParaSpinner.getSelectedItemPosition());
        editor.commit();
        Intent intent = new Intent(this, ConversaoActivity.class);
        startActivity(intent);
    }
}
