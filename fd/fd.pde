import hypermedia.video.*;
import java.awt.Rectangle;

OpenCV opencv;

int m01=0;
 int m02=500;
 
// contrast/brightness values
int contrast_value    = 0;
int brightness_value  = 0;

void setup() {

    opencv = new OpenCV( this );
    size( 698, 574 );
    opencv.loadImage("e:\\1.jpg");
    opencv.cascade( "C:\\Program Files\\OpenCV\\data\\haarcascades\\haarcascade_frontalface_alt.xml" ); 
}

public void stop() {
    opencv.stop();
    super.stop();
}



void draw() {
	if (millis()-m01 > m02) {
    m01 = millis();
    opencv.loadImage("e:\\1.jpg");
    Rectangle[] faces = opencv.detect( 1.2, 2, OpenCV.HAAR_DO_CANNY_PRUNING, 50, 50 );
	println(faces.length);
    image( opencv.image(), 0, 0 );
    // draw face area(s)
    noFill();
    stroke(255,0,0);
    for( int i=0; i<faces.length; i++ ) {
       rect( faces[i].x, faces[i].y, faces[i].width, faces[i].height ); 
       //opencv.threshold(80);
//       opencv.ROI( faces[i].x, faces[i].y, faces[i].width, faces[i].height );
 //     opencv.copy(opencv.image(),faces[i].x, faces[i].y, faces[i].width, faces[i].height,faces[i].x, faces[i].y, faces[i].width, faces[i].height);
       //image( opencv.image(), 0, 0 ); 
//       save("e:\\cap\\"+i+".jpg");            
       //opencv.cvRect(faces[i].x, faces[i].y, faces[i].width, faces[i].height);
    }
	}
}




