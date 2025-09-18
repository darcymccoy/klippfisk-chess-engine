package chessengine.system;

/**
 * A chess move with the engine's evaluation of the position after this move has been played.
 */
public class EngineMove extends Move {
	
	/** The engine's evaluation of the position after this move has been played. */
	private int score;
	
	/**
	 * Class constructor that specifies the <code>Move</code> and the engine's evaluation.
	 * 
	 * @param move this Move will be a clone of this move
	 * @param score engine's evaluation of the position after this move has been played
	 */
	public EngineMove(Move move, int score) {
		super(move);
		this.score = score;
	}
	
	/**
	 * Class constructor which specifies the engine's evaluation and a default chess move.
	 * 
	 * @param score engine's evaluation of the position after this move has been played
	 */
	public EngineMove(int score) {
		super();
		this.score = score;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}
}
