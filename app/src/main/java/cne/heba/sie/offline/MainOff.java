package cne.heba.sie.offline;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import cne.heba.sie.R;


public class MainOff extends Fragment {

    Button btnOff;

    View vistaoff;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vistaoff = inflater.inflate(R.layout.fragment_main_off, container, false);

        btnOff = vistaoff.findViewById(R.id.btnOff);

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent laundOch = new Intent(getContext(), offlineControlCap.class);
                startActivity(laundOch);
            }
        });


        return vistaoff;
    }
}