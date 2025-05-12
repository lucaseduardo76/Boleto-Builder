package com.ifba.utils.geradorPdf;

import com.ifba.model.boleto.Boleto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;

public class BoletoPdfGenerator {

    public static void gerarBoletoPdf(Boleto boleto, String caminhoArquivo) {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(caminhoArquivo));
            document.open();

            Font fonteTitulo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font fonteTexto = new Font(Font.FontFamily.HELVETICA, 10);
            Font fonteCampo = new Font(Font.FontFamily.HELVETICA, 9);

            // Cabeçalho
            PdfPTable cabecalho = new PdfPTable(new float[]{2, 6, 2});
            cabecalho.setWidthPercentage(100);
            cabecalho.setSpacingAfter(8f);
            cabecalho.addCell(celula(boleto.getBanco().getNome(), fonteTitulo, Element.ALIGN_CENTER));
            cabecalho.addCell(celula(boleto.getLinhaDigitavel(), fonteTexto, Element.ALIGN_CENTER));
            cabecalho.addCell(celula("Vencimento:\n" + boleto.getTitulo().getDataVencimento(), fonteTexto, Element.ALIGN_CENTER));
            document.add(cabecalho);

            // Local pagamento
            PdfPTable localPgto = new PdfPTable(new float[]{7, 3});
            localPgto.setWidthPercentage(100);
            localPgto.setSpacingAfter(5f);
            localPgto.addCell(celula("Local de pagamento: Pagável em qualquer banco até o vencimento", fonteCampo, Element.ALIGN_LEFT));
            localPgto.addCell(celula("Vencimento:\n" + boleto.getTitulo().getDataVencimento(), fonteCampo, Element.ALIGN_CENTER));
            document.add(localPgto);

            // Cedente
            PdfPTable cedente = new PdfPTable(new float[]{7, 3});
            cedente.setWidthPercentage(100);
            cedente.setSpacingAfter(5f);
            cedente.addCell(celula("Cedente: " + boleto.getBeneficiario().getNome(), fonteCampo, Element.ALIGN_LEFT));
            cedente.addCell(celula("Agência/Código Cedente: " + boleto.getConta().getAgencia() + "/" + boleto.getBeneficiario().getCnpjOrCpf(), fonteCampo, Element.ALIGN_LEFT));
            document.add(cedente);

            // Campos técnicos (data doc, número doc, valor etc)
            PdfPTable info = new PdfPTable(new float[]{2, 2, 2, 1, 2, 2, 2});
            info.setWidthPercentage(100);
            info.setSpacingAfter(5f);
            addCampo(info, "Data do documento", "10/05/2025");
            addCampo(info, "Número do documento", boleto.getTitulo().getNumeroDocumento());
            addCampo(info, "Espécie do documento", "DM");
            addCampo(info, "Aceite", "N");
            addCampo(info, "Data do processamento", "10/05/2025");
            addCampo(info, "Nosso número", boleto.getNumeroUnico());
            addCampo(info, "Valor do documento", "R$ " + boleto.getTitulo().getValor());
            document.add(info);

            // Instruções + descontos/multa
            PdfPTable instrucoes = new PdfPTable(new float[]{7, 3});
            instrucoes.setWidthPercentage(100);
            instrucoes.setSpacingAfter(5f);
            instrucoes.addCell(celula("Instruções: Não receber após o vencimento", fonteCampo, Element.ALIGN_LEFT));
            PdfPTable direita = new PdfPTable(1);
            direita.setWidthPercentage(100);
            direita.addCell(celula("Descontos/Abatimentos:", fonteCampo, Element.ALIGN_LEFT));
            direita.addCell(celula("Juros/Multa:", fonteCampo, Element.ALIGN_LEFT));
            direita.addCell(celula("Valor cobrado:", fonteCampo, Element.ALIGN_LEFT));
            PdfPCell cell = new PdfPCell(direita);
            cell.setRowspan(1);
            instrucoes.addCell(cell);
            document.add(instrucoes);

            // Sacado
            PdfPTable sacado = new PdfPTable(1);
            sacado.setWidthPercentage(100);
            sacado.setSpacingAfter(5f);
            sacado.addCell(celula("Sacado: " + boleto.getSacado().getNome(), fonteCampo, Element.ALIGN_LEFT));
            sacado.addCell(celula("CPF/CNPJ: " + boleto.getSacado().getCnpjOrCpf(), fonteCampo, Element.ALIGN_LEFT));
            sacado.addCell(celula("Endereço: " + boleto.getSacado().getEndereco().getRua() + ", " + boleto.getSacado().getEndereco().getBairro()
                    + " - " + boleto.getSacado().getEndereco().getCidade() + " - " + boleto.getSacado().getEndereco().getEstado()
                    + " - CEP: " + boleto.getSacado().getEndereco().getCep(), fonteCampo, Element.ALIGN_LEFT));
            document.add(sacado);

            // Código de Barras
            PdfContentByte cb = writer.getDirectContent();
            BarcodeInter25 barcode = new BarcodeInter25();

            String codigoDoBoleto = boleto.getCodigoBarras();

            if (codigoDoBoleto.length() < 44) {
                while (codigoDoBoleto.length() < 44) {
                    codigoDoBoleto = "0" + codigoDoBoleto;
                }
            } else if (codigoDoBoleto.length() > 44) {
                codigoDoBoleto = codigoDoBoleto.substring(codigoDoBoleto.length() - 44);
            }

            barcode.setCode(codigoDoBoleto);
            barcode.setBarHeight(40f);
            barcode.setX(1.2f);

            Image barcodeImage = barcode.createImageWithBarcode(cb, null, null);
            barcodeImage.setAlignment(Element.ALIGN_CENTER);
            document.add(barcodeImage);



            document.close();
            writer.close();
            System.out.println("PDF gerado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PdfPCell celula(String texto, Font fonte, int alinhamento) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, fonte));
        cell.setPadding(5);
        cell.setHorizontalAlignment(alinhamento);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private static void addCampo(PdfPTable tabela, String label, String valor) {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph();
        p.setFont(new Font(Font.FontFamily.HELVETICA, 8));
        p.add(new Chunk(label + "\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7)));
        p.add(new Chunk(valor));
        cell.setPadding(4);
        cell.addElement(p);
        tabela.addCell(cell);
    }
}

