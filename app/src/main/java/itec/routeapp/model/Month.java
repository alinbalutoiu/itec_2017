package itec.routeapp.model;

/**
 * Created by Mihaela Ilin on 4/8/2017.
 */

public enum Month {

    JANUARY("January"), FEBRUARY("February"), MARCH("March"), APRIL("April"), MAY("May"),
    JUNE("June"), JULY("July"), AUGUST("August"), SEPTEMBER("September"), OCTOBER("October"),
    NOVEMBER("November"), DECEMBER("December");

    private String title;

    Month(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
