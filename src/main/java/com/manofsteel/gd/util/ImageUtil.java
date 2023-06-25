package com.manofsteel.gd.util;

import com.manofsteel.gd.exception.ThumbnailCreationException;
import com.manofsteel.gd.exception.ThumbnailIOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@RequiredArgsConstructor
@Slf4j
@Component
public class ImageUtil {

    @Value("${resource.file.path}")
    private String filePath;
    @Value("${resource.file.url}")
    private String fileUrl;
    private static final int THUMBNAIL_WIDTH = 300;
    private static final int THUMBNAIL_HEIGHT = 300;

    public String createThumbnail(String postNum) throws ThumbnailCreationException, ThumbnailIOException {
        // Get the path to the directory.
        Path directoryPath = Paths.get(filePath + "/post/" + postNum);
        System.out.println("directoryPath: " + directoryPath);

        // Update the file list for the directory.
        File[] files = directoryPath.toFile().listFiles();
        System.out.println("files: " + files);

        if (files != null && files.length > 0) {
            // Randomly select a file.
            Random rand = new Random();
            File file = files[rand.nextInt(files.length)];

            // Create the thumbnail.
            String thumbnailFilePath = directoryPath + "/thumbnail_" + file.getName();
            try {
                Thumbnails.of(file)
                        .size(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT)
                        .toFile(thumbnailFilePath);
            } catch (IOException e) {
                throw new ThumbnailIOException(e.getMessage());
            }

            return (fileUrl + "/post/" + postNum + "/thumbnail_" + file.getName());
        } else {
            throw new ThumbnailCreationException("No files found in the directory.");
        }
    }

}
