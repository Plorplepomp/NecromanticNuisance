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
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.scottdennis.necromanticnuisance.NecromanticNuisance;

/**
 *
 * @author Scott
 */
public class Footman extends Actor
{
    Texture texture;
    MoveToAction ms, ma1, ma2, ma3, ma4, ma5, ma6, ma7, ma8, ma9, ma10;
    MoveToAction ma11, ma12, ma13, ma14, ma15, ma16, ma17, init;
    Sprite sprite, emptyHealthBar, fullHealthBar;
    public boolean inCombat, notmoving;
    public float health;
    public float damage;
    float velocity;
    int level;
    public int path;
    
    public Footman(float hlth, float dmg,  float x, float y, int lvl, int pth)
    {
        this.setName("footman");
        damage = dmg;
        health = hlth;
        level = lvl;
        path = pth;
        notmoving = false;
        if(damage < 400)
            texture = new Texture("footman0.png");
        if(damage < 450 && damage >= 400)
            texture = new Texture("footman1.png");
        if(damage < 600 && damage >= 450)
            texture = new Texture("footman2.png");
        if(damage < 1000 && damage >= 600)
            texture = new Texture("champion.png");
        if(damage >= 1000)
            texture = new Texture("footman3.png");
        sprite = new Sprite(texture);
        sprite.setScale(0.65f);
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        
        emptyHealthBar = new Sprite(new Texture("emptyBar.png"));
        fullHealthBar = new Sprite(new Texture("fullBar.png"));
        
        setTouchable(Touchable.disabled);
        
       velocity = 80;
       inCombat = false;
       
       assignMovement(x, y);
       
       /*
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
            SequenceAction sa9 = new SequenceAction(init, ma7);
            this.addAction(sa9);
        }
        
        */    
        
        
    }
    
    @Override
    public void act(float delta)
    {
        emptyHealthBar.setPosition(this.getX()+32, this.getY()+65);
        emptyHealthBar.setScale(1f, 0.65f);
        fullHealthBar.setPosition(this.getX()+32, this.getY()+66);
        fullHealthBar.setOrigin(0f,0f);
        if(health<=NecromanticNuisance.playScreen.footHealth)
            fullHealthBar.setScale(health/NecromanticNuisance.playScreen.footHealth, 0.7f);
        if((this.getX()==-100)&&(this.getY()==-100))
            this.remove();
        super.act(delta);
    }
    
    public void assignMovement(float x, float y)
    {
        init = new MoveToAction();
        init.setPosition(x, y);
        
        ma1 = new MoveToAction();
        ma2 = new MoveToAction();
        ma3 = new MoveToAction();
        ma4 = new MoveToAction();
        ma5 = new MoveToAction();
        ma6 = new MoveToAction();
        ma7 = new MoveToAction();
        
       if(level == 1)
       {
            ms = new MoveToAction();
            ms.setPosition(100f,375f);
        
            ma1.setPosition(315f,375f);
            ma1.setDuration(((315-100 )/velocity));

            ma2.setPosition(315f, 180f);
            ma2.setDuration((375-180)/velocity);
        
            ma3.setPosition(510f, 180f);
            ma3.setDuration((510-315)/velocity);
        
            ma4.setPosition(510f, 570f);
            ma4.setDuration((570-180)/velocity);
        
            ma5.setPosition(680f, 570f);
            ma5.setDuration((680-510)/velocity);
        
            ma6.setPosition(680f, 375f);
            ma6.setDuration((570-375)/velocity);
        
            ma7.setPosition(1000f, 375f);
            ma7.setDuration((1000-680)/velocity);
            
            SequenceAction sa1 = new SequenceAction(ms, ma1, ma2, ma3, ma4);
            SequenceAction sa2 = new SequenceAction(ma5, ma6, ma7);
            SequenceAction csa1 = new SequenceAction(sa1, sa2);
            
            if((x==100)&&(y==375))
            this.addAction(csa1);
            else if((y==375)&&(x<315))
            {
                ma1.setDuration((315-x)/velocity);
                SequenceAction sa3 = new SequenceAction(ma1, ma2, ma3, ma4);
                SequenceAction csa2 = new SequenceAction(init, sa3, sa2);
                this.addAction(csa2);
            }
            else if((x==315)&&(y!=180))
            {
                ma2.setDuration((y-180)/velocity);
                SequenceAction sa4 = new SequenceAction(ma2, ma3, ma4);
                SequenceAction csa3 = new SequenceAction(init, sa4, sa2);
                this.addAction(csa3);
            }
            else if((y==180)&&(x!=510))
            {
                ma3.setDuration((510-x)/velocity);
                SequenceAction sa5 = new SequenceAction(ma3, ma4, ma5, ma6, ma7);
                SequenceAction csa4 = new SequenceAction(init, sa5);
                this.addAction(csa4);
            }
            else if((x==510)&&(y!=570))
            {
                ma4.setDuration((570-y)/velocity);
                SequenceAction sa6 = new SequenceAction(init, ma4, ma5, ma6, ma7);
                this.addAction(sa6);
            }
            else if((y==570)&&(x!=680))
            {
                ma5.setDuration((680-x)/velocity);
                SequenceAction sa7 = new SequenceAction(init, ma5, ma6, ma7);
                this.addAction(sa7);
            }
            else if((x==680)&&(y!=375))
            {    
                ma6.setDuration((y-375)/velocity);
                SequenceAction sa8 = new SequenceAction(init, ma6, ma7);
                this.addAction(sa8);
            }
            else if((y==375)&&(x>315))
            {
                ma7.setDuration((1000-x)/velocity);
                SequenceAction sa9 = new SequenceAction(init, ma7);
                this.addAction(sa9);
            }
        }
       
        if(level == 2)
        {
            if(path == 1)
            {
                ma1.setPosition(225f, 460f);
                ma1.setDuration((225-100)/velocity);
                
                ma2.setPosition(225f, 590f);
                ma2.setDuration((590-460)/velocity);
                
                ma3.setPosition(350f, 590f);
                ma3.setDuration((350-225)/velocity);
                
                ma4.setPosition(350f, 640f);
                ma4.setDuration((640-590)/velocity);
                
                ma5.setPosition(550f, 640f);
                ma5.setDuration((550-350)/velocity);
                
                ma6.setPosition(550f, 550f);
                ma6.setDuration((640-550)/velocity);                   

                ma7.setPosition(1100f, 550f);
                ma7.setDuration((1100-550)/velocity);
                
                SequenceAction sa1, sa2;
                
                if(x == 100 && y == 460)
                {
                    sa1 = new SequenceAction(init, ma1, ma2, ma3, ma4);
                    sa2 = new SequenceAction(sa1, ma5, ma6, ma7);
                    addAction(sa2);
                }
                else if(x > 100 && y == 460)
                {
                    ma1.setDuration((225-x)/velocity);
                    sa1 = new SequenceAction(init, ma1, ma2, ma3, ma4);
                    sa2 = new SequenceAction(sa1, ma5, ma6, ma7);
                    addAction(sa2);
                }
                else if(x == 225 && y < 590)
                {
                    ma2.setDuration((590-y)/velocity);
                    sa1 = new SequenceAction(init, ma2, ma3, ma4);
                    sa2 = new SequenceAction(sa1, ma5, ma6, ma7);
                    addAction(sa2);
                }
                else if(x < 350 && y == 590)
                {
                    ma3.setDuration((350-x)/velocity);
                    sa1 = new SequenceAction(init, ma3, ma4);
                    sa2 = new SequenceAction(sa1, ma5, ma6, ma7);
                    addAction(sa2);
                }
                else if(x == 350 && y < 640)
                {
                    ma4.setDuration((640-y)/velocity);
                    sa1 = new SequenceAction(init, ma4);
                    sa2 = new SequenceAction(sa1, ma5, ma6, ma7);
                    addAction(sa2);
                }
                else if(x < 550 && y == 640)
                {
                    ma5.setDuration((550-x)/velocity);
                    sa2 = new SequenceAction(init, ma5, ma6, ma7);
                    addAction(sa2);
                }
                else if(x == 550 && y > 550)
                {
                    ma6.setDuration((y-550)/velocity);
                    sa2 = new SequenceAction(init, ma6, ma7);
                    addAction(sa2);
                }
                else if(x < 1100 && y == 550)
                {
                    ma7.setDuration((1100-x)/velocity);
                    sa2 = new SequenceAction(init, ma7);
                    addAction(sa2);
                }
            }
            else if(path == 2)
            {
                
                ma1.setPosition(225f, 435f);
                ma1.setDuration((225-100)/velocity);
                
                ma2.setPosition(225f, 310f);
                ma2.setDuration((435-310)/velocity);
                
                ma3.setPosition(350f, 310f);
                ma3.setDuration((350-225)/velocity);
                
                ma4.setPosition(350f, 250f);
                ma4.setDuration((310-250)/velocity);
                
                ma5.setPosition(550f, 250f);
                ma5.setDuration((550-350)/velocity);
                
                ma6.setPosition(550f, 350f);
                ma6.setDuration((350-250)/velocity);
                
                ma7.setPosition(1100f, 350f);
                ma7.setDuration((1100-550)/velocity);
                
                SequenceAction sa1, sa2;
                
                if(x == 100 && y == 435)
                {
                    sa1 = new SequenceAction(init, ma1, ma2, ma3, ma4);
                    sa2 = new SequenceAction(sa1, ma5, ma6, ma7);
                    addAction(sa2);
                }
                else if(x < 225 && y == 435)
                {
                    ma1.setDuration((225-x)/velocity);
                    sa1 = new SequenceAction(init, ma1, ma2, ma3, ma4);
                    sa2 = new SequenceAction(sa1, ma5, ma6, ma7);
                    addAction(sa2);
                }
                else if(x == 225 && y > 310)
                {
                    ma2.setDuration((y-310)/velocity);
                    sa1 = new SequenceAction(init, ma2, ma3, ma4);
                    sa2 = new SequenceAction(sa1, ma5, ma6, ma7);
                    addAction(sa2);
                }
                else if(x < 350 && y == 310)
                {
                    ma3.setDuration((350-x)/velocity);
                    sa1 = new SequenceAction(init, ma3, ma4);
                    sa2 = new SequenceAction(sa1, ma5, ma6, ma7);
                    addAction(sa2);
                }
                else if(x == 350 && y > 250)
                {
                    ma4.setDuration((y-250)/velocity);
                    sa1 = new SequenceAction(init, ma4);
                    sa2 = new SequenceAction(sa1, ma5, ma6, ma7);
                    addAction(sa2);
                }
                else if(x < 550 && y == 250)
                {
                    ma5.setDuration((550-x)/velocity);
                    sa2 = new SequenceAction(init, ma5, ma6, ma7);
                    addAction(sa2);
                }
                else if(x == 550 && y < 350)
                {
                    ma6.setDuration((350-y)/velocity);
                    sa2 = new SequenceAction(init, ma6, ma7);
                    addAction(sa2);
                }
                else if(x < 1100 && y == 350)
                {
                    ma7.setDuration((1100-x)/velocity);
                    sa2 = new SequenceAction(init, ma7);
                    addAction(sa2);
                }
            }
        }
    }
   
    public void draw(Batch batch, float parentAlpha)
    {
        
        sprite.setColor(this.getColor());
        batch.setColor(this.getColor());
        sprite.draw(batch);
        emptyHealthBar.draw(batch);
        if(health<99999)
            fullHealthBar.draw(batch);
    }
    
    
    @Override
    protected void positionChanged()
    {
      sprite.setPosition(getX(), getY());
      super.positionChanged();
    }
    
}

