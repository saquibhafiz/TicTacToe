package com.example.saquibsawesometictactoe.objects;

import android.util.Log;

import com.example.saquibsawesometictactoe.objects.GameBoard.Piece;

public class PlayerAI {
	private static Piece myPiece = Piece.O;
	private static Piece opponentsPiece = Piece.X;
	
	private static int[] nextMoveCoords = {-1, -1};
	
	public static int[] getNextMove(GameBoard currentGameBoard) {
		if (finishingMove(currentGameBoard, myPiece)) {
			Log.d("GBPAI", "Finishing move");
		} else if (finishingMove(currentGameBoard, opponentsPiece)) {
			Log.d("GBPAI", "Blocking move");
		} else { 
			Log.d("GBPAI", "Using random move");
			createRandomMove(currentGameBoard);
		}
		
		return nextMoveCoords;
	}
	
	private static void createRandomMove(GameBoard currentGameBoard) {
		int x = (int) (Math.random() * 3);
		int y = (int) (Math.random() * 3);
		
		while (currentGameBoard.getCell(x, y) != Piece.N) {
			x = (x+1)%3;
			if (x == 0)
				y = (y+1)%3;
		}
		
		nextMoveCoords[0] = x;
		nextMoveCoords[1] = y;
	}

	private static boolean finishingMove(GameBoard currentGameBoard, Piece winningPiece) {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (currentGameBoard.getCell(x, y) == winningPiece &&
						currentGameBoard.getCell((x+1)%3, y) == winningPiece && 
						currentGameBoard.getCell((x+2)%3, y) == Piece.N) {
					nextMoveCoords[0] = (x+2)%3;
					nextMoveCoords[1] = y;
					return true;
				}
				
				if (currentGameBoard.getCell(x, y) == winningPiece &&
						currentGameBoard.getCell(x, (y+1)%3) == winningPiece && 
						currentGameBoard.getCell(x, (y+2)%3) == Piece.N) {
					nextMoveCoords[0] = x;
					nextMoveCoords[1] = (y+2)%3;
					return true;
				}
			}
			
			if (currentGameBoard.getCell(x, x) == winningPiece &&
					currentGameBoard.getCell((x+1)%3, (x+1)%3) == winningPiece && 
					currentGameBoard.getCell((x+2)%3, (x+2)%3) == Piece.N) {
				nextMoveCoords[0] = (x+2)%3;
				nextMoveCoords[1] = (x+2)%3;
				return true;
			}
			
			if (currentGameBoard.getCell(x, (2-x+3)%3) == winningPiece &&
					currentGameBoard.getCell((x+1)%3, (2*(x+2))%3) == winningPiece && 
					currentGameBoard.getCell((x+2)%3, (x*2)%3) == Piece.N) {
				nextMoveCoords[0] = (x+2)%3;
				nextMoveCoords[1] = (x*2)%3;
				return true;
			}
		}
		
		return false;
	}

}
