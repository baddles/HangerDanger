package baddles.hangerdanger.hangerdanger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoardAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private List<ScoreBoard> scoreBoard;

    public ScoreBoardAdapter(Context context, int resource, List<ScoreBoard> scoreBoard) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.scoreBoard = scoreBoard;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) { // First time
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            convertView = layoutInflater.inflate(resource, parent, false);
        }
        final TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtDesc = convertView.findViewById(R.id.txtScore);
        final ScoreBoard item = scoreBoard.get(position);
        txtName.setText(item.getPlayerName());
        txtDesc.setText(item.getScore());
        return convertView;
    }

    @Override
    public int getCount() {
        if (scoreBoard.size() < 10) {
            return scoreBoard.size();
        }
        return 10;
    }
}