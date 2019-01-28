/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drawingapplication;

import drawingapplication.DrawingApplication.MyBoundedShape;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author nmh5395
 */



public class DrawingApplication extends JFrame {
    /**
     * @param args the command line arguments
     */
    private final JButton undo;
    private final JButton clear;
    private final JLabel shape;
    private final JLabel status_bar;
    private final JComboBox drop_down;
    private final JCheckBox filled;
    private final JCheckBox gradient;
    private final JButton color1;
    private final JButton color2;
    private final JCheckBox dashed;
    private final JLabel width_label;
    private final JLabel length_label;
    private final JTextField width_text_field;
    private final JTextField length_text_field;
    private final JPanel panel1;
    private final JPanel panel2;
    private final JPanel panel1ANDpanel2;
    private final DrawingPanel drawing_panel;
    private Color one_color = Color.LIGHT_GRAY;
    private Color two_color = Color.LIGHT_GRAY;
    private JFrame frame;
    public MyShape currentShape;

    
    private final ArrayList<MyShape> shapes = new ArrayList<>();
    
    
    public DrawingApplication() {
        super();
        
        undo = new JButton("Undo");
        clear = new JButton("Clear");
        shape = new JLabel("Shape:");
        drop_down = new JComboBox();
        color1 = new JButton("1st Color");
        color2 = new JButton("2nd Color");
        filled = new JCheckBox("Filled");
        gradient = new JCheckBox("Use Gradient");
        width_label = new JLabel("Line Width:");
        width_text_field = new JTextField("10");
        length_label = new JLabel("Dash Length:");
        length_text_field = new JTextField("10");
        dashed = new JCheckBox("Dashed");
        status_bar = new JLabel("");

        width_text_field.setPreferredSize(new Dimension(50, 24));
        length_text_field.setPreferredSize(new Dimension(50, 24));
        
//------------------------------------------------------------------------------

        panel1 = new JPanel(); //First panel
        
        panel1.add(undo);
        panel1.add(clear);
        panel1.add(shape);
        panel1.add(drop_down);
        panel1.add(filled);
        
        drop_down.addItem("Line");
        drop_down.addItem("Oval");
        drop_down.addItem("Rectangle");
        
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                
                shapes.remove(shapes.size() - 1);
                repaint();
            }
        });
       
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                
                shapes.clear();
                repaint();
            }
        });
        
        drop_down.getSelectedIndex();
        
//------------------------------------------------------------------------------
        panel2 = new JPanel(); //Second panel
        
        panel2.add(gradient);
        panel2.add(color1);
        panel2.add(color2);
        panel2.add(width_label);
        panel2.add(width_text_field);
        panel2.add(length_label);
        panel2.add(length_text_field);
        panel2.add(dashed);
        
        color1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                one_color = JColorChooser.showDialog(null, "First Color", one_color);
                
            }
        });
       
        color2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                two_color = JColorChooser.showDialog(null, "Second Color", two_color);
                
            }
        });

        panel1ANDpanel2 = new JPanel();
        panel1ANDpanel2.setLayout(new BorderLayout());
        panel1ANDpanel2.add(panel1, BorderLayout.NORTH);
        panel1ANDpanel2.add(panel2, BorderLayout.SOUTH);
        
        add(panel1ANDpanel2, BorderLayout.NORTH);
        
//------------------------------------------------------------------------------        
        drawing_panel = new DrawingPanel(); //Drawing panel
        
        drawing_panel.setBackground(Color.WHITE);
        add(drawing_panel, BorderLayout.CENTER);
        add(status_bar, BorderLayout.SOUTH);
    }
    
    private class DrawingPanel extends JPanel {
        
        public DrawingPanel() {
            MouseHandler handler = new MouseHandler();
            addMouseListener(handler);
            addMouseMotionListener(handler);
        }
        @Override
        public void paintComponent(Graphics g) {
            
            super.paintComponent(g);
            
            Graphics2D g2d = (Graphics2D) g;
            
            for (MyShape shape: shapes) {
                g2d.setPaint(shape.getPaint());
                g2d.setStroke(shape.getStroke());
                shape.draw(g2d);
            }
        }
        
        private class MouseHandler extends MouseAdapter {
            
            @Override
            public void mousePressed(MouseEvent event) {
                status_bar.setText(String.format("(%d, %d)", event.getX(), event.getY()));
                Point sp = event.getPoint();
                
                Paint paint;
                if (gradient.isSelected()) {
                    paint = new GradientPaint(0,0,one_color, 50, 50, two_color, true);
                }
                else {
                    paint = one_color;
                }
                
                Stroke stroke;
                
                if (dashed.isSelected()) {
                    float dashes [] = {0};
                    dashes[0] = Float.parseFloat(length_text_field.getText());
                    stroke = new BasicStroke(Integer.parseInt(length_text_field.getText()), 
                                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashes, 0);
                }
                
                else {
                    stroke = new BasicStroke(Integer.parseInt(width_text_field.getText()), 
                                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                }
                
                
                
                switch (drop_down.getSelectedIndex()) {
                    case 0: 
                        currentShape = new MyLine(sp, sp, paint, stroke);
                        break;
                        
                    case 1:
                        currentShape = new MyOval(sp, sp, paint, stroke, filled.isSelected());
                        break;
                        
                    case 2:
                        currentShape = new MyRect(sp, sp, paint, stroke, filled.isSelected());
                        break;
                }         
                
                shapes.add(currentShape);
                
            }
            
            @Override
            public void mouseReleased(MouseEvent event) {
                status_bar.setText(String.format("(%d, %d)", event.getX(), event.getY()));
                currentShape = null;
            }
            
            @Override
            public void mouseDragged(MouseEvent event) {
                status_bar.setText(String.format("(%d, %d)", event.getX(), event.getY()));
                
                shapes.get(shapes.size()-1).setEp(event.getPoint()); ;
                repaint();

            }
            
            @Override 
            public void mouseMoved(MouseEvent event) {
                status_bar.setText(String.format("(%d, %d)", event.getX(), event.getY()));
            }
            
        }
    }
    
    private abstract class MyShape {
        
        private Point sp;
        private Point ep;
        private Paint paint;
        private Stroke stroke;
        
        public MyShape(Point sp, Point ep, Paint paint, Stroke stroke) {
            this.sp = sp;
            this.ep = ep;
            this.paint = paint;
            this.stroke = stroke;
        } 
        
        public abstract void draw(Graphics2D g2d);

        /**
         * @return the sp
         */
        public Point getSp() {
            return sp;
        }

        /**
         * @return the ep
         */
        public Point getEp() {
            return ep;
        }

        /**
         * @param ep the ep to set
         */
        public void setEp(Point ep) {
            this.ep = ep;
        }

        /**
         * @return the paint
         */
        public Paint getPaint() {
            return paint;
        }

        /**
         * @return the stroke
         */
        public Stroke getStroke() {
            return stroke;
        }
        
    }
    
    public class MyLine extends MyShape{
        public MyLine(Point sp, Point ep, Paint paint, Stroke stroke) {
            super(sp, ep, paint, stroke);
        
        }
     
        public void draw(Graphics2D g2d) {
            g2d.drawLine((int) getSp().getX(), (int) getSp().getY(), (int) getEp().getX(), (int) getEp().getY());
        }
    }   
    
    public abstract class MyBoundedShape extends MyShape {
        private boolean filled;
        
        public MyBoundedShape(Point sp, Point ep, Paint paint, Stroke stroke, boolean filled) {
            super(sp, ep, paint, stroke);
            this.filled = filled;
        }
        
        public int getWidth() {
     
            return Math.abs((int) getSp().getX() - (int) getEp().getX());
        }
        
        public int getHeight() {
            
            return Math.abs((int) getSp().getY() - (int) getEp().getY());
        }
        
        public int getTopX() {
            return Math.min((int) getSp().getX(), (int) getEp().getX());
        }
        
        public int getTopY() {
            return Math.min((int) getSp().getY(), (int) getEp().getY()); 
        }

        /**
         * @return the filled
         */
        public boolean isFilled() {
            return filled;
        }

        /**
         * @param filled the filled to set
         */
        public void setFilled(boolean filled) {
            this.filled = filled;
        }
    }
        
    
    
    public class MyOval extends MyBoundedShape{
        public MyOval(Point sp, Point ep, Paint paint, Stroke stroke, boolean filled) {
            super(sp, ep, paint, stroke, filled);
        }
        
        public void draw(Graphics2D g2d) { 
            if (isFilled()){
                
                g2d.fillOval(getTopX(), getTopY(), getWidth(), getHeight());
            }
            
            else {
                
            g2d.drawOval(getTopX(), getTopY(), getWidth(), getHeight());
            
            }
        }    
        
    }
    
    public class MyRect extends MyBoundedShape{
        public MyRect(Point sp, Point ep, Paint paint, Stroke stroke, boolean filled) {
            super(sp, ep, paint, stroke, filled);
            
        }
        public void draw(Graphics2D g2d) {
            if (isFilled()) {
                g2d.fillRect(getTopX(), getTopY(), getWidth(), getHeight());
            }
            else{
                
            g2d.drawRect(getTopX(), getTopY(), getWidth(), getHeight());
            }
        }
    }
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        DrawingApplication photoshop = new DrawingApplication();
        photoshop.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        photoshop.setSize(960, 720);
        photoshop.setVisible(true);
    }
    
}