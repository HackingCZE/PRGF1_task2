import objectdata.Point2D;
import objectdata.Polygon;
import rasterdata.RasterBI;
import rasterops.LinerTrivial;
import rasterops.fill.ScanLine;
import rasterops.fill.SeedFill4Stack;
import rasterops.fill.conditions.TestBackground;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * task 01
 *
 * @author Dominik Hurta
 * @version 2023
 */

public class Canvas {

    private JFrame frame;
    private JPanel panel;
    private RasterBI img;

    int x1 = 0;
    int x2 = 0;
    int y1 = 0;
    int y2 = 0;


    int gap = 0;

    enum States {
        ShiftPressed,
        Gap,
        X1,
        Y1,
        X2,
        Y2
    }


    boolean shiftPressed = false;
    LinerTrivial linerTrivial;
    StatusBar statusBar;

    int backgroundColor = 0x2f2f2f;

    SeedFill4Stack seed = new SeedFill4Stack();
    ScanLine scanLine = new ScanLine();

    public Canvas(int width, int height) {
        frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        img = new RasterBI(width, height);
        linerTrivial = new LinerTrivial();

        panel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g);
            }
        };

        panel.setPreferredSize(new Dimension(width, height));
        panel.setFocusable(true);
        panel.requestFocusInWindow();

        frame.add(panel, BorderLayout.CENTER);
        statusBar = new StatusBar(2);
        frame.add(statusBar, BorderLayout.SOUTH);

        statusBar.addStatus(States.X1.toString(), "X1:" + Integer.toString(x1), 0);
        statusBar.addStatus(States.Y1.toString(), "Y1:" + Integer.toString(y1), 0);

        statusBar.addStatus(States.X2.toString(), "X2:" + Integer.toString(x2), 1);
        statusBar.addStatus(States.Y2.toString(), "Y2:" + Integer.toString(y2), 1);

        statusBar.addStatus(States.Gap.toString(), "Gap:" + Integer.toString(gap), 0);
        statusBar.addStatus(States.ShiftPressed.toString(), "Shift Pressed: " + Boolean.toString(shiftPressed), 0);

        statusBar.addStatus("C", "C to clear", 1);

        frame.pack();
        frame.setVisible(true);
    }

    void UpdateBar() {
        statusBar.editStatus(States.X1.toString(), "X1: " + Integer.toString(x1));
        statusBar.editStatus(States.Y1.toString(), "Y1: " + Integer.toString(y1));

        statusBar.editStatus(States.X2.toString(), "X2: " + Integer.toString(x2));
        statusBar.editStatus(States.Y2.toString(), "Y2: " + Integer.toString(y2));

        statusBar.editStatus(States.Gap.toString(), "Gap: " + Integer.toString(gap));
        statusBar.editStatus(States.ShiftPressed.toString(), "Shift Pressed: " + Boolean.toString(shiftPressed));
    }

    public void clear() {
        img.clear(backgroundColor);
    }

    public void present(Graphics graphics) {
        img.present(graphics);
    }

    public void draw() {
        clear();

        Polygon pol = new Polygon();

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'c') {
                    pol.getListPoints().clear();
                    clear();
                    panel.repaint();
                }
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shiftPressed = true;
                    UpdateBar();
                    panel.repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shiftPressed = false;
                    UpdateBar();
                    panel.repaint();
                }
            }
        });


        panel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                if (notches < 0) {
                    gap++;
                    if (gap > 15) gap = 15;
                } else {
                    gap--;
                    if (gap < 0) gap = 0;
                }
                if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK) {
                    clear();
                    drawLine();

                }
                if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == MouseEvent.BUTTON3_DOWN_MASK) {
                    clear();
                    drawPolygon(pol, false);

                }

                UpdateBar();
                panel.repaint();
            }
        });


        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x1 = e.getX();
                y1 = e.getY();

                if (e.getButton() == MouseEvent.BUTTON3) {
                    addPolygonPoint(pol, x1, y1);
                }
                if (e.getButton() == MouseEvent.BUTTON1) {
                    drawPoint();
                }

                panel.repaint();
                UpdateBar();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    clear();
                    if (pol.getListPoints().size() > 1) {
                        pol.setPoint(pol.getListPoints().size() - 1, new Point2D(x2, y2));

                        drawPolygon(pol, false); //update color of last point in polygon


                    }

                }

                if(pol.getListPoints().size() > 2){
                    //Point2D centerPoint = findSeed(pol.getListPoints());
                    //seed.fill(img, (int) centerPoint.x, (int)centerPoint.y, 0xffff00, new TestBackground(backgroundColor));
                    scanLine.fill(pol,img,0xffff00);
                }

                UpdateBar();
                panel.repaint();
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                clear();
                x2 = e.getX();
                y2 = e.getY();
                if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == MouseEvent.BUTTON3_DOWN_MASK) {
                    updatePolygon(pol, x2, y2);
                }
                if ((e.getModifiersEx() & MouseEvent.BUTTON2_DOWN_MASK) == MouseEvent.BUTTON2_DOWN_MASK) {
                    pol.drawCircle(img, x1, y1, calculateRadius(x1, y1, x2, y2), gap, 100, 0xffff00);
                }
                if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK) {
                    if (shiftPressed) {
                        smartLine();
                    }

                    drawPolygon(pol, false);
                    //drawLine();
                }
                panel.repaint();
                UpdateBar();
            }


        });


    }

    public Point2D findSeed(List<Point2D> polygonPoints) {
        int sumX = 0, sumY = 0;
        for (Point2D p : polygonPoints) {
            sumX += p.x;
            sumY += p.y;
        }
        return new Point2D(sumX / polygonPoints.size(), sumY / polygonPoints.size());
    }

    private double calculateRadius(double h, double k, double x, double y) {
        return Math.sqrt(Math.pow(x - h, 2) + Math.pow(y - k, 2));
    }

    private void smartLine() {
        double dx = Math.abs(x2 - x1);
        double dy = Math.abs(y2 - y1);
        double tolerance = 0.5;

        if (dx > dy) {
            if (dy / dx < Math.tan(Math.toRadians(45)) - tolerance) { // Horizontal
                y2 = y1;
            } else { // Diagonal
                if (y2 > y1) {
                    y2 = y1 + (int) dx;
                } else {
                    y2 = y1 - (int) dx;
                }
            }
        } else {
            if (dx / dy < Math.tan(Math.toRadians(45)) - tolerance) { // Vertical
                x2 = x1;
            } else { // Diagonal
                if (x2 > x1) {
                    x2 = x1 + (int) dy;
                } else {
                    x2 = x1 - (int) dy;
                }
            }
        }
    }

    private void drawPoint() {
        img.setColor(0xffff00,x1, y1 + 1);
        img.setColor(0xffff00,x1, y1 - 1);
        img.setColor(0xffff00,x1 + 1, y1);
        img.setColor(0xffff00,x1 - 1, y1);
    }

    private void drawLine() {
        linerTrivial.drawLine(img, (double) x1, (double) y1, (double) x2, (double) y2, gap, 0xffff00);
    }

    private void addPolygonPoint(Polygon pol, int x, int y) {
        pol.addPoint(new Point2D(x, y));
    }

    private void updatePolygon(Polygon pol, int x, int y) {
        if (pol.getListPoints().size() < 2) {
            addPolygonPoint(pol, 0, 0);
        }

        pol.getListPoints().set(pol.getListPoints().size() - 1, new Point2D(x, y));// update last point

        drawPolygon(pol, true);


    }

    private void drawPolygon(Polygon pol, boolean isUpdate) {
        if (pol.containMoreThanOneLine()) {
            pol.drawPolygon(img, gap, 0xffff00, isUpdate ? 0xff0000 : 0xffff00); //draw polygon with last point
        }
    }

    public void start() {
        draw();
        panel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Canvas(600, 600).start());
    }

}