package tv.spielemeister.mpg.client.input;

import tv.spielemeister.mpg.client.graphics.GameWindow;
import tv.spielemeister.mpg.engine.world.Vector;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

public class InputHandler implements MouseInputListener, KeyListener {

    private Component parent;

    public int mouseX = 0, mouseY = 0;

    public InputHandler(Component parent){
        this.parent = parent;
        parent.addMouseMotionListener(this);
        parent.addMouseListener(this);
        parent.addKeyListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT)
            GameWindow.renderLocation.add(new Vector(1, 0));
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
            GameWindow.renderLocation.add(new Vector(-1, 0));
        if(e.getKeyCode() == KeyEvent.VK_DOWN)
            GameWindow.renderLocation.add(new Vector(0, 1));
        if(e.getKeyCode() == KeyEvent.VK_UP)
            GameWindow.renderLocation.add(new Vector(0, -1));
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
