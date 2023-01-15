package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.bean.LibraryBean;
import javafx.scene.control.ListCell;

public class LibraryViewCell extends ListCell<LibraryBean> {

    @Override
    public void updateItem(LibraryBean library, boolean empty)
    {
        super.updateItem(library,empty);
        if(library != null && !empty)
        {

                //recuperare l'informazione se almeno una copia del libro è presente in questa libreria
                LibraryCell data = new LibraryCell();
                //setta contenuto della cella
                data.setInfo(library);
                //setta aspetto grafico della cella
                setGraphic(data.getCell());
        }
        else {
            setGraphic(null);
        }
    }
}
