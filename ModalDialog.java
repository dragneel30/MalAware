package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import java.awt.Image;

/**
 * Created by Lorence on 8/12/2017.
 */


public class ModalDialog extends Dialog {

    ModalDialog(String title, Skin skin, String windowStyleName)
    {
        super(title, skin, windowStyleName);
        BitmapFont font = BitmapFontFactory.createdBitmapFrontFromFile(Gdx.files.internal("graphics/fonts/arial.ttf"), 32);
        Sprite buttonSprite = new Sprite((Texture) ResourceManager.getObjectFromResource("button"));
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(buttonSprite),null,null,font);

        success = new ImageTextButton("Success", style);
        abort = new ImageTextButton("Abort", style);
        int w = ((int)getWidth()) / 2;
        success.setSize(w, 32);
        abort.setSize(w, 32);
        abort.setPosition(w, 0);

        Label.LabelStyle lblstyle = new Label.LabelStyle(font, Color.WHITE);
        label = new Label("", lblstyle);
        label.setPosition(50,50);
        button(success,DialogCode.SUCCESS);
        button(abort,DialogCode.ABORT);

        code = null;
        text(label);
    }
    void setText(String text)
    {
        label.setText(text);
    }
    Label label;

    DialogCode code;
    void setDialogCode(DialogCode newCode) { code = newCode; }
    DialogCode getCode() { return code; }
    enum DialogCode
    {
        SUCCESS(1), ABORT(2);
        int value;
        DialogCode(int iValue) { value = iValue; }
        int getValue() { return value; }
    }
    @Override
    protected void result(Object object)
    {

        DialogCode resultCode = (DialogCode) object;
        setDialogCode(resultCode);
        remove();
    }

    ImageTextButton success;
    ImageTextButton abort;
}

class ModalDialogExtended extends Dialog
{
    public ModalDialogExtended(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
    }

    enum DialogCode
    {
        SUCCESS(1), ABORT(2);
        int value;
        DialogCode(int iValue) { value = iValue; }
        int getValue() { return value; }
    }

    DialogCode code;
    void setDialogCode(DialogCode newCode) { code = newCode; }
    DialogCode getCode() { return code; }
    @Override
    protected void result(Object object)
    {
        DialogCode resultCode = (DialogCode) object;
        setDialogCode(resultCode);
        remove();
    }
}

class AlmanacDatas
{
    public AlmanacDatas()
    {
        datas = new java.util.ArrayList<AlmanacData>();
    }

    java.util.List<AlmanacData> datas;
}
class AlmanacData
{
    String cause;
    String definition;
    String name;
    String howtoremove;
    String prevention;
}

class AlmanacDataView extends Window
{
    public AlmanacDataView(String title, WindowStyle style, AlmanacData data) {
        super(title, style);
        ImageButton.ImageButtonStyle ibstyle = new ImageButton.ImageButtonStyle(new SpriteDrawable(new Sprite((Texture)ResourceManager.getObjectFromResource("closeButton"))), null, null, null, null, null);
        ImageButton closeButtonImage = new ImageButton(ibstyle);

        closeButtonImage.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });

        Label.LabelStyle lblStyle = new Label.LabelStyle(style.titleFont,Color.WHITE);

        Label cause = new Label(data.cause, lblStyle);
        Label definition = new Label(data.definition, lblStyle);
        Label name = new Label(data.name, lblStyle);
        Label howtoremove = new Label(data.howtoremove, lblStyle);
        Label prevention = new Label(data.prevention, lblStyle);

        left().top();
        add().expandX();
        add(closeButtonImage);
        row();
        add(new Label("Name: ", lblStyle)).left();
        row();
        add(name).left();
        row();
        add(new Label("Cause: ", lblStyle)).left();
        row();
        add(cause).left();
        row();
        add(new Label("Prevention: ", lblStyle)).left();
        row();
        add(prevention).left();
        row();
        add(new Label("How to remove: ", lblStyle)).left();
        row();
        add(howtoremove).left();
    }
}

class AlmanacDialog extends ModalDialogExtended
{
    AlmanacDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle(new SpriteDrawable(new Sprite((Texture)ResourceManager.getObjectFromResource("closeButton"))), null, null, null, null, null);
        closeButtonImage = new ImageButton(style);
        Table inner = new Table();
        ScrollPane scrollPane = new ScrollPane(inner);

        BitmapFont font = BitmapFontFactory.createdBitmapFrontFromFile(Gdx.files.internal("graphics/fonts/arial.ttf"), 32);
        Label.LabelStyle lblstyle = new Label.LabelStyle(font, Color.WHITE);
        label = new Label("ENter shit", lblstyle);
        Label label1 = new Label("sdfasdf", lblstyle);
        Label label2 = new Label("sdfasdf", lblstyle);
        Label label3 = new Label("sdfasdf", lblstyle);
        Label label4 = new Label("sdfasdf", lblstyle);
        Label label5 = new Label("sdfasdf", lblstyle);
        Label label6 = new Label("sdfasdf", lblstyle);
        Label label7 = new Label("sdfasdf", lblstyle);
        Label label8 = new Label("sdfasdf", lblstyle);
        Label label9 = new Label("sdfasdf", lblstyle);
        Label label10 = new Label("sdfasdf", lblstyle);
        Label label11 = new Label("sdfasdf", lblstyle);

        inner.add(label);
        inner.row();
        inner.add(label1);
        inner.row();
        inner.add(label2);
        inner.row();
        inner.add(label3);
        inner.row();
        inner.add(label4);
        inner.row();
        inner.add(label5);
        inner.row();
        inner.add(label6);
        inner.row();
        inner.add(label7);
        inner.row();
        inner.add(label8);
        inner.row();
        inner.add(label9);
        inner.row();
        inner.add(label10);
        inner.row();
        inner.add(label11);
        inner.row();
        inner.add(label10);
        inner.row();
        inner.add(label11);
        inner.row();
        inner.add(label10);
        inner.row();
        inner.add(label11);
        Table content = getContentTable();
getContentTable().setDebug(true);
        getContentTable().add(scrollPane);
       // content.setDebug(true);
        //content.add(scrollPane);
        //content.setFillParent(true);
        //content.add().expandX();
        button(closeButtonImage, 1);
    }

    Label label;

    ImageButton closeButtonImage;

    @Override
    protected void result(Object object)
    {
        Utils.makeLog("testt");/*
        DialogCode resultCode = (DialogCode) object;
        setDialogCode(resultCode);
        remove();*/
    }
}
