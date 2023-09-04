package com.avada.myHouse24.util;

import com.avada.myHouse24.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
//import org.springframework.mock.web.MockMultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@Log4j2
@RequiredArgsConstructor
public class ImageUtil {
    private static final String pathToFolder = "E:/MyHouse24";
    private static String contextPath = "/myHouse24DA";
    public static String imageForUser(User user, MultipartFile image) {
        String nameImage ="";
        try {
            Path uploadPath = Paths.get(pathToFolder+"/avatar");
            String originalFilename = image.getOriginalFilename();
            String format = originalFilename.substring(originalFilename.lastIndexOf("."));
            nameImage = generateName() + format;
            Files.copy(image.getInputStream(), uploadPath.resolve(nameImage));
        } catch (Exception e) {
            log.warn(e);
        }
        return contextPath+"/uploads/avatar/"+nameImage;
    }
    public static String fileForTemplate(MultipartFile image) {
        String nameImage ="";
        try {
            Path uploadPath = Paths.get(pathToFolder+"/template");
            String originalFilename = image.getOriginalFilename();
            String format = originalFilename.substring(originalFilename.lastIndexOf("."));
            nameImage = generateName() + format;
            Files.copy(image.getInputStream(), uploadPath.resolve(nameImage));
        } catch (Exception e) {
            log.warn(e);
        }
        return contextPath+"/uploads/template/"+nameImage;
    }
    public static String imageForHouse(MultipartFile file) {
        String nameImage ="";
        try {
            Path uploadPath = Paths.get(pathToFolder+"/house");
            String originalFilename = file.getOriginalFilename();
            String format = originalFilename.substring(originalFilename.lastIndexOf("."));
            nameImage = generateName() + format;
            Files.copy(file.getInputStream(), uploadPath.resolve(nameImage));
        } catch (Exception e) {
            log.warn(e);
        }
        return contextPath+"/uploads/house/"+nameImage;
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
    public static byte[] convertFileToBytes(String filePath) throws IOException {
        Path path = Paths.get(filePath.replace("/uploads", pathToFolder));
        return Files.readAllBytes(path);
    }
    public static void deleteFile(String name) {
        log.info(name.replace("/uploads", pathToFolder));
        new File(name.replace("/uploads", pathToFolder)).delete();
    }
}
