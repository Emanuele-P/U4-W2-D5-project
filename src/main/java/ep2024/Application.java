package ep2024;

import com.github.javafaker.Faker;
import ep2024.entities.Article;
import ep2024.entities.Book;
import ep2024.entities.Catalogue;
import ep2024.enums.Release;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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


        try {
            saveToDisk(initialCatalogue);
            System.out.println(System.lineSeparator() + "File successfully written!");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        try {
            List<Catalogue> loadCatalogue = loadFromDisk();
            System.out.println(System.lineSeparator() + "File successfully loaded!");
            System.out.println("Loaded catalogue: ");
            loadCatalogue.forEach(System.out::println);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void addElementToCatalogue(List<Catalogue> catalogue, Catalogue item) {
        catalogue.add(item);
        System.out.println(System.lineSeparator() + "'" + item.getTitle() + "' successfully added!");
        System.out.println("Updating catalogue...");

        catalogue.forEach(System.out::println);
    }

    public static void removeElementFromCatalogue(String isbn, List<Catalogue> catalogue) {
        boolean found = catalogue.removeIf(item -> item.getIsbn().equals(isbn));
        if (found) {
            System.out.println(System.lineSeparator() + "Element with ISBN " + isbn + " successfully removed!");
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

    public static void saveToDisk(List<Catalogue> catalogue) throws IOException {
        StringBuilder toWrite = new StringBuilder();

        for (Catalogue item : catalogue) {
            toWrite.append(item.getIsbn())
                    .append("@")
                    .append(item.getTitle())
                    .append("@")
                    .append(item.getYear())
                    .append("@")
                    .append(item.getPages());
            if (item instanceof Book book) {
                toWrite.append("@")
                        .append(book.getAuthor())
                        .append("@")
                        .append(book.getGenre());
            }
            if (item instanceof Article article) {
                toWrite.append("@")
                        .append(article.getRelease());
            }
            toWrite.append("#");
        }
        File file = new File("src/main/bibliographic_catalogue.txt");
        FileUtils.writeStringToFile(file, toWrite.toString(), "UTF-8");
    }

    public static List<Catalogue> loadFromDisk() throws IOException {
        File file = new File("src/main/bibliographic_catalogue.txt");
        String fileString = FileUtils.readFileToString(file, "UTF-8");
        List<String> splitElementString = Arrays.asList(fileString.split("#"));

        return splitElementString.stream().map(string -> {
            String[] catalogueInfo = string.split("@");
            String isbn = catalogueInfo[0];
            String title = catalogueInfo[1];
            int year = Integer.parseInt(catalogueInfo[2]);
            int pages = Integer.parseInt(catalogueInfo[3]);

            if (catalogueInfo.length == 6) {
                String author = catalogueInfo[4];
                String genre = catalogueInfo[5];
                return new Book(isbn, title, year, pages, author, genre);
            } else if (catalogueInfo.length == 5) {
                Release release = Release.valueOf(catalogueInfo[4]);
                return new Article(isbn, title, year, pages, release);
            } else {
                System.err.println("Invalid catalogue information");
                return null;
            }
        }).toList();

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
