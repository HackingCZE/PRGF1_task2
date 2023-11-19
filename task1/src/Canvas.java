import objectdata.Point2D;
import objectdata.Polygon;
import objectdata.Rectangle;
import objectdata.Triangle;
import objectop.PolygonCutter;
import rasterdata.Raster;
import rasterdata.RasterBI;
import rasterops.LinerTrivial;
import rasterops.fill.ScanLine;
import rasterops.fill.SeedFill4Stack;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
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

    enum Modes {
        Triangle,
        Rectangle,
        Polygon;

        public Modes next() {
            switch (this) {
                case Polygon:
                    return Triangle;
                case Triangle:
                    return Rectangle;
                case Rectangle:
                    return Polygon;
                default:
                    throw new AssertionError("Undefined code: " + this);
            }
        }
    }

    enum States {
        ShiftPressed,
        Gap,
        X1,
        Y1,
        X2,
        Y2,
        Mode,
        Pattern
    }

    Modes currentMode = Modes.Polygon;
    boolean shiftPressed = false;
    boolean isPattern = false;
    LinerTrivial linerTrivial;
    StatusBar statusBar;
    int backgroundColor = 0x000000;
    int primaryColor = 0x93C47D;
    int lineColor = 0xffffff;
    SeedFill4Stack seed;
    ScanLine scanLine;
    Rectangle rectangle;
    Triangle triangle;
    Polygon pol;
    Polygon circle;

    boolean polygonUpdating;

    public Canvas(int width, int height) {
        frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setTitle("UHK FIM PGRF1 : " + this.getClass().getName());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        img = new RasterBI(width, height);

        createPanel(width, height);
    }

    void createPanel(int width, int height) {
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
        statusBar.addStatus("Q", "Q to switch mode", 1);
        statusBar.addStatus(States.Mode.toString(), "Current mode: " + currentMode.toString(), 1);

        statusBar.addStatus("P", "P to switch pattern", 0);
        statusBar.addStatus(States.Pattern.toString(), "Pattern: " + isPattern, 0);

        frame.pack();
        frame.setVisible(true);
    }

    void start() {
        init();
        draw();
        panel.repaint();
        update();
    }


    void update() {
        scanLine.fill(img, pol, primaryColor, isPattern);
        scanLine.fill(img, triangle.getPol(), primaryColor, isPattern);
        scanLine.fill(img, rectangle.getPol(), primaryColor, isPattern);
        scanLine.fill(img, circle, primaryColor, isPattern);
        if (!polygonUpdating){
            drawPolygon(pol, false);
        }
        polygonUpdating = false;

        triangle.updateShape(img, gap, lineColor);

        rectangle.updateShape(img, gap, lineColor);

        circle.updateCircle(img, gap, lineColor);

        panel.repaint();
        updateBar();
    }

    void updateBar() {
        statusBar.editStatus(States.X1.toString(), "X1: " + Integer.toString(x1));
        statusBar.editStatus(States.Y1.toString(), "Y1: " + Integer.toString(y1));

        statusBar.editStatus(States.X2.toString(), "X2: " + Integer.toString(x2));
        statusBar.editStatus(States.Y2.toString(), "Y2: " + Integer.toString(y2));

        statusBar.editStatus(States.Gap.toString(), "Gap: " + Integer.toString(gap));
        statusBar.editStatus(States.ShiftPressed.toString(), "Shift Pressed: " + Boolean.toString(shiftPressed));

        statusBar.editStatus(States.Mode.toString(), "Current mode: " + currentMode.toString());
        statusBar.editStatus(States.Pattern.toString(), "Pattern: " + isPattern);
    }

    public void clear() {
        img.clear(backgroundColor);
    }

    void reset() {
        init();
        clear();
        update();
    }

    void init(){
        seed = new SeedFill4Stack();
        scanLine = new ScanLine();

        linerTrivial = new LinerTrivial();
        rectangle = new Rectangle(0, 0);
        triangle = new Triangle();
        pol = new Polygon();
        circle = new Polygon();
    }

    public void present(Graphics graphics) {
        img.present(graphics);
    }

    void switchMode() {
        currentMode = currentMode.next();
    }

    public void draw() {
        clear();
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                clear();
                if (e.getKeyChar() == 'c') {
                    reset();
                }
                if (e.getKeyChar() == 'q') {
                    switchMode();
                }
                if (e.getKeyChar() == 'p') {
                    isPattern = !isPattern;
                }
                update();
            }
        });

        // shift btn
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shiftPressed = true;
                    update();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shiftPressed = false;
                    update();
                }
            }
        });


        panel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                clear();
                int notches = e.getWheelRotation();
                if (notches < 0) {
                    gap++;
                    if (gap > 15) gap = 15;
                } else {
                    gap--;
                    if (gap < 0) gap = 0;
                }

                if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK) {
                    drawLine();
                }
                if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == MouseEvent.BUTTON3_DOWN_MASK) {
                    drawPolygon(pol, false);
                }
                update();
            }
        });


        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("ll");
                x1 = e.getX();
                y1 = e.getY();

                if (e.getButton() == MouseEvent.BUTTON3) {
                    switch (currentMode) {
                        case Triangle -> {
                            drawLine();
                        }
                        case Rectangle -> {
                            rectangle = new Rectangle(x1, y1);
                        }
                        case Polygon -> {
                            addPolygonPoint(pol, x1, y1);
                        }
                    }

                }
                if (e.getButton() == MouseEvent.BUTTON1) {
                    drawPoint();
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                clear();
                if (e.getButton() == MouseEvent.BUTTON3) {
                    switch (currentMode) {
                        case Triangle -> {
                            if (!triangle.isSetStartLine()) {
                                triangle.drawStartLine(img, gap, new Point2D(x1, y1), new Point2D(x2, y2), primaryColor);
                            }
                        }
                        case Rectangle -> {
                        }
                        case Polygon -> {
                            if (pol.getListPoints().size() > 1) {
                                pol.setPoint(pol.getListPoints().size() - 1, new Point2D(x2, y2));

                                drawPolygon(pol, false); //update color of last point in polygon
                                polygonUpdating = true;
                            }
                        }
                    }
                }

                update();
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                clear();
                x2 = e.getX();
                y2 = e.getY();
                if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == MouseEvent.BUTTON3_DOWN_MASK) {
                    switch (currentMode) {
                        case Triangle -> {
                            if (triangle.isSetStartLine()) {
                                triangle.drawShape(img, gap, x2, y2, primaryColor);
                            } else {
                                drawLine();
                            }
                        }
                        case Rectangle -> {
                            rectangle.drawShape(img, gap, x2, y2, primaryColor, shiftPressed);
                        }
                        case Polygon -> {
                            updatePolygon(pol, x2, y2);

                        }
                    }
                }
                if ((e.getModifiersEx() & MouseEvent.BUTTON2_DOWN_MASK) == MouseEvent.BUTTON2_DOWN_MASK) {
                    if (shiftPressed) {
                        circle.drawCircle(img, x1, y1, calculateRadius(x1, y1, x2, y2), gap, 100, primaryColor);
                    } else {
                        drawOval(img, x1, y1, x2, y2, gap, 100, primaryColor);
                    }

                }
                if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK) {
                    if (shiftPressed) {
                        smartLine();
                    }

                    drawPolygon(pol, false);
                    drawLine();
                }
                polygonUpdating = true;
                update();
            }


        });


    }

    public Point2D findSeed(List<Point2D> polygonPoints) {
        int sumX = 0, sumY = 0;
        for (Point2D p : polygonPoints) {
            sumX += p.getX();
            sumY += p.getY();
        }
        return new Point2D(sumX / polygonPoints.size(), sumY / polygonPoints.size());
    }

    private double calculateRadius(double h, double k, double x, double y) {
        return Math.sqrt(Math.pow(x - h, 2) + Math.pow(y - k, 2));
    }

    public void drawOval(Raster raster, double x1, double y1, double x2, double y2, int gap, int segments, int color) {
        double h = (x1 + x2) / 2; // střed oválu na ose x
        double k = (y1 + y2) / 2; // střed oválu na ose y

        double rx = Math.abs(x2 - x1) / 2; // poloměr oválu na ose x
        double ry = Math.abs(y2 - y1) / 2; // poloměr oválu na ose y

        circle.drawCircle(raster, h, k, rx, ry, gap, segments, color);
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
        img.setColor(primaryColor, x1, y1 + 1);
        img.setColor(primaryColor, x1, y1 - 1);
        img.setColor(primaryColor, x1 + 1, y1);
        img.setColor(primaryColor, x1 - 1, y1);

        update();
    }

    private void drawLine() {
        linerTrivial.drawLine(img, (double) x1, (double) y1, (double) x2, (double) y2, gap, primaryColor);
    }

    private void addPolygonPoint(Polygon pol, int x, int y) {
        System.out.println("add");
        pol.addPoint(new Point2D(x, y));
    }

    private void updatePolygon(Polygon pol, int x, int y) {
        if (pol.getListPoints().size() < 2) {
            addPolygonPoint(pol, 0, 0);
        }

        pol.setPoint(pol.getListPoints().size() - 1, new Point2D(x, y));// update last point

        drawPolygon(pol, true);


    }

    private void drawPolygon(Polygon pol, boolean isUpdate) {
        if (pol.containMoreThanOneLine()) {
            pol.drawPolygon(img, gap, lineColor, isUpdate ? 0xff0000 : lineColor); //draw polygon with last point
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Canvas(600, 600).start());
    }

}