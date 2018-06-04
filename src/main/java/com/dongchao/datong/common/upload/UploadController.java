package com.dongchao.datong.common.upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

@RestController
public class UploadController {
    @Value("${spring.http.multipart.location}")
    private String uploadPath;

    @PostMapping(value = {"/upload/single"})
    public String uploadingFile(@RequestParam("file") MultipartFile file) {
        // 获取上传文件的路径
        writeFile(file,uploadPath);
        return "上传成功！文件：" + file.getOriginalFilename();
    }

    @PostMapping(value = "/upload/multi")
    public String uploadFiles(MultipartHttpServletRequest multipartHttpServletRequest){
        List<MultipartFile> fileList = multipartHttpServletRequest.getFiles("file");
        for (MultipartFile file : fileList){
            if (!file.isEmpty()){
                writeFile(file,uploadPath);
            }
        }
        return "多文件上传成功！" ;
    }
    public String downloadFile() {

        return null;
    }

    private String parseFileName(String filePathName) {
        return filePathName.substring(filePathName.lastIndexOf('\\') + 1, filePathName.length());
    }

    private void writeFile(MultipartFile file,String destination){
        String uploadFile = parseFileName(file.getOriginalFilename());
        System.out.println("uploadFlePath:" + file.getOriginalFilename());


        System.out.println("------------->" + destination);
        FileChannel readChannel = null;
        FileChannel writeChannel = null;
        try {
            FileInputStream fileInputStream = (FileInputStream) file.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(new File(destination + "/" + uploadFile));

            readChannel = fileInputStream.getChannel();
            writeChannel = fileOutputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);//创建一块缓存,大小为1024个字节

            int readLength;
            while (true) {
                byteBuffer.clear(); //清空缓存
                readLength = readChannel.read(byteBuffer); //The number of bytes read, possibly zero, or -1 if the channel has reached end-of-stream

                if (readLength == -1) {
                    break;
                }
                /**
                 * Flips this buffer.
                 * The limit is set to the current position
                 * and then the position is set to zero.
                 * If the mark is defined then it is discarded.
                 * After a sequence of channel-read or put operations, invoke this method to prepare for a sequence of channel-write or relative get operations
                 */
                byteBuffer.flip();
                writeChannel.write(byteBuffer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != readChannel) {
                try {
                    readChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != writeChannel) {
                try {
                    writeChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
