package org.example.utils;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {

    private static final String SCREENSHOT_DIR = "target/screenshots/";

    public static void takeScreenshot(WebDriver driver, String testName) {
        try {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

            String fileName = testName + "_" + timestamp + ".png";

            byte[] screenshotBytes = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);

            Files.createDirectories(Path.of(SCREENSHOT_DIR));
            Files.write(Path.of(SCREENSHOT_DIR + fileName), screenshotBytes);

            //ATTACHEMENT ALLURE
            attachScreenshot(screenshotBytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Attachment(value = "Screenshot - échec du test", type = "image/png")
    private static byte[] attachScreenshot(byte[] screenshotBytes) {
        return screenshotBytes;
    }
}