package gui;

import service.OllamaService;
import util.OutputStyles;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatGUI extends JFrame {
    private final MainGUI mainGUI;
    private JButton sentButton;
    private JButton backButton;
    private final String databaseSelected;
    private final String infoDatabase;
    private final String llmSelected;
    private JTextField input;
    private String conversation = " ";
    private JEditorPane output;

    public ChatGUI(String databaseSelected, String llmSelected, String infoDatabase) {
        this.mainGUI = MainGUI.getInstance();
        this.infoDatabase = infoDatabase;
        this.llmSelected = llmSelected;
        this.databaseSelected = databaseSelected;
        createComponents();
        implementActionButtons();
    }

    private void createComponents() {
        setTitle("Chat bot");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        container();
        conversation += "<b>Modelo:   </b>" + llmSelected + "<br><br>";
        conversation += "<b>Bot:</b> Olá, eu sou o SQL bot, faça uma pergunta:<br><br>";
        add(input = jTextField());
        output = jEditorPane();
        add(jScrollPane(output));
        add(sentButton = jButton("ENVIAR", 400));
        add(backButton = jButton("VOLTAR", 15));
        add(image());
        add(jLabel("Banco de dados:        " + databaseSelected, 15));

        input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        setVisible(true);
    }

    private void implementActionButtons() {
        backButton.addActionListener(_ -> {
            mainGUI.setVisible(true);
            setVisible(false);
        });
        sentButton.addActionListener(_ -> sendMessage());
    }

    private void sendMessage() {
        String question = input.getText();
        input.setText(" ");
        conversation += "<b>You:</b> " + question + "<br><br>";
        output.setText(conversation);

        new Thread(() -> {
            String sql = OllamaService.sendOllamaQuestion(infoDatabase, question, llmSelected);
            String result = OllamaService.resultSQL(databaseSelected, sql);
            conversation += "<b>Bot:</b> " + result + "<br><br>";

            SwingUtilities.invokeLater(() -> output.setText(conversation));
        }).start();
    }

    //----Components-----
    private void container() {
        Container container = getContentPane();
        container.setBackground(Color.white);
        container.setLayout(null);
    }

    private JTextField jTextField() {
        JTextField jTextField = new JTextField();
        jTextField.setBounds(40, 400, 410, 40);
        jTextField.setFont(new Font("Arial", Font.PLAIN, 16));
        jTextField.setBackground(new Color(243, 242, 242));
        return jTextField;
    }

    private JEditorPane jEditorPane() {
        HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
        OutputStyles outputStyles = new OutputStyles();
        outputStyles.ApplyStyles(htmlEditorKit);
        JEditorPane jEditorPane = new JEditorPane();
        jEditorPane.setContentType("text/html");
        jEditorPane.setEditable(false);
        jEditorPane.setEditorKit(htmlEditorKit);
        jEditorPane.setForeground(Color.black);
        jEditorPane.setText("<html>" + conversation + "</html>");
        return jEditorPane;
    }

    private JScrollPane jScrollPane(JEditorPane jEditorPane) {
        JScrollPane jScrollPane = new JScrollPane(jEditorPane);
        jScrollPane.setBounds(40, 55, 525, 330);
        jScrollPane.setForeground(Color.blue);
        jScrollPane.setBorder(null);
        return jScrollPane;
    }

    private JLabel jLabel(String text, int y) {
        JLabel jLabel = new JLabel(text);
        jLabel.setBounds(44, y, 500, 35);
        jLabel.setFont(new Font("Arial", Font.BOLD, 14));
        jLabel.setForeground(Color.black);
        return jLabel;
    }

    private JLabel image() {
        ImageIcon imageIcon = new ImageIcon("src/main/resources/static/img/logo.jpg");
        Image image = imageIcon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);
        JLabel jLabel = new JLabel();
        jLabel.setIcon(imageIcon);
        jLabel.setBounds(252, 30, 90, 90);
        return jLabel;
    }

    private JButton jButton(String text, int y) {
        JButton jButton = new JButton(text);
        jButton.setBounds(190, y, 220, 40);
        jButton.setForeground(new Color(0, 0, 0, 153));
        jButton.setFont(new Font("Arial", Font.BOLD, 16));
        jButton.setOpaque(true);
        jButton.setBackground(Color.white);
        return jButton;
    }
}