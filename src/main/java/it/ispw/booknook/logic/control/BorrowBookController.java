package it.ispw.booknook.logic.control;

import it.ispw.booknook.logic.bean.BookBean;
import it.ispw.booknook.logic.bean.LibraryBean;
import it.ispw.booknook.logic.boundary.Gmailer;
import it.ispw.booknook.logic.boundary.JSONManager;
import it.ispw.booknook.logic.database.dao.BookDao;
import it.ispw.booknook.logic.database.dao.LibraryDao;
import it.ispw.booknook.logic.entity.*;
import it.ispw.booknook.logic.exception.BookNotFoundException;
import it.ispw.booknook.logic.exception.LostConnectionException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class BorrowBookController {

    //ritorna la lista di libri corrispondenti al titolo o autore inserito
    public List<BookBean> borrowByName(BookBean requestedBook) throws BookNotFoundException {
        List<BookBean> bookBeans = new ArrayList<BookBean>();
        try {
            List<Book> bookList = BookDao.getRequestedBooks(requestedBook.getTitle());
            for (int i = 0; i < bookList.size(); i++) {
                BookBean bookBean = new BookBean();
                bookBean.setBookDetails(bookList.get(i));
                bookBeans.add(bookBean);
            }
        } catch (SQLException e) {
            throw new BookNotFoundException("No books found matching with title or author", e);
        }

        return bookBeans;
    }

    //qui dentro ci sta la get del file json con l'immagine
    public List<BookBean> processRequestedBook(BookBean selectedBook) {
        //ritorna le informazioni sul libro selezionato (titolo, autore, immagine, tag)
        Book book = BookDao.getSelectedBookDetails(selectedBook.getTitle(), selectedBook.getAuthor());
        //recupera l'immagine della copertina del libro

        BookBean bookBean = new BookBean(book);

        List<BookBean> related = calculateBookList(book.getTags(), book.getIsbn());

        related.add(0, bookBean);  //aggiunge il libro selezionato alla lista

        related.forEach(bookBean1 -> {
            bookBean1.setTags(BookDao.getTagsByISBN(bookBean1.getIsbn()));
            JSONManager jsonManager = new JSONManager(bookBean1);
            Thread t1 = new Thread(jsonManager);
            t1.start();
            //se l'utente è loggato deve scoprire quali libri sono stati aggiunti ad una qualche lista
            if (new LoginController().verifyLogin()) {
                String username = User.getUser().getUsername();
                String isbn = bookBean1.getIsbn();
                bookBean1.setAddedtoList(BookDao.getListByISBN(username, isbn));
            }
        });

        return related;
    }

    //recupera la lista dei libri simili a quello selezionato
    private List<BookBean> calculateBookList(List<String> tags, String isbn) {
        List<Book> books = BookDao.getRelatedBooks(tags, isbn);
        //continua qui!
        return sortByOccurrences(books);
    }

    private List<BookBean> sortByOccurrences(List<Book> books) {
        HashMap<String, List<Book>> map = new HashMap<>();

        for (Book book : books) {
            List<Book> booksWithSameISBN = map.get(book.getIsbn());
            if (booksWithSameISBN == null) { //does not exist in map yet
                booksWithSameISBN = new ArrayList<Book>();
                map.put(book.getIsbn(), booksWithSameISBN);
            }
            booksWithSameISBN.add(book); //now add the item to the list for this key
        }

        class Data implements Comparable<Data> {

            private Book book;
            private int occurrences;

            public Data(Book book, int occurrences) {
                this.book = book;
                this.occurrences = occurrences;
            }

            public Book getBook() {
                return this.book;
            }

            @Override
            public int compareTo(Data o) {
                return o.getOccurrences() - this.occurrences;
            }

            public int getOccurrences() {
                return this.occurrences;
            }

            @Override
            public String toString() {
                return "Data{"+this.book.getIsbn()+"}";
            }
        }

        List<Data> sorted = new ArrayList<>();

        for (Map.Entry<String,List<Book>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<Book> listOfElementsWithSameKey = map.get(key);
            int size = listOfElementsWithSameKey.size();  //numero di occorrenze di un libro
            Book book = listOfElementsWithSameKey.get(0);

            Data data = new Data(book, size);
            sorted.add(data);
        }

        Collections.sort(sorted);  //ordina per numero di occorrenze (ossia numero di tag uguali a quelli del libro selezionato)

        List<BookBean> bookBeans = new ArrayList<>();  //lista con libri ordinati per affinità
        sorted.forEach(book -> bookBeans.add(new BookBean(book.getBook())));

        return bookBeans;
    }

    //recupera la lista di biblioteche con disponibilità del libro richiesto
    public List<LibraryBean> calculateLibraries(BookBean book) {
        Map<String, Library> libraries = LibraryDao.getLibrariesByISBN(book.getIsbn());
        List<LibraryBean> libraryList = new ArrayList<LibraryBean>();
        libraries.forEach((name, library) -> {
            LibraryBean bean = new LibraryBean(library);
            //setta "availability" a true se almeno uno copia è disponibile
            bean.setAvailability(library.getAvailability(book.getIsbn()));
            if (bean.isAvailable()) {
                library.getOwnedCopies().forEach(copy -> {
                    //recuper l'id di una copia disponibile
                    if (copy.getState() == CopyState.AVAILABLE) {
                        bean.setIdCopyAvailable(copy.getId());
                        bean.setIsbnAvailableBook(book.getIsbn());
                        bean.setTitleCopyAvailable(copy.getBook().getTitle());
                        bean.setAuthorCopyAvailable(copy.getBook().getAuthor());
                    }
                });
            }
            libraryList.add(bean);
        });

        return libraryList;
    }

    public void borrowBook(LibraryBean libraryDetails) throws LostConnectionException {
        BookCopy copyToBorrow = new BookCopy();
        Book book = new Book();
        Library library = new Library();
        book.setIsbn(libraryDetails.getIsbnAvailableBook());
        library.setUsername(libraryDetails.getUsername());
        copyToBorrow.setId(libraryDetails.getIdCopyAvailable());
        copyToBorrow.setBook(book);
        copyToBorrow.setLibrary(library);
        LibraryDao.setCopyOnLoan(copyToBorrow);
        //manda mail di conferma dell'ordine a utente e bibliotecario
        String readerToNotify = User.getUser().getEmail();
        String username = User.getUser().getUsername();
        String messageToReader = "Hi " + username + ",\n" + "thanks from your order!\nYou borrowed " + libraryDetails.getTitleCopyAvailable() + ", " +  libraryDetails.getAuthorCopyAvailable() + " from the library " + libraryDetails.getName() +
                " (" + libraryDetails.getAddress() + ", " + libraryDetails.getCity() + ", " + libraryDetails.getOpeningTime() + "-" + libraryDetails.getClosingTime() + ") " + "since " + LocalDate.now().toString() +
                    ".\nYou will have to return it within " + LocalDate.now().plusMonths(1) + ".";

        try {
            new Gmailer().sendEmail(readerToNotify, "Loan confirmation from BookNook", messageToReader);

        } catch (UnknownHostException e) {
            //connessione mancante
            throw new LostConnectionException("Cannot send email due to connection error", e);
        }
        catch(IOException | GeneralSecurityException | MessagingException e) {
            e.printStackTrace();
        }
    }


    //calcola la lista dei libri con un certo tag
    public List<BookBean> borrowByTag(BookBean book) {
        String selectedTag = book.getTags().get(0);
        List<Book> bookList = BookDao.getBookByCategory(selectedTag);
        List<BookBean> bookBeans = new ArrayList<>();
        for (int i = 0; i < bookList.size(); i++) {
            BookBean bookBean = new BookBean();
            bookBean.setIsbn(bookList.get(i).getIsbn());
            bookBean.setBookDetails(bookList.get(i));
            bookBean.setTags(bookList.get(i).getTags());
            bookBeans.add(bookBean);
            JSONManager jsonManager = new JSONManager(bookBean);
            Thread t1 = new Thread(jsonManager);
            t1.start();
            //se l'utente è loggato deve scoprire quali libri sono stati aggiunti ad una qualche lista
            if (new LoginController().verifyLogin()) {
                String username = User.getUser().getUsername();
                String isbn = bookBean.getIsbn();
                bookBean.setAddedtoList(BookDao.getListByISBN(username, isbn));
            }
        }
        return bookBeans;
    }
}
