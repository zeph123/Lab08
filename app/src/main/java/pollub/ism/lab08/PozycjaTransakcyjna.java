package pollub.ism.lab08;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Transakcje")
public class PozycjaTransakcyjna {

    @PrimaryKey(autoGenerate = true)
    public int _id;
    public String NAME;
    public String FULL_HISTORY;
}