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
  cam = new IPCapture(this, "http://192.168.100.176:8080/?action=stream", "", "");
  cam.start();
  opencv = new OpenCV( this );
  opencv.loadImage("e:\\_3.jpg");
  opencv.cascade( "C:\\Program Files\\OpenCV\\data\\haarcascades\\haarcascade_frontalface_alt.xml" );  
  
}

void draw() {
  if (cam.isAvailable()) {
    cam.read();
	prevFrame = cam.get(0,0,width,height);
	opencv.allocate(prevFrame.width, prevFrame.height);
	opencv.copy(prevFrame.pixels, prevFrame.width, 0,0, prevFrame.width, prevFrame.height, 0,0, prevFrame.width, prevFrame.height);
	opencv.convert(GRAY);
	Rectangle[] faces = opencv.detect( 1.2, 2, OpenCV.HAAR_DO_CANNY_PRUNING, 50, 50 );
	if (faces.length>0) println(n);
	//image( opencv.image(), 0, 0 ); 
	image( cam, 0, 0 ); 
	noFill();
    stroke(255,0,0);
	
    for( int i=0; i<faces.length; i++ ) {
      //opencv.ROI( faces[i].x, faces[i].y, faces[i].x+faces[i].width, faces[i].y+faces[i].height );
      //opencv.copy(opencv.image(),faces[i].x, faces[i].y, faces[i].width, faces[i].height,faces[i].x, faces[i].y, faces[i].width, faces[i].height);
       n++;
	   //image( opencv.image(),faces[i].x, faces[i].y, faces[i].x+faces[i].width, faces[i].y+faces[i].height ); 
	   //image(opencv.image(), 0,0); 
	   tek = cam.get(faces[i].x,faces[i].y,faces[i].width,faces[i].height);
	   //image(tek,0,0);
       tek.save("e:\\cap\\"+n+".jpg");            
       rect( faces[i].x-40, faces[i].y-40, faces[i].width+60, faces[i].height+60 ); 
	   tek = null;
    }
    //image(cam,0,0);
  }
}

void keyPressed() {
  if (key == ' ') {
    if (cam.isAlive()) cam.stop();
    else cam.start();
  }
}
