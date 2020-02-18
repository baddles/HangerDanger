package baddles.hangerdanger.hangerdanger;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity
public class ScoreBoard {
    @PrimaryKey
    private int id; // id is game number. This will be tracked somewhere.
    @ColumnInfo(name = "playername")
    private String playerName;
    @ColumnInfo(name = "score")
    private int score;

    public ScoreBoard(int id, String playerName, int score) {
        this.id = id;
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
