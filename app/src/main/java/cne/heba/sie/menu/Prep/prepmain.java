package cne.heba.sie.menu.Prep;

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
import cne.heba.sie.menu.Captura.Step.controlCap;


public class prepmain extends Fragment {

    View prep;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        prep = inflater.inflate(R.layout.fragment_prepmain, container, false);

        ArrayList<String> arrayList = new ArrayList<>();

        tabLayout = prep.findViewById(R.id.tabPrep);
        viewPager = prep.findViewById(R.id.viewPagerPrep);

        arrayList.add(""); //LUGAR 0
        arrayList.add("");


        prepareViewPager(viewPager, arrayList);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_qr);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_demand);


        return prep;
    }

    private void prepareViewPager(ViewPager viewPager, ArrayList<String> arrayList) {

        controlAsiste.MainAdapterAs adapter = new controlAsiste.MainAdapterAs(getChildFragmentManager());

        QRprep qRprep = new QRprep();
        casillasPrep casillasPrep = new casillasPrep();

        Fragment fragA[] = {qRprep, casillasPrep};

        for (int i = 0; i < arrayList.size(); i++) {

            adapter.addFragment(fragA[i], arrayList.get(i));

        }

        viewPager.setAdapter(adapter);

    }
}