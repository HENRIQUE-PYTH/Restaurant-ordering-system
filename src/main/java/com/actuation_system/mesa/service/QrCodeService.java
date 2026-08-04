package com.actuation_system.mesa.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;



@Service
public class QrCodeService {

    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendBaseUrl;

    public byte[] gerarImagemQrCode(String token) {
        try {
            String url = frontendBaseUrl + "/mesa/" + token;

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 300, 300);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar QR Code.", e);
        }
    }
}
