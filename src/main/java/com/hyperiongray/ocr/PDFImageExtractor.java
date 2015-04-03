package com.hyperiongray.ocr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

/**
 *
 * Class PDFImageExtractor.
 *
 * Extract images from PDF files, if any.
 *
 * @author ilazarov
 *
 */
public class PDFImageExtractor extends ImageExtractor {

    protected PDFImageExtractor(String file) {
        super(file);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<String> extractImages() {
        File extractionDir = new File(conf.getPdfImageExtractionDir());
        extractionDir.mkdirs();

        List<String> result = new ArrayList<String>();

        PDDocument document = null;
        try {
            document = PDDocument.load(file);

            List pages = document.getDocumentCatalog().getAllPages();
            Iterator iter = pages.iterator();
            int i = 1;
            while (iter.hasNext()) {
                PDPage page = (PDPage) iter.next();
                PDResources resources = page.getResources();
                Map pageImages = resources.getImages();
                if (pageImages != null) {
                    Iterator imageIter = pageImages.keySet().iterator();
                    while (imageIter.hasNext()) {
                        String key = (String) imageIter.next();
                        PDXObjectImage image = (PDXObjectImage) pageImages.get(key);

                        String fileName = conf.getPdfImageExtractionDir() + OCRUtil.createUniqueFileName("image");
                        image.write2file(fileName);

                        result.add(fileName + "." + image.getSuffix());

                        i++;
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return result;
    }
}
