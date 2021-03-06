package cn.lamppa.edu.platform.dao;

import cn.lamppa.edu.platform.domain.QuestionFilling;
import cn.lamppa.edu.platform.domain.QuestionJudge;

import java.util.List;

/**
 * Created by heizhiqiang on 2016/2/24 0024.
 */
public interface QuestionJudgeDao {
    /**
     * add judge question
     * **/
    public int addQuestion(QuestionJudge model);
    /**
     * find single QuestionJudge by id
     * @return
     */
    public List<QuestionJudge> findQuestionJudges(String questionId,Boolean  is_small );

    public List<QuestionJudge> findJudgeByPage(int start ,int end);

    public int count();
}
