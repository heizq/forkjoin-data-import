package cn.lamppa.edu.platform.service.impl;

import cn.lamppa.edu.platform.monogdao.IMongoDao;
import cn.lamppa.edu.platform.service.ImageDataService;
import cn.lamppa.edu.platform.util.ThreadPool;
import com.mongodb.DB;
import com.mongodb.gridfs.GridFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * Created by liupd on 16-2-25.
 **/
@Configurable
@Service(value="imageDataService")
public class ImageDataServiceImpl implements ImageDataService {

    @Value("${img.save_info}")
    private String textfilePath;

    @Autowired
    private IMongoDao iMongoDao;


    @Override
    public void saveImageToMongo(List<File> files)throws Exception {
        iMongoDao.saveRes(files);
        //ThreadPool.execute(new ImgMongoThread(iMongoDao,files));
        //new ImgMongoThread(iMongoDao,files).start();
    }


}
