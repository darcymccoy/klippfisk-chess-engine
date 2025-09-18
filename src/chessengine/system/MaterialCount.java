package chessengine.system;

public class MaterialCount {
	private static final int SUFFICIENT_QUEENS_ROOKS_PAWNS = 1;
	private static final int SUFFICIENT_BISHOPS_KNIGHTS = 2;
	private int whiteQueensRooksPawns;
	private int whiteBishopsKnights;
	private int blackQueensRooksPawns;
	private int blackBishopsKnights;
	
	public MaterialCount() {
		this(11, 4, 11, 4);
	}
	
	/**
	 * Class constructor that specifies a number for each category of piece.
	 * 
	 * @param whiteQueensRooksPawns
	 * @param whiteBishopsKnights
	 * @param blackQueensRooksPawns
	 * @param blackBishopsKnights
	 */
	public MaterialCount(int whiteQueensRooksPawns, int whiteBishopsKnights, int blackQueensRooksPawns,
			int blackBishopsKnights) {
		this.whiteQueensRooksPawns = whiteQueensRooksPawns;
		this.whiteBishopsKnights = whiteBishopsKnights;
		this.blackQueensRooksPawns = blackQueensRooksPawns;
		this.blackBishopsKnights = blackBishopsKnights;
	}

	public void updateForMove(Move move) {
		if ((move.getEndSqrContents() == Chess.WH_QUEEN) || (move.getEndSqrContents() == Chess.WH_ROOK) || (move.getEndSqrContents() == Chess.WH_PAWN)) {
			whiteQueensRooksPawns--;
		} else if ((move.getEndSqrContents() == Chess.WH_BISHOP) || (move.getEndSqrContents() == Chess.WH_KNIGHT)) {
			whiteBishopsKnights--;
		} else if ((move.getEndSqrContents() == Chess.BK_QUEEN) || (move.getEndSqrContents() == Chess.BK_ROOK) || (move.getEndSqrContents() == Chess.BK_PAWN)) {
			blackQueensRooksPawns--;
		} else if ((move.getEndSqrContents() == Chess.BK_BISHOP) || (move.getEndSqrContents() == Chess.BK_KNIGHT)) {
			blackBishopsKnights--;
		}
		if (move.isPromotion()) {
			if ((move.getPiece() == Chess.WH_PAWN) && ((move.getPromoteTo() == Chess.WH_BISHOP) || (move.getPromoteTo() == Chess.WH_KNIGHT))) {
				whiteQueensRooksPawns--;
				whiteBishopsKnights++;
			} else if ((move.getPiece() == Chess.BK_PAWN) && ((move.getPromoteTo() == Chess.WH_BISHOP) || (move.getPromoteTo() == Chess.WH_KNIGHT))) {
				blackQueensRooksPawns--;
				blackBishopsKnights++;
			}
		}
	}

	/**
	 * Returns true if this material count is insufficient for either colors to force checkmate.
	 * 
	 * @return <code>true</code> if this material count is insufficient;
	 *         <code>false</code> otherwise.
	 */
	public boolean isInsufficientMaterial() {
		return (whiteQueensRooksPawns < SUFFICIENT_QUEENS_ROOKS_PAWNS) && (whiteBishopsKnights < SUFFICIENT_BISHOPS_KNIGHTS) &&
				(blackQueensRooksPawns < SUFFICIENT_QUEENS_ROOKS_PAWNS) && (blackBishopsKnights < SUFFICIENT_BISHOPS_KNIGHTS);
	}
	
}
