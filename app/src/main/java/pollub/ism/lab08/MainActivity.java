package pollub.ism.lab08;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.time.ZoneId;

import pollub.ism.lab08.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayAdapter<CharSequence> adapter;
    private String wybraneWarzywoNazwa = null;
    private Integer wybraneWarzywoIlosc = null;
    private String wybraneWarzywoDataModyfikacji = null;
    private String wybraneWarzywoPelnaHistoriaModyfikacji = null;
    public enum OperacjaMagazynowa {SKLADUJ, WYDAJ};
    private BazaMagazynowa bazaDanych;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = ArrayAdapter.createFromResource(this, R.array.Asortyment, android.R.layout.simple_dropdown_item_1line);
        binding.spinner.setAdapter(adapter);

        bazaDanych = Room.databaseBuilder(getApplicationContext(), BazaMagazynowa.class, BazaMagazynowa.NAZWA_BAZY).allowMainThreadQueries().build();

        if(bazaDanych.pozycjaMagazynowaDAO().size() == 0){
            String[] asortyment = getResources().getStringArray(R.array.Asortyment);
            for(String nazwa : asortyment){

                PozycjaMagazynowa pozycjaMagazynowa = new PozycjaMagazynowa();
                pozycjaMagazynowa.NAME = nazwa;
                pozycjaMagazynowa.QUANTITY = 0;
                pozycjaMagazynowa.MODIFICATION_DATE = null;

                PozycjaTransakcyjna pozycjaTransakcyjna = new PozycjaTransakcyjna();
                pozycjaTransakcyjna.NAME = nazwa;
                pozycjaTransakcyjna.FULL_HISTORY = null;

                bazaDanych.pozycjaMagazynowaDAO().insert(pozycjaMagazynowa,pozycjaTransakcyjna);
            }
        }

        binding.przyciskSkladuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zmienStan(OperacjaMagazynowa.SKLADUJ);
            }
        });

        binding.przyciskWydaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zmienStan(OperacjaMagazynowa.WYDAJ);
            }
        });

        binding.przyciskWyczyscHistorieTransakcji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wyczyscHistorieTransakcji();
            }
        });



        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                wybraneWarzywoNazwa = adapter.getItem(i).toString();
                aktualizuj();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Nie będziemy implementować, ale musi być
            }
        });

    }

    private void aktualizuj(){
        wybraneWarzywoIlosc = bazaDanych.pozycjaMagazynowaDAO().findQuantityByName(wybraneWarzywoNazwa);
        String stanMagazynu = "Stan magazynu dla " + wybraneWarzywoNazwa + " wynosi: " + wybraneWarzywoIlosc;
        binding.tekstStanMagazynu.setText(stanMagazynu);

        wybraneWarzywoDataModyfikacji = bazaDanych.pozycjaMagazynowaDAO().findModificationDateByName(wybraneWarzywoNazwa);
        String dataModyfikacji;
        if(wybraneWarzywoDataModyfikacji != null){
            dataModyfikacji = "Data modyfikacji : " + wybraneWarzywoDataModyfikacji;
        } else {
            dataModyfikacji = "Data modyfikacji : ";
        }
        binding.tekstDataModyfikacji.setText(dataModyfikacji);

        wybraneWarzywoPelnaHistoriaModyfikacji = bazaDanych.pozycjaMagazynowaDAO().findFullHistoryByName(wybraneWarzywoNazwa);
        String pelnaHistoriaModyfikacji;
        if(wybraneWarzywoPelnaHistoriaModyfikacji != null){
            pelnaHistoriaModyfikacji = "Historia modyfikacji : \n" + wybraneWarzywoPelnaHistoriaModyfikacji;
        } else {
            pelnaHistoriaModyfikacji = "Historia modyfikacji : ";
        }
        binding.historiaWarzywa.setText(pelnaHistoriaModyfikacji);
    }

    private void zmienStan(OperacjaMagazynowa operacja){

        Integer zmianaIlosci = null, nowaIlosc = null, staraIlosc = null;
        String dataModyfikacji = null, czasModyfikacji = null, pelnaData = null, nowyWpis = null, historia = "";

        try {
            zmianaIlosci = Integer.parseInt(binding.edycjaIlosc.getText().toString());
        }catch(NumberFormatException ex){
            return;
        }finally {
            binding.edycjaIlosc.setText("");
        }

        switch (operacja){
            case SKLADUJ:
            {
                nowaIlosc = wybraneWarzywoIlosc + zmianaIlosci;
                // --
                // ustalenie starej ilosci
                staraIlosc = wybraneWarzywoIlosc;
                // dodanie daty oraz czasu
                dataModyfikacji = java.time.LocalDate.now().toString();
                czasModyfikacji = java.time.LocalTime.now(ZoneId.of("Europe/Warsaw")).toString();
                // utworzenie pełnej daty
                pelnaData = dataModyfikacji + ", " + czasModyfikacji;
                // utworzenie nowego wpisu
                nowyWpis = pelnaData + ", " + staraIlosc.toString() + " -> " + nowaIlosc.toString();
                // tworzenie pelnej historii
                wybraneWarzywoPelnaHistoriaModyfikacji = bazaDanych.pozycjaMagazynowaDAO().findFullHistoryByName(wybraneWarzywoNazwa);
                if(wybraneWarzywoPelnaHistoriaModyfikacji != null){
                    historia = wybraneWarzywoPelnaHistoriaModyfikacji + "\n" + nowyWpis;
                } else {
                    historia = nowyWpis;
                }
                // --
            } break;
            case WYDAJ:
            {
                nowaIlosc = wybraneWarzywoIlosc - zmianaIlosci;
                // --
                // ustalenie starej ilosci
                staraIlosc = wybraneWarzywoIlosc;
                // dodanie daty oraz czasu
                dataModyfikacji = java.time.LocalDate.now().toString();
                czasModyfikacji = java.time.LocalTime.now(ZoneId.of("Europe/Warsaw")).toString();
                // utworzenie pełnej daty
                pelnaData = dataModyfikacji + ", " + czasModyfikacji;
                // utworzenie nowego wpisu
                nowyWpis = pelnaData + ", " + staraIlosc.toString() + " -> " + nowaIlosc.toString();
                // tworzenie pelnej historii
                wybraneWarzywoPelnaHistoriaModyfikacji = bazaDanych.pozycjaMagazynowaDAO().findFullHistoryByName(wybraneWarzywoNazwa);
                if(wybraneWarzywoPelnaHistoriaModyfikacji != null){
                    historia = wybraneWarzywoPelnaHistoriaModyfikacji + "\n" + nowyWpis;
                } else {
                    historia = nowyWpis;
                }
                // --
            } break;
        }

        bazaDanych.pozycjaMagazynowaDAO().updateQuantityByName(wybraneWarzywoNazwa,nowaIlosc);
        bazaDanych.pozycjaMagazynowaDAO().updateModificationDateByName(wybraneWarzywoNazwa,pelnaData);
        bazaDanych.pozycjaMagazynowaDAO().updateFullHistoryByName(wybraneWarzywoNazwa,historia);

        aktualizuj();
    }

    private void wyczyscHistorieTransakcji(){

        bazaDanych.pozycjaMagazynowaDAO().updateFullHistoryByName(wybraneWarzywoNazwa,null);

        aktualizuj();
    }

}