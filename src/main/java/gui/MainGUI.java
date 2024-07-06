package gui;

import lombok.Getter;
import service.DataBaseService;
import service.LanguageModelService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

@Getter
public class MainGUI extends JFrame {
    private static final MainGUI INSTANCE = new MainGUI();
    private final DataBaseService dataBaseService;
    private JComboBox<String> listSchemas;
    private JComboBox<String> listLLM;
    private JButton startButton;
    private String infoDatabase;
    private String databaseSelected;
    private String llmSelected;

    public MainGUI(){
        this.dataBaseService = new DataBaseService();
        createComponents();
        implementActionButtons();
    }

    public static MainGUI getInstance() {
        return INSTANCE;
    }

    private void createComponents(){
        setTitle("Chat bot");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        container();

        add(logoTipo());
        add(jLabel("Bem-vindo ao SQL Bot", 130, 250, 50, 20));
        add(jLabel("Selecionar Banco de Dados: ", 200, 220, 30, 16));
        add(listSchemas = jComboBox(230, dataBaseService.listSchemaNames()));
        add(jLabel("Selecionar LLM: ", 290, 220, 30, 16));
        add(listLLM = jComboBox(320, LanguageModelService.listModelsName()));
        add(startButton = jButton());

        // pré-selection itens
        listSchemas.setSelectedIndex(0);
        listLLM.setSelectedIndex(0);
        databaseSelected = (String) listSchemas.getSelectedItem();
        llmSelected = (String) listLLM.getSelectedItem();
        infoDatabase = dataBaseService.generateDDL(databaseSelected);

        startButton.setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    startButton.doClick();
                }
            }
        });
        setFocusable(true);

        setVisible(true);
    }

    private void implementActionButtons(){
        listSchemas.addActionListener(_ -> {
            this.databaseSelected = (String) listSchemas.getSelectedItem();
            infoDatabase = dataBaseService.generateDDL(databaseSelected);
        });

        listLLM.addActionListener(_ -> this.llmSelected = (String) listLLM.getSelectedItem());

        startButton.addActionListener(_ -> {
            new ChatGUI(databaseSelected,llmSelected ,infoDatabase);
            setVisible(false);
        });
    }

    //----Components-----
    private void container(){
        Container container = getContentPane();
        container.setBackground(Color.white);
        container.setLayout(null);
    }

    private JLabel jLabel(String text, int y, int width, int height, int fontSize){
        JLabel jLabel = new JLabel(text);
        jLabel.setBounds(191, y, width, height);
        jLabel.setFont(new Font("Arial", Font.PLAIN, fontSize));
        jLabel.setForeground(Color.black);
        return jLabel;
    }

    private JLabel logoTipo(){
        ImageIcon imageIcon = new ImageIcon("src/main/resources/static/img/logo.jpg");
        Image image = imageIcon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);
        JLabel jLabel = new JLabel();
        jLabel.setIcon(imageIcon);
        jLabel.setBounds(252, 30, 90, 90);
        return jLabel;
    }

    private JComboBox<String> jComboBox (int y, ArrayList<String> arrayList){
        JComboBox<String> jComboBox = new JComboBox<>(arrayList.toArray(new String[0]));
        jComboBox.setBounds(186, y, 222, 30);
        jComboBox.setOpaque(true);
        jComboBox.setBackground(Color.white);
        jComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        return jComboBox;
    }

    private JButton jButton(){
        JButton jButton = new JButton("INICIAR");
        jButton.setBounds(190, 390, 220, 40);
        jButton.setForeground(new Color(0, 0, 0, 153));
        jButton.setFont(new Font("Arial", Font.BOLD, 16));
        jButton.setOpaque(true);
        jButton.setBackground(Color.white);
        return jButton;
    }
}
