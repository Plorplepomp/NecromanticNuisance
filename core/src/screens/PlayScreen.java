/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import actors.Archer;
import actors.BasicSkel;
import actors.Footman;
import actors.Necromancer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import static com.badlogic.gdx.graphics.Color.BLACK;
import static com.badlogic.gdx.graphics.Color.WHITE;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.scottdennis.necromanticnuisance.NecromanticNuisance;
import static java.lang.Math.abs;
import actors.Castle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 *
 * @author Scott
 */
public class PlayScreen implements Screen
{
    public Stage stage;
    ScreenViewport viewport;
    TiledMap map;
    private TmxMapLoader mapLoader;
    private OrthogonalTiledMapRenderer renderer;
    int spawnTimer, recruitTimer, difficulty, recruitReset, lives;
    boolean win, lose;
    public int gold;
    float footHealth, footDamage, skelHealth, skelDamage, currentFootHealth, currentSkelHealth;
    float archerHealth, archerDamage;
    public Sound skeletonDeath, footmanDeath, dropSword;
    public boolean soundPlayed, castleSpawned;
    BitmapFont font;
    public Label goldCount;
    public ColorAction red;
    public MoveToAction moveOff;
    public SequenceAction kill;
    public DelayAction stop;
    
    
    public PlayScreen(NecromanticNuisance game)
    {
        viewport = new ScreenViewport();
        
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1temp.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        
        recruitTimer = 200;
        recruitReset = 0;
        spawnTimer = 100;
        castleSpawned = false;
        difficulty = 0;
        gold = 0;
        lives = 10;
        win = false;
        lose = false;
        
        footHealth = 1000;
        footDamage = 300;
        archerHealth = 500;
        archerDamage = 300;
        skelHealth = 1000;
        skelDamage = 160+difficulty;
        
        skeletonDeath = Gdx.audio.newSound(Gdx.files.internal("skeletondeath.wav"));
        footmanDeath = Gdx.audio.newSound(Gdx.files.internal("footmanDeath.wav"));
        dropSword = Gdx.audio.newSound(Gdx.files.internal("dropSword.wav"));
        
        stage = new Stage(viewport);
        
        stage.addActor(new Necromancer(20000, skelDamage, stage));
        
        red = new ColorAction();
        red.setEndColor(Color.RED);
        red.setDuration(1f);
        moveOff = new MoveToAction();
        moveOff.setPosition(-100, -100);
        kill = new SequenceAction(red, moveOff);
        stop = new DelayAction(1f);
        
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        font.getData().setScale(0.9f, 0.9f);
        
        Skin skin = new Skin();
        TextureAtlas buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons.pack"));
        skin.addRegions(buttonAtlas);
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("unpressed");
        textButtonStyle.down = skin.getDrawable("pressed");
        TextButton button1 = new TextButton("  Increase Soldier Damage 30g  ", textButtonStyle);
        TextButton button2 = new TextButton("  Increase Soldier Health 30g  ", textButtonStyle);
        TextButton button3 = new TextButton("  Increase Soldier Training Speed 50g  ", textButtonStyle);
        TextButton button4 = new TextButton("  Train Juggernaut 50g ", textButtonStyle);
        TextButton button5 = new TextButton("  Train Archer 10g ", textButtonStyle);
        
        button1.addListener(new ChangeListener() 
        {
            @Override
            public void changed (ChangeEvent event, Actor actor) 
            {
                if(gold >= 30)
                {
                    gold -= 30;
                    footDamage += 40;
                }
                //System.out.println(footDamage);
            }
        });
        
        button2.addListener(new ChangeListener() 
        {
            @Override
            public void changed (ChangeEvent event, Actor actor) 
            {
                if(gold >= 30)
                {
                    gold -= 30;
                    footHealth += 200;
                }
                //System.out.println(footHealth);
            }
        });

        button3.addListener(new ChangeListener() 
        {
            @Override
            public void changed (ChangeEvent event, Actor actor) 
            {
                if(gold >= 50 && recruitReset < 360)
                {
                    gold -= 50;
                    recruitReset += 20;
                }
                //System.out.println(recruitReset);
            }
        });

        button4.addListener(new ChangeListener() 
        {
            @Override
            public void changed (ChangeEvent event, Actor actor) 
            {
                if(gold >= 50)
                {
                    gold -= 50;
                    stage.addActor(new Footman(10000+footHealth, 1000+footDamage, 100, 375));
                }
            }
        });        
        
        button5.addListener(new ChangeListener() 
        {
            @Override
            public void changed (ChangeEvent event, Actor actor) 
            {
                if(gold >= 10)
                {
                    gold -= 10;
                    stage.addActor(new Archer(archerHealth, archerDamage, 100f, 375f, stage));
                }
            }
        });
        
        Label goldLabel = new Label("Gold: ", new Label.LabelStyle(font, BLACK));
        Label goldCount =  new Label(String.format("%03d", gold), new Label.LabelStyle(font, BLACK));
        goldCount.setName("goldCouunt");
        
        Table table = new Table();
        table.add(goldLabel).width(50).right();
        table.add(goldCount).width(50).left();
        table.row();
        table.add(button1).size(315f, 40f).colspan(2);
        table.add(button5).size(315f, 40f).colspan(2);
        table.row();
        table.add(button2).size(315f, 40f).colspan(2);
        table.row();
        table.add(button3).size(315f, 40f).colspan(2);
        table.row();
        table.add(button4).size(315f, 40f).colspan(2);
        table.setPosition(310f, 100f);
        stage.addActor(table);
        table.setName("table");

    
        Label title = new Label("Stop the Necromancer!", new Label.LabelStyle(font, BLACK));
        title.setPosition(400f, 710f);
        
        stage.addActor(title);
        goldCount.setName("goldCount");
           
    }
    
    

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) 
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        renderer.setView((OrthographicCamera) stage.getCamera());
        renderer.render();
        update(delta);
        stage.act();
        stage.draw();
    }
    
    public void update(float delta)
    {
        red = new ColorAction();
        red.setEndColor(Color.RED);
        red.setDuration(1f);
        moveOff = new MoveToAction();
        moveOff.setPosition(-100, -100);
        kill = new SequenceAction(red, moveOff);
        
        
        recruitTimer -= 1*delta;;
        spawnTimer -= 1*delta;
        if(spawnTimer < 0) 
            spawnTimer = 0;
        if(recruitTimer < 0)
            recruitTimer = 0;       
              
        if(spawnTimer == 0)
        {
            skelDamage = 160 + difficulty;
            if(win==false)
                stage.addActor(new BasicSkel(skelHealth, skelDamage, 900f, 375f, stage));
            if(difficulty < 110)
                spawnTimer = 200 - difficulty;
            else spawnTimer = 90;
            difficulty++;
        }
        
        if(recruitTimer == 0)
        {
            if(!castleSpawned)
            {
                stage.addActor(new Castle(20000, 400, stage));
                castleSpawned = true;
            }    
            if(lose==false)
            {
                stage.addActor(new Footman(footHealth, footDamage, 100f, 375f));
                recruitTimer = 400 - recruitReset;
            }
        }
        Array<Actor> stageActors = stage.getActors();
        int len = stageActors.size;
        for(int i=0; i<len; i++)
        {
            
            len = stageActors.size;
            Actor a = stageActors.get(i);
            
            if("table".equals(a.getName()))
            {
                Label goldCountUpdate = ((Table) a).findActor("goldCount");
                goldCountUpdate.setText(String.format("%03d", gold));                
                len = stageActors.size;
            }
            
            if((a.getX() == 195f) && (a.getY() == 800f))
            {
                a.remove();
                lives--;
                if(lives==9)
                {
                    lose = true;
                    Label lost = new Label("YOU LOSE!", new Label.LabelStyle(font, BLACK));
                    lost.setPosition(455f, 380f);
                    stage.addActor(lost);
                    len = stageActors.size;
                }
                len = stageActors.size;
            }
            
            if(("footman".equals(a.getName()))&&(a.getX()==950f)&&(a.getY()==610f))
            {
                win = true;
                Label won = new Label("YOU WIN!", new Label.LabelStyle(font, BLACK));
                won.setPosition(455f, 380f);
                stage.addActor(won);
                len = stageActors.size;
            }
            
            if(("skeleton".equals(a.getName()))&&(win==true))
            {
                a.remove();
                len = stageActors.size;
            }
            
            for(int j = i+1; j<len; j++)
            {
                len = stageActors.size;
                Actor b = stageActors.get(j);
                if((abs(a.getX()-b.getX())<30) && (abs(a.getY()-b.getY())<30)&&(a.getX()!=0))
                {
                    MoveToAction stopa = new MoveToAction();
                    stopa.setPosition(a.getX(), a.getY());
                    MoveToAction stopb = new MoveToAction();
                    stopb.setPosition(b.getX(), b.getY());
                    
                    if(("footman".equals(a.getName())) && ("skeleton".equals(b.getName())))
                    {
                        a.addAction(stopa);
                        b.addAction(stopb);
//                         a.addAction(stop);
//                        b.addAction(stop);
//                        ((Footman) a).inCombat = true;
                        ((Footman) a).health -= ((BasicSkel) b).damage * Gdx.graphics.getDeltaTime();
                        ((BasicSkel) b).health -= ((Footman) a).damage * Gdx.graphics.getDeltaTime();
                        if(((Footman) a).health <= 0)
                        {
                            a.setName("dead");
                            ((Footman) a).health = 10000;
                            a.clearActions();
                            a.addAction(kill);
                            footmanDeath.play(1.0f);
                            dropSword.play(1.0f);
                            stage.addActor(new BasicSkel(((BasicSkel) b).health, skelDamage, ((BasicSkel) b).getX(), ((BasicSkel) b).getY(), stage));
                            ((BasicSkel) b).remove();
                        }
                        if(((BasicSkel) b).health <= 0)
                        {
                            b.setName("dead");
                            ((BasicSkel) b).health = 10000;
                            b.clearActions();
                            b.addAction(kill);
                            skeletonDeath.play(1.0f);
                            gold += 5;
                            stage.addActor(new Footman(((Footman) a).health, footDamage, ((Footman) a).getX(), ((Footman) a).getY()));
                            ((Footman) a).remove();

                        }
                    }
                    else if (("skeleton".equals(a.getName())) && ("footman".equals(b.getName())))
                    {
                        a.addAction(stopa);
                        b.addAction(stopb);
//                        a.addAction(stop);
//                        b.addAction(stop);
                        ((BasicSkel) a).health -= ((Footman) b).damage * Gdx.graphics.getDeltaTime();
                        ((Footman) b).health -= ((BasicSkel) a).damage * Gdx.graphics.getDeltaTime();
                        if(((BasicSkel) a).health <= 0)
                        {
                            a.setName("dead");
                            ((BasicSkel) a).health = 10000;
                            a.clearActions();
                            a.addAction(kill);
                            skeletonDeath.play(1.0f);
                            gold += 5;
                            stage.addActor(new Footman(((Footman) b).health, footDamage, ((Footman) b).getX(), ((Footman) b).getY()));
                            ((Footman) b).remove();
                        }
                        if(((Footman) b).health <= 0)
                        {
                            b.setName("dead");
                            ((Footman) b).health = 10000;
                            b.clearActions();
                            b.addAction(kill);
                            footmanDeath.play(1.0f);
                            dropSword.play(1.0f);
                            stage.addActor(new BasicSkel(((BasicSkel) a).health, skelDamage, ((BasicSkel) a).getX(), ((BasicSkel) a).getY(), stage));
                            ((BasicSkel) a).remove();
                        }
                        
                    }
               
                        
                }
                if((abs(a.getX()-b.getX())<100) && (abs(a.getY()-b.getY())<100)&&(a.getX()!=0))
                {
                    MoveToAction stopa = new MoveToAction();
                    stopa.setPosition(a.getX(), a.getY());
                    if (("archer".equals(a.getName())) && ("skeleton".equals(b.getName())))
                    {
                        a.addAction(stopa);
                        stage.addActor(new Arrow(a.getX(), a.getY(), b.getX(), b.getY()));
                    }
                }
                
                
                /*
                if((abs(a.getX()-b.getX())<50) &&
                  (abs(a.getY()-b.getY())<50)  &&
                  (("footman".equals(a.getName()))||("skeleton".equals(a.getName()))) &&
                  (("footman".equals(b.getName()))||("skeleton".equals(b.getName()))) )
                {
                    //System.out.println("Footman Skeleton Collision!");
                    MoveToAction stopa = new MoveToAction();
                    stopa.setPosition(a.getX(), a.getY());
                    MoveToAction stopb = new MoveToAction();
                    stopb.setPosition(b.getX(), b.getY());
                    //a.addAction(stopa);
                    //b.addAction(stopb);
                    //DelayAction delay = new DelayAction(1f);
                    //a.addAction(delay);
                    //b.addAction(delay);
                    if(("footman".equals(a.getName())) && ("skeleton".equals(b.getName())))
                    {
                        SequenceAction stopDelaya = new SequenceAction(stopa, new DelayAction(5f));
                        SequenceAction stopDelayb = new SequenceAction(stopb, new DelayAction(5f));
                        a.addAction(stopDelaya);
                        b.addAction(stopDelayb);
                        //a.addAction(new DelayAction(3f));
                        //b.addAction(new DelayAction(3f));
                        currentFootHealth = a.getZIndex();
                        currentFootHealth -= skelDamage * Gdx.graphics.getDeltaTime();
                        if(currentFootHealth <= 0)
                        {
                            a.setName("dead");
                            //a.remove();
                            ColorAction red = new ColorAction();
                            ColorAction clear = new ColorAction();
                            red.setEndColor(Color.RED);
                            red.setDuration(1f);
                            clear.setEndColor(Color.CLEAR);
                            MoveToAction moveOff = new MoveToAction();
                            moveOff.setPosition(-100, -100);
                            SequenceAction kill = new SequenceAction(red, moveOff);
                            a.clearActions();
                            a.addAction(kill);
                            footmanDeath.play(1.0f);
                            dropSword.play(1.0f);
                            //System.out.println("a Footman Removed");
                        }
                        else
                            a.setZIndex((int)currentFootHealth);
                        
                        currentSkelHealth = b.getZIndex();
                        //System.out.println("currentSkelHealth pre: " + currentSkelHealth);
                        currentSkelHealth -= footDamage * Gdx.graphics.getDeltaTime();
                        //System.out.println("currentSkelHealth post: " + currentSkelHealth);
                        if(currentSkelHealth <= 0)
                        {
                            b.setName("dead");
                            ColorAction red = new ColorAction();
                            ColorAction clear = new ColorAction();
                            red.setEndColor(Color.RED);
                            red.setDuration(1f);
                            clear.setEndColor(Color.CLEAR);
                            MoveToAction moveOff = new MoveToAction();
                            moveOff.setPosition(-100, -100);
                            SequenceAction kill = new SequenceAction(red, moveOff);
                            b.clearActions();
                            b.addAction(kill);
                            //b.remove();
                            skeletonDeath.play(1.0f);
                            gold += 5;
                            //System.out.println("b Skeleton Removed");
                        }
                        else
                            b.setZIndex((int)currentSkelHealth);
                        
                    }
                    else if (("skeleton".equals(a.getName())) && ("footman".equals(b.getName())))
                    {
                        a.addAction(stopa);
                        b.addAction(stopb);
                        a.addAction(new DelayAction(3f));
                        b.addAction(new DelayAction(3f));
                        currentSkelHealth = a.getZIndex();
                        currentSkelHealth -= footDamage * Gdx.graphics.getDeltaTime();
                        //System.out.println("currentSkelHealth: " + currentSkelHealth);
                        if(currentSkelHealth <= 0)
                        {
                            //a.remove();
                            a.setName("dead");
                            ColorAction red = new ColorAction();
                            ColorAction clear = new ColorAction();
                            red.setEndColor(Color.RED);
                            red.setDuration(1f);
                            clear.setEndColor(Color.CLEAR);
                            MoveToAction moveOff = new MoveToAction();
                            moveOff.setPosition(-100, -100);
                            SequenceAction kill = new SequenceAction(red, moveOff);
                            a.clearActions();
                            a.addAction(kill);
                            skeletonDeath.play(1.0f);
                            gold += 5;
                            //System.out.println("a Skeleton Removed");
                        }
                        else
                            a.setZIndex((int)currentSkelHealth);
                        
                        currentFootHealth = b.getZIndex();
                        currentFootHealth -= skelDamage * Gdx.graphics.getDeltaTime();
                        //System.out.println("currentFootHealth: " + currentFootHealth);
                        if(currentFootHealth <= 0)
                        {
                            //b.remove();
                            b.setName("dead");
                            ColorAction red = new ColorAction();
                            ColorAction clear = new ColorAction();
                            red.setEndColor(Color.RED);
                            red.setDuration(1f);
                            clear.setEndColor(Color.CLEAR);
                            MoveToAction moveOff = new MoveToAction();
                            moveOff.setPosition(-100, -100);
                            SequenceAction kill = new SequenceAction(red, moveOff);
                            b.clearActions();
                            b.addAction(kill);
                            footmanDeath.play(1.0f);
                            dropSword.play(1.0f);
                            //System.out.println("b Footman Removed");
                        }
                        else
                            b.setZIndex((int)currentFootHealth);
                        */
                        //a.remove();
                        /*
                        b.addAction(new DelayAction(2f));
                        ColorAction red = new ColorAction();
                        ColorAction clear = new ColorAction();
                        red.setEndColor(Color.RED);
                        red.setDuration(1f);
                        clear.setEndColor(Color.CLEAR);
                        MoveToAction moveOff = new MoveToAction();
                        moveOff.setPosition(-100f, -100f);
                        SequenceAction kill = new SequenceAction(red, moveOff);
                        a.clearActions();
                        a.addAction(kill);
                        */
                        //b.remove();
                        
//                    }
                
//                }
            }
        }
    }   

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
    
}
