
package baddles.hangerdanger.hangerdanger;


import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;


public class GameActivity extends Activity {
    public static AppDatabase db;
    static ArrayList<String> word = new ArrayList<>();
    static ArrayList<String> hint1 = new ArrayList<>();
    static ArrayList<String> hint2 = new ArrayList<>();
    static ArrayList<String> hint3 = new ArrayList<>();
    static HangerDanger hangerDanger = new HangerDanger();
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView mVoiceInputTv;
    private Button mSpeakBtn;
    CountDownLatch latch;
    static int getHintCount = 0;
    char txtCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"ScoreBoard").allowMainThreadQueries().build();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        newGame();
        setButtonHintAnimation(5000);
    }

    private void setButtonHintAnimation(long time) {
        final Button btnHint = findViewById(R.id.hint);
        Animation fadeIn = new AlphaAnimation(0,1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(1000);
        Animation fadeOut = new AlphaAnimation(1,0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);
        final AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        findViewById(R.id.hint).setAnimation(animation);
        final CountDownTimer repeatAnimation = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                this.start();
                btnHint.setBackgroundColor(Color.rgb(136, 169, 159));
                btnHint.setTextColor(Color.rgb(0,0,0));
                btnHint.startAnimation(animation);
                btnHint.setTextColor(Color.argb(0,0,0,0));
                btnHint.setBackgroundColor(Color.argb(0,0,0,0));
            }
        };

        final CountDownTimer appearHint = new CountDownTimer(time, 1000) { // Replace 5 with 15.
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                btnHint.setVisibility(View.VISIBLE);
                repeatAnimation.start();
                getHintCount++;
            }
        };
        appearHint.start();

    }

    public void newGame() {
        initDictionary();
        int rand = getRand();
        initGame(rand, true);
    }

    public void generateGame() {
        int rand = getRand();
        initGame(rand, false);
    }

    private void initGame(int rand, boolean isNewGame) {
        hangerDanger.setWordIndex(rand);
        String w = word.get(rand);
        String s = "";
        TextView txtWord = findViewById(R.id.textWord);
        for (int i = 0; i < w.length(); i++) {
            Character c = word.get(rand).charAt(i);
            if (Character.isSpaceChar(c)) {
                s += ' ';
            } else {
                s += '_';
            }
            s += " ";
        }
        txtWord.setText(s);
        String l = hint1.get(rand);
        TextView textHint1 = findViewById(R.id.textHint1);
        textHint1.setText(l);
        TextView textHint2 = findViewById(R.id.textHint2);
        textHint2.setText("");
        TextView textHint3 = findViewById(R.id.textHint3);
        textHint3.setText("");
        getHintCount = 0;
        if (isNewGame) {
            hangerDanger.newGame(w);
            TextView txtScore = findViewById(R.id.textScore);
            txtScore.setText("Your score: 0");
        }
        else {
            hangerDanger.generateGame(w);
            TextView txtScore = findViewById(R.id.textScore);
            txtScore.setText("Your score: " + hangerDanger.getScore());
        }
    }


    public int getRand() {
        Random random = new Random();
        return random.nextInt(word.size() - 1);
    }

    public void initDictionary() {
        Scanner scanner = new Scanner(getResources().openRawResource(R.raw.dictionary));
        int a = 1;
        while (scanner.hasNextLine()) {
            if (a == 1) {
                word.add(scanner.nextLine());
                a++;
            }
            else if (a == 2){
                hint1.add(scanner.nextLine());
                a++;
            }
            else if (a == 3) {
                hint2.add(scanner.nextLine());
                a++;
            }
            else {
                hint3.add(scanner.nextLine());
                a = 1;
            }
        }
        scanner.close();
    }



    public void hideGameplayButtons() {
        findViewById(R.id.hint).setVisibility(GONE);
        findViewById(R.id.hint).clearAnimation();
        findViewById(R.id.toOverwrite).setVisibility(GONE);
        findViewById(R.id.textWord).setVisibility(GONE);
        findViewById(R.id.textHint1).setVisibility(GONE);
        findViewById(R.id.textHint2).setVisibility(GONE);
        findViewById(R.id.textHint3).setVisibility(GONE);
        findViewById(R.id.textScore).setVisibility(GONE);
    }

    public void appearGameplayButtons() {
        findViewById(R.id.background_main).setBackground(getDrawable(R.drawable.bg_gameplay_0));
        findViewById(R.id.textContinueScore).setVisibility(GONE);
        findViewById(R.id.buttonContinue).setVisibility(GONE);
        findViewById(R.id.editTextName).setVisibility(GONE);
        findViewById(R.id.buttonConfirmName).setVisibility(GONE);
        findViewById(R.id.textView).setVisibility(GONE);
        findViewById(R.id.tryAgain).setVisibility(GONE);
        findViewById(R.id.stopPlaying).setVisibility(GONE);
        findViewById(R.id.toOverwrite).setVisibility(View.VISIBLE);
        findViewById(R.id.textWord).setVisibility(View.VISIBLE);
        findViewById(R.id.textHint1).setVisibility(View.VISIBLE);
        findViewById(R.id.textHint2).setVisibility(View.VISIBLE);
        findViewById(R.id.textHint3).setVisibility(View.VISIBLE);
        findViewById(R.id.textScore).setVisibility(View.VISIBLE);
        findViewById(R.id.A).setVisibility(View.VISIBLE);
        findViewById(R.id.B).setVisibility(View.VISIBLE);
        findViewById(R.id.C).setVisibility(View.VISIBLE);
        findViewById(R.id.D).setVisibility(View.VISIBLE);
        findViewById(R.id.E).setVisibility(View.VISIBLE);
        findViewById(R.id.F).setVisibility(View.VISIBLE);
        findViewById(R.id.G).setVisibility(View.VISIBLE);
        findViewById(R.id.H).setVisibility(View.VISIBLE);
        findViewById(R.id.I).setVisibility(View.VISIBLE);
        findViewById(R.id.J).setVisibility(View.VISIBLE);
        findViewById(R.id.K).setVisibility(View.VISIBLE);
        findViewById(R.id.L).setVisibility(View.VISIBLE);
        findViewById(R.id.M).setVisibility(View.VISIBLE);
        findViewById(R.id.N).setVisibility(View.VISIBLE);
        findViewById(R.id.O).setVisibility(View.VISIBLE);
        findViewById(R.id.P).setVisibility(View.VISIBLE);
        findViewById(R.id.Q).setVisibility(View.VISIBLE);
        findViewById(R.id.R).setVisibility(View.VISIBLE);
        findViewById(R.id.S).setVisibility(View.VISIBLE);
        findViewById(R.id.T).setVisibility(View.VISIBLE);
        findViewById(R.id.U).setVisibility(View.VISIBLE);
        findViewById(R.id.V).setVisibility(View.VISIBLE);
        findViewById(R.id.W).setVisibility(View.VISIBLE);
        findViewById(R.id.X).setVisibility(View.VISIBLE);
        findViewById(R.id.Y).setVisibility(View.VISIBLE);
        findViewById(R.id.Z).setVisibility(View.VISIBLE);

    }

    public void checkUserInput(char c) {
        switch (c) {
            case 'a': {
                findViewById(R.id.A).setVisibility(GONE);
                break;
            }
            case 'b': {
                findViewById(R.id.B).setVisibility(GONE);
                break;
            }
            case 'c': {
                findViewById(R.id.C).setVisibility(GONE);
                break;
            }
            case 'd': {
                findViewById(R.id.D).setVisibility(GONE);
                break;
            }
            case 'e': {
                findViewById(R.id.E).setVisibility(GONE);
                break;
            }
            case 'f': {
                findViewById(R.id.F).setVisibility(GONE);
                break;
            }
            case 'g': {
                findViewById(R.id.G).setVisibility(GONE);
                break;
            }
            case 'h': {
                findViewById(R.id.H).setVisibility(GONE);
                break;
            }
            case 'i': {
                findViewById(R.id.I).setVisibility(GONE);
                break;
            }
            case 'j': {
                findViewById(R.id.J).setVisibility(GONE);
                break;
            }
            case 'k': {
                findViewById(R.id.K).setVisibility(GONE);
                break;
            }
            case 'l': {
                findViewById(R.id.L).setVisibility(GONE);
                break;
            }
            case 'm': {
                findViewById(R.id.M).setVisibility(GONE);
                break;
            }
            case 'n': {
                findViewById(R.id.N).setVisibility(GONE);
                break;
            }
            case 'o': {
                findViewById(R.id.O).setVisibility(GONE);
                break;
            }
            case 'p': {
                findViewById(R.id.P).setVisibility(GONE);
                break;
            }
            case 'q': {
                findViewById(R.id.Q).setVisibility(GONE);
                break;
            }
            case 'r': {
                findViewById(R.id.R).setVisibility(GONE);
                break;
            }
            case 's': {
                findViewById(R.id.S).setVisibility(GONE);
                break;
            }
            case 't': {
                findViewById(R.id.T).setVisibility(GONE);
                break;
            }
            case 'u': {
                findViewById(R.id.U).setVisibility(GONE);
                break;
            }
            case 'v': {
                findViewById(R.id.V).setVisibility(GONE);
                break;
            }
            case 'w': {
                findViewById(R.id.W).setVisibility(GONE);
                break;
            }
            case 'x': {
                findViewById(R.id.X).setVisibility(GONE);
                break;
            }
            case 'y': {
                findViewById(R.id.Y).setVisibility(GONE);
                break;
            }
            case 'z': {
                findViewById(R.id.Z).setVisibility(GONE);
                break;
            }
        }
        boolean isCorrect = hangerDanger.checkUserInput(c);
        if (isCorrect) {
            TextView txtScore = findViewById(R.id.textScore);
            String score = "Your score: " + Integer.toString(hangerDanger.getScore());
            txtScore.setText(score);
            String wG = hangerDanger.getWordGuessed();
            TextView txtWord = findViewById(R.id.textWord);
            txtWord.setText(wG);
            if (hangerDanger.getWord().equals(hangerDanger.getWordGuessed())) {
                hideGameplayButtons();
                int temp = hangerDanger.getScore();
                hangerDanger.setScore(temp + 500);
                findViewById(R.id.background_main).setBackground(getDrawable(R.drawable.bg_nextstage));
                findViewById(R.id.textContinueScore).setVisibility(View.VISIBLE);
                TextView textContinueScore = findViewById(R.id.textContinueScore);
                textContinueScore.setText(Integer.toString(hangerDanger.getScore()));
                findViewById(R.id.buttonContinue).setVisibility(View.VISIBLE);
            }
        }
        else {
            switch (hangerDanger.getNumGuess()) {
                case 1: {
                    findViewById(R.id.background_main).setBackground(getDrawable(R.drawable.bg_gameplay_1));
                    break;
                }
                case 2: {
                    findViewById(R.id.background_main).setBackground(getDrawable(R.drawable.bg_gameplay_2));
                    break;
                }
                case 3: {
                    findViewById(R.id.background_main).setBackground(getDrawable(R.drawable.bg_gameplay_3));
                    break;
                }
                case 4: {
                    findViewById(R.id.background_main).setBackground(getDrawable(R.drawable.bg_gameplay_4));
                    break;
                }

                default: { // lose scenario.
                    hideGameplayButtons();
                    findViewById(R.id.background_main).setBackground(getDrawable(R.drawable.bg_lose));
                    CountDownTimer timer = new CountDownTimer(3000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            oopsYouLose(0);
                        }
                    };
                    timer.start();
                }
            }
        }
    }






    public void btnOnClickProcess(View view) {
        Button button = (Button) view;
        String str = button.getText().toString();
        //
        char t = button.getText().toString().charAt(0);
        String str2 = "";
        if (!button.getText().toString().toLowerCase().equals(""))
            checkUserInput(button.getText().toString().toLowerCase().charAt(0));
        button.setVisibility(View.INVISIBLE);
    }

    public void btnOnClickDraw(View view) {
        final Dialog drawDialog = new Dialog (this);
        drawDialog.setContentView(R.layout.dialog_draw);
        LinearLayout linearLayout = drawDialog.findViewById(R.id.canvas);
        Button btnReset = drawDialog.findViewById(R.id.refresh);
        View sketchSheetView = new SketchSheetView(GameActivity.this);
        drawDialog.findViewById(R.id.Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawDialog.dismiss();
            }
        });
        drawDialog.findViewById(R.id.Reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset canvas.
            }
        });
        drawDialog.findViewById(R.id.Confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        drawDialog.show();
        // Convert to text.
        String s = null;
        if (s == null || s.isEmpty() || s.trim().isEmpty()) {
            final AlertDialog.Builder denyDialog = new AlertDialog.Builder(this);
            denyDialog.setTitle("Error")
                    .setMessage("You didn't insert anything")
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            denyDialog.show();
        }
        else {
            AlertDialog.Builder confirmDialog = new AlertDialog.Builder(this);
            confirmDialog.setMessage(s).setTitle("The letter that you draw is")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    }).setNeutralButton("Redraw", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            confirmDialog.show();
        }
    }

    public void btnOnClickSpeak(View view) {
        final Dialog dialog = new Dialog(GameActivity.this);
        txtCache = '\u0000';
        dialog.setContentView(R.layout.dialog_speech);
        final TextView txtUserInput = dialog.findViewById(R.id.whatUserSaid);
        dialog.findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = Character.toString(txtCache);
                txtUserInput.setText(s.toUpperCase());
            }
        });
        dialog.findViewById(R.id.pressMeToSpeak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput(dialog);
            }
        });
        dialog.findViewById(R.id.Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.Reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtUserInput.setText("");
            }
        });
        dialog.findViewById(R.id.Confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Character c1 = txtUserInput.getText().toString().toLowerCase().charAt(0);
                Character c2 = txtCache;
                if (!txtUserInput.getText().toString().isEmpty() && c1.equals(c2) ) {
                    checkUserInput(txtUserInput.getText().toString().toLowerCase().charAt(0));
                }
                else {
                    Toast.makeText(getBaseContext(),"You forgot to refresh!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please note: ").setMessage("- Due to a bug, after you finished saying something, please press the refresh button to see what you said.\n- If you say a (string of) word(s) (e.g. Apple Banana Cheese), only the first letter of what you said will be accepted (in this case, A).")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog2, int which) {
                    }
                });
        builder.create();
        builder.show();
    }


    private void startVoiceInput(Dialog dialog) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now! (Only the first character will be accepted!)");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
            TextView txtUserInput = dialog.findViewById(R.id.whatUserSaid);
            txtUserInput.setText(Character.toString(txtCache));
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this,"Sorry, your device does not support speech to text! :(.", Toast.LENGTH_SHORT).show();
            findViewById(R.id.btnSpeak).setVisibility(GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                            try {
                                txtCache = result.get(0).charAt(0);
                            }
                            catch (NullPointerException e) {
                                // This means that user does not input anything. So we skip it.
                            }
                }
                break;
            }

        }
    }

    ScoreBoard scoreBoard;
    public void btnOnClickHint(View view) {
        findViewById(R.id.hint).setVisibility(GONE);
        if (getHintCount == 1) {
            setButtonHintAnimation(10000);
            TextView txtHint2 = findViewById(R.id.textHint2);
            txtHint2.setText(hint2.get(hangerDanger.getWordIndex()));
        }
        else {
            TextView txtHint3 = findViewById(R.id.textHint3);
            txtHint3.setText(hint3.get(hangerDanger.getWordIndex()));
        }
    }

    public void oopsYouLose(int counter) {
        List<ScoreBoard> list = GameActivity.db.ScoreBoardDao().loadScoreBoard();
        final int gameNumber = list.size() + 1;
        int lowestScore = GameActivity.db.ScoreBoardDao().getLowestScore();
        if (list.size() < 10 || hangerDanger.getScore() > lowestScore) {
            if (hangerDanger.getScore() > lowestScore)
                GameActivity.db.ScoreBoardDao().deleteLowestValue();
            findViewById(R.id.background_main).setBackground(getDrawable(R.drawable.bg_submitname));
            findViewById(R.id.editTextName).setVisibility(View.VISIBLE);
            findViewById(R.id.buttonConfirmName).setVisibility(View.VISIBLE);
            findViewById(R.id.buttonConfirmName).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = findViewById(R.id.editTextName);
                    String name = editText.getText().toString();
                    editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            hideKeyboard(v);
                        }
                    });
                    scoreBoard = new ScoreBoard(gameNumber, name, hangerDanger.getScore());
                    GameActivity.db.ScoreBoardDao().insert(scoreBoard);
                }
            });
        }
        findViewById(R.id.buttonConfirmName).setVisibility(GONE);
        findViewById(R.id.editTextName).setVisibility(GONE);
        findViewById(R.id.background_main).setBackground(getDrawable(R.drawable.bg_blank));
        final ListView listView = findViewById(R.id.listView);
        findViewById(R.id.textView).setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);
        listView.setAdapter(new ScoreBoardAdapter(getBaseContext(), R.layout.scoreboard_item,GameActivity.db.ScoreBoardDao().loadScoreBoard()));
        CountDownTimer countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                listView.setVisibility(GONE);
                findViewById(R.id.textView).setVisibility(GONE);
                findViewById(R.id.background_main).setBackground(getDrawable(R.drawable.bg_tryagain));
                // setBG for 2 other buttons.
                findViewById(R.id.tryAgain).setVisibility(View.VISIBLE);
                findViewById(R.id.stopPlaying).setVisibility(View.VISIBLE);
            }
        };
        countDownTimer.start();
    }

    public void btnOnClickNextStage(View view) {
        generateGame();
        appearGameplayButtons();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void btnOnClickTryAgain(View view) {
        newGame();
        appearGameplayButtons();
    }

    public void btnBackToMainMenu(View view) {
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        GameActivity.this.finish();
        startActivity(intent);
    }
}

