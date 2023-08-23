package com.avada.myHouse24.util;

import com.avada.myHouse24.entity.House;
import com.avada.myHouse24.entity.User;
import com.avada.myHouse24.model.HouseForAddDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
//import org.springframework.mock.web.MockMultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;

@Log4j2
@RequiredArgsConstructor
@Component
public class ImageUtil {
    @Value("${upload.path}")
    private String uploadPath;

    public void saveFile(String uploadDir, String fileName, MultipartFile file) throws IOException {

        log.info(this.uploadPath);

        Path path = Paths.get(uploadPath + uploadDir);

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = path.resolve(fileName);
            log.info("Saving path: " + filePath);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }
    public static String imageForUser(User user, MultipartFile image) throws IOException {
        String nameImage ="";
        try {
            Path uploadPath = Paths.get("uploads/user");
            String originalFilename = image.getOriginalFilename();
            String format = originalFilename.substring(originalFilename.lastIndexOf("."));
            nameImage = generateName() + format;
            Files.copy(image.getInputStream(), uploadPath.resolve(nameImage));
            deleteImage(String.valueOf(uploadPath.resolve(user.getImage())));
        } catch (Exception e) {
            log.warn(e);
        }
        return nameImage;
    }
    public static String imageForHouseForAdd(MultipartFile file) throws IOException {
        String nameImage ="";
        try {
            Path uploadPath = Paths.get("uploads/house");
            String originalFilename = file.getOriginalFilename();
            String format = originalFilename.substring(originalFilename.lastIndexOf("."));
            nameImage = generateName() + format;
            Files.copy(file.getInputStream(), uploadPath.resolve(nameImage));
        } catch (Exception e) {
            log.warn(e);
        }
        return nameImage;
    }
    public static String imageForHouseForEdit(MultipartFile file, String previousImageName) throws IOException {
        String nameImage ="";
        try {
            Path uploadPath = Paths.get("uploads/house");
            String originalFilename = file.getOriginalFilename();
            String format = originalFilename.substring(originalFilename.lastIndexOf("."));
            nameImage = generateName() + format;
            Files.copy(file.getInputStream(), uploadPath.resolve(nameImage));
            deleteImage(String.valueOf(uploadPath.resolve(previousImageName)));
        } catch (Exception e) {
            log.warn(e);
        }
        return nameImage;
    }

    public static String generateName() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            int index = random.nextInt(alphabet.length());
            sb.append(alphabet.charAt(index));
        }
        return sb.toString();
    }

    public static void deleteImage(String name) {
        new File(name).delete();
    }

//    public MultipartFile convertToMultipartFile(String filePath) throws IOException {
//        File file = new File(filePath);
//        Path path = Paths.get(file.getAbsolutePath());
//        String name = file.getName();
//        String originalFileName = file.getName();
//        String contentType = Files.probeContentType(path);
//        byte[] content = Files.readAllBytes(path);
//
//        return new MockMultipartFile(name, originalFileName, contentType, content);
//    }
}
