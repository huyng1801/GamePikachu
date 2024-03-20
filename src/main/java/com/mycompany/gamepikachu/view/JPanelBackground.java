package com.mycompany.gamepikachu.view;
import com.mycompany.gamepikachu.utils.Utils;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class JPanelBackground extends JPanel{
    protected Image backgroundImage = null;
    public JPanelBackground(){
        this(null);
    }
    public JPanelBackground(String imagePath){
        setOpaque(false);
        if(imagePath!=null)
            this.backgroundImage = new ImageIcon( getClass().getResource(imagePath) ).getImage();
        else Utils.debug(getClass(),"Khong the tai hinh anh!");
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if(backgroundImage!=null){
            int height,width;
            height = this.getSize().height;
            width = this.getSize().width;
            g.drawImage(backgroundImage, 0, 0, width,height,this);
        }
    }
    public void setBackgroundImage(String imagePath){
        if(imagePath!=null)
            this.backgroundImage = new ImageIcon( getClass().getResource(imagePath) ).getImage();
        else Utils.debug(getClass(),"Khong the tai hinh anh!");
        this.repaint();
    }
}
 
 