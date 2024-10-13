import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


public class HtmlRead {

    public static void main(String[] args) {
        HtmlRead html = new HtmlRead();
    }

    public HtmlRead() {

        try {
            System.out.println();
            URL url = new URL("https://www.milton.edu/");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream())
            );
            String line;
            while ( (line = reader.readLine()) != null ) {
//                if (line.contains("<!--")) {break;}
                if((line.contains("http") || line.contains("www.")) && line.contains("//")) {
                    int linkStart = line.contains("http") ?  line.indexOf("http") : line.indexOf("www");
                    String choppedLinkStart = line.substring(linkStart);

                    int linkEnd = choppedLinkStart.contains("'") ? line.indexOf("'", linkStart) : line.indexOf('"', linkStart);

                    if(linkEnd == -1) {
                        linkEnd = choppedLinkStart.contains(")") ? line.indexOf(")", linkStart) : line.indexOf(" ", linkStart);
                    }

                    if (linkEnd == -1) {continue;}

                    System.out.println(line.substring(linkStart, linkEnd));
                }
            }
            reader.close();
        } catch(Exception ex) {
            System.out.println(ex);
        }

    }

}
