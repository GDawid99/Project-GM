package com.project.files.gui;

public class RGBColor {
    private int r, g, b;
    public RGBColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RGBColor)) return false;

        RGBColor rgbColor = (RGBColor) o;

        if (getR() != rgbColor.getR()) return false;
        if (getG() != rgbColor.getG()) return false;
        return getB() == rgbColor.getB();
    }

    @Override
    public int hashCode() {
        int result = getR();
        result = 31 * result + getG();
        result = 31 * result + getB();
        return result;
    }
}
