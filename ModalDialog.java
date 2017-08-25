package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
