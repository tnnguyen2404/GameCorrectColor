/* Group members:
 * Le Phan, Vannary Sou, Tung Thanh Pham
 * August 6, 2021
 * Final Game
 * C SCI 143 - Professor Nizami Syeda
 */

package GameCorrectColor;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;



public class GameCorrectColor extends JFrame implements ActionListener{
	int size = 100;
	int n = 1, x = 0, y = 0, width = 500, height = 550;
	private JButton button[][] = new JButton[size][size]; // Two-dimensional array type JButton with 100 rows and 100 columns to make JButton matrix
	private int array[][] = new int[size][size]; // Two-dimensional array type integer with 100 rows and 100 columns to make matrix with value of 0 and 1
	int numberLv[] = {1, 2, 2, 3, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 11, 12, 12, 12, 12, 13, 13, 13, 1000000};
	private JButton bt; // Create JButton variable
	private JPanel panel, panel2; // Create two JPanel variable
	private JLabel timeLabel; // Create JLabel variable
	
	// Create a container and a timer
	Container container; 
	Timer timer;
	
	public GameCorrectColor(int k, int score, String s, int lv) { // Use the class Timer to support time counting
		this.setTitle("GameCorrectColor");
		container = matrix(k, score, s, lv);
		timer = new Timer(10, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timeLabel.setText(next(timeLabel, -1)); // Print out time remaining as string and the time keeps being deducted one millisecond
			}
		});
	}
	
	public Container matrix(int k, int h, String s, int lv) { // Method to create a JButton matrix in JFrame class
		Container container = this.getContentPane(); // Return the content pane of this frame
		x = k; y = h;
		n = x + 2;
		panel = new JPanel();
		panel.setLayout(new GridLayout(n, n)); // Set grid layout with n rows and n columns based on level 
		for (int i = 0; i < n; i++) {// Nested loop that sets each JButton to each elements in JButton matrix corresponding to n rows and n columns and add them to panel
			for (int j = 0; j < n; j++){
				button[i][j] = new JButton();
				panel.add(button[i][j]);
				button[i][j].setActionCommand(i + " " + j);
				button[i][j].addActionListener(this);
				button[i][j].setBackground(Color.black);
				button[i][j].setBorder(null);
			}
		}
		createMatrix();
		setIconMatrix();
		panel2 = new JPanel();
		panel2.setLayout(new FlowLayout());
		bt = new JButton(String.valueOf(lv)); // JButton that indicates current level
		timeLabel = new JLabel(s); // JLabel that indicates time remaining
		timeLabel.setFont(new Font("", 1, 20));
		panel2.add(bt);
		panel2.add(timeLabel); // add JButton and JLabel above to panel2 
		container.add(panel); 
		container.add(panel2, "North"); // Set panel2 at top of the container
		this.setVisible(true); 
		this.setSize(width, height);
		setDefaultCloseOperation(EXIT_ON_CLOSE); // Exit the application
		this.setLocationRelativeTo(null); // Set location of a frame to the center of a window
		return container;
	}
	
	public void createMatrix() {  // Method to create a matrix 
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				array[i][j] = 0; // Nested loop that sets zero to each elements in an array
		array[(int) (Math.random() * (n - 1) + 0.5)][(int) (Math.random() * (n - 1) + 0.5)] = 1; // Set random one element in an array with value of 1
	}
	
	public void setIconMatrix() { // Set image icon to each element in a matrix and reduce the size of the image icon when moving to a next level
		int numIcon = 14;
		int k = (int) (Math.random() * (numIcon - 1) + 0.5 + 1); // Set random number between 1 and 14 to k
		int h = (int) ((Math.random() + 0.5) + 1); // Set random number between 1 and 2 to h
		Icon ic[] = new Icon[2]; // Create an array type icon with size 2
		ic[0] = getIcon(k, h, (width - 100) / (x + 2));
		ic[1] = getIcon(k, 3 - h, (width - 100) / (x + 2)); //	Get the first icon image based on k and h, the second icon image based on k and 3 - h means 
															//we will get lighter or darker color of that color
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				button[i][j].setIcon(ic[array[i][j]]); // Set icon image to each JButton in a JButton matrix based on value 0 and 1 of the array.
														// There would be only one lighter or darker color of that color because the array only have one value of one.
	}
	
	private Icon getIcon(int index, int index2, int size) { //Method that get image from a folder linking to this class
		int w = size, h = size;
		Image image = new ImageIcon(getClass().getResource("/GameCorrectColor/image/image" + index + "_" + index2 + ".jpg")).getImage();
		Icon icon = new ImageIcon(image.getScaledInstance(w, h, image.SCALE_SMOOTH)); // Create a proper size for an image
		return icon;
	}
	
	public void nextGame() { //Method that increase size of a matrix when moving to a next level
		if (y == numberLv[x]) {
			x++;
			y = 0;
		}
		else
			y++;
		timer.stop();
		// Timer stops and start a new game with new time and new level
		GameCorrectColor game = new GameCorrectColor(x, y, timeLabel.getText(), Integer.parseInt(bt.getText()) + 1);
		game.timer.start();
	}

	public String next(JLabel lb, int k) { //A method that adds or subtract time
		String str[] = lb.getText().split(":"); // Split : out of time remaining 
		int tt = Integer.parseInt(str[1]); // millisecond
		int s = Integer.parseInt(str[0]); // second
		String kq = "";
		int sum = tt + s * 100 + k;
		if (sum <= 0) { // If time remaining equals zero, print out messages and assign them to choice buttons
			timer.stop();
			timeLabel.setText("0:00");
			showDialogNewGame("Time's up!!\n" +
					"Your Score: " + bt.getText() + "\n" +
					"Do you want to play again?", "Notice!!");
		}
		if (sum % 100 > 9) // time remaining as a string
			kq = ":" + sum % 100 + kq;
		else
			kq = ":0" + sum % 100 + kq;
		sum /= 100;
		kq = sum + kq;
		return kq;
	}
	
	public void showDialogNewGame(String message, String title) {  //Create a method that showing a message and list of choices
		int select = JOptionPane.showOptionDialog(null, message, title,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				null, null);
		if (select == 0) { // If a user select yes, start a new game with level 1, exit an application otherwise
			GameCorrectColor game = new GameCorrectColor(0, 0, "60:00", 1); 
			game.timer.start();
		} else {
			System.exit(0);
		}
	}
	

	public void actionPerformed(ActionEvent e) {  // Create a method to increase time limit when choosing a correct color, decrease otherwise
		int i, j;
		String s = e.getActionCommand(); //return the command string associated with the action
		int k = s.indexOf(32);
		i = Integer.parseInt(s.substring(0, k));
		j = Integer.parseInt(s.substring (k + 1, s.length()));
		if (array[i][j] == 1) { 
			timeLabel.setText(next(timeLabel, 100)); // If a user choose a correct color indicating value of 1 in a matrix, 
														//invoke next() method to increase time remaining by one second and move to a next level
			nextGame();
		}
		else {
			timeLabel.setText(next(timeLabel, -200)); // Decrease two second otherwise
		}
	}
	
	public static void main(String[] args) { // Main method
		GameCorrectColor game = new GameCorrectColor(0, 0, "60:00", 1);
		game.timer.start();
	}
}