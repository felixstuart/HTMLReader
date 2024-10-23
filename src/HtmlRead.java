import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.awt.*;
import java.util.Objects;
import javax.swing.*;


public class HtmlRead {
    private JFrame mainFrame;
    private int WIDTH = 1000;
    private int HEIGHT = 700;

    public HtmlRead() {
        prepareGUI();
    }

    public static void main(String[] args) {
        HtmlRead htmlRead = new HtmlRead();
        htmlRead.show();
    }

    private void prepareGUI() {
        mainFrame = new JFrame();
        mainFrame.setSize(WIDTH, HEIGHT);
        mainFrame.setLayout(new BorderLayout());


        JTextArea linkDisplay = new JTextArea();

        JPanel urlBarLayout = new JPanel();
        urlBarLayout.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        JTextField urlField = new JTextField("https://derbyacademy.org");
        urlField.createToolTip();
        urlField.setToolTipText("Enter a URL");
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        urlBarLayout.add(urlField, c);


        JTextField searchField = new JTextField();
        searchField.createToolTip();
        searchField.setToolTipText("Enter a Search Term");
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.5;
        urlBarLayout.add(searchField, c);

        JButton goButton = new JButton("Go");

        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getLinks(urlField.getText(), searchField.getText(), linkDisplay);
            }
        });

        c.weightx = 0;
        c.gridx = 1;
        c.gridy = 0;
        urlBarLayout.add(goButton, c);

        mainFrame.add(urlBarLayout, BorderLayout.NORTH);
        JScrollPane linkDisplayScrollable = new JScrollPane(linkDisplay);
        mainFrame.add(linkDisplayScrollable, BorderLayout.CENTER);
    }

    private void getLinks(String location, String term, JTextArea linkDisplay) {
        linkDisplay.setText("");
        try {
            URL url = new URL(location);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("href")) {
                    int indexHttp = line.indexOf("href") + 6;
                    String newLine = line.substring(indexHttp);

                    int end = newLine.indexOf("\"");
                    int oEnd = newLine.indexOf("'");

                    String link;
                    if (end == -1 || oEnd == -1) {
                        if (oEnd > end) {
                            link = newLine.substring(0, oEnd);
                        } else {
                            link = newLine.substring(0, end);
                        }
                    } else {
                        int linkEnd = Math.min(end, oEnd);
                        link = newLine.substring(0, linkEnd);
                    }

//                    add the full url back to relative links
                    if (link.startsWith("/")) {
                        link = location + link;
                    }
                    if (link.startsWith("#")) {
                        link = location + "/" + link;
                    }

                    if (link.contains(term)) {
                        linkDisplay.setText(linkDisplay.getText() + (Objects.equals(linkDisplay.getText(), "") ? "" : "\n") + link);
                    }
                }
            }
            reader.close();
        } catch (Exception ex) {
            System.out.println(ex);
            linkDisplay.setText("Oh No!" + "\n" + ex);
        }
    }

    private void show() {
        mainFrame.setVisible(true);
    }

}
