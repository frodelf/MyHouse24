package com.avada.myHouse24.util;

import com.avada.myHouse24.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
}
