package com.zw.stm.controller;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.zw.stm.common.pojo.PicUploadResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Objects;

@Controller
public class PicUploadController {
    @Value("http://192.168.25.130:8888/")
    private String IMAGE_SERVER_URL;
    private static String[] TYPE = { ".jpg", ".jpeg", ".png", ".bmp", ".gif" };

    @Resource
    private FastFileStorageClient storageClient ;

    // filePostName : "uploadFile", //上传文件名
    // uploadJson : '/rest/pic/upload', //图片上传请求路径
    // dir : "image" //上传文件类型
    @RequestMapping(value="pic/upload",method = RequestMethod.POST)
    @ResponseBody
    public PicUploadResult upload(MultipartFile uploadFile) throws Exception {
        // 声明标志位
        boolean flag = false;

        // 初始化返回数据,初始化上传失败
        PicUploadResult picUploadResult = new PicUploadResult();
        picUploadResult.setError(1);

        // 校验后缀
        for (String type : TYPE) {
            String oname = uploadFile.getOriginalFilename();
            // 如果后缀是要求的格式结尾，标志位设置为true，跳出寻汗
            if (StringUtils.endsWithIgnoreCase(oname, type)) {
                flag = true;
                break;
            }
        }

        // 如果校验失败，直接返回
        if (!flag) {
            return picUploadResult;
        }

        // 校验成功，需要上传图片
        String originalFilename = Objects.requireNonNull(uploadFile.getOriginalFilename()).
                substring(uploadFile.getOriginalFilename().
                        lastIndexOf(".") + 1);
        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(
                uploadFile.getInputStream(),
                uploadFile.getSize(),originalFilename , null);

        // 7. 进行返回的结果的拼接，上传图片的url
        String picUrl = this.IMAGE_SERVER_URL + storePath.getFullPath();

        // 设置图片url
        picUploadResult.setUrl(picUrl);

        // 上传成功设置为0
        picUploadResult.setError(0);


        return picUploadResult;
    }
}
