package me.vinymt.codename7;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class Lasers {

	float moveSpeed;
	
	float x, y;
	float width, height;
	
	Texture texture;
	
	public Lasers(float moveSpeed, float x, float y, float width, float height, Texture texture) {
		this.moveSpeed = moveSpeed;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;	
		this.texture = texture;
	}
	
	public Rectangle getBoundingBox() {
		return new Rectangle(x, y, width, height);
	}
	
	public void draw(Batch batch) {
		batch.draw(texture, x - width/2, y, width, height);
	}
}
