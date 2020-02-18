package baddles.hangerdanger.hangerdanger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

public class MainActivity extends Activity {

    static int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
    }

    public void btnOnClickNewGame(View view) {
        Button mainButton = findViewById(R.id.buttonNewGame);
        Button leaderboard = findViewById(R.id.buttonLeaderboards);
        final ConstraintLayout constraintLayout = findViewById(R.id.ConstraintMain);
        constraintLayout.setBackground(getDrawable(R.drawable.bg_tutorial_0));
        mainButton.setVisibility(View.GONE);
        leaderboard.setVisibility(View.GONE);
        final CountDownTimer count = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("@123", Integer.toString((int)millisUntilFinished));
            }

            @Override
            public void onFinish() {
                constraintLayout.setBackground(getDrawable(R.drawable.bg_tutorial_1));
                findViewById(R.id.buttonSkip).setVisibility(View.VISIBLE);
                findViewById(R.id.buttonBack).setVisibility(View.VISIBLE);
                findViewById(R.id.buttonNext).setVisibility(View.VISIBLE);
            }
        };
        count.start();
        counter = 1;
    }

    public void changeBackground(int i) {
        switch (i) {
            case 0: {
                findViewById(R.id.buttonBack).setVisibility(View.GONE);
                findViewById(R.id.buttonNext).setVisibility(View.GONE);
                findViewById(R.id.buttonNewGame).setVisibility(View.VISIBLE);
                findViewById(R.id.buttonLeaderboards).setVisibility(View.VISIBLE);
                findViewById(R.id.ConstraintMain).setBackground(getDrawable(R.drawable.bg_mainmenu));
                return;
            }
            case 1: {
                findViewById(R.id.ConstraintMain).setBackground(getDrawable(R.drawable.bg_tutorial_1));
                break;
            }
            case 2: {
                findViewById(R.id.ConstraintMain).setBackground(getDrawable(R.drawable.bg_tutorial_2));
                break;
            }
            case 3: {
                findViewById(R.id.ConstraintMain).setBackground(getDrawable(R.drawable.bg_tutorial_3));
                break;
            }
            case 4: {
                findViewById(R.id.ConstraintMain).setBackground(getDrawable(R.drawable.bg_tutorial_4));
                break;
            }
            case 5: {
                findViewById(R.id.ConstraintMain).setBackground(getDrawable(R.drawable.bg_tutorial_5));
                break;
            }
            case 6: {
                findViewById(R.id.ConstraintMain).setBackground(getDrawable(R.drawable.bg_tutorial_6));
                break;
            }
            case 7: {
                findViewById(R.id.ConstraintMain).setBackground(getDrawable(R.drawable.bg_tutorial_7));
                findViewById(R.id.buttonSkip).setVisibility(View.VISIBLE);
                findViewById(R.id.buttonBack).setVisibility(View.VISIBLE);
                findViewById(R.id.buttonNext).setVisibility(View.VISIBLE);
                findViewById(R.id.buttonPrev).setVisibility(View.GONE);
                findViewById(R.id.buttonPlay).setVisibility(View.GONE);
                break;
            }
            case 8: {
                findViewById(R.id.ConstraintMain).setBackground(getDrawable(R.drawable.bg_tutorial_8));
                findViewById(R.id.buttonSkip).setVisibility(View.GONE);
                findViewById(R.id.buttonBack).setVisibility(View.GONE);
                findViewById(R.id.buttonNext).setVisibility(View.GONE);
                findViewById(R.id.buttonPrev).setVisibility(View.VISIBLE);
                findViewById(R.id.buttonPlay).setVisibility(View.VISIBLE);
                break;
            }
            case 9: {
                findViewById(R.id.buttonBack).setVisibility(View.GONE);
                findViewById(R.id.buttonNext).setVisibility(View.GONE);
                startGame();
                break;
            }
            default: {
                findViewById(R.id.ConstraintMain).setBackground(getDrawable(R.drawable.bg_mainmenu));
                break;
            }
        }
    }

    private void startGame() {
        Intent intent = new Intent(this, GameActivity.class);
        this.finish();
        startActivity(intent);// placeholder for start game.
    }

    public void btnOnClickLeaderboards(View view) {
    }

    public void btnOnClickBack(View view) {
        counter--;
        changeBackground(counter);
    }

    public void btnOnClickNext(View view) {
        counter++;
        changeBackground(counter);
    }

    public void btnOnClickSkip(View view) {
        startGame();
    }
}
