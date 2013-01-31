import ipcapture.*;
import hypermedia.video.*;
import java.awt.Rectangle;

OpenCV opencv;

IPCapture cam;
PImage prevFrame,tek,cr;
long n = 0;
long x1 = 20;
boolean ps = false;
float sizeFactor=3.5;

void setup() {
  size(640,480);
  background(1);
  cam = new IPCapture(this, "http://192.168.100.176:8080/?action=stream", "", "");
  cam.start();
  opencv = new OpenCV( this );
  opencv.loadImage("e:\\_3.jpg");
  opencv.cascade( "C:\\Program Files\\OpenCV\\data\\haarcascades\\haarcascade_frontalface_alt.xml" );  
  
}

void draw() {

  if (cam.isAvailable()) {
    cam.read();
	image( cam, 0, 0 );
//	   tek = cam.get(faces[i].x,faces[i].y,faces[i].width,faces[i].height);
	   int d = day();
	   int m = month();
	   int y = year();
	   int mn= minute();
	   int h = hour();
	   int s = second();
//	   String st= str(y)+"-"+str(m)+"-"+str(d)+"_"+str(h)+"-"+str(m)+"-"+str(s)+"_"+str(n);
 //     tek.save("e:\\cap\\"+st+".jpg");            
	   tek = null;
	   
    }
  }


void keyPressed() {
  if (key == ' ') {
    if (cam.isAlive()) cam.stop();
    else cam.start();
  }
}
