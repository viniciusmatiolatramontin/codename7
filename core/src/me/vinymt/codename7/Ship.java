package me.vinymt.codename7;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Ship {
	float speed; 
	int life;
	
	float x;
	float y;
	Rectangle boundingBox;
	
	float width;
	float height;
	float laserWidth;
	float laserHeight;
	
	float shotDelay;
	float lastShotTime = 0;
	float betweenShotTime;
	float laserMoveSpeed;
	
	TextureRegion shipTexture;
	Texture laser;
	
	public Ship(float speed, int life, float laserWidth, float laserHeight, float betweenShotTime, float laserMoveSpeed, float x, float y, float width, float height, TextureRegion shipTexture, Texture laser) {
		this.speed = speed;
		this.life = life;
		this.x = x;
		this.y = y - height/2;
		this.width = width;
		this.height = height;
		this.boundingBox = new Rectangle(x, y, width, height);
		this.shipTexture = shipTexture;
		this.laser = laser;
		this.laserWidth = laserWidth;
		this.laserHeight = laserHeight;
		this.laserMoveSpeed = laserMoveSpeed;
		this.betweenShotTime = betweenShotTime;
	}
	
	public void update(float deltaTime) {
		lastShotTime += deltaTime;
		boundingBox.set(x, y, width, height);
	}
	
	public boolean canFire() {
		boolean result = lastShotTime - betweenShotTime >= 0;
		return result;
	}
	
	public Lasers[] shotLasers() {
		Lasers[] lasers = new Lasers[2];
		lasers[0] = new Lasers(laserMoveSpeed, x + width * 0.08f, y + height * 0.4f, laserWidth, laserHeight, laser);
		lasers[1] = new Lasers(laserMoveSpeed, x + width * 0.92f, y + height * 0.4f, laserWidth, laserHeight, laser);
		
		lastShotTime = 0;
		return lasers;
	}
	
	
	public boolean overlap(Rectangle oneRectangle) {
		return boundingBox.overlaps(oneRectangle);
	}
	
	public void hit() {
		life--;
	}
	
	public void draw(Batch batch) {
		batch.draw(shipTexture, x, y, width, height);
	}
}
