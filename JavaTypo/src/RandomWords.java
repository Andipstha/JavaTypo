import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Random;

public class RandomWords extends JPanel {
    private final Counter countdown;
    private Difficulty difficulty;
    public int wrongCount;
    public static int wordCount;
    public static double accuracy;
    private final JTextField wordField;
    public final JLabel wordCountLabel;
    public JComboBox difficultyComboBox;
    public JButton  startButton;
    public JButton soundBtn;
    private boolean isSoundPlaying = false;

// This constructor creates a new `RandomWords` object with the specified `countdown` object.
    public RandomWords(Counter countdown) {

        this.countdown = countdown;
        setLayout(new BorderLayout());

        // Initialize the `wordCount` and `accuracy` variables.
        wordCount = 0;
        accuracy = 100;

        wordCountLabel = new JLabel("WPM: " + wordCount + " Accuracy :" +  (accuracy < 0 ? 0 : String.format("%.0f", accuracy))  + "%");
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
        difficultyComboBox.addItem("Coder");
        add(difficultyComboBox, BorderLayout.SOUTH);

        //Default value of Level
        difficulty = Difficulty.Easy;

        wordField();
        soundEffect();
        StartButton();

    }
    public void StartButton(){
        //START BUTTON
        startButton = new JButton("          Let's Play  ");
        startButton.setBounds(20, 20, 100, 25);
        startButton.setFont(new Font("", Font.BOLD, 30));
        startButton.setBorderPainted(false);
        startButton.setBackground(Color.DARK_GRAY);
        startButton.setFocusPainted(false);

        add(startButton, BorderLayout.WEST);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButton.setVisible(false);
                // Disable the difficulty combo box
                difficultyComboBox.setEnabled(false);
                countdown.start();
                generateWord();
            }
        });
    }
    public void enableComponents(){
        difficultyComboBox.setEnabled(true);
        startButton.setVisible(true);
    }
    public void soundEffect(){
        //SOUND EFFECTS
        soundBtn = new JButton("🔈");
        soundBtn.setPreferredSize(new Dimension(100, 24));

        soundBtn.setFont(new Font("Arial", Font.BOLD, 30));
        soundBtn.setBorderPainted(false);
        soundBtn.setBackground(Color.DARK_GRAY);
        soundBtn.setFocusPainted(false);

        soundBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isSoundPlaying) {
                    try {
                        Sound.backgroundSound(1);
                        isSoundPlaying = true;
                        soundBtn.setText("🔊");

                    } catch (UnsupportedAudioFileException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (LineUnavailableException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    try {
                        Sound.backgroundSound(0);
                    } catch (UnsupportedAudioFileException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (LineUnavailableException ex) {
                        throw new RuntimeException(ex);
                    }
                    isSoundPlaying = false;
                    soundBtn.setText("🔇");
                }
            }
        });

        add(soundBtn, BorderLayout.EAST);

    }
    public void wordField(){
        wordField.requestFocusInWindow();
        wordField.addKeyListener(new WordKeyListener(this));
        wordField.setFont(new Font("Arial", Font.BOLD, 24));
        wordField.setBackground(Color.decode("#EEEEEE"));

    }
    public enum Difficulty {
        Easy,
        Medium,
        Hard,
        Coder
    }
    private void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void generateWord() {

        String[] words;
        switch (difficulty){
            case Easy:
                words = new String[]{"apple", "banana", "cherry", "orange", "pear","dog","cow","sheep","rabbit","bird","snake","worm","bee","frog","horse"};
                break;
            case Medium:
                words = new String[]{"computer", "television", "telephone", "newspaper", "dictionary","message","podcast","snapchat","facebook","instagram","youtube"};
                break;
            case Hard:
                words = new String[]{"Blockchain", "Robotics", "Cyber Security", "Intelligence", "Quantum","Newspaper","Calculator","Microwave","Salamander","Elephant","Crocodile"};
                break;
            case Coder:
                words = new String[]{"integer", "float", "world();", "System.out.println();", "main()","boolean","Calculator","array[]","java();","url","polymorphism"};
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

        int totalPressed = 0;
        int correctPressed = 0;
        @Override
        public void keyTyped(KeyEvent e) {
            totalPressed++;
            if (e.getKeyChar() == wordField.getText().charAt(0)) {
                try {
                    Sound.typeSound();
                } catch (UnsupportedAudioFileException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                }
                correctPressed++;
                wordField.setForeground(Color.black);
                wordField.setText(wordField.getText().substring(1));
                if (wordField.getText().isEmpty()) {
                    generateWord();
                    wordCount++;

                    // Update word count label
                    countdown.setWordCount(wordCount);
                    countdown.setAccuracyCount(accuracy);

                    // Set difficulty
                    Difficulty difficulty = Difficulty.valueOf(difficultyComboBox.getSelectedItem().toString()); //Value need to be change to String
                    randomWords.setDifficulty(difficulty);
                }
            } else {
                wordField.setForeground(Color.red);
                wrongCount++;
            }

            //ACCURACY CALCULATION
            accuracy = ((double) correctPressed /totalPressed)* 100;
            wordCountLabel.setText("WPM: " + wordCount + "     Accuracy: " + (accuracy < 0 ? 0 : String.format("%.0f", accuracy)) + "%");

        }
    }
}
