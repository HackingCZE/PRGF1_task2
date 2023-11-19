import javax.swing.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static java.awt.AWTEventMulticaster.add;

public class StatusBar extends JPanel{
    private Map<String, JLabel> labels = new HashMap<>();
    private JPanel[] rows;

    public StatusBar(int numberOfRows) {
        setLayout(new GridLayout(numberOfRows, 1));

        rows = new JPanel[numberOfRows];
        for (int i = 0; i < numberOfRows; i++) {
            rows[i] = new JPanel();
            rows[i].setLayout(new BoxLayout(rows[i], BoxLayout.X_AXIS));
            add(rows[i]);
        }
    }

    /**
     * add status text to StatuBar
     *
     * @param id id of status
     * @param text status text
     * @param rowIndex index row
     */
    public void addStatus(String id, String text, int rowIndex) {
        if (rowIndex >= rows.length) {
            throw new IllegalArgumentException("Row index is out of bounds.");
        }

        JLabel label = new JLabel(text);
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK), // outer border
                BorderFactory.createEmptyBorder(0, 5, 0, 5) // inner border (spacing)
        ));
        labels.put(id, label);
        rows[rowIndex].add(label);
        updateUI();
    }

    public void editStatus(String id, String newText) {
        JLabel label = labels.get(id);
        if (label != null) {
            label.setText(newText);
            updateUI();
        }
    }

    public void deleteStatus(String id) {
        JLabel label = labels.remove(id);
        if (label != null) {
            for (JPanel row : rows) {
                row.remove(label);
            }
            updateUI();
        }
    }

}
