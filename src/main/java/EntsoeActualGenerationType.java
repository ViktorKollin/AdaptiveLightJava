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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.TreeMap;


public class EntsoeActualGenerationType {
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    private final String securityToken;
    private TreeMap<LocalDateTime, Double> generationType = new TreeMap<>();
    private LocalDateTime unavailableDay = null;
    private LocalDateTime wasUnavailableAt = LocalDateTime.MIN;
    private Duration coolOffTime = Duration.ofMinutes(5);
    private int maxCacheSize = 5000;

    public EntsoeActualGenerationType(String securityToken) {

        this.securityToken = securityToken;
    }

    public TreeMap getGenerationType(String psrType, String zone) {

        LocalDateTime time = LocalDateTime.now();

        final LocalDateTime periodStart = time.truncatedTo(ChronoUnit.DAYS).minusDays(1);
        final LocalDateTime periodEnd = time.truncatedTo(ChronoUnit.DAYS);
        System.out.println(psrType + " " + periodStart.format(DATE_FORMAT) + " " + periodEnd.format(DATE_FORMAT));

        try {
            URL url = new URL("https://transparency.entsoe.eu/api?securityToken=" +
                    securityToken + "&documentType=A75&processType=A16&psrType=" + psrType +
                    "&in_Domain=" + zone + "&periodStart=" + periodStart.format(DATE_FORMAT) +
                    "&periodEnd=" + periodEnd.format(DATE_FORMAT));

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            if (conn.getResponseCode() == 400) {
                System.out.println(("Server returned 400 BAD REQUEST."));
                // cache error response for a certain time
                //unavailableDay = start;
                wasUnavailableAt = TimeUtils.now();
            } else {
                try (InputStream input = conn.getInputStream()) {
                    loadXML(input);
                }
            }
        } catch (IOException | XMLStreamException ex) {
            System.out.println("Could not fetch data.");

        }
        return generationType;
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
                                timestamp = startDate.plusHours(Integer.parseInt(data) + 1);


                            break;
                        case "quantity":
                            generationType.put(timestamp, Double.parseDouble(data));
                            break;
                    }
                    break;
            }
        }
        reader.close();
        // readTree();
    }

    public void readTree() {

        for (Map.Entry<LocalDateTime, Double>
                entry : generationType.entrySet())
            System.out.println(
                    "[" + entry.getKey()
                            + ", " + entry.getValue() + "]");
    }


}
