package me.vinymt.codename7;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameOverScreen implements Screen {

	Sound sound;
	
	Camera camera;
	Viewport viewport;
	
	SpriteBatch batch;
	
	MainMenu menu;
	
	Main game;
	int score, highScore;
	float world_width = 186, world_height = 312;
	
	int banner_width = 128;
	int banner_height = 96;
	
	Texture banner;
	BitmapFont highScoreFont;
	
	public GameOverScreen(Main game, int score) {
		sound = Gdx.audio.newSound(Gdx.files.internal("GameOver.wav"));
		sound.play(0.5f);
		
		camera = new OrthographicCamera();
		viewport = new StretchViewport(world_width, world_height, camera);
		
		batch = new SpriteBatch();
		this.game = game;
		this.score = score;
		
		Preferences prefs = Gdx.app.getPreferences("Main");
		this.highScore = prefs.getInteger("highscore", 0);
		
		if(score > highScore) {
			prefs.putInteger("highscore", score);
			prefs.flush();
		}
		
		banner = new Texture("GAME OVER.png");
		highScoreFont = new BitmapFont(Gdx.files.internal("score.fnt"));
	}
	
	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		
		GlyphLayout scoreLayout = new GlyphLayout(highScoreFont, "PONTOS:" + score);
		highScoreFont.draw(batch, scoreLayout, world_width/2 - scoreLayout.width/2, world_height/2 + 80);
		
		GlyphLayout highScoreLayout = new GlyphLayout(highScoreFont, "RECORDE:" + highScore);
		highScoreFont.draw(batch, highScoreLayout, world_width/2 - highScoreLayout.width/2, world_height/2 + 40);
		
		GlyphLayout tryAgain = new GlyphLayout(highScoreFont, "JOGAR DE NOVO");
		GlyphLayout menu = new GlyphLayout(highScoreFont, "VOLTAR AO MENU");
		
		float tryAgainX = world_width/2 - tryAgain.width/2;
		float tryAgainY = world_height/2 - tryAgain.height/2 - 10;
		float menuX = world_width/2 - menu.width/2;
		float menuY = world_height/2 - menu.height/2 - tryAgain.height - 40;
		
		Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		
		if(Gdx.input.isTouched()) {
			if(mousePos.x > tryAgainX && mousePos.x < tryAgainX + tryAgain.width && mousePos.y < tryAgainY && mousePos.y > tryAgainY - tryAgain.height) {
				this.dispose();
				batch.end();
				game.setScreen(new MainScreen(game));
				return;
			}
			
			if(mousePos.x > menuX && mousePos.x < menuX + tryAgain.width && mousePos.y < menuY && mousePos.y > menuY - menu.height) {
				this.dispose();
				batch.end();
				game.setScreen(new MainMenu(game));
				return;
			}
		}
		
		highScoreFont.draw(batch, tryAgain, tryAgainX, tryAgainY);
		highScoreFont.draw(batch, menu, menuX, menuY);
		
		batch.end();
	}
	
	

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		viewport.update(width, height, true);
		batch.setProjectionMatrix(camera.combined);
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
