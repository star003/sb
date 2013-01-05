int i=10;
String[] user;
String[] list;
String[] ls = {
  "1:-30", "2:-25", "3:-20", "4:-15", "5:-10", "6:-5", "7:0", "8:5", "9:10", "10:15", "11:20", "12:25", "13:30"
};

void setup() {
  size(800,600);
  frameRate(1);
  background(255, 255, 204);
  try {
      user = loadStrings("http://star003.dlinkddns.com/05.php");
      list = splitTokens(user[0], "<br>");
    }
    catch (Exception e) {
      list = ls;
    }
}
void draw() {
  background(255, 255, 204);
  for(int i=0 ;i<list.length;i++) {
    String g = list[list.length-i-1];
    String tm[] = splitTokens(g, ":");
  fill(0);
  pushMatrix();
  translate(10,10);
  rotate(radians(90));
  text(tm[0],200,15*(1-i));
  popMatrix();
  }
}

