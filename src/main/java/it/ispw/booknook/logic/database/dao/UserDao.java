package it.ispw.booknook.logic.database.dao;

import it.ispw.booknook.logic.database.BookNookDB;
import it.ispw.booknook.logic.database.queries.LogQueries;
import it.ispw.booknook.logic.entity.Library;
import it.ispw.booknook.logic.entity.User;
import it.ispw.booknook.logic.entity.UserType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDao {

    private UserDao() {
    }

    //registra un nuovo utente Reader nel sistema
    public static void registerReaderUser(User user) {
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();
        try {
            String type = switch (user.getType()) {
                case READER -> "lettore";
                case LIBRARIAN -> "bibliotecario";
            };
            LogQueries.saveReaderUser(conn, user, type);
            if (user.getType() == UserType.READER)
                LogQueries.saveUsername(conn, user);
        } catch (SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        }
    }

    //recupera un utente dal db se presente
    public static User getReaderUser(String email) throws Exception {
        Connection conn = null;
        User user = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();

        try {
            ResultSet rs = LogQueries.selectReaderUser(conn, email);

            if (!rs.first()) { // rs empty
                throw new Exception("No User Found matching with email and password");
            }

            //altrimenti l'utente è presente
            rs.first();

            String username = rs.getString("username");
            String userEmail = rs.getString("email");
            String password = rs.getString("password");
            String type = rs.getString("tipo");

            UserType userType = switch (type) {
                case "lettore" -> UserType.READER;
                case "bibliotecario" -> UserType.LIBRARIAN;
                default -> UserType.READER;
            };

            user = User.getUser();
            user.setLogDetails(username, userEmail, password, userType);
            rs.close();


            if (user.getType() == UserType.READER) {
                //recupera il codice dell'immagine profilo
                rs = LogQueries.getReaderImage(conn, username);

                if (!rs.first()) { // rs empty
                    throw new Exception("No User Found matching with username");
                }

                //altrimenti l'utente è presente
                rs.first();

                Integer profileImage = rs.getInt("immagine_profilo");

                String imageUrl = switch (profileImage) {
                    case 0 -> "C:\\Users\\HP\\IdeaProjects\\BookNook\\src\\main\\resources\\it\\ispw\\booknook\\mainView\\account_circle_24dp.png";
                    case 1 -> "C:\\Users\\HP\\IdeaProjects\\BookNook\\src\\main\\resources\\it\\ispw\\booknook\\mainView\\avatar_1.png";
                    case 2 -> "C:\\Users\\HP\\IdeaProjects\\BookNook\\src\\main\\resources\\it\\ispw\\booknook\\mainView\\avatar_2.png";
                    case 3 -> "C:\\Users\\HP\\IdeaProjects\\BookNook\\src\\main\\resources\\it\\ispw\\booknook\\mainView\\avatar_3.png";
                    case 4 -> "C:\\Users\\HP\\IdeaProjects\\BookNook\\src\\main\\resources\\it\\ispw\\booknook\\mainView\\avatar_4.png";
                    default -> "C:\\Users\\HP\\IdeaProjects\\BookNook\\src\\main\\resources\\it\\ispw\\booknook\\mainView\\account_circle_24dp.png";
                };

                user.setImageProfile(imageUrl);
                rs.close();

                //recupera ulteriori informazioni sull'utente se presenti
                rs = LogQueries.getProfileDetails(conn, username);

                rs.first();

                User.getUser().setAddress(rs.getString("indirizzo"));
                User.getUser().setCity(rs.getString("città"));
                User.getUser().setZip(rs.getString("codice_postale"));
                User.getUser().setCountry(rs.getString("paese"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static String getPassUser(String email) throws Exception {
        Connection conn = null;
        String readerPassword = null;


        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();


        try {
            ResultSet rs = LogQueries.getpass(conn, email);

            if (!rs.first()) { // rs empty
                throw new Exception("No User Found matching with email and password");
            }

            //altrimenti l'utente è presente
            rs.first();

            readerPassword = rs.getString("password");


            rs.close();

        } catch (SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        }
        return readerPassword;
    }


    public static void updateEmail(String oldEmail, String newEmail) throws SQLException {
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();

        LogQueries.saveEmail(conn, oldEmail, newEmail);
    }

    public static void updatePassword(String username, String password) throws SQLException {
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();

        LogQueries.savePassword(conn, username, password);
    }

    public static void updateProfile(String username, String firstName, String lastName, String address, String city, String zip, String country) throws SQLException {
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();

        LogQueries.updateProfileDetails(conn, username, firstName, lastName, address, city, zip, country);
    }

    public static void getProfile(String username) throws Exception {
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();

        try {
            ResultSet rs = LogQueries.getProfileDetails(conn, username);

            if (!rs.first()) { // rs empty
                throw new Exception("No User Found matching with username");
            }

            //altrimenti l'utente è presente
            rs.first();

            User.getUser().setFirstName(rs.getString("nome"));
            User.getUser().setLastName(rs.getString("cognome"));
            User.getUser().setAddress(rs.getString("indirizzo"));
            User.getUser().setCity(rs.getString("città"));
            User.getUser().setZip(rs.getString("codice_postale"));
            User.getUser().setCountry(rs.getString("paese"));

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveProfileImage(User user) {
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();
        try {
            String username = User.getUser().getUsername();
            Integer numImage = switch (user.getImageProfile()) {
                case "C:\\Users\\HP\\IdeaProjects\\BookNook\\src\\main\\resources\\it\\ispw\\booknook\\mainView\\avatar_1.png" -> 1;
                case "C:\\Users\\HP\\IdeaProjects\\BookNook\\src\\main\\resources\\it\\ispw\\booknook\\mainView\\avatar_2.png" -> 2;
                case "C:\\Users\\HP\\IdeaProjects\\BookNook\\src\\main\\resources\\it\\ispw\\booknook\\mainView\\avatar_3.png" -> 3;
                case "C:\\Users\\HP\\IdeaProjects\\BookNook\\src\\main\\resources\\it\\ispw\\booknook\\mainView\\avatar_4.png" -> 4;
                default -> 1;
            };

            LogQueries.saveImage(conn, username, numImage);

        } catch (SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        }
    }

    public static void deleteProfile(User user) {
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();

        String username = user.getUsername();
        try {
            LogQueries.deleteReader(conn, username);
            LogQueries.deleteUser(conn, username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void registerLibraryDetails(Library library) {
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();

        try {
            String username = library.getUsername();
            String name = library.getName();
            String address = library.getAddress();
            Time startTime = library.getOpeningTime();
            Time endTime = library.getClosingTime();
            String city = library.getCity();
            LogQueries.insertLibraryDetails(conn, username, name, address, city, startTime, endTime);
        } catch (SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        }

        //crea file per memorizzazione recensioni della biblioteca
        try (FileOutputStream ignored = new FileOutputStream("src/main/resources/" + library.getUsername() + ".ser")) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}










