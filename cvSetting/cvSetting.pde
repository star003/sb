import hypermedia.video.*;
import java.awt.Rectangle;

OpenCV opencv;

int m01=0;
int m02=500;
PImage x; 
// contrast/brightness values
int contrast_value    = 0;
int brightness_value  = 0;

void setup() {
	size(800,300);
    opencv = new OpenCV( this );
    opencv.loadImage("e:\\sb\\el\\5143-1.jpg");
	opencv.convert(OpenCV.GRAY);
	//opencv.blur( OpenCV.GAUSSIAN, 13);
	opencv.blur( OpenCV.GAUSSIAN, 20);
	//opencv.threshold(170);
	image( opencv.image(), 0, 0 );
}


void draw() {
	    
}
