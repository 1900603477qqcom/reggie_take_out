package com.itheima.reegie.controller;

import com.itheima.reegie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
//    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传和下载
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
    //file是一个临时文件，需要转存到指定位置，否则完毕后临时文件会被删除
        log.info(file.toString());
    //1、获取文件原始名称
    String originalFilename = file.getOriginalFilename();//获取当前文件的名称
    //截取字符串，获取图片后缀名
    String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
    //3、随机生成一个不可重复名称 uuid生成 防止重名覆盖文件
    String uuid = UUID.randomUUID().toString();//获取随机不可重复字符串，作为转存图片的名字
    // 4. 拼接新的图片名称
    String fileName = uuid+suffix;
    //5. 创建一个目录对象，指定存储图片的位置
    File dir = new File(basePath);
    //6. 判断当前文件路径是否存在，如果不存在，创建这个路径文件
    if(!dir.exists()) dir.mkdir();
    //7. 将临时文件转存到指定位置
    try {
        file.transferTo(new File(basePath+fileName));
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
      return R.success(fileName);
    }
    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name,  HttpServletResponse response){
       try {
           //创建入流对象，通过输入读取文件内容
           FileInputStream fileInputStream = new FileInputStream(basePath + name);
           //创建输出流对象，通过流将文件写回到浏览器
           ServletOutputStream outputStream = response.getOutputStream();
           //设置写回浏览器文件格式
           response.setContentType("image/jpeg");//表示写入的格式为图片
           int len = 0;//标注我们每次读取了多少字节，如果是0，则表示没有读取到任何字符
           byte[] bytes = new byte[1024]; //规定每次最多可以读取1024个字节
           while ((len = fileInputStream.read(bytes)) > 0) {
               outputStream.write(bytes, 0, len);
               outputStream.flush();//方法用于刷新输出流的缓冲区，将缓冲区中的数据立即写入目标设备，以确保数据的及时性
           }
           //关闭流资源
           outputStream.close();
           fileInputStream.close();
       }catch (FileNotFoundException e){
           throw new RuntimeException(e);
       }catch (IOException e){
           throw new RuntimeException(e);
       }
    }
}
