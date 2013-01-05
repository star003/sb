
import controlP5.*;
ControlP5 cp5;
int myColor = color(255);
int c1,c2;
float n,n1;

void setup() {
  size(400,600);
  noStroke();
  cp5 = new ControlP5(this);

  cp5.addButton(">>")
     .setValue(0)
     .setPosition(100,100)
     .setSize(50,25)
     ;
 }

void draw() {
  background(myColor);
  myColor = lerpColor(c1,c2,n);
  n += (1-n)* 0.1; 
}

public void controlEvent(ControlEvent theEvent) {
  println(theEvent.getController().getName());
  n = 0;
}




