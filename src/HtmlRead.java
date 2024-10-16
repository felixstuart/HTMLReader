import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


public class HtmlRead {

    public static void main(String[] args) {
        HtmlRead htmlRead = new HtmlRead();
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
                if(line.contains("href")) {
                    int indexHttp = line.indexOf("href") + 6;
                    String newLine = line.substring(indexHttp);

                    int end = newLine.indexOf("\"");
                    int oEnd = newLine.indexOf("'");

                    if (oEnd > end) {
                        System.out.println(newLine.substring(0,oEnd));
                    } else {
                        System.out.println(newLine.substring(0,end));
                    }
                }
            }
            reader.close();
        } catch(Exception ex) {
            System.out.println(ex);
        }

    }

}
