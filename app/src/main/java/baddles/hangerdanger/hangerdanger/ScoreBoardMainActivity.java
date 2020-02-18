package baddles.hangerdanger.hangerdanger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ScoreBoardMainActivity extends Activity {
    ArrayList<ScoreBoard> scoreBoardArrayList;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        populateScoreBoardToListView();
        this.setTitle("Top 10 best scores:");
    }

    private void populateScoreBoardToListView() {
        // Add SQL here.
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"scoreboard").build();
        listView = this.findViewById(R.id.listview_heroes);
        ScoreBoardAdapter listViewAdapter = new ScoreBoardAdapter(this.getBaseContext(), R.layout.scoreboard_item, scoreBoardArrayList);
        listView.setAdapter(listViewAdapter);
    }
}
