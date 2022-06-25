package com.tree.Controller;

import com.tree.Common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * @Auther: 来两碗米饭
 * @ClassName：CommonController
 * @Date: 2022/6/20 19:32
 * @Description TODO:
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${uploadPath.path}")
    private String uploadPath;
    private BufferedInputStream bis;


    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        log.info(file.toString());
        //原文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //判断目录是否存在
        File dir = new File(uploadPath);
        if (!dir.exists()){
            dir.mkdirs();
        }

        //新文件名
        String name = UUID.randomUUID() + suffix;

        try {
            file.transferTo(new File(uploadPath+name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(name);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        BufferedInputStream bis = null;
        ServletOutputStream outputStream = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(uploadPath + name));
            outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len=0;
            byte[] bytes = new byte[1024];
            while ( (len = bis.read(bytes)) != -1 ){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }



        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            if (bis!=null){
                try {
                    bis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
