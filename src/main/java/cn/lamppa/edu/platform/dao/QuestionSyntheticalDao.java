package cn.lamppa.edu.platform.dao;

import cn.lamppa.edu.platform.domain.QuestionSynthetical;
import cn.lamppa.edu.platform.domain.QuestionSyntheticalItem;

import java.util.List;

/**
 * Created by heizhiqiang on 2016/2/29 0029.
 */
public interface QuestionSyntheticalDao {

    public String  addQuestion(QuestionSynthetical model);

    public String  addItem(QuestionSyntheticalItem model);

    public List<QuestionSynthetical> findQuestionSynthetical();

    public List<QuestionSyntheticalItem> findItems(String questionId);

    public List<QuestionSynthetical> findQuestionByPage(int start ,int end);

    public int getCount();
}
