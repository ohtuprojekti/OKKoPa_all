package fi.helsinki.cs.okkopa.pdfprocessor;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class QRCodeReaderTest {

    QRCodeReader reader;

    @Before
    public void setUp() throws IOException {
        reader = new QRCodeReader();
    }

    @Test(expected = Exception.class)
    public void readEmptyPage() throws Exception {
        InputStream barCodeInputStream = getClass().getResourceAsStream("/images/empty_page.png");
        BufferedImage image = ImageIO.read(barCodeInputStream);
        reader.readQRCode(image);
    }

    /**
     * Test reading an exam paper with only a single QR code.
     */
    @Test
    public void readASingleQRCode() throws Exception {
        InputStream barCodeInputStream = getClass().getResourceAsStream("/images/basic_qr-0.png");
        BufferedImage image = ImageIO.read(barCodeInputStream);
        assertEquals("asperhee", reader.readQRCode(image).getText());
    }

    /**
     * Test reading an exam paper without QR code.
     */
    @Test(expected = Exception.class)
    public void readExamPaperWithoutQRCode() throws Exception {
        InputStream barCodeInputStream = getClass().getResourceAsStream("/images/empty_page.png");
        BufferedImage image = ImageIO.read(barCodeInputStream);
        reader.readQRCode(image);
    }

    /**
     * Test reading an exam paper with two same QR codes on same side.
     */
    @Test
    public void readTwoQRCodesSameSide() throws Exception {
        InputStream barCodeInputStream = getClass().getResourceAsStream("/images/two_same.png");
        BufferedImage image = ImageIO.read(barCodeInputStream);
        assertEquals("asperhee", reader.readQRCode(image).getText());
    }
    
    /**
     * Test reading an exam paper with two same QR codes on same side.
     */
    @Test
    public void readTwoQRCodesSameSide2() throws Exception {
        InputStream barCodeInputStream = getClass().getResourceAsStream("/images/testi.png");
        BufferedImage image = ImageIO.read(barCodeInputStream);
        System.out.println(reader.readQRCode(image).getText());
        assertEquals("582201:K:2008:L:3:49naeh027", reader.readQRCode(image).getText());
        
        //same with one qr-code
        barCodeInputStream = getClass().getResourceAsStream("/images/testi2.png");
        image = ImageIO.read(barCodeInputStream);
        assertEquals("582201:K:2008:L:3:49naeh027", reader.readQRCode(image).getText());
    }

    /**
     * Test reading an exam paper with two and a half QR codes on same side.
     */
    @Test
    public void readTwoAndAHalfQRCode() throws Exception {
        InputStream barCodeInputStream = getClass().getResourceAsStream("/images/two_half_upsidedown.png");
        BufferedImage image = ImageIO.read(barCodeInputStream);
        reader.readQRCode(image);
    }

    /**
     * Test reading an exam paper with two different QR codes on same side.
     */
    @Test(expected = Exception.class)
    public void readTwoDifferentQRCodesSameSide() throws Exception {
        InputStream barCodeInputStream = getClass().getResourceAsStream("/images/two_different.png");
        BufferedImage image = ImageIO.read(barCodeInputStream);
        reader.readQRCode(image);
    }

    @Test
    public void testFailedOne() throws IOException {
        InputStream barCodeInputStream = getClass().getResourceAsStream("/images/fail60dpi.png");
        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
        
        images.add(ImageIO.read(barCodeInputStream));
        barCodeInputStream = getClass().getResourceAsStream("/images/fail65dpi.png");
        images.add(ImageIO.read(barCodeInputStream));
        barCodeInputStream = getClass().getResourceAsStream("/images/fail70dpi.png");
        images.add(ImageIO.read(barCodeInputStream));
        barCodeInputStream = getClass().getResourceAsStream("/images/fail75dpi.png");
        images.add(ImageIO.read(barCodeInputStream));
        
        boolean found = false;
        for (BufferedImage img : images) {
            try {
                found = reader.readQRCode(img).getText().equals("teeyoshi");
                if (found) {
                    break;
                }
            } catch (Exception ex) {
            }
        }
        assertTrue(found);
    }
}
