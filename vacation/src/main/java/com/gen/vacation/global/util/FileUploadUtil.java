package com.gen.vacation.global.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
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

        new FileOutputStream(path+"/"+name).write(file);
        return name;

    }


    public void fileDelete(String fileName, String path) throws Exception {

        File file = new File(path + "/" + fileName);

        if (file.exists()) {
            file.delete();
        } else {
            throw new IllegalArgumentException();
        }


    }

    public Map<String, Object> approvalFileUpload(MultipartFile file, String path) throws Exception {

        Map<String, Object> result = new HashMap<String, Object>();
        File dir = new File(path);

        if (!dir.isDirectory()) {

            dir.mkdirs();

        }
        UUID uid = UUID.randomUUID();
        /*String fileName = file.getOriginalFilename();*/
        String name = System.currentTimeMillis() + "_" + uid; /*+ fileName.substring(fileName.indexOf("."));*/
        file.transferTo(new File(path + "/" + name));


        // String hash = getHash(path + "/" + name);
        result.put("name", name);
        // result.put("hash", hash);

        return result;

    }


    public String getHash(String path) throws IOException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        FileInputStream fileInputStream = new FileInputStream(path);
        byte[] dataBytes = new byte[1024];
        Integer nRead = 0;
        while ((nRead = fileInputStream.read(dataBytes)) != -1) {
            messageDigest.update(dataBytes, 0, nRead);
        }
        byte[] mdBytes = messageDigest.digest();
        StringBuffer stringBuffer = new StringBuffer();
        for (Integer i = 0; i < mdBytes.length; i++) {
            stringBuffer.append(Integer.toString((mdBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuffer.toString();
    }

}
