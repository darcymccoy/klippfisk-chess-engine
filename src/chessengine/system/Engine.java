package chessengine.system;

import java.util.LinkedList;

/**
 * Calculation and evaluation engine for a chess position.
 * This class contains a chess position and must be constructed
 * with a legal chess position to return accurate results.
 * 
 * @author Darcy McCoy
 * @since 1.0
 */
public class Engine {
	
	/** <code>Position</code> to be searched for the top move. */
	Position position;
	
	/** Pawn values based on location (each integer corresponds to a square on the board). */
	public static final int[] PAWN_VALUES = {0, 0, 0, 0, 0, 0, 0, 0, 
			500, 500, 500, 500, 500, 500, 500, 500, 
			160, 155, 150, 150, 150, 150, 155, 160, 
			100, 100, 100, 120, 120, 100, 100, 100, 
			80, 100, 100, 120, 120, 100, 100, 80, 
			95, 100, 100, 110, 110, 80, 100, 96, 
			90, 100, 100, 90, 90, 100, 100, 90, 
			0, 0, 0, 0, 0, 0, 0, 0};
	
	/** Rook values based on location (each integer corresponds to a square on the board). */
	public static final int[] ROOK_VALUES = {500, 500, 500, 500, 500, 500, 500, 500, 
			500, 500, 500, 500, 500, 500, 500, 500, 
			500, 500, 500, 500, 500, 500, 500, 500, 
			500, 500, 500, 500, 500, 500, 500, 500, 
			500, 500, 500, 500, 500, 500, 500, 500, 
			500, 500, 500, 500, 500, 500, 500, 500, 
			500, 500, 500, 500, 500, 500, 500, 500, 
			500, 500, 500, 519, 520, 505, 500, 500};
	
	/** Knight values based on location (each integer corresponds to a square on the board). */
	public static final int[] KNIGHT_VALUES = {200, 220, 220, 220, 220, 220, 220, 200, 
			200, 270, 300, 300, 300, 300, 270, 200, 
			200, 250, 320, 320, 320, 320, 250, 200, 
			200, 250, 310, 326, 326, 310, 250, 200, 
			200, 250, 310, 325, 325, 310, 250, 200, 
			200, 250, 309, 310, 310, 310, 250, 200, 
			200, 250, 250, 250, 250, 250, 250, 200, 
			100, 225, 215, 215, 215, 215, 225, 100};
	
	/** Bishop values based on location (each integer corresponds to a square on the board). */
	public static final int[] BISHOP_VALUES = {280, 280, 280, 280, 280, 280, 280, 280, 
			280, 310, 310, 310, 310, 310, 310, 280, 
			280, 315, 315, 315, 315, 315, 315, 280, 
			290, 320, 320, 320, 320, 320, 320, 290, 
			290, 320, 335, 320, 320, 335, 320, 290, 
			280, 320, 320, 330, 330, 320, 320, 280, 
			280, 340, 310, 310, 310, 310, 340, 280, 
			280, 280, 280, 280, 280, 280, 280, 280};
	
	/** Queen values based on location (each integer corresponds to a square on the board). */
	public static final int[] QUEEN_VALUES = {900, 900, 900, 900, 900, 900, 900, 900,
			900, 900, 900, 900, 900, 900, 900, 900,
			900, 900, 900, 900, 900, 900, 900, 900,
			900, 900, 900, 900, 900, 900, 900, 900,
			900, 900, 900, 900, 900, 900, 900, 900,
			900, 900, 900, 900, 900, 900, 900, 900,
			900, 900, 900, 906, 906, 900, 900, 900,
			900, 900, 900, 900, 900, 900, 900, 900};
	
	/** King values based on location (each integer corresponds to a square on the board). */
	public static final int[] KING_VALUES = {100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 
			100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 
			100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 
			100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 
			100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 
			100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 
			100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 
			100000, 100040, 100030, 100000, 100000, 100000, 100040, 100000};
	
	/** Pawn endgame values based on location (each integer corresponds to a square on the board). */
	public static final int[] PAWN_ENDGAME_VALUES = {0, 0, 0, 0, 0, 0, 0, 0, 
			510, 510, 510, 510, 510, 510, 510, 510, 
			240, 220, 220, 250, 250, 220, 220, 240, 
			210, 200, 200, 210, 210, 200, 200, 210, 
			160, 150, 150, 160, 160, 150, 150, 160, 
			100, 105, 105, 110, 110, 105, 105, 100, 
			90, 90, 90, 90, 90, 90, 90, 90, 
			0, 0, 0, 0, 0, 0, 0, 0};
	
	/** King endgame values based on location (each integer corresponds to a square on the board). */
	public static final int[] KING_ENDGAME_VALUES = {100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 
			100000, 100010, 100010, 100010, 100010, 100010, 100010, 100000, 
			100000, 100010, 100030, 100030, 100030, 100030, 100010, 100000, 
			100000, 100010, 100030, 100040, 100040, 100030, 100010, 100000, 
			100000, 100010, 100030, 100040, 100040, 100030, 100010, 100000, 
			100000, 100010, 100030, 100030, 100030, 100030, 100010, 100000, 
			100000, 100010, 100010, 100010, 100010, 100010, 100010, 100000, 
			100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000};
	
	/**
	 * Class constructor that sets this engine to search the standard starting position.
	 */
	public Engine() {
		this(new Position());
	}
	
	/**
	 * Class constructor to set the position to search.
	 * 
	 * @param position <code>Position</code> to be searched for the top move
	 */
	public Engine(Position position) {
		this.position = position;
	}
	
	/**
	 * Returns the top move (as decided by the engine) for a position. 
	 * The engine searches to a hard coded depth of 3. 
	 * If no legal moves are found, a {@code NoLegalMovesException} will be thrown.
	 * 
	 * @return <code>Move</code> the top move
	 * @throws NoLegalMovesException if there are no legal moves in the position
	 */
	public Move findTopMoveDepth3() throws NoLegalMovesException {
		Move topMove = null;
		int topMoveMaxReply = 1000000;
		int maxReplyDepth1 = -1000000;
		int maxReplyDepth2 = 1000000;
		
		LinkedList<Move> legalMovesDepth1 = position.findLegalMoves();
		for (int i = 0; i < legalMovesDepth1.size(); i++) {
			maxReplyDepth1 = -1000000;
			Position tempPositionDepth1 = position.clone();
			tempPositionDepth1.makeMove(legalMovesDepth1.get(i));
			
			LinkedList<Move> legalMovesDepth2 = tempPositionDepth1.findLegalMoves();
			for (int j = 0; j < legalMovesDepth2.size(); j++) {
				maxReplyDepth2 = 1000000;
				Position tempPositionDepth2 = tempPositionDepth1.clone();
				tempPositionDepth2.makeMove(legalMovesDepth2.get(j));
			
				LinkedList<Move> legalMovesDepth3 = tempPositionDepth2.findLegalMoves();
				for (int k = 0; k < legalMovesDepth3.size(); k++) {
					Position tempPositionDepth3 = tempPositionDepth2.clone();
					tempPositionDepth3.makeMove(legalMovesDepth3.get(k));
					
					int evaluationDepth3 = position.evaluate();
					if (evaluationDepth3 < maxReplyDepth2)
						maxReplyDepth2 = evaluationDepth3;
				}
				
				if (maxReplyDepth2 > maxReplyDepth1)
					maxReplyDepth1 = maxReplyDepth2;
			}
			
			if (maxReplyDepth1 < topMoveMaxReply) {
				topMoveMaxReply = maxReplyDepth1;
				topMove = legalMovesDepth1.get(i);
			}
		}
		return topMove;
	}
	
	/**
	 * Finds the best move for the current position using the alpha beta search.
	 * 
	 * @return EngineMove the move that the engine evaluated to be the strongest
	 */
	public EngineMove findTopMove() {
		return alphaBeta(position, 3, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	/**
	 * Alpha beta search algorithm (minimax using alpha beta pruning) that searches for the top move.
	 * 
	 * @param position the Position to be searched
	 * @param depth how many moves to look ahead
	 * @param alpha minimum score for the maximizing color
	 * @param beta maximum score for the minimizing color
	 * @return
	 */
	public static EngineMove alphaBeta(Position position, int depth, int alpha, int beta) {
		boolean maximizingColor = position.isWhiteToPlay();
		Move topMove = null;
	    int topScore = getTopScore(maximizingColor);
		LinkedList<Move> legalMoves = null;
		
		try {
			legalMoves = position.findLegalMoves();
		} catch (CheckmateException e) {
			return new EngineMove(topScore);
		} catch (StalemateException e) {
			return new EngineMove(0);
		}
	    if (depth == 0) {
	      return new EngineMove(position.evaluate());
	    }

	    for (Move move : legalMoves) {
	      position.makeMove(move);
	      int score = alphaBeta(position, depth - 1, alpha, beta).getScore();
	      position.undoMove(move);

	      if (maximizingColor) {
	        if (score > topScore) {
	          topScore = score;
	          topMove = move;
	        }
	        alpha = Math.max(alpha, score);
	      } else {
	        if (score < topScore) {
	          topScore = score;
	          topMove = move;
	        }
	        beta = Math.min(beta, score);
	      }

	      // Prune branches if possible
	      if (alpha >= beta) {
	        break;
	      }
	    }

	    return new EngineMove(topMove, topScore);
	  }
	
	/**
	 * @param maximizingColor boolean whether to get white's top score
	 * @return the maximum value for the corresponding color
	 */
	private static int getTopScore(boolean maximizingColor) {
		if (maximizingColor) {
			return Integer.MIN_VALUE;
		} else {
			return Integer.MAX_VALUE;
		}
	}

}
