package it.ispw.booknook.logic.database.dao;

import it.ispw.booknook.logic.database.BookNookDB;
import it.ispw.booknook.logic.database.queries.BookQueries;
import it.ispw.booknook.logic.entity.Book;
import it.ispw.booknook.logic.entity.BookCopy;
import it.ispw.booknook.logic.entity.Library;
import it.ispw.booknook.logic.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookDao {

    private BookDao() {}

    public static List<Book>  getRequestedBooks(String title) {
        List<Book> list = new ArrayList<Book>();
        Connection conn = null;
        Book book;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();
        try {
            ResultSet rs = BookQueries.getBooks(conn, title);

            if (!rs.first()){ // rs empty
                throw new Exception("No Books found matching with title or author");
            }
           book = new Book(rs.getString("titolo"), rs.getString("autore"));
           list.add(book);

            //altrimenti libri presenti
            while (rs.next()) {
                book = new Book(rs.getString("titolo"), rs.getString("autore"));
                list.add(book);
            }

            rs.close();

        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public static Book getSelectedBookDetails(String title, String author) {
        Book book = new Book();
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();
        try {
            ResultSet rs = BookQueries.getABook(conn, title, author);

            if (!rs.first()){ // rs empty
                throw new Exception("No Books found matching with title or author");
            }

            book.setIsbn(rs.getString("ISBN"));
            book.setTitle(rs.getString("titolo"));
            book.setAuthor(rs.getString("autore"));
            book.setPublisher(rs.getString("editore"));
            book.setPublishingYear(rs.getInt("anno"));

            rs.close();

        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //recupera i tag associati a quel libro
        try {
            ResultSet rs = BookQueries.getTags(conn, book.getIsbn());

            if (!rs.first()){ // rs empty
                throw new Exception("No Books found matching with title or author");
            }

            book.setTag(rs.getString("descrizione"));

            while (rs.next()) {
                book.setTag(rs.getString("descrizione"));
            }

            rs.close();
        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return book;
    }

    public static List<String> getTagsByISBN(String ISBN) {
        //recupera i tag associati a quel libro
        Connection conn = null;
        List<String> tags = new ArrayList<>();

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();

        try {
            ResultSet rs = BookQueries.getTags(conn, ISBN);


            if (!rs.first()){ // rs empty
                throw new Exception("No Books found matching with title or author");
            }

            tags.add(rs.getString("descrizione"));

            while (rs.next()) {
                tags.add(rs.getString("descrizione"));
            }

            rs.close();
        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tags;
    }

    public static List<Book> getRelatedBooks(List<String> tags, String isbn){
        List<Book> bookList = new ArrayList<>();
        Connection conn = null;
        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();
        try {
            for (int i= 0; i< tags.size(); i++) {
                ResultSet rs = BookQueries.getBooksByTags(conn, isbn, tags.get(i));
                while(rs.next()){
                    Book book = new Book();
                    book.setIsbn(rs.getString("libri.ISBN"));
                    book.setTitle(rs.getString("titolo"));
                    book.setAuthor(rs.getString("autore"));
                    book.setPublisher(rs.getString("editore"));
                    book.setPublishingYear(rs.getInt("anno"));
                    book.setTag(tags.get(i));
                    bookList.add(book);
                }
                rs.close();
            }

        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookList;
    }

    public static void insertFavorites(String reader, String listName, String isbn) {
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();

        try {
            BookQueries.insertBookInList(conn, reader, listName, isbn);

        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Book>  getFavoriteBooks(String username, String listName) {
        List<Book> list = new ArrayList<Book>();
        Connection conn = null;
        Book book;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();
        try {
            ResultSet rs = BookQueries.getList(conn, username, listName);

            if (!rs.first()){ // rs empty
                throw new Exception("No List found matching with name");
            }

            rs.first();
            book = new Book();
            book.setIsbn(rs.getString("libri.ISBN"));
            System.out.println(rs.getString("libri.ISBN"));
            book.setTitle(rs.getString("titolo"));
            book.setAuthor(rs.getString("autore"));
            list.add(book);

            //altrimenti libri presenti
            while (rs.next()) {
                book = new Book();
                book.setIsbn(rs.getString("libri.ISBN"));
                System.out.println(rs.getString("libri.ISBN"));
                book.setTitle(rs.getString("titolo"));
                book.setAuthor(rs.getString("autore"));
                list.add(book);
            }

            rs.close();

        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    //recupera le informazioni sui libri attualmente in prestito dell'utente
    public static List<BookCopy>  getBooksOnLoan(String username) {
        List<BookCopy> list = new ArrayList<BookCopy>();
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();
        try {
            ResultSet rs = BookQueries.getLoanBooks(conn, username);

            if (!rs.first()){ // rs empty
                throw new Exception("No List found matching with username");
            }

            rs.first();
            Book book = new Book();
            book.setIsbn(rs.getString("libri.ISBN"));
            book.setTitle(rs.getString("titolo"));
            book.setAuthor(rs.getString("autore"));
            BookCopy copy = new BookCopy();
            copy.setLoanDate(rs.getDate("data_prestito"));
            copy.setBook(book);
            Library library = new Library();
            library.setName(rs.getString("biblioteche.nome")); //biblioteca da cui è in prestito la copia
            library.setUsername(rs.getString("biblioteche.username"));
            copy.setLibrary(library);
            list.add(copy); //lista di copie in prestito al lettore

            //altrimenti libri presenti
            while (rs.next()) {
                book = new Book();
                book.setIsbn(rs.getString("libri.ISBN"));
                book.setTitle(rs.getString("titolo"));
                book.setAuthor(rs.getString("autore"));
                copy = new BookCopy();
                copy.setLoanDate(rs.getDate("data_prestito"));
                copy.setBook(book);
                library = new Library();
                library.setName(rs.getString("biblioteche.nome")); //biblioteca da cui è in prestito la copia
                copy.setLibrary(library);
                list.add(copy); //lista di copie in prestito al lettore
            }

            rs.close();

        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static boolean getListByISBN(String username, String isbn) {
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();
        boolean state = false;
        try {
            ResultSet rs = BookQueries.getBookInList(conn, username, isbn);

            state = rs.first();

            rs.close();

        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return state;
    }

    public static void deleteBookFromList(String username, String isbn) {
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();

        try {
            BookQueries.removeBookFromList(conn, username, isbn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Book> getBookByCategory(String tag){
        List<Book> bookList = new ArrayList<>();
        Connection conn = null;
        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();
        try {
                ResultSet rs = BookQueries.getBooksByGenre(conn, tag);
                while(rs.next()){
                    Book book = new Book();
                    book.setIsbn(rs.getString("libri.ISBN"));
                    book.setTitle(rs.getString("titolo"));
                    book.setAuthor(rs.getString("autore"));
                    book.setPublisher(rs.getString("editore"));
                    book.setPublishingYear(rs.getInt("anno"));
                    ResultSet tags = BookQueries.getTags(conn, book.getIsbn());
                    while(tags.next()){
                        book.setTag(tags.getString("descrizione"));
                    }
                    tags.close();
                    bookList.add(book);

                }
                rs.close();
        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookList;
    }

    public static List<Book>  getConsutableBook(String title) {
        List<Book> list = new ArrayList<Book>();
        Connection conn = null;
        Book book;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();
        try {
            ResultSet rs = BookQueries.getConsultationBooks(conn, title);

            if (!rs.first()){ // rs empty
                throw new Exception("No Books found matching with title or author");
            }

            book = new Book(rs.getString("titolo"), rs.getString("autore"));
            book.setIsbn(rs.getString("ISBN"));
            book.setPublisher(rs.getString("editore"));
            book.setPublishingYear(rs.getInt("anno"));
            list.add(book);

            //altrimenti libri presenti
            while (rs.next()) {
                book = new Book(rs.getString("titolo"), rs.getString("autore"));
                book.setIsbn(rs.getString("ISBN"));
                book.setPublisher(rs.getString("editore"));
                book.setPublishingYear(rs.getInt("anno"));
                list.add(book);
            }

            rs.close();

        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    //aggiorna le prenotazioni di consultazioni
    public static void setConsultationReserved(Library library) {
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();

        try {
            String username = User.getUser().getUsername();
            String libraryName = library.getUsername();
            Date consultationDate = library.getShifts().get(0).getDate();
            Time startTime =  library.getShifts().get(0).getStartTime();
            BookQueries.updateConsultation(conn, username, libraryName, consultationDate, startTime);

        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addNewBook(Book book) {
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();

        try {
            String isbn = book.getIsbn();
            String title = book.getTitle();
            String author = book.getAuthor();
            String publisher = book.getPublisher();
            int publicationYear = book.getPublishingYear();
            int state = (book.isConsultable() ? 1 : 0);
            //inserisce il libro nel db
            BookQueries.insertBook(conn, isbn, title, author, publisher, publicationYear, state);

            String libraryUsername = book.getCopies().get(0).getLibrary().getUsername();
            //inserisce le copie nel db
            for (int i = 1; i<=book.getCopies().size(); i++) {
                BookQueries.insertCopy(conn, isbn, libraryUsername);
            }

            ResultSet rs = null;
            //assegna i tag scelti al libro
            for (int i = 0; i<book.getTags().size(); i++) {
                System.out.println(book.getTags().get(i));
                rs = BookQueries.getIdTag(conn, book.getTags().get(i));
                rs.first();
                int id = rs.getInt("id");
                BookQueries.setTag(conn, book.getIsbn(), id);
            }

            rs.close();


        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<String> getAvailableTags() {
        List<String> tags= new ArrayList<>();
        Connection conn = null;

        BookNookDB db = BookNookDB.getInstance();
        conn = db.getConn();
        try {
            ResultSet rs = BookQueries.getAllTags(conn);

            if (!rs.first()){ // rs empty
                throw new Exception("No tags available");
            }

            rs.previous();

            //altrimenti libri presenti
            while (rs.next()) {
                String newTag = rs.getString("descrizione");
                tags.add(newTag);
            }

            rs.close();

        } catch(SQLException e) {
            Logger logger = Logger.getLogger("MyLog");
            logger.log(Level.INFO, "This is message 1", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tags;
    }
}
