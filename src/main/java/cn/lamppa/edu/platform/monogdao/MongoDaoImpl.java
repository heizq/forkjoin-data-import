package cn.lamppa.edu.platform.monogdao;

import cn.lamppa.edu.platform.util.ImgFileUtil;
import com.mongodb.*;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import javax.xml.ws.Action;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liupd on 16-2-25.
 **/
@Repository
public class MongoDaoImpl  implements IMongoDao{

    private static final Logger log = LoggerFactory.getLogger(MongoDaoImpl.class);

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void saveRes(List<File> fileList)throws Exception{
        try {
            for(File resource:fileList){
                log.info("开始导入图片"+(resource.getPath()));
                DB db = mongoTemplate.getDb();
                GridFS fss = new GridFS(db);
                GridFSInputFile inputFile = fss.createFile(resource);
                inputFile.setFilename(resource.getName());
                inputFile.save();
                GridFSDBFile gfsFile = fss.find(new ObjectId(inputFile.getId().toString()));
                String info="{mongoId:"+gfsFile.get("_id")+"  "+"filepath:"+resource.getPath()+"  "+"filename:"+gfsFile.get("filename")+"  "+"contentType:"+gfsFile.get("contentType")+"}"+"\n";
                ImgFileUtil.writeToSqlFile(info);
                log.info("图片"+(resource.getPath())+"导入成功");
            }
        } catch (Exception e) {
            log.info("MongoDaoImpl saveRes:"+(e.getMessage()));
            e.printStackTrace();
        }
    }

    @Override
    public Map<String,Object> getSourceByFileId(String resFileId) {
        DBCollection dbc= mongoTemplate.getCollection("fs");
        DB db=dbc.getDB();
        GridFS myFS = new GridFS(db);
        GridFSDBFile gfsFile = myFS.find(new ObjectId(resFileId));
        Map<String,Object> map=new HashMap<String,Object>();
        if(null!=gfsFile){
            map.put("contentType",gfsFile.get("contentType"));
            map.put("fileName",gfsFile.get("filename"));
            return map;
        }
        return null;
    }


    @Override
    public boolean removeRes(String fileId) {
        boolean succ = false;
        try{
            DB db = mongoTemplate.getDb();
            GridFS fs = new GridFS(db);
            ObjectId oid = new ObjectId(fileId);
            fs.remove(oid);
            succ = true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return succ;
    }

    @Override
    public String getFileById(String resFileId) {
        DBCollection dbc= mongoTemplate.getCollection("fs");
        DB db=dbc.getDB();
        GridFS myFS = new GridFS(db);
        GridFSDBFile gfsFile = myFS.find(new ObjectId(resFileId));
        if(null!=gfsFile)
        {
            return String.valueOf(gfsFile.getId());
        }
        return null;
    }


    private String getContentType(String pathStr)throws IOException{
        Path path = Paths.get(pathStr);
        return Files.probeContentType(path);
    }

}
