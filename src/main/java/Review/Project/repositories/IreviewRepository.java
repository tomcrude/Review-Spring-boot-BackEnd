package Review.Project.repositories;

import Review.Project.Models.ReviewModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;


@Repository
public interface IreviewRepository extends JpaRepository<ReviewModel, Long> {

    @Query(value = "SELECT id,title,des,user FROM reviews WHERE user = :id and NOT id = :reviewId",
            nativeQuery = true)
    Optional<ArrayList<Map>> findByUser(Long id, Long reviewId);

    @Query(value = "SELECT id,title,des,user FROM reviews",
            nativeQuery = true)
    Optional<ArrayList> findAllReviews();

    @Query(value = "SELECT reviews.id,title,des,user,peoples.name FROM reviews INNER JOIN peoples ON reviews.user = peoples.id WHERE reviews.id = :id",
    nativeQuery = true)
    Optional<Map> findReview(String id);

}
