PImage img;
PImage img1;
int g = 0;
int g1= 0;
int it= 0;
long ves = 0;
void setup() {
  //String path = selectInput("select a video file ...");
	size(800,600);
	img  = loadImage("8363.jpg"); 
	//img.filter(THRESHOLD,0.5);
	img.loadPixels();

		for (int i = 0; i < img.pixels.length; i++) {
			img.pixels[i] = img.pixels[i]+10;
		}
			img.updatePixels(); 
}

void draw() {
 image(img,0,0,800,600); 
}
