package com.automation.web.interaction;

import lombok.Setter;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Lazy
@Service
/**
 * A class for taking screenshots and storing them in a structured directory system.
 * Screenshots are stored at 'build/screenshots/' by default
 */
@Log
public class Screenshot {

    private WebDriver driver;
    private String rootSaveDir = "build/screenshots";
    @Setter
    private boolean takeScreenshots = Boolean.parseBoolean(System.getProperty("screenshots", "true"));

    public Screenshot(WebDriver driver) {
        this.driver = driver;
    }

    public void setSaveDirectory(String rootSaveDir) {
        this.rootSaveDir = rootSaveDir;
    }

    /**
     * Take a screenshot as bytes.
     *
     * @return byte array of screenshot
     */
    public byte[] asBytes() {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.severe("Error taking screenshot: " + e.getMessage());
            return new byte[0];
        }
    }

    /**
     * Save screenshot to file. Directory structure is feature/com.alex.page/screenshot[number].png Disable
     * screenshots with system property '-Dscreenshots=false'.
     *
     * @param feature name of feature
     * @param page name of com.alex.page
     */
    public void saveToFile(String feature, String page) {
        saveToFile(feature, page, null, 300);
    }

    /**
     * Save screenshot to file. Directory structure is feature/com.alex.page/screenshot[number].png Disable
     * screenshots with system property '-Dscreenshots=false'.
     *
     * @param feature name of feature
     * @param page name of com.alex.page
     * @param name filename
     */
    public void saveToFile(String feature, String page, String name) {
        saveToFile(feature, page, name, 300);
    }

    /**
     * Save screenshot to file. Directory structure is feature/com.alex.page/name[number].png Disable
     * screenshots with system property '-Dscreenshots=false'.
     *
     * @param feature name of feature
     * @param page name of com.alex.page
     * @param name name of file. Defaults to 'screenshot' if null
     * @param resizeWidth pixel width of the image if it should be scaled. 0 value saves the
     *    image at original size. Aspect ratio is maintained.
     */
    public void saveToFile(String feature, String page, String name, int resizeWidth) {
        if (takeScreenshots) {
            try {
                File tmpScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                File screenshot = getOutputFile(feature, page, name);

                if (resizeWidth > 0) {
                    BufferedImage image = ImageIO.read(tmpScreenshot);
                    ImageIO.write(scaleImage(image, resizeWidth), "png", screenshot);
                } else {
                    FileUtils.copyFile(tmpScreenshot, screenshot);
                }
            } catch (IOException e) {
                log.severe("Error saving screenshot: " + e.getMessage());
                log.fine(ExceptionUtils.getStackTrace(e));
            }
        } else {
            log.info("Screenshots disabled");
        }
    }

    /**
     * Save screenshot to file. Directory structure is feature/com.alex.page/name[number].png Disable
     * screenshots with system property '-Dscreenshots=false'.
     *
     * @param feature name of feature
     * @param page name of com.alex.page
     * @param name name of file. Defaults to 'screenshot' if null
     * @param scaledPercent percentage at which the image should be scaled. 0 value saves the
     *    image at original size. Aspect ratio is maintained.
     */
    public void saveToFile(String feature, String page, String name, double scaledPercent) {
        if (takeScreenshots) {
            try {
                File tmpScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                File screenshot = getOutputFile(feature, page, name);

                BufferedImage image = ImageIO.read(tmpScreenshot);
                int scaledWidth = (int) (image.getWidth() * (scaledPercent / 100D));
                ImageIO.write(scaleImage(image, scaledWidth), "png", screenshot);

            } catch (IOException e) {
                log.severe("Error saving screenshot: " + e.getMessage());
                log.fine(ExceptionUtils.getStackTrace(e));
            }
        } else {
            log.info("Screenshots disabled");
        }
    }

    /**
     * Get the output file where the screenshot will be written to.
     *
     * @param feature name of feature
     * @param page name of com.alex.page
     * @param name name of file. Defaults to 'screenshot' if null
     * @return screenshot output file
     */
    private File getOutputFile(String feature, String page, String name) {
        if(StringUtils.isEmpty(name)) {
            name = "screenshot";
        }

        File screenshot = processFilename(
                String.format("%s/%s/%s/%s.png", rootSaveDir, feature, page, name));
        log.info("Taking screenshot. Stored at " + screenshot.getAbsolutePath());

        if (!screenshot.getParentFile().exists()) {
            screenshot.getParentFile().mkdirs();
        }

        return screenshot;
    }

    /**
     * Scale image size
     *
     * @param image The image to be scaled
     * @param newWidth The required width
     * @return The scaled image
     */
    private BufferedImage scaleImage(BufferedImage image, int newWidth) {
        double aspectRatio = (double) image.getWidth() / (double) image.getHeight();
        int newHeight = (int) (newWidth / aspectRatio);
        Image scaledImg = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = newImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(scaledImg, 0, 0, null);

        return newImage;
    }

    /**
     * Check if file already exists and if it does then increment the filename with a number.
     *
     * @param filepath path of file
     * @return new writable file that does not currently exist
     */
    private File processFilename(String filepath) {
        File file = new File(filepath);

        if (file.exists()) {
            int fileNumber = 1;
            int dotIndex = filepath.lastIndexOf('.');
            String prefix = filepath.substring(0, dotIndex);
            String suffix = filepath.substring(dotIndex);

            do {
                file = new File(prefix + (fileNumber++) + suffix);
            } while (file.exists());
        }

        return file;
    }
}
