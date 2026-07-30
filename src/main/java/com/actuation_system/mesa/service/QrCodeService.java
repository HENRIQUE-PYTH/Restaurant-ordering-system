package com.actuation_system.mesa.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class QrCodeService {


    public void gerarQrCode(String token) {

        try {

            String url = "http://localhost:8080/api/mesas/" + token;

            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            BitMatrix bitMatrix = qrCodeWriter.encode(
                    url,
                    BarcodeFormat.QR_CODE,
                    300,
                    300
            );

            Path diretorio = Paths.get("qrcodes");

            if (Files.notExists(diretorio)) {
                Files.createDirectories(diretorio);
            }

            Path path = diretorio.resolve(token + ".png");
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar QR Code.", e);
        }

    }

}
