package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.*;

import javax.swing.Box;

/**
 * Created by Lorence on 9/14/2017.
 */


public class ImageText {

    public ImageText(SpriteDrawable drawable, float width, float height, String lblText, Label.LabelStyle lblStyle, DIRECTION lblPos) {
        image = new Image(drawable);
        label = new Label(lblText, lblStyle);
        selected = false;
        image.setSize(width, height);

        table = new Table();

        if (lblPos == DIRECTION.RIGHT) {
            table.add(image);
            table.add(label);
        } else if (lblPos == DIRECTION.LEFT) {
            table.add(label);
            table.add(image);
        } else if (lblPos == DIRECTION.DOWN) {
            table.add(image).left();
            table.row();
            table.add(label);
        } else if (lblPos == DIRECTION.UP) {
            table.add(label);
            table.row();
            table.add(image);
        }

        filler = new Table();
    }

    Table filler;

    public Table getFiller() {
        return filler;
    }

    public String getText() {
        return label.getText().toString();
    }

    public Table getTable() {
        return table;
    }

    public Cell<Table> addWithFillerTo(Table container, DIRECTION fillerPosition) {

        if ( fillerPosition == DIRECTION.LEFT )
        {
            addTo(container).fill();
            return container.add(filler).expandX().fill();

        }
        else if ( fillerPosition == DIRECTION.DOWN )
        {
            addTo(container).fill();
            container.row();
            return container.add(filler).fill();
        }
        else if ( fillerPosition == DIRECTION.RIGHT )
        {
            container.add(filler).expandX().fill();
            return addTo(container).fill();
        }
        else if ( fillerPosition == DIRECTION.UP )
        {
            container.add(filler).expandY().fill();
            container.row();
            return addTo(container).fill();
        }

        return null;
    }

    public Cell<Table> addTo(Table container)
    {
        return container.add(table);
    }

    public void addClickListener(ClickListener listener)
    {
        table.addListener(listener);
        filler.addListener(listener);
    }

    public void setBackgroundWhenClicked(SpriteDrawable iBackgroundWhenClicked)
    {
        backgroundWhenClicked = iBackgroundWhenClicked;
    }
    public void setSelected(boolean select)
    {
        selected = select;
        if ( select ) {
            if ( !Utils.isObjectNull(filler) ) {

                filler.setBackground(backgroundWhenClicked);

            }
            table.setBackground(backgroundWhenClicked);

            Utils.makeLog("shaded");
        }
        else
        {
            if ( !Utils.isObjectNull(filler) )
            {
                filler.setBackground((SpriteDrawable)null);
                /////////// remove background filler
            }
            table.setBackground((SpriteDrawable)null);
            // remove background table
        }

    }


    public boolean isEqual(Actor actor)
    {
        return actor == table || actor == filler;
    }

    public boolean isSelected()
    {
        return selected;
    }
    boolean selected;

    SpriteDrawable backgroundWhenClicked;

    private Table table;
    private Image image;
    private Label label;

}
