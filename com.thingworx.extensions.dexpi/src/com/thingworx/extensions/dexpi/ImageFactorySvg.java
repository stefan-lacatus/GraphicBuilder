package com.thingworx.extensions.dexpi;

import org.dexpi.pid.imaging.ImageFactory_SVG;

import java.awt.image.BufferedImage;

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
}
