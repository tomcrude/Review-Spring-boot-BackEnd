package Review.Project.services;

import Review.Project.Models.ReviewModel;
import Review.Project.Models.UserModel;
import Review.Project.repositories.IreviewRepository;
import Review.Project.repositories.IuserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class ReviewServices {

    @Value("${media.location}")
    private String mediaLocation;

    private Path rootLocation;

    private Path imgDirectory = Paths.get("src//main//resources//static//images");
    private String absoluteRoute = imgDirectory.toFile().getAbsolutePath();

    @Autowired
    IreviewRepository reviewRepository;

    @Autowired
    IuserRepository userRepository;

    @PostConstruct
    public void init() throws IOException {
        rootLocation = Paths.get(mediaLocation);
        Files.createDirectories(rootLocation);
    }

    // CREATE REVIEW
    public Map createReview(String title, String des, Long id, MultipartFile img){

        Map res = new HashMap();

        String type = img.getContentType();

        if (title == null || des == null){res.put("message", "You must enter data."); return res;}
        if (title.length() > 21 || title.length() < 5){res.put("message", "The title cannot be less than 4 nor more than 20 characters."); return res;}
        if (des.length() > 101 || des.length() < 11){res.put("message", "The description cannot be less than 10 nor more than 100 characters."); return res;}
        if (img.isEmpty()){res.put("message", "You must upload an image."); return res;}
        else if (img.getSize() > 600000){res.put("message", "The image must not weigh more than 6MB."); return res;}
        else if (!type.equals("image/png") && !type.equals("image/jpeg") && !type.equals("image/jpg")){res.put("message", "The image must be PNG - JPG - JPEG."); return res;}


        ReviewModel data = new ReviewModel();

        try {

            data.setTitle(title);
            data.setDes(des);
            data.setUser(id);
            data.setImg(img.getBytes());

            Long reviewId = reviewRepository.save(data).getId();

            UserModel userReview = userRepository.findById(id).get();

            String data404 = userReview.getReview() + "," + reviewId.toString();
            if (userReview.getReview() == null){
                userReview.setReview(reviewId.toString());
            }else{
                userReview.setReview(data404);
            }

            userRepository.save(userReview);

            Path destination = rootLocation.resolve(Paths.get(reviewId + "-image.png"))
                    .normalize()
                    .toAbsolutePath();

            try (InputStream input = img.getInputStream()){
                Files.copy(input,destination, StandardCopyOption.REPLACE_EXISTING);

            }



            res.put("id", reviewId);
            res.put("message", "success");

            return res;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // LOAD IMAGES

    public Resource loadImg(String name){


        try {
            Path file = rootLocation.resolve(name);
            Resource resource = new UrlResource((file.toUri()));

            if (resource.exists() || resource.isReadable()){
                return resource;
            }
            else {
                throw new RuntimeException("Could not read file " + name);
            }
        }
        catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file " + name);
        }
    }

    // GET REVIEWS
    public Optional<ArrayList> getReviews() {
        return (Optional<ArrayList>) reviewRepository.findAllReviews();
    }

    // GET SINGLE REVIEW
    public Optional<Map> getSingleReviews(String id) {

        return reviewRepository.findReview(id);
    }

    // GET USER REVIEWS
    public Optional<ArrayList<Map>> getUserReviews(Long id,Long reviewId) {


        Optional<ArrayList<Map>> data = reviewRepository.findByUser(id, reviewId);

        if (data.get().isEmpty()){return null;}

         return data;
    }

    // DELETE REVIEW
    public Map deleteReviews(Long id) {

        Map res = new HashMap();

        try
        {
            reviewRepository.deleteById(id);

            Path route = Paths.get(absoluteRoute + "//" + id + "-" + "image.png");

            Files.delete(route);

            res.put("message","success");
            return res;
        }
         catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // EDIT REVIEW
    public Map editReview(String title, String des, Long id, MultipartFile img, String userId) {

        Map res = new HashMap();

        String type = img.getContentType();

        if (title == null || des == null){res.put("message", "You must enter data."); return res;}
        if (title.length() > 21 || title.length() < 5){res.put("message", "The title cannot be less than 4 nor more than 20 characters."); return res;}
        if (des.length() > 101 || des.length() < 11){res.put("message", "The description cannot be less than 10 nor more than 100 characters."); return res;}
        else if (img.getSize() > 600000){res.put("message", "The image must not weigh more than 6MB."); return res;}
        else if (!type.equals("image/png") && !type.equals("image/jpeg") && !type.equals("image/jpg")){res.put("message", "The image must be PNG - JPG - JPEG."); return res;}


        ReviewModel data = reviewRepository.findById(id).get();

        if (data == null || !data.getUser().toString().equals(userId)){res.put("message", "error"); return res;}


        try {
            data.setTitle(title);
            data.setDes(des);
            data.setImg(img.getBytes());

            Long reviewId = reviewRepository.save(data).getId();

            byte[] imgByte = img.getBytes();

            Path route = Paths.get(absoluteRoute + "//" + reviewId + "-" + "image.png");

            Files.write(route,imgByte);

            res.put("id", data.getId());
            res.put("message", "success"); return res;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }




}
