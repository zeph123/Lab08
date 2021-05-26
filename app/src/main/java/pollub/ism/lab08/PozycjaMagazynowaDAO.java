package pollub.ism.lab08;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PozycjaMagazynowaDAO {

    @Insert  //Automatyczna kwerenda wystarczy
    public void insert(PozycjaMagazynowa pozycja, PozycjaTransakcyjna pozycja2);

    @Update //Automatyczna kwerenda wystarczy
    void update(PozycjaMagazynowa pozycja, PozycjaTransakcyjna pozycja2);

    @Query("SELECT QUANTITY FROM Warzywniak WHERE NAME= :wybraneWarzywoNazwa") //Nasza kwerenda
    int findQuantityByName(String wybraneWarzywoNazwa);

    @Query("UPDATE Warzywniak SET QUANTITY = :wybraneWarzywoNowaIlosc WHERE NAME= :wybraneWarzywoNazwa")
    void updateQuantityByName(String wybraneWarzywoNazwa, int wybraneWarzywoNowaIlosc);

    // znalezienie daty oraz czasu modyfikacji ilości warzywa
    @Query("SELECT MODIFICATION_DATE FROM Warzywniak WHERE NAME= :wybraneWarzywoNazwa")
    String findModificationDateByName(String wybraneWarzywoNazwa);

    // uzupełnienie daty oraz czasu modyfikacji dla wybranego warzywa
    @Query("UPDATE Warzywniak SET MODIFICATION_DATE = :wybraneWarzywoNowaData WHERE NAME= :wybraneWarzywoNazwa")
    void updateModificationDateByName(String wybraneWarzywoNazwa, String wybraneWarzywoNowaData);

    // znalezienie daty oraz czasu modyfikacji ilości warzywa
    @Query("SELECT FULL_HISTORY FROM Transakcje WHERE NAME= :wybraneWarzywoNazwa")
    String findFullHistoryByName(String wybraneWarzywoNazwa);

    // uzupełnienie historii modyfikacji dla wybranego warzywa
    @Query("UPDATE Transakcje SET FULL_HISTORY = :wybraneWarzywoHistoria WHERE NAME= :wybraneWarzywoNazwa")
    void updateFullHistoryByName(String wybraneWarzywoNazwa, String wybraneWarzywoHistoria);

    @Query("SELECT COUNT(*) FROM Warzywniak") //Ile jest rekordów w tabeli
    int size();
}
