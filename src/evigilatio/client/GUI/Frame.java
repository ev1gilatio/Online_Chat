package evigilatio.client.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class Frame {
    private final JFrame frame;
    private JTextArea textArea;
    private JButton btn;
    private JTextField inputArea;
    private final Consumer<String> inMessageConsumer;
    private final Consumer<String> outMessageConsumer;
    private final Font textFont;

    public Frame(Consumer<String> outMessageConsumer) {
        this.outMessageConsumer = outMessageConsumer;

        textFont = new Font("Arial", Font.PLAIN, 16);

        inMessageConsumer = new Consumer<String>() {
            @Override
            public void accept(String inMessage) {
                textArea.append(inMessage);
                textArea.append("\n");
            }
        };

        frame = new JFrame();
        frame.setTitle("ChitChat");
        frame.setSize(417, 669);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        createPanel();

        frame.setVisible(true);
    }

    private void createPanel() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);
        frame.add(jPanel);

        textArea = new JTextArea();
        textArea.setLocation(5, 5);
        textArea.setSize(390, 590);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setFont(textFont);
        textArea.setMargin(new Insets(0, 2, 0, 2));

        inputArea = new JTextField();
        inputArea.setLocation(5, 600);
        inputArea.setSize(300, 25);
        inputArea.setFont(textFont);
        inputArea.setMargin(new Insets(0, 2, 0, 2));
        inputArea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearInputArea();
            }
        });

        btn = new JButton("SEND");
        btn.setLocation(310, 600);
        btn.setSize(85, 25);
        btn.setFont(textFont);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearInputArea();
            }
        });

        jPanel.add(textArea);
        jPanel.add(inputArea);
        jPanel.add(btn);
    }

    private void clearInputArea() {
        String text = inputArea.getText();
        inputArea.setText("");
        outMessageConsumer.accept(text);

        if (text.equals("/exit")) {
            System.exit(0);
        }
    }

    public Consumer<String> getInMessageConsumer() {
        return inMessageConsumer;
    }
}
