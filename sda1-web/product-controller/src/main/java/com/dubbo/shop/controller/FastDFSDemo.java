package com.dubbo.shop.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("test")
public class FastDFSDemo {  // 用于测试fastdfs

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @GetMapping("uploadFile")
    private Map<String, String> uploadFile() throws Exception{

        String o_file_path = "C:\\Users\\Administrator\\Desktop\\4da4fa5d-ee2d-4496-9950-e53b102f0e8e.jpg";

        File file = new File(o_file_path);

        String fileName = file.getName();

        // 后缀名
        String extName = fileName.substring(fileName.lastIndexOf(".") + 1);

        FileInputStream fileInputStream = new FileInputStream(file);

        // 上传文件： 输入流，文件大小，后缀名，null
        // 返回存储路径
        StorePath storePath = fastFileStorageClient.uploadFile(fileInputStream, file.length(), extName, null);

        storePath.getFullPath();

        Map<String, String> res_map = new HashMap();

        res_map.put("group", storePath.getGroup());
        res_map.put("path", storePath.getPath());
        res_map.put("fullPath", storePath.getFullPath());

        return res_map;

        // 返回结果如下
        //        {
        //            "fullPath":"group1/M00/00/00/wKjOh18YbTOAagtXAACNALdDTW4334.jpg",
        //            "path":"M00/00/00/wKjOh18YbTOAagtXAACNALdDTW4334.jpg",
        //            "group":"group1"
        //        }
        //       访问 http://192.168.206.135/group1/M00/00/00/wKjOh18YbTOAagtXAACNALdDTW4334.jpg  可以得到图片
    }

    @RequestMapping("uplodify")
    public String uplodify(@RequestParam("img") MultipartFile file){
        System.out.println(file);
        return "success ~~~";
    }
}
