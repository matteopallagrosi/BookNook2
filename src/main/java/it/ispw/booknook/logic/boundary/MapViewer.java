package it.ispw.booknook.logic.boundary;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import javafx.scene.image.Image;

public class MapViewer {

    private ArcGISMap map;
    private MapView mapView;
    private double currentLatitude;
    private double currentLongitude;
    private GraphicsOverlay graphicsOverlay;

    private static final String MARKER_SYMBOL = "C:\\Users\\HP\\IdeaProjects\\BookNook\\src\\main\\resources\\it\\ispw\\booknook\\mainView\\icons8-segnaposto-40.png";

    public MapView createMap(double latitude, double longitude) {
        //crea la finestra che conterrà la mappa
        mapView = new MapView();
        //crea la mappa da visualizzare
        map = new ArcGISMap(BasemapStyle.ARCGIS_STREETS);
        mapView.setMap(map);
        //setta la posizione mostrata al lancio della mappa
        this.currentLatitude = latitude;
        this.currentLongitude =longitude;
        mapView.setViewpoint(new Viewpoint(currentLatitude, currentLongitude, 5000));
        //crea un indicatore sulla posizione corrente
        createMarkerAtCurrentPos();

        return mapView;
    }

    //crea un marker centrato sulla posizione corrente
    private void createMarkerAtCurrentPos() {
        // create il contenitore grafico per il marker
        graphicsOverlay = new GraphicsOverlay();

        // crea il punto
        Point point = new Point(currentLongitude, currentLatitude, SpatialReferences.getWgs84());

        // crea un puntatore da usare come marker
        Image markerImage = new Image(MARKER_SYMBOL);
        PictureMarkerSymbol markerSymbol = new PictureMarkerSymbol(markerImage);
        // crea un oggetto che conterrà il Punto e il marker
        Graphic markerGraphic = new Graphic(point, markerSymbol);

        graphicsOverlay.getGraphics().add(markerGraphic);
        mapView.getGraphicsOverlays().add(graphicsOverlay);
    }

    public void changePosition(double newLatitude, double newLongitude) {
        this.currentLatitude = newLatitude;
        this.currentLongitude = newLongitude;
        mapView.setViewpoint(new Viewpoint(currentLatitude, currentLongitude, 5000));
        graphicsOverlay.getGraphics().clear();
        createMarkerAtCurrentPos();
    }
}
