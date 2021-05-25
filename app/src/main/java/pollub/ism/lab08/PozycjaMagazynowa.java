package pollub.ism.lab08;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Warzywniak")
public class PozycjaMagazynowa {

    @PrimaryKey(autoGenerate = true)
    public int _id;
    public String NAME;
    public int QUANTITY;
    public String MODIFICATION_DATE;
    public String FULL_HISTORY;
}
