package com.example.saquibsawesometictactoe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class StartScreenActivity extends Activity {
	private Button startGameButton;
	private TextView title;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_start_screen);
		
		title = (TextView) findViewById(R.id.title);
		startGameButton = (Button) findViewById(R.id.start_game_buttons);
		
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Lato-Black.ttf");
	    title.setTypeface(myTypeface);
	    startGameButton.setTypeface(myTypeface);
		
		startGameButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent gameModeChoiceIntent = new Intent(StartScreenActivity.this, GameModeActivity.class);
				startActivity(gameModeChoiceIntent);
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		getSharedPreferences(GameBoardActivity.SHAREDPREF, MODE_PRIVATE).edit().clear().commit();
	}
}
