package me.vinymt.codename7;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class EnemyShip extends Ship {
	
	public boolean remove = false;
	public Rectangle boundingBox;
	
	public EnemyShip(float speed, int life, float laserWidth, float laserHeight, float betweenShotTime,
			float laserMoveSpeed, float x, float y, float width, float height, TextureRegion shipTexture,
			Texture laser) {
		super(speed, life, laserWidth, laserHeight, betweenShotTime, laserMoveSpeed, x, y, width, height, shipTexture, laser);
		
		this.boundingBox = new Rectangle(x, y, width, height);
		
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		boundingBox.set(x, y, width, height);
		
		if(y < -height) {
			remove = true;
		}
		
		y -= speed * deltaTime;
	}
	
	public Lasers[] shotLasers() {
		Lasers[] lasers = new Lasers[2];
		lasers[0] = new Lasers(laserMoveSpeed, x + width * 0.23f, y - laserHeight, laserWidth, laserHeight, laser);
		lasers[1] = new Lasers(laserMoveSpeed, x + width * 0.77f, y - laserHeight, laserWidth, laserHeight, laser);
		
		lastShotTime = 0;
		return lasers;
	}

	public void draw(Batch batch) {
		batch.draw(shipTexture, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
	}
}
