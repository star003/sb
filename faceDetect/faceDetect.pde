import hypermedia.video.*;
import java.awt.Rectangle;

OpenCV opencv;
PImage pumpkin;
float sizeFactor=3.5; // bigger = smaller pumpkin

void setup() {

    size( 320, 240 );
    pumpkin = loadImage("pumpkin-head.png");
    opencv = new OpenCV(this);
    opencv.capture( width, height );
    opencv.cascade( OpenCV.CASCADE_FRONTALFACE_ALT );    // load the FRONTALFACE description file
}

void draw() {
    
    opencv.read();
    image( opencv.image(), 0, 0 );
    
    // detect anything ressembling a FRONTALFACE
    Rectangle[] faces = opencv.detect();
    
    // draw detected face area(s)
    noFill();
    stroke(255,0,0);
    for( int i=0; i<faces.length; i++ ) {
//        rect( faces[i].x, faces[i].y, faces[i].width, faces[i].height ); 
        image(pumpkin, faces[i].x - faces[i].width/sizeFactor, faces[i].y - faces[i].height/sizeFactor, faces[i].width+ faces[i].width/sizeFactor*2, faces[i].height + faces[i].height/sizeFactor*2 ); 
    }
}

