package it.ispw.booknook.logic.boundary.secondary_view;

import it.ispw.booknook.logic.bean.BookBean;
import it.ispw.booknook.logic.bean.LibraryBean;
import it.ispw.booknook.logic.bean.ShiftBean;
import it.ispw.booknook.logic.control.ConsultationController;
import java.util.List;
import java.util.Locale;

public class ConsultationCLController {

    public void reserveConsultation() {
        InOut.printLine("*** Insert title or author you want to consult ***\n");

        String searchedText = "";

        while (searchedText.isEmpty()) {
            InOut.print("Title or author: ");
            searchedText = InOut.readLine();
        }

        BookBean searchedBook = new BookBean(searchedText);
        ConsultationController consultationController = new ConsultationController();
        List<BookBean> results = consultationController.searchBook(searchedBook);

        InOut.printLine("*** Here are some books matching with your search ***\n");
        for (int i = 1; i <= results.size(); i++) {
            InOut.printLine(i + ") " + results.get(i - 1).getTitle() + ", " + results.get(i - 1).getAuthor() + " [" + results.get(i-1).getPublishingYear() + "]");
        }

        int bookChoice = InOut.multiIntChoice(results.size());
        //libro scelto dall'utente
        BookBean bookToConsult = results.get(bookChoice - 1);
        InOut.printLine("*** " + bookToConsult.getTitle() + " is available in the following libraries ***\n");
        List<LibraryBean> libraries = consultationController.checkLibraries(bookToConsult);

        chooseLibrary(libraries, consultationController);
    }

    private void chooseLibrary(List<LibraryBean> libraries, ConsultationController controller) {
        for (int i = 1; i <= libraries.size(); i++) {
            InOut.printLine(i + ") " + libraries.get(i - 1).getName() + " [" + libraries.get(i-1).getAddress() + ", " + libraries.get(i-1).getCity() + ", " + libraries.get(i-1).getOpeningTime() + "-" + libraries.get(i-1).getClosingTime() + "]");
        }
        //liberia scelta dall'utente
        int libraryChoice = InOut.multiIntChoice(libraries.size());
        LibraryBean selectedLibrary = libraries.get(libraryChoice-1);
        InOut.printLine("*** Choose a date for consultation in " + selectedLibrary.getName() + " ***\n");

        String date = "";
        //verifica che la data non sia vuota o con un formato errato
        while (date.isEmpty() || !InOut.isValidFormat("yyyy-MM-dd", date, Locale.US)) {
            InOut.print("Date[yyyy-MM-dd]: ");
            date = InOut.readLine();
        }
        ShiftBean selectedDate = new ShiftBean();
        selectedDate.setDateString(date);
        selectedDate.setUsernameLibrary(selectedLibrary.getUsername());
        List<ShiftBean> shifts = controller.getAvailableShifts(selectedDate);

        InOut.printLine("*** Consultation shifts available in " + selectedLibrary.getName() + " ***\n");
        for (int i = 1; i <= shifts.size(); i++) {
            InOut.printLine(i + ") " + shifts.get(i - 1).getTime());
        }

        int shiftChoice = InOut.multiIntChoice(shifts.size());

        ShiftBean currentShift = shifts.get(shiftChoice-1);

        //invoca il controller applicativo per effettuare prenotazione
        controller.reserveConsultation(currentShift, selectedLibrary);

        InOut.printLine("*** You succesfully reserve a consultation in " + selectedLibrary.getName() + " on " + currentShift.getDate() + ", " +  currentShift.getTime() + " ***\n");
    }
}
