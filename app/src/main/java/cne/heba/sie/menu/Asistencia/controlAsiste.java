package cne.heba.sie.menu.Asistencia;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import cne.heba.sie.R;
import cne.heba.sie.menu.Captura.Step.Cap1;
import cne.heba.sie.menu.Captura.Step.Cap2;
import cne.heba.sie.menu.Captura.Step.Cap3;
import cne.heba.sie.menu.Captura.Step.Cap4;
import cne.heba.sie.menu.Captura.Step.EnviarFoto;
import cne.heba.sie.menu.Captura.Step.controlCap;
import cne.heba.sie.menu.Captura.Step.demandas;
import cne.heba.sie.menu.Captura.Step.estructur;
import cne.heba.sie.util.Constantes;


public class controlAsiste extends Fragment {

    View ControlVista;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ControlVista = inflater.inflate(R.layout.fragment_control_asiste, container, false);


        ArrayList<String> arrayList = new ArrayList<>();

        tabLayout = ControlVista.findViewById(R.id.tabAsiste);
        viewPager = ControlVista.findViewById(R.id.viewPagerAsiste);

        arrayList.add(""); //LUGAR 0
        arrayList.add("");


        prepareViewPager(viewPager, arrayList);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_account_box_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_lista);


        return ControlVista;
    }

    private void prepareViewPager(ViewPager viewPager, ArrayList<String> arrayList) {

            MainAdapterAs adapter = new MainAdapterAs(getChildFragmentManager());

            asiste pasaLista = new asiste();
            altaReunion altaRe = new altaReunion();

            Fragment fragA[] = {pasaLista, altaRe};

            for (int i = 0; i < arrayList.size(); i++) {

                adapter.addFragment(fragA[i], arrayList.get(i));

            }

            viewPager.setAdapter(adapter);
        }


    public static class MainAdapterAs extends FragmentPagerAdapter {


        ArrayList<String> arrayList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();


        public void addFragment(Fragment fragment, String title) {

            arrayList.add(title);
            fragmentList.add(fragment);


        }


        public MainAdapterAs(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            return fragmentList.get(position);
        }

        @Override
        public int getCount() {

            return fragmentList.size();


        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            return arrayList.get(position);

        }
    }
}