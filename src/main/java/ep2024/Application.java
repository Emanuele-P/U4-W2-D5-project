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

        removeElementFromCatalogue("6029950737040", initialCatalogue);

        findElementByIsbn("8762950430408", initialCatalogue);

        findElementsByYear(1956, initialCatalogue);
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

        for (int i = 0; i < 3; i++) {
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
