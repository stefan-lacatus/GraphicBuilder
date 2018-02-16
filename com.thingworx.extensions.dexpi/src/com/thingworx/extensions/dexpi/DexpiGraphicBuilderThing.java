package com.thingworx.extensions.dexpi;

import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
import com.thingworx.extensions.dexpi.old.SvgImageFactory;
import com.thingworx.metadata.annotations.ThingworxServiceDefinition;
import com.thingworx.metadata.annotations.ThingworxServiceParameter;
import com.thingworx.metadata.annotations.ThingworxServiceResult;
import org.dexpi.pid.imaging.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.logging.Logger;

public class DexpiGraphicBuilderThing extends VirtualThing {
    private static Logger logger =
            Logger.getLogger(DexpiGraphicBuilderThing.class.getName());
    private static final boolean USE_DEXPI_IMPL = true;

    public DexpiGraphicBuilderThing(String name, String description, String identifier, ConnectedThingClient client) throws Exception {
        super(name, description, identifier, client);
        super.initializeFromAnnotations();

    }

    @ThingworxServiceDefinition(
            name = "GenerateImageForFile", description = "Generates a new PNG file out of a given sample file")
    @ThingworxServiceResult(
            name = "result", description = "", baseType = "IMAGE")
    public byte[] GenerateImageForFile(@ThingworxServiceParameter(
            name = "data", description = "", baseType = "BLOB") byte[] data) throws Exception {
        logger.info("Starting Tester for Graphic Builder.");

        final Path tempFilePath = Paths.get(String.format("tmp%d.xml", Instant.now().toEpochMilli()));

        Files.write(tempFilePath, data);

        // Building image from xmlFile:

        int resolutionX = 6000;

        JaxbErrorLogRepository errorRep = new JaxbErrorLogRepository(tempFilePath.toFile());
        InputRepository inputRep = new JaxbInputRepository(tempFilePath.toFile());
        GraphicFactory gFac = new ImageFactory_PNG();
        GraphicBuilder gBuilder = new GraphicBuilder(inputRep, gFac, errorRep);
        BufferedImage image = gBuilder.buildImage(resolutionX, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);

        logger.info(gBuilder.generateHTMLimageMap("TestImageMap"));

        logger.info("Tester finished.");
        return baos.toByteArray();
    }

    @ThingworxServiceDefinition(
            name = "GenerateSvgForFile", description = "Generates a new SVG file out of a given sample file")
    @ThingworxServiceResult(
            name = "result", description = "", baseType = "BLOB")
    public byte[] GenerateSvgForFile(@ThingworxServiceParameter(
            name = "data", description = "", baseType = "BLOB") byte[] data) throws Exception {
        logger.info("Starting generator for Graphic Builder.");

        final Path tempFilePath = Paths.get(String.format("tmp%d.xml", Instant.now().toEpochMilli()));

        // Building image from xmlFile:

        int resolutionX = 6000;

        JaxbErrorLogRepository errorRep = new JaxbErrorLogRepository(tempFilePath.toFile());
        InputRepository inputRep = new JaxbInputRepository(new ByteArrayInputStream(data));
        if(USE_DEXPI_IMPL) {
            GraphicFactory gFac = new ImageFactorySvg();
            GraphicBuilder gBuilder = new GraphicBuilder(inputRep, gFac, errorRep);

            gBuilder.buildImage(resolutionX, null);

            logger.info("Generator finished.");
            return ((ImageFactorySvg) gFac).getImageSvg().toByteArray();
        } else {
            StringWriter writer = new StringWriter();

            GraphicFactory gFac = new SvgImageFactory(writer);
            GraphicBuilder gBuilder = new GraphicBuilder(inputRep, gFac, errorRep);

            gBuilder.buildImage(resolutionX, "test.svg");
            logger.info(gBuilder.generateHTMLimageMap("TestImageMap"));


            logger.info("Tester finished.");
            return writer.toString().getBytes("UTF-8");
        }

    }
}
