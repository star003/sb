// fjenett 20081129

import de.bezier.data.sql.*;

SQLite db;

void setup()
{
    db = new SQLite( this, "e:\\vkget\\data\\vkget.db" );  // open database file
    if ( db.connect() )
    {
        db.query( "SELECT * FROM vk" );
        while (db.next())
        {
            println( db.getString("uid") );
        }
    }
}

