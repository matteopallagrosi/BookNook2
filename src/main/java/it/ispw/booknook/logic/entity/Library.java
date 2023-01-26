package it.ispw.booknook.logic.entity;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Library {

        private String username;
        private String name;
        private String address;
        private Time openingTime;
        private Time closingTime;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private String city;
        private List<Book> ownedBooks;
        private List<BookCopy> ownedCopies;
        private List<ConsultationShift> shifts = null;

        public Library(){}

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Library(String name) {
                this.name = name;
            }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Time getOpeningTime() {
            return openingTime;
        }

        public void setOpeningTime(Time openingTime) {
            this.openingTime = openingTime;
        }

        public Time getClosingTime() {
            return closingTime;
        }

        public void setClosingTime(Time closingTime) {
            this.closingTime = closingTime;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public List<Book> getOwnedBooks() {
            return ownedBooks;
        }

        public void setOwnedBooks(List<Book> ownedBooks) {
            this.ownedBooks = ownedBooks;
        }

        public BigDecimal getLatitude() {
            return latitude;
        }

        public void setLatitude(BigDecimal latitude) {
            this.latitude = latitude;
        }

        public BigDecimal getLongitude() {
            return longitude;
        }

        public void setLongitude(BigDecimal longitude) {
            this.longitude = longitude;
        }

        public List<BookCopy> getOwnedCopies() {
            return ownedCopies;
        }

        public void setOwnedCopies(List<BookCopy> ownedCopies) {
            this.ownedCopies = ownedCopies;
        }

        public List<ConsultationShift> getShifts() {
            return shifts;
        }

        public void setShifts(List<ConsultationShift> shifts) {
            this.shifts = shifts;
        }

        public void addShift(ConsultationShift shift) {
            if (shifts == null) this.shifts = new ArrayList<>();
            this.shifts.add(shift);
        }

        public void addBook(Book book){
                    if(ownedBooks == null){
                        ownedBooks = new ArrayList<Book>();
                    }
                    ownedBooks.add(book);
                }

            public void addCopy(BookCopy copy) {
                if(ownedCopies == null){
                    ownedCopies = new ArrayList<BookCopy>();
                }
                ownedCopies.add(copy);
            }

            //verifica la disponibilità di copie del libro con l'ISBN richiesto presso la libreria
            public boolean getAvailability(String isbn) {
                for (int i=0; i<ownedCopies.size(); i++) {
                    BookCopy copy = ownedCopies.get(i);
                    if (copy.getBook().getIsbn().equals(isbn) && copy.getState().equals(CopyState.AVAILABLE)) {
                        return true;
                    }
                }
                return false;
            }
}


