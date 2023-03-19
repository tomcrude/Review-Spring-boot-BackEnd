package Review.Project.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reviews")
public class ReviewModel {

    @Getter @Setter @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    @Lob @Getter @Setter
    private byte[] img;
    @Column @Getter @Setter
    private String title;
    @Column @Getter @Setter
    private Long user;
    @Column @Getter @Setter
    private String des;

}
