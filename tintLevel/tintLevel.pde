PImage source;       // Source image
PImage destination;  // Destination image
float br=0;
float br1=0;
void setup() {
  size(800, 600);
  source = loadImage("2013-1-31_5-1-24_34.0.jpg");  
  destination = createImage(source.width, source.height, RGB);
	
}

void draw() {  
  float threshold = 200;
	image(source,0,0,640,480);
  }
  
float tnt(PImage source) {
//средн€€ €ркость изображени€
	source.loadPixels();
  for (int x = 0; x < source.pixels.length; x++) {
       br += brightness(source.pixels[x]);
    }
	return br/source.pixels.length;
}  

float a1(PImage source) {
	for (int x = 1; x < width; x++) {
		for (int y = 0; y < height; y++ ) {
			int loc = x + y*source.width;
			//source.pixels[loc]
		}
	}
return 0;
}

