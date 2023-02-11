package it.ispw.booknook.logic.control;

import it.ispw.booknook.logic.bean.BookBean;
import it.ispw.booknook.logic.database.dao.BookDao;
import it.ispw.booknook.logic.entity.Book;
import it.ispw.booknook.logic.entity.BookCopy;
import it.ispw.booknook.logic.entity.Library;
import it.ispw.booknook.logic.entity.User;

public class AddBookController {

    public void addABook(BookBean insertedBook) {
        int a;
        Book book =  new Book();
        book.setIsbn(insertedBook.getIsbn());
        book.setTitle(insertedBook.getTitle());
        book.setAuthor(insertedBook.getAuthor());
        book.setPublisher(insertedBook.getPublisher());
        book.setPublishingYear(insertedBook.getYear());
        book.setConsultationState(insertedBook.getForConsultation());
        book.setTags(insertedBook.getTags());
        Library library = new Library();
        library.setUsername(User.getUser().getUsername());
        for(int i = 0; i < insertedBook.getNumCopies(); i++) {
            BookCopy newCopy = new BookCopy();
            newCopy.setLibrary(library);
            book.addCopy(newCopy);
        }
        BookDao.addNewBook(book);
    }

    public BookBean calculateAvailableTags() {
        BookBean bookBean = new BookBean();
        bookBean.setTags(BookDao.getAvailableTags());
        return bookBean;
    }
}
