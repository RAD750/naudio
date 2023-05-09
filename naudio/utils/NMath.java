package naudio.utils;

public class NMath {
    public static double inverseLerp(double a, double b, double v) {
        return (v - a) / (b - a);
    }

    public static double clamp(double a, double b, double v) {
        return Math.max(a, Math.min(b, v));
    }
}
