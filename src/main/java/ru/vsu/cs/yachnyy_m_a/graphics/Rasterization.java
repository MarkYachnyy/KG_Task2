package ru.vsu.cs.yachnyy_m_a.graphics;

import java.awt.*;

public class Rasterization {

    public static void fillTriangle(PixelWriter pixelWriter, int x1, int y1, int x2, int y2, int x3, int y3, Color color1, Color color2, Color color3) {

        if (y1 > y2) {
            int t = y1;
            y1 = y2;
            y2 = t;

            t = x1;
            x1 = x2;
            x2 = t;

            Color ct = color1;
            color1 = color2;
            color2 = ct;
        }

        if (y1 > y3) {
            int t = y1;
            y1 = y3;
            y3 = t;

            t = x1;
            x1 = x3;
            x3 = t;

            Color ct = color1;
            color1 = color3;
            color3 = ct;
        }

        if (y2 > y3) {
            int t = y2;
            y2 = y3;
            y3 = t;

            t = x2;
            x2 = x3;
            x3 = t;

            Color ct = color3;
            color3 = color2;
            color2 = ct;
        }

        int dxL, dyL, dxR, dyR, yL, yR, xL, xR;
        int dxL_sign, dxR_sign;
        boolean steepL, steepR;
        Color colorL, colorR;

        boolean longSideOnTheRight = (y3 - y1) * (x2 - x1) - (y2 - y1) * (x3 - x1) < 0;

        if (longSideOnTheRight) {
            dxL = x2 - x1;
            dxR = x3 - x1;

            xL = x2;
            yL = y2;

            xR = x3;
            yR = y3;
            colorL = color2;
            colorR = color3;
        } else {
            dxL = x3 - x1;
            dxR = x2 - x1;

            xL = x3;
            yL = y3;

            xR = x2;
            yR = y2;
            colorL = color3;
            colorR = color2;
        }


        dyL = yL - y1;
        dyR = yR - y1;
        dxL_sign = dxL == 0 ? 1 : dxL / Math.abs(dxL);
        dxR_sign = dxR == 0 ? 1 : dxR / Math.abs(dxR);

        dxL = Math.abs(dxL);
        dxR = Math.abs(dxR);


        steepL = dyL >= dxL;
        steepR = dyR >= dxR;

        int faultL_num = steepL ? dyL : dxL;
        int faultL_denom = faultL_num * 2;
        int faultR_num = steepR ? dyR : dxR;
        int faultR_denom = faultR_num * 2;

        int xcL = x1;
        int xcR = x1;

        int from, to;

        //ОТРИСОВКА ВЕРХНЕЙ ПОЛОВИНЫ
        for (int y0 = y1; y0 < y2; y0++) {

            if (steepL) {
                Color color = new Color(color1.getRed() * (yL - y0) / dyL + colorL.getRed() * (y0 - y1) / dyL,
                        color1.getGreen() * (yL - y0) / dyL + colorL.getGreen() * (y0 - y1) / dyL,
                        color1.getBlue() * (yL - y0) / dyL + colorL.getBlue() * (y0 - y1) / dyL);
                pixelWriter.setRGB(xcL, y0, color);
                from = xcL + 1;
                faultL_num += 2 * dxL;
                if (faultL_num >= faultL_denom) {
                    xcL += dxL_sign;
                    faultL_num -= faultL_denom;
                }
            } else {
                int xToMove = 1 + (faultL_denom - faultL_num) / (2 * dyL);
                xcL += xToMove * dxL_sign;
                faultL_num += 2 * dyL * xToMove - faultL_denom;
                drawLineWithInterpolation(pixelWriter, x1, xL, color1, colorL, xcL - dxL_sign, y0, xToMove, -dxL_sign);
                from = Math.max(xcL, xcL - xToMove * dxL_sign + 1);
            }

            if (steepR) {
                Color color = new Color(color1.getRed() * (yR - y0) / dyR + colorR.getRed() * (y0 - y1) / dyR,
                        color1.getGreen() * (yR - y0) / dyR + colorR.getGreen() * (y0 - y1) / dyR,
                        color1.getBlue() * (yR - y0) / dyR + colorR.getBlue() * (y0 - y1) / dyR);
                pixelWriter.setRGB(xcR, y0, color);
                to = xcR - 1;
                faultR_num += 2 * dxR;
                if (faultR_num >= faultR_denom) {
                    xcR += dxR_sign;
                    faultR_num -= faultR_denom;
                }
            } else {
                int xToMove = 1 + (faultR_denom - faultR_num) / (2 * dyR);
                xcR += xToMove * dxR_sign;
                faultR_num += 2 * dyR * xToMove - faultR_denom;
                drawLineWithInterpolation(pixelWriter, x1, xR, color1, colorR, xcR - dxR_sign, y0, xToMove, -dxR_sign);
                to = Math.min(xcR, xcR - xToMove * dxR_sign - 1);
            }

            drawLineWithInterpolation(pixelWriter, x1, x2, x3, y1, y2, y3, color1, color2, color3, y0, from, to);
        }

        //ОБРАБОТКА ПОСЛЕДНЕЙ ИТЕРАЦИИ ВЕРХНЕЙ ПОЛОВИНЫ

        if (longSideOnTheRight) {
            if (!steepL)
                drawLineWithInterpolation(pixelWriter, x1, xL, color1, colorL, x2, y2, Math.abs(xcL - x2) + 1, -dxL_sign);
            xcL = x2;
        } else {
            if (!steepR)
                drawLineWithInterpolation(pixelWriter, x1, xR, color1, colorR, x2, y2, Math.abs(xcR - x2) + 1, -dxR_sign);
            xcR = x2;
        }

        //ПЕРЕНАСТРОЙКА ПАРАМЕТРОВ ДЛЯ ОТРИСОВКИ НИЖНЕЙ ПОЛОВИНЫ
        if (longSideOnTheRight) {
            dxL = x3 - xL;
            dxL_sign = dxL == 0 ? 1 : Math.abs(dxL) / dxL;
            dxL = Math.abs(dxL);
            dyL = y3 - yL;
            steepL = dyL >= dxL;
            faultL_num = steepL ? dyL : dxL;
            faultL_denom = faultL_num * 2;
            xR = x1;
            xL = x2;
            yR = y1;
            yL = y2;

            colorR = color1;
            colorL = color2;
        } else {
            dxR = x3 - xR;
            dxR_sign = dxR == 0 ? 1 : Math.abs(dxR) / dxR;
            dxR = Math.abs(dxR);
            dyR = y3 - yR;
            steepR = dyR >= dxR;
            faultR_num = steepR ? dyR : dxR;
            faultR_denom = faultR_num * 2;
            xR = x2;
            xL = x1;
            yR = y2;
            yL = y1;
            colorR = color2;
            colorL = color1;
        }

        //ОТРИСОВКА НИЖНЕЙ ПОЛОВИНЫ

        for (int y0 = y2; y0 < y3; y0++) {
            if (steepL) {
                Color color = new Color(color3.getRed() * (y0 - yL) / dyL + colorL.getRed() * (y3 - y0) / dyL,
                        color3.getGreen() * (y0 - yL) / dyL + colorL.getGreen() * (y3 - y0) / dyL,
                        color3.getBlue() * (y0 - yL) / dyL + colorL.getBlue() * (y3 - y0) / dyL);
                pixelWriter.setRGB(xcL, y0, color);
                from = xcL + 1;
                faultL_num += 2 * dxL;
                if (faultL_num >= faultL_denom) {
                    xcL += dxL_sign;
                    faultL_num -= faultL_denom;
                }

            } else {
                int xToMove = 1 + (faultL_denom - faultL_num) / (2 * dyL);
                xcL += xToMove * dxL_sign;
                faultL_num += 2 * dyL * xToMove - faultL_denom;
                drawLineWithInterpolation(pixelWriter, x3, xL, color3, colorL, xcL - dxL_sign, y0, xToMove, -dxL_sign);
                from = Math.max(xcL, xcL - xToMove * dxL_sign + 1);
            }

            if (steepR) {
                Color color = new Color(color3.getRed() * (y0 - yR) / dyR + colorR.getRed() * (y3 - y0) / dyR,
                        color3.getGreen() * (y0 - yR) / dyR + colorR.getGreen() * (y3 - y0) / dyR,
                        color3.getBlue() * (y0 - yR) / dyR + colorR.getBlue() * (y3 - y0) / dyR);
                pixelWriter.setRGB(xcR, y0, color);
                to = xcR - 1;
                faultR_num += 2 * dxR;
                if (faultR_num >= faultR_denom) {
                    xcR += dxR_sign;
                    faultR_num -= faultR_denom;
                }
            } else {
                int xToMove = 1 + (faultR_denom - faultR_num) / (2 * dyR);
                xcR += xToMove * dxR_sign;
                faultR_num += 2 * dyR * xToMove - faultR_denom;
                drawLineWithInterpolation(pixelWriter, x3, xR, color3, colorR, xcR - dxR_sign, y0, xToMove, -dxR_sign);
                to = Math.min(xcR, xcR - xToMove * dxR_sign - 1);
            }

            drawLineWithInterpolation(pixelWriter, x1, x2, x3, y1, y2, y3, color1, color2, color3, y0, from, to);
        }

        //ОБРАБОТКА ИТЕРАЦИИ ПОСЛЕДНЕЙ ПОЛОВИНЫ

        if (!steepL)
            drawLineWithInterpolation(pixelWriter, x3, xL, color3, colorL, x3, y3, Math.abs(xcL - x3) + 1, -dxL_sign);

        if (!steepR)
            drawLineWithInterpolation(pixelWriter, x3, xR, color3, colorR, x3, y3, Math.abs(xcR - x3) + 1, -dxR_sign);

    }

    private static void drawLineWithInterpolation(PixelWriter pixelWriter, int x1, int x2, Color color1, Color color2, int xc, int yc, int l, int sign) {
        int x0 = xc;
        int dx = x2 - x1;
        for (int i = 0; i < l; i++) {
            try {
                Color color = new Color(color1.getRed() * (x2 - x0) / dx + color2.getRed() * (x0 - x1) / dx,
                        color1.getGreen() * (x2 - x0) / dx + color2.getGreen() * (x0 - x1) / dx,
                        color1.getBlue() * (x2 - x0) / dx + color2.getBlue() * (x0 - x1) / dx);
                pixelWriter.setRGB(x0, yc, color);
            } catch (Exception e) {
                fillRect(pixelWriter, x0 - 1, yc - 1, 3, 3, Color.RED);
            }
            x0 += sign;
        }
    }

    private static void drawLineWithInterpolation(PixelWriter pixelWriter, int x1, int x2, int x3, int y1, int y2, int y3, Color color1, Color color2, Color color3, int yc, int from, int to) {

        int s = x2 * y3 + x3 * y1 + x1 * y2 - y1 * x2 - y2 * x3 - x1 * y3;
        if (s == 0) return;

        int r1 = color1.getRed();
        int r2 = color2.getRed();
        int r3 = color3.getRed();

        int g1 = color1.getGreen();
        int g2 = color2.getGreen();
        int g3 = color3.getGreen();

        int b1 = color1.getBlue();
        int b2 = color2.getBlue();
        int b3 = color3.getBlue();

        for (int x0 = from; x0 <= to; x0++) {

            int s1 = x2 * y3 + x0 * y2 + yc * x3 - yc * x2 - y2 * x3 - x0 * y3;
            int s2 = x0 * y3 + x1 * yc + y1 * x3 - x0 * y1 - yc * x3 - x1 * y3;
            int s3 = x2 * yc + x1 * y2 + x0 * y1 - x2 * y1 - x0 * y2 - x1 * yc;


            int r = r1 * s1 / s + r2 * s2 / s + r3 * s3 / s;
            int g = g1 * s1 / s + g2 * s2 / s + g3 * s3 / s;
            int b = b1 * s1 / s + b2 * s2 / s + b3 * s3 / s;
            Color color;

            try {
                color = new Color(r, g, b);
                pixelWriter.setRGB(x0, yc, color);
            } catch (Exception e) {
                fillRect(pixelWriter, x0 - 1, yc - 1, 3, 3, Color.MAGENTA);
            }
        }
    }

    public static void fillTriangle(PixelWriter pixelWriter, int x1, int y1, int x2, int y2, int x3, int y3, Color color) {
        fillTriangle(pixelWriter, x1, y1, x2, y2, x3, y3, color, color, color);
    }

    public static void fillRect(PixelWriter pixelWriter, int x, int y, int width, int height, Color color) {
        for (int x0 = x; x0 < x + width; x0++) {
            for (int y0 = y; y0 < y + height; y0++) {
                pixelWriter.setRGB(x0, y0, color);
            }
        }
    }

    public static void fillCircle(PixelWriter pixelWriter, int xc, int yc, int radius, Color color) {
        int y0 = radius;
        int x0 = 0;
        for(int y = y0; y >= x0; y--){
            draw8CirclePixels(pixelWriter, xc, yc, x0, y, color);
        }
        while (x0 <= y0) {
            x0++;
            if (x0 * x0 + (y0 - 0.5) * (y0 - 0.5) - radius * radius > 0) y0--;
            for(int y = y0; y >= x0; y--){
                draw8CirclePixels(pixelWriter, xc, yc, x0, y, color);
            }
            draw8CirclePixels(pixelWriter, xc, yc, x0, y0, color);
        }
    }

    private static void draw8CirclePixels(PixelWriter pixelWriter, int xc, int yc, int x0, int y0, Color color) {
        pixelWriter.setRGB(xc + x0, yc + y0, color);
        pixelWriter.setRGB(xc - x0, yc + y0, color);
        pixelWriter.setRGB(xc + x0, yc - y0, color);
        pixelWriter.setRGB(xc - x0, yc - y0, color);
        pixelWriter.setRGB(xc + y0, yc + x0, color);
        pixelWriter.setRGB(xc - y0, yc + x0, color);
        pixelWriter.setRGB(xc + y0, yc - x0, color);
        pixelWriter.setRGB(xc - y0, yc - x0, color);
    }
}
