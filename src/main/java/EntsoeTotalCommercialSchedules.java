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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.TreeMap;

public class EntsoeTotalCommercialSchedules {
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    private final String securityToken;
    private LocalDateTime unavailableDay = null;
    private LocalDateTime wasUnavailableAt = LocalDateTime.MIN;
    private TreeMap<LocalDateTime, Double> schedule = new TreeMap<>();


    public EntsoeTotalCommercialSchedules(String securityToken) {
        this.securityToken = securityToken;
    }


    public TreeMap getTotalGeneration(String zone, String zone2) {
        schedule.clear();
        LocalDateTime time = LocalDateTime.now().minusDays(1);

        final LocalDateTime periodStart = time.truncatedTo(ChronoUnit.DAYS);
        final LocalDateTime periodEnd = time.truncatedTo(ChronoUnit.DAYS).plusDays(1);
        try {
            URL url = new URL("https://transparency.entsoe.eu/api?securityToken=" +
                    securityToken + "&documentType=A09&in_Domain="
                    + zone + "&out_Domain=" + zone2 + "&periodStart=" + periodStart.format(DATE_FORMAT) +
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
        return schedule;
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
                            schedule.put(timestamp, Double.parseDouble(data));
                            break;
                    }
                    break;
            }
        }
        reader.close();

    }
}
