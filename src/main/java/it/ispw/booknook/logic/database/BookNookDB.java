package it.ispw.booknook.logic.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


//singleton
//gestisce la connesione unica al database
public class BookNookDB {

    private static BookNookDB db = null;
    private Connection conn = null;
    private Properties p = null;

    private BookNookDB() {}

    public static synchronized BookNookDB getInstance() {
        if (BookNookDB.db == null)
            BookNookDB.db = new BookNookDB();
        return db;
    }

    public Connection getConn() {
        if (db.conn == null) {
            p = new Properties();

            //FileInputStream implementa interfaccia Autoclosable perciò try-with-resources assicura sempre la chiusura
            try (FileInputStream f = new FileInputStream("src/main/resources/dbconfig.properties")) {
                p.load(f);
                Class.forName(p.getProperty("jdbcDriver"));
                db.conn = DriverManager.getConnection(p.getProperty("jdbcUrl"), p.getProperty("jdbcUser"), p.getProperty("jdbcPass"));
            } catch (ClassNotFoundException | IOException | SQLException e) {
                Logger logger = Logger.getLogger("MyLog");
                logger.log(Level.INFO, "This is message 1", e);
            }
        }
        return db.conn;
    }

    public void closeConn() {
        try {
            db.conn.close();
            db.conn = null;
        } catch (SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        }
    }


}
