package com.dubbo.shop.controller;

import com.dubbo.shop.pojo.ResBean;
import com.dubbo.shop.pojo.wangEditorBean;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("file")
public class FastDFSDemo {  // 用于测试fastdfs

    @Value("${filePath.service}")   // 获取application.yml中的值
    private String base_url;

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
    public ResBean uplodify(@RequestParam("img") MultipartFile file) throws Exception{
        String file_name = file.getOriginalFilename();

        String extName = file_name.substring(file_name.lastIndexOf(".") + 1);

        InputStream inputStream = file.getInputStream();

        StorePath storePath = fastFileStorageClient.uploadFile(inputStream, file.getSize(), extName, null);

        String full_path = storePath.getFullPath();

        // StringBuilder线程安全
        String img_url = new StringBuilder(base_url).append(full_path).toString();
        System.out.println(img_url);

        return ResBean.success(img_url);
    }


    // 使用富文本编辑器上传多张图片
    @RequestMapping("wangEditor_uplodify")
    public wangEditorBean wangEditor_uplodify(@RequestParam("imgs") MultipartFile[] files) throws Exception{

        List<String> images = new ArrayList<String>();
        for(MultipartFile file: files){
            String img_url = uploadFileUtil(file);
            images.add(img_url);
        }

        return wangEditorBean.success(images);
    }

    public String uploadFileUtil(MultipartFile file) throws Exception{
        String file_name = file.getOriginalFilename();

        String extName = file_name.substring(file_name.lastIndexOf(".") + 1);

        InputStream inputStream = file.getInputStream();

        StorePath storePath = fastFileStorageClient.uploadFile(inputStream, file.getSize(), extName, null);

        String full_path = storePath.getFullPath();

        // StringBuilder线程安全
        String img_url = new StringBuilder(base_url).append(full_path).toString();
        System.out.println(img_url);

        return img_url;
    }


    @RequestMapping("delete")
    public String delete(String file_path){

        fastFileStorageClient.deleteFile(file_path);
        return null;
    }
}
