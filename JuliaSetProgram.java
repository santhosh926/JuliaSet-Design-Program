import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class JuliaSetProgram extends JPanel implements AdjustmentListener, ActionListener
{

    public static void main(String[] args)
    {
        JuliaSetProgram app = new JuliaSetProgram();
    }

    JFrame frame;
    JPanel scrollPanel, boxPanel, southPanel;
    JCheckBox[] boxes;
    JScrollBar aBar, bBar, cBar;
    BufferedImage img;
    double zx, zy, alpha, beta, chi;
    float zoom = 1, max, value;
    int w = 1200;
    int h = 800;

    public JuliaSetProgram()
    {
        frame = new JFrame("Julia Set Program");
        frame.add(this);

        aBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, -2000, 2000);
        alpha = aBar.getValue()/1000.0;
        aBar.addAdjustmentListener(this);

        bBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, -2000, 2000);
        beta = bBar.getValue()/1000.0;
        bBar.addAdjustmentListener(this);

        cBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 50);
        chi = cBar.getValue();
        cBar.addAdjustmentListener(this);

        scrollPanel = new JPanel();
        scrollPanel.setLayout(new GridLayout(3, 1));
        scrollPanel.add(aBar);
        scrollPanel.add(bBar);
        scrollPanel.add(cBar);

        boxPanel = new JPanel();
        boxPanel.setLayout(new GridLayout(3, 2));
        boxes = new JCheckBox[6];
        max = 100;

        for(int i = 0; i < boxes.length; i++)
        {
            boxes[i] = new JCheckBox();
            boxes[i].addActionListener(this);
            boxPanel.add(boxes[i]);
        }

        southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(scrollPanel, BorderLayout.CENTER);
        southPanel.add(boxPanel, BorderLayout.EAST);

        frame.add(southPanel, BorderLayout.SOUTH);
        frame.setSize(w, h);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public BufferedImage drawJulia()
    {
        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for(int i = 0; i < w; i++)
        {
            for(int j = 0; j < h; j++)
            {
                value = max;

                zx = 1.5 * ((i - w/2.0) / (.5 * chi * w));
                zy = (j - h/2.0) / (.5 * chi * h);

                while((zx*zx + zy*zy) < 6 && value > 0)
                {
                    double diff = (zx*zx - zy*zy) + alpha;
                    zy = (2.0 * zx * zy) + beta;
                    zx = diff;
                    value--;
                }

                int c;
                if(value>0)
                    c = Color.HSBtoRGB((max / value) % 1, 1, 1);
                else 
                    c = Color.HSBtoRGB(max / value, 0, 0);

                img.setRGB(i, j, c);
            }
        }

        return img;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //g.setColor(new Color(redValue, greenValue, blueValue));
        //g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
        g.drawImage(drawJulia(), 0, 0, null);
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) 
    {
        if(e.getSource() == aBar)
            alpha = aBar.getValue()/1000.0;

        if(e.getSource() == bBar)
            beta = bBar.getValue()/1000.0;

        if(e.getSource() == cBar)
            chi = cBar.getValue();

        repaint();
    }

    public void actionPerformed(ActionEvent e)
    {
        // if(boxes[0].isSelected())
        //     redValue = 0;
        // else
        //     redValue = aBar.getValue();

        repaint();
    }

}