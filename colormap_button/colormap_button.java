import processing.core.*; 
import processing.xml.*; 

import java.applet.*; 
import java.awt.*; 
import java.awt.image.*; 
import java.awt.event.*; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class colormap_button extends PApplet {

// an unusual way to create shaped buttons with Processing

PImage button;        //image of the buttons (visible)
PImage buttoncolmap;  //colormap of the buttons (hidden)

String[] buttonstring={"quadrat","thumb up","smile","envelope",
                        "flower","rectangle","oval","fivestar",
                        "circle","four star","banner","boom",
                        "textfield",""}; //the names of the buttons
int[] buttoncolor={0xFF0000FF,0xFF00FFFF,0xFF00FF00,0xFFFFFF00,
                        0xFFFF0000,0xFFFF00FF,0xFF9900CC,0xFFFF6600,
                        0xFFFF99CC,0xFF663333,0xFF66FFCC,0xFF99FF00,
                        0xFFFFFF66,0xFFFFFFFF}; //the colors of the buttons

String textfield="";

PFont font;

public void setup ()
  {
    size(800,600,JAVA2D);
    smooth();
    noLoop();
    //noCursor();
    font=loadFont("CourierNewPS-BoldMT-48.vlw");
    textFont(font);
    button=loadImage("button.png");
    buttoncolmap=loadImage("colormap.png");     
  }
  
public void draw()
  {
  background(button);
  fill(0);
  textAlign(CENTER);
  text(textfield,400,205);
  }
  
public void mouseReleased()
  {
    int testcolor=0;
    testcolor=buttoncolmap.get(mouseX,mouseY); //get the color in the hidden image
    //println("0x"+hex(testcolor));
    
    for(int i=0;i<buttonstring.length;++i) //check the color and copy the name of the button
      {
        if(testcolor==buttoncolor[i])
          textfield=buttonstring[i];
      }
    
    redraw();
  }
    

  static public void main(String args[]) {
    PApplet.main(new String[] { "--present", "--bgcolor=#666666", "--stop-color=#cccccc", "colormap_button" });
  }
}
