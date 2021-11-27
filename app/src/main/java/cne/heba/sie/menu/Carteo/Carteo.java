package cne.heba.sie.menu.Carteo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import cne.heba.sie.R;
import cne.heba.sie.menu.Asistencia.controlAsiste;


public class Carteo extends Fragment {

    View ControlCarteo;
    TabLayout tabLayout;
    ViewPager viewPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ControlCarteo = inflater.inflate(R.layout.fragment_carteo, container, false);

        ArrayList<String> arrayList = new ArrayList<>();

        tabLayout = ControlCarteo.findViewById(R.id.tabCarteo);
        viewPager = ControlCarteo.findViewById(R.id.viewPagerCarteo);

        arrayList.add(""); //LUGAR 0
        arrayList.add("");


        prepareViewPager(viewPager, arrayList);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_lista);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_demand);

     return ControlCarteo;
    }

    private void prepareViewPager(ViewPager viewPager, ArrayList<String> arrayList) {

        controlAsiste.MainAdapterAs adapter = new controlAsiste.MainAdapterAs(getChildFragmentManager());


        CarteoUbicacion ubicacion = new CarteoUbicacion();
        CarteoEntrega entrega = new CarteoEntrega();


        Fragment fragA[] = {ubicacion, entrega};

        for (int i = 0; i < arrayList.size(); i++) {

            adapter.addFragment(fragA[i], arrayList.get(i));

        }

        viewPager.setAdapter(adapter);
    }


}
