package naudio.utils;

public class NData {
    private final int id;
    private final String urlString;
    private final double sourceX;
    private final double sourceY;
    private final double sourceZ;

    public NData(int id, String urlString, double sourceX, double sourceY, double sourceZ) {
        this.id = id;
        this.urlString = urlString;
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.sourceZ = sourceZ;
    }

    public int getId() {
        return id;
    }

    public String getUrlString() {
        return urlString;
    }

    public double getSourceX() {
        return sourceX;
    }

    public double getSourceY() {
        return sourceY;
    }

    public double getSourceZ() {
        return sourceZ;
    }
}
