package com.example.saquibsawesometictactoe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saquibsawesometictactoe.GameModeActivity.GameMode;
import com.example.saquibsawesometictactoe.objects.GameBoard;
import com.example.saquibsawesometictactoe.objects.GameBoard.Piece;
import com.example.saquibsawesometictactoe.objects.PlayerAI;

public class GameBoardActivity extends Activity implements OnClickListener{
	
	public static final String SHAREDPREF = "GBA";
	private static final String FRESH_BOARD = "NNNNNNNNN";
	private static final String GAMEOVER = "GAMEOVER";
	private static final String GAME_BOARD = "GAME_BOARD";
	private static final String P2_SCORE = "P2_SCORE";
	private static final String P1_SCORE = "P1_SCORE";
	public static final String GAME_MODE = "GAME_MODE";
	public static final String STARTING_PIECE = "STARTING_PIECE";
	private static final String CURRENT_PIECE = "CURRENT_PIECE";
	private static final String TIED = "TIED";
	
	private int p1Score = 0;
	private int p2Score = 0;
	
	private boolean gameOver;
	
	private GameMode gameMode = GameMode.NONE;
	
	private Piece currentPiece = Piece.N;
	private Piece startingPiece = Piece.N;
	
	private GameBoard gameBoard;
	
	private GridLayout gameBoardGridLayout;
	private TextView winnerAnnouncement;
	private TextView p1ScoreTextView;
	private TextView p2ScoreTextView;
	private TextView p1ScoreLabel;
	private TextView p2ScoreLabel;
	private Button rematchButton;
	private boolean tied;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		gameMode = getIntent().getBooleanExtra(GameModeActivity.GAMEMODE, true) ? GameMode.PVP : GameMode.PVE;
		startingPiece = getIntent().getBooleanExtra(GameModeActivity.STARTINGPIECE, true) ? Piece.X : Piece.O;
		
		currentPiece = startingPiece;
		
		retreivePreviousValues();

		setContentView(R.layout.activity_game_screen);
		
		declareViews();
		
		updateScoreTextViews();
		updateGameStatus();
		drawBoardFromObject();
		
		moveByAI();
		
		rematchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				reset();
				
				rematchButton.setVisibility(View.INVISIBLE);
				winnerAnnouncement.setVisibility(View.INVISIBLE);
			}
		});
		
		for (int x = 0; x < gameBoardGridLayout.getChildCount(); x++) {
			gameBoardGridLayout.getChildAt(x).setOnClickListener(this);
		}

	}

	public void moveByAI() {
		if (gameMode == GameMode.PVE && currentPiece == Piece.O) {
			int nextMove[] = PlayerAI.getNextMove(gameBoard);
			makeMoveIfPossible(gameBoardGridLayout.getChildAt(nextMove[0]*3 + nextMove[1]), nextMove[0], nextMove[1]);
		}
	}

	private void updateGameStatus() {
		if (gameOver) {
			rematchButton.setVisibility(View.VISIBLE);
			if (tied)
				showTieGameLabel();
			else
				showVictoryLabel(currentPiece);
		} else {
			rematchButton.setVisibility(View.INVISIBLE);
		}
		
	}

	public void declareViews() {
		gameBoardGridLayout = (GridLayout) findViewById(R.id.game_board_grid_layout);
		rematchButton = (Button) findViewById(R.id.rematch_button);
		winnerAnnouncement = (TextView) findViewById(R.id.winner_announcement);
		p1ScoreTextView = (TextView) findViewById(R.id.p1score);
		p2ScoreTextView = (TextView) findViewById(R.id.p2score);
		p1ScoreLabel = (TextView) findViewById(R.id.p1score_label);
		p2ScoreLabel = (TextView) findViewById(R.id.p2score_label);
		
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Lato-Black.ttf");
		
		rematchButton.setTypeface(myTypeface);
		winnerAnnouncement.setTypeface(myTypeface);
		p1ScoreTextView.setTypeface(myTypeface);
		p2ScoreTextView.setTypeface(myTypeface);
		p1ScoreLabel.setTypeface(myTypeface);
		p2ScoreLabel.setTypeface(myTypeface);
	}

	public void retreivePreviousValues() {
		SharedPreferences pref = getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
		currentPiece = Piece.values()[pref.getInt(CURRENT_PIECE, currentPiece.ordinal())];
		startingPiece = Piece.values()[pref.getInt(STARTING_PIECE, currentPiece.ordinal())];
		gameMode = GameMode.values()[pref.getInt(GAME_MODE, gameMode.ordinal())];
	    p1Score = pref.getInt(P1_SCORE, 0);
	    p2Score = pref.getInt(P2_SCORE, 0);
	    gameBoard = new GameBoard(pref.getString(GAME_BOARD, FRESH_BOARD));
	    gameOver = pref.getBoolean(GAMEOVER, false);
	    tied = pref.getBoolean(TIED, false);
	}
	
	private void drawBoardFromObject() {
		for (int x = 0; x < gameBoardGridLayout.getChildCount(); x++) {
			if (gameBoard.getCell(x/3, x%3) == Piece.X)
				((ImageView)gameBoardGridLayout.getChildAt(x)).setImageResource(R.drawable.gameboardcellpiecex);
			else if (gameBoard.getCell(x/3, x%3) == Piece.O)
				((ImageView)gameBoardGridLayout.getChildAt(x)).setImageResource(R.drawable.gameboardcellpieceo);
		}
	}

	private void reset() {
		gameOver  = false;
		gameBoard.clear();
		startingPiece = currentPiece;
		resetUI();
		
		if (gameMode == GameMode.PVE && currentPiece == Piece.O)
			moveByAI();
	}
	
	private void resetUI() {
		for (int x = 0; x < gameBoardGridLayout.getChildCount(); x++) {
			((ImageView) gameBoardGridLayout.getChildAt(x)).setImageResource(R.drawable.gameboardcell);
		}
	}

	private void drawOnBoard(View v) {
		((ImageView)v).setImageResource(currentPiece == Piece.X ? R.drawable.gameboardcellpiecex : R.drawable.gameboardcellpieceo);
	}

	private void nextTurn() {
		currentPiece = currentPiece == Piece.X ? Piece.O : Piece.X;
	}

	@Override
	public void onClick(View v) {
		if (gameOver || (gameMode == GameMode.PVE && currentPiece == Piece.O))
			return;
		
		String tag = (String) v.getTag();
		int cellCoords = Integer.parseInt(tag);
		
		makeMoveIfPossible(v, cellCoords/10, cellCoords%10);
		
	}

	public void makeMoveIfPossible(View v, int x, int y) {
		if (gameBoard.occupyCell(x, y, currentPiece)) {
			drawOnBoard(v);
			
			nextTurn();
			
			Piece winner;
			
			if (gameBoard.isTied(currentPiece)) {
				showTieGameLabel();
				rematchButton.setVisibility(View.VISIBLE);
				gameOver = true;
				tied = true;
			}
			
			if ((winner = gameBoard.hasWon()) != Piece.N) {
				gameOver = true;
				updateScore(winner);
				showVictoryLabel(winner);
				rematchButton.setVisibility(View.VISIBLE);
				tied = false;
			}
			
			if (!gameOver)
				moveByAI();
		}
	}

	private void updateScore(Piece winner) {
		if (winner == Piece.X)
			p1Score++;
		else if (winner == Piece.O)
			p2Score++;
	}

	private void showVictoryLabel(Piece winner) {
		winnerAnnouncement.setText("Player " + (currentPiece == Piece.X ? Piece.O : Piece.X) + " has won!");
		winnerAnnouncement.setVisibility(View.VISIBLE);
		
		updateScoreTextViews();
	}
	
	private void updateScoreTextViews() {
		p1ScoreTextView.setText(""+p1Score);
		p2ScoreTextView.setText(""+p2Score);
	}

	private void showTieGameLabel() {
		winnerAnnouncement.setText("Tie Game!");
		winnerAnnouncement.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	    
	    SharedPreferences.Editor ed = getSharedPreferences(SHAREDPREF, MODE_PRIVATE).edit();
	    ed.putInt(CURRENT_PIECE, currentPiece.ordinal());
	    ed.putInt(STARTING_PIECE, startingPiece.ordinal());
	    ed.putInt(GAME_MODE, gameMode.ordinal());
	    ed.putInt(P1_SCORE, p1Score);
	    ed.putInt(P2_SCORE, p2Score);
	    ed.putString(GAME_BOARD, gameBoard.getStringRep());
	    ed.putBoolean(GAMEOVER, gameOver);
	    ed.putBoolean(TIED, tied);
        ed.commit();
	}
	
}
