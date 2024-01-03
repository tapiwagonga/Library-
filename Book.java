import java.util.ArrayList;
import java.util.List;


public class Book {
    private String title;
    private String author;
    private String content;
    private List<Review> reviews;




    public Book(String title, String author, String content) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.reviews = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getBookContent() {
        return content;
    }
    public List<Review> getReviews() {
        return reviews;
    }
    public void addReview(Review review) {
        reviews.add(review);
    }


    public void deleteBook() {

    }
}