package com.MedSetu.med_setu.Services;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

@Service
public class OCRServiceImp implements OCRService {

    @Override
    public String extractText(String filePath) {

        try {

            // ================= IMAGE LOAD =================
            BufferedImage original = ImageIO.read(new File(filePath));

            // ================= ENLARGE IMAGE (2X) =================
            Image scaled = original.getScaledInstance(
                    original.getWidth() * 2,
                    original.getHeight() * 2,
                    Image.SCALE_SMOOTH
            );

            BufferedImage enlarged = new BufferedImage(
                    scaled.getWidth(null),
                    scaled.getHeight(null),
                    BufferedImage.TYPE_INT_RGB
            );

            Graphics2D g2d = enlarged.createGraphics();
            g2d.drawImage(scaled, 0, 0, null);
            g2d.dispose();

            // ================= GRAYSCALE =================
            BufferedImage gray = new BufferedImage(
                    enlarged.getWidth(),
                    enlarged.getHeight(),
                    BufferedImage.TYPE_BYTE_GRAY
            );

            Graphics g = gray.getGraphics();
            g.drawImage(enlarged, 0, 0, null);
            g.dispose();

            // ================= THRESHOLD (HIGH CONTRAST) =================
            for (int y = 0; y < gray.getHeight(); y++) {
                for (int x = 0; x < gray.getWidth(); x++) {

                    int rgb = gray.getRGB(x, y);
                    int r = (rgb >> 16) & 0xff;

                    if (r > 150) {
                        gray.setRGB(x, y, 0xFFFFFF);
                    } else {
                        gray.setRGB(x, y, 0x000000);
                    }
                }
            }

            // ================= CROP BOTTOM HALF =================
            int width = gray.getWidth();
            int height = gray.getHeight();

            BufferedImage cropped = gray.getSubimage(
                    0,
                    height / 2,
                    width,
                    height / 2
            );

            // ================= TESSERACT CONFIG =================
            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
            tesseract.setLanguage("eng");

            // Better for small blocks
            tesseract.setPageSegMode(6);

            // LSTM engine
            tesseract.setOcrEngineMode(1);

            // Whitelist only useful characters
            tesseract.setTessVariable(
                    "tessedit_char_whitelist",
                    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789:/- "
            );

            // ================= OCR RUN =================
            String result = tesseract.doOCR(cropped);

            return result;

        } catch (Exception e) {
            throw new RuntimeException("OCR Failed: " + e.getMessage());
        }
    }
}
