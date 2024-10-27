package com.example.lab5_20191047;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText edName, edAltura, edEdad, edPeso, edTiempo;
    private Spinner spinnergenero, spinneraf, spinnerobj;
    private Button btnIngDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        edName = findViewById(R.id.ed_name);
        edAltura = findViewById(R.id.ed_altura);
        edEdad = findViewById(R.id.ed_edad);
        edPeso = findViewById(R.id.ed_peso);
        edTiempo = findViewById(R.id.ed_tiempo);

        btnIngDatos = findViewById(R.id.btn_login);

        spinnergenero = findViewById(R.id.spinner_genero);
        spinneraf = findViewById(R.id.spinner_af);
        spinnerobj = findViewById(R.id.spinner_obj);

        // Adaptador para spinner de género
        ArrayAdapter<CharSequence> adapterGenero = ArrayAdapter.createFromResource(this,
                R.array.opciones_genero, android.R.layout.simple_spinner_item);
        adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnergenero.setAdapter(adapterGenero);

        // Adaptador para spinner de actividad física
        ArrayAdapter<CharSequence> adapterAf = ArrayAdapter.createFromResource(this,
                R.array.opciones_af, android.R.layout.simple_spinner_item);
        adapterAf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinneraf.setAdapter(adapterAf);

        // Adaptador para spinner de objetivos
        ArrayAdapter<CharSequence> adapterObj = ArrayAdapter.createFromResource(this,
                R.array.opciones_obj, android.R.layout.simple_spinner_item);
        adapterObj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerobj.setAdapter(adapterObj);

        btnIngDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén los valores de los campos
                double peso = Double.parseDouble(edPeso.getText().toString());
                double altura = Double.parseDouble(edAltura.getText().toString());
                int edad = Integer.parseInt(edEdad.getText().toString());
                String nombre  = edName.getText().toString();
                String tiempo  = edTiempo.getText().toString();
                String genero = spinnergenero.getSelectedItem().toString();
                String actividadFisica = spinneraf.getSelectedItem().toString();
                String objetivo = spinnerobj.getSelectedItem().toString();

                double tmb;
                if (genero.equals("Masculino")) {
                    tmb = (10 * peso) + (6.25 * altura) - (5 * edad) + 5;
                } else { // Femenino
                    tmb = (10 * peso) + (6.25 * altura) - (5 * edad) - 161;
                }
                double tmbAjustado;
                if (actividadFisica.equals("Poco o ningún ejercicio")) {
                    tmbAjustado = tmb * 1.2;
                } else if (actividadFisica.equals("Ejercicio ligero (1 - 3 días por semana)")) {
                    tmbAjustado = tmb * 1.375;
                } else if (actividadFisica.equals("Ejercicio moderado (3 - 5 días por semana)")) {
                    tmbAjustado = tmb * 1.55;
                } else if (actividadFisica.equals("Ejercicio fuerte (6 - 7 días por semana)")) {
                    tmbAjustado = tmb * 1.725;
                } else if (actividadFisica.equals("Ejercicio muy fuerte (dos veces al día)")) {
                    tmbAjustado = tmb * 1.9;
                } else {
                    tmbAjustado = tmb;
                }


                double caloriasRecomendadas;
                if (objetivo.equals("Mantener")) {
                    caloriasRecomendadas = tmbAjustado;
                } else if (objetivo.equals("Subir")) {
                    caloriasRecomendadas = tmbAjustado + 500;
                } else { // Bajar de peso
                    caloriasRecomendadas = tmbAjustado - 300;
                }

                ArrayList<String> datosUsuario = new ArrayList<>();
                datosUsuario.add("Nombre: " + nombre);
                datosUsuario.add("Género: " + genero);
                datosUsuario.add("Objetivo: " + objetivo);
                datosUsuario.add("Calorías a consumir: " + caloriasRecomendadas);
                datosUsuario.add(tiempo);

                // Intent para pasar a la siguiente actividad
                Intent intent = new Intent(MainActivity.this, ResultadoActivity.class);
                intent.putStringArrayListExtra("datosUsuario", datosUsuario);
                startActivity(intent);
            }
        });


    }
}