package baddles.hangerdanger.hangerdanger;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScoreBoardDAO {
    @Query("SELECT * FROM ScoreBoard ORDER BY score DESC, playername DESC")
    List<ScoreBoard> loadScoreBoard();

    @Delete
    void delete(ScoreBoard ScoreBoard);

    @Insert
    void insert(ScoreBoard scoreBoard);

    @Query("SELECT id FROM ScoreBoard ORDER BY id DESC LIMIT 1")
    int getNumberOfGamesPlayedInSQLTable();

    @Query("SELECT * FROM ScoreBoard ORDER BY score ASC, id DESC LIMIT 1")
    int getLowestScore();

    @Query("DELETE FROM ScoreBoard WHERE EXISTS (SELECT * FROM ScoreBoard ORDER BY SCORE ASC, id DESC LIMIT 1)")
    void deleteLowestValue();
}
