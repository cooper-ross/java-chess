import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.util.Arrays;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;
import java.awt.Font;
import javax.swing.plaf.basic.BasicButtonUI;

public class Display {
	private Chess game;
	private GamePanel gamePanel;
	private JPanel buttonPanel;
	private JPanel evalPanel;
	private SquareButton[] buttons;
	private JLabel statusLabel;
	private JProgressBar evaluationBar;
	private Board board;
	private long maxSearchTime = 1000;

	private byte lastSelectedIndex = -1;
	private List<Move> lastLegalMovesList = new ArrayList<Move>(0);

	public Display() {
		System.setProperty("awt.useSystemAAFontSettings", "on");
		// System.setProperty("swing.aatext", "true");

		game = new Chess();

		gamePanel = new GamePanel();
		buttons = gamePanel.getButtons();

		board = game.getBoard();

		buildUI();
		refreshDisplay();
	}

	private void buildUI() {
		final JFrame frame = new JFrame("Chess");
		frame.getContentPane().setBackground(new Color(95, 67, 42));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		evaluationBar = new JProgressBar();
		evaluationBar.setValue(50);
		evaluationBar.setStringPainted(false);
		evaluationBar.setBorderPainted(false);
		evaluationBar.setBackground(Color.BLACK);
		evaluationBar.setForeground(Color.WHITE);
		evaluationBar.setPreferredSize(new Dimension(522, 10));

		evalPanel = new JPanel();
		buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(95, 67, 42));
		evalPanel.setBackground(new Color(95, 67, 42));

		String[] buttonLabels = { "Best Move", "Reset Game", "Undo Move", "Load FEN", "Search Time" };
		JButton[] buttons = new JButton[buttonLabels.length];

		// Create and add buttons with action listeners in a loop
		for (int i = 0; i < buttonLabels.length; i++) {

			final JButton currentButton = new JButton(buttonLabels[i]);
			buttonPanel.add(currentButton);

			currentButton.setUI(new BasicButtonUI());
			currentButton.setBackground(new Color(235, 177, 126));
			currentButton.setForeground(Color.BLACK);
			currentButton.setFont(new Font("Tahoma", Font.BOLD, 12));
			currentButton.setBorder(new EmptyBorder(11, 11, 11, 11));

			final int buttonIndex = i;
			currentButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Button " + (buttonIndex + 1) + " clicked!");
					if (buttonIndex == 0) {
						EvaluationResult result = board.iterativeDeepening(maxSearchTime);
						board.makeMove(result.move);

						evaluationBar.setValue((int) Math.max(Math.min(result.value / 10 + 50, 100), 0));
						refreshDisplay();
					} else if (buttonIndex == 1) {
						board.parseFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
						refreshDisplay();
					} else if (buttonIndex == 2) {
						board.undoLastMove();
						refreshDisplay();
					} else if (buttonIndex == 3) {
						String s = (String) JOptionPane.showInputDialog(frame, "Paste the FEN string here:",
								"FEN input", JOptionPane.PLAIN_MESSAGE, null, null,
								"7r/p3ppk1/3p4/2p1P1Kp/2Pb4/3P1QPq/PP5P/R6R b - - 0 1");

						if ((s != null) && (s.length() > 0)) {
							board.parseFEN(s);
							refreshDisplay();
						}
					} else if (buttonIndex == 4) {
						String s = (String) JOptionPane.showInputDialog(frame, "Enter the max time here:",
								"Set search time", JOptionPane.PLAIN_MESSAGE, null, null, "1000"); // 1000

						if ((s != null) && (s.length() > 0)) {
							maxSearchTime = Long.parseLong(s);
						}
					}
				}
			});
		}

		frame.add(gamePanel, BorderLayout.NORTH);

		// Add the panel to the frame
		frame.add(buttonPanel, BorderLayout.SOUTH);
		frame.add(evalPanel, BorderLayout.CENTER);

		evalPanel.add(evaluationBar, BorderLayout.CENTER);

		initializeButtonListeners();

		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}

	private void initializeButtonListeners() {
		for (byte i = 0; i < 64; i++) {
			final byte index = i;
			SquareButton button = buttons[index];
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					handleButtonClick(index);
				}
			});
		}
	}

	public void refreshDisplay() {
		byte[] currentPosition = board.getCurrentPosition();
		for (byte i = 0; i < 64; i++) {
			// No texture if the tile is empty
			if (currentPosition[i] == 0) {
				buttons[i].setIcon(null);
				continue;
			}

			buttons[i].setIcon(Piece.mapPieceTexture(currentPosition[i]));
		}
	}

	public void highlightSquare(byte index) {
		byte i = (byte) (Math.floor(index / 8));
		byte j = (byte) (index % 8);

		final Color color = (j % 2 + (i % 2) == 1) ? new Color(199, 151, 100) : new Color(255, 210, 160);
		buttons[index].setBackground(color);
	}

	public void unhighlightSquare(byte index) {
		byte i = (byte) (Math.floor(index / 8));
		byte j = (byte) (index % 8);

		final Color color = (j % 2 + (i % 2) == 1) ? new Color(169, 121, 79) : new Color(235, 177, 126);
		buttons[index].setBackground(color);
	}

	private void handleButtonClick(byte index) {
		if (lastSelectedIndex == -1) {
			handleFirstSelection(index);
		} else {
			handleSecondSelection(index);
		}
	}

	private void handleFirstSelection(byte index) {
		byte[] currentPosition = board.getCurrentPosition();
		if (currentPosition[index] == Piece.EMPTY || Piece.isWhite(currentPosition[index]) != board.isWhiteTurn())
			return;

		lastSelectedIndex = index;
		lastLegalMovesList = MoveGenerator.generateMovesForIndex(index, board);
		highlightLegalMoves(lastLegalMovesList);
	}

	private void handleSecondSelection(byte index) {
		byte startSquare = lastSelectedIndex;
		byte targetSquare = index;

		unhighlightLegalMoves(lastLegalMovesList);

		boolean isMoveLegal = isMoveLegal(startSquare, targetSquare);

		if (startSquare == targetSquare) {
			lastSelectedIndex = -1;
		} else if (!isMoveLegal) {
			lastSelectedIndex = -1;
			byte[] currentPosition = board.getCurrentPosition();
			if (currentPosition[index] == Piece.EMPTY)
				return;
			handleFirstSelection(index);
		} else if (isMoveLegal) {
			Move selectedMove = getMove(startSquare, targetSquare);
			board.makeMove(selectedMove);
			lastSelectedIndex = -1;
			refreshDisplay();

			// System.out.println(board.kingInCheck(true, board.getCurrentPosition()));
		}
	}

	private Move getMove(byte startSquare, byte targetSquare) {
		for (Move move : lastLegalMovesList) {
			if (move.startSquare == startSquare && move.targetSquare == targetSquare) {
				return move;
			}
		}
		return lastLegalMovesList.get(0);
	}

	private boolean isMoveLegal(byte startSquare, byte targetSquare) {
		for (Move move : lastLegalMovesList) {
			if (move.startSquare == startSquare && move.targetSquare == targetSquare) {
				return true;
			}
		}
		return false;
	}

	public void highlightLegalMoves(List<Move> moves) {
		for (Move move : moves) {
			highlightSquare(move.targetSquare);
		}
	}

	private void unhighlightLegalMoves(List<Move> moves) {
		for (Move move : moves) {
			unhighlightSquare(move.targetSquare);
		}
	}

	public void resetLegalMoves() {
		if (lastLegalMovesList.isEmpty()) {
			return;
		}

		unhighlightLegalMoves(lastLegalMovesList);
		lastLegalMovesList.clear();
	}

	public Chess getChessGame() {
		return game;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Display();
			}
		});
	}
}