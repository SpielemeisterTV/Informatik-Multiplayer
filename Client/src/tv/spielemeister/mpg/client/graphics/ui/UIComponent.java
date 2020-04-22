package tv.spielemeister.mpg.client.graphics.ui;

import tv.spielemeister.mpg.client.graphics.GameWindow;

import java.awt.*;

public abstract class UIComponent {

    public final boolean transparent; // Defines, if the world should still be rendered or not

    private boolean updated = false;

    protected UIComponent(boolean transparent) {
        this.transparent = transparent;
    }

    public abstract void render(Graphics2D graphics);

    public void show(GameWindow window){
        if(window.UI != this) {
            window.UI = this;
            updated = true;
        }
    }

    public void hide(GameWindow window){
        if(window.UI == this)
            window.UI = null;
        updated = false;
    }



}
