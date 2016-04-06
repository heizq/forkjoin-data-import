package cn.lamppa.edu.platform.service;

import cn.lamppa.edu.platform.domain.QuestionFilling;
import cn.lamppa.edu.platform.domain.QuestionShortanswer;

import java.util.List;

/**
 * Created by Administrator on 2016/2/25.
 **/
public interface QuestionShortanswerService {

    public List<QuestionShortanswer> findQuestionShortanswerById(String questionId);

    public void syncQuestion();

    public String syncItemQuestion(String questionId);

    public int getCount();

    public List<QuestionShortanswer> findShortanswerByPage(int start, int end);

    public String transferData(List<QuestionShortanswer> list);
}
