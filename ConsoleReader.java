import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ConsoleReader implements Runnable {
	private Display display;
	private Board board;

	public void run() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				display = new Display();
				board = display.getChessGame().getBoard();
			}
		});

		System.out.print(">> ");

		while (true) {
			try {
				Thread.sleep(50);

				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				String input = reader.readLine();

				if (input.equals("exit")) {
					System.exit(0);
					break;
				}

				List<String> commandAndArgs = parseCommand(input);

				if (commandAndArgs.isEmpty()) {
					System.out.println("Invalid input.\n>> ");
					continue;
				}

				String command = commandAndArgs.get(0).toLowerCase();
				List<String> args = commandAndArgs.subList(1, commandAndArgs.size());

				switch (command) {
					case "undo":
						if (!board.undoLastMove()) {
							System.out.println("No moves to undo!");
						} else {
							System.out.println("Successfully undid the move!");
						}
						display.resetLegalMoves();
						display.refreshDisplay();
						break;
					case "best_iterative":
						int maxDepth = 6;
						if (args.isEmpty()) {
							System.out.println("Defaulted max depth: " + maxDepth);
						} else {
							System.out.println("Custom max depth: " + args.get(0));
							maxDepth = Integer.parseInt(args.get(0));
						}

						EvaluationResult result = board.iterativeDeepening(maxDepth);

						System.out.println("Bot's Current Evaluation: " + result.value);
						board.makeMove(result.move);
						display.refreshDisplay();
						break;
					case "best":
						int depth = 4;
						if (args.isEmpty()) {
							System.out.println("Defaulted depth: " + depth);
						} else {
							System.out.println("Custom depth: " + args.get(0));
							depth = Integer.parseInt(args.get(0));
						}

						EvaluationResult result2 = board.minimax(depth, Double.NEGATIVE_INFINITY,
								Double.POSITIVE_INFINITY, board.isWhiteTurn(), null);

						System.out.println("Bot's Current Evaluation: " + result2.value);
						board.makeMove(result2.move);
						display.refreshDisplay();
						break;
					case "generate_moves":
						int iterations = 10000;
						if (args.isEmpty()) {
							System.out.println("Defaulted iterations: " + iterations);
						} else {
							System.out.println("Custom depth: " + args.get(0));
							iterations = Integer.parseInt(args.get(0));
						}

						long total = 0;
						for (int i = 0; i < iterations; i++) {
							long startTime = System.currentTimeMillis();
							board.getAllLegalMoves(true);
							long endTime = System.currentTimeMillis();

							total += (endTime - startTime);

						}

						System.out.println("Time: " + (total / iterations));
						break;
					case "eval":
						int evalDepth = 4;
						if (args.isEmpty()) {
							System.out.println("Defaulted depth: " + evalDepth);
						} else {
							System.out.println("Custom depth: " + args.get(0));
							evalDepth = Integer.parseInt(args.get(0));
						}

						EvaluationResult evalResult = board.minimax(evalDepth, Double.NEGATIVE_INFINITY,
								Double.POSITIVE_INFINITY, board.isWhiteTurn(), null);
						System.out.println("Bot's Current Evaluation: " + evalResult.value);

						break;
					case "shallow":
						System.out.println("Current Shallow Evaluation: "
								+ board.evaluateCurrentPosition(0, board.getAllLegalMoves(false).size()));

						break;
					case "fen":
						if (args.isEmpty()) {
							System.out.println("You need to pass in a FEN string to load the position!");
							break;
						}

						board.parseFEN(args.get(0));
						display.refreshDisplay();
						System.out.println("Successfully loaded your FEN!");
						break;
					default:
						System.out.println("Unknown command: " + command);
				}

				System.out.print(">> ");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static List<String> parseCommand(String input) {
		List<String> result = new ArrayList<>();
		Pattern pattern = Pattern.compile("\"([^\"]*)\"|(\\S+)");
		Matcher matcher = pattern.matcher(input);

		while (matcher.find()) {
			if (matcher.group(1) != null) {
				// Quoted argument
				result.add(matcher.group(1));
			} else {
				// Unquoted argument
				result.add(matcher.group(2));
			}
		}

		return result;
	}
}