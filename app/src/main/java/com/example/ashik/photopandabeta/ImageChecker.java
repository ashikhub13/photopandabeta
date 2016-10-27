package com.example.ashik.photopandabeta;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by ashik on 27/10/16.
 */
public class ImageChecker {

    private Bitmap one;
    private Bitmap two;
    private double difference = 0;
    private int x = 0;
    private int y = 0;

    public ImageChecker() {

    }

    public boolean compareImages() {
        int f = 20;
        int w1 = Math.min(50, one.getWidth() - two.getWidth());
        int h1 = Math.min(50, one.getHeight() - two.getHeight());
        int w2 = Math.min(5, one.getWidth() - two.getWidth());
        int h2 = Math.min(5, one.getHeight() - two.getHeight());
        for (int i = 0; i <= one.getWidth() - two.getWidth(); i += f) {
            for (int j = 0; j <= one.getHeight() - two.getHeight(); j += f) {
                compareSubset(i, j, f);
            }
        }
        one = one.createBitmap(one,Math.max(0, x - w1), Math.max(0, y - h1),
                Math.min(two.getWidth() + w1, one.getWidth() - x + w1),
                Math.min(two.getHeight() + h1, one.getHeight() - y + h1));
        x = 0;
        y = 0;
        difference = 0;
        f = 5;
        for (int i = 0; i <= one.getWidth() - two.getWidth(); i += f) {
            for (int j = 0; j <= one.getHeight() - two.getHeight(); j += f) {
                compareSubset(i, j, f);
            }
        }
        one = one.createBitmap(one,Math.max(0, x - w2), Math.max(0, y - h2),
                Math.min(two.getWidth() + w2, one.getWidth() - x + w2),
                Math.min(two.getHeight() + h2, one.getHeight() - y + h2));

        f = 1;
        for (int i = 0; i <= one.getWidth() - two.getWidth(); i += f) {
            for (int j = 0; j <= one.getHeight() - two.getHeight(); j += f) {
                compareSubset(i, j, f);
            }
        }
        System.out.println(difference);
        return difference < 0.1;
    }

    public void compareSubset(int a, int b, int f) {
        double diff = 0;
        for (int i = 0; i < two.getWidth(); i += f) {
            for (int j = 0; j < two.getHeight(); j += f) {
                int colorone = 0,colortwo=0;
                colorone = one.getPixel(i + a, j + b);
                colortwo = two.getPixel(i,j);
                int r1 = (colorone >> 16);
                int g1 = (colorone >> 8) & 0xff;
                int b1 = (colorone) & 0xff;
                int r2 = (colortwo >> 16);
                int g2 = (colortwo >> 8) & 0xff;
                int b2 = (colortwo) & 0xff;
                diff += (Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1
                        - b2)) / 3.0 / 255.0;
            }
        }
        double percentDiff = diff * f * f / (two.getWidth() * two.getHeight());
        if (percentDiff < difference || difference == 0) {
            difference = percentDiff;
            x = a;
            y = b;
        }
    }

    public Bitmap getOne() {
        return one;
    }

    public void setOne(Bitmap one) {
        this.one = one;
    }

    public Bitmap getTwo() {
        return two;
    }

    public void setTwo(Bitmap two) {
        this.two = two;
    }
}
