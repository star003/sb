String[] dat =  new String[32767];
PImage img;
int i=0;
void setup() {
	size(800,600);
	int v=0;
	String[] user = loadStrings("http://api-fotki.yandex.ru/api/users/byaka5917/photos/");
		for (int j = 0; j < user.length; j++) {
			if (user[j].indexOf("f:img") != -1) {
				String[] x  = split(user[j],'=');
				String[] x1 = split(x[2],'"');
					if (x1[1].indexOf("http") != -1) {
						dat[v] = x1[1]+".jpg";
							try {
								img = loadImage(dat[v]);
								println(fileName(dat[v]));
								if (img.width > 499) img.save("e:\\001\\"+fileName(dat[v]));
								}
							catch (Exception e) {
							}
				v++;
					}
			}
		}
}


/*
void draw() {
			}
*/

String delKav(String s) {
	String[] list = split(s, '"');
		if (list.length<2) {
			return "";
		}
		else {
			return list[1];
		}
} 

String fileName(String s) {
	String[] x = split(s,'/');
	if (x.length<2) return "";
	else return x[x.length-1];
}

String orFileName(String s) {
	String[] x = split(s,'_');
	if (x.length<2) return "";
	else return x[0]+"_"+x[1]+"_"+x[2]+".jpg";
}


