package entities;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.zip.CRC32;

public class Conference {
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
    private String conferenceDigest;

    /**
     * Create conference entity from firebase db (firebaseId required).
     */
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
        this.makeMd5Digest();
    }

    /**
     * Create conference entity which not presented in firebase DB yet.
     */
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
        this.makeMd5Digest();
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

    public String getConferenceDigest() {
        return conferenceDigest;
    }

    public void setId(String firebaseId) {
        this.firebaseId = firebaseId;
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

    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Conference conf = (Conference) o;

        return this.name.equals(conf.getName()) &&
                this.year == conf.getYear() &&
                this.startDate.equals(conf.getStartDate()) &&
                this.endDate.equals(conf.getEndDate()) &&
                this.country.equals(conf.getCountry()) &&
                this.city.equals(conf.getCity()) &&
                this.url.equals(conf.getUrl()) &&
                this.cfpUrl.equals(conf.getCfpUrl()) &&
                this.cfpStartDate.equals(conf.getCfpStartDate()) &&
                this.cfpEndDate.equals(conf.getCfpEndDate());
    }

    public int hashCode() {
        byte[] bytes = (this.getName() + this.getYear() + this.getCity() + this.getCountry()).getBytes();
        CRC32 checksum = new CRC32();
        checksum.update(bytes, 0, bytes.length);

        return (int) checksum.getValue();
    }

    private void makeMd5Digest() {
        this.conferenceDigest = DigestUtils.md5Hex(this.toString());
    }
}