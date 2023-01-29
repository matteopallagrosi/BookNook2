package it.ispw.booknook.logic.boundary.secondary_view;

import it.ispw.booknook.logic.bean.LibraryBean;
import it.ispw.booknook.logic.bean.ReviewBean;
import it.ispw.booknook.logic.control.ReviewController;

import java.util.List;

public class ReviewCLController{

    public void writeAReview(LibraryBean library) {
        InOut.printLine("*** Write a review ***\n");
        String title = "";
        String text = "";

        while (title.isEmpty()) {
            InOut.print("Title: ");
            title = InOut.readLine();
        }

        while (text.isEmpty()) {
            InOut.print("Text: ");
            text = InOut.readLine();
        }

        InOut.printLine("*** Rate library's services ***\n");
        String availability = "";
        String services = "";
        String location = "";

        while (availability.isEmpty() || Integer.parseInt(availability) < 1 ||  Integer.parseInt(availability) > 5) {
            InOut.print("Availability [1-5]: ");
            availability = InOut.readLine();
        }
        while (services.isEmpty() || Integer.parseInt(services) < 1 ||  Integer.parseInt(services) > 5) {
            InOut.print("Services [1-5]: ");
            services = InOut.readLine();
        }
        while (location.isEmpty() || Integer.parseInt(location) < 1 ||  Integer.parseInt(location) > 5) {
            InOut.print("Location [1-5]: ");
            location = InOut.readLine();
        }
        ReviewBean reviewBean = new ReviewBean();
        reviewBean.setTitle(title);
        reviewBean.setText(text);
        reviewBean.setAvailability(availability);
        reviewBean.setService(services);
        reviewBean.setLocation(location);


        ReviewController reviewController = new ReviewController();
        reviewController.addReview(reviewBean, library);

        InOut.printLine("*** Review correctly added ***\n");

    }


    public void showReviews(LibraryBean library) {
        InOut.printLine("*** Read reviews ***\n");

        ReviewController reviewController = new ReviewController();
        List<ReviewBean> reviews = reviewController.getReviews(library);

        for (int i = 1; i <= reviews.size(); i++) {
            ReviewBean review = reviews.get(i-1);
            InOut.printLine("Creator: " + review.getUsername());
            InOut.printLine("Reviewed on: " + review.getDate());
            InOut.printLine("Title: " + review.getTitle());
            InOut.print("Content: ");
            String[] strings = InOut.splitStringFixedLen(review.getText(), 50);
            for (String string : strings) {
                InOut.printLine(string);
            }
            InOut.printLine("Services [1-5]: " + review.getServiceRate());
            InOut.printLine("Availability [1-5]: " + review.getAvailabilityRate());
            InOut.printLine("Location [1-5]: " + review.getLocationRate());
            InOut.printLine("Medium rate [1-5]: " + review.getMediumRate());
            InOut.printLine("***********************************************************");
        }
    }

}
