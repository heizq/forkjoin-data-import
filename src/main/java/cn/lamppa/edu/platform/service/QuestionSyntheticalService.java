package cn.lamppa.edu.platform.service;

import cn.lamppa.edu.platform.domain.QuestionSynthetical;

import java.util.List;
import java.util.Map;

/**
 * Created by heizhiqiang on 2016/3/1 0001.
 */
public interface QuestionSyntheticalService {
    public void syncQuestion();

    public String transferData(List<QuestionSynthetical> resList);

    public int  getCount();

    public List<QuestionSynthetical> findQuestionByPage(int start ,int end);
}
