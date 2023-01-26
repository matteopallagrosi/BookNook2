package it.ispw.booknook.logic.control;


import it.ispw.booknook.logic.bean.LibraryBean;
import it.ispw.booknook.logic.bean.ReviewBean;
import it.ispw.booknook.logic.database.dao.ReviewDao;
import it.ispw.booknook.logic.entity.Rate;
import it.ispw.booknook.logic.entity.Review;

import java.util.ArrayList;
import java.util.Date;

public class ReviewController {

    public ArrayList<ReviewBean> getReviews(LibraryBean library) {
        String username = library.getUsername();
        ArrayList<Review> reviews = ReviewDao.getReviews(username);
        //calcola numero medio di rate
        ArrayList<ReviewBean> reviewBeans = new ArrayList<>();
        if (reviews != null) {
            for (Review review : reviews) {
                ReviewBean bean = new ReviewBean();
                bean.setUsername(review.getUsername());
                bean.setText(review.getContent());
                bean.setRate(review.getRate());
                bean.setTitle(review.getTitle());
                bean.setMediumRate(review.getRate().getMediumRate());
                bean.setDate(review.getDate());
                reviewBeans.add(bean);
            }
        }
        return reviewBeans;
    }

    public void addReview(ReviewBean reviewToAdd, LibraryBean library) {
        Review review = new Review();
        review.setTitle(reviewToAdd.getTitle());
        review.setContent(reviewToAdd.getText());
        review.setUsername(reviewToAdd.getUsername());
        review.setDate(new Date());
        Rate rate = new Rate();
        rate.setAvailability(reviewToAdd.getAvailabilityRate());
        rate.setService(reviewToAdd.getServiceRate());
        rate.setLocation(reviewToAdd.getLocationRate());
        review.setRate(rate);
        ReviewDao.addReview(review, library.getUsername());
    }
}
