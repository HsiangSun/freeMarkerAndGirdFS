package cn.hsiangsun.controller;


import cn.hsiangsun.model.Student;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.*;

//@Controller //Do not use @RestController
@RestController
@RequestMapping("/")
public class FreeMarkerController {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    @GetMapping("/hello")
    public String  test(Map<String,Object> map){

        Student student1 = new Student();
        student1.setName("tom");
        student1.setAge(12);
        student1.setBirthday(new Date());
        student1.setMoney(20.00F);

        Student student2 = new Student();
        student2.setName("bob");
        student2.setAge(13);
        student2.setBirthday(new Date());
        student2.setMoney(30.00F);

        Student student3 = new Student();
        student3.setName("marry");
        student3.setAge(11);
        student3.setBirthday(new Date());
        student3.setMoney(10.00F);

        List friendsList =new ArrayList();
        friendsList.add(student1);
        friendsList.add(student2);

        student3.setFriends(friendsList);
        student3.setBestFriend(student2);

        Map<String,Student> map2 = new HashMap();
        map2.put("stu1",student1);
        map2.put("stu2",student2);

        map.put("name","hsiang sun");
        map.put("list",friendsList);
        map.put("stumap",map2);
        return "index";//This is template name(index.ftl) resources/template/<templateName>
    }

    @GetMapping("/store")
    public String store(){
        File file =new File("C:\\Users\\xy110\\Pictures\\girl00.jpg");
        FileInputStream ins = null;
        try {
            ins = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectId objectId = gridFsTemplate.store(ins, "girl");
        return   objectId.toString();
    }

    @GetMapping("/download")
    public String download() throws IOException {
        //根据ObjectId查询
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5d3acd3fec28a022bc747336")));
        //建立一个下载流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //获取流
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
        InputStream inputStream = gridFsResource.getInputStream();
        File outFile = new File("C:\\Users\\xy110\\Desktop\\mogo.jpg");
        byte[] buff =new byte[1024];
        int len = 0;
        OutputStream ous = new FileOutputStream(outFile);
        while ( (len= inputStream.read(buff)) != -1 ){
            ous.write(buff,0,len);
        }

        ous.close();
        inputStream.close();

        return "C:\\Users\\xy110\\Desktop\\mogo.jpg";

    }

}
