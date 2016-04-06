package cn.lamppa.edu.platform.dao;

import cn.lamppa.edu.platform.domain.QuestionShortanswer;

import java.util.List;

/**
 * Created by Administrator on 2016/2/25.
 */
public interface QuestionShortanswerDao {

    public String addQuestionShortanswer(QuestionShortanswer questionShortanswer);

    public List<QuestionShortanswer> findQuestionShortanswerById(String questionId);

    public List<QuestionShortanswer> findQuestionShortanswer(String questionId,Boolean isSmall);

    public int getCount();

    public List<QuestionShortanswer> findShortanswerByPage(int start, int end);
}
