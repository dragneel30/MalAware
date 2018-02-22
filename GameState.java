package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.google.gson.Gson;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
/**
 * Created by Lorence on 7/17/2017.
 */

class ScreenAnimation implements Drawable  {
    ScreenAnimation()
    {

        sprites = new ArrayList<Sprite>();
        startingAlpha = 0;
        fade_rate_per_sec = 254 / fading_duration;
        isFadingout = false;
        screenEnded = false;
        start();
    }
    void addSprite(Sprite newSprite)
    {
        if ( !sprites.contains(newSprite) )
        {
            setAlpha(newSprite, 0f);
            sprites.add(newSprite);
        }
    }
    void setAlpha(Sprite sprite, float alpha)
    {
        Color color = sprite.getColor();
        color.a = alpha;

        sprite.setColor(color);
    }

    boolean isScreenEnded() { return screenEnded; }
    void update(float delta)
    {
        if ( isScreenEnded() ) return;
        int temp = 0;
        if ( isFadingout )
        {
            if ( startingAlpha <= 0f && !finish )
            {
                screenEnded = true;
                isFadingout = false;
                return;
            }
            temp = -1;
        }
        else if ( !isFadingout )// fading in
        {
            if ( startingAlpha >= 254f && !finish )
            {
                isFadingout = true;
                finish = true;
                return;
            }
            temp = 1;
        }

        startingAlpha = startingAlpha + (fade_rate_per_sec * delta * temp);
        if ( isFadingout && startingAlpha < 0f ) startingAlpha = 0f;
        else if ( !isFadingout && startingAlpha > 254f ) startingAlpha = 254f;
        changeAlpha(startingAlpha);
    }
    void start()
    {
        screenEnded = false;
        finish = false;
    }
    void changeAlpha(float alpha)
    {
        for ( int a = 0; a < sprites.size(); a++ )
        {
            setAlpha(sprites.get(a), alpha/255f);
        }
    }
    boolean isFinished()
    {
        return finish;
    }
    float startingAlpha;
    boolean finish;// finish fading in
    java.util.List<Sprite> sprites;

    @Override
    public void draw(SpriteBatch batch) {
        batch.begin();
        for ( int a = 0; a < sprites.size(); a++ )
        {
            sprites.get(a).draw(batch);
        }
        batch.end();
    }
    float fade_rate_per_sec;
    final float fading_duration = 3.0f;
    boolean isFadingout;
    boolean screenEnded;
}
public abstract class GameState extends ClickListener {

    boolean isActiveState()
    {
        return states.get(states.size() - 1) == this;
    }
    void start()
    {
        animation.start();
    }
    GameState(java.util.List<GameState> iStates)
    {
        states = iStates;
        animation = new ScreenAnimation();
        selectedActor = null;
        stage = new Stage(new ScreenViewport(), new SpriteBatch());
        stage.addAction(Actions.alpha(0));
        stage.act();
        stage.addAction(Actions.fadeIn(3.0f));


        setDisabled(true);

    }
    java.util.List<GameState> states;
    abstract void draw(SpriteBatch batch);
    void update(float delta)
    {
        if ( !animation.isFinished() )
        {
            animation.update(delta);
            stage.act(delta);
            //Utils.makeLog("stage acted");
        }
        else
        {
            stage.act(delta);
            setDisabled(false);
        }
    }
    protected ScreenAnimation animation;
    ScreenAnimation getAnimation()
    {
        return animation;
    }
    void setDisabled(boolean disable)
    {

        if ( disable )
        {
            Gdx.input.setInputProcessor(null);
        }
        else
        {
            Gdx.input.setInputProcessor(stage);
        }
    }
    void startAnimation()
    {
        if ( animation.isScreenEnded() )
            stage.addAction(Actions.fadeIn(1.0f));
        else
            stage.addAction(Actions.fadeOut(1.0f));
        setDisabled(true);
        animation.start();
    }
    protected Actor selectedActor;
    Stage stage;
}
class QuarantineState extends GameState
{

    QuarantineState(java.util.List<GameState> iStates) {
        super(iStates);
        inner = new Table();
        scrollpane = new ScrollPane(inner);
        inner.setFillParent(true);
       // scrollpane.setFillParent(true);
        Table outer = new Table();
        stage.addActor(outer);
        outer.setFillParent(true);
        outer.add(scrollpane).expand().top().left();
        outer.row();
        scrollpane.setSize(GAME_GLOBALS.DESKTOP_SCREEN_WIDTH, GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - 32);

        Music music = Gdx.audio.newMusic(Gdx.files.internal("audios/scanning.mp3"));
        music.play();
        music.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                music.dispose();
            }
        });
        //stage.addActor(scrollpane);
        BitmapFont font = (BitmapFont)ResourceManager.getObjectFromResource("font");
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(new Sprite((Texture)ResourceManager.getObjectFromResource("button"))),null, null, font);
        delete = new ImageTextButton("DELETE",style);
        back = new ImageTextButton("BACK", style);
        back.setSize(GAME_GLOBALS.DESKTOP_SCREEN_WIDTH / 2, 32);
        delete.setSize(GAME_GLOBALS.DESKTOP_SCREEN_WIDTH / 2, 32);
        delete.setPosition(GAME_GLOBALS.DESKTOP_SCREEN_WIDTH / 2, 0);
        outer.add(delete).expandX().left();
        outer.row();
        outer.add(back).expandX().right();
        back.addListener(this);
        delete.addListener(this);
        stage.addActor(back);
        stage.addActor(delete);

        HashMap<String, Integer> list = new HashMap<String, Integer>();


        //ImageText imagetxt = new ImageText()
        if ( PlayerGlobals.loadedData.leaked != null ) {
            Label label;

            for ( int a = 0; a < PlayerGlobals.loadedData.leaked.size(); a++ )
            {
                String currName = PlayerGlobals.loadedData.leaked.get(a).name;
                if (list.containsKey(currName))
                {
                    list.put(currName, list.get(currName) + 1);
                }
                else
                {
                    list.put(currName, 1);
                }
                Utils.makeLog(currName);
            }
            int a = 0;
            Label.LabelStyle lblStyle = new Label.LabelStyle(font, Color.WHITE);
            for ( String key : list.keySet() )
            {
                SpriteDrawable virusSprite = new SpriteDrawable(new Sprite((Texture) ResourceManager.getObjectFromResource("q_" + key)));
                ImageText virus = new ImageText(virusSprite, 16, 16, key + " x" + Integer.toString(list.get(key)), lblStyle, DIRECTION.RIGHT);
                virus.addWithFillerTo(inner, DIRECTION.RIGHT);
                inner.row();

                a++;
            }
        }
    }

    @Override
    public void clicked (InputEvent event, float x, float y) {
        selectedActor = event.getListenerActor();

        if ( selectedActor == delete )
        {
            isDeleting = true;
            Music music = Gdx.audio.newMusic(Gdx.files.internal("audios/eliminating.mp3"));
            music.play();
            music.setOnCompletionListener(new Music.OnCompletionListener() {
                @Override
                public void onCompletion(Music music) {
                    music.dispose();
                }
            });
            SaveList lists = Utils.getFromJson("savestate.json", SaveList.class);
            for ( int a = 0; a < lists.list.size(); a++ )
            {
                if ( lists.list.get(a).name.equals(PlayerGlobals.loadedData.name) ) {
                    Utils.makeLog(PlayerGlobals.loadedData.name);
                    Utils.makeLog(String.valueOf(lists.list.get(a).leaked.size()));

                    lists.list.get(a).leaked.clear();
                    PlayerGlobals.loadedData.leaked.clear();
                }
            }

            Utils.serialize(lists);
            selectedActor = back;
            startAnimation();
            setDisabled(true);
        }
        else if ( selectedActor == back )
        {
            startAnimation();
            setDisabled(true);
        }
    }
    boolean isDeleting = false;
    ImageTextButton delete;
    ImageTextButton back;
    ScrollPane scrollpane;
    Table inner;
    int index = 0;
    float sec = 1.0f;
    @Override
    void draw(SpriteBatch batch) {


stage.draw();

    }
    float accumulated_time = 0;
    boolean isFinished = false;
    boolean isFinishedDeleting = false;
    @Override
    void update(float delta)
    {/*
        if ( PlayerGlobals.loadedData.leaked != null ) {
                Label label;

                for ( int a = 0; a < PlayerGlobals.loadedData.leaked.size(); a++ )
                {

                    label = new Label(
                            PlayerGlobals.loadedData.leaked.get(index).name,
                            new Label.LabelStyle((BitmapFont) ResourceManager.getObjectFromResource("font"), Color.RED));
                    label.setPosition(label.getX(), label.getY() * a);
                    inner.add(label);
                }

        }*/

        if ( animation.isScreenEnded() )
        {
            if ( selectedActor == back )
            {
                GameState currState = states.remove(states.size() - 1);
                states.get(states.size() - 1).startAnimation();
            }
            else if ( selectedActor == delete )
            {
            }
        }
        else super.update(delta);
    }

}
class MenuState extends GameState {

    @Override
    public void clicked (InputEvent event, float x, float y) {
        selectedActor = event.getListenerActor();
        if ( selectedActor == newgame || selectedActor == loadgame )
        {
            startAnimation();
        }
    }

    MenuState(java.util.List<GameState> iStates)
    {
        super(iStates);

        Music backgroundMusic = (Music) ResourceManager.getObjectFromResource("menuBackgroundMusic");
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        background = new Sprite((Texture) ResourceManager.getObjectFromResource("splash"));
        background.setSize(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.GAME_CAMERA_HEIGHT);
        Sprite buttonSprite = new Sprite((Texture) ResourceManager.getObjectFromResource("button"));
        BitmapFont font = BitmapFontFactory.createdBitmapFrontFromFile(Gdx.files.internal("graphics/fonts/arial.ttf"), 32);
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(buttonSprite), null, null, font);
        GlyphLayout layout = new GlyphLayout(font, "New Game");

        newgame = new ImageTextButton("New Game", style);
        loadgame = new ImageTextButton("Load Game", style);
        float w = layout.width + 4, h = 96;
        float baseX = GAME_GLOBALS.DESKTOP_SCREEN_WIDTH/2, baseY = GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT / 2;
        newgame.setSize(w, h);
        newgame.setPosition(baseX - (w/2), baseY);
        layout.setText(font, "Load Game");
        w = layout.width;
        loadgame.setSize(w, h);
        loadgame.setPosition(baseX - (w/2), baseY - h);
        newgame.addListener(this);
        loadgame.addListener(this);
        stage.addActor(newgame);
        stage.addActor(loadgame);
        animation.addSprite(background);
        setDisabled(true);
        backgroundAnimation = GifDecoder.loadGIFAnimation(0, Gdx.files.internal("graphics/textures/userinterfaces/mainmenu.gif").read());
    }
    com.badlogic.gdx.graphics.g2d.Animation backgroundAnimation;
    @Override
    void start()
    {
        super.start();
        stage.addAction(Actions.fadeOut(1.0f));

    }
    @Override
    void draw(SpriteBatch batch) {

        animation.draw(batch);
        //batch.draw(backgroundAnimation.getKeyFrame(Gdx.graphics.getDeltaTime(), true),200, 200);
        stage.draw();
    }
    @Override
    void update(float delta) {
        if (animation.isScreenEnded())
        {
            if (selectedActor == newgame)
            {
                states.add(new RegisterState(states));
            }
            else if (selectedActor == loadgame)
            {
                states.add(new LoadGameState(states));
            }
        }
        else super.update(delta);
    }
    Sprite background;
    ImageTextButton newgame;
    ImageTextButton loadgame;

}
class SplashState extends GameState {
    float accumulated_data;

    SplashState(java.util.List<GameState> iStates)
    {
        super(iStates);
        accumulated_data = 0;
        splashscreen = new Sprite((Texture)ResourceManager.getObjectFromResource("mainsplash"));
        splashscreen.setSize(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.GAME_CAMERA_HEIGHT);
        animation.addSprite(splashscreen);
    }
    Sprite splashscreen;
    @Override
    public void draw(SpriteBatch batch) {
        animation.draw(batch);
    }
    @Override
    public void update(float delta)
    {
        if ( animation.isScreenEnded() )
        {
            GameState game = states.remove(0);
            states.add(new MenuState(states));
        }
        else if ( animation.isFinished() )
        {
            if ( accumulated_data >= splash_time )
            {
                startAnimation();
            }
            else
            {
                accumulated_data += delta;
            }
        }
        else
        {
            super.update(delta);
        }
    }


    final float splash_time = 2.0f;
}

class Scene extends GameState
{

    float accumulated_data;

    Scene(java.util.List<GameState> iStates, Sprite sprite, java.util.List<Scene> iScenes)
    {
        super(iStates);
        accumulated_data = 0;
        splashscreen = sprite;
        animation.addSprite(splashscreen);
        scenes = iScenes;
    }
    Sprite splashscreen;
    @Override
    public void draw(SpriteBatch batch) {
        animation.draw(batch);
    }
    @Override
    public void update(float delta)
    {
        if ( animation.isScreenEnded() )
        {
            scenes.remove(scenes.size() - 1);
        }
        else if ( animation.isFinished() )
        {
            if ( accumulated_data >= splash_time )
            {
                startAnimation();
            }
            else
            {
                accumulated_data += delta;
            }
        }
        else
        {
            super.update(delta);
        }
    }

    java.util.List<Scene> scenes;
    final float splash_time = 2.0f;
}
class CutsceneState extends GameState
{
    CutsceneState(java.util.List<GameState> iStates, java.util.List<Scene> scenes, Music music) {
        super(iStates);
        startingState = iStates.size();
        setDisabled(false);
        sceneList = scenes;
        sceneMusic = music;
        sceneMusic.play();
        sceneMusic.setLooping(true);

        BitmapFont font = BitmapFontFactory.createdBitmapFrontFromFile(Gdx.files.internal("graphics/fonts/arial.ttf"), 32);
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(new Sprite((Texture)ResourceManager.getObjectFromResource("skipButton"))),null, null, font);
        skipButton = new ImageTextButton("SKIP",style);
        skipButton.setSize(200, 32);
        skipButton.setPosition(GAME_GLOBALS.DESKTOP_SCREEN_WIDTH - skipButton.getWidth(), 0);
        skipButton.addListener(this);
        stage.addActor(skipButton);
    }
    Music sceneMusic;
    ImageTextButton skipButton;

    ///skip
    public void clicked (InputEvent event, float x, float y)
    {
        sceneList.remove(sceneList.size() - 1);
    }


    int startingState;
    java.util.List<Scene> sceneList;
    @Override
    void update(float delta)
    {
        if ( sceneList.size() <= 0 )
        {
            if ( !Core.isFinishedGame ) {
                Core.isTutorial = true;
                Core.tutorial_step = 0;
            }
            sceneMusic.stop();

            states.add(new PlayerProfileState(states));
        }
        else {
            sceneList.get(sceneList.size() - 1).update(delta);
            super.update(delta);
        }
    }
    @Override
    void draw(SpriteBatch batch) {
        if ( sceneList.size() > 0 ) {
            sceneList.get(sceneList.size() - 1).draw(batch);
            stage.draw();
        }
    }


}
class LoadGameState extends GameState {
    public void clicked (InputEvent event, float x, float y)
    {
        selectedActor = event.getListenerActor();
        if ( selectedActor == back )
        {
            startAnimation();
            setDisabled(false);
        }
        else if ( selectedActor == load )
        {
            startAnimation();
            setDisabled(false);
        }
    }


    LoadGameState(java.util.List<GameState> iStates) {
        super(iStates);


        Sprite listSprite = new Sprite((Texture) ResourceManager.getObjectFromResource("splash"));
        listSprite.setSize(GAME_GLOBALS.DESKTOP_SCREEN_WIDTH, GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - 32);
        BitmapFont font = BitmapFontFactory.createdBitmapFrontFromFile(Gdx.files.internal("graphics/fonts/arial.ttf"), 32);
        List.ListStyle style = new List.ListStyle(font, Color.RED, Color.RED, new SpriteDrawable(listSprite));
        Sprite buttonSprite = new Sprite((Texture) ResourceManager.getObjectFromResource("button"));
        ImageTextButton.ImageTextButtonStyle buttonStyle = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(buttonSprite), null, null, font);
        load = new ImageTextButton("Load", buttonStyle);
        back = new ImageTextButton("Back", buttonStyle);
        accountList = new List<String>(style);

        accountList.setSize(GAME_GLOBALS.DESKTOP_SCREEN_WIDTH, GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - 32);
        accountList.setPosition(32, 0);
        list = Utils.deserialize("savestate.json", SaveList.class);
        if (!Utils.isObjectNull(list)) {
            Array<String> saves = new Array<String>();
            for (int a = 0; a < list.list.size(); a++) {
                saves.add(list.list.get(a).name);
            }
            accountList.setItems(saves);
        }

        int w = (int)(GAME_GLOBALS.DESKTOP_SCREEN_WIDTH / 2);
        load.setSize(w, 32);
        load.setPosition(0, 0);
        back.setSize(w, 32);
        back.setPosition(w, 0);
        stage.addActor(accountList);
        stage.addActor(back);
        stage.addActor(load);
        back.addListener(this);
        background = new Sprite((Texture) ResourceManager.getObjectFromResource("mainmenu"));
        background.setSize(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.GAME_CAMERA_HEIGHT);
        animation.addSprite(background);

        setDisabled(true);
        load.addListener(this);

        Gdx.input.setInputProcessor(stage);
    }
    SaveList list;
    @Override
    void draw(SpriteBatch batch) {
        animation.draw(batch);
        stage.draw();

    }

    @Override
    void update(float delta) {
        if ( animation.isScreenEnded() )
        {
            if ( selectedActor == back )
            {
                if ( Core.isTutorial )
                    Core.tutorial_step = 0;
                GameState currState = states.remove(states.size() - 1);
                states.get(states.size() - 1).startAnimation();

            }
            else if ( selectedActor == load )
            {
                for ( int a = 0; a < list.list.size(); a++ )
                {
                    if ( list.list.get(a).name.equals(getLoadedItem()) )
                    {
                        PlayerGlobals.loadedData = list.list.get(a);
                        break;
                    }
                }
                states.add(new PlayerProfileState(states));
            }
        }
        else super.update(delta);
    }

    String getLoadedItem()
    {
        return accountList.getSelected();
    }

    Sprite background;
    List<String> accountList;
    ImageTextButton back;
    ImageTextButton load;
}
class RegisterState extends GameState {
    String getDecidedName()
    {
        return input.getText();
    }

    public void clicked (InputEvent event, float x, float y)
    {
        selectedActor = event.getListenerActor();
        if ( selectedActor == create )
        {
            BitmapFont font = BitmapFontFactory.createdBitmapFrontFromFile(Gdx.files.internal("graphics/fonts/arial.ttf"), 32);
            Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
            SaveList lists = Utils.deserialize("savestate.json", SaveList.class);
            if (Utils.isObjectNull(lists)) {
                lists = new SaveList();
            }
            if ( lists.isExist(input.getText()) )
            {
                input.setText("");
            }
            else
            {
                resultDialog.show(stage);
                resultDialog.setText("Account created! press okay to continue");
                SaveInfo newAccount = new SaveInfo(getDecidedName());
                PlayerGlobals.loadedData = newAccount;
                lists.add(newAccount);
                Utils.serialize(lists);

            }

        }
        else if ( selectedActor == cancel )
        {
            startAnimation();
            setDisabled(true);
        }
    }
    RegisterState(java.util.List<GameState> iStates)
    {
        super(iStates);
        Sprite listSprite = new Sprite((Texture) ResourceManager.getObjectFromResource("textbox"));
        listSprite.setSize(GAME_GLOBALS.DESKTOP_SCREEN_WIDTH, GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - 32);
        BitmapFont font = BitmapFontFactory.createdBitmapFrontFromFile(Gdx.files.internal("graphics/fonts/arial.ttf"), 32);
        Sprite buttonSprite = new Sprite((Texture) ResourceManager.getObjectFromResource("button"));
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
        label = new Label("Please enter your name", style);
        TextField.TextFieldStyle txtFieldStyle = new TextField.TextFieldStyle(font, Color.WHITE, null, null, new SpriteDrawable(listSprite));
        input = new TextField("", txtFieldStyle);
        ImageTextButton.ImageTextButtonStyle buttonStyle = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(buttonSprite), null, null, font);
        create = new ImageTextButton("Create", buttonStyle);
        cancel = new ImageTextButton("Cancel", buttonStyle);

        label.setSize(GAME_GLOBALS.DESKTOP_SCREEN_WIDTH, 32);
        input.setSize(GAME_GLOBALS.DESKTOP_SCREEN_WIDTH, 32);
        label.setPosition(0, ( GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT / 2 ) + 96);
        input.setPosition(0, label.getY() - 32);
        int w = GAME_GLOBALS.DESKTOP_SCREEN_WIDTH / 2;
        create.setSize(w, 32);
        create.setPosition(0, 0);
        cancel.setSize(w, 32);
        cancel.setPosition(w, 0);

        stage.addActor(create);
        stage.addActor(cancel);
        stage.addActor(label);
        stage.addActor(input);
        background = new Sprite((Texture) ResourceManager.getObjectFromResource("splash"));
        background.setSize(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.GAME_CAMERA_HEIGHT);
        animation.addSprite(background);
        cancel.addListener(this);
        create.addListener(this);
        Skin dialogSkin = new Skin();
        Window.WindowStyle windowStyle = new Window.WindowStyle(font, Color.WHITE,  new SpriteDrawable(listSprite) );
        dialogSkin.add("splash", windowStyle);
        Label lbl = new Label("test", style);
        resultDialog = new ModalDialog("", dialogSkin,"splash");
        resultDialog.setFillParent(true);
        resultDialog.setPosition(0, 0);
        setDisabled(true);


    }

    @Override
    void draw(SpriteBatch batch)
    {
        animation.draw(batch);
        stage.draw();
        //animation.draw(batch);
    }

    @Override
    void update(float delta)
    {
        ModalDialog.DialogCode code = resultDialog.getCode();

        if ( !Utils.isObjectNull(code) )
        {
            Utils.makeLog("ModalDialog.DialogCode.NOTNULL");
            startAnimation();
            setDisabled(true);
            resultDialog.setDialogCode(null);
        }

        if ( animation.isScreenEnded() )
        {
            GameState currState = states.remove(states.size() - 1);
            if ( selectedActor == cancel )
            {
                states.get(states.size() - 1).startAnimation();
            }
            else if ( selectedActor == create )
            {
                states.clear();
                java.util.List<Scene> scenes = new ArrayList<Scene>();
                for ( int a = 13; a > 0 ; a-- )
                {
                    Sprite sprite = new Sprite(new Texture(Gdx.files.internal("graphics/textures/CUTSCENE/" + Integer.toString(a) + ".png")));

                    sprite.setSize(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.GAME_CAMERA_HEIGHT);
                    scenes.add(new Scene(states,
                            sprite,
                            scenes
                    ));
                }
                states.add(new CutsceneState(states,scenes,(Gdx.audio.newMusic(Gdx.files.internal("audios/arrival.mp3")))));
            }
        }
        else super.update(delta);
    }
    Sprite background;
    Label label;
    TextField input;
    ImageTextButton create;
    ImageTextButton cancel;


    ModalDialog resultDialog;
}
class PlayerProfileState extends GameState {


    Sprite background;
    PlayerProfileState(java.util.List<GameState> iStates) {
        super(iStates);
        Music music = (Music)ResourceManager.getObjectFromResource("menuBackgroundMusic");
        if ( !Core.isTutorial )
        {
            music.play();
        }
        else {
            music.stop();
        }
        background = new Sprite((Texture) ResourceManager.getObjectFromResource("splash"));
        background.setSize(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.GAME_CAMERA_HEIGHT);
        animation.addSprite(background);
        Sprite buttonSprite = new Sprite((Texture) ResourceManager.getObjectFromResource("button"));
        BitmapFont font = BitmapFontFactory.createdBitmapFrontFromFile(Gdx.files.internal("graphics/fonts/arial.ttf"), 64);
        SpriteDrawable sprdraw = new SpriteDrawable(buttonSprite);

        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(sprdraw, null, null, font);

        GlyphLayout layout = new GlyphLayout(font, "Play game");
        playGame = new ImageTextButton("Play game", style);

        //playGame.setSize
        almanac = new ImageTextButton("almanac", style);
        settings = new ImageTextButton("Settings", style);
        quit = new ImageTextButton("Quit", style);
        quarantined = new ImageTextButton("quarantine", style);
        Label.LabelStyle lblStyle = new Label.LabelStyle(font, Color.WHITE);
        money = new Label("insert money here", lblStyle);

        quarantined.addListener(this);
        Table table1 = new Table();
        table1.setFillParent(true);
        table1.top().left();
        layout.setText(font, "Play Game");
        table1.add(playGame).size(layout.width, layout.height).expand().fillY();
        table1.row().expand();
        layout.setText(font, "Almanac");
        table1.add(almanac).size(layout.width, layout.height).expand().fillY();
        table1.row().expand();
        layout.setText(font, "Settings");
        table1.add(settings).size(layout.width, layout.height).expand().fillY();
        table1.row().expand();

        layout.setText(font, "Quarantine");
        table1.add(quarantined).size(layout.width, layout.height).expand().fillY();
        table1.row().expand();
        layout.setText(font, "Quit");
        table1.add(quit).size(layout.width, layout.height).expand().fillY();
        table1.row().expand();

        stage.addActor(table1);




        if ( Core.isTutorial ) {
                almanac.addListener(this);
                playGame.removeListener(this);
                Music quarantineExplanation = Gdx.audio.newMusic(Gdx.files.internal("audios/quarantineExplanation.mp3"));
                quarantineExplanation.play();
                quarantineExplanation.setOnCompletionListener(new Music.OnCompletionListener() {
                    @Override
                    public void onCompletion(Music music) {

                        music.dispose();
                    }
                });
        }
        else {
            playGame.addListener(this);
            almanac.addListener(this);
            settings.addListener(this);
            quit.addListener(this);
        }

    }

    boolean explained = false;
    Label money;
    @Override
    public void clicked (InputEvent event, float x, float y) {
        selectedActor = event.getListenerActor();
        setDisabled(true);
        startAnimation();
        if ( selectedActor == playGame )
        {
            //0 = alamanac
            //1 = playgame
            //2 = quara

            if ( Core.isTutorial )
            {
                if ( Core.tutorial_step == 0 )
                {
                    selectedActor = null;
                }

            }
            Utils.makeLog("plagegame");
        }
        else if ( selectedActor == almanac )
        {
            if ( Core.isTutorial )
            {
                if ( Core.tutorial_step == 1 )
                {
                    selectedActor = null;
                }
            }
            Utils.makeLog("almanac");
        }
        else if ( selectedActor == settings )
        {
            if ( Core.isTutorial )
                selectedActor = null;
            Utils.makeLog("settings");
        }
        else if ( selectedActor == quit )
        {
            if (Core.isTutorial)
                selectedActor = null;
            Utils.makeLog("clicked quit");
        }
    }
    @Override
    void draw(SpriteBatch batch) {
        animation.draw(batch); stage.draw();
    }

    @Override
    void update(float delta)
    {
        if ( Core.isTutorial && Core.tutorial_step == 1 && !explained) {
            playGame.addListener(this);
            almanac.removeListener(this);
            Music quarantineExplanation = Gdx.audio.newMusic(Gdx.files.internal("audios/playGameExplanation.mp3"));
            quarantineExplanation.play();
            quarantineExplanation.setOnCompletionListener(new Music.OnCompletionListener() {
                @Override
                public void onCompletion(Music music) {

                    music.dispose();
                }
            });
            explained = true;
        }
        if ( animation.isScreenEnded() )
        {
            if ( selectedActor == playGame )
            {
                states.add(new PlayerStage(states));

            }
            else if ( selectedActor == almanac )
            {
                states.add(new PlayerAlmanacState(states));
            }
            else if ( selectedActor == settings )
            {
                states.add(new PlayerSettingState(states));
            }
            else if ( selectedActor == quit )
            {
                states.remove(states.size() - 1);
                states.add(new MenuState(states));
            }
            else if ( selectedActor == quarantined )
            {
                states.add(new QuarantineState(states));
            }
        }
        else super.update(delta);
    }


    ImageTextButton playGame;
    ImageTextButton almanac;
    ImageTextButton settings;
    ImageTextButton quit;
    ImageTextButton quarantined;
}
class PlayerSettingState extends GameState {

    PlayerSettingState(java.util.List<GameState> iStates) {
        super(iStates);

        BitmapFont font = BitmapFontFactory.createdBitmapFrontFromFile(Gdx.files.internal("graphics/fonts/arial.ttf"), 32);
        Sprite buttonSprite = new Sprite((Texture) ResourceManager.getObjectFromResource("button"));
        ImageTextButton.ImageTextButtonStyle buttonStyle = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(buttonSprite), null, null, font);

        Sprite background = new Sprite((Texture) ResourceManager.getObjectFromResource("splash"));

        background.setSize(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.GAME_CAMERA_HEIGHT);
        easy = new ImageTextButton("Easy", buttonStyle);
        medium = new ImageTextButton("Medium", buttonStyle);
        hard = new ImageTextButton("Hard", buttonStyle);


        animation.addSprite(background);
        Table table = new Table();
        table.add(easy).fill();
        table.row();
        table.add(medium).fill();
        table.row();
        table.add(hard).fill();


        easy.addListener(this);
        medium.addListener(this);
        hard.addListener(this);
        stage.addActor(table);
        table.setFillParent(true);



    }

    @Override
    public void clicked (InputEvent event, float x, float y) {
        selectedActor = event.getListenerActor();

        startAnimation();


    }
    @Override
    void draw(SpriteBatch batch) {
        animation.draw(batch);
        stage.draw();
    }




    @Override
    void update(float delta)
    {
        if ( animation.isScreenEnded() )
        {
            if (selectedActor == easy) Core.level = 1;
            else if (selectedActor == medium) Core.level = 2;
            else Core.level = 3;


            GameState currState = states.remove(states.size() - 1 );
            states.get(states.size() - 1).startAnimation();
        }
        else super.update(delta);
    }

    ImageTextButton easy;
    ImageTextButton medium;
    ImageTextButton hard;
}
class PlayerAlmanacState extends GameState {

    Sprite background;
    PlayerAlmanacState(java.util.List<GameState> iStates)
    {
        super(iStates);

        if ( Core.isTutorial )
        {
            Music music = Gdx.audio.newMusic(Gdx.files.internal("audios/almanacExplanation.mp3"));
            music.play();
            music.setOnCompletionListener(new Music.OnCompletionListener() {
                @Override
                public void onCompletion(Music music) {
                    music.dispose();
                }
            });
        }
        background = new Sprite((Texture) ResourceManager.getObjectFromResource("splash"));
        background.setSize(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.GAME_CAMERA_HEIGHT);
        animation.addSprite(background);
        virusesAndDescriptions = new java.util.ArrayList<ImageText>();
        Table inner = new Table();
        Sprite buttonSprite = new Sprite((Texture) ResourceManager.getObjectFromResource("button"));


        ScrollPane scrollPane = new ScrollPane(inner);
        //inner.background(bg);
        BitmapFont font = (BitmapFont) ResourceManager.getObjectFromResource("font");
        Label.LabelStyle lblStyle = new Label.LabelStyle(font, Color.WHITE);
       // inner.setDebug(true);
        SpriteDrawable tableBackgroundWhenClicked = new SpriteDrawable(new Sprite((Texture) ResourceManager.getObjectFromResource("selectedItemBackgroundAlmanac")));





        datas = Utils.deserialize("quarantine.json", AlmanacDatas.class);
        if ( Core.isTutorial )
        {/*
            datas.datas.clear();;
            AlmanacData data = new AlmanacData();
            data.definition = "Test definition";
            data.cause = "Test cause";
            data.howtoremove = "Test how to remove";
            data.name = "Test name";
            data.prevention = "Test prevention";
            datas.datas.add(data);*/
        }

        for ( int a = 0; a < datas.datas.size(); a++ )
        {
            ImageText virusAndDescription = new ImageText(new SpriteDrawable(buttonSprite),16, 16, datas.datas.get(a).name, lblStyle, DIRECTION.RIGHT);
            virusAndDescription.setBackgroundWhenClicked(tableBackgroundWhenClicked);
            virusAndDescription.addClickListener(this);

            virusAndDescription.addWithFillerTo(inner, DIRECTION.LEFT);
            inner.row();

            virusesAndDescriptions.add(virusAndDescription);
        }

        SpriteDrawable sprdraw = new SpriteDrawable(buttonSprite);
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(sprdraw, null, null, font);
        back = new ImageTextButton("BACK", style);
        inner.left();
        Table root = new Table();
        root.setFillParent(true);
        root.add(scrollPane).expand().left().fill();
        root.row();
        root.add(back).expand().left().fill();

        back.addListener(this);
        stage.addActor(root);
        Sprite listSprite = new Sprite((Texture) ResourceManager.getObjectFromResource("splash"));
        Skin dialogSkin = new Skin();
        Window.WindowStyle windowStyle = new Window.WindowStyle(font, Color.WHITE,  new SpriteDrawable(listSprite) );
        dialogSkin.add("splash", windowStyle);
        dialog = new AlmanacDialog("Almanac", dialogSkin, "splash");
    }


    ImageTextButton back;
    java.util.List<ImageText> virusesAndDescriptions;
    @Override
    public void clicked (InputEvent event, float x, float y) {
        selectedActor = event.getListenerActor();

        if ( selectedActor == back ) {

            if ( Core.isTutorial)
            Core.tutorial_step = 1;
            setDisabled(true);
            startAnimation();

        }
        else {
            boolean selected = false, deselected = false;
            String almanacName = "nameless shit";
            for ( int a = 0; a < virusesAndDescriptions.size(); a++ ) {
                ImageText curr = virusesAndDescriptions.get(a);
                if (curr.isSelected())
                {
                    curr.setSelected(false);
                    deselected = true;
                }
                if ( curr.isEqual(selectedActor) ) {
                    curr.setSelected(true);
                    selected = true;
                    almanacName = curr.getText();
                }
                if ( selected && deselected ) break;
            }

            AlmanacData data = datas.find(almanacName);
            if ( Core.isTutorial )
            {
                data.prevention = "Test prevention";
                data.name = "Test name";
                data.howtoremove = "Test how to remove";
                data.cause = "Test cause";
                data.definition = "Test definition";
            }
            Window.WindowStyle winStyle = new Window.WindowStyle((BitmapFont) ResourceManager.getObjectFromResource("font"), Color.WHITE, new SpriteDrawable(new Sprite((Texture) ResourceManager.getObjectFromResource("splash"))));
            view = new AlmanacDataView("", winStyle, data);
            view.setFillParent(true);
            view.setPosition(0, 0);
            stage.addActor(view);
            //Gdx.input.setInputProcessor(view.getStage());
        }

    }
    AlmanacDatas datas;
    AlmanacDataView view;
    AlmanacDialog dialog;
    @Override
    void draw(SpriteBatch batch) {
        animation.draw(batch); stage.draw();
    }

    @Override
    void update(float delta)
    {
        if ( animation.isScreenEnded() )
        {
            if ( selectedActor == back )
            {
                GameState currState = states.remove(states.size() - 1 );
                states.get(states.size() - 1).startAnimation();
            }
        }
        else
            super.update(delta);
    }


}


class PlayerStage extends GameState
{
    Sprite background;
    PlayerStage(java.util.List<GameState> iStates) {
        super(iStates);

        if (Core.isTutorial) {
            Music music = Gdx.audio.newMusic(Gdx.files.internal("audios/stageExplanation.mp3"));
            music.play();
            music.setOnCompletionListener(new Music.OnCompletionListener() {
                @Override
                public void onCompletion(Music music) {
                    music.dispose();
                }
            });
        }
        background = new Sprite((Texture) ResourceManager.getObjectFromResource("splash"));
        background.setSize(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.GAME_CAMERA_HEIGHT);
        animation.addSprite(background);

        SpriteDrawable buttonDrawable = new SpriteDrawable(new Sprite((Texture)ResourceManager.getObjectFromResource("button")));
        SpriteDrawable disabledButtonDrawable = new SpriteDrawable(new Sprite((Texture)ResourceManager.getObjectFromResource("button")));
        BitmapFont font = (BitmapFont) ResourceManager.getObjectFromResource("font");
        Label.LabelStyle lblStyle = new Label.LabelStyle(font, Color.WHITE);

       // String stage1str = "STAGE 1 \n" + Integer.toString(PlayerGlobals.loadedData.stage1star) + " stars";
        //String stage2str = "STAGE 2 \n" + Integer.toString(PlayerGlobals.loadedData.stage2star) + " stars";
        //String stage3str = "STAGE 3 \n" + Integer.toString(PlayerGlobals.loadedData.stage3star) + " stars";
        stage1 = new ImageText(buttonDrawable, 64, 64, "STAGE 1", lblStyle, DIRECTION.UP);
        stage2 = new ImageText(buttonDrawable, 64, 64, "STAGE 2", lblStyle, DIRECTION.UP);
        stage3 = new ImageText(buttonDrawable, 64, 64, "STAGE 3", lblStyle, DIRECTION.UP);
        ImageTextButton.ImageTextButtonStyle buttonStyle = new ImageTextButton.ImageTextButtonStyle(buttonDrawable, null, null, font);
        ImageTextButton.ImageTextButtonStyle disabledButtonStyle = new ImageTextButton.ImageTextButtonStyle(disabledButtonDrawable, null, null, font);
        back = new ImageTextButton("BACK", buttonStyle);
            stage1.addClickListener(this);
        if ( PlayerGlobals.loadedData.stage >= 2 ) {

            stage2.addClickListener(this);
        }

        if ( PlayerGlobals.loadedData.stage >= 3 ) {
            stage3.addClickListener(this);
        }




        Table root = new Table();








        java.util.List<java.util.List<Image>> stagesStars = new ArrayList<java.util.List<Image>>();

        stagesStars.add(generateStar(PlayerGlobals.loadedData.stage1star, 3));
        stagesStars.add(generateStar(PlayerGlobals.loadedData.stage2star, 3));
        stagesStars.add(generateStar(PlayerGlobals.loadedData.stage3star, 3));

        root.setFillParent(true);

        root.row();
        for ( int a = 0; a < stagesStars.size(); a++ )
        {
            for ( int b = 0; b < stagesStars.get(a).size(); b++ ) {
                root.add(stagesStars.get(a).get(b)).left();
            }
            root.row();
            if (a == 0)
                stage1.addTo(root);
            else if (a == 1)
                stage2.addTo(root);
            else if (a == 2)
                stage3.addTo(root);
            root.row();
        }

        root.add(back);

        stage.addActor(root);
        back.addListener(this);
    }

    java.util.List<Image> generateStar(int nStar, int max)
    {
        SpriteDrawable star = new SpriteDrawable(new Sprite((Texture) ResourceManager.getObjectFromResource("star")));
        SpriteDrawable blankStar = new SpriteDrawable(new Sprite((Texture) ResourceManager.getObjectFromResource("blackStar")));



        java.util.List<Image> images = new ArrayList<Image>();
        for ( int a = 0; a < max; a++ )
        {
            if ( a < nStar )
                images.add(new Image(star));
            else
                images.add(new Image(blankStar));
        }


        return images;
    }
    @Override
    void draw(SpriteBatch batch) {
        animation.draw(batch);
        stage.draw();
    }
    ImageText stage1;
    ImageText stage2;
    ImageText stage3;
    ImageTextButton back;
    @Override
    void update(float delta)
    {
        if ( animation.isScreenEnded() )
        {
            if ( selectedActor == back )
            {
                GameState currState = states.remove(states.size() - 1);
                states.get(states.size() - 1).startAnimation();
                return;
            }
            PlayerSkillTreeState state = new PlayerSkillTreeState(states);
            if ( stage1.isEqual(selectedActor) )
            {
                state.currStage = 1;
            }
            else if ( stage2.isEqual(selectedActor) )
            {
                state.currStage = 2;
            }
            else if ( stage3.isEqual(selectedActor) )
            {
                state.currStage = 3;
            }
            states.add(state);
        }
        else super.update(delta);
    }
    @Override
    public void clicked (InputEvent event, float x, float y)
    {
        selectedActor = event.getListenerActor();
        if ( stage1.isEqual(selectedActor) )
        {
            startAnimation();
            setDisabled(true);
        }
        else if ( stage2.isEqual(selectedActor) )
        {
            startAnimation();
            setDisabled(true);
        }
        else if ( stage3.isEqual(selectedActor) )
        {
            startAnimation();
            setDisabled(true);
        }
        else if ( selectedActor == back )
        {
            startAnimation();
            setDisabled(true);
        }
    }


}
class PlayerSkillTreeState extends GameState /* for now, inflexible data only */ {
    Sprite background;
    PlayerSkillTreeState(java.util .List<GameState> iStates) {
        super(iStates);


        if ( Core.isTutorial )
        {
            Music music = Gdx.audio.newMusic(Gdx.files.internal("audios/skillTreeExplanation.mp3"));
            music.play();
            music.setOnCompletionListener(new Music.OnCompletionListener() {
                @Override
                public void onCompletion(Music music) {
                    music.dispose();
                }
            });
        }
        background = new Sprite((Texture) ResourceManager.getObjectFromResource("splash"));
        background.setSize(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.GAME_CAMERA_HEIGHT);
        animation.addSprite(background);
        Texture skillTreeAtlas = (Texture) ResourceManager.getObjectFromResource("skilltree");
        TextureRegion skill1 = new TextureRegion(skillTreeAtlas, (int)0, (int)0, 32, 32);
        TextureRegion skill2 = new TextureRegion(skillTreeAtlas, 32, 0, 32, 32);
        TextureRegion skill3 = new TextureRegion(skillTreeAtlas, 64, 0, 32, 32);
        TextureRegion skill4 = new TextureRegion(skillTreeAtlas, 96, 0, 32, 32);
        TextureRegion skill5 = new TextureRegion(skillTreeAtlas, 128, 0, 32, 32);
        TextureRegion skill6 = new TextureRegion(skillTreeAtlas, 160, 0, 32, 32);
        TextureRegion skill7 = new TextureRegion(skillTreeAtlas, 192, 0, 32, 32);
        BitmapFont font = (BitmapFont) ResourceManager.getObjectFromResource("font");
        Label.LabelStyle lblStyle = new Label.LabelStyle(font,Color.WHITE);
        ImageText attrib = new ImageText(new SpriteDrawable(new Sprite(skill1)),32, 32,"ATTRIB",lblStyle, DIRECTION.UP);
        ImageText patch = new ImageText(new SpriteDrawable(new Sprite(skill2)),32, 32,"PATCH",lblStyle, DIRECTION.UP);
        ImageText secureNetwork = new ImageText(new SpriteDrawable(new Sprite(skill3)),32, 32,"SECURE NETWORK",lblStyle, DIRECTION.UP);
        ImageText update = new ImageText(new SpriteDrawable(new Sprite(skill4)),32, 32,"UPDATE",lblStyle, DIRECTION.UP);
        ImageText thinkBeforeYouClick = new ImageText(new SpriteDrawable(new Sprite(skill5)),32, 32,"THINK BEFORE YOU CLICK",lblStyle, DIRECTION.UP);
        ImageText regularScan = new ImageText(new SpriteDrawable(new Sprite(skill6)),32, 32,"REGULAR SCAN",lblStyle, DIRECTION.UP);
        ImageText osUpdate = new ImageText(new SpriteDrawable(new Sprite(skill7)),32, 32,"OS UPDATE",lblStyle, DIRECTION.UP);
        Label label = new Label("BUILD UP PROTECTION AND ENHANCEMENT OF YOUR SYSTEM BEFORE GOING INTO BATTLE!!", lblStyle);
        label.setWrap(true);
        points = 0;
        lblPoints = new Label("Points left: 0", lblStyle);
        Table root = new Table();
        Texture btnSkinTexture = (Texture) ResourceManager.getObjectFromResource("button");
        ImageTextButton.ImageTextButtonStyle btnStyle = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(new Sprite(btnSkinTexture)), null, null, font);
        start = new ImageTextButton("START", btnStyle);
        back = new ImageTextButton("BACK", btnStyle);
        back.addListener(this);
        Table skillTreeTable = new Table();
        skillTreeTable.setDebug(true);
        skillTreeTable.add(lblPoints);
        skillTreeTable.row();
        attrib.addTo(skillTreeTable);

        skillTreeTable.row();
        patch.addTo(skillTreeTable);
        skillTreeTable.row();

        secureNetwork.addTo(skillTreeTable);
        skillTreeTable.row();

        update.addTo(skillTreeTable);
        skillTreeTable.row();

        thinkBeforeYouClick.addTo(skillTreeTable);
        skillTreeTable.row();

        regularScan.addTo(skillTreeTable);
        skillTreeTable.row();

        osUpdate.addTo(skillTreeTable);
        skillTreeTable.row();

        skillTreeTable.add(start).expandX();
        skillTreeTable.add(back).expandX();

        Table lblTable = new Table();
        lblTable.add(label).width(GAME_GLOBALS.DESKTOP_SCREEN_WIDTH);
        root.setFillParent(true);
        root.setDebug(true);
        root.add(lblTable).left();
        root.row();
        root.add(skillTreeTable).expandY().top().left();


        stage.addActor(root);

        skillTree = new java.util.ArrayList<ImageText>();
        skillTree.add(attrib);
        skillTree.add(patch);
        skillTree.add(secureNetwork);
        skillTree.add(update);
        skillTree.add(thinkBeforeYouClick);
        skillTree.add(regularScan);
        skillTree.add(osUpdate);
        for ( int a = 0; a < skillTree.size(); a++ )
        {
            skillTree.get(a).setBackgroundWhenClicked(new SpriteDrawable(new Sprite((Texture)ResourceManager.getObjectFromResource("selectedItemBackgroundAlmanac"))));
            skillTree.get(a).addClickListener(this);
        }
        //start.addListener(this);
        back.addListener(this);
        setPoints(PlayerGlobals.loadedData.skilltree);

    }
    Label lblPoints;
    java.util.List<ImageText> skillTree;
    int points;
    void setPoints(int iPoints)
    {
        points = iPoints;
        lblPoints.setText("Points left: " + Integer.toString(points));
        if ( points == 0 ) start.addListener(this);
        else start.clearListeners();
    }
    ImageTextButton start;
    ImageTextButton back;
    int currStage;


    @Override
    public void clicked (InputEvent event, float x, float y) {
        selectedActor = event.getListenerActor();
        for ( int a = 0; a < skillTree.size(); a++ ) {
            ImageText curr = skillTree.get(a);
            if ( curr.isEqual(selectedActor) ) {
                if ( curr.isSelected() )
                {
                    curr.setSelected(false);
                    setPoints(points + 1);
                }
                else
                {
                    if ( points > 0 ) {
                        setPoints(points - 1);
                        curr.setSelected(true);
                    }
                }

                return;
            }
        }

        if ( selectedActor == back )
        {
                Utils.makeLog("clicked back");
            setDisabled(true);
            startAnimation();
        }
        else if ( selectedActor == start )
        {
            setDisabled(true);


            startAnimation();
        }

    }
    @Override
    void draw(SpriteBatch batch)
    {

        animation.draw(batch);stage.draw();
    }


    @Override
    void update(float delta)
    {
        if ( animation.isScreenEnded() )
        {
            if ( selectedActor == start )
            {
                Core.stageplaying = currStage;
                states.clear();

            }
            else if ( selectedActor == back )
            {
                Utils.makeLog("testestsetestsetsetset");
                GameState currState = states.remove(states.size() - 1);
                states.get(states.size() - 1).startAnimation();
            }
        }
        else super.update(delta);
    }
}

class PlayState extends GameState
{
    World currWorld;
    PlayState(java.util.List<GameState> iStates) {
        super(iStates);

        sharedModels = new ArrayList<EntityModel>();
        sharedAnimationModels = new ArrayList<AnimationModel>();

        Sprite sprite = new Sprite((Texture) ResourceManager.getObjectFromResource("button"));
        BitmapFont font = (BitmapFont)ResourceManager.getObjectFromResource("font");
        ImageTextButton.ImageTextButtonStyle style1 = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(sprite), null, null, font);

        menuButton = new ImageTextButton("MENU", style1);

        worldCam = new OrthographicCamera(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.ASPECT_RATIO);
        worldCam.position.set(worldCam.viewportWidth / 2f, worldCam.viewportHeight / 2f, 0);
        worldCam.update();

        EntityModel model = new EntityModel((Texture)ResourceManager.getObjectFromResource("virus1"), "virus1");

        AnimationInfo virus1AnimationInfo = Utils.getFromJson("jsons/animations/virus1.json", AnimationInfo.class);
        AnimationModel virus1AnimationModel = AnimationFactory.createAnimationModelFromParsedJSON(virus1AnimationInfo);
        sharedAnimationModels.add(virus1AnimationModel);
        sharedModels.add(model);
        Texture bullet = (Texture) ResourceManager.getObjectFromResource("bullet");
        currWorld = WorldFactory.createWorldFromJSON("jsons/levels/first/map1.json");
        Table root = new Table();
        root.setFillParent(true);
        root.add().expandX();
        root.add(menuButton);
        root.row();
        root.add().expandX();
        root.add().expandY();

        Texture testtower1 = (Texture) ResourceManager.getObjectFromResource("testtower1");
        Texture testtower2 = (Texture) ResourceManager.getObjectFromResource("testtower2");
        Texture testtower3 = (Texture) ResourceManager.getObjectFromResource("testtower3");

        sharedAnimationModels.add(virus1AnimationModel);
        sharedModels.add(createModel(testtower1, "tower1"));
        sharedModels.add(createModel(bullet, "tower1Bullet"));
        sharedModels.add(createModel(testtower2, "tower2"));
        sharedModels.add(createModel(bullet, "tower2Bullet"));
        sharedModels.add(createModel(testtower3, "tower5"));
        sharedModels.add(createModel(bullet, "tower5Bullet"));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(new SpriteDrawable(new Sprite(
                (Texture) ResourceManager.getObjectFromResource("button"))), null, null, font);
        java.util.List<ShopUnit> shopUnits = new ArrayList<ShopUnit>();

        ImageTextButton.ImageTextButtonStyle style2 = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(new Sprite(
                testtower1)), null, null, font);

        ImageTextButton.ImageTextButtonStyle style3 = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(new Sprite(
                testtower2)), null, null, font);

        ImageTextButton.ImageTextButtonStyle style4 = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(new Sprite(
                testtower3)), null, null, font);
        Bullet bbullet = new Bullet(getModel("tower1Bullet"), null, 50, new Vector2(), new Vector2(0.3f, 0.3f), DIRECTION.RIGHT, ENTITYTYPE.BULLET);

       Utils.makeLog("tower11");
        ShopUnit image1 = new ShopUnit("500", style2, new Tower(getModel("tower1"), null, 1.0f, new Vector2(), new Vector2(1, 1), bbullet, ENTITYTYPE.TOWER));
       Utils.makeLog("tower22");
        ShopUnit image2 = new ShopUnit("500", style3, new Tower(getModel("tower2"), null, 1.0f, new Vector2(), new Vector2(1, 1), bbullet, ENTITYTYPE.TOWER));
       Utils.makeLog("tower33");
        ShopUnit image3 = new ShopUnit("500", style4, new Tower(getModel("tower5"), null, 1.0f, new Vector2(), new Vector2(1, 1), bbullet, ENTITYTYPE.TOWER));
        image1.addListener(new ShopUnitClick(image1));
        image2.addListener(new ShopUnitClick(image2));
        image3.addListener(new ShopUnitClick(image3));

        shopUnits.add(image1);
        shopUnits.add(image2);
        shopUnits.add(image3);
        currentHoldingTower = new Tower(null, null, 0, null, null, null, ENTITYTYPE.TOWER);
        worldInput = new PlayerInput(worldCam);
        image1.setSize(100,100);
        image2.setSize(100,100);
        image3.setSize(100,100);
        image1.setPosition(0, GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - image1.getHeight());
        image2.setPosition(100, GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - image2.getHeight());
        image3.setPosition(200, GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - image3.getHeight());
        shopUnits.add(image1);
        shopUnits.add(image2);
        shopUnits.add(image3);
        for( int a = 0; a < shopUnits.size(); a++ )
            stage.addActor(shopUnits.get(a));
        stage.addActor(root);


        entities = new java.util.ArrayList<Entity>();
        menuButton.addListener(this);



        multiplexer = new InputMultiplexer(worldInput, stage);
        Gdx.input.setInputProcessor(multiplexer);
    }
    EntityModel createModel(Texture texture, String modelName)
    {
        //no checking for now
        EntityModel newModel = new EntityModel(texture, modelName);
        return newModel;
    }
    EntityModel getModel(String modelToSearch)
    {
        for ( int a = 0; a < sharedModels.size(); a++ )
        {
            if ( sharedModels.get(a).getName().equals(modelToSearch) )
            {

                Utils.makeLog(modelToSearch);
                return sharedModels.get(a);
            }
        }
        return null;
    }
    @Override
    public void clicked (InputEvent event, float x, float y) {
        selectedActor = event.getListenerActor();
        if ( selectedActor == menuButton )
        {
            Window.WindowStyle windowStyle = new Window.WindowStyle((BitmapFont)ResourceManager.getObjectFromResource("font"), Color.WHITE, new SpriteDrawable(new Sprite((Texture)ResourceManager.getObjectFromResource("splash"))));
            PauseView pause = new PauseView("", windowStyle);
            pause.setFillParent(true);
            pause.setPosition(0, 0);
            pause.setVisible(true);
            stage.addActor(pause);
        }
    }
    PauseView view;
    ImageTextButton menuButton;

    Camera worldCam;
    java.util.List<Entity> entities;
    @Override
    void draw(SpriteBatch batch)
    {
        batch.begin();
        currWorld.draw(batch);
        for ( int a = 0; a < entities.size(); a++ )
        {
            if ( entities.get(a).isVisible() )
                entities.get(a).draw(batch);
        }
        if ( !Utils.isObjectNull(ShopUnitClick.getCurrentClickedUnit()) )
            currentHoldingTower.draw(batch);
        batch.end();
        stage.draw();
    }

    InputMultiplexer multiplexer;
    PlayerInput worldInput;
    Tower currentHoldingTower;
    java.util.List<EntityModel> sharedModels;
    java.util.List<AnimationModel> sharedAnimationModels;
    float accumulated_data = 0;
    @Override
    void update(float delta) {
        if (animation.isScreenEnded()) {

        }
        else
        {
            super.update(delta);
           // float delta = Gdx.graphics.getDeltaTime();
            accumulated_data += delta;
            if (accumulated_data >= 1.0f) {
                accumulated_data = 0;
                Utils.makeLog("test");
                entities.add(new Virus(getModel("virus1"), new Animation(1, 1, sharedAnimationModels.get(0)), 11, new Vector2(GAME_GLOBALS.GAME_CAMERA_WIDTH - 1, Utils.getRand(5)), new Vector2(1, 1), DIRECTION.LEFT, ENTITYTYPE.VIRUS));

            }
            for ( int i = 0; i < entities.size(); i++ ) {
                Entity currentEntity = entities.get(i);
                if (currentEntity.getType() == ENTITYTYPE.TOWER) {
                    Tower tower = (Tower) currentEntity;
                    if (tower.isReadyToFire()) {
                        for (int a = 0; a < entities.size(); a++) {
                            Entity enemy = entities.get(a);
                            if (enemy.getType() == ENTITYTYPE.VIRUS && enemy.getAlignment() == tower.getAlignment() && enemy.getPostiion().x > tower.getPostiion().x) {
                                tower.fire();
                                tower.reload();
                                break;
                            }
                        }
                    }
                    java.util.List<Bullet> refbullets = tower.getBullets();
                    for (int a = 0; a < refbullets.size(); a++) {
                        Bullet bullet = refbullets.get(a);
                        for (int b = 0; b < entities.size(); b++) {
                            Entity enemy = entities.get(b);
                            if (enemy.getType() == ENTITYTYPE.VIRUS && Utils.isAligned(enemy.getAlignment(), bullet.getAlignment())) {
                                if (Utils.has2DCollision(bullet.getSprite(), enemy.getSprite())) {
                                    entities.remove(enemy);
                                    refbullets.remove(bullet);
                                    a--;
                                    break;
                                }

                            }
                        }
                    }
                }
                currentEntity.update(delta);
            }

            Vector3 worldUnitCoord;

            if ( !Utils.isObjectNull(ShopUnitClick.getCurrentClickedUnit()) )
            {
                if ( currentHoldingTower != ShopUnitClick.getCurrentClickedUnit().getUnit() )
                    currentHoldingTower = ShopUnitClick.getCurrentClickedUnit().getUnit();
                worldUnitCoord = worldCam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                currentHoldingTower.getSprite().setPosition(worldUnitCoord.x - 0.5f, worldUnitCoord.y - 0.5f);
            }

            worldUnitCoord = worldInput.getUnprojectedCoord();
            Vector2 coord = new Vector2((int) worldUnitCoord.x, (int)worldUnitCoord.y);

            if ( coord.x >= 0 && coord.y >= 0 && coord.x < worldCam.viewportWidth && coord.y < worldCam.viewportHeight - 1 )
            {
                if ( !Utils.isObjectNull(ShopUnitClick.getCurrentClickedUnit()) )
                {
                    currentHoldingTower.getSprite().setPosition(coord.x, coord.y);
                    entities.add(new Tower(currentHoldingTower));
                    ShopUnitClick.resetClick();
                }
            }
        }
    }
}

