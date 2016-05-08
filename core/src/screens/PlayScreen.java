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
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    boolean win, lose, loseLabel;
    public int gold, archerRange, playerSpell, level, skelPath, footPath, necromancerCount;
    public float footHealth, footDamage, skelHealth, skelDamage, currentFootHealth, currentSkelHealth, archerSpeed,
                archerHealth, archerDamage, archerMoveTimer, footmanMoveTimer, playerRange, playerDamage, spreadTimer,
                slowSkelTimer, fastSkelTimer;
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
    TextButton nextLevel, topPath, bottomPath;
    Label won;
    NecromanticNuisance game;
    
    public PlayScreen(NecromanticNuisance gm)
    {
        game = gm;
        
        level = 1;
        necromancerCount = 1;
        
        viewport = new ScreenViewport();
        
        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();
        
        player = new PlayerCharacter(world, 100f, 400f);
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
        archerMoveTimer = 80;
        footmanMoveTimer = 80;
        castleSpawned = false;
        difficulty = 0;
        gold = 0;
        lives = 10;
        skelPath = 1;
        footPath = 1;
        win = false;
        lose = false;
        loseLabel = false;
        
        slowSkelTimer = 15;
        fastSkelTimer = 30;
        
        footHealth = 1000;
        footDamage = 300;
        
        archerHealth = 500;
        archerDamage = 250;
        archerSpeed = 0;
        archerRange = 100;
        
        skelHealth = 1100;
        skelDamage = 160+difficulty;
        
        playerRange = 200;
        playerDamage = 50;
        spreadTimer = 50;
        
        skeletonDeath = Gdx.audio.newSound(Gdx.files.internal("skeletondeath.wav"));
        footmanDeath = Gdx.audio.newSound(Gdx.files.internal("footmanDeath.wav"));
        dropSword = Gdx.audio.newSound(Gdx.files.internal("dropSword.wav"));
        arrowShot = Gdx.audio.newSound(Gdx.files.internal("arrow.wav"));
        spellSound = Gdx.audio.newSound(Gdx.files.internal("fireball.wav"));
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("curseofthescarab.mp3"));
        gameMusic.play();
        gameMusic.setLooping(true);
        
        stage = new Stage(viewport);
        
        stage.addActor(new Necromancer(20000, skelDamage, 940f, 375f, stage));
        
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
        TextButton button5 = new TextButton("  Train Archer 20g ", textButtonStyle);
        TextButton button6 = new TextButton("  Increase Archer Range 30g  ", textButtonStyle);
        TextButton button7 = new TextButton("  Select Fireball  ", textButtonStyle);
        TextButton button8 = new TextButton("  Select Icebolt  ", textButtonStyle);
        TextButton button9 = new TextButton("  Select Poison Wave  ", textButtonStyle);
        TextButton button10 = new TextButton("  Increase Archer Damage 30g  ", textButtonStyle);
        TextButton button11 = new TextButton("  Increase Archer FireRate 30g", textButtonStyle);
        TextButton button12 = new TextButton("  Increase Spell Damage 30g ", textButtonStyle);
        
        nextLevel = new TextButton("  Next Level  ", textButtonStyle);
        
        bottomPath = new TextButton("Send Trained Units Down", textButtonStyle);
        bottomPath.setPosition(5f, 320f);
        topPath = new TextButton("Send Trained Units Up", textButtonStyle);
        topPath.setPosition(5f, 620f);
        
        
        TextButton cheat = new TextButton(" GOLD  ", textButtonStyle);
        cheat.addListener(new ChangeListener() 
        {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) 
            {
                    gold += 100;
            }
        });
        cheat.setPosition(100f, 700f);
        stage.addActor(cheat);
        
        nextLevel.addListener(new ChangeListener() 
        {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) 
            {
                level++;
                necromancerCount = level;
                nextLevel.remove();
                won.remove();
                Array<Actor> stageActors = stage.getActors();
                int len = stageActors.size;
                for(int j=0; j<5; j++)
                {
                    for(int i=0; i<len; i++)
                    {
                        Actor a = stageActors.get(i);
                        if("skeleton".equals(a.getName()) || "footman".equals(a.getName())
                                            || "castle".equals(a.getName())
                                            || "archer".equals(a.getName())
                                            || "necromancer".equals(a.getName())
                                            || "dead".equals(a.getName()))
                        a.remove();
                        len = stageActors.size;
                    }
                }
                
                if(level == 2)
                {
                    skelHealth = 2100;
                    
                    map = mapLoader.load("level2.tmx");
                    renderer = new OrthogonalTiledMapRenderer(map);
                    
                    world = new World(new Vector2(0, 0), true);
                    player = new PlayerCharacter(world, 100f, 445f);
                    
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

                    
                    stage.addActor(new Castle(20000, 400, 35f, 450f, stage));
                    stage.addActor(new Necromancer(40000, skelDamage, 940f, 550f, stage));
                    stage.addActor(new Necromancer(40000, skelDamage, 940f, 340f, stage));
                    
                    stage.addActor(topPath);
                    stage.addActor(bottomPath);                   
                    
                }
            }
        });
        
        bottomPath.addListener(new ChangeListener() 
        {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) 
            {
                footPath = 2;
            }
        });
        
        topPath.addListener(new ChangeListener() 
        {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) 
            {
                footPath = 1;
            }
        });
        
        
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
                    recruitReset += 30;
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
                    if(level == 1)
                        stage.addActor(new Footman(10000+footHealth, 1000+footDamage, 100, 375, level, footPath));
                    if(level == 2 && footPath == 1)
                        stage.addActor(new Footman(10000+footHealth, 1000+footDamage, 100, 460, level, footPath));
                    if(level == 2 && footPath == 2)
                        stage.addActor(new Footman(10000+footHealth, 1000+footDamage, 100, 435, level, footPath));
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
                    if(level == 1)
                        stage.addActor(new Archer(archerHealth, archerDamage, 100f, 375f, 3f, stage, level, footPath));
                    if(level == 2 && footPath == 1)
                        stage.addActor(new Archer(archerHealth, archerDamage, 100f, 460f, 3f, stage, level, footPath));
                    if(level == 2 && footPath == 2)
                        stage.addActor(new Archer(archerHealth, archerDamage, 100f, 435f, 3f, stage, level, footPath));
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
        
        button9.addListener(new ChangeListener() 
        {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) 
            {
                playerSpell = 3;
            }
        });
        
        button10.addListener(new ChangeListener() 
        {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) 
            {
                if(gold >= 30)
                {
                    gold -= 30;
                    archerDamage += 30;
                }
            }
        });
        
        button11.addListener(new ChangeListener() 
        {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) 
            {
                if(gold >= 30)
                {
                    gold -= 30;
                    archerSpeed += 0.15;
                }
            }
        });
        
        button12.addListener(new ChangeListener() 
        {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) 
            {
                if(gold >= 30)
                {
                    gold -= 30;
                    playerDamage += 5;
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
        table.add(button7).size(315f, 40f).colspan(2);
        table.row();
        table.add(button2).size(315f, 40f).colspan(2);
        table.add(button6).size(315f, 40f).colspan(2);
        table.add(button8).size(315f, 40f).colspan(2);
        table.row();
        table.add(button3).size(315f, 40f).colspan(2);
        table.add(button10).size(315f, 40f).colspan(2);
        table.add(button9).size(315f, 40f).colspan(2);
        table.row();
        table.add(button4).size(315f, 40f).colspan(2);
        table.add(button11).size(315f, 40f).colspan(2);
        table.add(button12).size(315f, 40f).colspan(2);
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
        if(lose == false)
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
    }
    
    public void update(float delta)
    {
        // Level Management
        
        // Level 2
        if(level == 2)
        {
            slowSkelTimer -= 1*delta;
            System.out.println(String.valueOf(slowSkelTimer));
            fastSkelTimer -= 1*delta;
            if(slowSkelTimer < 0)
                slowSkelTimer = 0;
            if(fastSkelTimer < 0)
                fastSkelTimer = 0;
            if(slowSkelTimer == 0)
            {
                BasicSkel slowSkel = new BasicSkel(10000, 2000, 900f, 550f, level, 1, stage, this);
                slowSkel.velocity = 15;
                slowSkel.clearActions();
                slowSkel.texture = new Texture("slowSkel.png");
                slowSkel.sprite = new Sprite(slowSkel.texture);
                slowSkel.assignMovement(900f, 550f);
                stage.addActor(slowSkel);
                slowSkelTimer = 35;
            }
            
            if(fastSkelTimer == 0)
            {
                BasicSkel fastSkel = new BasicSkel(10000, 2000, 900f, 350f, level, 2, stage, this);
                fastSkel.velocity = 15;
                fastSkel.texture = new Texture("slowSkel.png");
                fastSkel.sprite = new Sprite(fastSkel.texture);
                fastSkel.clearActions();
                stage.addActor(fastSkel);
                fastSkel.assignMovement(900f, 350f);
                fastSkelTimer = 35;
            }
            
            
        }
        	
        
        
        
        red = new ColorAction();
        red.setEndColor(Color.RED);
        red.setDuration(1f);
        moveOff = new MoveToAction();
        moveOff.setPosition(-100, -100);
        kill = new SequenceAction(red, moveOff);
        
        player.update(delta);
        handleInput(delta);
        
        world.step(1/60f, 6, 2);
        
        recruitTimer -= 1*delta;
        spawnTimer -= 1*delta;
        if(spawnTimer < 0) 
            spawnTimer = 0;
        if(recruitTimer < 0)
            recruitTimer = 0;       
              
        if(spawnTimer == 0)
        {
            skelDamage = 160 + difficulty;
            if(win==false)
                if(level == 1)
                    stage.addActor(new BasicSkel(skelHealth, skelDamage, 900f, 375f, level, skelPath, stage, this));
                if(level == 2)
                {
                    stage.addActor(new BasicSkel(skelHealth, skelDamage, 900f, 550f, level, 1, stage, this));
                    stage.addActor(new BasicSkel(skelHealth, skelDamage, 900f, 350f, level, 2, stage, this));
                }
            if(difficulty < 110)
                spawnTimer = 200 - difficulty;
            else spawnTimer = 90;
            difficulty += 1;
//            if(skelPath == 1)
//                skelPath = 2;
//            else if(skelPath == 2)
//                skelPath = 1;
            
        }
        
        if(recruitTimer == 0)
        {
            if(!castleSpawned)
            {
                stage.addActor(new Castle(20000, 400, 35f, 370f, stage));
                castleSpawned = true;
            }    
            if(lose==false)
            {   
                if(level == 1)
                    stage.addActor(new Footman(footHealth, footDamage, 100f, 375f, level, 1));
                if(level == 2)
                {
                    stage.addActor(new Footman(footHealth, footDamage, 100f, 460f, level, 1));
                    stage.addActor(new Footman(footHealth, footDamage, 100f, 435f, level, 2));
                }
                recruitTimer = 400 - recruitReset;
            }
        }
        
            
        if(lose == true && loseLabel == false)
        {
            Label lost = new Label("YOU LOSE!", new Label.LabelStyle(font, BLACK));
            lost.setPosition(455f, 380f);
            Label playAgain = new Label("Play Again?", new Label.LabelStyle(font, BLACK));
            playAgain.setPosition(450f, 340f);
            stage.addActor(lost);
            stage.addActor(playAgain);
            loseLabel = true;
        }
            
        if(win == true)
        {
            won = new Label("YOU WIN!", new Label.LabelStyle(font, BLACK));
            won.setPosition(455f, 380f);
            nextLevel.setPosition(455f, 320f);
            stage.addActor(won);
            stage.addActor(nextLevel);
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
                        a.clearActions();
                        b.clearActions();
                        a.addAction(stopa);
                        b.addAction(stopb);
                        ((Footman) a).notmoving = true;
                        ((BasicSkel) b).notmoving = true;
                        
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
                            if(((BasicSkel) b).velocity == 15)
                            {
                                BasicSkel slowSkel = new BasicSkel(((BasicSkel) b).health, ((BasicSkel) b).damage, ((BasicSkel) b).getX(), ((BasicSkel) b).getY(), level, ((BasicSkel) b).path, stage, this);
                                slowSkel.velocity = 15;
                                slowSkel.clearActions();
                                slowSkel.texture = new Texture("slowSkel.png");
                                slowSkel.sprite = new Sprite(slowSkel.texture);
                                slowSkel.assignMovement(((BasicSkel) b).getX(), ((BasicSkel) b).getY());
                                stage.addActor(slowSkel);
                            }
                            else
                                stage.addActor(new BasicSkel(((BasicSkel) b).health, skelDamage, ((BasicSkel) b).getX(), ((BasicSkel) b).getY(), level, ((BasicSkel) b).path, stage, this));
                            ((BasicSkel) b).remove();
                        }
                        if(((BasicSkel) b).health <= 0)
                        {
                            b.setName("dead");
                            ((BasicSkel) b).health = 100000;
                            b.clearActions();
                            b.addAction(kill);
                            skeletonDeath.play(1.0f);
                            gold += 6;
                            stage.addActor(new Footman(((Footman) a).health, footDamage, ((Footman) a).getX(), ((Footman) a).getY(), level, ((Footman) a).path));
                            ((Footman) a).remove();

                        }
                    }
                    else if (("skeleton".equals(a.getName())) && ("footman".equals(b.getName())))
                    {
                        a.clearActions();
                        b.clearActions();
                        a.addAction(stopa);
                        b.addAction(stopb);
                        ((Footman) b).notmoving = true;
                        ((BasicSkel) a).notmoving = true;
                        ((BasicSkel) a).health -= ((Footman) b).damage * Gdx.graphics.getDeltaTime();
                        ((Footman) b).health -= ((BasicSkel) a).damage * Gdx.graphics.getDeltaTime();
                        if(((BasicSkel) a).health <= 0)
                        {
                            a.setName("dead");
                            ((BasicSkel) a).health = 100000;
                            a.clearActions();
                            a.addAction(kill);
                            skeletonDeath.play(1.0f);
                            gold += 6;
                            stage.addActor(new Footman(((Footman) b).health, footDamage, ((Footman) b).getX(), ((Footman) b).getY(), level, ((Footman) b).path));
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
                            if(((BasicSkel) a).velocity == 15)
                            {
                                BasicSkel slowSkel = new BasicSkel(((BasicSkel) a).health, ((BasicSkel) a).damage, ((BasicSkel) a).getX(), ((BasicSkel) a).getY(), level, ((BasicSkel) a).path, stage, this);
                                slowSkel.velocity = 15;
                                slowSkel.clearActions();
                                slowSkel.texture = new Texture("slowSkel.png");
                                slowSkel.sprite = new Sprite(slowSkel.texture);
                                slowSkel.assignMovement(((BasicSkel) a).getX(), ((BasicSkel) a).getY());
                                stage.addActor(slowSkel);
                            }
                            else
                                stage.addActor(new BasicSkel(((BasicSkel) a).health, skelDamage, ((BasicSkel) a).getX(), ((BasicSkel) a).getY(), level, ((BasicSkel) a).path, stage, this));
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
                            if(((BasicSkel) b).velocity == 15)
                            {
                                BasicSkel slowSkel = new BasicSkel(((BasicSkel) b).health, ((BasicSkel) b).damage, ((BasicSkel) b).getX(), ((BasicSkel) b).getY(), level, ((BasicSkel) b).path, stage, this);
                                slowSkel.velocity = 15;
                                slowSkel.clearActions();
                                slowSkel.assignMovement(((BasicSkel) b).getX(), ((BasicSkel) b).getY());
                                stage.addActor(slowSkel);
                            }
                            else
                                stage.addActor(new BasicSkel(((BasicSkel) b).health, skelDamage, ((BasicSkel) b).getX(), ((BasicSkel) b).getY(), level, ((BasicSkel) b).path, stage, this));
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
                            stage.addActor(new BasicSkel(((BasicSkel) a).health, skelDamage, ((BasicSkel) a).getX(), ((BasicSkel) a).getY(), level, ((BasicSkel) a).path, stage, this));
                            ((BasicSkel) a).remove();
                        }
                    }
                    
                    //
                    //Footman necromancer coliision
                    //
                    
                    if(("footman".equals(a.getName())) && ("necromancer".equals(b.getName())))
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
                            gold += 60;
                            stage.addActor(new Footman(((Footman) a).health, footDamage, ((Footman) a).getX(), ((Footman) a).getY(), level, ((Footman) a).path));
                            ((Footman) a).remove();
                            necromancerCount--;
                            if(necromancerCount == 0)
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
                            gold += 60;
                            stage.addActor(new Footman(((Footman) b).health, footDamage, ((Footman) b).getX(), ((Footman) b).getY(), level, ((Footman) b).path));
                            ((Footman) b).remove();
                            necromancerCount--;
                            if(necromancerCount == 0)
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
                    
                    if(("kkeleton".equals(a.getName())) && ("castle".equals(b.getName())))
                    {
                        a.addAction(stopa);
                        ((Castle) b).health -= ((BasicSkel) a).damage * Gdx.graphics.getDeltaTime();
                        if(((Castle) b).health <= 0)
                        {
                            b.setName("dead");
                            ((Castle) b).health = 100000;
                            b.clearActions();
                            b.addAction(kill);
                            stage.addActor(new BasicSkel(((BasicSkel) a).health, skelDamage, ((BasicSkel) a).getX(), ((BasicSkel) a).getY(), level, ((BasicSkel) a).path, stage, this));
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
                            stage.addActor(new BasicSkel(((BasicSkel) b).health, skelDamage, ((BasicSkel) b).getX(), ((BasicSkel) b).getY(), level, ((BasicSkel) b).path, stage, this));
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
                            ((Archer) a).arrowTimer = 3 - archerSpeed;
                            ((BasicSkel) b).health -= archerDamage;
                            if(((BasicSkel) b).health <= 0)
                            {
                                b.setName("dead");
                                ((BasicSkel) b).health = 100000;
                                b.clearActions();
                                b.addAction(kill);
                                skeletonDeath.play(1.0f);
                                gold += 6;
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
                            ((Archer) b).arrowTimer = 3 - archerSpeed;
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
                                gold += 6;
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
                            ((Archer) a).arrowTimer = 3 - archerSpeed;
                            ((Necromancer) b).health -= archerDamage;
                            if(((Necromancer) b).health <= 0)
                            {
                                b.setName("dead");
                                ((Necromancer) b).health = 100000;
                                b.clearActions();
                                b.addAction(kill);
                                skeletonDeath.play(1.0f);
                                gold += 60;
                                win = true;
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
                            ((Archer) b).arrowTimer = 3 - archerSpeed;
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
                                gold += 60;
                                win = true;
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
                            ((Castle) a).arrowTimer = 3 - archerSpeed;
                            ((BasicSkel) b).health -= 200 + archerDamage;
                            if(((BasicSkel) b).health <= 0)
                            {
                                b.setName("dead");
                                ((BasicSkel) b).health = 100000;
                                b.clearActions();
                                b.addAction(kill);
                                skeletonDeath.play(1.0f);
                                gold += 6;
                            }
                        }
                        
                    }
                    if (("castle".equals(b.getName())) && ("skeleton".equals(a.getName())))
                    {
                        if(((Castle) b).arrowTimer <= 0)
                        {
                            ((Castle) b).arrowTimer = 3 - archerSpeed;
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
                                gold += 6;
                            }
                        }
                    }
                
                
                }
                
                //
                //Necromancer combat
                //
                
                
                if((abs(a.getX()-b.getX())<200) && (abs(a.getY()-b.getY())<200)&&(a.getX()!=0))
                {
                    if (("necromancer".equals(a.getName())) && ("footman".equals(b.getName())))
                    {
                        if(((Necromancer) a).attackTimer <= 0)
                        {
                            stage.addActor(new NecromancerAttack(a.getX(), a.getY(), b.getX(), b.getY()));
                            spellSound.play(1.0f);
                            ((Necromancer) a).attackTimer = 2;
                            ((Footman) b).health -= 300;
                            if(((Footman) b).health <= 0)
                            {
                                b.setName("dead");
                                ((Footman) b).health = 100000;
                                b.clearActions();
                                b.addAction(kill);
                                footmanDeath.play(1.0f);
                                dropSword.play(1.0f);
                            }
                        }
                        
                    }
                    if (("necromancer".equals(b.getName())) && ("footman".equals(a.getName())))
                    {
                        if(((Necromancer) b).attackTimer <= 0)
                        {
                            ((Necromancer) b).attackTimer = 2;
                            stage.addActor(new NecromancerAttack(b.getX(), b.getY(), a.getX(), a.getY()));
                            spellSound.play(1.0f);
                            ((Footman) a).health -= 300;
                            if(((Footman) a).health <= 0)
                            {
                                a.setName("dead");
                                ((Footman) a).health = 100000;
                                a.clearActions();
                                a.addAction(kill);
                                footmanDeath.play();
                                dropSword.play();
                            }
                        }
                    }
                    if (("necromancer".equals(a.getName())) && ("archer".equals(b.getName())))
                    {
                        if(((Necromancer) a).attackTimer <= 0)
                        {
                            stage.addActor(new NecromancerAttack(a.getX(), a.getY(), b.getX(), b.getY()));
                            spellSound.play(1.0f);
                            ((Necromancer) a).attackTimer = 2;
                            ((Archer) b).health -= 300;
                            if(((Archer) b).health <= 0)
                            {
                                b.setName("dead");
                                ((Archer) b).health = 100000;
                                b.clearActions();
                                b.addAction(kill);
                                footmanDeath.play(1.0f);
                                dropSword.play(1.0f);
                            }
                        }
                        
                    }
                    if (("necromancer".equals(b.getName())) && ("archer".equals(a.getName())))
                    {
                        if(((Necromancer) b).attackTimer <= 0)
                        {
                            ((Necromancer) b).attackTimer = 2;
                            stage.addActor(new NecromancerAttack(b.getX(), b.getY(), a.getX(), a.getY()));
                            spellSound.play(1.0f);
                            ((Archer) a).health -= 300;
                            if(((Archer) a).health <= 0)
                            {
                                a.setName("dead");
                                ((Archer) a).health = 100000;
                                a.clearActions();
                                a.addAction(kill);
                                footmanDeath.play();
                                dropSword.play();
                            }
                        }
                    }
                }
                
                spreadTimer -= delta;
                 if((abs(a.getX()-b.getX())<130) && (abs(a.getY()-b.getY())<130)&&(a.getX()!=0))
                 {
                     if(spreadTimer <= 0)
                     {
                        if ("skeleton".equals(b.getName()) && "skeleton".equals(a.getName()))
                        {
                            if(((BasicSkel) a).poisoned)
                                ((BasicSkel) b).poisoned = true;
                            if(((BasicSkel) b).poisoned)
                                 ((BasicSkel) a).poisoned = true;
                            spreadTimer = 0.1f;
                        }
                     }
                 }
                
                archerMoveTimer -= Gdx.graphics.getDeltaTime();
                if("archer".equals(a.getName()) && ((Archer) a).notmoving && (archerMoveTimer <= 0))
                {
//                    System.out.println("Archer not moving, new archer spawned");
                    stage.addActor(new Archer(((Archer) a).health, ((Archer) a).damage, ((Archer) a).getX(), ((Archer) a).getY(), ((Archer)a).arrowTimer, stage, ((Archer) a).level, ((Archer) a).path));
                    ((Archer) a).remove();
                    len = stageActors.size;
                    archerMoveTimer = 80;
                }
                
                footmanMoveTimer -= Gdx.graphics.getDeltaTime();
                if("footman".equals(a.getName()) && ((Footman) a).notmoving && footmanMoveTimer <= 0)
                {
                    stage.addActor(new Footman(((Footman) a).health, ((Footman) a).damage, ((Footman) a).getX(), ((Footman) a).getY(), level, ((Footman) a).path));
                    ((Footman) a).remove();
                    len = stageActors.size;
                    footmanMoveTimer = 500;
                }
                
                if("skeleton".equals(a.getName()) && ((BasicSkel) a).notmoving && footmanMoveTimer <= 0)
                {
                    stage.addActor(new BasicSkel(((BasicSkel) a).health, ((BasicSkel) a).damage, ((BasicSkel) a).getX(), ((BasicSkel) a).getY(), level, ((BasicSkel) a).path, stage, this));
                    ((BasicSkel) a).remove();
                    len = stageActors.size;
                    footmanMoveTimer = 500;
                }
                
                if(("skeleton".equals(a.getName()) || "necromancer".equals(a.getName())) && abs(player.getX()-a.getX())<25 && abs(player.getY()-a.getY())<25)
                {
                    player.health -= skelDamage * delta;
                    if(player.health <= 0)
                        player.dead = true;
                }
                if("castle".equals(a.getName()) && (abs(player.getX()-a.getX())<30 && abs(player.getY()-a.getY())<30))
                {
                    if(player.health < 1000)
                        player.health += 100*delta;
                }
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
