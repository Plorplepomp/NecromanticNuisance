/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import static java.lang.Math.abs;

/**
 *
 * @author Scott
 */
public class Archer extends Actor
{
    public float health, damage;
    Sprite sprite;
    float velocity;
    Texture texture;
    MoveToAction init, ms, ma1, ma2, ma3, ma4, ma5, ma6, ma7;
    public Stage stage;
    int len;
    float arrowTimer;
    
    public Archer(float hlth, float dmg, float x, float y, Stage stg)
    {
        health = hlth;
        damage = dmg;
        stage = stg;
        arrowTimer = 3;
        this.setName("archer");
        texture = new Texture("archer.png");
        sprite = new Sprite(texture);
        sprite.setScale(0.65f);
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        
        velocity = 50;
       
        init = new MoveToAction();
        init.setPosition(x, y);
               
        ms = new MoveToAction();
        ms.setPosition(100f,375f);
        
        ma1 = new MoveToAction();
        ma1.setPosition(315f,375f);
        ma1.setDuration(((315-100 )/velocity));
        
        ma2 = new MoveToAction();
        ma2.setPosition(315f, 180f);
        ma2.setDuration((375-180)/velocity);
        
        ma3 = new MoveToAction();
        ma3.setPosition(510f, 180f);
        ma3.setDuration((510-315)/velocity);
        
        ma4 = new MoveToAction();
        ma4.setPosition(510f, 570f);
        ma4.setDuration((570-180)/velocity);
        
        ma5 = new MoveToAction();
        ma5.setPosition(680f, 570f);
        ma5.setDuration((680-510)/velocity);
        
        ma6 = new MoveToAction();
        ma6.setPosition(680f, 375f);
        ma6.setDuration((570-375)/velocity);
        
        ma7 = new MoveToAction();
        ma7.setPosition(1000f, 375f);
        ma7.setDuration((1000-680)/velocity);
       
           
        
        SequenceAction sa1 = new SequenceAction(ms, ma1, ma2, ma3, ma4);
        SequenceAction sa2 = new SequenceAction(ma5, ma6, ma7);
        SequenceAction sa3 = new SequenceAction(ma1, ma2, ma3, ma4);
        SequenceAction sa4 = new SequenceAction(ma2, ma3, ma4);
        SequenceAction sa5 = new SequenceAction(ma3, ma4, ma5, ma6, ma7);
        SequenceAction sa6 = new SequenceAction(init, ma4, ma5, ma6, ma7);
        SequenceAction sa7 = new SequenceAction(init, ma5, ma6, ma7);
        SequenceAction sa8 = new SequenceAction(init, ma6, ma7);
        SequenceAction csa1 = new SequenceAction(sa1, sa2);
        SequenceAction csa2 = new SequenceAction(init, sa3, sa2);
        SequenceAction csa3 = new SequenceAction(init, sa4, sa2);
        SequenceAction csa4 = new SequenceAction(init, sa5);
        
        if((x==100)&&(y==375))
            this.addAction(csa1);
        else if((y==375)&&(x<315))
        {
            ma1.setDuration((315-x)/velocity);
            sa3 = new SequenceAction(ma1, ma2, ma3, ma4);
            csa2 = new SequenceAction(init, sa3, sa2);
            this.addAction(csa2);
        }
        else if((x==315)&&(y!=180))
        {
            ma2.setDuration((y-180)/velocity);
            sa4 = new SequenceAction(ma2, ma3, ma4);
            csa3 = new SequenceAction(init, sa4, sa2);
            this.addAction(csa3);
        }
        else if((y==180)&&(x!=510))
        {
            ma3.setDuration((510-x)/velocity);
            sa5 = new SequenceAction(ma3, ma4, ma5, ma6, ma7);
            csa4 = new SequenceAction(init, sa5);
            this.addAction(csa4);
        }
        else if((x==510)&&(y!=570))
        {
            ma4.setDuration((570-y)/velocity);
            sa6 = new SequenceAction(init, ma4, ma5, ma6, ma7);
            this.addAction(sa6);
        }
        else if((y==570)&&(x!=680))
        {
            ma5.setDuration((680-x)/velocity);
            sa7 = new SequenceAction(init, ma5, ma6, ma7);
            this.addAction(sa7);
        }
        else if((x==680)&&(y!=375))
        {    
            ma6.setDuration((y-375)/velocity);
            sa8 = new SequenceAction(init, ma6, ma7);
            this.addAction(sa8);
        }
        else if((y==375)&&(x>315))
        {
            ma7.setDuration((1000-x)/velocity);
            this.addAction(ma7);
        }
        
    }
    
    @Override
    public void act(float delta)
    {
        /*arrowTimer -= delta;
        if(arrowTimer <= 0)
        {
            Array<Actor> stageActors = stage.getActors();
            for(int i=0; i<len; i++)
            {
                len = stageActors.size;
                Actor a = stageActors.get(i);
                if((abs(this.getX()-a.getX())<100) && (abs(this.getY()-a.getY())<100)&&(a.getX()!=0))
                {
                    if(("skeleton".equals(a.getName())))
                    {
                        MoveToAction stop = new MoveToAction();
                        stop.setPosition(this.getX(), this.getY());
                        this.clearActions();
                        this.addAction(stop);
                    }
                }
            }
            arrowTimer = 1;
        }*/
        super.act(delta);
    }
   
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
      super.positionChanged();
    }
    
}