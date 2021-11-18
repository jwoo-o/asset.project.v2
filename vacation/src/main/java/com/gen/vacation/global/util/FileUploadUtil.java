package com.gen.vacation.global.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Created by jinwoo.
 * User: jwoh
 * Date: 2020-05-11
 * Time: 오후 6:11
 */
@Component
public class FileUploadUtil {


    public String fileUpload(MultipartFile file, String path) throws Exception {

        File dir = new File(path);

        if (!dir.isDirectory()) {

            dir.mkdirs();

        }
        UUID uid = UUID.randomUUID();
        String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String name = System.currentTimeMillis() + "_" + uid+type;

        file.transferTo(new File(path + "/" + name));
        return name;

    }

    public String fileUpload(byte[] file, String path) throws Exception {

        File dir = new File(path);

        if (!dir.isDirectory()) {

            dir.mkdirs();

        }
        UUID uid = UUID.randomUUID();
        String type = ".png";
        String name = System.currentTimeMillis() + "_" + uid+type;

        try (FileOutputStream fileOutputStream = new FileOutputStream(path+"/"+name)) {
            fileOutputStream.write(file);
        }
        return name;

    }


    public void fileDelete(String fileName, String path) throws Exception {
        Files.delete(Paths.get(path + "/" + fileName));
    }
}
