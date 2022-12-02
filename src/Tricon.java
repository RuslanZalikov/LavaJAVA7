import java.awt.geom.Rectangle2D;

public class Tricon extends FractalGenerator{

    private final int MAX_ITERATIONS = 2000;

    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -2;
        range.width = 4;
        range.height = 4;
    }


    @Override
    public int numIterations(double x, double y) {
        int iteration = 0; // число итераций
        double zreal = 0; // действительная часть числа
        double zimaginary = 0; // мнимая часть числа

        while (iteration < this.MAX_ITERATIONS
                && zreal * zreal + zimaginary * zimaginary < 4){
            double zrealUpdated = zreal * zreal - zimaginary * zimaginary + x;
            double zimaginaryUpdated = -(2 * zrealUpdated * zimaginary) + y;
            zreal = zrealUpdated;
            zimaginary = zimaginaryUpdated;
            iteration++;
        }

        if (iteration == this.MAX_ITERATIONS) {
            return -1;
        }
        return iteration;
    }

    public String toString(){
        return "Tricon";
    }

}