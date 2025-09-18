package chessengine.system;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * Abstract class for any type of chess game. 
 * To play a game, this class must be extended and the <code>play()</code> method be implemented.
 * 
 * @author Darcy McCoy
 * @since 1.0
 */
public abstract class Game {
	
	/** After 100 plies without a pawn move or capture a game is drawn. In chess this is known as the 50 move rule. */
	private static final int PLY_MAX_FOR_50_MOVE_RULE = 100;
	
	/** If 2 other positions are exactly the same as the current one, a game is drawn by threefold repetition. */
	private static final int THREEFOLD_LIMIT = 2;
	
	/** <code>Scanner</code> to get user input. */
	private static final Scanner scanner = new Scanner(System.in);
	
	/** The chess engine which can play against a user or against itself. */
	private Engine engine;
	
	/** Whether this game is currently being played. */
	protected boolean inGame;
	
	/** The current position in this game. */
	private Position currentPosition;
	
	/** The moves that have previously been made in this game. */
	private LinkedList<Move> movesMade;
	
	/** The message to display the draw type. */
	private String message;
	
	/** To track sufficient material */
	private MaterialCount materialCount;
	
	/**
	 * Default constructor (standard starting position and the game hasn't been started).
	 */
	public Game() {
		this(false, new Position(), new LinkedList<Move>(), new MaterialCount());
	}

	/**
	 * Parameterized constructor to set up a game from any position other than the standard starting position.
	 * 
	 * @param inGame <code>boolean</code> whether the game is currently being played
	 * @param currentPosition the <code>Position</code> that the game will start from
	 * @param movesMade <code>LinkedList</code> for the moves that have already been made
	 */
	public Game(boolean inGame, Position currentPosition, LinkedList<Move> movesMade, MaterialCount materialCount) {
		this.inGame = inGame;
		this.currentPosition = currentPosition;
		this.movesMade = movesMade;
		this.materialCount = materialCount;
		this.message = "";
		this.engine = new Engine(currentPosition);
	}
	
	/**
	 * Makes a move on the current game position.
	 * 
	 * @param move the <code>Move</code> to be made
	 */
	public void addGameMove(Move move) {
		currentPosition.makeMove(move);
		movesMade.add(move);
		materialCount.updateForMove(move);
		if (isDraw())
			stopGame();
	}
	
	/**
	 * Starts and plays whichever game is extending this class.
	 */
	public abstract void play();

	/**
	 * Sets inGame to true.
	 */
	public void startGame() {
		inGame = true;
	}

	/**
	 * Sets inGame to false.
	 */
	public void stopGame() {
		inGame = false;
	}
	
	/**
	 * Adds the engines move to the game documentation.
	 * If there are no legal moves then the game is ended.
	 */
	public void letEngineMakeMove() {
		addGameMove(engine.findTopMove());
	}
	
	/**
	 * Prompts the user for a move and makes that move on the current position.
	 */
	public void letUserMakeMove() {
		Move userMove = null;
		
		while (inGame) {
			try {
				System.out.print("Enter the starting square of your move: ");
				int startSqr = scanner.nextInt();
				System.out.print("Enter the ending square of your move: ");
				int endSqr = scanner.nextInt();
				userMove = currentPosition.constructUserMove(startSqr, endSqr);
				if (currentPosition.isLegalMove(userMove)) {
					addGameMove(userMove);
					break;
				} else {
					System.out.println("This isn't a legal move. Try again.");
				}
			} catch(RuntimeException | IllegalMoveException e) {
				scanner.nextLine();
				System.out.println("This isn't a legal move. Try again.");
			} catch (CheckmateException e) {
				e.printStackTrace();
			} catch (StalemateException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Allows the user to choose whether to play white or black against the engine.
	 * 
	 * @return <code>true</code> if the user chooses to play white; <code>false</code> otherwise
	 */
	public boolean userChoosesToPlayWhite() {
		boolean userToPlayWhite = true;
		
		while (true) {
			System.out.print("Enter \"true\" to play as white or \"false\" to play as black: ");
			try {
				userToPlayWhite = scanner.nextBoolean();
			} catch(RuntimeException e) {
				scanner.nextLine();
				System.out.println("This isn't one of the choices. Try again.");
				continue;
			}
			return userToPlayWhite;
		}
	}
	
	/**
	 * Returns true if the game is drawn by threefold repetition, 50 move, or insufficient material rules.
	 * 
	 * @return <code>true</code> if this game is a draw by one of the special rules;
	 *         <code>false</code> otherwise.
	 */
	public boolean isDraw() {
		return isInsufficientMaterialDraw() || isThreefoldDraw() || is50MoveDraw();
	}
	
	/**
	 * Returns true if the game is drawn by the insufficient material rule.
	 * 
	 * @return <code>true</code> if neither color has enough material to checkmate;
	 *         <code>false</code> otherwise.
	 */
	private boolean isInsufficientMaterialDraw() {
		return materialCount.isInsufficientMaterial();
	}
	
	/**
	 * Returns true if the game is drawn by threefold repetition.
	 * 
	 * @return <code>true</code> if the same position has been reached 3 times;
	 *         <code>false</code> otherwise.
	 */
	private boolean isThreefoldDraw() {
		int equalPositionsCount = 0;
		Position tempPosition = new Position();
		for (Move move : movesMade) {
			if (tempPosition.equals(currentPosition))
				equalPositionsCount++;
			tempPosition.makeMove(move);
		}
		return equalPositionsCount >= THREEFOLD_LIMIT;
	}
	
	/**
	 * Returns true if the game is drawn by the 50 move rule.
	 * 
	 * @return <code>true</code> if 50 moves without a capture or pawn move have been made;
	 *         <code>false</code> otherwise.
	 */
	private boolean is50MoveDraw() {
		if (movesMade.size() < PLY_MAX_FOR_50_MOVE_RULE)
			return false;
		for (int i = 1; i < PLY_MAX_FOR_50_MOVE_RULE; i++) {
			Move move = movesMade.get(movesMade.size() - i);
			if (move.isCapture() || (move.getPiece() == Chess.WH_PAWN) || (move.getPiece() == Chess.BK_PAWN))
				return false;
		}
		return true;
	}
	
	/**
	 * Closes the scanner object.
	 */
	public void closeScanner() {
		scanner.close();
	}
	
	@Override
	public String toString() {
		String printGame = currentPosition.toString() + "\n";
		for (int i = 0; i < movesMade.size(); i++) {
			printGame += movesMade.get(i).toString() + " ";
			if (((i + 1) % 2) == 0)
				printGame += "\n";
		}
		printGame += message;
		return printGame;
	}
	
}