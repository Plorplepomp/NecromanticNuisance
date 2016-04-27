/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import java.lang.Math;
import static java.lang.Math.sqrt;
import static java.lang.Math.asin;
import static java.lang.Math.atan;

/**
 *
 * @author Scott
 */
public class Arrow extends Actor
{
    Texture texture;
    Sprite sprite;
    float velocity;
    float distSquared,angle;
    MoveToAction init, shoot, moveOff;
    
    public Arrow(float startX, float startY, float destX, float destY)
    {
        this.setName("arrow");
        texture = new Texture("arrow.png");
        sprite = new Sprite(texture);
        sprite.setScale(0.1f);
        angle = (float) Math.toDegrees(atan((destY-startY)/(destX-startX)));
        System.out.println(String.valueOf(angle));
        sprite.setOriginCenter();
        sprite.rotate(angle+90);
        //sprite.setRotation(angle);
        
        velocity = 200;
        
        init = new MoveToAction();
        init.setPosition(startX, startY);
        distSquared = ((startX-destX)*(startX-destX)+(startY-destY)*(startY-destY));
        
        shoot = new MoveToAction();
        shoot.setPosition(destX, destY);
        shoot.setDuration((float) (sqrt(distSquared)/velocity));
        
        moveOff = new MoveToAction();
        moveOff.setPosition(-100f, -100f);
        
        SequenceAction sa = new SequenceAction(init, shoot, moveOff);
        
        this.addAction(sa);
    } 
        
    @Override
    public void act(float delta)
    {
        if((this.getX()==-100)&&(this.getY()==-100))
            this.remove();
        super.act(delta);
    }
   
    public void draw(Batch batch, float parentAlpha)
    {
        sprite.setColor(this.getColor());
        batch.setColor(this.getColor());
        sprite.draw(batch);
    }
    
    
    @Override
    protected void positionChanged()
    {
      sprite.setPosition(getX()+30, getY()-150);
      super.positionChanged();
    }
        
    
}
