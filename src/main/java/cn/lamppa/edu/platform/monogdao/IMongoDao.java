package cn.lamppa.edu.platform.monogdao;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by Liupd on 16-2-25.
 **/
public interface IMongoDao {

    public void saveRes(List<File> fileList)throws Exception;

    public Map<String,Object> getSourceByFileId(String resFileId);

    public boolean removeRes(String fileId);

    public String getFileById(String resFileId);



}
