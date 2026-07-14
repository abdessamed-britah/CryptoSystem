import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.Base64;

/**
 * CryptoSyst — Système professionnel de chiffrement et déchiffrement.
 * Supporte les algorithmes AES-128 (CBC), DES-56 (CBC), et RSA-2048.
 *
 * @author Mini Projet Cryptographie
 * @version 2.0
 */
public class CryptoSyst extends JFrame {

    // ──────────────────────────────────────────────
    //  UI Components
    // ──────────────────────────────────────────────
    private JTextArea inputArea, outputArea;
    private JTextField keyField;
    private JComboBox<String> algorithmBox;
    private JButton encryptBtn, decryptBtn, loadFileBtn, saveFileBtn, generateKeyBtn, clearBtn, copyBtn;
    private JLabel statusLabel, keySizeLabel, inputLengthLabel, outputLengthLabel;
    private JToggleButton themeToggle;

    // ──────────────────────────────────────────────
    //  State
    // ──────────────────────────────────────────────
    private File selectedFile;
    private KeyPair rsaKeyPair;
    private boolean isDarkTheme = true;

    // ──────────────────────────────────────────────
    //  Dark Theme Palette
    // ──────────────────────────────────────────────
    private static final Color DARK_BG         = new Color(22, 22, 30);
    private static final Color DARK_SURFACE    = new Color(30, 31, 43);
    private static final Color DARK_CARD       = new Color(38, 39, 54);
    private static final Color DARK_BORDER     = new Color(55, 56, 75);
    private static final Color DARK_TEXT       = new Color(230, 230, 240);
    private static final Color DARK_TEXT_SEC   = new Color(150, 152, 170);
    private static final Color DARK_INPUT_BG   = new Color(25, 26, 38);

    // ──────────────────────────────────────────────
    //  Light Theme Palette
    // ──────────────────────────────────────────────
    private static final Color LIGHT_BG        = new Color(243, 244, 249);
    private static final Color LIGHT_SURFACE   = new Color(255, 255, 255);
    private static final Color LIGHT_CARD      = new Color(255, 255, 255);
    private static final Color LIGHT_BORDER    = new Color(215, 218, 230);
    private static final Color LIGHT_TEXT      = new Color(30, 30, 45);
    private static final Color LIGHT_TEXT_SEC  = new Color(110, 115, 135);
    private static final Color LIGHT_INPUT_BG  = new Color(248, 249, 252);

    // ──────────────────────────────────────────────
    //  Accent Colors
    // ──────────────────────────────────────────────
    private static final Color ACCENT_PRIMARY  = new Color(99, 102, 241);   // Indigo
    private static final Color ACCENT_SECOND   = new Color(139, 92, 246);   // Purple
    private static final Color ACCENT_SUCCESS  = new Color(34, 197, 94);    // Green
    private static final Color ACCENT_INFO     = new Color(59, 130, 246);   // Blue
    private static final Color ACCENT_WARNING  = new Color(245, 158, 11);   // Amber
    private static final Color ACCENT_DANGER   = new Color(239, 68, 68);    // Red

    // ──────────────────────────────────────────────
    //  Fonts
    // ──────────────────────────────────────────────
    private static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font FONT_SUB     = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_LABEL   = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_MONO    = new Font("Cascadia Code", Font.PLAIN, 13);
    private static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font FONT_BTN     = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_BTN_LG  = new Font("Segoe UI", Font.BOLD, 14);

    // ──────────────────────────────────────────────
    //  Dimensions
    // ──────────────────────────────────────────────
    private static final int CARD_RADIUS   = 14;
    private static final int BTN_RADIUS    = 10;
    private static final int SIDEBAR_WIDTH = 290;

    // ──────────────────────────────────────────────
    //  References for theme switching
    // ──────────────────────────────────────────────
    private JPanel mainPanel, centerPanel, sidebarPanel, textPanel, buttonPanel, statusPanel;
    private JPanel headerPanel;


    // ================================================================
    //  Constructor
    // ================================================================

    public CryptoSyst() {
        initLookAndFeel();
        initComponents();
    }

    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // Global rendering hints for smoother text
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
        } catch (Exception ignored) {}
    }


    // ================================================================
    //  UI Initialization
    // ================================================================

    private void initComponents() {
        setTitle("CryptoSyst — Chiffrement & Déchiffrement");
        setSize(1200, 780);
        setMinimumSize(new Dimension(950, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ── Main container ──
        mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(getThemeBg());

        // ── Header ──
        headerPanel = createHeaderPanel();

        // ── Center with sidebar + content ──
        centerPanel = new JPanel(new BorderLayout(16, 16));
        centerPanel.setBorder(new EmptyBorder(16, 16, 16, 16));
        centerPanel.setOpaque(false);

        sidebarPanel = createSidebarPanel();
        textPanel = createTextPanel();
        buttonPanel = createButtonPanel();

        centerPanel.add(sidebarPanel, BorderLayout.WEST);
        centerPanel.add(textPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ── Status bar ──
        statusPanel = createStatusPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        // ── Keyboard shortcuts ──
        registerShortcuts();
    }


    // ================================================================
    //  Header Panel — Gradient
    // ================================================================

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                    0, 0, ACCENT_PRIMARY,
                    getWidth(), 0, ACCENT_SECOND
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        panel.setBorder(new EmptyBorder(18, 24, 18, 24));
        panel.setOpaque(false);

        // Title
        JLabel titleLabel = new JLabel("\uD83D\uDD10  CryptoSyst");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Système de chiffrement sécurisé  •  AES-128  •  DES-56  •  RSA-2048");
        subtitleLabel.setFont(FONT_SUB);
        subtitleLabel.setForeground(new Color(255, 255, 255, 180));

        JPanel titleGroup = new JPanel();
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));
        titleGroup.setOpaque(false);
        titleGroup.add(titleLabel);
        titleGroup.add(Box.createRigidArea(new Dimension(0, 4)));
        titleGroup.add(subtitleLabel);

        // Theme toggle button
        themeToggle = new JToggleButton("\u263E");
        themeToggle.setSelected(isDarkTheme);
        themeToggle.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
        themeToggle.setForeground(Color.WHITE);
        themeToggle.setOpaque(false);
        themeToggle.setContentAreaFilled(false);
        themeToggle.setBorderPainted(false);
        themeToggle.setFocusPainted(false);
        themeToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        themeToggle.setToolTipText("Basculer thème sombre / clair");
        themeToggle.addActionListener(e -> toggleTheme());

        panel.add(titleGroup, BorderLayout.WEST);
        panel.add(themeToggle, BorderLayout.EAST);

        return panel;
    }


    // ================================================================
    //  Sidebar Panel — Configuration
    // ================================================================

    private JPanel createSidebarPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));
        panel.setOpaque(false);

        // ── Algorithm section ──
        JPanel algoCard = createCard("\uD83D\uDD27  Algorithme");
        String[] algorithms = {"AES-128", "DES-56", "RSA-2048"};
        algorithmBox = createStyledComboBox(algorithms);
        algorithmBox.addActionListener(e -> updateKeyFieldForAlgorithm());
        addToCard(algoCard, algorithmBox);

        // ── Key section ──
        JPanel keyCard = createCard("\uD83D\uDD11  Clé de chiffrement");

        keyField = createStyledTextField("Clé en Base64...");
        keySizeLabel = new JLabel("Taille : 0 bits");
        keySizeLabel.setFont(FONT_SMALL);
        keySizeLabel.setForeground(getThemeTextSec());

        keyField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateKeySize(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateKeySize(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateKeySize(); }
        });

        generateKeyBtn = createAccentButton("⚡  Générer une clé", ACCENT_SUCCESS);
        generateKeyBtn.addActionListener(e -> generateKey());

        addToCard(keyCard, keyField);
        keyCard.add(Box.createRigidArea(new Dimension(0, 6)));
        addToCard(keyCard, keySizeLabel);
        keyCard.add(Box.createRigidArea(new Dimension(0, 10)));
        addToCard(keyCard, generateKeyBtn);

        // ── File management section ──
        JPanel fileCard = createCard("\uD83D\uDCC1  Fichiers");

        loadFileBtn = createAccentButton("\uD83D\uDCC2  Charger un fichier", ACCENT_INFO);
        loadFileBtn.addActionListener(e -> loadFile());

        saveFileBtn = createAccentButton("\uD83D\uDCBE  Sauvegarder résultat", ACCENT_WARNING);
        saveFileBtn.addActionListener(e -> saveFile());

        addToCard(fileCard, loadFileBtn);
        fileCard.add(Box.createRigidArea(new Dimension(0, 8)));
        addToCard(fileCard, saveFileBtn);

        // ── Quick help section ──
        JPanel helpCard = createCard("\u2139\uFE0F  Aide rapide");
        JTextArea helpText = new JTextArea(
            "1. Sélectionnez un algorithme\n" +
            "2. Générez ou entrez une clé\n" +
            "3. Saisissez votre texte\n" +
            "4. Cliquez sur Chiffrer\n\n" +
            "Raccourcis clavier :\n" +
            "  Ctrl+E — Chiffrer\n" +
            "  Ctrl+D — Déchiffrer\n" +
            "  Ctrl+G — Générer clé"
        );
        helpText.setFont(FONT_SMALL);
        helpText.setEditable(false);
        helpText.setOpaque(false);
        helpText.setForeground(getThemeTextSec());
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true);
        helpText.setBorder(null);
        addToCard(helpCard, helpText);

        // ── Assemble sidebar ──
        panel.add(algoCard);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(keyCard);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(fileCard);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(helpCard);
        panel.add(Box.createVerticalGlue());

        return panel;
    }


    // ================================================================
    //  Text Panel — Input / Output
    // ================================================================

    private JPanel createTextPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 14));
        panel.setOpaque(false);

        // ── Input card ──
        JPanel inputCard = createCard("\u270F\uFE0F  Texte à chiffrer / déchiffrer");

        inputArea = new JTextArea();
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setFont(FONT_MONO);
        inputArea.setBackground(getThemeInputBg());
        inputArea.setForeground(getThemeText());
        inputArea.setCaretColor(ACCENT_PRIMARY);
        inputArea.setBorder(new EmptyBorder(12, 12, 12, 12));

        inputLengthLabel = new JLabel("0 caractères");
        inputLengthLabel.setFont(FONT_SMALL);
        inputLengthLabel.setForeground(getThemeTextSec());

        inputArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateInputLength(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateInputLength(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateInputLength(); }
        });

        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setBorder(BorderFactory.createLineBorder(getThemeBorder(), 1));
        inputScroll.getViewport().setBackground(getThemeInputBg());

        JPanel inputFooter = new JPanel(new BorderLayout());
        inputFooter.setOpaque(false);
        inputFooter.setBorder(new EmptyBorder(4, 0, 0, 0));
        inputFooter.add(inputLengthLabel, BorderLayout.EAST);

        addToCard(inputCard, inputScroll);
        addToCard(inputCard, inputFooter);

        // ── Output card ──
        JPanel outputCard = createCard("\u2705  Résultat");

        outputArea = new JTextArea();
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setFont(FONT_MONO);
        outputArea.setEditable(false);
        outputArea.setBackground(getThemeInputBg());
        outputArea.setForeground(getThemeText());
        outputArea.setCaretColor(ACCENT_PRIMARY);
        outputArea.setBorder(new EmptyBorder(12, 12, 12, 12));

        outputLengthLabel = new JLabel("0 caractères");
        outputLengthLabel.setFont(FONT_SMALL);
        outputLengthLabel.setForeground(getThemeTextSec());

        copyBtn = createSmallButton("\uD83D\uDCCB Copier");
        copyBtn.addActionListener(e -> copyToClipboard());

        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(BorderFactory.createLineBorder(getThemeBorder(), 1));
        outputScroll.getViewport().setBackground(getThemeInputBg());

        JPanel outputFooter = new JPanel(new BorderLayout());
        outputFooter.setOpaque(false);
        outputFooter.setBorder(new EmptyBorder(4, 0, 0, 0));
        outputFooter.add(outputLengthLabel, BorderLayout.WEST);
        outputFooter.add(copyBtn, BorderLayout.EAST);

        addToCard(outputCard, outputScroll);
        addToCard(outputCard, outputFooter);

        panel.add(inputCard);
        panel.add(outputCard);

        return panel;
    }


    // ================================================================
    //  Action Buttons Panel
    // ================================================================

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 8));
        panel.setOpaque(false);

        encryptBtn = createPillButton("\uD83D\uDD12  Chiffrer", ACCENT_SUCCESS);
        encryptBtn.setPreferredSize(new Dimension(170, 46));
        encryptBtn.setFont(FONT_BTN_LG);
        encryptBtn.addActionListener(e -> performEncryption());

        decryptBtn = createPillButton("\uD83D\uDD13  Déchiffrer", ACCENT_INFO);
        decryptBtn.setPreferredSize(new Dimension(170, 46));
        decryptBtn.setFont(FONT_BTN_LG);
        decryptBtn.addActionListener(e -> performDecryption());

        clearBtn = createPillButton("\uD83D\uDDD1  Effacer", ACCENT_DANGER);
        clearBtn.setPreferredSize(new Dimension(170, 46));
        clearBtn.setFont(FONT_BTN_LG);
        clearBtn.addActionListener(e -> clearFields());

        panel.add(encryptBtn);
        panel.add(decryptBtn);
        panel.add(clearBtn);

        return panel;
    }


    // ================================================================
    //  Status Bar
    // ================================================================

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(getThemeBorder());
                g2.drawLine(0, 0, getWidth(), 0);
                g2.dispose();
            }
        };
        panel.setBackground(getThemeSurface());
        panel.setBorder(new EmptyBorder(6, 16, 6, 16));
        panel.setPreferredSize(new Dimension(0, 32));

        statusLabel = new JLabel("✓  Prêt");
        statusLabel.setFont(FONT_SMALL);
        statusLabel.setForeground(ACCENT_SUCCESS);

        JLabel versionLabel = new JLabel("CryptoSyst v2.0");
        versionLabel.setFont(FONT_SMALL);
        versionLabel.setForeground(getThemeTextSec());

        panel.add(statusLabel, BorderLayout.WEST);
        panel.add(versionLabel, BorderLayout.EAST);

        return panel;
    }


    // ================================================================
    //  Reusable UI Factory Methods
    // ================================================================

    /**
     * Creates a rounded card panel with title.
     */
    private JPanel createCard(String title) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getThemeCard());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), CARD_RADIUS, CARD_RADIUS));
                g2.setColor(getThemeBorder());
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, CARD_RADIUS, CARD_RADIUS));
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        if (title != null && !title.isEmpty()) {
            JLabel lbl = new JLabel(title);
            lbl.setFont(FONT_LABEL);
            lbl.setForeground(getThemeText());
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(lbl);
            card.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        return card;
    }

    /**
     * Utility to add a component with LEFT_ALIGNMENT to a card.
     */
    private void addToCard(JPanel card, JComponent comp) {
        comp.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(comp);
    }

    /**
     * Styled combo box.
     */
    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(FONT_BODY);
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        box.setPreferredSize(new Dimension(240, 36));
        box.setBackground(getThemeInputBg());
        box.setForeground(getThemeText());
        box.setCursor(new Cursor(Cursor.HAND_CURSOR));
        box.setFocusable(false);
        return box;
    }

    /**
     * Styled text field with placeholder tooltip.
     */
    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(FONT_MONO);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        field.setPreferredSize(new Dimension(240, 36));
        field.setBackground(getThemeInputBg());
        field.setForeground(getThemeText());
        field.setCaretColor(ACCENT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(getThemeBorder()),
            new EmptyBorder(4, 8, 4, 8)
        ));
        field.setToolTipText(placeholder);
        return field;
    }

    /**
     * Accent-colored button used in sidebar.
     */
    private JButton createAccentButton(String text, Color accentColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(accentColor.brighter());
                } else {
                    g2.setColor(accentColor);
                }
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), BTN_RADIUS, BTN_RADIUS));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BTN);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setPreferredSize(new Dimension(240, 38));
        return btn;
    }

    /**
     * Large pill-shaped action button.
     */
    private JButton createPillButton(String text, Color accentColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bg = getModel().isRollover() ? accentColor.brighter() : accentColor;
                // Shadow
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fill(new RoundRectangle2D.Float(2, 3, getWidth() - 4, getHeight() - 4, 22, 22));
                // Background
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight() - 2, 22, 22));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BTN_LG);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Small utility button (e.g., Copy).
     */
    private JButton createSmallButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_SMALL);
        btn.setForeground(ACCENT_PRIMARY);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(ACCENT_PRIMARY.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setForeground(ACCENT_PRIMARY);
            }
        });
        return btn;
    }


    // ================================================================
    //  Theme Helpers
    // ================================================================

    private Color getThemeBg()      { return isDarkTheme ? DARK_BG       : LIGHT_BG; }
    private Color getThemeSurface() { return isDarkTheme ? DARK_SURFACE  : LIGHT_SURFACE; }
    private Color getThemeCard()    { return isDarkTheme ? DARK_CARD     : LIGHT_CARD; }
    private Color getThemeBorder()  { return isDarkTheme ? DARK_BORDER   : LIGHT_BORDER; }
    private Color getThemeText()    { return isDarkTheme ? DARK_TEXT     : LIGHT_TEXT; }
    private Color getThemeTextSec() { return isDarkTheme ? DARK_TEXT_SEC : LIGHT_TEXT_SEC; }
    private Color getThemeInputBg() { return isDarkTheme ? DARK_INPUT_BG : LIGHT_INPUT_BG; }

    private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        themeToggle.setText(isDarkTheme ? "\u263E" : "\u2600");
        // Rebuild the entire UI with new theme
        getContentPane().removeAll();
        initComponents();
        revalidate();
        repaint();
        setStatus("✓  Thème " + (isDarkTheme ? "sombre" : "clair") + " activé", ACCENT_SUCCESS);
    }


    // ================================================================
    //  Keyboard Shortcuts
    // ================================================================

    private void registerShortcuts() {
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK), "encrypt");
        am.put("encrypt", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { performEncryption(); }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK), "decrypt");
        am.put("decrypt", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { performDecryption(); }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK), "genkey");
        am.put("genkey", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { generateKey(); }
        });
    }


    // ================================================================
    //  Status Bar Updates
    // ================================================================

    private void setStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    private void setStatusTimed(String message, Color color, int millis) {
        setStatus(message, color);
        Timer timer = new Timer(millis, e -> setStatus("✓  Prêt", ACCENT_SUCCESS));
        timer.setRepeats(false);
        timer.start();
    }


    // ================================================================
    //  Event Handlers — Key & Input
    // ================================================================

    private void updateKeySize() {
        try {
            String key = keyField.getText().trim();
            if (!key.isEmpty()) {
                byte[] keyBytes = Base64.getDecoder().decode(key);
                int bits = keyBytes.length * 8;
                keySizeLabel.setText("Taille : " + bits + " bits");
                keySizeLabel.setForeground(bits >= 128 ? ACCENT_SUCCESS : ACCENT_WARNING);
            } else {
                keySizeLabel.setText("Taille : 0 bits");
                keySizeLabel.setForeground(getThemeTextSec());
            }
        } catch (Exception e) {
            keySizeLabel.setText("⚠ Format Base64 invalide");
            keySizeLabel.setForeground(ACCENT_DANGER);
        }
    }

    private void updateInputLength() {
        int len = inputArea.getText().length();
        inputLengthLabel.setText(len + " caractère" + (len != 1 ? "s" : ""));
    }

    private void updateOutputLength() {
        int len = outputArea.getText().length();
        outputLengthLabel.setText(len + " caractère" + (len != 1 ? "s" : ""));
    }

    private void updateKeyFieldForAlgorithm() {
        String algo = (String) algorithmBox.getSelectedItem();
        if (algo == null) return;

        if (algo.startsWith("AES")) {
            keyField.setToolTipText("AES : Clé de 128 bits (16 octets) en Base64");
            setStatus("\uD83D\uDD12  AES-128 sélectionné — Chiffrement symétrique sécurisé (CBC)", ACCENT_INFO);
        } else if (algo.startsWith("DES")) {
            keyField.setToolTipText("DES : Clé de 56 bits (8 octets) en Base64");
            setStatus("\uD83D\uDD12  DES-56 sélectionné — Chiffrement symétrique classique (CBC)", ACCENT_WARNING);
        } else if (algo.startsWith("RSA")) {
            keyField.setToolTipText("RSA : Les clés sont générées automatiquement");
            setStatus("\uD83D\uDD10  RSA-2048 sélectionné — Chiffrement asymétrique", ACCENT_INFO);
        }
        keyField.setText("");
    }


    // ================================================================
    //  Key Generation
    // ================================================================

    private void generateKey() {
        String algo = (String) algorithmBox.getSelectedItem();
        if (algo == null) return;

        try {
            if (algo.startsWith("AES")) {
                KeyGenerator keyGen = KeyGenerator.getInstance("AES");
                keyGen.init(128, new SecureRandom());
                SecretKey key = keyGen.generateKey();
                keyField.setText(Base64.getEncoder().encodeToString(key.getEncoded()));
                setStatus("✓  Clé AES-128 générée avec succès", ACCENT_SUCCESS);
            } else if (algo.startsWith("DES")) {
                KeyGenerator keyGen = KeyGenerator.getInstance("DES");
                keyGen.init(new SecureRandom());
                SecretKey key = keyGen.generateKey();
                keyField.setText(Base64.getEncoder().encodeToString(key.getEncoded()));
                setStatus("✓  Clé DES-56 générée avec succès", ACCENT_SUCCESS);
            } else if (algo.startsWith("RSA")) {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                setStatus("⏳  Génération de la paire RSA-2048 en cours...", ACCENT_WARNING);

                KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
                keyGen.initialize(2048, new SecureRandom());
                rsaKeyPair = keyGen.generateKeyPair();

                String pubKeyB64 = Base64.getEncoder().encodeToString(rsaKeyPair.getPublic().getEncoded());
                keyField.setText(pubKeyB64);
                setCursor(Cursor.getDefaultCursor());
                setStatus("✓  Paire de clés RSA-2048 générée", ACCENT_SUCCESS);
            }
            updateKeySize();
        } catch (Exception ex) {
            showError("Erreur lors de la génération de clé :\n" + ex.getMessage());
        }
    }


    // ================================================================
    //  File Operations
    // ================================================================

    private void loadFile() {
        JFileChooser fc = new JFileChooser(System.getProperty("user.home"));
        fc.setDialogTitle("Charger un fichier texte");

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fc.getSelectedFile();
            try {
                String content = new String(Files.readAllBytes(selectedFile.toPath()), StandardCharsets.UTF_8);
                inputArea.setText(content);
                updateInputLength();
                setStatus("✓  Fichier chargé : " + selectedFile.getName(), ACCENT_SUCCESS);
            } catch (IOException ex) {
                showError("Impossible de lire le fichier :\n" + ex.getMessage());
            }
        }
    }

    private void saveFile() {
        if (outputArea.getText().isEmpty()) {
            showError("Aucun résultat à sauvegarder.");
            return;
        }

        JFileChooser fc = new JFileChooser(System.getProperty("user.home"));
        fc.setDialogTitle("Sauvegarder le résultat");

        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                Files.write(file.toPath(), outputArea.getText().getBytes(StandardCharsets.UTF_8));
                setStatus("✓  Fichier sauvegardé : " + file.getName(), ACCENT_SUCCESS);
            } catch (IOException ex) {
                showError("Impossible de sauvegarder :\n" + ex.getMessage());
            }
        }
    }

    private void copyToClipboard() {
        String text = outputArea.getText();
        if (!text.isEmpty()) {
            Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new java.awt.datatransfer.StringSelection(text), null);
            setStatusTimed("✓  Résultat copié dans le presse-papiers", ACCENT_SUCCESS, 2500);
        }
    }


    // ================================================================
    //  Encryption / Decryption Orchestration
    // ================================================================

    private void performEncryption() {
        String input = inputArea.getText();
        if (input.isEmpty()) {
            showError("Veuillez entrer un texte à chiffrer.");
            return;
        }

        String key = keyField.getText().trim();
        String algo = (String) algorithmBox.getSelectedItem();
        if (algo == null) return;

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        setStatus("⏳  Chiffrement " + algo + " en cours...", ACCENT_WARNING);

        try {
            String encrypted;
            if (algo.startsWith("AES")) {
                encrypted = encryptAES(input, key);
            } else if (algo.startsWith("DES")) {
                encrypted = encryptDES(input, key);
            } else {
                encrypted = encryptRSA(input);
            }

            outputArea.setText(encrypted);
            updateOutputLength();
            setStatus("✓  Chiffrement " + algo + " réussi", ACCENT_SUCCESS);
        } catch (Exception ex) {
            showError("Erreur de chiffrement :\n" + ex.getMessage());
            setStatus("✗  Échec du chiffrement", ACCENT_DANGER);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void performDecryption() {
        String input = inputArea.getText();
        if (input.isEmpty()) {
            showError("Veuillez entrer un texte chiffré à déchiffrer.");
            return;
        }

        String key = keyField.getText().trim();
        String algo = (String) algorithmBox.getSelectedItem();
        if (algo == null) return;

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        setStatus("⏳  Déchiffrement " + algo + " en cours...", ACCENT_WARNING);

        try {
            String decrypted;
            if (algo.startsWith("AES")) {
                decrypted = decryptAES(input, key);
            } else if (algo.startsWith("DES")) {
                decrypted = decryptDES(input, key);
            } else {
                decrypted = decryptRSA(input);
            }

            outputArea.setText(decrypted);
            updateOutputLength();
            setStatus("✓  Déchiffrement " + algo + " réussi", ACCENT_SUCCESS);
        } catch (Exception ex) {
            showError("Erreur de déchiffrement :\n" + ex.getMessage());
            setStatus("✗  Échec du déchiffrement", ACCENT_DANGER);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }


    // ================================================================
    //  Cryptographic Operations — AES-128 CBC
    // ================================================================

    private String encryptAES(String data, String keyBase64) throws Exception {
        if (keyBase64.isEmpty()) throw new Exception("Clé AES requise ! Cliquez sur « Générer une clé ».");
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
        if (keyBytes.length != 16) throw new Exception("La clé AES doit faire 128 bits (16 octets).");

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // Generate random IV
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // Prepend IV to ciphertext
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    private String decryptAES(String encryptedBase64, String keyBase64) throws Exception {
        if (keyBase64.isEmpty()) throw new Exception("Clé AES requise !");
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
        if (keyBytes.length != 16) throw new Exception("La clé AES doit faire 128 bits (16 octets).");

        byte[] combined = Base64.getDecoder().decode(encryptedBase64);
        if (combined.length < 16) throw new Exception("Données chiffrées invalides (trop courtes).");

        // Extract IV from first 16 bytes
        byte[] iv = new byte[16];
        byte[] encrypted = new byte[combined.length - 16];
        System.arraycopy(combined, 0, iv, 0, 16);
        System.arraycopy(combined, 16, encrypted, 0, encrypted.length);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
    }


    // ================================================================
    //  Cryptographic Operations — DES-56 CBC
    // ================================================================

    private String encryptDES(String data, String keyBase64) throws Exception {
        if (keyBase64.isEmpty()) throw new Exception("Clé DES requise ! Cliquez sur « Générer une clé ».");
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
        if (keyBytes.length != 8) throw new Exception("La clé DES doit faire 64 bits (8 octets).");

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        // Generate random IV
        byte[] iv = new byte[8];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // Prepend IV to ciphertext
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    private String decryptDES(String encryptedBase64, String keyBase64) throws Exception {
        if (keyBase64.isEmpty()) throw new Exception("Clé DES requise !");
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
        if (keyBytes.length != 8) throw new Exception("La clé DES doit faire 64 bits (8 octets).");

        byte[] combined = Base64.getDecoder().decode(encryptedBase64);
        if (combined.length < 8) throw new Exception("Données chiffrées invalides (trop courtes).");

        // Extract IV from first 8 bytes
        byte[] iv = new byte[8];
        byte[] encrypted = new byte[combined.length - 8];
        System.arraycopy(combined, 0, iv, 0, 8);
        System.arraycopy(combined, 8, encrypted, 0, encrypted.length);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "DES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
    }


    // ================================================================
    //  Cryptographic Operations — RSA-2048
    // ================================================================

    private String encryptRSA(String data) throws Exception {
        if (rsaKeyPair == null) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            setStatus("⏳  Génération de la paire RSA-2048...", ACCENT_WARNING);
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048, new SecureRandom());
            rsaKeyPair = keyGen.generateKeyPair();
            setCursor(Cursor.getDefaultCursor());
        }

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, rsaKeyPair.getPublic());
        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // Show truncated public key for reference
        String pubKeyB64 = Base64.getEncoder().encodeToString(rsaKeyPair.getPublic().getEncoded());
        keyField.setText(pubKeyB64.substring(0, Math.min(pubKeyB64.length(), 40)) + "...");
        keyField.setToolTipText("Clé publique RSA (tronquée). La clé privée est conservée en mémoire.");

        return Base64.getEncoder().encodeToString(encrypted);
    }

    private String decryptRSA(String encryptedBase64) throws Exception {
        if (rsaKeyPair == null) {
            throw new Exception("Aucune paire de clés RSA en mémoire.\nVeuillez d'abord chiffrer un texte avec RSA.");
        }

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, rsaKeyPair.getPrivate());
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedBase64));

        return new String(decrypted, StandardCharsets.UTF_8);
    }


    // ================================================================
    //  Utility Methods
    // ================================================================

    private void clearFields() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Voulez-vous effacer tous les champs ?\nLes clés RSA en mémoire seront également supprimées.",
            "Confirmer l'effacement",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            inputArea.setText("");
            outputArea.setText("");
            keyField.setText("");
            rsaKeyPair = null;
            updateInputLength();
            updateOutputLength();
            updateKeySize();
            setStatusTimed("✓  Tous les champs ont été effacés", ACCENT_SUCCESS, 3000);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }


    // ================================================================
    //  Entry Point
    // ================================================================

    public static void main(String[] args) {
        // Enable anti-aliasing globally
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            CryptoSyst app = new CryptoSyst();
            app.setVisible(true);
        });
    }
}
