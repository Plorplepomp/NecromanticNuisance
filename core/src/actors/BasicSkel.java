/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.scottdennis.necromanticnuisance.NecromanticNuisance;
import screens.PlayScreen;
import static java.lang.Math.abs;
import actors.Fireball;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

/**
 *
 * @author Scott
 */
public class BasicSkel extends Actor
{
    Texture texture;
    MoveToAction ms, ma1, ma2, ma3, ma4, ma5, ma6, ma7, ma8, ma9, ma10, init;
    public Sprite sprite, emptyHealthBar, fullHealthBar;
    public float health, slowTimer, poisonTimer;
    public float damage;
    public PlayScreen screen;
    float velocity;
    public SequenceAction kill;
    public Stage stage;
    public ParticleEffect fireEffect, iceEffect, poisonEffect;
    public Sound fireballSound;
    boolean slowed, reset;
    public boolean poisoned, notmoving;
    //public Rectangle body;
    
    public BasicSkel(float hlth, float dmg, float x, float y, Stage stg, PlayScreen scrn)
    {
        
        this.setName("skeleton");
        stage = stg;
        screen = scrn;
        health = hlth;
        damage = dmg;
        slowTimer = -1;
        slowed = false;
        reset = false;
        poisoned = false;
        notmoving = false;
        if(damage < 220)
            texture = new Texture("skelsword1.png");
        if(damage < 240 && damage >= 220)
            texture = new Texture("skelsword2.png");
        if(damage >= 240)
            texture = new Texture("skelsword3.png");
        sprite = new Sprite(texture);
        sprite.setScale(0.65f);
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        
        fireEffect = new ParticleEffect();
        fireEffect.load(Gdx.files.internal("fire"), Gdx.files.internal(""));
        fireEffect.getEmitters().first().setPosition(getX(), getY());
        fireballSound = Gdx.audio.newSound(Gdx.files.internal("fireball.wav"));
        
        iceEffect = new ParticleEffect();
        iceEffect.load(Gdx.files.internal("ice"), Gdx.files.internal(""));
        iceEffect.getEmitters().first().setPosition(getX(), getY());
        
        poisonEffect = new ParticleEffect();
        poisonEffect.load(Gdx.files.internal("poison"), Gdx.files.internal(""));
        poisonEffect.getEmitters().first().setPosition(getX(), getY());
        
        
        emptyHealthBar = new Sprite(new Texture("emptyBar.png"));
        fullHealthBar = new Sprite(new Texture("fullBar.png"));
        
        ColorAction red = new ColorAction();
        red.setEndColor(Color.RED);
        red.setDuration(1f);
        MoveToAction moveOff = new MoveToAction();
        moveOff.setPosition(-100, -100);
        kill = new SequenceAction(red, moveOff);
        
        
        this.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y) 
                {
                    if(health != 100000 && abs(screen.player.getX()-getX())<screen.playerRange && abs(screen.player.getY()-getY())<screen.playerRange)
                    {
                        if(screen.playerSpell == 1)
                        {
                            health -= screen.playerDamage;
                            stage.addActor(new Fireball(screen.player.getX(), screen.player.getY(), getX()+16, getY()+16));
                            fireballSound.play(0.8f);
                            fireEffect.start();
                        }
                        if(screen.playerSpell == 2)
                        {
                            health -= screen.playerDamage*0.75;
                            stage.addActor(new IceBolt(screen.player.getX(), screen.player.getY(), getX()+16, getY()+16));
                            fireballSound.play(0.8f);
                            iceEffect.start();
                            slowTimer = 3;
                            if(health >= 0)
                                slowed = true;
                        }
                        if(screen.playerSpell == 3)
                        {
                            poisoned = true;
                            stage.addActor(new IceBolt(screen.player.getX(), screen.player.getY(), getX()+16, getY()+16));
                            fireballSound.play(0.8f);
                            poisonEffect.start();
                        }
                        
                    
                    }
                    if(health <= 0)
                    { 
                        poisoned = false;
                        setName("dead");
                        health = 100000;
                        clearActions();
                        addAction(kill);
                        screen.skeletonDeath.play(1.0f);
                        screen.addGold(5);
                    }
                }
            });
        
        velocity = 70;
        //Rectangle body = new Rectangle(getX(), getY(), getWidth(), getHeight());
        //ScaleByAction sba = new ScaleByAction();
        //sba.setAmount(0.25f);
        
        assignMovement(x, y);
        
        /*
        init = new MoveToAction();
        init.setPosition(x, y);
        
        ms = new MoveToAction();
        ms.setPosition(900f,375f);
        
        ma1 = new MoveToAction();
        ma1.setPosition(680f,375f);
        ma1.setDuration((900-680)/velocity);
        
        ma2 = new MoveToAction();
        ma2.setPosition(680f, 575f);
        ma2.setDuration((575-375)/velocity);
        
        ma3 = new MoveToAction();
        ma3.setPosition(505f, 575f);
        ma3.setDuration((680-505)/velocity);
        
        ma4 = new MoveToAction();
        ma4.setPosition(505f, 185f);
        ma4.setDuration((575-185)/velocity);
        
        ma5 = new MoveToAction();
        ma5.setPosition(310f, 185f);
        ma5.setDuration((505-310)/velocity);
        
        ma6 = new MoveToAction();
        ma6.setPosition(310f, 375f);
        ma6.setDuration((375-185)/velocity);
        
        ma7 = new MoveToAction();
        ma7.setPosition(-40f, 375f);
        ma7.setDuration((310+40)/velocity);
        

        SequenceAction sa1 = new SequenceAction(ms, ma1, ma2, ma3, ma4);
        SequenceAction sa2 = new SequenceAction(ma5, ma6, ma7);
        SequenceAction sa3 = new SequenceAction(init, ma1, ma2, ma3, ma4);
        SequenceAction csa = new SequenceAction(sa1, sa2);
        
        if((x==900)&&(y==375))
            this.addAction(csa);
        else if((y==375)&&(x>680))
        {
            ma1.setDuration((x-680)/velocity);
            sa3 = new SequenceAction(init, ma1, ma2, ma3, ma4);
            SequenceAction csa1 = new SequenceAction(sa3, sa2);
            this.addAction(csa1);
        }
        else if((x==680)&&(y!=575))
        {
            ma2.setDuration((575-y)/velocity);
            SequenceAction sa4 = new SequenceAction(init, ma2, ma3, ma4);
            SequenceAction csa4 = new SequenceAction(sa4, sa2);
            this.addAction(csa4);
        }
        else if((y==575)&&(x!=505))
        {
            ma3.setDuration((x-505)/velocity);
            SequenceAction sa5 = new SequenceAction(ma3, ma4, ma5, ma6, ma7);
            SequenceAction csa5 = new SequenceAction(init, sa5);
            this.addAction(csa5);
        }
        else if((x==505)&&(y!=185))
        {
            ma4.setDuration((y-185)/velocity);
            SequenceAction sa6 = new SequenceAction(init, ma4, ma5, ma6, ma7);
            this.addAction(sa6);
        }
        else if((y==185)&&(x!=310))
        {
            ma5.setDuration((x-310)/velocity);
            SequenceAction sa7 = new SequenceAction(init, ma5, ma6, ma7);
            this.addAction(sa7);
        }
        else if((x==310)&&(y!=375))
        {    
            ma6.setDuration((375-y)/velocity);
            SequenceAction sa8 = new SequenceAction(init, ma6, ma7);
            this.addAction(sa8);
        }
        else if((y==375)&&(x<600))
        {
            ma7.setDuration((x+40)/velocity);
            SequenceAction sa9 = new SequenceAction(init, ma7);
            this.addAction(sa9);
        }
        
        /*      
        int len = stage.size;
        for(i=0; i<len; i++){
        Actor a = stageActors.get(i);
        if(a.getName().equals("myactor")){s
        //a is your Actor!
        break;
    */
    }

    public void assignMovement(float x, float y)
    {
         init = new MoveToAction();
        init.setPosition(x, y);
        
        ms = new MoveToAction();
        ms.setPosition(900f,375f);
        
        ma1 = new MoveToAction();
        ma1.setPosition(680f,375f);
        ma1.setDuration((900-680)/velocity);
        
        ma2 = new MoveToAction();
        ma2.setPosition(680f, 575f);
        ma2.setDuration((575-375)/velocity);
        
        ma3 = new MoveToAction();
        ma3.setPosition(505f, 575f);
        ma3.setDuration((680-505)/velocity);
        
        ma4 = new MoveToAction();
        ma4.setPosition(505f, 185f);
        ma4.setDuration((575-185)/velocity);
        
        ma5 = new MoveToAction();
        ma5.setPosition(310f, 185f);
        ma5.setDuration((505-310)/velocity);
        
        ma6 = new MoveToAction();
        ma6.setPosition(310f, 375f);
        ma6.setDuration((375-185)/velocity);
        
        ma7 = new MoveToAction();
        ma7.setPosition(-100f, 375f);
        ma7.setDuration((310+40)/velocity);
        

        SequenceAction sa1 = new SequenceAction(ms, ma1, ma2, ma3, ma4);
        SequenceAction sa2 = new SequenceAction(ma5, ma6, ma7);
        SequenceAction sa3 = new SequenceAction(init, ma1, ma2, ma3, ma4);
        SequenceAction csa = new SequenceAction(sa1, sa2);
        
        if((x==900)&&(y==375))
            this.addAction(csa);
        else if((y==375)&&(x>680))
        {
            ma1.setDuration((x-680)/velocity);
            sa3 = new SequenceAction(init, ma1, ma2, ma3, ma4);
            SequenceAction csa1 = new SequenceAction(sa3, sa2);
            this.addAction(csa1);
        }
        else if((x==680)&&(y!=575))
        {
            ma2.setDuration((575-y)/velocity);
            SequenceAction sa4 = new SequenceAction(init, ma2, ma3, ma4);
            SequenceAction csa4 = new SequenceAction(sa4, sa2);
            this.addAction(csa4);
        }
        else if((y==575)&&(x!=505))
        {
            ma3.setDuration((x-505)/velocity);
            SequenceAction sa5 = new SequenceAction(ma3, ma4, ma5, ma6, ma7);
            SequenceAction csa5 = new SequenceAction(init, sa5);
            this.addAction(csa5);
        }
        else if((x==505)&&(y!=185))
        {
            ma4.setDuration((y-185)/velocity);
            SequenceAction sa6 = new SequenceAction(init, ma4, ma5, ma6, ma7);
            this.addAction(sa6);
        }
        else if((y==185)&&(x!=310))
        {
            ma5.setDuration((x-310)/velocity);
            SequenceAction sa7 = new SequenceAction(init, ma5, ma6, ma7);
            this.addAction(sa7);
        }
        else if((x==310)&&(y!=375))
        {    
            ma6.setDuration((375-y)/velocity);
            SequenceAction sa8 = new SequenceAction(init, ma6, ma7);
            this.addAction(sa8);
        }
        else if((y==375)&&(x<600))
        {
            ma7.setDuration((x+100)/velocity);
            SequenceAction sa9 = new SequenceAction(init, ma7);
            this.addAction(sa9);
        }
    }
    
    
    
    
   @Override
    public void act(float delta)
    {
        if(health > 0 && health != 10000)
            poisonTimer -= delta;
        if(poisoned && poisonTimer <= 0)
        {
            health -= screen.playerDamage * 0.2;
            poisonTimer = 0.3f;
            poisonEffect.start();
        }
        if(slowTimer>0)
        {
            slowTimer -= delta;
            if(slowTimer < 0)
                slowTimer = 0;
        }
        if(slowTimer == 0 && health >= 0)
            reset = true;            
        if(slowed)
        {
            velocity = 35;
            clearActions();
            assignMovement(getX(), getY());
            slowed = false;
        }
        if(reset)
        {
            velocity = 70;
            clearActions();
            assignMovement(getX(), getY());
            reset = false;
            slowTimer = -1;
        }
        
        fireEffect.getEmitters().first().setPosition(getX()+45, getY()+32);
        iceEffect.getEmitters().first().setPosition(getX()+45, getY()+32);
        poisonEffect.getEmitters().first().setPosition(getX()+45, getY()+32);
        
        emptyHealthBar.setPosition(this.getX()+32, this.getY()+65);
        emptyHealthBar.setScale(1f, 0.65f);
        fullHealthBar.setPosition(this.getX()+32, this.getY()+66);
        fullHealthBar.setOrigin(0f,0f);
        if(health < NecromanticNuisance.playScreen.skelHealth)
            fullHealthBar.setScale(health/NecromanticNuisance.playScreen.skelHealth, 0.7f);
        if((this.getX()==-100)&&(this.getY()==-100))
            this.remove();
        super.act(delta);
    }

    
    public void draw(Batch batch, float parentAlpha)
    {
        fireEffect.update(Gdx.graphics.getDeltaTime());
        fireEffect.draw(batch);
        iceEffect.update(Gdx.graphics.getDeltaTime());
        iceEffect.draw(batch);
        poisonEffect.update(Gdx.graphics.getDeltaTime());
        poisonEffect.draw(batch);
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