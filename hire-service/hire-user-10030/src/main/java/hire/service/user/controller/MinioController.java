package hire.service.user.controller;

import com.hire.common.base.response.ResponseData;
import hire.service.user.utils.MinioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user")
public class MinioController {
    @Autowired
    private MinioUtils minioUtils;

    @Value("${minio.endpoint}")
    private String address;

    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * minio分布式存储图片
     * @param files 图片
     * @return 返回图片地址
     */
    @PostMapping("/upload")
    public ResponseData<List<String>> upload(@RequestParam("file") MultipartFile[] files) {

        List<String> upload = minioUtils.upload(files);

        return ResponseData.Success(upload);
    }

}


