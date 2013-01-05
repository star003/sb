void setup() {
  String[] user = loadStrings("https://www.google.com/accounts");
  for (int i=0; i<user.length; i++)  {
    println(user[i]);
    }
  
  }
