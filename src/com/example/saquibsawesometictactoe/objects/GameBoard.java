package com.example.saquibsawesometictactoe.objects;



public class GameBoard {
	public static enum Piece { N, X, O };
	
	private Piece boardLayout[][] = new Piece [][]{{Piece.N, Piece.N, Piece.N} , {Piece.N, Piece.N, Piece.N} , {Piece.N, Piece.N, Piece.N}};
	
	int piecesSoFar = 0;
	
	public GameBoard(String rep) {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				char piece = rep.charAt(3*x + y);
				
				if (piece == 'X') {
					piecesSoFar++;
					boardLayout[x][y] = Piece.X;
				} else if (piece == 'O') {
					piecesSoFar++;
					boardLayout[x][y] = Piece.O;
				} else {
					boardLayout[x][y] = Piece.N;
				}
			}
		}
	}
	
	public boolean occupyCell(int x, int y, Piece currentPiece) {
		if (boardLayout[x][y] != Piece.N) {
			return false;
		} else {
			boardLayout[x][y] = currentPiece;
			piecesSoFar++;
			return true;
		}
	}

	public void clear() {
		for (int x = 0; x < boardLayout.length; x++) {
			for (int y = 0; y < boardLayout[x].length; y++) {
				boardLayout[x][y] = Piece.N;
			}
		}
		
		piecesSoFar = 0;
	}
	
	public Piece getCell(int x, int y) {
		return boardLayout[x][y];
	}
	
	public Piece hasWon() {
		for (int x = 0; x < 3; x++) {
			if (getCell(x, 0) != Piece.N &&
					getCell(x, 0) == getCell(x, 1) && 
					getCell(x, 1) == getCell(x, 2)) {
				return getCell(x, 0);
			}
			
			if (getCell(0, x) != Piece.N &&
					getCell(0, x) == getCell(1, x) && 
					getCell(1, x) == getCell(2, x)) {
				return getCell(0, x);
			}
		}
		
		if (getCell(0, 0) != Piece.N &&
				getCell(0, 0) == getCell(1, 1) && 
				getCell(1, 1) == getCell(2, 2)) {
			return getCell(0, 0);
		}
		
		if (getCell(0, 2) != Piece.N &&
				getCell(0, 2) == getCell(1, 1) && 
				getCell(1, 1) == getCell(2, 0)) {
			return getCell(0, 2);
		}
		
		return Piece.N;
	}

	public boolean isTied(Piece currentPiece) {
		if (piecesSoFar < 5)
			return false;
		
		if (piecesSoFar == 9 && hasWon() == Piece.N)
			return true;
		else if (piecesSoFar == 9)
			return false;
		
		boolean val = true;
		
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (getCell(x, y) == Piece.N) {
					boardLayout[x][y] = currentPiece;
					piecesSoFar++;
					val &= isTied(currentPiece == Piece.X ? Piece.O : Piece.X);
					boardLayout[x][y] = Piece.N;
					piecesSoFar--;
				}
			}
		}
		
		return val;
	}
	
	public String getStringRep() {
		String rep = "";
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				rep += getCell(x, y);
			}
		}
		return rep;
	}

}
