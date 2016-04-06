package cn.lamppa.edu.platform.service;

import cn.lamppa.edu.platform.domain.QuestionFilling;
import cn.lamppa.edu.platform.domain.QuestionJudge;

import java.util.List;
import java.util.Map;

/**
 * Created by heizhiqiang on 2016/2/24 0024.
 */
public interface QuestionJudgeService {

    public void syncQuestion();

    public String syncItemQuestion(String questionId);

    public List<QuestionJudge> findJudgeByPage(int start ,int end);

    public String  transferData(List<QuestionJudge> resList);

    public int  getCount();



}
