import java.awt.Button;
import java.awt.event.*;

playPanel playPanel;
Button Button;
ActionListener ActionListener;
Button b;

void setup() {
  b = new Button("click me");
  
  playPanel = new playPanel();
  playPanel.init();
  playPanel.x =50; 
  playPanel.y =200;
  size(640, 480);
  add(b);
  b.setActionCommand("Good Morning"); 
  //b.addActionListener(this);
  
  
}

void draw() {
  
  playPanel.display();
  //println(b.ActionListener() );
  
}

class playPanel {
  int x;
  int y;
  PImage[] pic = new PImage[7];
  String[] nameFile = new String[7];

  void init() {
    nameFile[0] = "Button First.png";                
    nameFile[1] = "Button Rewind.png";        
    nameFile[2] = "Button Play.png";
    nameFile[3] = "Button Stop.png";
    nameFile[4] = "Button Pause.png";
    nameFile[5] = "Button Fast Forward.png";
    nameFile[6] = "Button Last.png";
    for (byte i=0;i<7;i++) {
      pic[i]=loadImage(nameFile[i]);
    }
  }
  void display() {
    for (byte i=0;i<7;i++) {
      image(pic[i], x+40*i, y, 30, 30);
    }
  }
}

void keyPressed() {
  println("---");
  }

