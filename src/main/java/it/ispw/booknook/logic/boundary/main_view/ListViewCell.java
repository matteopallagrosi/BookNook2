package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.bean.BookBean;
import javafx.scene.control.*;

public class ListViewCell extends ListCell<BookBean>
{
    @Override
    public void updateItem(BookBean book, boolean empty)
    {
        super.updateItem(book,empty);

        if(book != null && !empty)
        {
            BookCell data = new BookCell();
            book.attach(data);
            //setta contenuto della cella
            data.setInfo(book);
            //setta aspetto grafico della cella
            setGraphic(data.getCell());
        }
    }
}
