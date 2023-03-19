package Review.Project.services;


import Review.Project.Models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Review.Project.repositories.IuserRepository;

import java.util.HashMap;
import java.util.Map;


@Service
public class UserServices {

  @Autowired
  IuserRepository repository;



  // CREATE USER
  public Map createUser(UserModel data, String Rpass){

      Map res = new HashMap();

      String name = data.getName();
      String pass = data.getPass();

      if (!data.getPass().equals(Rpass)){res.put("message", "Passwords must match."); return res;}
      if (name == null || pass == null ){res.put("message", "You must enter data."); return res;}
      if (name.length() > 13 || name.length() < 5 || pass.length() > 13 || pass.length() < 5){res.put("message", "The password and the name cannot be less than 4 nor more than 12 characters."); return res;}

      UserModel nameDB = repository.findByName(data.getName());
      if (nameDB != null){res.put("message", "The username has already been registered\n."); return res;}


      repository.save(data);
      res.put("message", "success");
      return res;
  }

  // LOG IN
  public Map logIn(UserModel data){

      String name = data.getName();
      String pass = data.getPass();

      Map res = new HashMap();

      if (name == null || pass == null){res.put("message", "You must enter data."); return res;}
      if (name.length() > 13 || name.length() < 5 || pass.length() > 13 || pass.length() < 5){res.put("message", "The password and the name cannot be less than 4 nor more than 12 characters."); return res;}

      UserModel info = repository.logIn(data.getName(), data.getPass());

      if (info == null){res.put("message", "The data entered is incorrect."); return res;}

      res.put("message", "success");
      res.put("id", info.getId());
      res.put("user", info.getName());

      return res;
  }


}
