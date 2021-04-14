package me.vinymt.codename7;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Explosion {

	
	public float frame_length = 0.1f;
	public int sizeX = 112;
	public int sizeY = 109;
	
	public Rectangle boundingBox;
	
	private Animation<TextureRegion> anim = null;
	float x, y, width, height;
	float stateTime;
	
	public boolean remove = false;
	
	public Explosion(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		stateTime = 0;
		this.boundingBox = new Rectangle(x, y, width, height);
		
		if(anim == null) {
			anim = new Animation<TextureRegion>(frame_length, TextureRegion.split(new Texture("explosionBig.png"), sizeX, sizeY)[0]);
		}
	}
	
	public void update(float deltaTime) {
		stateTime += deltaTime;
		if(anim.isAnimationFinished(stateTime)) {
			remove = true;
		}
		
	}
	   
	public void draw(SpriteBatch batch) {
		batch.draw(anim.getKeyFrame(stateTime), boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
	}
	
}
