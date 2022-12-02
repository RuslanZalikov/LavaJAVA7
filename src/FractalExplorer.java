import javax.imageio.ImageIO;
import javax.management.remote.JMXConnectorFactory;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicButtonListener;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.Objects;

public class FractalExplorer
{
    /** Целочисленный размер отображения - это ширина и высота отображения в пикселях. **/
    private int displaySize;

    /**
     * Ссылка JImageDisplay для обновления отображения с помощью различных методов как
     * фрактал вычислен.
     */
    private JImageDisplay display;

    /** Объект FractalGenerator для каждого типа фрактала. **/
    private FractalGenerator fractal;

    /**
     * Объект Rectangle2D.Double, который определяет диапазон
     * то, что мы в настоящее время показываем.
     */
    private Rectangle2D.Double range;

    /**
     * Конструктор, который принимает размер отображения, сохраняет его и
     * инициализирует объекты диапазона и фрактал-генератора.
     */
    public FractalExplorer(int size) {
        /** Размер дисплея  **/
        this.displaySize = size;

        /** Инициализирует фрактальный генератор и объекты диапазона. **/
        this.fractal = new Mandelbrot();
        this.range = new Rectangle2D.Double();
        this.fractal.getInitialRange(range);
        this.display = new JImageDisplay(displaySize, displaySize);

    }

    /**
     * Этот метод инициализирует графический интерфейс Swing с помощью JFrame, содержащего
     * Объект JImageDisplay и кнопку для очистки дисплея
     */
    public void createAndShowGUI()
    {
        /** Установите для frame использование java.awt.BorderLayout для своего содержимого. **/
        this.display.setLayout(new BorderLayout());
        JFrame myFrame = new JFrame("Fractal Explorer");

        /** Добавьте объект отображения изображения в
         * BorderLayout.CENTER position.
         */
        myFrame.add(this.display, BorderLayout.CENTER);

        /** Экземпляр MouseHandler в компоненте фрактального отображения. **/
        MouseHandler click = new MouseHandler();
        this.display.addMouseListener(click);

        /** Вызываем операцию закрытия фрейма по умолчанию на "выход".. **/
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);




        /**
         * Создаем новый объект JPanel, и добавляем панель в рамку в NORTH и SOUTH
         * позиции в макете.
         */
        JPanel nPanel = new JPanel();
        JPanel sPanel = new JPanel();

        JLabel label = new JLabel("Fractal:");

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox) e.getSource();
                FractalGenerator item = (FractalGenerator) box.getSelectedItem();
                fractal = item;
                fractal.getInitialRange(range);
                drawFractal();
            }
        };

        JComboBox fractalList = new JComboBox();
        FractalGenerator mandelbrot = new Mandelbrot();
        FractalGenerator tricorn = new Tricon();
        FractalGenerator burningShip = new BurningShip();
        fractalList.addItem(mandelbrot);
        fractalList.addItem(tricorn);
        fractalList.addItem(burningShip);
        fractalList.setSelectedIndex(0);
        fractalList.addActionListener(actionListener);

        nPanel.add(label);
        nPanel.add(fractalList);

        myFrame.add(nPanel, BorderLayout.NORTH);


        ActionListener buttonActionListener = new ButtonListener();
        JButton resetButton = new JButton("Reset Display");
        resetButton.setActionCommand("reset");
        resetButton.addActionListener(buttonActionListener);

        JButton saveButton = new JButton("Save Image");
        saveButton.setActionCommand("save");
        saveButton.addActionListener(buttonActionListener);

        sPanel.add(resetButton);
        sPanel.add(saveButton);
        myFrame.add(sPanel, BorderLayout.SOUTH);




        /**
         * Размещаем содержимое фрейма, делаем его видимым и
         * запрещаем изменение размера окна.
         */
        myFrame.pack();
        myFrame.setVisible(true);
        myFrame.setResizable(false);

    }

    /**
     * Приватный вспомогательный метод для отображения фрактала. Этот метод проходит
     * через каждый пиксель на дисплее и вычисляет количество
     * итераций для соответствующих координат во фрактале
     * Область отображения. Если количество итераций равно -1, установит цвет пикселя.
     * в черный. В противном случае выберет значение в зависимости от количества итераций.
     * Обновит дисплей цветом для каждого пикселя и перекрасит
     * JImageDisplay, когда все пиксели нарисованы.
     */
    private void drawFractal()
    {
        /**Проходим через каждый пиксель на дисплее **/
        for (int x=0; x<this.displaySize; x++){
            for (int y=0; y<this.displaySize; y++){

                /**
                 * Находим соответствующие координаты xCoord и yCoord
                 * в области отображения фрактала.
                 */
                double xCoord = this.fractal.getCoord(this.range.x,
                        this.range.x + this.range.width, this.displaySize, x);
                double yCoord = this.fractal.getCoord(this.range.y,
                        this.range.y + this.range.height, this.displaySize, y);

                /**
                 * Вычисляем количество итераций для координат в
                 * область отображения фрактала.
                 */
                int iteration = this.fractal.numIterations(xCoord, yCoord);

                /** если -1 то красим в черный **/
                if (iteration == -1){
                    display.drawPixel(x, y, 0);
                }

                else {
                    /**
                     * В противном случае выбераем значение оттенка на основе числа
                     * итераций.
                     */
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                    /** Обновляем дисплей цветом для каждого пикселя. **/
                    this.display.drawPixel(x, y, rgbColor);
                }

            }
        }
        /**
         * Когда все пиксели будут нарисованы, перекрасим JImageDisplay, чтобы он соответствовал
         * текущее содержимому его изображения.
         */
        this.display.repaint();
    }

    /**
     * Внутренний класс для обработки событий MouseListener с дисплея.
     */
    private class MouseHandler extends MouseAdapter
    {
        /**
         * Когда обработчик получает событие щелчка мыши, он отображает пиксель-
         * координаты щелчка в области фрактала, который
         * отображается, а затем вызывает функцию RecenterAndZoomRange () генератора
         * метод с координатами, по которым был выполнен щелчок, и шкалой 0,5.
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            /** Получаем координату x области отображения щелчка мыши. **/
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x,
                    range.x + range.width, displaySize, x);

            /** Получаем координату y области отображения щелчка мышью. **/
            int y = e.getY();
            double yCoord = fractal.getCoord(range.y,
                    range.y + range.height, displaySize, y);

            /**
             * Вызывааем метод генератора RecenterAndZoomRange() с помощью
             * координаты, по которым был выполнен щелчок, и шкала 0,5.
             */
            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

            /**
             * Перерисовываем фрактал после изменения отображаемой области.
             */
            drawFractal();
        }
    }

    private class ButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if (e.getActionCommand().equals("reset")){
                fractal.getInitialRange(range);
                drawFractal();
            }
            if (e.getActionCommand().equals("save")){
                JFileChooser fileChooser = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
                fileChooser.setFileFilter(filter);
                fileChooser.setAcceptAllFileFilterUsed(false);
                int result = fileChooser.showSaveDialog(display);
                if (result == JFileChooser.APPROVE_OPTION){
                    try {
                        ImageIO.write(display.getMatrix(), "png", fileChooser.getSelectedFile());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(fileChooser, ex.getMessage(), "Ошибка сохранения", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        }
    }

    /**
     * Статический метод main() для запуска FractalExplorer. Инициализирует новый
     * Экземпляр FractalExplorer с размером дисплея 600, вызывает
     * createAndShowGU () в объекте проводника, а затем вызывает
     * drawFractal() в проводнике, чтобы увидеть исходный вид.
     */
    public static void main(String[] args)
    {
        FractalExplorer displayExplorer = new FractalExplorer(800);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}