package ep2024.entities;

public class Book extends Catalogue {

    private String author;
    private String genre;

    public Book(String isbn, String title, int year, int pages, String author, String genre) {
        super(isbn, title, year, pages);
        this.author = author;
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Book {" +
                super.toString() +
                " author: '" + author + '\'' +
                ", genre: '" + genre + '\'' +
                '}';
    }
}
