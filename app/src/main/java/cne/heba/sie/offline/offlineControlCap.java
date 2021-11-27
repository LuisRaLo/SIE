package cne.heba.sie.offline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import cne.heba.sie.R;
import cne.heba.sie.menu.Captura.Step.controlCap;

public class offlineControlCap extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_control_cap);

        ArrayList<String> arrayList = new ArrayList<>();

        tabLayout = findViewById(R.id.offCap);
        viewPager = findViewById(R.id.vPageOff);

        arrayList.add(""); //LUGAR 0 //CLAVES CURP E INES
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");

        prepareViewPager(viewPager, arrayList);
        //TabLayout tabLayout = viewPager.findViewById(R.id.offCap);
        tabLayout.setupWithViewPager(viewPager);

        //AGREGO EL PRIMER ICONO PARA ESTILIZAR BIEN LA APP
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_iden);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_account_box_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_inicio_white_24dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.inefront);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_struct);
        tabLayout.getTabAt(5).setIcon(R.drawable.ic_finish);


        viewPager.setOffscreenPageLimit(4);

    }
        //Método view
    private void prepareViewPager(ViewPager viewPager, ArrayList<String> arrayList) {

        controlCap.MainAdapter adapter = new controlCap.MainAdapter(getSupportFragmentManager());

        offCap1 cap0 = new offCap1();
        offCap2 cap1 = new offCap2();
        offCapDomicilio cap2 = new offCapDomicilio();
        offCap3 cap3 = new offCap3();
        capEstructuraOff cap4 = new capEstructuraOff();
        offCap4 cap5 = new offCap4();

        Fragment fragA[] = {cap0, cap1, cap2, cap3, cap4,cap5};

        for (int i = 0; i < arrayList.size(); i++) {

            adapter.addFragment(fragA[i], arrayList.get(i));
        }

        viewPager = findViewById(R.id.vPageOff);
        viewPager.setAdapter(adapter);

    }


}