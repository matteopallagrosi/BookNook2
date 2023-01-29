import it.ispw.booknook.logic.control.BorrowBookController;
import it.ispw.booknook.logic.entity.Book;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class TestBorrowBookController {

    @Test
    void testSortByOccurancesGeneralList() {
        BorrowBookController borrowBookController = new BorrowBookController();
        Book firstBook = new Book("1");
        Book secondBook = new Book("1");
        Book thirdBook = new Book("1");
        Book fourthBook = new Book("1");
        Book fifthBook = new Book("2");
        Book sixthBook = new Book("2");
        Book seventhBook = new Book("3");
        List<Book> books = new ArrayList<>();
        books.add(firstBook);
        books.add(secondBook);
        books.add(thirdBook);
        books.add(fourthBook);
        books.add(fifthBook);
        books.add(sixthBook);
        books.add(seventhBook);
        List<Book> sortedList = borrowBookController.sortByOccurrences(books);
        List<String> sortedIsbn = new ArrayList<>();
        sortedIsbn.add("1");
        sortedIsbn.add("2");
        sortedIsbn.add("3");
        List<String> actualIsbn = new ArrayList<>();
        sortedList.forEach(book -> actualIsbn.add(book.getIsbn()));
        assertLinesMatch(sortedIsbn, actualIsbn);
    }

    @Test
    void testSortByOccurancesJustOne() {
        Book first = new Book("1");
        Book second = new Book("2");
        Book third = new Book("3");
        List<Book> books = new ArrayList<>();
        books.add(first);
        books.add(second);
        books.add(third);
        Book []inputList = new Book[] {first, second, third};
        BorrowBookController borrowBookController = new BorrowBookController();
        List<Book> sortedList = borrowBookController.sortByOccurrences(books);
        Book []sorted = new Book[3];
        for (int i = 0; i<sortedList.size(); i++) {
            sorted[i] = sortedList.get(i);
        }
        assertArrayEquals(inputList, sorted);
    }
}
