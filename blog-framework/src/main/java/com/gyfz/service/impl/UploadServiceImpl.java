package com.gyfz.service.impl;

import com.google.gson.Gson;
import com.gyfz.domain.ResponseResult;
import com.gyfz.enums.AppHttpCodeEnum;
import com.gyfz.exception.SystemException;
import com.gyfz.service.UploadService;
import com.gyfz.utils.PathUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
@Service
@Data
@ConfigurationProperties(prefix = "oss")
public class UploadServiceImpl implements UploadService {

    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        String originalFilename = img.getOriginalFilename();
        if (!originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpg")){
            throw  new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }

        String url = uploadOss(img, PathUtils.generateFilePath(originalFilename));
        if (url.equals("www")){
            throw  new SystemException(AppHttpCodeEnum.UPLOAD_ERROR);
        }
        return ResponseResult.okResult(url);
    }

    private String accessKey;
    private String secretKey;
    private String bucket;
    private String uploadOss(MultipartFile imgFile, String filePath){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;
        try {
            InputStream inputStream = imgFile.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return "http://rhh3gt6j1.hd-bkt.clouddn.com/"+key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return "www";
    }
}
