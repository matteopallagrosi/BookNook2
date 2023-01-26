package it.ispw.booknook.logic.boundary.main_view;

import it.ispw.booknook.logic.entity.Club;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ClubCell {

    @FXML
    private Button exitBtn;

    @FXML
    private Label clubName;

    @FXML
    private Label description;

    @FXML
    private ImageView logo;

    @FXML
    private AnchorPane cell;

    @FXML
    private Label numMembers;


    public ClubCell()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/ispw/booknook/mainView/clubCell.fxml"));
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void setInfo(Club club)
    {
        clubName.setText(club.getName());
        description.setText(club.getDescription());
        numMembers.setText(club.getNumMembers().toString() + " members");
    }



    public AnchorPane getCell()
    {
        return cell;
    }

}
