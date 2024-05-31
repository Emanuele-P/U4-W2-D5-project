package ep2024.entities;

import ep2024.enums.Release;

public class Article extends Catalogue {

    private Release release;

    public Article(String isbn, String title, int year, int pages, Release release) {
        super(isbn, title, year, pages);
        this.release = release;
    }

    public Release getRelease() {
        return release;
    }

    public void setRelease(Release release) {
        this.release = release;
    }
}
