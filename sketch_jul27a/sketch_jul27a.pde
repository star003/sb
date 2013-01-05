import hypermedia.video.*;
OpenCV opencv; //opecv object

void setup() {
size( 320, 240 ); //window size
opencv = new OpenCV( this ); // opencv object
opencv.capture( width, height ); // open video stream
}

void draw() {
opencv.read(); // grab frame from camera
image( opencv.image(), 0, 0 ); 
} // and display image
