package com.david.cedict;

import com.david.cedict.struct.Entry;
import com.david.cedict.util.PinyinDecoder;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;


import java.awt.Toolkit;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class GUI extends JFrame implements NativeKeyListener {
    private Retriever r;
    private String temp = "";
    private JLabel label;
    private JPanel container;
    private Boolean ctrlDown = false;
    private Boolean shiftDown = false;
    private List<JPanel> panels = new ArrayList<>();

    private static final Color TEXT_COLOR = new Color(219,219,219);
    private static final Color BG_COLOR = new Color(48,48,48);
    private static final Color PANEL_COLOR = new Color(100,100,100);
    private static final Color ERROR_COLOR = new Color(168, 99, 99);

    private final int SCREEN_WIDTH = 600;
    private final int SCREEN_HEIGHT = 400;

    public GUI(Retriever r) {
        this.r = r;

        setTitle("CEDICT");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();

        setAlwaysOnTop(true);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        setVisible(true);
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        if ((e.getKeyCode() == NativeKeyEvent.VC_C) && ctrlDown) {
            String data = "";
            try {
                Thread.sleep(250);
                data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException | InterruptedException ex) {
                wipePanels();
                panels.add(createErrorPanel());
                updateUI();
                ex.printStackTrace();
            }

            data = data.toLowerCase();
            System.out.println(data);
            PriorityQueue<Retriever.ScoreEntry> ranking = r.outputRanking(data);
            if (ranking.size() == 0) {
                wipePanels();
                panels.add(createErrorPanel());
                updateUI();
            }
            else {
                List<JPanel> newPanels = new ArrayList<>();
                while (ranking.size() > 0) {
                    Retriever.ScoreEntry top = ranking.poll();
                    Entry entry = top.getEntry();
                    newPanels.add(createNewPanel(entry));
                }
                this.panels = newPanels;
                updateUI();
            }

        }
        if ((e.getKeyCode() == NativeKeyEvent.VC_CONTROL)) {
            ctrlDown = true;
        }
        if ((e.getKeyCode() == NativeKeyEvent.VC_SHIFT)) {
            shiftDown = true;
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        if ((e.getKeyCode() == NativeKeyEvent.VC_CONTROL)) {
            ctrlDown = false;
        }
        if ((e.getKeyCode() == NativeKeyEvent.VC_SHIFT)) {
            shiftDown = false;
        }
    }

    private JPanel createNewPanel(Entry entry) {
        // Main vertical panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel simpLabel = new JLabel(entry.getSimp());
        JLabel pinyinLabel = new JLabel(PinyinDecoder.decodePinyin(entry.getPinyin()));

        simpLabel.setForeground(TEXT_COLOR);
        simpLabel.setFont(new Font("DengXian", Font.PLAIN, 24));
        simpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        pinyinLabel.setForeground(TEXT_COLOR);
        pinyinLabel.setFont(new Font("DengXian", Font.BOLD, 18));
        pinyinLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create a JTextArea for the englishLabel to support text wrapping
        JTextArea englishTextArea = new JTextArea(entry.getEnglish());
        englishTextArea.setForeground(TEXT_COLOR);
        englishTextArea.setFont(new Font("DengXian", Font.PLAIN, 13));
        englishTextArea.setWrapStyleWord(true);
        englishTextArea.setLineWrap(true);
        englishTextArea.setOpaque(false); // Make the background of the JTextArea transparent
        englishTextArea.setEditable(false);
        englishTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        englishTextArea.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 33)); // Remove the border

        // Set the preferred size for the JTextArea to ensure it wraps properly
        englishTextArea.setPreferredSize(new Dimension(SCREEN_WIDTH - 20, 60));

        // Add components to the main panel
        panel.add(simpLabel);
        panel.add(pinyinLabel);
        panel.add(englishTextArea);

        panel.setBorder(BorderFactory.createLineBorder(BG_COLOR));
        panel.setBackground(PANEL_COLOR);
        panel.setPreferredSize(new Dimension(SCREEN_WIDTH, 100));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // Ensure the panel takes the full width

        return panel;
    }

    private JPanel createErrorPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Invalid selection, please try again.");

        label.setForeground(ERROR_COLOR);
        label.setFont(new Font("DengXian", Font.PLAIN, 24));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.setBackground(BG_COLOR);
        panel.add(Box.createVerticalGlue());
        panel.add(label);
        panel.add(Box.createVerticalGlue());
        return panel;
    }


    private void updateUI() {
        container.removeAll();
        for (JPanel panel : panels) {
            container.add(panel);
        }
        container.revalidate();
        container.repaint();
    }

    private void initUI() {
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS)); // Set layout to vertical
        container.setBackground(BG_COLOR);

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Add the scroll pane to the frame
        add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);

        JLabel label = new JLabel("Highlight some text and press Ctrl+C");
        label.setForeground(PANEL_COLOR);
        label.setFont(new Font("DengXian", Font.PLAIN, 24));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(Box.createVerticalGlue());
        panel.add(label);
        panel.add(Box.createVerticalGlue());

        panels.add(panel);
        updateUI();
        wipePanels();
    }

    public void wipePanels() {
        this.panels.clear();
    }
}

