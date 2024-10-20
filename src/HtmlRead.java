import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class HtmlRead {
    private JFrame mainFrame;
    private int WIDTH = 1000;
    private int HEIGHT = 700;

    public HtmlRead() {
        prepareGUI();
//        getLinks("https://www.milton.edu");
    }

    public static void main(String[] args) {
        HtmlRead htmlRead = new HtmlRead();
        htmlRead.show();
    }

    private void prepareGUI() {
        mainFrame = new JFrame();
        mainFrame.setSize(WIDTH, HEIGHT);
        mainFrame.setLayout(new BorderLayout());

        JPanel urlBarLayout = new JPanel();
        urlBarLayout.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        JTextField urlField = new JTextField();
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        urlBarLayout.add(urlField, c);

        JButton goButton = new JButton("Go");
        c.weightx = 0;
        c.gridx = 1;
        c.gridy = 0;
        urlBarLayout.add(goButton, c);

        JTextField searchField = new JTextField();
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.5;
        urlBarLayout.add(searchField, c);



        mainFrame.add(urlBarLayout, BorderLayout.NORTH);

    }


    private void getLinks(String location) {
        try {
            System.out.println();
            System.out.println(location);
            URL url = new URL(location);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream())
            );
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("href")) {
                    int indexHttp = line.indexOf("href") + 6;
                    String newLine = line.substring(indexHttp);

                    int end = newLine.indexOf("\"");
                    int oEnd = newLine.indexOf("'");

                    if (oEnd > end) {
                        System.out.println(newLine.substring(0, oEnd));
                    } else {
                        System.out.println(newLine.substring(0, end));

                    }
                }
            }
            reader.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private void show() {
        mainFrame.setVisible(true);
    }


    private class GoButtonListener implements ActionListener {
        private JTextArea urlField;
        private JTextArea searchField;

        public GoButtonListener(JTextArea url, JTextArea searchBar) {
            urlField = url;
            searchField = searchBar;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("button clicked");
            getLinks(urlField.getText());
        }
    }

}
