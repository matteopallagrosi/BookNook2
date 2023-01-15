package it.ispw.booknook.logic.entity;

import java.sql.Date;

public class BookCopy {
    private int id;
    private Library library;  //la biblioteca che possiede la copia
    private CopyState state;
    private Book book;  //il libro di cui è copia
    private Date loanDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public CopyState getState() {
        return state;
    }

    public void setState(int state) {
        if(state == 1){
            this.state = CopyState.AVAILABLE;
        }
        else {
            this.state = CopyState.NOT_AVAILABLE;
        }
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }
}
