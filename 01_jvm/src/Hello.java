public class Hello {
    static {
        System.out.println("static body");
    }

    public static void main(String[] args) {
        System.out.println("main method");
        Hello hello = new Hello();
        int n = 10, sub = 1, add = 10, mul = 2, div = 10;
        double result = 0.0;

        for (int i = 0; i < 10; i++) {
            if (result > 100) {
                result = sub(result, sub);
                result = div(result, div);
            } else {
                result = mul(result, mul);
            }
            result = add(result, add);
        }
        System.out.println(result);
    }

    private static double add(double a, double b) {
        return a+b;
    }

    private static double sub(double a, double b) {
        return a-b;
    }

    private static double mul(double a, double b) {
        return a*b;
    }

    private static double div(double a, double b) {
        return a/b;
    }
}
