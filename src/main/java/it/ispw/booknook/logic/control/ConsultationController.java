package it.ispw.booknook.logic.control;

import it.ispw.booknook.logic.bean.BookBean;
import it.ispw.booknook.logic.bean.LibraryBean;
import it.ispw.booknook.logic.bean.ShiftBean;
import it.ispw.booknook.logic.boundary.Gmailer;
import it.ispw.booknook.logic.database.dao.BookDao;
import it.ispw.booknook.logic.database.dao.LibraryDao;
import it.ispw.booknook.logic.entity.Book;
import it.ispw.booknook.logic.entity.ConsultationShift;
import it.ispw.booknook.logic.entity.Library;
import it.ispw.booknook.logic.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConsultationController {

    //recupera lista di libri consultabili dato un certo autore o titolo
    public List<BookBean> searchBook(BookBean requestedBook) {
        List<BookBean> bookBeans = new ArrayList<BookBean>();
        List<Book> books = BookDao.getConsutableBook(requestedBook.getTitle());
        for (int i = 0; i < books.size(); i++) {
            BookBean bookBean = new BookBean();
            bookBean.setBookDetails(books.get(i));
            bookBean.setIsbn(books.get(i).getIsbn());
            bookBean.setPublishingYear(books.get(i).getPublishingYear());
            bookBeans.add(bookBean);
        }
        return bookBeans;
    }


    //recupera librerie con disponibilità del libro selezionato
    public List<LibraryBean> checkLibraries(BookBean selectedBook) {
        Map<String, Library> libraries = LibraryDao.getLibrariesByISBN(selectedBook.getIsbn());
        List<LibraryBean> resultLibraries = new ArrayList<>();
        libraries.forEach((name, library) -> {
            LibraryBean newLibrary = new LibraryBean(library);
            resultLibraries.add(newLibrary);
        });
        return resultLibraries;
    }


    //calcola i turni disponibili per un certo giorno
    public List<ShiftBean> getAvailableShifts(ShiftBean shift) {
        Library library = new Library();
        library.setUsername(shift.getUsernameLibrary());
        LibraryDao.getLibraryShift(library, shift.getDate());
        //library ora mantiene i riferimenti ai turni disponibili
        List<ShiftBean> shifts = new ArrayList<>();
        library.getShifts().forEach(currShift -> {
            ShiftBean shiftBean = new ShiftBean();
            shiftBean.setConsultationDate(shift.getDate());
            shiftBean.setStartTime(currShift.getStartTime());
            shiftBean.setEndTime(currShift.getEndTime());
            shifts.add(shiftBean);
        });

        return shifts;
    }

    //conferma la prenotazione
    public void reserveConsultation(ShiftBean selectedShift, LibraryBean selectedLibrary) {
        Library library = new Library();
        library.setUsername(selectedLibrary.getUsername());
        ConsultationShift shift = new ConsultationShift(selectedShift.getDate(),selectedShift.getStartTime(), selectedShift.getEndTime());
        library.addShift(shift);
        BookDao.setConsultationReserved(library);
        //invio mail con conferma prenotazione
        String readerToNotify = User.getUser().getEmail();
        String username = User.getUser().getUsername();
        String messageToReader = "Hi " + username + ",\n" + "You have successfully reserve a consultation at  " + selectedLibrary.getName() +
                " (" + selectedLibrary.getAddress() + ", " + selectedLibrary.getCity() + ") " + "at the following time:  " + selectedShift.getTime() + ".";

       try {
            new Gmailer().sendEmail(readerToNotify, "Consultation confirmed from BookNook", messageToReader);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}
