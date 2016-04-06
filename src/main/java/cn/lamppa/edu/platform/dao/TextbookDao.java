package cn.lamppa.edu.platform.dao;

import cn.lamppa.edu.platform.domain.Textbook;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/25.
 */
public interface TextbookDao {
    public String addTextbook(Textbook textbook);

    public List<Textbook> getTextBook();
}
