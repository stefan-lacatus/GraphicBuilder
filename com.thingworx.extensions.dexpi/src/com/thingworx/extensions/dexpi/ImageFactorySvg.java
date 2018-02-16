package com.thingworx.extensions.dexpi;

import org.dexpi.pid.imaging.ImageFactory_SVG;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageFactorySvg extends ImageFactory_SVG {
    /**
     * Same build image as in the parent class, but does not also create a png image
     * This is because this transcoding takes very long, and we do not need its result
     *
     * @return the final image
     */
    @Override
    public BufferedImage buildImage() {
        return null;
    }

    public ByteArrayOutputStream getImageSvg() throws TransformerException, IOException {
        // we are calling the cleanup here, so we do not need to extent the
        // GraphicFactory
        cleanUpEmptyGroupNodes();

        DOMSource source = new DOMSource(doc);
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {

            StreamResult result = new StreamResult(stream);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(source, result);
            return stream;
        } catch (IOException | TransformerException e) {
            e.printStackTrace();
            throw e;
        }

    }
}
