package com.example.lab5_20191047;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {
    private ArrayList<String> listaPlatos = new ArrayList<>();
    private ArrayList<Double> listaCalorias = new ArrayList<>();
    private TextView edKcalPlato, edPlato;
    private Button btn_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        btn_add = findViewById(R.id.btn_login);
        edPlato = findViewById(R.id.ed_nameplato);
        edKcalPlato = findViewById(R.id.ed_kcalplato);
        String nombrePlato  = edPlato.getText().toString();
        double kcalPlato = Double.parseDouble(edKcalPlato.getText().toString());

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resultIntent = new Intent();
                resultIntent.putExtra("nombrePlato", nombrePlato);
                resultIntent.putExtra("kcalPlato", kcalPlato);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });



    }
}
