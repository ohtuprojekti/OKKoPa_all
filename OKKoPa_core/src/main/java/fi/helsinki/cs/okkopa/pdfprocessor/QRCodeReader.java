package fi.helsinki.cs.okkopa.pdfprocessor;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import fi.helsinki.cs.okkopa.shared.exception.NotFoundException;
import java.awt.image.BufferedImage;
import org.springframework.stereotype.Component;

@Component
public class QRCodeReader {

    /**
     * Reads QR code from single image.
     *
     * @param page is a single PDF page converted into BufferedImage.
     * @return QR code decoded into bitmap.
     * @throws ChecksumException if error correction fails for any reason.
     * @throws NotFoundException if no QR code is found in the BufferdImage.
     * @throws FormatException if the QR code cannot be decoded.
     */
    public Result readQRCode(BufferedImage page) throws Exception {
        LuminanceSource source = new BufferedImageLuminanceSource((BufferedImage) page);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            com.google.zxing.qrcode.QRCodeReader reader2 = new com.google.zxing.qrcode.QRCodeReader();
            return reader2.decode(bitmap);
        } catch (Exception ex) {
            QRCodeMultiReader reader = new QRCodeMultiReader();
            Result[] decodeMultiple = reader.decodeMultiple(bitmap);

            if (decodeMultiple.length == 0) {
                throw new NotFoundException("Zero QR-codes found.");
            }
            return decodeMultiple[0];
        }
    }
}