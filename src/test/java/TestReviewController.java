import it.ispw.booknook.logic.bean.ReviewBean;
import it.ispw.booknook.logic.control.ReviewController;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestReviewController {

    @Test
    void testCalculateMediumIntRate() {
        ReviewController reviewController = new ReviewController();
        List<ReviewBean> reviews = new ArrayList<>();
        ReviewBean firstReview = new ReviewBean();
        ReviewBean secondReview = new ReviewBean();
        ReviewBean thirdReview = new ReviewBean();
        firstReview.setMediumRate(5);
        secondReview.setMediumRate(3);
        thirdReview.setMediumRate(4);
        reviews.add(firstReview);
        reviews.add(secondReview);
        reviews.add(thirdReview);
        int medium = reviewController.calculateMedium(reviews);
        assertEquals(4, medium);
    }

    @Test
    void testCalculateMediumEmptyList() {
        ReviewController reviewController = new ReviewController();
        //nel caso la biblioteca non abbia recensioni
        List<ReviewBean> reviews = new ArrayList<>();
        int medium = reviewController.calculateMedium(reviews);
        assertEquals(0, medium);
    }

    @Test
    void testCalculateMediumDoubleRate() {
        ReviewController reviewController = new ReviewController();
        List<ReviewBean> reviews = new ArrayList<>();
        ReviewBean firstReview = new ReviewBean();
        ReviewBean secondReview = new ReviewBean();
        ReviewBean thirdReview = new ReviewBean();
        firstReview.setMediumRate(2);
        secondReview.setMediumRate(2);
        thirdReview.setMediumRate(3);
        reviews.add(firstReview);
        reviews.add(secondReview);
        reviews.add(thirdReview);
        int medium = reviewController.calculateMedium(reviews);
        assertEquals(2, medium);
    }
}
