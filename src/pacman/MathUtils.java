package pacman;

public class MathUtils {
    public static double[] subtract(double[] a, double b) {
        double[] result = new double[a.length];
        System.arraycopy(a, 0, result, 0, a.length);
        for (int i = 0; i < result.length; i++) {
            result[i] -= b;
        }
        return result;
    }

    public static double[] subtract(double[] a, double[] b) {
        double[] result = new double[a.length];
        System.arraycopy(a, 0, result, 0, a.length);
        for (int i = 0; i < result.length; i++) {
            result[i] -= b[i];
        }
        return result;
    }

    public static double[] multiply(double[] a, double b) {
        double[] result = new double[a.length];
        System.arraycopy(a, 0, result, 0, a.length);
        for (int i = 0; i < result.length; i++) {
            result[i] *= b;
        }
        return result;
    }

    public static double[] multiply(double[] a, double[] b) {
        double[] result = new double[a.length];
        System.arraycopy(a, 0, result, 0, a.length);
        for (int i = 0; i < result.length; i++) {
            result[i] *= b[i];
        }
        return result;
    }

    public static double[] divide(double[] a, double b) {
        double[] result = new double[a.length];
        System.arraycopy(a, 0, result, 0, a.length);
        for (int i = 0; i < result.length; i++) {
            result[i] /= b;
        }
        return result;
    }

    public static double[] divide(double[] a, double[] b) {
        double[] result = new double[a.length];
        System.arraycopy(a, 0, result, 0, a.length);
        for (int i = 0; i < result.length; i++) {
            result[i] /= b[i];
        }
        return result;
    }

    public static double[] add(double[] a, double b) {
        double[] result = new double[a.length];
        System.arraycopy(a, 0, result, 0, a.length);
        for (int i = 0; i < result.length; i++) {
            result[i] += b;
        }
        return result;
    }

    public static double[] add(double[] a, double[] b) {
        double[] result = new double[a.length];
        System.arraycopy(a, 0, result, 0, a.length);
        for (int i = 0; i < result.length; i++) {
            result[i] += b[i];
        }
        return result;
    }

    public static double dotProduct(double[] a, double[] b){
        double dotProduct = 0;
        for (int i = 0; i < a.length; i++) {
            dotProduct += a[i] * b[i];
        }
        return dotProduct;
    }
}
