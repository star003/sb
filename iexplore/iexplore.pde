PImage img;
PFont f;

void setup() {
size(640,480);
frameRate(1);
img = loadImage("19116_03.jpg");
f = loadFont("Courier-12.vlw");
textFont(f,24);
}

void draw() {
  image(img,0,0,640,480);
  fill(0,0,0);
  text("у вас нет работы ? скоро ее может вообще не быть...", 10,320,600,100);
}
