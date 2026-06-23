package ru.ifmo.cs.bcomp.axl;

import ru.ifmo.cs.bcomp.assembler.AsmNg;
import ru.ifmo.cs.bcomp.assembler.Program;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class AxlEditorPanel extends JPanel {

    private static final Set<String> KEYWORDS = new HashSet<String>();
    private static final Set<String> TYPES = new HashSet<String>();

    static {
        String[] kw = {"if", "else", "while", "do", "for", "return", "break", "continue", "goto", "org", "word"};
        for (String s : kw) {
            KEYWORDS.add(s);
        }
        String[] ty = {"int", "uint", "char", "long", "void"};
        for (String s : ty) {
            TYPES.add(s);
        }
    }

    private static final String SAMPLE =
            "int result;\n\n"
            + "int fact(int n) {\n"
            + "    if (n <= 1) return 1;\n"
            + "    return n * fact(n - 1);\n"
            + "}\n\n"
            + "void main() {\n"
            + "    result = fact(5);\n"
            + "    halt();\n"
            + "}\n";

    private final JTextPane editor;
    private final JTextArea output;
    private final SimpleAttributeSet stDefault = new SimpleAttributeSet();
    private final SimpleAttributeSet stKeyword = new SimpleAttributeSet();
    private final SimpleAttributeSet stType = new SimpleAttributeSet();
    private final SimpleAttributeSet stIntrinsic = new SimpleAttributeSet();
    private final SimpleAttributeSet stNumber = new SimpleAttributeSet();
    private final SimpleAttributeSet stComment = new SimpleAttributeSet();
    private boolean highlighting = false;

    public AxlEditorPanel(final AxlLoadHandler handler) {
        super(new BorderLayout());

        Font mono = new Font(Font.MONOSPACED, Font.PLAIN, 16);

        StyleConstants.setForeground(stDefault, new Color(0x1A1A1A));
        StyleConstants.setForeground(stKeyword, new Color(0x0033B3));
        StyleConstants.setBold(stKeyword, true);
        StyleConstants.setForeground(stType, new Color(0x008080));
        StyleConstants.setBold(stType, true);
        StyleConstants.setForeground(stIntrinsic, new Color(0x7A3E9D));
        StyleConstants.setForeground(stNumber, new Color(0xB5651D));
        StyleConstants.setForeground(stComment, new Color(0x008000));
        StyleConstants.setItalic(stComment, true);

        editor = new JTextPane();
        editor.setFont(mono);
        editor.setText(SAMPLE);
        installAutoIndent(editor);

        output = new JTextArea();
        output.setFont(mono);
        output.setEditable(false);

        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        JButton compile = new JButton("Компилировать");
        JButton load = new JButton("Загрузить в ЭВМ");
        compile.setFocusable(false);
        load.setFocusable(false);
        bar.add(compile);
        bar.add(load);

        JScrollPane editorScroll = new JScrollPane(editor);
        editorScroll.setRowHeaderView(new LineNumberView(editor));
        editorScroll.setBorder(BorderFactory.createTitledBorder("Исходный код (axl)"));
        JScrollPane outputScroll = new JScrollPane(output);
        outputScroll.setBorder(BorderFactory.createTitledBorder("Результат компиляции"));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editorScroll, outputScroll);
        split.setResizeWeight(0.6);
        split.setDividerSize(6);

        add(bar, BorderLayout.PAGE_START);
        add(split, BorderLayout.CENTER);

        editor.getStyledDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                scheduleHighlight();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                scheduleHighlight();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        compile.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                doCompile(false, handler);
            }
        });
        load.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                doCompile(true, handler);
            }
        });

        scheduleHighlight();
    }

    public void focusEditor() {
        editor.requestFocusInWindow();
    }

    private void installAutoIndent(final JTextPane pane) {
        pane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "axlNewline");
        pane.getActionMap().put("axlNewline", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int pos = pane.getCaretPosition();
                    Document doc = pane.getDocument();
                    Element root = doc.getDefaultRootElement();
                    int line = root.getElementIndex(pos);
                    int start = root.getElement(line).getStartOffset();
                    String prefix = doc.getText(start, pos - start);
                    StringBuilder indent = new StringBuilder();
                    for (int i = 0; i < prefix.length(); i++) {
                        char c = prefix.charAt(i);
                        if (c == ' ' || c == '\t') {
                            indent.append(c);
                        } else {
                            break;
                        }
                    }
                    String add = "\n" + indent;
                    if (prefix.trim().endsWith("{")) {
                        add += "    ";
                    }
                    pane.replaceSelection(add);
                } catch (BadLocationException ex) {
                    pane.replaceSelection("\n");
                }
            }
        });
    }

    private static final class LineNumberView extends JComponent {

        private final JTextComponent text;
        private final Color back = new Color(0xF0F0F0);
        private final Color fore = new Color(0x808080);

        LineNumberView(JTextComponent text) {
            this.text = text;
            setFont(text.getFont());
            text.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    refresh();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    refresh();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                }
            });
        }

        private void refresh() {
            revalidate();
            repaint();
        }

        @Override
        public Dimension getPreferredSize() {
            int lines = text.getDocument().getDefaultRootElement().getElementCount();
            int digits = Math.max(3, String.valueOf(lines).length());
            FontMetrics fm = getFontMetrics(getFont());
            int w = fm.charWidth('0') * digits + 12;
            return new Dimension(w, Math.max(text.getHeight(), 1));
        }

        @Override
        @SuppressWarnings("deprecation")
        protected void paintComponent(Graphics g) {
            Rectangle clip = g.getClipBounds();
            g.setColor(back);
            g.fillRect(clip.x, clip.y, clip.width, clip.height);
            g.setColor(fore);
            g.setFont(getFont());
            FontMetrics fm = getFontMetrics(getFont());
            Element root = text.getDocument().getDefaultRootElement();
            int n = root.getElementCount();
            int width = getWidth();
            for (int i = 0; i < n; i++) {
                int off = root.getElement(i).getStartOffset();
                try {
                    Rectangle r = text.modelToView(off);
                    if (r == null) {
                        continue;
                    }
                    String num = String.valueOf(i + 1);
                    int x = width - 6 - fm.stringWidth(num);
                    int y = r.y + fm.getAscent();
                    g.drawString(num, x, y);
                } catch (BadLocationException ex) {
                    return;
                }
            }
        }
    }

    private void doCompile(boolean load, AxlLoadHandler handler) {
        String src = editor.getText();
        AxlCompiler cc = new AxlCompiler();
        String asm = cc.compile(src);
        if (asm == null) {
            StringBuilder sb = new StringBuilder("Ошибки компилятора axl:\n");
            for (String err : cc.getErrors()) {
                sb.append(err).append('\n');
            }
            output.setText(sb.toString());
            return;
        }
        AsmNg assembler = new AsmNg(asm);
        Program prog = assembler.compile();
        StringBuilder sb = new StringBuilder();
        if (!assembler.getErrors().isEmpty()) {
            sb.append("Ошибки ассемблера:\n");
            for (String err : assembler.getErrors()) {
                sb.append(err).append('\n');
            }
            sb.append("\n");
            sb.append(asm);
            output.setText(sb.toString());
            return;
        }
        if (load && prog != null && handler != null) {
            List<Integer> bin = prog.getBinaryFormat();
            handler.loadBinary(bin);
            sb.append("Загружено в ЭВМ. ");
            sb.append("start=0x").append(Integer.toHexString(prog.start_address));
            sb.append(", слов: ").append(prog.binary.size()).append("\n\n");
        }
        sb.append(asm);
        output.setText(sb.toString());
    }

    private void scheduleHighlight() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                highlight();
            }
        });
    }

    private void highlight() {
        if (highlighting) {
            return;
        }
        highlighting = true;
        try {
            StyledDocument doc = editor.getStyledDocument();
            String text;
            try {
                text = doc.getText(0, doc.getLength());
            } catch (BadLocationException ex) {
                return;
            }
            int n = text.length();
            doc.setCharacterAttributes(0, n, stDefault, true);
            int i = 0;
            while (i < n) {
                char c = text.charAt(i);
                if (c == '/' && i + 1 < n && text.charAt(i + 1) == '/') {
                    int j = i + 2;
                    while (j < n && text.charAt(j) != '\n') {
                        j++;
                    }
                    doc.setCharacterAttributes(i, j - i, stComment, true);
                    i = j;
                } else if (c == '/' && i + 1 < n && text.charAt(i + 1) == '*') {
                    int j = i + 2;
                    while (j + 1 < n && !(text.charAt(j) == '*' && text.charAt(j + 1) == '/')) {
                        j++;
                    }
                    j = Math.min(n, j + 2);
                    doc.setCharacterAttributes(i, j - i, stComment, true);
                    i = j;
                } else if (c == '\'') {
                    int j = i + 1;
                    while (j < n && text.charAt(j) != '\'') {
                        if (text.charAt(j) == '\\') {
                            j++;
                        }
                        j++;
                    }
                    j = Math.min(n, j + 1);
                    doc.setCharacterAttributes(i, j - i, stNumber, true);
                    i = j;
                } else if (Character.isDigit(c)) {
                    int j = i + 1;
                    while (j < n && (Character.isLetterOrDigit(text.charAt(j)))) {
                        j++;
                    }
                    doc.setCharacterAttributes(i, j - i, stNumber, true);
                    i = j;
                } else if (Character.isLetter(c) || c == '_') {
                    int j = i + 1;
                    while (j < n && (Character.isLetterOrDigit(text.charAt(j)) || text.charAt(j) == '_')) {
                        j++;
                    }
                    String word = text.substring(i, j);
                    if (TYPES.contains(word)) {
                        doc.setCharacterAttributes(i, j - i, stType, true);
                    } else if (KEYWORDS.contains(word)) {
                        doc.setCharacterAttributes(i, j - i, stKeyword, true);
                    } else if (Intrinsics.isIntrinsic(word)) {
                        doc.setCharacterAttributes(i, j - i, stIntrinsic, true);
                    }
                    i = j;
                } else {
                    i++;
                }
            }
        } finally {
            highlighting = false;
        }
    }
}
