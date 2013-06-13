import de.bezier.data.sql.*;
PImage img;
SQLite db,db1;

void setup() {
String [] town 		= new String[32768];
String [] idTown 	= new String[32768];
db = new SQLite( this, "E://sb//vkgetHline//data//vkget.db" );
db.connect();
db.query( "delete from city;");
db.query( "SELECT vk.sity as ss FROM vk GROUP BY vk.sity;");

int i=0;
 while (db.next()) {
 String h = db.getString("ss");
	if (h.length() <8 ){
		String t = getTown(h);
			if (t !="") {
			//
			println( i+" => "+h+" / "+t);
			town[i] = t;
			idTown[i]=h;
			i++;
			} //if
	}	//if	
 }//while
 for (int j=0;j<town.length;j++) {
	println( "ins " +idTown[j]+" / "+town[j]);
	db.query( "INSERT INTO city (id,sity) VALUES ('"+idTown[j]+"','"+town[j]+"')");
 } //for j
 db.close();
} //setup()

String getTown(String id) {
String[] user = loadStrings("https://api.vk.com/method/places.getCityById.xml?cids="+id);
	for (int i0 = 0; i0 < user.length; i0++) {
	if (user[i0].indexOf("<name>")!= -1){
		return(gt(user[i0]));
		}
	}
	return "";
}

String gt(String s) {
 String[] x = split(s,'>');
 String[] x1 = split(x[1],'<');
 if (x1[0]==null) return "0";
 else return x1[0]; 
} //gt(String s)