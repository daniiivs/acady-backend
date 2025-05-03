package org.springboot.acadybackend.utils;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.io.InputStream;

public class PdfUtils {
    public static String exactText(InputStream inputStream) throws IOException {
        PDDocument document = Loader.loadPDF(new RandomAccessReadBuffer(inputStream));
        PDFTextStripper stripper = new PDFTextStripper();
        return stripper.getText(document);
    }
}
