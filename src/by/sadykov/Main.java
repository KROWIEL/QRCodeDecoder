package by.sadykov;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import java.awt.image.BufferedImage;
import java.io.IOException;

import marvin.image.MarvinImage;
import marvin.video.MarvinJavaCVAdapter;
import marvin.video.MarvinVideoInterface;
import marvin.video.MarvinVideoInterfaceException;

public class Main {

    private static String decodeQRCode(BufferedImage bufferedImage) throws IOException {
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            //System.out.println("There is no QR code in the image");
            return null;
        }
    }

    public static void main(String[] args) {
        MarvinVideoInterface videoAdapter = new MarvinJavaCVAdapter();
        MarvinImage image = null;
        try {
            videoAdapter.connect(0);
            image = videoAdapter.getFrame();
        } catch (MarvinVideoInterfaceException e) {
            System.out.println(e.getMessage());
        }
        while (image != null) {
            try {
                image = videoAdapter.getFrame();
                String decodedText = decodeQRCode(image.getBufferedImageNoAlpha());
                System.out.println();
                if (decodedText == null) {
                    //System.out.println("No QR Code found in the image");
                } else {
                    System.out.println("Decoded text = " + decodedText);
                }
                //Thread.sleep(1000);
            } catch (IOException e) {
                System.out.println("Could not decode QR Code, IOException :: " + e.getMessage());
            } catch (MarvinVideoInterfaceException e) {
                e.printStackTrace();
            }
        }
    }
}
