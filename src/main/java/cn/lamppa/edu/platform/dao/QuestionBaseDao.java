package cn.lamppa.edu.platform.dao;

import cn.lamppa.edu.platform.domain.QuestionBase;

/**
 * Created by heizhiqiang on 2016/2/24 0024.
 */
public interface QuestionBaseDao {
    /**
     * add Question's QuestionBase
     * @param model
     * @return
     */
    public int addQuestionBase(QuestionBase model);

    public QuestionBase findQuestionBase(String id);
}
