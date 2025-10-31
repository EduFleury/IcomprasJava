package io.github.eduardopina.icompras.faturamento;

import io.github.eduardopina.icompras.faturamento.bucket.BucketFile;
import io.github.eduardopina.icompras.faturamento.bucket.BucketService;
import io.github.eduardopina.icompras.faturamento.model.Pedido;
import io.github.eduardopina.icompras.faturamento.service.NotaFiscalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayInputStream;

@Component
@Slf4j
@RequiredArgsConstructor
public class GeradorNotaFiscalService {

    private final NotaFiscalService notaFiscalService;
    private final BucketService bucketService;

    public void gerar(Pedido pedido) {
        log.info("Gerada nota fiscal para o pedido {} ", pedido.codigo());
//        try {
//            byte[] byteArray = notaFiscalService.gerarNota(pedido);
//
//            String nomeArquivo = String.format("notafiscal_pedido_%d.pdf", pedido.codigo());
//            Long tamanhoArquivo = Long.valueOf(byteArray.length);
//            var file = new BucketFile(
//                    nomeArquivo, new ByteArrayInputStream(byteArray), MediaType.APPLICATION_PDF, tamanhoArquivo);
//
//            bucketService.upload(file);
//        }catch (Exception e){
//            log.error(e.getMessage(), e);
//        }
    }
}
