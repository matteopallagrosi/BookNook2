package it.ispw.booknook.logic.control;

import it.ispw.booknook.logic.bean.BookBean;
import it.ispw.booknook.logic.bean.FavoriteBean;
import it.ispw.booknook.logic.boundary.BookDetailsBoundary;
import it.ispw.booknook.logic.database.dao.BookDao;
import it.ispw.booknook.logic.entity.Book;
import it.ispw.booknook.logic.entity.BookCopy;
import it.ispw.booknook.logic.entity.User;

import java.util.ArrayList;
import java.util.List;

public class ReadingListController {

    public void addToReadingList(FavoriteBean bookToAdd) {
        String isbn = bookToAdd.getIsbn();
        String listName = bookToAdd.getListName();
        String reader = User.getUser().getUsername();
        BookDao.insertFavorites(reader, listName, isbn);
    }

    public List<BookBean> getReadingList(FavoriteBean list){
        List<Book> readingList = BookDao.getFavoriteBooks(User.getUser().getUsername(), list.getListName());
        List<BookBean> bookList = new ArrayList<>();
        readingList.forEach(book -> {
            BookBean bookBean = new BookBean();
            bookBean.setIsbn(book.getIsbn());
            bookBean.setAuthor(book.getAuthor());
            bookBean.setTitle(book.getTitle());
            BookDetailsBoundary bookDetailsBoundary = new BookDetailsBoundary(bookBean);
            Thread t1 = new Thread(bookDetailsBoundary);
            t1.start();
            bookList.add(bookBean);
            }
        );
        return bookList;
    }

    public List<BookBean> getBooksOnLoan(){
        List<BookCopy> booksOnLoan = BookDao.getBooksOnLoan(User.getUser().getUsername());
        List<BookBean> bookList = new ArrayList<>();
        booksOnLoan.forEach(copy -> {
            BookBean bookBean = new BookBean();
            //setta le informazioni da restituire al controller grafico
            bookBean.setIsbn(copy.getBook().getIsbn());
            bookBean.setAuthor(copy.getBook().getAuthor());
            bookBean.setTitle(copy.getBook().getTitle());
            bookBean.setLoanDate(copy.getLoanDate());
            bookBean.setLibraryName(copy.getLibrary().getName());
            bookBean.setUsernameLibrary(copy.getLibrary().getUsername());
            BookDetailsBoundary bookDetailsBoundary = new BookDetailsBoundary(bookBean);
            Thread t1 = new Thread(bookDetailsBoundary);
            t1.start();
            bookList.add(bookBean);
            }
        );
        return bookList;
    }

    public void deleteFromReadingList(BookBean book) {
        String username = User.getUser().getUsername();
        String isbn = book.getIsbn();
        BookDao.deleteBookFromList(username, isbn);
    }
}
