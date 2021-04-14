package me.vinymt.codename7;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainScreen implements com.badlogic.gdx.Screen {

	//som
	Sound sound;
	Sound laserSound;
	Sound expSound;
	Sound kamiSound;
	
	//tela
	private Camera camera;
	private Viewport viewport;
	
	//graficos
	private SpriteBatch batch;
	private TextureAtlas atlas;
	private TextureRegion background;
	private TextureRegion player, enemy, kamikaze, life, meteor;
	private Texture playerLasers, enemyLasers;
	
	//tempo
	private int backgroundOffset;
	private float timer;
	private float timerKamikaze;
	public float timerMeteor;
	public static final float min_time = 0.8f;
	public static final float max_time = 1.3f;
	public static final float min_Kamikaze = 22;
	public static final float max_Kamikaze = 27;
	public static final float min_Meteor = 1.25f;
	public static final float max_Meteor = 3.5f;
	
	//mundo
	private final int world_width = 144;
	private final int world_height = 256;
	
	//objetos do jogo
	private Ship playerShip;
	private LinkedList<Lasers> playerLasersList;
	private LinkedList<Lasers> enemyLasersList;
	private ArrayList<EnemyShip> enemyShipList;
	private ArrayList<Kamikaze> kamikazeList;
	private ArrayList<Meteor> meteorList;
	private CopyOnWriteArrayList<Explosion> explosionsList;
	private CopyOnWriteArrayList<Explosion> meteorExplosionsList;
	
	//spawn
	private Random random;
	
	//fonte
	private BitmapFont font;
	private int score;
	
	//game
	private Main game;
	
	public MainScreen(Main game) {
		sound = Gdx.audio.newSound(Gdx.files.internal("boss_battle_#2.wav"));
		sound.loop(0.5f);
		laserSound = Gdx.audio.newSound(Gdx.files.internal("sfx_sounds_high5.wav"));
		expSound = Gdx.audio.newSound(Gdx.files.internal("sfx_exp_short_hard16.wav"));
		kamiSound = Gdx.audio.newSound(Gdx.files.internal("sfx_sound_depressurizing.wav"));
		
		camera = new OrthographicCamera();
		viewport = new StretchViewport(world_width, world_height, camera);
		
		atlas = new TextureAtlas("textures.atlas");
		
		background = new TextureRegion(atlas.findRegion("purple"));
		backgroundOffset = 0;
		
		player = new TextureRegion(atlas.findRegion("playerShip1_red"));
		enemy = new TextureRegion(atlas.findRegion("enemyRed3"));
		kamikaze = new TextureRegion(atlas.findRegion("enemyBlack4"));
		playerLasers = new Texture("laserBlue03.png");
		enemyLasers = new Texture("laserGreen05.png");
		life = new TextureRegion(atlas.findRegion("pill_red"));
		meteor = new TextureRegion(atlas.findRegion("meteorBrown_med1"));
		
		playerShip = new Ship(65, 6, 0.8f, 8, 0.5f, 75, world_width/2, world_height/4, 20, 20, player, playerLasers);
		
		playerLasersList = new LinkedList<>();
		enemyLasersList = new LinkedList<>();
		enemyShipList = new ArrayList<EnemyShip>();
		explosionsList = new CopyOnWriteArrayList<Explosion>();
		meteorExplosionsList = new CopyOnWriteArrayList<Explosion>();
		kamikazeList = new ArrayList<Kamikaze>();
		meteorList = new ArrayList<Meteor>();
		
		random = new Random();
		
		timer = random.nextFloat() * (max_time - min_time) + min_time;
		timerKamikaze = random.nextFloat() * (max_Kamikaze - min_Kamikaze) + min_Kamikaze;
		
		font = new BitmapFont(Gdx.files.internal("score.fnt"));
		score = 0;
		
		batch = new SpriteBatch();
		
		this.game = game;
	}
	
	@Override
	public void render(float deltaTime) {
		// TODO Auto-generated method stub
		batch.begin();
		
		isOutOfBounds();
		
		detectInput(deltaTime);
		
		playerShip.update(deltaTime);
		
		//fundo rolando
		backgroundOffset++;
		if(backgroundOffset % world_height == 0) {
			backgroundOffset = 0;
		}
		
		batch.draw(background, 0, -backgroundOffset, world_width, world_height);
		batch.draw(background, 0, -backgroundOffset + world_height, world_width, world_height);
		
		//spawna inimigos
		if(timer <= 0) {
			timer = random.nextFloat() * (max_time - min_time) + min_time;
			enemyShipList.add(new EnemyShip(85, 3, 0.8f, 8, 0.5f, 125, random.nextInt(world_width - 20), world_height + 20, 20, 20, enemy, enemyLasers));
		}
		
		if(timerKamikaze <= 0) {
			timerKamikaze = random.nextFloat() * (max_Kamikaze - min_Kamikaze) + min_Kamikaze;
			kamikazeList.add(new Kamikaze(222, 30, playerShip.x, world_height + 20, 17.8f, 18, kamikaze));
			kamiSound.play(0.85f);
		}
		
		if(timerMeteor <= 0) {
			timerMeteor = random.nextFloat() * (max_Meteor - min_Meteor) + min_Meteor;
			meteorList.add(new Meteor(95, 1, random.nextInt(world_width - 10), world_height + 10, 10, 10, meteor));
		}
		
		//remove inimigos
		ArrayList<EnemyShip> shipsToRemove = new ArrayList<EnemyShip>();
		for(EnemyShip ships : enemyShipList) {
			ships.update(deltaTime);
			if(ships.remove) {
				shipsToRemove.add(ships);
			}
			if(ships.life <= 0) {
				shipsToRemove.add(ships);
				score += 100;
				explosionsList.add(new Explosion(ships.x, ships.y, ships.width, ships.height));
				expSound.play(0.75f);
			}
			if(ships.overlap(playerShip.boundingBox)) {
				playerShip.life = 0;
			}
		}
		enemyShipList.removeAll(shipsToRemove);
		
		ArrayList<Kamikaze> kamikazeToRemove = new ArrayList<Kamikaze>();
		for(Kamikaze ships : kamikazeList) {
			ships.update(deltaTime);
			if(ships.remove) {
				kamikazeToRemove.add(ships);
			}
			if(ships.overlap(playerShip.boundingBox)) {
				playerShip.life = 0;
			}
		}
		
		ArrayList<Meteor> meteorToRemove = new ArrayList<Meteor>();
		for(Meteor meteor : meteorList) {
			meteor.update(deltaTime);
			if(meteor.remove) {
				meteorToRemove.add(meteor);
			}
			if(meteor.life <= 0) {
				meteorToRemove.add(meteor);
				score += 5;
				meteorExplosionsList.add(new Explosion(meteor.x, meteor.y, meteor.width, meteor.height));
				expSound.play(0.75f);
			}
			if(meteor.overlap(playerShip.boundingBox)) {
				playerShip.life = 0;
			}
		}
		
		meteorList.removeAll(meteorToRemove);
		
		//desenha a a nave do jogador
		playerShip.draw(batch);
		
		//desenha os inimigos
		for(EnemyShip ships : enemyShipList) {
			ships.draw(batch);
		}
		
		for(Kamikaze ships : kamikazeList) {
			ships.draw(batch);
		}
		
		for(Meteor meteor : meteorList) {
			meteor.draw(batch);
		}
		
		timer -= deltaTime;
		timerKamikaze -= deltaTime;
		timerMeteor -= deltaTime;
		
		renderExplosion(deltaTime);
		renderMeteorExplosion(deltaTime);
		
		//checa se o player pode atirar
		if(playerShip.canFire() && Gdx.input.isKeyPressed(Keys.SPACE)) {
			Lasers[] lasers = playerShip.shotLasers();
			for(int i = 0; i < lasers.length; i++) {
				playerLasersList.add(lasers[i]);
				laserSound.play(0.2f);
			}
		}
		
		//checa se o inimigo vai atirar
		for(EnemyShip ships : enemyShipList) {
			if(ships.canFire()) {
				Lasers[] lasers = ships.shotLasers();
				for(int i = 0; i < lasers.length; i++) {
					enemyLasersList.add(lasers[i]);
				}
			}
		}
		
		//checa se o laser intersecta com a nave
		ListIterator<Lasers> iterator3 = enemyLasersList.listIterator();
		while(iterator3.hasNext()) {
			Lasers laser = iterator3.next();
				if(playerShip.overlap(laser.getBoundingBox())) {
					playerShip.hit();
					iterator3.remove();
				}
		}
		
		ListIterator<Lasers> iterator4 = playerLasersList.listIterator();
		while(iterator4.hasNext()) {
			Lasers laser = iterator4.next();
			for(EnemyShip ships: enemyShipList) {
				if(ships.overlap(laser.getBoundingBox())) {
					ships.hit();
					iterator4.remove();
				}
			}
		}
		
		ListIterator<Lasers> iterator5 = playerLasersList.listIterator();
		while(iterator5.hasNext()) {
			Lasers laser = iterator5.next();
			for(Kamikaze ships: kamikazeList) {
				if(ships.overlap(laser.getBoundingBox())) {
					ships.hit();
					iterator5.remove();
				}
			}
		}
		
		ListIterator<Lasers> iterator6 = playerLasersList.listIterator();
		while(iterator6.hasNext()) {
			Lasers laser = iterator6.next();
			for(Meteor meteor: meteorList) {
				if(meteor.overlap(laser.getBoundingBox())) {
					meteor.hit();
					iterator6.remove();
				}
			}
		}
		
		//desenha e remove os lasers do player da tela
		ListIterator<Lasers> iterator1 = playerLasersList.listIterator();
		while(iterator1.hasNext()) {
			Lasers laser = iterator1.next();
			laser.draw(batch);
			laser.y += laser.moveSpeed * deltaTime;
			if(laser.y > world_height) {
				iterator1.remove();
			}
			
		}
		
		//desenha e remove os lasers inimigos da tela
		ListIterator<Lasers> iterator2 = enemyLasersList.listIterator();
		while(iterator2.hasNext()) {
			Lasers laser = iterator2.next();
			laser.draw(batch);
			laser.y -= laser.moveSpeed * deltaTime;
			
			if(laser.y + laser.height < 0) {
				iterator2.remove();
			}
			
		}
		
		GlyphLayout scoreLayout = new GlyphLayout(font, "" + score);
		font.draw(batch, scoreLayout, world_width - scoreLayout.width - 3, world_height - scoreLayout.height);
		
		GlyphLayout lifeLayout = new GlyphLayout(font, "" + playerShip.life);
		batch.draw(life, 3, world_height - 22, 13, 13);
		font.draw(batch, lifeLayout, lifeLayout.width/2 + 14, world_height - lifeLayout.height);
		
		
		batch.end();
		
		if(playerShip.life <= 0) {
			this.dispose();
			sound.dispose();
			laserSound.dispose();
			kamiSound.dispose();
			expSound.dispose();
			game.setScreen(new GameOverScreen(game, score));
			return;
		}
	}
	
	//checa se o player esta fora da tela e reposiciona-o para dentro
	private void isOutOfBounds() {
		float topLimit, bottomLimit, rightLimit, leftLimit;
		topLimit = world_height - playerShip.height;
		bottomLimit = 0;
		rightLimit = world_width - playerShip.width;
		leftLimit = 0;
		if(playerShip.x >= rightLimit) {
			playerShip.x = rightLimit - 1;
		}
		if(playerShip.x <= leftLimit) {
			playerShip.x = leftLimit + 1;
		}
		if(playerShip.y >= topLimit) {
			playerShip.y = topLimit - 1;
		}
		if(playerShip.y <= bottomLimit) {
			playerShip.y = bottomLimit + 1;
		}
	}
	
	//detecta input e move o player
	private void detectInput(float deltaTime) {	
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			playerShip.y = playerShip.y + playerShip.speed * deltaTime;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			playerShip.x = playerShip.x + playerShip.speed * deltaTime;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			playerShip.y = playerShip.y - playerShip.speed * deltaTime;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			playerShip.x = playerShip.x - playerShip.speed * deltaTime;
		}
	}
	
	public void renderExplosion(float deltaTime) {
		ListIterator<Explosion> explosionIterator = explosionsList.listIterator();
		while(explosionIterator.hasNext()) {
			Explosion explosion = explosionIterator.next();
			explosion.update(deltaTime);
			if(explosion.remove) {
				explosionsList.remove(explosion);
			} else {
				explosion.draw(batch);
			}
		}
	}
	
	public void renderMeteorExplosion(float deltaTime) {
		ListIterator<Explosion> explosionIterator = meteorExplosionsList.listIterator();
		while(explosionIterator.hasNext()) {
			Explosion explosion = explosionIterator.next();
			explosion.update(deltaTime);
			if(explosion.remove) {
				meteorExplosionsList.remove(explosion);
			} else {
				explosion.draw(batch);
			}
		}
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


	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

}
