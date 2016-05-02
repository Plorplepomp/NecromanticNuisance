/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import actors.Archer;
import actors.Arrow;
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
import actors.NecromancerAttack;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.scottdennis.necromanticnuisance.PlayerCharacter;

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
    public int gold, archerRange, playerSpell;
    public float footHealth, footDamage, skelHealth, skelDamage, currentFootHealth, currentSkelHealth;
    public float archerHealth, archerDamage, archerMoveTimer, playerRange, playerDamage;
    static public Sound skeletonDeath;
    public Sound footmanDeath, dropSword, arrowShot, spellSound;
    public Music gameMusic;
    public boolean soundPlayed, castleSpawned;
    BitmapFont font;
    public Label goldCount;
    public ColorAction red;
    public MoveToAction moveOff;
    public SequenceAction kill;
    public DelayAction stop;
    public World world;
    private Box2DDebugRenderer b2dr;
    public PlayerCharacter player;
    NecromanticNuisance game;
    
    public PlayScreen(NecromanticNuisance gm)
    {
        game = gm;
        
        viewport = new ScreenViewport();
        
        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();
        
        player = new PlayerCharacter(world);
        playerSpell = 1;
        
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1temp.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() + rect.getWidth()/2, rect.getY() + rect.getHeight()/2);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth()/2, rect.getHeight()/2);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
        
        
        recruitTimer = 15;
        recruitReset = 0;
        spawnTimer = 100;
        archerMoveTimer = 10;
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
        archerRange = 100;
        skelHealth = 1100;
        skelDamage = 160+difficulty;
        
        playerRange = 200;
        playerDamage = 50;
        
        skeletonDeath = Gdx.audio.newSound(Gdx.files.internal("skeletondeath.wav"));
        footmanDeath = Gdx.audio.newSound(Gdx.files.internal("footmanDeath.wav"));
        dropSword = Gdx.audio.newSound(Gdx.files.internal("dropSword.wav"));
        arrowShot = Gdx.audio.newSound(Gdx.files.internal("arrow.wav"));
        spellSound = Gdx.audio.newSound(Gdx.files.internal("fireball.wav"));
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("curseofthescarab.mp3"));
        gameMusic.play();
        
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
        TextButton button6 = new TextButton("  Increase Archer Range 30g  ", textButtonStyle);
        TextButton button7 = new TextButton("  Select Fireball  ", textButtonStyle);
        TextButton button8 = new TextButton("  Select Icebolt  ", textButtonStyle);
        
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
                    stage.addActor(new Archer(archerHealth, archerDamage, 100f, 375f, 3f, stage));
                }
            }
        });
        
        button6.addListener(new ChangeListener() 
        {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) 
            {
                if(gold >= 30)
                {
                    gold -= 30;
                    archerRange += 20;
                }
            }
        });
        
        button7.addListener(new ChangeListener() 
        {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) 
            {
                playerSpell = 1;
            }
        });
        
        button8.addListener(new ChangeListener() 
        {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) 
            {
                playerSpell = 2;
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
        table.add(button7).size(315f, 40f).colspan(2);
        table.row();
        table.add(button2).size(315f, 40f).colspan(2);
        table.add(button6).size(315f, 40f).colspan(2);
        table.add(button8).size(315f, 40f).colspan(2);
        table.row();
        table.add(button3).size(315f, 40f).colspan(2);
        table.row();
        table.add(button4).size(315f, 40f).colspan(2);
        table.setPosition(510f, 100f);
        stage.addActor(table);
        table.setName("table");

    
        Label title = new Label("Stop the Necromancer!", new Label.LabelStyle(font, BLACK));
        title.setPosition(400f, 710f);
        
        stage.addActor(title);
        goldCount.setName("goldCount");
           
    }
    
    public void addGold(int addition)
    {
        gold += addition;
    }
    

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) 
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        renderer.setView((OrthographicCamera) stage.getCamera());
//        b2dr.render(world, stage.getCamera().combined);
        renderer.render();
        update(delta);
                
        
        stage.act();
        stage.draw();
        
        stage.getBatch().begin();
        player.draw(stage.getBatch());
        stage.getBatch().end();
        
//        b2dr.render(world, stage.getCamera().combined);
       
//        game.batch.setProjectionMatrix(stage.getCamera().combined);
//        stage.getBatch().begin();
//        game.batch.begin();
//        player.draw(game.batch);
//        stage.getBatch().end();
//        game.batch.end();
        
    }
    
    public void handleInput(float delta)
    {
        if(Gdx.input.isKeyPressed(Input.Keys.W))
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            player.b2body.applyLinearImpulse(new Vector2(-4f, 0), player.b2body.getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Input.Keys.S))
            player.b2body.applyLinearImpulse(new Vector2(0, -4f), player.b2body.getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Input.Keys.D))
            player.b2body.applyLinearImpulse(new Vector2(4f, 0), player.b2body.getWorldCenter(), true);
//        if(Gdx.input.isKeyJustPressed(Input.Buttons.LEFT))
//            stage.
    }
    
    public void update(float delta)
    {
        red = new ColorAction();
        red.setEndColor(Color.RED);
        red.setDuration(1f);
        moveOff = new MoveToAction();
        moveOff.setPosition(-100, -100);
        kill = new SequenceAction(red, moveOff);
        
        player.update(delta);
        handleInput(delta);
        
        world.step(1/60f, 6, 2);
        
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
                stage.addActor(new BasicSkel(skelHealth, skelDamage, 900f, 375f, stage, this));
            if(difficulty < 110)
                spawnTimer = 200 - difficulty;
            else spawnTimer = 90;
            difficulty += 2;
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
        
            
        if(lose == true)
        {
            Label lost = new Label("YOU LOSE!", new Label.LabelStyle(font, BLACK));
            lost.setPosition(455f, 380f);
            Label playAgain = new Label("Play Again?", new Label.LabelStyle(font, BLACK));
            playAgain.setPosition(450f, 340f);
            stage.addActor(lost);
            stage.addActor(playAgain);
            lose = false;
        }
            
        if(win == true)
        {
            Label won = new Label("YOU WIN!", new Label.LabelStyle(font, BLACK));
            won.setPosition(455f, 380f);                
            stage.addActor(won);
            win = false;
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
            
            
            if(("skeleton".equals(a.getName()))&&(win==true))
            {
                a.remove();
                len = stageActors.size;
            }
            
            for(int j = i+1; j<len; j++)
            {
                len = stageActors.size;
                Actor b = stageActors.get(j);
                if((abs(a.getX()-b.getX())<30) 
                        && (abs(a.getY()-b.getY())<30)
                        &&(a.getX()>20 && a.getY()>20)
                        &&(b.getX()>20 && b.getY()>20))
                {
                    MoveToAction stopa = new MoveToAction();
                    stopa.setPosition(a.getX(), a.getY());
                    MoveToAction stopb = new MoveToAction();
                    stopb.setPosition(b.getX(), b.getY());
                    
                    
                    //
                    // Footman Skeleton Collision
                    //
                    
                    if(("footman".equals(a.getName())) && ("skeleton".equals(b.getName())))
                    {
                        a.addAction(stopa);
                        b.addAction(stopb);
                        ((Footman) a).health -= ((BasicSkel) b).damage * Gdx.graphics.getDeltaTime();
                        ((BasicSkel) b).health -= ((Footman) a).damage * Gdx.graphics.getDeltaTime();
                        if(((Footman) a).health <= 0)
                        {
                            a.setName("dead");
                            ((Footman) a).health = 100000;
                            a.clearActions();
                            a.addAction(kill);
                            footmanDeath.play(0.7f);
                            dropSword.play(0.7f);
                            stage.addActor(new BasicSkel(((BasicSkel) b).health, skelDamage, ((BasicSkel) b).getX(), ((BasicSkel) b).getY(), stage, this));
                            ((BasicSkel) b).remove();
                        }
                        if(((BasicSkel) b).health <= 0)
                        {
                            b.setName("dead");
                            ((BasicSkel) b).health = 100000;
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
                            ((BasicSkel) a).health = 100000;
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
                            ((Footman) b).health = 100000;
                            b.clearActions();
                            b.addAction(kill);
                            footmanDeath.play(0.7f);
                            dropSword.play(0.7f);
                            stage.addActor(new BasicSkel(((BasicSkel) a).health, skelDamage, ((BasicSkel) a).getX(), ((BasicSkel) a).getY(), stage, this));
                            ((BasicSkel) a).remove();
                        }
                        
                    }
                    
                    //
                    //Skeleton Archer Collision
                    //
                    
                    if(("archer".equals(a.getName())) && ("skeleton".equals(b.getName())))
                    {
                        a.addAction(stopa);
                        ((Archer) a).notmoving = true;
                        b.addAction(stopb);
                        ((Archer) a).health -= ((BasicSkel) b).damage * Gdx.graphics.getDeltaTime();
                        if(((Archer) a).health <= 0)
                        {
                            ((Archer) a).notmoving = false;
                            a.setName("dead");
                            ((Archer) a).health = 100000;
                            a.clearActions();
                            a.addAction(kill);
                            footmanDeath.play(0.7f);
                            stage.addActor(new BasicSkel(((BasicSkel) b).health, skelDamage, ((BasicSkel) b).getX(), ((BasicSkel) b).getY(), stage, this));
                            ((BasicSkel) b).remove();
                        }
                    }
                    else if(("archer".equals(b.getName())) && ("skeleton".equals(a.getName())))
                    {
                        b.addAction(stopb);
                        ((Archer) b).notmoving = true;
                        a.addAction(stopa);
                        ((Archer) b).health -= ((BasicSkel) a).damage * Gdx.graphics.getDeltaTime();
                        if(((Archer) b).health <= 0)
                        {
                            ((Archer) b).notmoving = false;
                            b.setName("dead");
                            ((Archer) b).health = 100000;
                            b.clearActions();
                            b.addAction(kill);
                            footmanDeath.play(0.7f);
                            stage.addActor(new BasicSkel(((BasicSkel) a).health, skelDamage, ((BasicSkel) a).getX(), ((BasicSkel) a).getY(), stage, this));
                            ((BasicSkel) a).remove();
                        }
                    }
                    
                    //
                    //Footman necromancer coliision
                    //
                    
                    if(("footman".equals(a.getName())) && ("Necromancer".equals(b.getName())))
                    {
                        a.addAction(stopa);
                        ((Footman) a).health -= ((Necromancer) b).damage * Gdx.graphics.getDeltaTime();
                        ((Necromancer) b).health -= ((Footman) a).damage * Gdx.graphics.getDeltaTime();
                        if(((Footman) a).health <= 0)
                        {
                            a.setName("dead");
                            ((Footman) a).health = 100000;
                            a.clearActions();
                            a.addAction(kill);
                            footmanDeath.play(0.7f);
                            dropSword.play(0.7f);
                        }
                        if(((Necromancer) b).health <= 0)
                        {
                            b.setName("dead");
                            ((Necromancer) b).health = 100000;
                            b.clearActions();
                            b.addAction(kill);
                            skeletonDeath.play(1.0f);
                            gold += 50;
                            stage.addActor(new Footman(((Footman) a).health, footDamage, ((Footman) a).getX(), ((Footman) a).getY()));
                            ((Footman) a).remove();
                            win = true;
                        }
                    }
                    else if (("necromancer".equals(a.getName())) && ("footman".equals(b.getName())))
                    {
                        b.addAction(stopb);
                        ((Necromancer) a).health -= ((Footman) b).damage * Gdx.graphics.getDeltaTime();
                        ((Footman) b).health -= ((Necromancer) a).damage * Gdx.graphics.getDeltaTime();
                        if(((Necromancer) a).health <= 0)
                        {
                            a.setName("dead");
                            ((Necromancer) a).health = 100000;
                            a.clearActions();
                            a.addAction(kill);
                            skeletonDeath.play(1.0f);
                            gold += 50;
                            stage.addActor(new Footman(((Footman) b).health, footDamage, ((Footman) b).getX(), ((Footman) b).getY()));
                            ((Footman) b).remove();
                            win = true;
                        }
                        if(((Footman) b).health <= 0)
                        {
                            b.setName("dead");
                            ((Footman) b).health = 100000;
                            b.clearActions();
                            b.addAction(kill);
                            footmanDeath.play(0.7f);
                            dropSword.play(0.7f);
                        }
                        
                    }
                    
                    //
                    //Skeleton Castle Collision
                    //
                    
                    if(("Skeleton".equals(a.getName())) && ("castle".equals(b.getName())))
                    {
                        a.addAction(stopa);
                        ((Castle) b).health -= ((BasicSkel) a).damage * Gdx.graphics.getDeltaTime();
                        if(((Castle) b).health <= 0)
                        {
                            b.setName("dead");
                            ((Castle) b).health = 100000;
                            b.clearActions();
                            b.addAction(kill);
                            stage.addActor(new BasicSkel(((BasicSkel) a).health, skelDamage, ((BasicSkel) a).getX(), ((BasicSkel) a).getY(), stage, this));
                            ((BasicSkel) a).remove();
                            lose = true;
                        }
                    }
                    else if (("castle".equals(a.getName())) && ("skeleton".equals(b.getName())))
                    {
                        b.addAction(stopb);
                        ((Castle) a).health -= ((BasicSkel) b).damage * Gdx.graphics.getDeltaTime();
                        if(((Castle) a).health <= 0)
                        {
                            a.setName("dead");
                            ((Castle) a).health = 100000;
                            a.clearActions();
                            a.addAction(kill);
                            stage.addActor(new BasicSkel(((BasicSkel) b).health, skelDamage, ((BasicSkel) b).getX(), ((BasicSkel) b).getY(), stage, this));
                            ((BasicSkel) b).remove();
                            lose = true;
                        }
                        
                    }
                }
                
                //
                // Archer combat
                //
                
                if((abs(a.getX()-b.getX())<archerRange) && (abs(a.getY()-b.getY())<archerRange)&&(a.getX()!=0))
                {
                    MoveToAction stopa = new MoveToAction();
                    stopa.setPosition(a.getX(), a.getY());
                    MoveToAction stopb = new MoveToAction();
                    stopb.setPosition(b.getX(), b.getY());
                    
                    //
                    // Archer skeleton combat
                    //
                    
                    if (("archer".equals(a.getName())) && ("skeleton".equals(b.getName())))
                    {
                        a.clearActions();
                        a.addAction(stopa);
                        ((Archer) a).notmoving = true;
                        if(((Archer) a).arrowTimer <= 0)
                        {
                            stage.addActor(new Arrow(a.getX(), a.getY(), b.getX(), b.getY()));
                            arrowShot.play(1.0f);
                            ((Archer) a).arrowTimer = 3;
                            ((BasicSkel) b).health -= archerDamage;
                            if(((BasicSkel) b).health <= 0)
                            {
                                b.setName("dead");
                                ((BasicSkel) b).health = 100000;
                                b.clearActions();
                                b.addAction(kill);
                                skeletonDeath.play(1.0f);
                                gold += 5;
                            }
                        }
                        
                    }
                    if (("archer".equals(b.getName())) && ("skeleton".equals(a.getName())))
                    {
                        b.clearActions();
                        b.addAction(stopb);
                        ((Archer) b).notmoving = true;
                        if(((Archer) b).arrowTimer <= 0)
                        {
                            ((Archer) b).arrowTimer = 3;
                            stage.addActor(new Arrow(b.getX(), b.getY(), a.getX(), a.getY()));
                            arrowShot.play(1.0f);
                            ((BasicSkel) a).health -= archerDamage;
                            if(((BasicSkel) a).health <= 0)
                            {
                                a.setName("dead");
                                ((BasicSkel) a).health = 100000;
                                a.clearActions();
                                a.addAction(kill);
                                skeletonDeath.play(1.0f);
                                gold += 5;
                            }
                        }
                    }
                    
                    //
                    //Archer Necromancer combat
                    //
                    
                    if (("archer".equals(a.getName())) && ("necromancer".equals(b.getName())))
                    {
                        a.clearActions();
                        a.addAction(stopa);
                        ((Archer) a).notmoving = true;
                        if(((Archer) a).arrowTimer <= 0)
                        {
                            stage.addActor(new Arrow(a.getX(), a.getY(), b.getX(), b.getY()));
                            arrowShot.play(1.0f);
                            ((Archer) a).arrowTimer = 3;
                            ((Necromancer) b).health -= archerDamage;
                            if(((Necromancer) b).health <= 0)
                            {
                                b.setName("dead");
                                ((Necromancer) b).health = 100000;
                                b.clearActions();
                                b.addAction(kill);
                                skeletonDeath.play(1.0f);
                                gold += 50;
                            }
                        }
                        
                    }
                    if (("archer".equals(b.getName())) && ("necromancer".equals(a.getName())))
                    {
                        b.clearActions();
                        b.addAction(stopb);
                        ((Archer) b).notmoving = true;
                        if(((Archer) b).arrowTimer <= 0)
                        {
                            ((Archer) b).arrowTimer = 3;
                            stage.addActor(new Arrow(b.getX(), b.getY(), a.getX(), a.getY()));
                            arrowShot.play(1.0f);
                            ((Necromancer) a).health -= archerDamage;
                            if(((Necromancer) a).health <= 0)
                            {
                                a.setName("dead");
                                ((Necromancer) a).health = 100000;
                                a.clearActions();
                                a.addAction(kill);
                                skeletonDeath.play(1.0f);
                                gold += 50;
                            }
                        }
                    }
                
                    //
                    // Castle skeleton combat
                    //
                    
                    if (("castle".equals(a.getName())) && ("skeleton".equals(b.getName())))
                    {
                        if(((Castle) a).arrowTimer <= 0)
                        {
                            stage.addActor(new Arrow(a.getX(), a.getY(), b.getX(), b.getY()));
                            arrowShot.play(1.0f);
                            ((Castle) a).arrowTimer = 3;
                            ((BasicSkel) b).health -= 200 + archerDamage;
                            if(((BasicSkel) b).health <= 0)
                            {
                                b.setName("dead");
                                ((BasicSkel) b).health = 100000;
                                b.clearActions();
                                b.addAction(kill);
                                skeletonDeath.play(1.0f);
                                gold += 5;
                            }
                        }
                        
                    }
                    if (("castle".equals(b.getName())) && ("skeleton".equals(a.getName())))
                    {
                        if(((Castle) b).arrowTimer <= 0)
                        {
                            ((Castle) b).arrowTimer = 3;
                            stage.addActor(new Arrow(b.getX(), b.getY(), a.getX(), a.getY()));
                            arrowShot.play(1.0f);
                            ((BasicSkel) a).health -= 200 + archerDamage;
                            if(((BasicSkel) a).health <= 0)
                            {
                                a.setName("dead");
                                ((BasicSkel) a).health = 100000;
                                a.clearActions();
                                a.addAction(kill);
                                skeletonDeath.play(1.0f);
                                gold += 5;
                            }
                        }
                    }
                
                
                }
                if((abs(a.getX()-b.getX())<200) && (abs(a.getY()-b.getY())<200)&&(a.getX()!=0))
                {
                    if (("necromancer".equals(a.getName())) && ("footman".equals(b.getName())))
                    {
                        if(((Necromancer) a).attackTimer <= 0)
                        {
                            stage.addActor(new NecromancerAttack(a.getX(), a.getY(), b.getX(), b.getY()));
                            arrowShot.play(1.0f);
                            ((Castle) a).arrowTimer = 3;
                            ((BasicSkel) b).health -= 200 + archerDamage;
                            if(((BasicSkel) b).health <= 0)
                            {
                                b.setName("dead");
                                ((BasicSkel) b).health = 100000;
                                b.clearActions();
                                b.addAction(kill);
                                skeletonDeath.play(1.0f);
                                gold += 5;
                            }
                        }
                        
                    }
                    if (("castle".equals(b.getName())) && ("skeleton".equals(a.getName())))
                    {
                        if(((Castle) b).arrowTimer <= 0)
                        {
                            ((Castle) b).arrowTimer = 3;
                            stage.addActor(new Arrow(b.getX(), b.getY(), a.getX(), a.getY()));
                            arrowShot.play(1.0f);
                            ((BasicSkel) a).health -= 200 + archerDamage;
                            if(((BasicSkel) a).health <= 0)
                            {
                                a.setName("dead");
                                ((BasicSkel) a).health = 100000;
                                a.clearActions();
                                a.addAction(kill);
                                skeletonDeath.play(1.0f);
                                gold += 5;
                            }
                        }
                    }
                }
                
                
                archerMoveTimer -= Gdx.graphics.getDeltaTime();
                if("archer".equals(a.getName()) && ((Archer) a).notmoving && (archerMoveTimer <= 0))
                {
                    System.out.println("Archer not moving, new archer spawned");
                    stage.addActor(new Archer(((Archer) a).health, ((Archer) a).damage, ((Archer) a).getX(), ((Archer) a).getY(), ((Archer)a).arrowTimer, stage));
                    ((Archer) a).remove();
                    len = stageActors.size;
                    archerMoveTimer = 50;
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
    public void dispose() 
    {
        stage.dispose();
        world.dispose();
    }
    
}
