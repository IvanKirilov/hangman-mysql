package GamesDemo;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Hangman extends JFrame {

	private JPanel mainPanel;
	private JLabel word, stick1, stick2,
	stick3, head, lHand, rHand, body, lLeg, rLeg,
	usedLetters, alphabet;
	private JTextField letter;
	private JButton submit, newGame;
	private List<String> words;
	private int lives = 6;
	private String realWord, fakeWord;
	private ListenForButton lb;
	private static final String URL = "jdbc:mysql://localhost:3306/geography";
	private static final String USER = "root";
	private static final String PASS = "1234";
	private Connection connection;
	private String[] allLetters;

	public static void main(String[] args) throws Exception {
		new Hangman();
	}

	public void choseWord() {
		int idx = new Random().nextInt(words.size());
		realWord = words.get(idx);
	}

	public void printLeftLeg() {
		lLeg = new JLabel();
		lLeg.setBounds(75, 230, 70, 5);
		lLeg.setBackground(Color.black);
		lLeg.setOpaque(true);
		mainPanel.add(lLeg);
		mainPanel.repaint();
	}

	public void printRightLeg() {
		rLeg = new JLabel();
		rLeg.setBounds(145, 230, 70, 5);
		rLeg.setBackground(Color.black);
		rLeg.setOpaque(true);
		mainPanel.add(rLeg);
		mainPanel.repaint();
	}

	public void printLeftHand() {
		lHand = new JLabel();
		lHand.setBounds(75, 140, 70, 5);
		lHand.setBackground(Color.black);
		lHand.setOpaque(true);
		mainPanel.add(lHand);
		mainPanel.repaint();
	}

	public void printRightHand() {
		rHand = new JLabel();
		rHand.setBounds(145, 140, 70, 5);
		rHand.setBackground(Color.black);
		rHand.setOpaque(true);
		mainPanel.add(rHand);
		mainPanel.repaint();
	}

	public void printHead() {
		head = new JLabel();
		head.setBounds(123, 83, 40, 40);
		head.setBackground(Color.black);
		head.setOpaque(true);
		mainPanel.add(head);
		mainPanel.repaint();
	}

	public void printBody() {
		body = new JLabel();
		body.setBounds(142, 83, 6, 150);
		body.setBackground(Color.black);
		body.setOpaque(true);
		mainPanel.add(body);
		mainPanel.repaint();
	}

	public void prepareDB() throws SQLException{
		this.connection =
				DriverManager.getConnection(
						URL, USER, PASS);
	}
	
	public boolean isOkey(String str){
		
		for(char p : str.toCharArray()){
			if(p < 'a' || p > 'z'){
				if(p != ' ' && p != '-'){
					return false;
				}
			}
		}
		
		return true;
	}
	
	public List<String> getAllDBCapitals() throws SQLException{
		
		List<String> result = new ArrayList<>();
		
		String query = "SELECT country_name FROM countries;";
		
		Statement st = connection.createStatement();
		
		ResultSet rs = st.executeQuery(query);
		
		while(rs.next()){
			
			String country = rs.getString("country_name");
			country = country.toLowerCase();
			
			if(isOkey(country)) {
				result.add(country);
			}
			
		}
		
		return result;
	}
	
	public Hangman() throws Exception {
		this.setTitle("Hangman");
		this.setSize(500, 500);
		this.setLocation(50, 50);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		prepareDB();
		
		lb = new ListenForButton();

		words = new ArrayList<>();
		words = getAllDBCapitals();

		choseWord();

		mainPanel = new JPanel();
		mainPanel.setBackground(Color.CYAN);
		mainPanel.setLayout(null);

		alphabet = new JLabel();
		allLetters = new String[26];
		for(int i=0;i<26;i++) allLetters[i] = (char)(i+97) + "";
		alphabet.setText(String.join(" ", allLetters));
		alphabet.setBounds(120, 400, 500, 30);
		mainPanel.add(alphabet);
		
		stick1 = new JLabel();
		stick1.setBounds(20, 50, 6, 240);
		stick1.setBackground(Color.BLACK);
		stick1.setOpaque(true);
		mainPanel.add(stick1);

		stick2 = new JLabel();
		stick2.setBounds(20, 50, 120, 6);
		stick2.setBackground(Color.BLACK);
		stick2.setOpaque(true);
		mainPanel.add(stick2);

		stick3 = new JLabel();
		stick3.setBounds(140, 50, 6, 36);
		stick3.setBackground(Color.BLACK);
		stick3.setOpaque(true);
		mainPanel.add(stick3);

		letter = new JTextField();
		letter.setBounds(300, 50, 50, 30);
		letter.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent ev) {

				if (ev.getKeyChar() == KeyEvent.VK_ENTER) {
					submit.doClick();
					
				}
			}

			@Override
			public void keyReleased(KeyEvent ev) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent ev) {
				// TODO Auto-generated method stub

			}
		});
		mainPanel.add(letter);

		submit = new JButton("Submit");
		submit.setBounds(300, 100, 100, 30);
		submit.addActionListener(lb);
		mainPanel.add(submit);

		newGame = new JButton(" New Game");
		newGame.setBounds(300, 300, 100, 30);
		newGame.addActionListener(lb);
		mainPanel.add(newGame);

		usedLetters = new JLabel();
		usedLetters.setBounds(300, 150, 100, 30);
		mainPanel.add(usedLetters);

		fakeWord = "";
		for (int i = 0; i < realWord.length(); i++) {
			if(realWord.charAt(i) == ' ') fakeWord += " ";
			else if(realWord.charAt(i) == '-') fakeWord += "-";
			else fakeWord += "_";
		}

		String[] dividedFakeWord = fakeWord.split("");
		
		word = new JLabel(fakeWord);
		word.setText(String.join(" ", dividedFakeWord));
		word.setBounds(300, 200, 100, 20);
		mainPanel.add(word);

		this.add(mainPanel);
		this.setVisible(true);
	}

	private class ListenForButton implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == submit) {

				char p = letter.getText().charAt(0);

				String oldVal = usedLetters.getText();
				usedLetters.setText(oldVal + p + " ");
				allLetters[p - 97] = "";

				if (realWord.contains(p + "")) {
					for (int i = 0; i < realWord.length(); i++) {
						if (realWord.charAt(i) == p) {
							fakeWord = fakeWord.substring(0, i) + p + fakeWord.substring(i + 1, fakeWord.length());
						}
					}
					
					String[] dividedFakeWord = fakeWord.split("");
					word.setText(String.join(" ", dividedFakeWord));

					if (!fakeWord.contains("_")) {
						usedLetters.setText("You won!!!");
						submit.setEnabled(false);
					}
				} else {

					lives--;

					switch (lives) {
					case 5:
						printHead();
						break;
					case 4:
						printBody();
						break;
					case 3:
						printLeftHand();
						break;
					case 2:
						printRightHand();
						break;
					case 1:
						printLeftLeg();
						break;
					case 0:
						printRightLeg();
						usedLetters.setText("You lost!!!");
						word.setText(realWord);
						submit.setEnabled(false);
						break;
					}

				}
				letter.setText("");
				alphabet.setText(String.join(" ", allLetters));
			}
			if (e.getSource() == newGame) {
				submit.setEnabled(true);
				if (head != null) {
					mainPanel.remove(head);
				}
				if (body != null) {
					mainPanel.remove(body);
				}
				if (lHand != null) {
					mainPanel.remove(lHand);
				}
				if (rHand != null) {
					mainPanel.remove(rHand);
				}
				if (lLeg != null) {
					mainPanel.remove(lLeg);
				}
				if (rLeg != null) {
					mainPanel.remove(rLeg);
				}

				choseWord();
				lives = 6;
				word.setText(fakeWord);
				
				fakeWord = "";
				for (int i = 0; i < realWord.length(); i++) {
					if(realWord.charAt(i) == ' ') fakeWord += " ";
					else if(realWord.charAt(i) == '-') fakeWord += "-";
					else fakeWord += "_";
				}
				
				String[] dividedFakeWord = fakeWord.split("");
				word.setText(String.join(" ", dividedFakeWord));
				
				for(int i=0;i<26;i++) allLetters[i] = (char)(i+97) + "";
				alphabet.setText(String.join(" ", allLetters));
				
				mainPanel.repaint();
				usedLetters.setText("");
			}

		}

	}

}
