package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.bean.BookBean;
import it.ispw.booknook.logic.control.ReadingListController;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;

public class FavoritesViewCell extends ListCell<BookBean> {

    @Override
    public void updateItem(BookBean book, boolean empty)
    {
        super.updateItem(book,empty);

        if(book != null && !empty)
        {
            FavoritesCell data = new FavoritesCell();
            book.attach(data);
            //setta contenuto della cella
            data.setInfo(book);
            //setta aspetto grafico della cella
            setGraphic(data.getCell());

            Button removeButton = (Button) data.getCell().lookup("#removeBtn");
            removeButton.setOnAction(event -> {
                ReadingListController readingListController = new ReadingListController();
                //rimuove dal db il libro
                readingListController.deleteFromReadingList(book);
                //rimuove l'elemento dalla listView (observableList si aggiorna di conseguenza)
                getListView().getItems().remove(getItem());
            });
        }
        else {
            setGraphic(null);
        }
    }
}
