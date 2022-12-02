import javax.swing.JComponent;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JImageDisplay extends JComponent{
    private BufferedImage matrix;

    JImageDisplay(int width, int height){
        super();
        this.matrix = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.setPreferredSize(new Dimension(width, height));
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(this.matrix, 0, 0, this.matrix.getWidth(), this.matrix.getHeight(), null);
    }

    public void clearImage(){
        matrix.setRGB(0, 0, this.matrix.getWidth(), this.matrix.getHeight(), new int[this.matrix.getWidth() * this.matrix.getHeight()], 0, 1);
    }

    public void drawPixel(int x, int y, int color){
        matrix.setRGB(x, y, color);
    }

    public BufferedImage getMatrix(){
        return this.matrix;
    }
}