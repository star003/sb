import processing.core.*; 
import processing.xml.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class iexplore extends PApplet {

PImage img;
PFont f;

public void setup() {
size(640,480);
frameRate(1);
img = loadImage("19116_03.jpg");
f = loadFont("Courier-12.vlw");
textFont(f,24);
}

public void draw() {
  image(img,0,0,640,480);
  fill(0,0,0);
  text("\u0443 \u0432\u0430\u0441 \u043d\u0435\u0442 \u0440\u0430\u0431\u043e\u0442\u044b ? \u0441\u043a\u043e\u0440\u043e \u0435\u0435 \u043c\u043e\u0436\u0435\u0442 \u0432\u043e\u043e\u0431\u0449\u0435 \u043d\u0435 \u0431\u044b\u0442\u044c...", 10,320,600,100);
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "iexplore" });
  }
}
