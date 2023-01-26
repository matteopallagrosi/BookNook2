package it.ispw.booknook.logic.bean;

import it.ispw.booknook.logic.Subject;
import it.ispw.booknook.logic.entity.Book;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class BookBean extends Subject {
    private String isbn;
    private String title;
    private String author;
    private List<String> tags;
    private String cover;
    private Image coverImage = new Image("C:\\Users\\HP\\IdeaProjects\\BookNook\\src\\main\\resources\\it\\ispw\\booknook\\mainView\\product-not-found.png");
    //questa bean è usata anche per tenere traccia delle informazioni sulla copia
    private String loanDate;
    private String expireDate;
    private String libraryName;
    private boolean isAddedtoList;
    private ImageView viewToUpdate;
    private String usernameLibrary;
    private String publisher;
    private String publishingYear;
    private int numCopies;

    public void setViewToUpdate(ImageView view) {
        this.viewToUpdate = view;
    }


    public BookBean() {}

    public BookBean(String requestedTitle) {
        this.title = requestedTitle;
    }

    public BookBean(Book book) {
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.tags = book.getTags();
        this.cover = book.getCover();
        this.isbn = book.getIsbn();

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
        this.coverImage = new Image(cover);
        notifyObservers();
        if (viewToUpdate != null) {
            viewToUpdate.setImage(coverImage);
        }
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Image getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(Image coverImage) {
        this.coverImage = coverImage;
    }


    public void setBookDetails(Book book) {
        this.title = book.getTitle();
        this.author = book.getAuthor();
    }

    public void splitDetails(String details){
        String[] strings = details.split(", ");
        this.title = strings[0];
        this.author = strings[1];
    }

    public String getLoanDate() {
        return loanDate; //ritorna la data come stringa
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate.toString();  //converte la data in stringa
        setExpireDate(loanDate); //setta anche la data di restituzione
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date loanDate) {
        //aggiunge un mese alla data di prestito
       LocalDate expire = loanDate.toLocalDate().plusMonths(1);
       //riconverte la data di restituzione in Stringa
       this.expireDate = Date.valueOf(expire).toString();
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public boolean isAddedtoList() {
        return isAddedtoList;
    }

    public void setAddedtoList(boolean addedtoList) {
        isAddedtoList = addedtoList;
    }

    public String getUsernameLibrary() {
        return usernameLibrary;
    }

    public void setUsernameLibrary(String usernameLibrary) {
        this.usernameLibrary = usernameLibrary;
    }

    public String getPublishingYear() {
        return publishingYear;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPublishingYear(int publishingYear) {
        this.publishingYear = Integer.toString(publishingYear);
    }

    public int getYear() {
        return Integer.parseInt(this.getPublishingYear());
    }

    public void setYear(String year) {
        this.publishingYear = year;
    }

    public int getNumCopies() {
        return numCopies;
    }

    public void setNumCopies(int numCopies) {
        this.numCopies = numCopies;
    }
}
