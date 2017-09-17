package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

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
        Gdx.input.setInputProcessor(stage);

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
    void setDisabled(boolean disable) {}
    void startAnimation()
    {
        if ( animation.isScreenEnded() )
            stage.addAction(Actions.fadeIn(1.0f));
        else
            stage.addAction(Actions.fadeOut(1.0f));
        setDisabled(true);
        animation.start();
        Gdx.input.setInputProcessor(stage);
    }
    protected Actor selectedActor;
    Stage stage;
}
class MenuState extends GameState {

    @Override
    void setDisabled(boolean disable)
    {
        if ( disable )
        {
            newgame.clearListeners();
            loadgame.clearListeners();
        }
        else
        {
            loadgame.addListener(this);
            newgame.addListener(this);
        }
    }
    @Override
    public void clicked (InputEvent event, float x, float y) {
        selectedActor = event.getListenerActor();
        if ( selectedActor == newgame )
        {
            setDisabled(true);
            startAnimation();
        }
        else if ( selectedActor == loadgame )
        {
            setDisabled(true);
            startAnimation();
        }
    }

    MenuState(java.util.List<GameState> iStates)
    {
        super(iStates);

        background = new Sprite((Texture) ResourceManager.getObjectFromResource("mainmenu"));
        background.setSize(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.GAME_CAMERA_HEIGHT);
        Sprite buttonSprite = new Sprite((Texture) ResourceManager.getObjectFromResource("button"));
        BitmapFont font = BitmapFontFactory.createdBitmapFrontFromFile(Gdx.files.internal("graphics/fonts/arial.ttf"), 32);
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(buttonSprite), null, null, font);
        GlyphLayout layout = new GlyphLayout(font, "New Game");

        newgame = new ImageTextButton("New Game", style);
        loadgame = new ImageTextButton("Load Game", style);
        float w = layout.width, h = 32;
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

    }
    @Override
    void start()
    {
        super.start();
        stage.addAction(Actions.fadeOut(1.0f));

    }
    @Override
    void draw(SpriteBatch batch) {

        animation.draw(batch);
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
        splashscreen = new Sprite((Texture) ResourceManager.getObjectFromResource("splash"));
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
class LoadGameState extends GameState {
    public void clicked (InputEvent event, float x, float y)
    {
        selectedActor = event.getListenerActor();
        if ( selectedActor == back )
        {
            startAnimation();
            setDisabled(true);
        }
        else if ( selectedActor == load )
        {
            startAnimation();
            setDisabled(true);
        }
    }

    LoadGameState(java.util.List<GameState> iStates) {
        super(iStates);
        Sprite listSprite = new Sprite((Texture) ResourceManager.getObjectFromResource("splash"));
        listSprite.setSize(GAME_GLOBALS.DESKTOP_SCREEN_WIDTH, GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - 32);
        BitmapFont font = BitmapFontFactory.createdBitmapFrontFromFile(Gdx.files.internal("graphics/fonts/arial.ttf"), 32);
        List.ListStyle style = new List.ListStyle(font, Color.GRAY, Color.BLACK, new SpriteDrawable(listSprite));
        Sprite buttonSprite = new Sprite((Texture) ResourceManager.getObjectFromResource("button"));

        ImageTextButton.ImageTextButtonStyle buttonStyle = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(buttonSprite), null, null, font);
        load = new ImageTextButton("Load", buttonStyle);
        back = new ImageTextButton("Back", buttonStyle);
        accountList = new List<String>(style);

        accountList.setSize(GAME_GLOBALS.DESKTOP_SCREEN_WIDTH, GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT - 32);
        accountList.setPosition(32, 0);
        SaveList list = Utils.deserialize("savestate.txt", SaveList.class);
        Array<String> saves = new Array<String>();

        for ( int a = 0; a < list.list.size(); a++ )
        {
            saves.add(list.list.get(a).name);
        }
        accountList.setItems(saves);
        int w = GAME_GLOBALS.DESKTOP_SCREEN_WIDTH / 2;
        load.setSize(w, 32);
        load.setPosition(0, 0);
        back.setSize(w, 32);
        back.setPosition(w, 0);
        stage.addActor(accountList);
        stage.addActor(back);
        stage.addActor(load);
        background = new Sprite((Texture) ResourceManager.getObjectFromResource("mainmenu"));
        background.setSize(GAME_GLOBALS.GAME_CAMERA_WIDTH, GAME_GLOBALS.GAME_CAMERA_HEIGHT);
        animation.addSprite(background);


    }

    @Override
    void draw(SpriteBatch batch) {
        animation.draw(batch);
        stage.draw();

    }

    @Override
    public void setDisabled(boolean disable)
    {
        if ( disable )
        {
            accountList.clearListeners();
            back.clearListeners();
            load.clearListeners();
        }
        else
        {
            accountList.addListener(this);
            back.addListener(this);
            load.addListener(this);
        }
    }
    @Override
    void update(float delta) {
        if ( animation.isScreenEnded() )
        {
            if ( selectedActor == back )
            {
                states.get(states.size() - 2).startAnimation();
                states.remove(states.size() - 1);

            }
            else if ( selectedActor == load )
            {
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
            resultDialog.show(stage);
            BitmapFont font = BitmapFontFactory.createdBitmapFrontFromFile(Gdx.files.internal("graphics/fonts/arial.ttf"), 32);
            Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
            SaveList lists = Utils.deserialize("savestate.txt", SaveList.class);
            if (Utils.isObjectNull(lists)) {
                lists = new SaveList();
            }
            if ( lists.isExist(input.getText()) )
            {
                resultDialog.setText(getDecidedName() + " already exist! try another name!");
            }
            else
            {
                resultDialog.setText("Account created! press okay to continue");
                lists.add(new SaveInfo(getDecidedName()));
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
        Sprite listSprite = new Sprite((Texture) ResourceManager.getObjectFromResource("splash"));
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
        label.setPosition(0, ( GAME_GLOBALS.DESKTOP_SCREEN_HEIGHT / 2 ) - 32);
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
        resultDialog = new ModalDialog("Result", dialogSkin,"splash");
        resultDialog.setSize(500,500);
        resultDialog.setPosition(100,100);
        setDisabled(true);


    }

    @Override
    void draw(SpriteBatch batch)
    {
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
            if ( selectedActor == cancel )
            {
                GameState currState = states.remove(states.size() - 1);
                states.get(states.size() - 1).startAnimation();
            }
            else if ( selectedActor == create )
            {

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

    PlayerProfileState(java.util.List<GameState> iStates) {
        super(iStates);

        Sprite buttonSprite = new Sprite((Texture) ResourceManager.getObjectFromResource("button"));
        BitmapFont font = BitmapFontFactory.createdBitmapFrontFromFile(Gdx.files.internal("graphics/fonts/arial.ttf"), 32);
        SpriteDrawable sprdraw = new SpriteDrawable(buttonSprite);
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(sprdraw, null, null, font);

        playGame = new ImageTextButton("Play game", style);
        almanac = new ImageTextButton("Almanac", style);
        settings = new ImageTextButton("Settings", style);
        quit = new ImageTextButton("Quit", style);

        Label.LabelStyle lblStyle = new Label.LabelStyle(font, Color.WHITE);
        money = new Label("insert money here", lblStyle);


        Table table1 = new Table();
        table1.setFillParent(true);
        table1.top().left();
        table1.add(money).expand().top().left().row();
        table1.add(playGame).row();
        table1.add(almanac).row();
        table1.add(settings).row();
        table1.add(quit).row();
        table1.add().expand();
        stage.addActor(table1);
        playGame.addListener(this);
        almanac.addListener(this);
        settings.addListener(this);
        quit.addListener(this);
    }
    Label money;
    @Override
    public void clicked (InputEvent event, float x, float y) {
        selectedActor = event.getListenerActor();
        setDisabled(true);
        startAnimation();
        if ( selectedActor == playGame )
        {
            Utils.makeLog("plagegame");
        }
        else if ( selectedActor == almanac )
        {
            Utils.makeLog("almanac");
        }
        else if ( selectedActor == settings )
        {
            Utils.makeLog("settings");
        }
        else if ( selectedActor == quit )
        {
            Utils.makeLog("clicked quit");
        }
    }
    @Override
    void draw(SpriteBatch batch) {
        stage.draw();
    }

    @Override
    void update(float delta)
    {
        if ( animation.isScreenEnded() )
        {
            if ( selectedActor == playGame )
            {

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
                states.get(states.size() - 1).startAnimation();
            }
        }
        else super.update(delta);
    }


    ImageTextButton playGame;
    ImageTextButton almanac;
    ImageTextButton settings;
    ImageTextButton quit;
}
class PlayerSettingState extends GameState {

    PlayerSettingState(java.util.List<GameState> iStates) {
        super(iStates);
    }

    @Override
    public void clicked (InputEvent event, float x, float y) {
        selectedActor = event.getListenerActor();


    }
    @Override
    void draw(SpriteBatch batch) {
        stage.draw();
    }


    @Override
    void setDisabled(boolean disable)
    {
        if ( disable )
        {
        }
        else
        {
        }
    }

    @Override
    void update(float delta)
    {
        if ( animation.isScreenEnded() )
        {
        }
        else super.update(delta);
    }


}
class PlayerAlmanacState extends GameState {

    PlayerAlmanacState(java.util.List<GameState> iStates)
    {
        super(iStates);
        virusesAndDescriptions = new java.util.ArrayList<ImageText>();
        Table inner = new Table();

        Sprite buttonSprite = new Sprite((Texture) ResourceManager.getObjectFromResource("button"));

        ScrollPane scrollPane = new ScrollPane(inner);
        BitmapFont font = (BitmapFont) ResourceManager.getObjectFromResource("font");
        Label.LabelStyle lblStyle = new Label.LabelStyle(font,Color.WHITE);

        SpriteDrawable tableBackgroundWhenClicked = new SpriteDrawable(new Sprite((Texture) ResourceManager.getObjectFromResource("selectedItemBackgroundAlmanac")));
        for ( int a = 0; a < 20; a++ )
        {
            ImageText virusAndDescription = new ImageText(new SpriteDrawable(buttonSprite),32, 32, "Virus" + Integer.toString(a+1), lblStyle, DIRECTION.RIGHT);
            virusAndDescription.setBackgroundWhenClicked(tableBackgroundWhenClicked);
            virusAndDescription.getTable().addListener(this);
            inner.add(virusAndDescription.getTable()).fill();
            inner.add(virusAndDescription.getFiller()).expandX().fill();
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

            setDisabled(false);
            startAnimation();


        }
        else {
            ImageText selected = null;
            for (int a = 0; a < virusesAndDescriptions.size(); a++) {
                ImageText curr = virusesAndDescriptions.get(a);
                if (curr.getTable() == selectedActor) {
                    curr.setSelected(true);
                    selected = curr;
                    continue;
                } else if (virusesAndDescriptions.get(a).isSelected()) {
                    curr.setSelected(false);
                    if (!Utils.isObjectNull(selected))
                        break;
                }
            }

            AlmanacData data = new AlmanacData();
            data.cause = "cause";
            data.name = "name";
            data.definition = "definition";
            data.howtoremove = "howtoremove";
            data.prevention = "prevention";

            Window.WindowStyle winStyle = new Window.WindowStyle((BitmapFont)ResourceManager.getObjectFromResource("font"), Color.WHITE, new SpriteDrawable(new Sprite((Texture)ResourceManager.getObjectFromResource("splash"))));
            view = new AlmanacDataView("", winStyle, data);
            view.setFillParent(true);
            view.setPosition(0, 0);
            stage.addActor(view);
        }

    }
AlmanacDataView view;
    AlmanacDialog dialog;
    @Override
    void draw(SpriteBatch batch) {
        stage.draw();
    }

    @Override
    void setDisabled(boolean disable)
    {
        if ( disable )
        {
        }
        else
        {
        }
    }

    @Override
    void update(float delta)
    {
        if ( animation.isScreenEnded() )
        {
            Utils.makeLog("screenEnded");
        }
        else
            super.update(delta);
    }


}
class PlayerSkillTreeState extends GameState
{
    PlayerSkillTreeState(java.util .List<GameState> iStates) {
        super(iStates);
    }
    int points;
    void setPoints(int iPoints)
    {
        points = iPoints;
    }
    @Override
    public void clicked (InputEvent event, float x, float y) {
        selectedActor = event.getListenerActor();


    }
    @Override
    void draw(SpriteBatch batch)
    {
        stage.draw();
    }


    @Override
    void setDisabled(boolean disable)
    {
        if ( disable )
        {
        }
        else
        {
        }
    }

    @Override
    void update(float delta)
    {
        if ( animation.isScreenEnded() )
        {
        }
        else super.update(delta);
    }
}

