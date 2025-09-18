package chessengine.system;

/**
 * Contains the information for a single chess move. Illegal, pseudo legal, and
 * legal moves can be stored as a <code>Move</code> object.
 * 
 * @author Darcy McCoy
 * @since 1.0
 */
public class Move {

	/** The character representation of the piece that is making this move. */
	private char piece;

	/** The square on the board that the piece originated from. */
	private int startSqr;

	/** The square on the board that the piece ends on. */
	private int endSqr;

	/** The character at the square that the piece ends on */
	private char endSqrContents;

	/**
	 * The character representation of the piece that the pawn is promoting to. If
	 * the move isn't promotion, then this will be <code>'-'</code>.
	 */
	private char promoteTo;

	/** The Position that this move is being played from */
	private Position originalPosition;
	
	/**
	 * Default constructor.
	 */
	public Move() {
		this('0', 0, 0, '0', '0', new Position());
	}
	
	/**
	 * Class constructor specifying the piece, start and end squares of this move.
	 * This can construct every move except for pawns promoting.
	 * 
	 * @param piece          character representing the piece that is making this
	 *                       move
	 * @param startSqr       integer square on the board that the piece originated
	 *                       on
	 * @param endSqr         integer square on the board that the piece ends on
	 * @param endSqrContents character at the square that the piece ends on
	 */
	public Move(char piece, int startSqr, int endSqr, char endSqrContents) {
		this(piece, startSqr, endSqr, endSqrContents, Chess.EMPTY, null);
	}

	/**
	 * Parameterized constructor specifying the start and end squares, and the piece
	 * being promoted to. This should only be called externally when used to
	 * construct moves that are pawns promoting.
	 * 
	 * @param piece          character representing the piece that is making this
	 *                       move
	 * @param startSqr       integer square on the board that the piece originated
	 *                       on
	 * @param endSqr         integer square on the board that the piece ends on
	 * @param promoteTo      character representing the piece that the pawn promotes
	 *                       to. If this move isn't promotion, then this will be
	 *                       <code>'-'</code>.
	 * @param endSqrContents character at the square that the piece ends on
	 * @param originalPosition the Position that this move is played from
	 */
	public Move(char piece, int startSqr, int endSqr, char endSqrContents, char promoteTo, Position originalPosition) {
		this.piece = piece;
		this.startSqr = startSqr;
		this.endSqr = endSqr;
		this.promoteTo = promoteTo;
		this.endSqrContents = endSqrContents;
		this.originalPosition = originalPosition;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param otherMove the <code>Move</code> to copy
	 */
	public Move(Move otherMove) {
		this(otherMove.piece, otherMove.startSqr, otherMove.endSqr, otherMove.endSqrContents, otherMove.promoteTo, otherMove.originalPosition);
	}

	/**
	 * Returns a copy of this move.
	 * 
	 * @return <code>Move</code> that is a copy of this move
	 */
	@Override
	public Move clone() {
		return new Move(this);
	}

	/**
	 * Returns true if this move captures a piece.
	 * 
	 * @return <code>true</code> if this move is capturing an opposing piece;
	 *         <code>false</code> otherwise.
	 */
	public boolean isCapture() {
		return isEnPassant() || (endSqrContents != Chess.EMPTY);
	}

	/**
	 * Returns true if this move is promotion.
	 * 
	 * @return <code>true</code> if this move is a pawn promoting;
	 *         <code>false</code> otherwise.
	 */
	public boolean isPromotion() {
		return ((piece == Chess.WH_PAWN) && Board.isRank8Sqr(endSqr))
				|| ((piece == Chess.BK_PAWN) && Board.isRank1Sqr(endSqr));
	}

	/**
	 * Returns true if this move is castling.
	 * 
	 * @return <code>true</code> if this move is a king castling; <code>false</code>
	 *         otherwise.
	 */
	public boolean isCastling() {
		return isKingsideCastling() || isQueensideCastling();
	}

	/**
	 * Returns true if this move is castling on the kingside of the board.
	 * 
	 * @return <code>true</code> if this move is a king castling on the kingside;
	 *         <code>false</code> otherwise.
	 */
	public boolean isKingsideCastling() {
		return ((piece == Chess.WH_KING) || (piece == Chess.BK_KING)) && (startSqr == (endSqr + Chess.WEST_2));
	}

	/**
	 * Returns true if this move is castling on the queenside of the board.
	 * 
	 * @return <code>true</code> if this move is a king castling on the queenside;
	 *         <code>false</code> otherwise.
	 */
	public boolean isQueensideCastling() {
		return ((piece == Chess.WH_KING) || (piece == Chess.BK_KING)) && (startSqr == (endSqr + Chess.EAST_2));
	}

	/**
	 * Returns true if this move is a pawn capturing en passant.
	 * 
	 * @return <code>true</code> if this move is a pawn capturing en passant;
	 *         <code>false</code> otherwise.
	 */
	public boolean isEnPassant() {
		return ((piece == Chess.WH_PAWN) || (piece == Chess.BK_PAWN)) && (endSqrContents == Chess.EMPTY)
				&& ((startSqr % Board.WIDTH) != (endSqr % Board.WIDTH));
	}

	/**
	 * Returns a string representation of the move. For promotion moves, a "=" and
	 * the character representation of the move being promoted to will be added to
	 * the end of the string.
	 * 
	 * @return a string representation of this move
	 */
	@Override
	public String toString() {
		if (isPromotion()) {
			return Integer.toString(startSqr) + Integer.toString(endSqr) + "=" + promoteTo;
		} else {
			return Integer.toString(startSqr) + Integer.toString(endSqr);
		}
	}

	/**
	 * Tests if 2 moves are the same.
	 * 
	 * @param obj Object to be tested
	 * @return <code>true</code> if this move and the testing move are the same
	 *         chess moves
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		return (piece == other.piece) && (endSqr == other.endSqr) && (promoteTo == other.promoteTo)
				&& (startSqr == other.startSqr) && (endSqrContents == other.endSqrContents) && (originalPosition.equals(other.originalPosition));
	}

	/**
	 * @return the piece
	 */
	public char getPiece() {
		return piece;
	}

	/**
	 * @param piece the piece to set
	 */
	public void setPiece(char piece) {
		this.piece = piece;
	}

	/**
	 * @return the startSqr
	 */
	public int getStartSqr() {
		return startSqr;
	}

	/**
	 * @param startSqr the startSqr to set
	 */
	public void setStartSqr(int startSqr) {
		this.startSqr = startSqr;
	}

	/**
	 * @return the endSqr
	 */
	public int getEndSqr() {
		return endSqr;
	}

	/**
	 * @param endSqr the endSqr to set
	 */
	public void setEndSqr(int endSqr) {
		this.endSqr = endSqr;
	}

	/**
	 * @return the promoteTo
	 */
	public char getPromoteTo() {
		return promoteTo;
	}

	/**
	 * @param promoteTo the promoteTo to set
	 */
	public void setPromoteTo(char promoteTo) {
		this.promoteTo = promoteTo;
	}

	/**
	 * @return the originalPosition
	 */
	public Position getOriginalPosition() {
		return originalPosition;
	}

	/**
	 * @param originalPosition the originalPosition to set
	 */
	public void setOriginalPosition(Position originalPosition) {
		this.originalPosition = originalPosition;
	}

	/**
	 * @return the endSqrContents
	 */
	public char getEndSqrContents() {
		return endSqrContents;
	}

	/**
	 * @param endSqrContents the endSqrContents to set
	 */
	public void setEndSqrContents(char endSqrContents) {
		this.endSqrContents = endSqrContents;
	}

}
