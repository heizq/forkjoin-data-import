package cn.lamppa.edu.platform.service;


import cn.lamppa.edu.platform.domain.QuestionFilling;

import java.util.List;

/**
 * Created by heizhiqiang on 2016/2/24 0024.
 */
public interface QuestionFillingService {

    public void syncQuestion();

    public String syncItemQuestion(String questionId);

    public String transferData(List<QuestionFilling> resList);

    public String transferItemData(List<QuestionFilling> resList);

    public int  getCount();

    public List<QuestionFilling> findQuestionByPage(int start ,int end);

    public List<QuestionFilling> findAll();


}
