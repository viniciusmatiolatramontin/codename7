package me.vinymt.codename7;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Kamikaze {

	float speed;
	int life;
	
	float x;
	float y;
	float width;
	float height;
	Rectangle boundingBox;
	
	TextureRegion texture;
	boolean remove = false;
	
	
	public Kamikaze(float speed, int life, float x, float y, float width, float height, TextureRegion texture) {
		this.speed = speed;
		this.life = life;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.boundingBox = new Rectangle(x, y, width, height);
		this.texture = texture;
	}
	
	public void update(float deltaTime) {
		boundingBox.set(x, y, width, height);
		
		if(y < -height) {
			remove = true;
		}
		
		y -= speed * deltaTime;
	}
	
	public boolean overlap(Rectangle oneRectangle) {
		return boundingBox.overlaps(oneRectangle);
	}
	
	public void hit() {
		life--;
	}
	
	public void draw(Batch batch) {
		batch.draw(texture, x, y, width, height);
	}
}
