package com.actuation_system.mesa.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class QrCodeService {

    public void gerarQrCode(String token) {

        String url = "https://meusistema.com.br/mesa/" + token;

        try {

            QRCodeWriter qrCodeWriter = new QRCodeWriter(); //gera a matriz do QR Code.

            BitMatrix bitMatrix = qrCodeWriter.encode( // representa o QR Code em memória.
                    url,
                    BarcodeFormat.QR_CODE, //informa que o código será do tipo QR_CODE.
                    300,
                    300
            );

            Path path = Paths.get("qrcodes/" + token + ".png");

            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path); //converte a matriz em uma imagem PNG.

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar QR Code.", e);
        }

    }

}
