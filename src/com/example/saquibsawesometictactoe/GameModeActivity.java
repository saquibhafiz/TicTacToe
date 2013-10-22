package com.example.saquibsawesometictactoe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saquibsawesometictactoe.objects.GameBoard.Piece;

public class GameModeActivity extends Activity {
	
	public static final String GAMEMODE = "GAME MODE";
	public static final String STARTINGPIECE = "STARTING PIECE";
	
	private TextView gameModelLabel;
	private ImageView pvp;
	private ImageView pve;
	
	private TextView startPieceLabel;
	private ImageView pieceX;
	private ImageView pieceO;
	
	public static enum GameMode { NONE, PVP, PVE };
	
	GameMode gameMode = GameMode.NONE;
	Piece startingPiece = Piece.N;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game_mode);
		
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Lato-Black.ttf");
		gameModelLabel = (TextView) findViewById(R.id.game_mode_label);
		startPieceLabel = (TextView) findViewById(R.id.starting_piece_label);
		gameModelLabel.setTypeface(myTypeface);
		startPieceLabel.setTypeface(myTypeface);
		
		pvp = (ImageView) findViewById(R.id.pvp_choice);
		pve = (ImageView) findViewById(R.id.pve_choice);
		
		pieceX = (ImageView) findViewById(R.id.piece_x_choice);
		pieceO = (ImageView) findViewById(R.id.piece_o_choice);
		
		pvp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setGameMode(GameMode.PVP);
			}
		});
		
		pve.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setGameMode(GameMode.PVE);
			}
		});
		
		pieceX.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setStartingPiece(Piece.X);
			}
		});
		
		pieceO.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setStartingPiece(Piece.O);
			}
		});
		
		retreivePreviousValues();
		
	}
	
	private void setStartingPiece(Piece newStartingPiece) {
		startingPiece = newStartingPiece;
		
		switch(startingPiece) {
		case X:
			pieceX.setImageResource(R.drawable.piecex);
			pieceO.setImageResource(R.drawable.pieceogrey);
			break;
		case O:
			pieceX.setImageResource(R.drawable.piecexgrey);
			pieceO.setImageResource(R.drawable.pieceo);
			break;
		case N:
			break;
		}
		
		notifiyDataSelected();
		
	}

	private void setGameMode(GameMode newGameMode) {
		gameMode = newGameMode;
		
		switch(gameMode) {
		case PVE:
			pvp.setImageResource(R.drawable.pvpgrey);
			pve.setImageResource(R.drawable.pve);
			break;
		case PVP:
			pvp.setImageResource(R.drawable.pvp);
			pve.setImageResource(R.drawable.pvegrey);
			break;
		case NONE:
			break;
		}
		
		notifiyDataSelected();
	}

	private void notifiyDataSelected() {
		if (startingPiece != Piece.N && gameMode != GameMode.NONE) {
			Intent gameBoardIntent = new Intent(this, GameBoardActivity.class);
			gameBoardIntent.putExtra(GAMEMODE, gameMode == GameMode.PVP);
			gameBoardIntent.putExtra(STARTINGPIECE, startingPiece == Piece.X);
			startActivity(gameBoardIntent);
			finish();
		}
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	    
	    SharedPreferences.Editor ed = getSharedPreferences(GameBoardActivity.SHAREDPREF, MODE_PRIVATE).edit();
	    ed.putInt(GameBoardActivity.STARTING_PIECE, startingPiece.ordinal());
	    ed.putInt(GameBoardActivity.GAME_MODE, gameMode.ordinal());
        ed.commit();
	}
	
	public void retreivePreviousValues() {
		SharedPreferences pref = getSharedPreferences(GameBoardActivity.SHAREDPREF, MODE_PRIVATE);
		setStartingPiece(Piece.values()[pref.getInt(GameBoardActivity.STARTING_PIECE, 0)]);
		setGameMode(GameMode.values()[pref.getInt(GameBoardActivity.GAME_MODE, gameMode.ordinal())]);
	}
	
}
