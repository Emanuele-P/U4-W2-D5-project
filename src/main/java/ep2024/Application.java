package ep2024;

import com.github.javafaker.Faker;
import ep2024.entities.Article;
import ep2024.entities.Book;
import ep2024.entities.Catalogue;
import ep2024.enums.Release;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Application {

    public static void main(String[] args) {

        List<Catalogue> initialCatalogue = generateInitialCatalogue();
//        System.out.println("BIBLIOGRAPHIC CATALOGUE:");
//        for (Catalogue item : initialCatalogue) {
//            System.out.println(item);
//        }

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

        Scanner sc = new Scanner(System.in);
        int menuChoice;

        do {
            System.out.println(System.lineSeparator() + "--------MAIN MENU--------");
            System.out.println("1 - View complete catalogue");
            System.out.println("2 - Add a new book");
            System.out.println("3 - Add a new article");
            System.out.println("4 - Remove an item by ISBN");
            System.out.println("5 - Find an item by ISBN");
            System.out.println("6 - Find an item by Year");
            System.out.println("7 - Find books by Author");
            System.out.println("0 - Exit");
            System.out.println("Select a number: ");
            menuChoice = sc.nextInt();
            sc.nextLine();

            switch (menuChoice) {
                case 1:
                    System.out.println("--------COMPLETE CATALOGUE--------");
                    initialCatalogue.forEach(System.out::println);
                    break;
                case 2:
                    System.out.println("--------ADD A NEW BOOK--------");
                    System.out.println("Enter ISBN: ");
                    String isbn = sc.nextLine();
                    System.out.println("Enter title: ");
                    String title = sc.nextLine();
                    System.out.println("Enter year: ");
                    int year = sc.nextInt();
                    System.out.println("Enter number of pages: ");
                    int pages = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter name of the author: ");
                    String author = sc.nextLine();
                    System.out.println("Enter genre: ");
                    String genre = sc.nextLine();
                    Book book = new Book(isbn, title, year, pages, author, genre);
                    addElementToCatalogue(initialCatalogue, book);
                    break;
                case 3:
                    System.out.println("--------ADD A NEW ARTICLE--------");
                    System.out.println("Enter ISBN: ");
                    String isbn2 = sc.nextLine();
                    System.out.println("Enter title: ");
                    String title2 = sc.nextLine();
                    System.out.println("Enter year: ");
                    int year2 = sc.nextInt();
                    System.out.println("Enter number of pages: ");
                    int pages2 = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter release period (WEEKLY/MONTHLY/SEMESTRAL)");
                    String releasePeriod = sc.nextLine();
                    Release release = Release.valueOf(releasePeriod.toUpperCase());
                    Article article = new Article(isbn2, title2, year2, pages2, release);
                    addElementToCatalogue(initialCatalogue, article);
                    break;
                case 4:
                    System.out.println("--------REMOVE ITEM BY ISBN--------");
                    System.out.print("Enter ISBN of item to remove: ");
                    String removeIsbn = sc.nextLine();
                    removeElementFromCatalogue(removeIsbn, initialCatalogue);
                    break;
                case 5:
                    System.out.println("--------FIND ITEM BY ISBN--------");
                    System.out.print("Enter ISBN to find: ");
                    String findIsbn = sc.nextLine();
                    findElementByIsbn(findIsbn, initialCatalogue);
                    break;
                case 6:
                    System.out.println("--------FIND ITEM BY YEAR--------");
                    System.out.print("Enter year to find: ");
                    int findYear = sc.nextInt();
                    findElementsByYear(findYear, initialCatalogue);
                    break;
                case 7:
                    System.out.println("--------FIND BOOK BY AUTHOR--------");
                    System.out.print("Enter author to find: ");
                    String findAuthor = sc.nextLine();
                    findBooksByAuthor(findAuthor, initialCatalogue);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid selection :( Please try again.");
            }
        } while (menuChoice != 0);

        sc.close();

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

        for (int i = 0; i < 5; i++) {
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
