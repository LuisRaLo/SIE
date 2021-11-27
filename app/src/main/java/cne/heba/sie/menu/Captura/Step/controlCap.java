package cne.heba.sie.menu.Captura.Step;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import cne.heba.sie.R;
import cne.heba.sie.util.Constantes;

public class controlCap extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_cap);

        ArrayList<String> arrayList = new ArrayList<>();

        tabLayout = findViewById(R.id.tabCAP);
        viewPager = findViewById(R.id.viewPagerCap);

        if(Constantes.ID_USER_NIVEL<103) {
            //QUITO TEXTOS PARA AGREGAR ICONOS
            arrayList.add(""); //LUGAR 0 //CLAVES CURP E INES
            arrayList.add("");
            arrayList.add("");
            arrayList.add("");
            arrayList.add("");
            arrayList.add("");
            arrayList.add("");


            prepareViewPager(viewPager, arrayList);
            tabLayout.setupWithViewPager(viewPager);


            //AGREGO EL PRIMER ICONO PARA ESTILIZAR BIEN LA APP
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_iden);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_account_box_24);
            tabLayout.getTabAt(2).setIcon(R.drawable.ic_lista);
            tabLayout.getTabAt(4).setIcon(R.drawable.ic_struct);
            tabLayout.getTabAt(5).setIcon(R.drawable.ic_demand);
            tabLayout.getTabAt(6).setIcon(R.drawable.ic_finish);

            viewPager.setOffscreenPageLimit(7);
        }else {
            Toast.makeText(getApplicationContext(),"Usuario Mayor a 103",Toast.LENGTH_SHORT).show();
            //QUITO TEXTOS PARA AGREGAR ICONOS
            arrayList.add(""); //LUGAR 0 //CLAVES CURP E INES
            arrayList.add("");
            arrayList.add("");
            arrayList.add("");
            arrayList.add("");
            arrayList.add("");


            prepareViewPager(viewPager, arrayList);
            tabLayout.setupWithViewPager(viewPager);


            //AGREGO EL PRIMER ICONO PARA ESTILIZAR BIEN LA APP
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_iden);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_account_box_24);
            tabLayout.getTabAt(2).setIcon(R.drawable.ic_lista);
            tabLayout.getTabAt(4).setIcon(R.drawable.ic_demand);
            tabLayout.getTabAt(5).setIcon(R.drawable.ic_finish);

            viewPager.setOffscreenPageLimit(7);

        }


    }

    private void prepareViewPager(ViewPager viewPager, ArrayList<String> arrayList) {

        if(Constantes.ID_USER_NIVEL<103) {

            MainAdapter adapter = new MainAdapter(getSupportFragmentManager());

            Cap1 cap0 = new Cap1();
            Cap2 cap1 = new Cap2();
            Cap3 cap2 = new Cap3();
            EnviarFoto cap3 = new EnviarFoto();
            estructur cap4 = new estructur();
            Cap4 cap5 = new Cap4();
            demandas cap6 = new demandas();


            Fragment fragA[] = {cap0, cap1, cap2, cap3, cap4, cap6, cap5};

            for (int i = 0; i < arrayList.size(); i++) {

                adapter.addFragment(fragA[i], arrayList.get(i));

            }

            viewPager.setAdapter(adapter);
        }
        else {
            Toast.makeText(getApplicationContext(),"Usuario Mayor a 103",Toast.LENGTH_SHORT).show();
            MainAdapter adapter = new MainAdapter(getSupportFragmentManager());

            Cap1 cap0 = new Cap1();
            Cap2 cap1 = new Cap2();
            Cap3 cap2 = new Cap3();
            EnviarFoto cap3 = new EnviarFoto();
            Cap4 cap5 = new Cap4();
            demandas cap6 = new demandas();


            Fragment fragA[] = {cap0, cap1, cap2, cap3, cap6, cap5};

            for (int i = 0; i < arrayList.size(); i++) {

                adapter.addFragment(fragA[i], arrayList.get(i));

            }

            viewPager.setAdapter(adapter);
        }
    }


    public static class MainAdapter extends FragmentPagerAdapter {


        ArrayList<String> arrayList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();


        public void addFragment(Fragment fragment, String title) {

            arrayList.add(title);
            fragmentList.add(fragment);


        }


        public MainAdapter(@NonNull FragmentManager fm) {
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


