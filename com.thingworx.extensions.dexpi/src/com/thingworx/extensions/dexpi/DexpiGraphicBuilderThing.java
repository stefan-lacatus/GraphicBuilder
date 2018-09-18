package com.thingworx.extensions.dexpi;

import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
import com.thingworx.metadata.FieldDefinition;
import com.thingworx.metadata.annotations.ThingworxServiceDefinition;
import com.thingworx.metadata.annotations.ThingworxServiceParameter;
import com.thingworx.metadata.annotations.ThingworxServiceResult;
import com.thingworx.metadata.collections.FieldDefinitionCollection;
import com.thingworx.types.BaseTypes;
import com.thingworx.types.InfoTable;
import com.thingworx.types.collections.AspectCollection;
import com.thingworx.types.collections.ValueCollection;
import org.dexpi.pid.imaging.*;
import org.dexpi.pid.xml.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.String;
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

        FieldDefinitionCollection dexpiGenericAttr = new FieldDefinitionCollection();
        dexpiGenericAttr.addFieldDefinition(new FieldDefinition("Set", BaseTypes.STRING));
        dexpiGenericAttr.addFieldDefinition(new FieldDefinition("Name", BaseTypes.STRING));
        dexpiGenericAttr.addFieldDefinition(new FieldDefinition("Value", BaseTypes.STRING));
        dexpiGenericAttr.addFieldDefinition(new FieldDefinition("DefaultValue", BaseTypes.STRING));
        dexpiGenericAttr.addFieldDefinition(new FieldDefinition("Units", BaseTypes.STRING));
        dexpiGenericAttr.addFieldDefinition(new FieldDefinition("Format", BaseTypes.STRING));
        dexpiGenericAttr.addFieldDefinition(new FieldDefinition("AttributeUri", BaseTypes.STRING));
        dexpiGenericAttr.addFieldDefinition(new FieldDefinition("ValueUri", BaseTypes.STRING));
        dexpiGenericAttr.addFieldDefinition(new FieldDefinition("UnitsUri", BaseTypes.STRING));
        this.defineDataShapeDefinition("DexpiGenericAttribute", dexpiGenericAttr);


        // generate information about the dexpi info datashape
        FieldDefinitionCollection dexpiDef = new FieldDefinitionCollection();
        dexpiDef.addFieldDefinition(new FieldDefinition("TagName", BaseTypes.STRING));
        dexpiDef.addFieldDefinition(new FieldDefinition("Specification", BaseTypes.STRING));
        dexpiDef.addFieldDefinition(new FieldDefinition("SpecificationUri", BaseTypes.STRING));
        dexpiDef.addFieldDefinition(new FieldDefinition("StockNumber", BaseTypes.STRING));
        dexpiDef.addFieldDefinition(new FieldDefinition("ComponentName", BaseTypes.STRING));
        dexpiDef.addFieldDefinition(new FieldDefinition("ComponentType", BaseTypes.STRING));
        dexpiDef.addFieldDefinition(new FieldDefinition("ComponentClass", BaseTypes.STRING));
        dexpiDef.addFieldDefinition(new FieldDefinition("ComponentClassUri", BaseTypes.STRING));
        dexpiDef.addFieldDefinition(new FieldDefinition("Revision", BaseTypes.STRING));
        dexpiDef.addFieldDefinition(new FieldDefinition("RevisionUri", BaseTypes.STRING));
        dexpiDef.addFieldDefinition(new FieldDefinition("Status", BaseTypes.STRING));
        dexpiDef.addFieldDefinition(new FieldDefinition("StatusUri", BaseTypes.STRING));
        dexpiDef.addFieldDefinition(new FieldDefinition("Id", BaseTypes.STRING));
        dexpiDef.addFieldDefinition(new FieldDefinition("Purpose", BaseTypes.STRING));
        dexpiDef.addFieldDefinition(new FieldDefinition("ElementTagName", BaseTypes.STRING));
        dexpiDef.addFieldDefinition(new FieldDefinition("Attributes", BaseTypes.INFOTABLE, AspectCollection.fromString("dataShape:DexpiGenericAttribute")));
        dexpiDef.addFieldDefinition(new FieldDefinition("Subcomponents", BaseTypes.INFOTABLE, AspectCollection.fromString("dataShape:DexpiObjectInfo")));

        this.defineDataShapeDefinition("DexpiObjectInfo", dexpiDef);
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
        GraphicFactory gFac = new ImageFactorySvg();
        GraphicBuilder gBuilder = new GraphicBuilder(inputRep, gFac, errorRep);

        gBuilder.buildImage(resolutionX, null);

        logger.info("Generator finished.");
        return ((ImageFactorySvg) gFac).getImageSvg().toByteArray();
    }

    @ThingworxServiceDefinition(
            name = "GetEquipmentInformation", description = "Uses the dexpi file as the source for the plant schema")
    @ThingworxServiceResult(
            name = "result", description = "", baseType = "INFOTABLE", aspects = {"dataShape:DexpiObjectInfo"})
    public InfoTable GetEquipmentInformation(@ThingworxServiceParameter(
            name = "data", description = "", baseType = "BLOB") byte[] data) throws Exception {
        logger.info("Starting generator for dexpi file info.");

        InfoTable result = new InfoTable(this.getDataShapeDefinition("DexpiObjectInfo"));

        JaxbInputRepository inputRep = new JaxbInputRepository(new ByteArrayInputStream(data));

        for (Object object : inputRep.getPlantModel().presentationOrShapeCatalogueOrDrawing) {
            if (object instanceof PlantItem) {
                if (object instanceof Equipment) {
                    Equipment equipment = (Equipment) object;
                    logger.info("Parsing equipment " + equipment.getComponentName() + " " + equipment.getTagName() + " with ID " + equipment.getID());
                    ValueCollection collection = parseEquipment(equipment);
                    result.addRow(collection);
                } else if (object instanceof PipingNetworkSystem) {
                    PipingNetworkSystem pipingNetworkSystem = (PipingNetworkSystem) object;

                    logger.info("Parsing pipingNetwork system " + pipingNetworkSystem.getComponentName() + " " + pipingNetworkSystem.getTagName() + " with ID " + pipingNetworkSystem.getID());
                    ValueCollection collection = parsePipingNetworkSystem(pipingNetworkSystem);
                    result.addRow(collection);

                }
            }
        }
        logger.info("Finished generation");
        return result;
    }

    /**
     * Transforms a pipingNetworkSystem into a value collection using the dexpiEquipmentDatashape
     *
     * @param pipingNetworkSystem pipingNetworkSystem to parse
     * @return an infotable with the equipment information
     * @throws Exception
     */
    private ValueCollection parsePipingNetworkSystem(PipingNetworkSystem pipingNetworkSystem) throws Exception {
        ValueCollection collection = parseGenericPlantElementWithAttributes(pipingNetworkSystem);
        InfoTable children = new InfoTable((this.getDataShapeDefinition("DexpiObjectInfo")));
        for (Object child : pipingNetworkSystem.getNominalDiameterOrInsideDiameterOrOutsideDiameter()) {
            if (child instanceof PipingNetworkSystem) {
                children.addRow(parsePipingNetworkSystem((PipingNetworkSystem) child));
            } else if (child instanceof PipingNetworkSegment) {
                children.addRow(parsePipingNetworkSegment((PipingNetworkSegment) child));
            }
        }
        collection.SetInfoTableValue("Subcomponents", children);

        return collection;
    }

    /**
     * Transforms a pipingNetworkSegment into a value collection using the dexpiEquipmentDatashape
     *
     * @param pipingNetworkSegment pipingNetworkSegment to parse
     * @return an infotable with the equipment information
     * @throws Exception
     */
    private ValueCollection parsePipingNetworkSegment(PipingNetworkSegment pipingNetworkSegment) throws Exception {
        ValueCollection collection = parseGenericPlantElementWithAttributes(pipingNetworkSegment);
        InfoTable children = new InfoTable((this.getDataShapeDefinition("DexpiObjectInfo")));
        for (Object child : pipingNetworkSegment.getNominalDiameterOrInsideDiameterOrOutsideDiameter()) {
            if (child instanceof PipingNetworkSegment) {
                children.addRow(parsePipingNetworkSegment((PipingNetworkSegment) child));
            } else if (child instanceof PipingComponent) {
                children.addRow(parseGenericPlantElementWithAttributes((PipingComponent) child));
            } else if (child instanceof Equipment) {
                children.addRow(parseEquipment((Equipment) child));
            }
        }
        collection.SetInfoTableValue("Subcomponents", children);

        return collection;
    }


    /**
     * Transforms a equipment into a value collection using the dexpiEquipmentDatashape
     *
     * @param equipment Equipment to parse
     * @return an infotable with the equipment information
     * @throws Exception
     */
    private ValueCollection parseEquipment(Equipment equipment) throws Exception {
        ValueCollection collection = parseGenericPlantElementWithAttributes(equipment);
        collection.SetStringValue("Purpose", equipment.getPurpose());
        InfoTable children = new InfoTable((this.getDataShapeDefinition("DexpiObjectInfo")));
        for (Object child : equipment.getDisciplineOrMinimumDesignPressureOrMaximumDesignPressure()) {
            if (child instanceof Equipment) {
                children.addRow(parseEquipment((Equipment) child));
            } else if (child instanceof Nozzle) {
                children.addRow(parseGenericPlantElementWithAttributes((Nozzle) child));
            }
        }
        collection.SetInfoTableValue("Subcomponents", children);

        return collection;
    }

    private ValueCollection parseGenericPlantElementWithAttributes(PlantItem equipment) throws Exception {
        ValueCollection collection = new ValueCollection();
        collection.SetStringValue("ElementTagName", equipment.getClass().getSimpleName());
        collection.SetStringValue("TagName", equipment.getTagName());
        collection.SetStringValue("Specification", equipment.getSpecification());
        collection.SetStringValue("SpecificationUri", equipment.getSpecificationURI());
        collection.SetStringValue("StockNumber", equipment.getStockNumber());
        collection.SetStringValue("ComponentName", equipment.getComponentName());
        collection.SetStringValue("ComponentType", equipment.getComponentType());
        collection.SetStringValue("ComponentClass", equipment.getComponentClass());
        collection.SetStringValue("ComponentClassUri", equipment.getComponentClassURI());
        collection.SetStringValue("Revision", equipment.getRevision());
        collection.SetStringValue("RevisionUri", equipment.getRevisionURI());
        collection.SetStringValue("Status", equipment.getStatus());
        collection.SetStringValue("StatusUri", equipment.getStatusURI());
        collection.SetStringValue("Id", equipment.getID());
        InfoTable attrs = new InfoTable(this.getDataShapeDefinition("DexpiGenericAttribute"));

        // look through all the GenericAttributes of this equipment
        for (Object child : equipment.getPresentationOrExtentOrPersistentID()) {
            if (child instanceof GenericAttributes) {
                //  logger.info("Parsing equipment attributes with set " + ((GenericAttributes) child).getSet());
                String setName = ((GenericAttributes) child).getSet();
                // transfrom the Generic Attributes into infotable rows
                for (Object attrChild : ((GenericAttributes) child).getContent()) {
                    if (attrChild instanceof GenericAttribute) {
                        GenericAttribute genericAttr = (GenericAttribute) attrChild;
                        ValueCollection attrCollection = new ValueCollection();
                        attrCollection.SetStringValue("Name", genericAttr.getName());
                        attrCollection.SetStringValue("Value", genericAttr.getValue());
                        attrCollection.SetStringValue("DefaultValue", genericAttr.getDefaultValue());
                        attrCollection.SetStringValue("Units", genericAttr.getUnits());
                        attrCollection.SetStringValue("Format", genericAttr.getFormat());
                        attrCollection.SetStringValue("AttributeUri", genericAttr.getAttributeURI());
                        attrCollection.SetStringValue("UnitsUri", genericAttr.getUnitsURI());
                        attrCollection.SetStringValue("ValueUri", genericAttr.getValueURI());
                        attrCollection.SetStringValue("Set", setName);
                        attrs.addRow(attrCollection);
                    }
                }

            }
        }
        collection.SetInfoTableValue("Attributes", attrs);

        return collection;
    }


}
