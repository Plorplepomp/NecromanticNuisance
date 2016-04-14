/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actors;

/**
 *
 * @author Scott
 */

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class Castle extends Actor
{
    Texture texture;
    MoveToAction ms, ma1, ma2, ma3, ma4, ma5, ma6, ma7, ma8, ma9, ma10;
    public Sprite sprite;
    float health;
    float velocity;
    
    public Castle(float health, float damage, Stage stage)
    {
        this.setName("footman");
        texture = new Texture("castle.png");
        sprite = new Sprite(texture);
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        this.setZIndex((int)health);
        velocity = 70;
        //Rectangle body = new Rectangle(getX(), getY(), getWidth(), getHeight());
        //ScaleByAction sba = new ScaleByAction();
        //sba.setAmount(0.25f);
        
        ms = new MoveToAction();
        ms.setPosition(50f,400f);
        
        Castle.this.addAction(ms);
  /*      
        int len = stage.size;
        for(i=0; i<len; i++){
        Actor a = stageActors.get(i);
        if(a.getName().equals("myactor")){
        //a is your Actor!
        break;
    */
    }

    @Override
    public void setZIndex(int health)
    {
        this.health = (float)health;
    }
    
    @Override
    public int getZIndex()
    {
        return (int)this.health;
    }

 /*   @Override
    public void act(float delta)
    {
        Array<Actor> stageActors = stage.getActors();
    }
 */
    
    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        sprite.setColor(this.getColor());
        batch.setColor(this.getColor());
        sprite.draw(batch);
    }
    
    @Override
    protected void positionChanged()
    {
      sprite.setPosition(getX(), getY());
      /*
      body.x = getX();
      body.y = getY();
      body.height = getHeight();
      body.width = getWidth();
      super.positionChanged();
      
    //  Array<Actor> stageActors = stage.getActors();
      */
    }
    
}