package cn.lamppa.edu.platform.service;

import java.io.File;
import java.util.List;

/**
 * Created by liupd on 16-2-25.
 **/
public interface ImageDataService {



    public void saveImageToMongo(List<File> files)throws Exception;

}
