package ep2024.entities;

public class Book extends Catalogue {

    private String author;
    private String genre;

    public Book(String isbn, String title, int year, int pages, String author, String genre) {
        super(isbn, title, year, pages);
        this.author = author;
        this.genre = genre;
    }


}
