package ep2024;

import com.github.javafaker.Faker;
import ep2024.entities.Article;
import ep2024.entities.Book;
import ep2024.entities.Catalogue;
import ep2024.enums.Release;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Application {

    public static void main(String[] args) {

        List<Catalogue> initialCatalogue = generateInitialCatalogue();
        System.out.println("BIBLIOGRAPHIC CATALOGUE:");
        for (Catalogue item : initialCatalogue) {
            System.out.println(item);
        }

        Book newBook = new Book("8762950430408", "Snow country", 1956, 346, "Yasunari Kawabata", "Novel");
        addElementToCatalogue(initialCatalogue, newBook);
        Article newArticle = new Article("6029950737040", "A Swiftly Tilting Planet", 2012, 133, Release.SEMESTRAL);
        addElementToCatalogue(initialCatalogue, newArticle);
        Book newBook2 = new Book("4729385627103", "1984", 1949, 328, "George Orwell", "Dystopian");
        addElementToCatalogue(initialCatalogue, newBook2);
        Book newBook3 = new Book("9354738291564", "Animal Farm", 1945, 92, "George Orwell", "Political satire");
        addElementToCatalogue(initialCatalogue, newBook3);


        removeElementFromCatalogue("6029950737040", initialCatalogue);

        findElementByIsbn("8762950430408", initialCatalogue);

        findElementsByYear(1956, initialCatalogue);

        findBooksByAuthor("George Orwell", initialCatalogue);
    }

    public static void addElementToCatalogue(List<Catalogue> catalogue, Catalogue item) {
        catalogue.add(item);
        System.out.println();
        System.out.println("'" + item.getTitle() + "' successfully added!");
        System.out.println("Updating catalogue...");

        catalogue.forEach(System.out::println);
    }

    public static void removeElementFromCatalogue(String isbn, List<Catalogue> catalogue) {
        boolean found = catalogue.removeIf(item -> item.getIsbn().equals(isbn));
        if (found) {
            System.out.println();
            System.out.println("Element with ISBN " + isbn + " successfully removed!");
            System.out.println("Updating catalogue...");

            catalogue.forEach(System.out::println);
        } else {
            System.out.println("No elements found for the ISBN " + isbn + ". Cannot remove");
        }
    }

    public static void findElementByIsbn(String isbn, List<Catalogue> catalogue) {
        Map<String, List<Catalogue>> isbnCatalogue = catalogue.stream()
                .collect(Collectors.groupingBy(Catalogue::getIsbn));

        List<Catalogue> isbnSearch = isbnCatalogue.get(isbn);
        if (isbnSearch != null) {
            System.out.println("-----Elements found by ISBN (" + isbn + "):");
            isbnSearch.forEach(System.out::println);
        } else {
            System.out.println("No elements found for the ISBN " + isbn);
        }
    }

    public static void findElementsByYear(int year, List<Catalogue> catalogue) {
        Map<Integer, List<Catalogue>> yearCatalogue = catalogue.stream()
                .collect(Collectors.groupingBy(Catalogue::getYear));

        List<Catalogue> yearSearch = yearCatalogue.get(year);
        if (yearSearch != null) {
            System.out.println("-----Elements found by Year (" + year + "):");
            yearSearch.forEach(System.out::println);
        } else {
            System.out.println("No elements found for the year " + year);
        }
    }

    public static void findBooksByAuthor(String author, List<Catalogue> catalogue) {
        Map<String, List<Book>> authorCatalogue = catalogue.stream()
                .filter(item -> item instanceof Book)
                .map(item -> (Book) item)
                .collect(Collectors.groupingBy(Book::getAuthor));

        List<Book> booksByAuthor = authorCatalogue.get(author);
        if (booksByAuthor != null) {
            System.out.println("-----Books found by Author (" + author + "):");
            booksByAuthor.forEach(System.out::println);
        } else {
            System.out.println("No books found by Author " + author);
        }
    }

    public static List<Catalogue> generateInitialCatalogue() {
        List<Catalogue> catalogue = new ArrayList<>();
        catalogue.addAll(generateBook());
        catalogue.addAll(generateArticle());
        return catalogue;
    }

    public static List<Book> generateBook() {
        Faker faker = new Faker();
        List<Book> books = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            String isbn = faker.code().isbn13();
            String title = faker.book().title();
            int year = faker.number().numberBetween(1900, 2024);
            int pages = faker.number().numberBetween(100, 900);
            String author = faker.book().author();
            String genre = faker.book().genre();

            Book book = new Book(isbn, title, year, pages, author, genre);
            books.add(book);
        }
        return books;
    }

    public static List<Article> generateArticle() {
        Faker faker = new Faker();
        List<Article> articles = new ArrayList<>();
        Release[] releases = Release.values();

        for (int i = 0; i < 5; i++) {
            String isbn = faker.code().isbn13();
            String title = faker.book().title();
            int year = faker.number().numberBetween(1900, 2024);
            int pages = faker.number().numberBetween(30, 250);
            Release release = releases[faker.random().nextInt(0, 2)];

            Article article = new Article(isbn, title, year, pages, release);
            articles.add(article);
        }
        return articles;
    }
}
