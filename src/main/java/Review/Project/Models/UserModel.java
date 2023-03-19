package Review.Project.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "peoples")
public class UserModel {

  @Getter @Setter @Id
  @GeneratedValue(strategy =  GenerationType.IDENTITY)
  private Long id;
  @Column @Getter @Setter
  private String name;
  @Column @Getter @Setter
  private String pass;
  @Column @Getter @Setter
  private String review;

}
