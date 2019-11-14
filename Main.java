import java.awt.Color; 
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

/** 
 * A game based on the mobile game "Nom Cat". The player tries to catch fish in the mouths of catswhile avoiding bombs.
 * 
 * This class is the main class of the game, creating most of the labels and implementing most of the logic.
 *
 * @author Julia
 * @author Victoria
 * @version %I% %G%
 */
public class Main extends JFrame implements ActionListener, KeyListener{ // Main class for the game

	// FIELDS:
	
	/** 
	 * The levels available to play in an enum
	 */
	private enum Difficulty {EASY, MEDIUM, HARD}; 
	private Difficulty difficulty; // Difficulty level is initialized

	private final double SCREENX, SCREENY; // Dimensions of the screen
	private JLabel lblTitle, lblScores, lblEnterUsername, lblCannotPlay, lblChooseDifficulty, lblLeaderboardsNames, lblInstructions, lblChooseCat, lblUsernameRules; // Labels for title and scores 
	private JButton btnExit, btnPlay, btnCat1, btnCat2, btnCat3, btnCat4, btnDifficultyEasy, btnDifficultyMedium, btnDifficultyHard, btnLeaderboards, btnInstructions, btnGo, btnGameOver; // Buttons are created for exit, play, and choosing the cats
	private JLayeredPane layeredPane;
	private Sound sound; // Create an object of the Sound class
	private Cat cat1, cat2; // Create cat objects of the Cat class
	private Timer t; // Create a timer
	private TimerTask task; // Create a timer task
	// Create array lists to store fish coming out from left side and coming out from right side
	private ArrayList<Fish> fishLeft = new ArrayList<Fish>();
	private ArrayList<Fish> fishRight = new ArrayList<Fish>();
	private ArrayList<String> leaderboards = new ArrayList<String>(); // Create an array list to store the top 5 leaders
	private Fish fish1, fish2; // Create two fish objects 
	private int scores, chooseCat1, chooseCat2, counter; // Create a variable to store scores and choose which cat 
	private JTextField textUsername; // Creates text field for entering username 
	private String userName, txtFile, top5names = ""; 
	private final int LEADERBOARDS_LIMIT = 5; // Creates a constant for the number of people on the leaderboards 
	private final ImageIcon GREY_CAT, WHITE_CAT, BROWN_CAT, ORANGE_CAT, DARK_LAYER, TITLE, EXIT, BUTTON, BACKGROUND_IMAGE;
	private Random r;
	// Creates custom colours to match theme
	private final Color GOLD = new Color(250, 222, 86);
	private final Color LIGHTPURPLE = new Color(170, 144, 255);
	// fields

	// METHODS:

	public static void main(String[] args) { // main method, constructs the game
		Main game = new Main(); // constructs new game
		game.setVisible(true); // sets it to visible
	}

	public Main () { // constructor, gets the screen size and images, constructs the sound class and sets up the main menu screen.

		SCREENX = (double) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		SCREENY = (double) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		// sets these variables to the maximum screen size

		BACKGROUND_IMAGE = new ImageIcon("background image.jpg");
		GREY_CAT = resizeCat("greycatclosemouth.PNG");
		WHITE_CAT = resizeCat("whitecatclosemouth.PNG");
		BROWN_CAT = resizeCat("browncatclosemouth.PNG");
		ORANGE_CAT = resizeCat("orangecatclosemouth.PNG");
		DARK_LAYER = new ImageIcon(new ImageIcon("rectangle.png").getImage().getScaledInstance((int)SCREENX, (int)SCREENY, Image.SCALE_DEFAULT));
		TITLE = new ImageIcon(new ImageIcon("Title Image.PNG").getImage().getScaledInstance((int) (SCREENX/2), (int) (SCREENX/2), Image.SCALE_DEFAULT));
		EXIT = new ImageIcon(new ImageIcon("Exit Button.PNG").getImage().getScaledInstance((int) (SCREENX/9), (int) (SCREENX/9), Image.SCALE_DEFAULT));
		BUTTON = new ImageIcon(new ImageIcon("Button.PNG").getImage().getScaledInstance((int) (SCREENX/6), (int) (SCREENX/36), Image.SCALE_DEFAULT));
		// sets all the ImageIcons to their appropriate images

		difficulty = null; // sets difficulty to null 
		txtFile = "leaderboards.txt"; // Text file refers to leaderboards text file 
		sound = new Sound(); // initializes the sound 
		r = new Random(); // initializes the Random object
		setUp(); // sets up the jlabels and jbuttons for the game

	}

	private void setUp() { // sets up the jlabels, jbuttons etc needed in the menus

		setResizable(false);
		setUndecorated(true);
		setTitle("Nom Cat 2.0");
		setBounds(0, 0, (int)SCREENX, (int)SCREENY);
		getContentPane().setLayout(null);
		setContentPane(new JLabel(BACKGROUND_IMAGE));
		setFocusable(true);
		// sets up the jframe

		// LABELS:

		lblTitle = new JLabel(TITLE);
		lblTitle.setOpaque(false);
		lblTitle.setBounds((int) (SCREENX/4), 0, TITLE.getIconWidth(), TITLE.getIconHeight());
		lblTitle.setVisible(true);
		getContentPane().add(lblTitle);
		// sets up the jlabel for the title

		lblCannotPlay = new JLabel();
		lblCannotPlay.setHorizontalAlignment(SwingConstants.CENTER);
		lblCannotPlay.setForeground(new Color(255, 255, 255));
		lblCannotPlay.setFont(new Font("Verdana", Font.PLAIN, (int) (SCREENY/50)));
		lblCannotPlay.setBounds((int) (SCREENX/2-BUTTON.getIconWidth()/2), (int) (SCREENY/14*13), BUTTON.getIconWidth(), BUTTON.getIconHeight());
		lblCannotPlay.setVisible(false);
		getContentPane().add(lblCannotPlay);
		// tells the player if they can't play

		lblChooseDifficulty = new JLabel(DARK_LAYER);
		lblChooseDifficulty.setText("1. Choose Difficulty:");
		lblChooseDifficulty.setHorizontalTextPosition(SwingConstants.CENTER);
		lblChooseDifficulty.setVerticalAlignment(lblChooseDifficulty.CENTER);
		lblChooseDifficulty.setFont(new Font("Verdana", Font.PLAIN, (int) (SCREENY/50)));
		lblChooseDifficulty.setForeground(new Color(255, 255, 255));
		lblChooseDifficulty.setVisible(false);
		lblChooseDifficulty.setBounds((int) (SCREENX/13), (int) (SCREENY/10*3), (int) (SCREENX/13*3), (int) (SCREENY/20));
		getContentPane().add(lblChooseDifficulty);
		// tells to select difficulty

		lblChooseCat = new JLabel(DARK_LAYER);
		lblChooseCat.setText("2. Choose 2 Cats to Play With:");
		lblChooseCat.setHorizontalTextPosition(SwingConstants.CENTER);
		lblChooseCat.setVerticalAlignment(lblChooseCat.CENTER);
		lblChooseCat.setFont(new Font("Verdana", Font.PLAIN, (int) (SCREENY/50)));
		lblChooseCat.setForeground(new Color(255, 255, 255));
		lblChooseCat.setBounds((int) (SCREENX/13*5), (int) (SCREENY/10*3), (int) (SCREENX/13*3), (int) (SCREENY/20));
		lblChooseCat.setVisible(false);
		getContentPane().add(lblChooseCat);
		// tells to select their cats

		lblEnterUsername = new JLabel(DARK_LAYER);
		lblEnterUsername.setText("3. Enter Username:");
		lblEnterUsername.setHorizontalTextPosition(SwingConstants.CENTER);
		lblEnterUsername.setVerticalAlignment(lblEnterUsername.CENTER);
		lblEnterUsername.setForeground(new Color(255, 255, 255));
		lblEnterUsername.setFont(new Font("Verdana", Font.PLAIN, (int) (SCREENY/50)));
		lblEnterUsername.setVisible(false);
		lblEnterUsername.setBounds((int) (SCREENX/13*9), (int) (SCREENY/10*3), (int) (SCREENX/13*3), (int) (SCREENY/20));
		getContentPane().add(lblEnterUsername);
		// sets up label to prompt user to enter username

		lblUsernameRules = new JLabel("<html><center>Username must be less than 10 characters.");
		lblUsernameRules.setHorizontalTextPosition(SwingConstants.CENTER);
		lblUsernameRules.setVerticalAlignment(lblUsernameRules.CENTER);
		lblUsernameRules.setForeground(new Color(255, 255, 255));
		lblUsernameRules.setFont(new Font("Verdana", Font.PLAIN, (int) (SCREENY/50)));
		lblUsernameRules.setVisible(false);
		lblUsernameRules.setBounds((int) (SCREENX/156*113), (int) (SCREENY/5*2), (int) (SCREENX/6), (int) (SCREENY/10));
		getContentPane().add(lblUsernameRules);
		// sets up label for the username rules

		lblLeaderboardsNames = new JLabel(DARK_LAYER);
		lblLeaderboardsNames.setFont(new Font("Lucida Console", Font.PLAIN, (int) (SCREENY/25)));
		lblLeaderboardsNames.setHorizontalTextPosition(lblLeaderboardsNames.CENTER);
		lblLeaderboardsNames.setForeground(LIGHTPURPLE);
		lblLeaderboardsNames.setBounds((int)(SCREENX/6), (int)(SCREENY/6), (int)(SCREENX/3*2), (int)(SCREENY/2));
		// to display the leaderboards 

		lblInstructions = new JLabel(DARK_LAYER);
		lblInstructions.setText("<html>"
				+ "<CENTER> <body> <B> Instructions </B> "
				+ "<P> <br> Welcome to NomCat! <br> Try to eat as many fish as <br> you can by opening the cats' <br>mouths using your left and right keys. <br> Don't miss any and don't eat <br> any bombs or you'll lose. <br>"
				+ " <P> </P> <B> Press Play to choose your cats, choose a username and set the difficulty of the game. </B> </P>"
				+ "</body> </CENTER>"
				+ "</html>");
		lblInstructions.setBounds((int)(SCREENX/6), (int)(SCREENY/6), (int)(SCREENX/3*2), (int)(SCREENY/2));
		lblInstructions.setFont(new Font("Lucida Console", Font.PLAIN, (int) (SCREENY/30)));
		lblInstructions.setForeground(GOLD);
		lblInstructions.setVerticalAlignment(SwingConstants.CENTER);
		lblInstructions.setHorizontalTextPosition(btnInstructions.CENTER);
		lblInstructions.setVisible(false);
		getContentPane().add(lblInstructions);
		// displays the instructions on how to play 

		// BUTTONS:

		btnDifficultyEasy = new JButton(BUTTON);
		btnDifficultyEasy.setText("Easy");
		btnDifficultyEasy.setEnabled(false);
		btnDifficultyEasy.setVisible(false);
		btnDifficultyEasy.setForeground(GOLD);
		btnDifficultyEasy.setBorderPainted(false);
		btnDifficultyEasy.setFont(new Font("Verdana", Font.PLAIN, (int) (SCREENY/40)));
		btnDifficultyEasy.setHorizontalTextPosition(btnDifficultyEasy.CENTER);
		btnDifficultyEasy.setVerticalAlignment(SwingConstants.CENTER);
		btnDifficultyEasy.setBounds((int) (SCREENX/156*17), (int) (SCREENY/8*3), BUTTON.getIconWidth(), BUTTON.getIconHeight());
		btnDifficultyEasy.addActionListener(new ActionListener() { // when clicked
			@Override
			public void actionPerformed(ActionEvent e) {
				sound.playButton();
				btnDifficultyEasy.setForeground(Color.WHITE);
				btnDifficultyMedium.setForeground(GOLD);
				btnDifficultyHard.setForeground(GOLD);
				difficulty = Difficulty.EASY;
			}
		});
		getContentPane().add(btnDifficultyEasy);
		// sets up the jbutton for selecting difficulty easy

		btnDifficultyMedium = new JButton(BUTTON);
		btnDifficultyMedium.setText("Medium");
		btnDifficultyMedium.setEnabled(false);
		btnDifficultyMedium.setVisible(false);
		btnDifficultyMedium.setForeground(GOLD);
		btnDifficultyMedium.setBorderPainted(false);
		btnDifficultyMedium.setFont(new Font("Verdana", Font.PLAIN, (int) (SCREENY/40)));
		btnDifficultyMedium.setHorizontalTextPosition(btnDifficultyMedium.CENTER);
		btnDifficultyMedium.setVerticalAlignment(SwingConstants.CENTER);
		btnDifficultyMedium.setBounds((int) (SCREENX/156*17), (int) (SCREENY/8*3)+(2*BUTTON.getIconHeight()), BUTTON.getIconWidth(), BUTTON.getIconHeight());
		btnDifficultyMedium.addActionListener(new ActionListener() { // when clicked
			@Override
			public void actionPerformed(ActionEvent e) {
				sound.playButton();
				btnDifficultyEasy.setForeground(GOLD);
				btnDifficultyMedium.setForeground(Color.WHITE);
				btnDifficultyHard.setForeground(GOLD);
				difficulty = Difficulty.MEDIUM;
			}
		});
		getContentPane().add(btnDifficultyMedium);
		// sets up jbutton for selecting difficulty medium 

		btnDifficultyHard = new JButton(BUTTON);
		btnDifficultyHard.setText("Hard");
		btnDifficultyHard.setEnabled(false);
		btnDifficultyHard.setVisible(false);
		btnDifficultyHard.setForeground(GOLD);
		btnDifficultyHard.setBorderPainted(false);
		btnDifficultyHard.setHorizontalTextPosition(btnDifficultyHard.CENTER);
		btnDifficultyHard.setFont(new Font("Verdana", Font.PLAIN, (int) (SCREENY/40)));
		btnDifficultyHard.setVerticalAlignment(SwingConstants.CENTER);
		btnDifficultyHard.setBounds((int) (SCREENX/156*17), (int) (SCREENY/8*3)+(4*BUTTON.getIconHeight()), BUTTON.getIconWidth(), BUTTON.getIconHeight());
		btnDifficultyHard.addActionListener(new ActionListener() { // when clicked
			@Override
			public void actionPerformed(ActionEvent e) {
				sound.playButton();
				btnDifficultyEasy.setForeground(GOLD);
				btnDifficultyMedium.setForeground(GOLD);
				btnDifficultyHard.setForeground(Color.WHITE);
				difficulty = Difficulty.HARD;
			}
		});
		getContentPane().add(btnDifficultyHard);	
		// sets up jbutton for selecting difficulty hard

		btnCat1 = new JButton(GREY_CAT);
		btnCat1.setOpaque(false);
		btnCat1.setContentAreaFilled(false);
		btnCat1.setBorderPainted(false);
		btnCat1.setVisible(false);
		btnCat1.setEnabled(true);
		btnCat1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sound.playButton();
				if (chooseCat1==0) {
					pickCat1(1);
				}
				else if (chooseCat2==0) {
					pickCat2(1);
				}
				else if (chooseCat1!=0 && chooseCat2!=0) {
					pickCat1(1);
					pickCat2(0);
				}
			}
		});
		getContentPane().add(btnCat1).setBounds((int) (SCREENX/13*5), (int) (SCREENY/2), GREY_CAT.getIconWidth(), GREY_CAT.getIconHeight());
		// sets up a cat selection button 

		btnCat2 = new JButton(WHITE_CAT);
		btnCat2.setOpaque(false);
		btnCat2.setContentAreaFilled(false);
		btnCat2.setBorderPainted(false);
		btnCat2.setVisible(false);
		btnCat2.setEnabled(true);
		btnCat2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sound.playButton();
				if (chooseCat1==0) {
					pickCat1(2);
				}
				else if (chooseCat2==0) {
					pickCat2(2);
				}
				else if (chooseCat1!=0 && chooseCat2!=0) {
					pickCat1(2);
					pickCat2(0);
				}
			}
		});
		getContentPane().add(btnCat2).setBounds((int) (SCREENX/52*21), (int) (SCREENY/20*7), WHITE_CAT.getIconWidth(), WHITE_CAT.getIconHeight());
		// sets up a cat selection button 

		btnCat3 = new JButton(BROWN_CAT);
		btnCat3.setOpaque(false);
		btnCat3.setVisible(false);
		btnCat3.setContentAreaFilled(false);
		btnCat3.setBorderPainted(false);
		btnCat3.setEnabled(true);
		btnCat3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sound.playButton();
				if (chooseCat1==0) {
					pickCat1(3);
				}
				else if (chooseCat2==0) {
					pickCat2(3);
				}
				else if (chooseCat1!=0 && chooseCat2!=0) {
					pickCat1(3);
					pickCat2(0);
				}
			}
		});
		getContentPane().add(btnCat3).setBounds((int) (SCREENX/52*31)-BROWN_CAT.getIconWidth(), (int) (SCREENY/2), BROWN_CAT.getIconWidth(), BROWN_CAT.getIconHeight());
		// sets up a cat selection button

		btnCat4 = new JButton(ORANGE_CAT);
		btnCat4.setOpaque(false);
		btnCat4.setContentAreaFilled(false);
		btnCat4.setBorderPainted(false);
		btnCat4.setVisible(false);
		btnCat4.setEnabled(true);
		btnCat4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sound.playButton();
				if (chooseCat1==0) {
					pickCat1(4);
				}
				else if (chooseCat2==0) {
					pickCat2(4);
				}
				else if (chooseCat1!=0 && chooseCat2!=0) {
					pickCat1(4);
					pickCat2(0);
				}
			}
		});
		getContentPane().add(btnCat4).setBounds((int) (SCREENX/13*8)-ORANGE_CAT.getIconWidth(), (int) (SCREENY/20*7), ORANGE_CAT.getIconWidth(), ORANGE_CAT.getIconHeight());
		// sets up a cat selection button

		btnGo = new JButton(BUTTON);
		btnGo.setText("Go");
		btnGo.setVisible(false);
		btnGo.setEnabled(false);
		btnGo.setForeground(GOLD);
		btnGo.setHorizontalTextPosition(btnGo.CENTER);
		btnGo.setFont(new Font("Verdana", Font.PLAIN, 20));
		btnGo.setVerticalAlignment(SwingConstants.CENTER);
		btnGo.setBounds((int) (SCREENX/2-BUTTON.getIconWidth()/2), (int) (SCREENY/7*6), BUTTON.getIconWidth(), BUTTON.getIconHeight());
		btnGo.addActionListener(new ActionListener() {
			@Override
			// checks if the play button is pressed 
			public void actionPerformed(ActionEvent e) {
				sound.playButton();
				// disable the button 
				textUsername.setVisible(true);
				if (!textUsername.getText().isEmpty() && textUsername.getText().length()<10 && chooseCat1>0 && chooseCat2>0 && difficulty!=null) {
					lblCannotPlay.setVisible(false);
					userName = textUsername.getText();
					removeEverything();
					sound.stopAll();
					play();
				}
				else if (difficulty==null) { // gives warning if difficulty is not selected
					lblCannotPlay.setText("Please select difficulty");
					lblCannotPlay.setVisible(true);
				}
				else if (chooseCat1==0 || chooseCat2==0) { // gives warning if cats are not selected 
					lblCannotPlay.setText("Please choose your cats");
					lblCannotPlay.setVisible(true);
				}
				else { // gives warning if username is not valid
					lblCannotPlay.setText("Enter a valid username"); 
					lblCannotPlay.setVisible(true);
				}
			}
		});
		getContentPane().add(btnGo);
		// sets up the Go button to begin the game 

		btnPlay = new JButton(BUTTON);
		btnPlay.setText("Play");
		btnPlay.setForeground(GOLD);
		btnPlay.setHorizontalTextPosition(btnPlay.CENTER);
		btnPlay.setFont(new Font("Verdana", Font.PLAIN, 20));
		btnPlay.setVerticalAlignment(SwingConstants.CENTER);
		btnPlay.setBounds((int) (SCREENX/2-BUTTON.getIconWidth()/2), (int) (SCREENY/7*6), BUTTON.getIconWidth(), BUTTON.getIconHeight());
		btnPlay.addActionListener( new ActionListener() {
			@Override
			// checks if the play button is pressed 
			public void actionPerformed(ActionEvent e) {
				sound.playButton();
				// disable the button 
				removeEverything();
				textUsername.setVisible(true);
				lblChooseCat.setVisible(true);
				btnCat1.setVisible(true);
				btnCat1.setEnabled(true);
				btnCat2.setVisible(true);
				btnCat2.setEnabled(true);
				btnCat3.setVisible(true);
				btnCat3.setEnabled(true);
				btnCat4.setVisible(true);
				btnCat4.setEnabled(true);
				lblChooseDifficulty.setVisible(true);
				btnDifficultyEasy.setVisible(true);
				btnDifficultyEasy.setEnabled(true);
				btnDifficultyMedium.setVisible(true);
				btnDifficultyMedium.setEnabled(true);
				btnDifficultyHard.setVisible(true);
				btnDifficultyHard.setEnabled(true);
				lblEnterUsername.setVisible(true);
				lblUsernameRules.setVisible(true);
				btnGo.setVisible(true);
				btnGo.setEnabled(true);
				// SHOULD THIS BE IN  A METHOD
			}
		});
		getContentPane().add(btnPlay);
		// adds jbutton to play, which from there you can choose cats, username and difficulty 

		btnLeaderboards = new JButton(BUTTON);
		btnLeaderboards.setText("Leaderboards");
		btnLeaderboards.setEnabled(true);
		btnLeaderboards.setForeground(GOLD);
		btnLeaderboards.setFont(new Font("Verdana", Font.PLAIN, 20));
		btnLeaderboards.setVerticalAlignment(SwingConstants.CENTER);
		btnLeaderboards.setHorizontalTextPosition(btnLeaderboards.CENTER);
		btnLeaderboards.setBounds((int) (SCREENX/6), (int) (SCREENY/7*6), BUTTON.getIconWidth(), BUTTON.getIconHeight());
		btnLeaderboards.addActionListener(new ActionListener() { // when clicked
			@Override
			public void actionPerformed(ActionEvent e) {
				sound.playButton();
				removeEverything();
				btnExit.setText("Go Back");
				Scanner s=null;
				try {
					s = new Scanner(new File("leaderboards.txt"));
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				while (s.hasNext()){
					leaderboards.add(s.next());
				}
				s.close();
				for (int i=0; i<leaderboards.size();i+=3) {
					top5names+= "<tr><td align=center>"  + (i/3+1) + "." + 
							"</td><td align=center>"  + leaderboards.get(i) +
							"</td><td align=center>" + leaderboards.get(i+1) +
							"</td><td align=center>" +  leaderboards.get(i+2) +
							"</td></tr>";
				}
				lblLeaderboardsNames.setText("<html><center><b>LEADERBOARDS</b><br><br><table><tr>"
						+ "<th>&nbsp;&nbsp;Place&nbsp;&nbsp;</th>"
						+ "<th>&nbsp;&nbsp;Name&nbsp;&nbsp;</th>"
						+ "<th>&nbsp;&nbsp;Score&nbsp;&nbsp;</th>"
						+ "<th>&nbsp;&nbsp;Difficulty&nbsp;&nbsp;</th>" 
						+ top5names + "<table>");
				lblLeaderboardsNames.setVisible(true);
				getContentPane().add(lblLeaderboardsNames);
			}
		});
		getContentPane().add(btnLeaderboards);
		// sets up the jbutton for the  exit button

		btnInstructions = new JButton(BUTTON);
		btnInstructions.setText("Instructions");
		btnInstructions.setEnabled(true);
		btnInstructions.setForeground(GOLD);
		btnInstructions.setFont(new Font("Verdana", Font.PLAIN, 20));
		btnInstructions.setVerticalAlignment(SwingConstants.CENTER);
		btnInstructions.setHorizontalTextPosition(btnLeaderboards.CENTER);
		btnInstructions.setBounds((int) (SCREENX/6*5-BUTTON.getIconWidth()), (int) (SCREENY/7*6), BUTTON.getIconWidth(), BUTTON.getIconHeight());
		btnInstructions.addActionListener(new ActionListener() { // when clicked
			@Override
			public void actionPerformed(ActionEvent e) {
				sound.playButton();
				removeEverything();
				btnExit.setText("Go Back");
				lblInstructions.setVisible(true);
			}
		});
		getContentPane().add(btnInstructions);
		// sets up the jbutton for the instructions button

		btnExit = new JButton(EXIT);
		btnExit.setText("Exit");
		btnExit.setEnabled(true);
		btnExit.setForeground(GOLD);
		btnExit.setOpaque(false);
		btnExit.setContentAreaFilled(false);
		btnExit.setBorderPainted(false);
		btnExit.setFont(new Font("Verdana", Font.PLAIN, (int)(SCREENY/30)));
		btnExit.setHorizontalTextPosition(btnExit.CENTER);
		btnExit.setVerticalAlignment(SwingConstants.CENTER);
		btnExit.setBounds((int) (SCREENX/8*7), (int) (SCREENY/25), EXIT.getIconWidth(), EXIT.getIconHeight());
		btnExit.addActionListener(new ActionListener() { // when clicked
			@Override
			public void actionPerformed(ActionEvent e) {
				sound.playButton();
				if (btnExit.getText().equals("Go Back")) { 
					lblTitle.setVisible(true);
					btnInstructions.setVisible(true);
					btnInstructions.setEnabled(true);
					btnLeaderboards.setVisible(true);
					btnLeaderboards.setEnabled(true);
					btnPlay.setVisible(true);
					btnPlay.setEnabled(true);
					lblLeaderboardsNames.setVisible(false);
					top5names = "";
					leaderboards.clear();
					lblInstructions.setVisible(false);
					btnExit.setText("Exit");
				}
				else
					exit();
			}
		});
		getContentPane().add(btnExit);
		// sets up the jbutton for the  exit button

		btnGameOver = new JButton(DARK_LAYER);
		btnGameOver.setText("GAME OVER");
		btnGameOver.setEnabled(false);
		btnGameOver.setVisible(false);
		btnGameOver.setForeground(GOLD);
		btnGameOver.setOpaque(false);
		btnGameOver.setContentAreaFilled(false);
		btnGameOver.setBorderPainted(false);
		btnGameOver.setFont(new Font("Verdana", Font.PLAIN, (int) (SCREENY/15)));
		btnGameOver.setHorizontalTextPosition(btnGameOver.CENTER);
		btnGameOver.setVerticalAlignment(SwingConstants.CENTER);
		btnGameOver.setBounds((int)(SCREENX/3), (int)(SCREENY/4), (int)(SCREENX/3), (int)(SCREENY/3));
		btnGameOver.addActionListener(new ActionListener() { // when clicked
			@Override
			public void actionPerformed(ActionEvent e) {
				sound.playButton();
				makeNewGame();
				exit();
			}
		});
		getContentPane().add(btnGameOver);
		// sets up jbutton for replaying

		// OTHER:

		textUsername = new JTextField();
		textUsername.setFont((new Font("Verdana", Font.PLAIN, (int) (SCREENY/40))));
		textUsername.setHorizontalAlignment(SwingConstants.CENTER);
		textUsername.setBounds((int) (SCREENX/156*113), (int) (SCREENY/2), (int) (SCREENX/6), (int) (SCREENY/16));
		getContentPane().add(textUsername);
		textUsername.setVisible(false);
		textUsername.setColumns(10);
		// sets up text field for username
		
		layeredPane = new JLayeredPane();
		layeredPane.setBounds(0, 0, (int)SCREENX, (int)SCREENY);
		layeredPane.setVisible(true);
		getContentPane().add(layeredPane);
		//this is a jlayeredpane to make the fish overlap the cat

		sound.playMenuMusic();
		// plays menu music

		addKeyListener(this);
		// adds the KeyListener

	}

	private void removeEverything () { // removes everything from the main screen to begin the game 
		lblTitle.setVisible(false);
		btnPlay.setEnabled(false);
		btnPlay.setVisible(false);
		textUsername.setVisible(false);
		lblEnterUsername.setVisible(false);
		lblCannotPlay.setVisible(false);
		btnCat1.setEnabled(false);
		btnCat1.setVisible(false);
		btnCat2.setEnabled(false);
		btnCat2.setVisible(false);
		btnCat3.setEnabled(false);
		btnCat3.setVisible(false);
		btnCat4.setEnabled(false);
		btnCat4.setVisible(false);
		btnLeaderboards.setVisible(false);
		btnLeaderboards.setEnabled(false);
		btnInstructions.setVisible(false);
		btnInstructions.setEnabled(false);
		lblChooseDifficulty.setVisible(false);
		btnDifficultyEasy.setVisible(false);
		btnDifficultyEasy.setEnabled(false);
		btnDifficultyMedium.setVisible(false);
		btnDifficultyMedium.setEnabled(false);
		btnDifficultyHard.setVisible(false);
		btnDifficultyHard.setEnabled(false);
		btnGo.setVisible(false);
		btnGo.setEnabled(false);
		lblChooseCat.setVisible(false);
		lblEnterUsername.setVisible(false);
		lblUsernameRules.setVisible(false);
	}

	private void pickCat1 (int choice) { // choosing the left cat 
		chooseCat1 = choice;

		if (cat1 != null) {
			layeredPane.remove(cat1);
		//	remove(cat1);
		}

		if (choice > 0) {
			cat1 = new Cat(choice, 1, SCREENX, SCREENY);
			layeredPane.add(cat1, JLayeredPane.DEFAULT_LAYER);
			//getContentPane().add(cat1);
		}
		repaint();
	}

	private void pickCat2 (int choice) { // choosing the right cat 
		chooseCat2 = choice;

		if (cat2 != null) {
			layeredPane.remove(cat2);
		//	remove(cat2);
		}

		if (choice > 0) {
			cat2 = new Cat(choice, 2, SCREENX, SCREENY);
			layeredPane.add(cat2, JLayeredPane.DEFAULT_LAYER);
			//getContentPane().add(cat2);
		}
		repaint();
	}

	private void play() { // when the game begins 

		sound.playGameMusic();
		
		lblScores = new JLabel(BUTTON);
		lblScores.setVerticalAlignment(SwingConstants.TOP);
		lblScores.setOpaque(false);
		lblScores.setForeground(GOLD);
		lblScores.setFont(new Font("Times New Roman", Font.PLAIN, (int) (SCREENX/35)));
		lblScores.setText(scores+"");
		lblScores.setHorizontalTextPosition(lblScores.CENTER);
		lblScores.setBounds((int) (SCREENX/5*2), (int) (SCREENY/8), (int) (SCREENX/5), (int) (SCREENY/16));
		lblScores.setVisible(true);
		getContentPane().add(lblScores);
		// sets up jlabel for the score

		switch (difficulty) { // the difficulty is in 3 levels 
		case EASY: 
			sendOutFish(3);
			break; 
		case MEDIUM:
			sendOutFish(2);
			break;
		case HARD:
			sendOutFish(1);
		}
		// sends out the fish

		repaint();
		// repaints the new additions
	}

	private void sendOutFish(int speed) { // creates the fish, adds them to the screen and adds them to an arraylist 
		
		task = new TimerTask() { // start the timertask 
			public void run() { 
				
				counter++;
				lblScores.setText(scores+"");

				if (counter%(70*speed)==0){
					if (r.nextInt(5) == 1) {
						fish1 = new Fish(SCREENX, SCREENY, (int)(cat1.getX()+(cat1.getWidth()/2.0)), (int)(cat2.getX()+(cat2.getWidth())/2), cat2.getY(), false, true);
						if (r.nextBoolean()) {
							fish1 = new Fish(SCREENX, SCREENY, (int)(cat1.getX()+(cat1.getWidth()/2.0)), (int)(cat2.getX()+(cat2.getWidth())/2), cat2.getY(), true, true);
						}
						layeredPane.add(fish1, JLayeredPane.POPUP_LAYER);
						fishLeft.add(fish1);
					}
					if (r.nextInt(5) == 1) {
						fish2 = new Fish(SCREENX, SCREENY, (int)(cat2.getX()+(cat2.getWidth()/2.0)), (int)(cat1.getX()+(cat1.getWidth())/2), cat1.getY(), false, false);
						if (r.nextBoolean()) {
							fish2 = new Fish(SCREENX, SCREENY, (int)(cat2.getX()+(cat2.getWidth()/2.0)), (int)(cat1.getX()+(cat1.getWidth())/2), cat1.getY(), true, false);
						}
						layeredPane.add(fish2, JLayeredPane.POPUP_LAYER);
						fishRight.add(fish2);
					}
				}

				for (int i=0; i<fishLeft.size(); i++) {
					if (checkDeath(fishLeft.get(i), cat2, true)) {
						sound.stopAll();
						t.cancel();
						lose();
						break;
					}
				}
				for (int i=0; i<fishRight.size(); i++) {
					if (checkDeath(fishRight.get(i), cat1, false)) {
						sound.stopAll();
						t.cancel();
						lose();
						break;
					}
				}

				if (fish1!=null) {
					for (int i = 0; i < fishLeft.size(); i++)
						fishLeft.get(i).move((4-speed)+(counter/10000.0));
				}
				if (fish2!=null) {
					for (int i = 0; i < fishRight.size(); i++)
						fishRight.get(i).move((4-speed)+(counter/10000.0));
				}
				repaint();
			}
		};
		t = new Timer();
		t.schedule(task, 0, 1);
	}

	private void lose() { // method runs to terminate the game
		
		sound.playGameOver(); // plays the sound of game over
		
		if (t != null)
			t.cancel();
		// cancels the timertask

		cat1.cry(); 
		cat2.cry();
		// sets the images of the cats to animated crying

		updateScore(userName, scores, difficulty.toString()); // updates the high scores

		sound.stopAll(); // stops the background music

		btnGameOver.setText("<html><center>GAME OVER!<br><font size=+10>Your score: " + (scores) + "</font><br><font size=+2>Click here to return to the main menu.</font>");
		btnGameOver.setEnabled(true);
		btnGameOver.setVisible(true);
		// displays the game over button so the user can replay

		//creates a new game with an updated high score
	}

	private void exit() { // method that terminates everything
		
		if (t != null)
			t.cancel();
		// cancels the timertask
		
		sound.stopAll(); // stops the sound

		super.dispose(); // removes current game
	}

	private void makeNewGame() { // makes new game
		
		Main newMain = new Main();
		newMain.setVisible(true);
		// creates new game
	}

	private boolean checkDeath(Fish fish, Cat cat, boolean fishFromLeft) { // checks if a fish has not been caught or a bomb has been eaten

		if (fish!=null && fish.getY()>(cat.getY()+cat.getHeight()/9) && fish.getY()<(cat.getY()+cat.getHeight()/4)) {
			fish.setVisible(false);
			if (cat.isOpen){
				if (fish.isBomb) {
					return true;
				}
				else{
					scores++;
				}
			}
			if (!cat.isOpen) {
				if (!fish.isBomb) {
					return true;
				}
			}
			else {
				if (fishFromLeft) {
					fishLeft.remove(fish);
				}
				if (!fishFromLeft) {
					fishRight.remove(fish);
				}
				sound.playChew();
			}	
		}
		return false;
	}

	private ImageIcon resizeCat(String cat) { // resizes the cat images

		return new ImageIcon(new ImageIcon(cat).getImage().getScaledInstance((int)(SCREENX/10), (int)(SCREENX/20*3), Image.SCALE_DEFAULT));
	}

	@Override
	public void actionPerformed(ActionEvent e) { // not used
	}

	@Override
	public void keyTyped(KeyEvent e) { // not used
	}

	@Override
	public void keyPressed(KeyEvent e) { // opens the cats mouths if key is pressed

		if (e.getKeyCode() == KeyEvent.VK_LEFT && cat1!=null) {
			if (!cat1.isCrying)
				cat1.openMouth();
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT && cat2!=null) {
			if (!cat2.isCrying)
				cat2.openMouth();
		}
	}

	@Override
	public void keyReleased(KeyEvent e){ // closes the cats mouths if the key is released

		if (e.getKeyCode() == KeyEvent.VK_LEFT && cat1!=null) {
			if (!cat1.isCrying)
				cat1.closeMouth();
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT && cat2!=null) {
			if (!cat2.isCrying)
				cat2.closeMouth();
		}
	}

	private void updateScore(String userName, int score, String difficulty) { // updates the leaderboards with top scores
		
		// importing classes that allow writing and reading a file
		FileWriter fw;
		BufferedWriter bw;
		FileReader fr;
		BufferedReader br;
		// creating an array list to store the scores of players, and for top 5 positions 
		ArrayList <String> storeScores = new ArrayList <String>(5);

		try {
			try {
				// allow the file reader to read the leaderboards file 
				fr = new FileReader(txtFile); 
				br = new BufferedReader(fr);
				// initialize the variables index and line
				int index = 0;
				String line = null;
				// check if there are lines to be read, and if the index is less than 5, which acts as a counter in the loop in order to keep the array to 5 indexes
				while((line = br.readLine()) != null && index<5) {
					// the scores, usernames and difficulties are being saved into the array list
					storeScores.add(index, line);
					index++;
				} 

				// close the buffered reader
				br.close();
				// overwrites on the file 
				fw = new FileWriter(txtFile, false); 
				// initialize the buffered writer
				bw = new BufferedWriter(fw);    
				String newScore = "";
				//checks through every score on the leaderboards to compare to the new player score and whether to update or not
				for (int i=0; i <LEADERBOARDS_LIMIT; i++){   
					// trim any spaces
					storeScores.get(i).trim();
					// set position to the index of the space 
					int position = storeScores.get(i).indexOf(" "); 
					// set the string number to the score part of the line, not including the username or the difficulty
					String number = storeScores.get(i).substring(position+1, storeScores.get(i).lastIndexOf(" "));
					// assign number to thescore and change it to an int
					int thescore = Integer.parseInt(number);
					// checks if the player scores higher than any score on the leaderboards
					if (score>thescore) {
						// newScore becomes the username and their score and the difficulty they played at
						newScore=(userName + " " + score + " " + difficulty +"\n" );
						// assign score to 0 so it will not enter the loop
						score= 0;
						// decrement i by 1 to not miss a name on the list 
						i--;
					}
					else  {
						// set newscore to what is was previously on the leaderboards
						newScore = storeScores.get(i).substring(0, storeScores.get(i).lastIndexOf(" "))+ " "+ storeScores.get(i).substring(storeScores.get(i).lastIndexOf(" ")+1) + "\n" ;
					}               
					// use buffered writer to write newScore into the file
					bw.write(newScore);     
				}
			}finally {
			}
			bw.close();
			// close the buffered writer 

		} catch (IOException e) {
			e.printStackTrace();
		}   
	}

}