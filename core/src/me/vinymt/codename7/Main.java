package me.vinymt.codename7;

import com.badlogic.gdx.Game;

public class Main extends Game {

	MainMenu menu;

	@Override
	public void create () {
		menu = new MainMenu(this);
		setScreen(menu);
	}
	
	public void dispose() {
		screen.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		screen.resize(width, height);
	}
	
	@Override
	public void render() {
		super.render();
	}
}

