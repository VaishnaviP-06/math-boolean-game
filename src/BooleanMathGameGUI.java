import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class BooleanMathGameGUI extends JFrame {
    static Random rand = new Random();
    static List<ScoreEntry> leaderboard = new ArrayList<>();
    
    // Initialize with some demo data
    static {
        leaderboard.add(new ScoreEntry("Alston", 280, 95));
        leaderboard.add(new ScoreEntry("Vaishnavi", 190, 120));
        leaderboard.add(new ScoreEntry("Yash", 150, 135));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomeFrame().setVisible(true));
    }
    
    static void saveScore(String playerName, int score, int timeTaken) {
        leaderboard.add(new ScoreEntry(playerName, score, timeTaken));
    }
    
    static List<ScoreEntry> getTopScores(int limit) {
        // Sort by score descending and return top entries
        return leaderboard.stream()
                .sorted((a, b) -> Integer.compare(b.score, a.score))
                .limit(limit)
                .toList();
    }
    
    static String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return minutes + "m " + secs + "s";
    }
}

// Score Entry class
class ScoreEntry {
    String playerName;
    int score;
    int timeTaken;
    
    ScoreEntry(String playerName, int score, int timeTaken) {
        this.playerName = playerName;
        this.score = score;
        this.timeTaken = timeTaken;
    }
}

// ==================== WELCOME FRAME ====================
class WelcomeFrame extends JFrame {
    private javax.swing.Timer animationTimer;
    private Random animRand = new Random();
    private String[] binaryNumbers = new String[30];
    private int[] yPositions = new int[30];
    private int[] speeds = new int[30];
    
    public WelcomeFrame() {
        setTitle("Boolean Math Game - Welcome");
        setSize(700, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Initialize binary numbers at random positions
        for (int i = 0; i < binaryNumbers.length; i++) {
            binaryNumbers[i] = animRand.nextBoolean() ? "0" : "1";
            yPositions[i] = animRand.nextInt(650);
            speeds[i] = 1 + animRand.nextInt(3);
        }
        
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Animated gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(15, 23, 42),
                    getWidth(), getHeight(), new Color(30, 41, 59)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Animated scrolling binary numbers
                g2d.setFont(new Font("Monospace", Font.BOLD, 36));
                for (int i = 0; i < binaryNumbers.length; i++) {
                    int alpha = 30 + animRand.nextInt(50);
                    g2d.setColor(new Color(99, 102, 241, alpha));
                    int xPos = (i * 60) % getWidth();
                    g2d.drawString(binaryNumbers[i], xPos, yPositions[i]);
                }
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        
        // Animation timer
        animationTimer = new javax.swing.Timer(50, e -> {
            for (int i = 0; i < yPositions.length; i++) {
                yPositions[i] += speeds[i];
                if (yPositions[i] > getHeight()) {
                    yPositions[i] = -20;
                    binaryNumbers[i] = animRand.nextBoolean() ? "0" : "1";
                }
            }
            mainPanel.repaint();
        });
        animationTimer.start();
        
        // Title with glow effect
        JLabel titleLabel = new JLabel("BOOLEAN MATH GAME");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(new Color(99, 102, 241));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Master the Logic Gates!");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        subtitleLabel.setForeground(new Color(148, 163, 184));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Game info with icons and styling
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(30, 41, 59, 200));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(99, 102, 241), 2),
            BorderFactory.createEmptyBorder(25, 40, 25, 40)
        ));
        infoPanel.setMaximumSize(new Dimension(500, 320));
        
        String[] info = {
            "⏱  Level 1: EASY - 60 seconds (AND, OR, NOT)",
            "⏱  Level 2: MEDIUM - 45 seconds (+ XOR, NAND, NOR)",
            "⏱  Level 3: HARD - 30 seconds (Complex 3-var ops)",
            "",
            "✓ Answer all questions correctly to advance",
            "✓ Complete before time runs out!",
            "✗ Wrong answer = -50% score penalty (continue playing)",
            "✗ Time up = Game Over!"
        };
        
        for (String line : info) {
            JLabel label = new JLabel(line);
            if (line.isEmpty()) {
                infoPanel.add(javax.swing.Box.createVerticalStrut(10));
                continue;
            }
            label.setFont(new Font("Arial", Font.PLAIN, 15));
            label.setForeground(Color.WHITE);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            label.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
            infoPanel.add(label);
        }
        
        // Name input with styling
        JPanel namePanel = new JPanel(new FlowLayout());
        namePanel.setOpaque(false);
        JLabel nameLabel = new JLabel("Enter Your Name: ");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(Color.WHITE);
        JTextField nameField = new JTextField(15);
        nameField.setFont(new Font("Arial", Font.PLAIN, 18));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(99, 102, 241), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        
        // Animated start button
        JButton startButton = new JButton("START GAME");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setBackground(new Color(99, 102, 241));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(99, 102, 241), 3),
            BorderFactory.createEmptyBorder(15, 40, 15, 40)
        ));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(79, 82, 221));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(99, 102, 241));
            }
        });
        
        startButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your name!", "Name Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            animationTimer.stop();
            new LevelFrame(name, 1, 0, 0).setVisible(true);
            dispose();
        });
        
        // Add components with spacing
        mainPanel.add(javax.swing.Box.createVerticalStrut(15));
        mainPanel.add(titleLabel);
        mainPanel.add(javax.swing.Box.createVerticalStrut(10));
        mainPanel.add(subtitleLabel);
        mainPanel.add(javax.swing.Box.createVerticalStrut(30));
        mainPanel.add(infoPanel);
        mainPanel.add(javax.swing.Box.createVerticalStrut(25));
        mainPanel.add(namePanel);
        mainPanel.add(javax.swing.Box.createVerticalStrut(20));
        mainPanel.add(startButton);
        
        add(mainPanel);
    }
}

// ==================== LEVEL FRAME ====================
class LevelFrame extends JFrame {
    String playerName;
    int level;
    int currentQuestion = 0;
    int totalQuestions;
    int score = 0;
    int totalScore;
    
    int timeRemaining;
    int timeLimit;
    int totalTimeTaken = 0;
    javax.swing.Timer gameTimer;
    
    int x, y, z;
    String currentOp;
    int correctAnswer;
    
    // Store previous question to avoid duplicates
    String previousQuestion = "";
    
    JLabel levelLabel, questionCountLabel, scoreLabel, timerLabel;
    JLabel xLabel, yLabel, zLabel, questionLabel;
    JTextField answerField;
    JButton submitButton;
    
    Color[] bgColors = {
        new Color(15, 23, 42),
        new Color(17, 24, 39),
        new Color(22, 30, 46)
    };
    
    Color[] accentColors = {
        new Color(34, 197, 94),
        new Color(234, 179, 8),
        new Color(239, 68, 68)
    };
    
    String[][] levelOperators = {
        {"AND", "OR", "NOT"},
        {"AND", "OR", "NOT", "XOR", "NAND", "NOR"},
        {"COMPLEX"}
    };
    
    public LevelFrame(String name, int lvl, int previousScore, int previousTimeTaken) {
        this.playerName = name;
        this.level = lvl;
        this.totalScore = previousScore;
        this.totalTimeTaken = previousTimeTaken;
        
        // Set time limit and questions per level
        if (level == 1) {
            totalQuestions = 6;
            timeLimit = 60;
        } else if (level == 2) {
            totalQuestions = 5;
            timeLimit = 45;
        } else {
            totalQuestions = 3;
            timeLimit = 30;
        }
        
        timeRemaining = timeLimit;
        
        setTitle("Boolean Math Game - Level " + level);
        setSize(700, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(bgColors[level - 1]);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Top panel - Level info
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(bgColors[level - 1]);
        
        String difficulty = level == 1 ? "EASY" : level == 2 ? "MEDIUM" : "HARD";
        levelLabel = new JLabel("LEVEL " + level + " - " + difficulty);
        levelLabel.setFont(new Font("Arial", Font.BOLD, 32));
        levelLabel.setForeground(accentColors[level - 1]);
        
        JPanel rightInfo = new JPanel(new GridLayout(3, 1));
        rightInfo.setBackground(bgColors[level - 1]);
        
        timerLabel = new JLabel("Time: " + timeRemaining + "s");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        timerLabel.setForeground(new Color(239, 68, 68));
        
        questionCountLabel = new JLabel("Question: 1/" + totalQuestions);
        questionCountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionCountLabel.setForeground(Color.WHITE);
        
        scoreLabel = new JLabel("Score: " + totalScore);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setForeground(Color.WHITE);
        
        rightInfo.add(timerLabel);
        rightInfo.add(questionCountLabel);
        rightInfo.add(scoreLabel);
        
        topPanel.add(levelLabel, BorderLayout.WEST);
        topPanel.add(rightInfo, BorderLayout.EAST);
        
        // Center panel - Question area
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(30, 41, 59));
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColors[level - 1], 3),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        
        // Values panel
        JPanel valuesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        valuesPanel.setBackground(new Color(30, 41, 59));
        xLabel = new JLabel("x = ?");
        xLabel.setFont(new Font("Monospace", Font.BOLD, 32));
        xLabel.setForeground(accentColors[level - 1]);
        yLabel = new JLabel("y = ?");
        yLabel.setFont(new Font("Monospace", Font.BOLD, 32));
        yLabel.setForeground(accentColors[level - 1]);
        zLabel = new JLabel("z = ?");
        zLabel.setFont(new Font("Monospace", Font.BOLD, 32));
        zLabel.setForeground(accentColors[level - 1]);
        zLabel.setVisible(false);
        
        valuesPanel.add(xLabel);
        valuesPanel.add(yLabel);
        valuesPanel.add(zLabel);
        
        // Question label
        questionLabel = new JLabel("Question will appear here", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 22));
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Answer panel
        JPanel answerPanel = new JPanel(new FlowLayout());
        answerPanel.setBackground(new Color(30, 41, 59));
        JLabel ansLabel = new JLabel("Your Answer (0 or 1): ");
        ansLabel.setFont(new Font("Arial", Font.BOLD, 18));
        ansLabel.setForeground(Color.WHITE);
        answerField = new JTextField(8);
        answerField.setFont(new Font("Monospace", Font.BOLD, 24));
        answerField.setHorizontalAlignment(JTextField.CENTER);
        answerField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColors[level - 1], 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        answerPanel.add(ansLabel);
        answerPanel.add(answerField);
        
        // Submit button
        submitButton = new JButton("SUBMIT");
        submitButton.setFont(new Font("Arial", Font.BOLD, 18));
        submitButton.setBackground(accentColors[level - 1]);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColors[level - 1], 2),
            BorderFactory.createEmptyBorder(12, 50, 12, 50)
        ));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        submitButton.addActionListener(e -> checkAnswer());
        answerField.addActionListener(e -> checkAnswer());
        
        centerPanel.add(valuesPanel);
        centerPanel.add(javax.swing.Box.createVerticalStrut(30));
        centerPanel.add(questionLabel);
        centerPanel.add(javax.swing.Box.createVerticalStrut(30));
        centerPanel.add(answerPanel);
        centerPanel.add(javax.swing.Box.createVerticalStrut(20));
        centerPanel.add(submitButton);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        
        // Start timer
        gameTimer = new javax.swing.Timer(1000, e -> {
            timeRemaining--;
            totalTimeTaken++;
            timerLabel.setText("Time: " + timeRemaining + "s");
            
            if (timeRemaining <= 10) {
                timerLabel.setForeground(Color.RED);
            }
            
            if (timeRemaining <= 0) {
                gameTimer.stop();
                JOptionPane.showMessageDialog(this, 
                    "Time's Up!\nYou ran out of time!",
                    "Game Over", JOptionPane.ERROR_MESSAGE);
                showGameOver();
            }
        });
        gameTimer.start();
        
        nextRound();
    }
    
    void nextRound() {
        currentQuestion++;
        questionCountLabel.setText("Question: " + currentQuestion + "/" + totalQuestions);
        
        String newQuestion;
        int attempts = 0;
        
        // Keep generating until we get a different question (max 10 attempts to avoid infinite loop)
        do {
            // Generate random values (0 or 1)
            x = BooleanMathGameGUI.rand.nextInt(2);
            y = BooleanMathGameGUI.rand.nextInt(2);
            z = BooleanMathGameGUI.rand.nextInt(2);
            
            if (level == 3) {
                int opType = BooleanMathGameGUI.rand.nextInt(6);
                switch (opType) {
                    case 0:
                        correctAnswer = (x & y) | z;
                        newQuestion = "(x AND y) OR z = ? [x=" + x + ",y=" + y + ",z=" + z + "]";
                        break;
                    case 1:
                        correctAnswer = (x | y) & z;
                        newQuestion = "(x OR y) AND z = ? [x=" + x + ",y=" + y + ",z=" + z + "]";
                        break;
                    case 2:
                        correctAnswer = (x ^ y) & z;
                        newQuestion = "(x XOR y) AND z = ? [x=" + x + ",y=" + y + ",z=" + z + "]";
                        break;
                    case 3:
                        correctAnswer = x & (y | z);
                        newQuestion = "x AND (y OR z) = ? [x=" + x + ",y=" + y + ",z=" + z + "]";
                        break;
                    case 4:
                        correctAnswer = (x & y) ^ z;
                        newQuestion = "(x AND y) XOR z = ? [x=" + x + ",y=" + y + ",z=" + z + "]";
                        break;
                    case 5:
                        correctAnswer = ((x & y) == 1 ? 0 : 1) | z;
                        newQuestion = "NOT(x AND y) OR z = ? [x=" + x + ",y=" + y + ",z=" + z + "]";
                        break;
                    default:
                        newQuestion = "";
                }
            } else {
                String[] ops = levelOperators[level - 1];
                currentOp = ops[BooleanMathGameGUI.rand.nextInt(ops.length)];
                
                if (currentOp.equals("AND")) {
                    correctAnswer = x & y;
                    newQuestion = "x AND y = ? [x=" + x + ",y=" + y + "]";
                } else if (currentOp.equals("OR")) {
                    correctAnswer = x | y;
                    newQuestion = "x OR y = ? [x=" + x + ",y=" + y + "]";
                } else if (currentOp.equals("XOR")) {
                    correctAnswer = x ^ y;
                    newQuestion = "x XOR y = ? [x=" + x + ",y=" + y + "]";
                } else if (currentOp.equals("NAND")) {
                    correctAnswer = (x & y) == 1 ? 0 : 1;
                    newQuestion = "x NAND y = ? [x=" + x + ",y=" + y + "]";
                } else if (currentOp.equals("NOR")) {
                    correctAnswer = (x | y) == 1 ? 0 : 1;
                    newQuestion = "x NOR y = ? [x=" + x + ",y=" + y + "]";
                } else if (currentOp.equals("NOT")) {
                    if (BooleanMathGameGUI.rand.nextBoolean()) {
                        correctAnswer = (x == 1 ? 0 : 1);
                        newQuestion = "NOT x = ? [x=" + x + "]";
                    } else {
                        correctAnswer = (y == 1 ? 0 : 1);
                        newQuestion = "NOT y = ? [y=" + y + "]";
                    }
                } else {
                    newQuestion = "";
                }
            }
            
            attempts++;
        } while (newQuestion.equals(previousQuestion) && attempts < 10);
        
        previousQuestion = newQuestion;
        
        // Update UI
        xLabel.setText("x = " + x);
        yLabel.setText("y = " + y);
        
        if (level == 3) {
            zLabel.setVisible(true);
            zLabel.setText("z = " + z);
            
            // Extract the operation from the question string for display
            String displayQuestion = newQuestion.substring(0, newQuestion.indexOf('[') - 1);
            questionLabel.setText(displayQuestion);
        } else {
            zLabel.setVisible(false);
            
            // Extract the operation from the question string for display
            String displayQuestion = newQuestion.substring(0, newQuestion.indexOf('[') - 1);
            questionLabel.setText(displayQuestion);
        }
        
        answerField.setText("");
        answerField.requestFocus();
    }
    
    void checkAnswer() {
        // Disable submit button to prevent multiple clicks
        submitButton.setEnabled(false);
        answerField.setEnabled(false);
        
        try {
            String input = answerField.getText().trim();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter 0 or 1!", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                submitButton.setEnabled(true);
                answerField.setEnabled(true);
                return;
            }
            
            int ans = Integer.parseInt(input);
            if (ans != 0 && ans != 1) {
                JOptionPane.showMessageDialog(this, "Please enter 0 or 1 only!", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                submitButton.setEnabled(true);
                answerField.setEnabled(true);
                return;
            }
            
            if (ans != correctAnswer) {
                // Deduct half of current level score as penalty
                int penalty = score / 2;
                totalScore -= penalty;
                if (totalScore < 0) totalScore = 0;
                
                score -= penalty;
                if (score < 0) score = 0;
                
                scoreLabel.setText("Score: " + totalScore);
                
                JOptionPane.showMessageDialog(this, 
                    "Wrong Answer!\n" +
                    "Correct answer was: " + correctAnswer + "\n" +
                    "Penalty: -" + penalty + " points\n" +
                    "Total Score: " + totalScore + "\n\n" +
                    "Continue with next question...",
                    "Incorrect", JOptionPane.WARNING_MESSAGE);
                
                // Continue to next question instead of ending game
                if (currentQuestion >= totalQuestions) {
                    gameTimer.stop();
                    if (level < 3) {
                        int timeBonus = 5;
                        totalScore += timeBonus;
                        
                        int choice = JOptionPane.showConfirmDialog(this,
                            "Level " + level + " Complete!\n\n" +
                            "Level Score: " + score + "\n" +
                            "Total Score: " + totalScore + "\n" +
                            "Time Bonus: +" + timeBonus + "\n\n" +
                            "Proceed to Level " + (level + 1) + "?",
                            "Level Complete", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                        
                        if (choice == JOptionPane.YES_OPTION) {
                            new LevelFrame(playerName, level + 1, totalScore, totalTimeTaken).setVisible(true);
                            dispose();
                        } else {
                            showGameOver();
                        }
                        return;
                    } else {
                        // Level 3 complete - Game finished
                        int timeBonus = 5;
                        totalScore += timeBonus;
                        JOptionPane.showMessageDialog(this,
                            "CONGRATULATIONS!\n\nYou completed all levels!\n" +
                            "Final Score: " + totalScore + "\n" +
                            "Total Time: " + BooleanMathGameGUI.formatTime(totalTimeTaken),
                            "Game Complete!", JOptionPane.INFORMATION_MESSAGE);
                        showGameOver();
                        return;
                    }
                } else {
                    // More questions in current level
                    submitButton.setEnabled(true);
                    answerField.setEnabled(true);
                    nextRound();
                }
                return;
            } else {
                score += 10 * level;
                totalScore += 10 * level;
                scoreLabel.setText("Score: " + totalScore);
                
                if (currentQuestion >= totalQuestions) {
                    gameTimer.stop();
                    if (level < 3) {
                        int timeBonus = timeRemaining * level * 5;
                        totalScore += timeBonus;
                        
                        int choice = JOptionPane.showConfirmDialog(this,
                            "Level " + level + " Complete!\n\n" +
                            "Level Score: " + score + "\n" +
                            "Total Score: " + totalScore + "\n" +
                            "Time Bonus: +" + timeBonus + "\n\n" +
                            "Proceed to Level " + (level + 1) + "?",
                            "Level Complete", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                        
                        if (choice == JOptionPane.YES_OPTION) {
                            new LevelFrame(playerName, level + 1, totalScore, totalTimeTaken).setVisible(true);
                            dispose();
                        } else {
                            showGameOver();
                        }
                        return;
                    } else {
                        // Level 3 complete - Game finished
                        int timeBonus = timeRemaining * level * 5;
                        totalScore += timeBonus;
                        JOptionPane.showMessageDialog(this,
                            "CONGRATULATIONS!\n\nYou completed all levels!\n" +
                            "Final Score: " + totalScore + "\n" +
                            "Total Time: " + BooleanMathGameGUI.formatTime(totalTimeTaken),
                            "Game Complete!", JOptionPane.INFORMATION_MESSAGE);
                        showGameOver();
                        return;
                    }
                } else {
                    // More questions in current level
                    submitButton.setEnabled(true);
                    answerField.setEnabled(true);
                    nextRound();
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter 0 or 1 only!", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            submitButton.setEnabled(true);
            answerField.setEnabled(true);
        }
    }
    
    void showGameOver() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        BooleanMathGameGUI.saveScore(playerName, totalScore, totalTimeTaken);
        new GameOverFrame(playerName, totalScore, totalTimeTaken).setVisible(true);
        dispose();
    }
}

// ==================== GAME OVER FRAME ====================
class GameOverFrame extends JFrame {
    public GameOverFrame(String playerName, int score, int timeTaken) {
        setTitle("Game Over - Boolean Math Game");
        setSize(700, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(15, 23, 42));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel titleLabel = new JLabel("GAME OVER");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 42));
        titleLabel.setForeground(new Color(239, 68, 68));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel statsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        statsPanel.setBackground(new Color(30, 41, 59));
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(99, 102, 241), 2),
            BorderFactory.createEmptyBorder(20, 40, 20, 40)
        ));
        
        JLabel playerLabel = new JLabel("Player: " + playerName);
        playerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        playerLabel.setForeground(Color.WHITE);
        
        JLabel scoreLabel = new JLabel("Final Score: " + score);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 22));
        scoreLabel.setForeground(new Color(34, 197, 94));
        
        JLabel timeLabel = new JLabel("Total Time: " + BooleanMathGameGUI.formatTime(timeTaken));
        timeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        timeLabel.setForeground(new Color(234, 179, 8));
        
        statsPanel.add(playerLabel);
        statsPanel.add(scoreLabel);
        statsPanel.add(timeLabel);
        
        JLabel leaderTitle = new JLabel("LEADERBOARD - TOP 10");
        leaderTitle.setFont(new Font("Arial", Font.BOLD, 26));
        leaderTitle.setForeground(new Color(99, 102, 241));
        leaderTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        String[] columns = {"Rank", "Player", "Score", "Time"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 15));
        table.setRowHeight(33);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        table.setBackground(new Color(30, 41, 59));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(99, 102, 241));
        table.getTableHeader().setBackground(new Color(99, 102, 241));
        table.getTableHeader().setForeground(Color.WHITE);
        
        List<ScoreEntry> topScores = BooleanMathGameGUI.getTopScores(10);
        int rank = 1;
        for (ScoreEntry entry : topScores) {
            model.addRow(new Object[]{rank, entry.playerName, entry.score, BooleanMathGameGUI.formatTime(entry.timeTaken)});
            rank++;
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(550, 200));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(99, 102, 241), 2));
        
        JButton playAgainButton = new JButton("PLAY AGAIN");
        playAgainButton.setFont(new Font("Arial", Font.BOLD, 18));
        playAgainButton.setBackground(new Color(99, 102, 241));
        playAgainButton.setForeground(Color.WHITE);
        playAgainButton.setFocusPainted(false);
        playAgainButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(99, 102, 241), 2),
            BorderFactory.createEmptyBorder(12, 40, 12, 40)
        ));
        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        playAgainButton.addActionListener(e -> {
            new WelcomeFrame().setVisible(true);
            dispose();
        });
        
        mainPanel.add(titleLabel);
        mainPanel.add(javax.swing.Box.createVerticalStrut(25));
        mainPanel.add(statsPanel);
        mainPanel.add(javax.swing.Box.createVerticalStrut(25));
        mainPanel.add(leaderTitle);
        mainPanel.add(javax.swing.Box.createVerticalStrut(15));
        mainPanel.add(scrollPane);
        mainPanel.add(javax.swing.Box.createVerticalStrut(25));
        mainPanel.add(playAgainButton);
        
        add(mainPanel);
    }
}