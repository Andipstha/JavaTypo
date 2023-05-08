import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class RandomWords extends JPanel {
    public final JLabel wordCountLabel;
    private final JTextField wordField;
    private final Counter countdown;
    public JComboBox difficultyComboBox;
    private Difficulty difficulty;
    public int wordCount;
    public int wrongCount;
    public double accuracy;
    public JButton  startButton;

    public RandomWords(Counter countdown) {

        this.countdown = countdown;

        setLayout(new BorderLayout());

        wordCountLabel = new JLabel("WPM: 0     Accuracy: 100%");
        Font labelFont = new Font("Arial", Font.BOLD, 15);
        wordCountLabel.setHorizontalAlignment((SwingConstants.CENTER));
        add(wordCountLabel, BorderLayout.NORTH);

        wordField = new JTextField();
        wordField.setEditable(false);
        Font wordFieldFont = new Font("Arial", Font.PLAIN, 30);
        wordField.setFont(wordFieldFont);
        wordField.setForeground(Color.black);
        wordField.setHorizontalAlignment(SwingConstants.CENTER);
        add(wordField, BorderLayout.CENTER);

        difficultyComboBox = new JComboBox();
        difficultyComboBox.addItem("Easy");
        difficultyComboBox.addItem("Medium");
        difficultyComboBox.addItem("Hard");
        add(difficultyComboBox, BorderLayout.SOUTH);

        //Default value of Level
        difficulty = Difficulty.Easy;

        wordField.requestFocusInWindow();
        wordField.addKeyListener(new WordKeyListener(this));
        wordField.setFont(new Font("Arial", Font.BOLD, 24));


        //START BUTTON
        startButton = new JButton("Start");
        startButton.setBounds(20, 20, 100, 25);
        add(startButton, BorderLayout.WEST);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButton.setVisible(false);
                // Disable the difficulty combo box
                difficultyComboBox.disable();
                countdown.start();
                generateWord();
            }
        });

    }
    public enum Difficulty {
        Easy,
        Medium,
        Hard
    }
    private void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void generateWord() {

        String[] words;
        switch (difficulty){
            case Easy:
                words = new String[]{"apple", "banana", "cherry", "orange", "pear"};
                break;
            case Medium:
                words = new String[]{"computer", "television", "telephone", "newspaper", "dictionary"};
                break;
            case Hard:
                words = new String[]{"blockchain", "robotics", "cybersecurity", "intelligence", "quantum"};
                break;
            default:
                words = new String[]{"apple", "banana", "cherry", "orange", "pear"};
                break;
        }
        Random random = new Random();
        int index = random.nextInt(words.length);
        wordField.setText(words[index]);

    }
    private class WordKeyListener extends KeyAdapter {
        private RandomWords randomWords;

        public WordKeyListener(RandomWords randomWords) {
            this.randomWords = randomWords;
        }
        @Override
        public void keyTyped(KeyEvent e) {

            if (e.getKeyChar() == wordField.getText().charAt(0)) {
                wordField.setForeground(Color.black);
                wordField.setText(wordField.getText().substring(1));
                if (wordField.getText().isEmpty()) {
                    generateWord();
                    wordCount++;

                    // Update word count label
                    countdown.setWordCount(wordCount);

                    // Set difficulty
                    Difficulty difficulty = Difficulty.valueOf(difficultyComboBox.getSelectedItem().toString()); //Value need to be change to String
                    randomWords.setDifficulty(difficulty);
                }
            } else {
                wordField.setForeground(Color.red);
                wrongCount++;
            }

            //ACCURACY CALCULATION
            int correctCount = wordCount - wrongCount;
            accuracy = ((double) correctCount /wordCount)* 100;
//            wordCountLabel.setText("WPM: " + wordCount + "Accuracy: "+ accuracy +"%");
            wordCountLabel.setText("WPM: " + wordCount + "     Accuracy: " + (accuracy < 0 ? 0 : String.format("%.0f", accuracy)) + "%");

        }
    }
}
