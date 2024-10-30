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

    private int linkCount = 0;
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

        JTabbedPane mainPanel = new JTabbedPane();

        JMenu file = new JMenu("File");
        JMenuBar mb = new JMenuBar();

        JMenuItem newTab = new JMenuItem("New Tab");
        newTab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.addTab(String.format("Tab %x", mainPanel.getTabCount()+1), makeTabUI(mainPanel));
            }
        });

        JMenuItem closeThisTab = new JMenuItem("Close This Tab");
        closeThisTab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeTabAt(mainPanel.getSelectedIndex());
            }
        });

        file.add(newTab);
        file.add(closeThisTab);
        mb.add(file);
        mainFrame.setJMenuBar(mb);


//        JButton newTab = new JButton("New Tab");
//        newTab.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                mainPanel.addTab(String.format("Tab %x", mainPanel.getTabCount()), makeTabUI(mainPanel));
//            }
//        });

        mainPanel.addTab(String.format("Tab %x", mainPanel.getTabCount()+1), makeTabUI(mainPanel));

        mainFrame.add(mainPanel);
    }


    private JPanel makeTabUI(JTabbedPane parent) {
        JPanel defaultPage = new JPanel();
        defaultPage.setLayout(new BorderLayout());

        JTextArea linkDisplay = new JTextArea();

        JPanel urlBarLayout = new JPanel();
        urlBarLayout.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        JTextField urlField = new JTextField("https://www.milton.edu");
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
                getLinks(urlField.getText(), searchField.getText(), linkDisplay, parent);
            }
        });

        c.weightx = 0;
        c.gridx = 1;
        c.gridy = 0;
        urlBarLayout.add(goButton, c);

        JButton newTabButton = new JButton("New Tab");

        newTabButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.addTab(String.format("Tab %x", parent.getTabCount()+1), makeTabUI(parent));
            }
        });

        c.weightx = 0;
        c.gridx = 1;
        c.gridy = 1;
        urlBarLayout.add(newTabButton, c);

        defaultPage.add(urlBarLayout, BorderLayout.NORTH);
        JScrollPane linkDisplayScrollable = new JScrollPane(linkDisplay);
        defaultPage.add(linkDisplayScrollable, BorderLayout.CENTER);
        return defaultPage;
    }

    private void getLinks(String location, String term, JTextArea linkDisplay, JTabbedPane parent) {
        linkDisplay.setText("");
        try {
            URL url = new URL(location);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("href")) {
                    linkCount++;
                    for (int i = 0; i <= getLinkCount(line); i++) {
                        String link = getRawLink(line);
                        line = line.substring(line.indexOf(link));
                        link = optionalFixRelativeLinks(location, link);
                        if (link.contains(term)) {
                            linkDisplay.setText(linkDisplay.getText() + (Objects.equals(linkDisplay.getText(), "") ? "" : "\n") + link);
                        }
                    }
                }
            }
            reader.close();
            if (Objects.equals(linkDisplay.getText(), "")) {
                linkDisplay.setText("No Links Found with \"" + term + "\". Try a different search term, or a different URL.");
            }
        } catch (Exception ex) {
            System.out.println(ex);
            linkDisplay.setText("Oh No!" + "\n" + ex);
        }
    }

    private static String getRawLink(String line) {
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

        return link;
    }

    private static String optionalFixRelativeLinks(String location, String link) {
        //                    add the full url back to relative links
        if (link.startsWith("/")) {
            link = location + link;
        }
        if (link.startsWith("#")) {
            link = location + "/" + link;
        }
        return link;
    }

    private static int getLinkCount(String line) {
//        from stackoverflow (answer 2): https://stackoverflow.com/questions/767759/find-the-number-of-occurrences-of-a-substring-in-a-string
        //        count the number of links in the line
        int index = 0;
        int count = 0;
        while (index != -1) {
            index = line.indexOf("href", index);
            if (index != -1) {
                count++;
                index += 4;
            }
        }
        return count;
    }

    private void show() {
        mainFrame.setVisible(true);
    }

}
