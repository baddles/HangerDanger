package baddles.hangerdanger.hangerdanger;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ScoreBoard.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
        public abstract ScoreBoardDAO ScoreBoardDao();
}

