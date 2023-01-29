package it.ispw.booknook.logic.bean;

public class FavoriteBean {
    private String isbn;
    private String listName;

    public FavoriteBean() {}

    public FavoriteBean(String isbn, String listName) {
        setIsbn(isbn);
        setListName(listName);
    }

    public FavoriteBean(String listName){
        setListName(listName);
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }
}
