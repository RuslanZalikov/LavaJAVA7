import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator{

    public static int MAX_ITERATIONS = 2000;

    public void getInitialRange(Rectangle2D.Double range)
    {
        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }
    public int numIterations(double x, double y) {
        /** Start with iterations at 0. */
        int iteration = 0;
        /** Initialize zreal and zimaginary. */
        double zreal = 0;
        double zimaginary = 0;

        /**
         * Вычисляем Zn = Zn-1 ^ 2 + c, где значения представляют собой комплексные числа, представленные
         * по zreal и zimaginary, Z0 = 0, а c - особая точка в
         * фрактал, который мы показываем (заданный x и y). Это повторяется
         * до Z ^ 2> 4 (абсолютное значение Z больше 2) или максимум
         * достигнуто количество итераций.
         */
        while (iteration < this.MAX_ITERATIONS &&
                zreal * zreal + zimaginary * zimaginary < 4) {
            double zrealUpdated = zreal * zreal - zimaginary * zimaginary + x;
            double zimaginaryUpdated = 2 * zreal * zimaginary + y;
            zreal = zrealUpdated;
            zimaginary = zimaginaryUpdated;
            iteration += 1;
        }

        /**
         * Если количество максимальных итераций достигнуто, возвращаем -1, чтобы
         * указать, что точка не вышла за границу.
         */
        if (iteration == this.MAX_ITERATIONS) {
            return -1;
        }

        return iteration;
    }

    public String toString(){
        return "Mandelbrot";
    }
}