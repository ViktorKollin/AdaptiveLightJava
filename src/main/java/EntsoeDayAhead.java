
import javax.net.ssl.HttpsURLConnection;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.TreeMap;


/**
 * Supplies day-ahead price information from the ENTSOE transparency API. The
 * data is cached.
 */
public class EntsoeDayAhead {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    private final TreeMap<LocalDateTime, Double> prices = new TreeMap<>();
    private final String areaEIC;
    private final ZoneId timezone;
    private final String securityToken;
    private int maxCacheSize = 5000;



    public EntsoeDayAhead(String areaEIC, ZoneId timezone, String securityToken) {
        this.areaEIC = areaEIC;
        this.timezone = timezone;
        this.securityToken = securityToken;
    }

    public TreeMap<LocalDateTime, Double> getCostForDayAhead(LocalDateTime time) {
        prices.clear();
        ZonedDateTime timeLocal = time.atZone(TimeUtils.UTC).withZoneSameInstant(timezone);
        final LocalDateTime dayStart = timeLocal.truncatedTo(ChronoUnit.DAYS).plusHours(15).toLocalDateTime();
        final LocalDateTime dayEnd = timeLocal.truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS).toLocalDateTime();

        getCostFromEntsoe(dayStart, dayEnd);
        cleanTreeMap();

        return prices;

    }

    protected void getCostFromEntsoe(LocalDateTime start, LocalDateTime end) {

        try {
            URL url = new URL("https://transparency.entsoe.eu/api?securityToken=" +
                    securityToken + "&documentType=A44" +
                    "&in_Domain=" + areaEIC + "&out_Domain=" + areaEIC +
                    "&periodStart=" + start.format(DATE_FORMAT) +
                    "&periodEnd=" + end.format(DATE_FORMAT));

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            if (conn.getResponseCode() == 400) {
                System.out.println("Server returned 400 BAD REQUEST");
            } else {
                try (InputStream input = conn.getInputStream()) {
                    loadXML(input);
                }
            }
        } catch (IOException | XMLStreamException ex) {
            System.out.println("Could not fetch data.");
        }
    }

    private void loadXML(InputStream source) throws XMLStreamException {
        XMLInputFactory xif = XMLInputFactory.newFactory();
        XMLEventReader reader = xif.createXMLEventReader(source);

        String currentElement = "";
        LocalDateTime startDate = null;
        LocalDateTime timestamp = null;

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    StartElement el = event.asStartElement();
                    currentElement = el.getName().getLocalPart();
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    currentElement = "";
                    break;

                case XMLStreamConstants.CHARACTERS:
                    String data = event.asCharacters().getData();
                    switch (currentElement) {
                        case "start":
                            // remove trailing Z:
                            if (data.endsWith("Z"))
                                data = data.substring(0, data.length() - 1);

                            startDate = LocalDateTime.parse(data);
                            break;
                        case "position":
                            if (startDate != null)
                                //TODo Måste ändras i simulering
                                timestamp = startDate.plusHours(Integer.parseInt(data) + 1);

                            break;
                        case "price.amount":
                            prices.put(timestamp, Double.parseDouble(data));
                            break;
                    }
                    break;
            }
        }
        reader.close();
    }


    protected TreeMap<LocalDateTime, Double> getPrices() {
        return new TreeMap<>(prices);
    }

    private void cleanTreeMap() {
        for (int i = 0; i <= 14; i++) {
            prices.pollFirstEntry();
        }
    }


}