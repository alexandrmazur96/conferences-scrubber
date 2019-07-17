package entities;

import java.util.zip.CRC32;

public class Conference implements Comparable<Conference> {
    private String name;
    private String url;
    private String startDate;
    private String endDate;
    private String city;
    private String country;
    private String twitter;
    private String cfpUrl;
    private String cfpStartDate;
    private String cfpEndDate;
    private int year;
    private String firebaseId;

    public Conference(String name, String url, String startDate, String endDate, String city, String country, String twitter, String cfpUrl, String cfpStartDate, String cfpEndDate, int year, String firebaseId) {
        this.name = name;
        this.url = url;
        this.startDate = startDate;
        this.endDate = endDate;
        this.city = city;
        this.country = country;
        this.twitter = twitter;
        this.cfpUrl = cfpUrl;
        this.cfpStartDate = cfpStartDate;
        this.cfpEndDate = cfpEndDate;
        this.year = year;
        this.firebaseId = firebaseId;
    }

    public Conference(String name, String url, String startDate, String endDate, String city, String country, String twitter, String cfpUrl, String cfpStartDate, String cfpEndDate, int year) {
        this.name = name;
        this.url = url;
        this.startDate = startDate;
        this.endDate = endDate;
        this.city = city;
        this.country = country;
        this.twitter = twitter;
        this.cfpUrl = cfpUrl;
        this.cfpStartDate = cfpStartDate;
        this.cfpEndDate = cfpEndDate;
        this.year = year;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public int getYear() {
        return this.year;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getCfpUrl() {
        return cfpUrl;
    }

    public String getCfpStartDate() {
        return cfpStartDate;
    }

    public String getCfpEndDate() {
        return cfpEndDate;
    }

    public String toString() {

        return "[" + System.lineSeparator() +
                "Name: " + this.getName() + System.lineSeparator() +
                "URL: " + this.getUrl() + System.lineSeparator() +
                "Start date: " + this.getStartDate() + System.lineSeparator() +
                "End date: " + this.getEndDate() + System.lineSeparator() +
                "Twitter: " + this.getTwitter() + System.lineSeparator() +
                "Country: " + this.getCountry() + System.lineSeparator() +
                "City: " + this.getCity() + System.lineSeparator() +
                "Cfp URL: " + this.getCfpUrl() + System.lineSeparator() +
                "Cfp start date: " + this.getCfpStartDate() + System.lineSeparator() +
                "Cfp end date: " + this.getCfpEndDate() + System.lineSeparator() +
                "]";
    }

    @Override
    public int compareTo(Conference conf) {
        /* todo: need to check this condition */
        if (
                this.name.equals(conf.getName()) &&
                        this.year == conf.getYear() &&
                        this.startDate.equals(conf.getStartDate()) &&
                        this.endDate.equals(conf.getEndDate()) &&
                        this.country.equals(conf.getCountry()) &&
                        this.city.equals(conf.getCity())
        ) {
            return 0;
        }

        return (this.year > conf.getYear() && this.startDate.compareTo(conf.getStartDate()) == 1) ? 1 : -1;
    }

    public int hashCode() {
        byte[] bytes = this.toString().getBytes();
        CRC32 checksum = new CRC32();
        checksum.update(bytes, 0, bytes.length);

        return (int) checksum.getValue();
    }
}