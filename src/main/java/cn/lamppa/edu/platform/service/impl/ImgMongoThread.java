package cn.lamppa.edu.platform.service.impl;

import cn.lamppa.edu.platform.monogdao.IMongoDao;
import cn.lamppa.edu.platform.service.ImageDataService;
import cn.lamppa.edu.platform.util.ImgFileUtil;
import com.mongodb.DB;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by liupd on 16-2-27.
 **/

public class ImgMongoThread extends Thread{

    private static final Logger log = LoggerFactory.getLogger(ImgMongoThread.class);

    private IMongoDao iMongoDao;

    private List<File> files;


    public ImgMongoThread(IMongoDao iMongoDao,List<File> files) {
        this.iMongoDao = iMongoDao;
        this.files=files;
    }


    @Override
    public void run() {
        try{
            if(null!=files&&files.size()>0){
                iMongoDao.saveRes(files);
            }
        }catch (Exception e){
            log.info("mongo import file exception:"+(e.getMessage()));
            e.getStackTrace();
        }

    }
}
