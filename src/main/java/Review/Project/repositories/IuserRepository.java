package Review.Project.repositories;

import Review.Project.Models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IuserRepository extends JpaRepository<UserModel, Long> {

    @Query("From UserModel where name = :name")
    UserModel findByName(String name);
    @Query("From UserModel where name = :name and pass = :pass")
    UserModel logIn(String name, String pass);


}
