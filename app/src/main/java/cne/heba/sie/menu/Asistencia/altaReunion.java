package cne.heba.sie.menu.Asistencia;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import cne.heba.sie.R;
import cne.heba.sie.myUIX.DatePickerFragment;
import cne.heba.sie.myUIX.TimePickerFragment;


public class altaReunion extends Fragment {

    String[] tipReuniones = {"Seleccione Tipo de Reunión","Equipo","Ruta","Sección","Zona"};

    Button fech,altaa,hora;
    EditText lugar;
    Spinner tipoReun;

    View altaRView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        altaRView = inflater.inflate(R.layout.fragment_alta_reunion, container, false);

        fech = altaRView.findViewById(R.id.fechaReuni);
        hora = altaRView.findViewById(R.id.horaReuni);
        altaa = altaRView.findViewById(R.id.darAltaReuni);

        lugar = altaRView.findViewById(R.id.lugarReuni);

        tipoReun = altaRView.findViewById(R.id.tipReuni);

        tipoReun.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, tipReuniones));

        tipoReun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(),"El Elemento seleccionado es posición número:" +position + " El String es: " +zon.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getChildFragmentManager(), "datePicker");

            }
        });


        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getChildFragmentManager(), "timePicker");

            }
        });

        return altaRView;
    }
}