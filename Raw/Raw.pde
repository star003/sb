import codeanticode.gsvideo.*;
import hypermedia.video.*;
import java.awt.Rectangle;

long n = 0;
long x1 = 20;
boolean ps = false;
GSMovie movie;
OpenCV opencv;
PImage prevFrame,tek,cr;
float sizeFactor=3.5;

//PFont font;

void setup() {
  size(1000, 600);
  frameRate(10);
  movie = new GSMovie(this, "2012-11-12.avi");
  movie.play();
  opencv = new OpenCV( this );
  opencv.loadImage("e:\\_3.jpg");
  opencv.cascade( "C:\\Program Files\\OpenCV\\data\\haarcascades\\haarcascade_frontalface_alt.xml" );  
}

void movieEvent(GSMovie myMovie) {
  movie.read();
}

void draw() {
//movie.frameRate(10);
  if (movie.width > 1 && movie.height > 1) {
    if (abs(movie.time() - movie.duration()) < 0.01) {
      println("Reached the end");
    }
	//opencv.remember();
	prevFrame = movie.get(0,0,width,height);
	opencv.allocate(prevFrame.width, prevFrame.height);
	opencv.copy(prevFrame.pixels, prevFrame.width, 0,0, prevFrame.width, prevFrame.height, 0,0, prevFrame.width, prevFrame.height);
	opencv.convert(GRAY);
	Rectangle[] faces = opencv.detect( 1.2, 2, OpenCV.HAAR_DO_CANNY_PRUNING, 50, 50 );
	if (faces.length>0) println(n);
	//image( opencv.image(), 0, 0 ); 
	image( movie, 0, 0 ); 
	noFill();
    stroke(255,0,0);
	
    for( int i=0; i<faces.length; i++ ) {
      //opencv.ROI( faces[i].x, faces[i].y, faces[i].x+faces[i].width, faces[i].y+faces[i].height );
      //opencv.copy(opencv.image(),faces[i].x, faces[i].y, faces[i].width, faces[i].height,faces[i].x, faces[i].y, faces[i].width, faces[i].height);
       n++;
	   //image( opencv.image(),faces[i].x, faces[i].y, faces[i].x+faces[i].width, faces[i].y+faces[i].height ); 
	   //image(opencv.image(), 0,0); 
	   tek = movie.get(faces[i].x,faces[i].y,faces[i].width,faces[i].height);
	   //image(tek,0,0);
       tek.save("e:\\cap\\"+n+".jpg");            
       rect( faces[i].x-40, faces[i].y-40, faces[i].width+60, faces[i].height+60 ); 
	   tek = null;
    }
  }  
}


 
