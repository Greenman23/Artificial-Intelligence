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

    public static double dotProduct(double[] a, double[] b) {
        double dotProduct = 0;
        for (int i = 0; i < a.length; i++) {
            dotProduct += a[i] * b[i];
        }
        return dotProduct;
    }

    public static int maxIndex(double[] a) {
        int max = 0;
        for (int i = 1; i < a.length; i++) {
            if (a[i] > a[max]) max = i;
        }
        return max;
    }

    public static int minIndex(double[] a) {
        int min = 0;
        for (int i = 1; i < a.length; i++) {
            if (a[i] < a[min]) min = i;
        }
        return min;
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public static double getRealDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public static int[] convertIntToBinary( int x, int base) {
        int arr[] = new int[base];
        int curentNum = (int)Math.pow(2,base) / 2;
        for (int i = 0; i < arr.length; i++) {
          if(curentNum <= x){
              arr[i] = 1;
              x -= curentNum;
          }

          else{
              arr[i] = 0;
          }
        }

        return arr;
    }


    public static int convertToDecimal(int[] arr) {
        int base = 1;
        int value = 0;
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] == 1)
                value += base;
            base *= 2;
        }
        return value;
    }


    public static boolean epsilon(double value1, double value2, double compareFactor) {
        double max = Math.max(value1, value2);
        double min = Math.min(value1, value2);
        if (max - compareFactor <= min)
            return true;
        else
            return false;
    }
}
