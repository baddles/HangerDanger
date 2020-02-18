package baddles.hangerdanger.hangerdanger;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class HangerDanger {
    private String word;
    private static final int maxNum = 5;
    private int numGuess;
    private int combo;
    private int score;
    private String wordGuessed;
    private ArrayList<Character> inputtedWords;
    private int wordIndex;
    HangerDanger () {
        this.word = "";
        numGuess = 0;
        score = 0;
        combo = 0;
        wordGuessed = "";
        inputtedWords = new ArrayList<>();
        wordIndex = 0;
    }

    public void generateGame(String word) {
        this.word = word;
        numGuess = 0;
        combo = 0;
        wordGuessed = "";
        for (int i = 0; i < this.word.length(); i++) {
            Character c = this.word.charAt(i);
            if (Character.isSpaceChar(c)) {
                wordGuessed += ' ';
            } else {
                wordGuessed += '_';
            }
            if (inputtedWords == null) {
                inputtedWords = new ArrayList<>();
            }
        }
    }

    public void newGame(String word) {
        this.word = word;
        numGuess = 0;
        score = 0;
        combo = 0;
        for (int i = 0; i < this.word.length(); i++) {
            Character c = this.word.charAt(i);
            if (Character.isSpaceChar(c)) {
                wordGuessed += ' ';
            } else {
                wordGuessed += '_';
            }
            if (inputtedWords == null) {
                inputtedWords = new ArrayList<>();
            }
        }
    }

    public ArrayList<Integer> atWhichChars (char c) {
        inputtedWords.add(c);
        ArrayList<Integer> toReturn = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            Character c2 = word.charAt(i);
            if (c2.equals(c)) {
                toReturn.add(i);
            }
        }
        //
        toReturn.size();
        return toReturn;
    }

    public void addInputtedWords (char c) {
        inputtedWords.add(c);
    }
    void correctGuess() {
        combo++;
        if (combo > 1) {
            score+=(5 * combo);
        }
        score += 10;
    }

    void wrongGuess(String s) {
        combo = 0;
        this.word = s;
        numGuess++;
    }


    public boolean checkUserInput(char c) {
        ArrayList<Integer> position = atWhichChars(c);
        addInputtedWords(c);
        String s = "";
        if (position.size() > 0) {
            int index = 0;
            for (int i = 0; i < getWord().length(); i++) {
                if (index < position.size()) {
                    position.size();
                    int j = position.get(index);
                    if (i == j) {
                        s += c;
                        index++;
                    }
                    else {
                        if (Character.isSpaceChar(getWord().charAt(i))) {
                            s += ' ';
                        }
                        else {
                            Character c1 = getWordGuessed().charAt(i);
                            if (!c1.equals('_')) {
                                s += getWordGuessed().charAt(i);
                            }
                            else { s += '_'; }
                        }
                    }
                }
                else {
                    if (Character.isSpaceChar(getWord().charAt(i))) {
                        s += ' ';
                    }
                    else {
                        Character c1 = getWordGuessed().charAt(i);
                        if (!c1.equals('_')) {
                            s += getWordGuessed().charAt(i);
                        }
                        else { s += '_'; }
                    }
                }
            }
            correctGuess();
            setWordGuessed(s);
            return true;
        }
        else {
            numGuess++;
            combo = 0;
        }
        return false;
    }

















    public static int getMaxNum() {
        return maxNum;
    }

    public int getNumGuess() {
        return numGuess;
    }

    public void setNumGuess(int numGuess) {
        this.numGuess = numGuess;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getCombo() {
        return combo;
    }

    public void setCombo(int combo) {
        this.combo = combo;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getWordGuessed() {
        return wordGuessed;
    }

    public void setWordGuessed(String wordGuessed) {
        this.wordGuessed = wordGuessed;
    }

    public ArrayList<Character> getInputtedWords () {
        return inputtedWords;
    }

    public void setWordIndex(int wordIndex) {
        this.wordIndex = wordIndex;
    }

    public int getWordIndex() {
        return wordIndex;
    }
}
