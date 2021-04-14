package me.vinymt.codename7;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenu implements Screen {

	Sound sound;
	
	SpriteBatch batch;
	
	Camera camera;
	Viewport viewport;
	
	Main game;
	
	float world_width = 186, world_height = 312;
	
	BitmapFont font;
	
	public MainMenu(Main game) {
		sound = Gdx.audio.newSound(Gdx.files.internal("Loop_1.ogg"));
		sound.loop(0.5f);
		
		camera = new OrthographicCamera();
		viewport = new StretchViewport(world_width, world_height, camera);
		
		font = new BitmapFont(Gdx.files.internal("score.fnt"));
		
		batch = new SpriteBatch();
		this.game = game;
		
	}
	
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		
		GlyphLayout highScoreLayout = new GlyphLayout(font, "Codename 7");
		font.draw(batch, highScoreLayout, world_width/2 - highScoreLayout.width/2, world_height/2 + 80);
		
		GlyphLayout play = new GlyphLayout(font, "JOGAR");
		GlyphLayout quit = new GlyphLayout(font, "SAIR");
		
		float playX = world_width/2 - play.width/2;
		float playY = world_height/2 - play.height/2;
		float quitX = world_width/2 - quit.width/2;
		float quitY = world_height/2 - quit.height/2 - play.height - 25;
		
		Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		
		if(Gdx.input.isTouched()) {
			if(mousePos.x > playX && mousePos.x < playX + play.width && mousePos.y < playY && mousePos.y > playY - play.height) {
				this.dispose();
				sound.dispose();
				batch.end();
				game.setScreen(new MainScreen(game));
				return;
			}
			
			if(mousePos.x > quitX && mousePos.x < quitX + play.width && mousePos.y < quitY && mousePos.y > quitY - quit.height) {
				this.dispose();
				sound.dispose();
				batch.end();
				Gdx.app.exit();
				return;
			}
		}
		
		font.draw(batch, play, playX, playY);
		font.draw(batch, quit, quitX, quitY);
		
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
