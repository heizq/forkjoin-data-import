package cn.lamppa.edu.platform.dao;

import cn.lamppa.edu.platform.domain.QuestionTestPoint;

import java.util.List;

/**
 * Created by heizhiqiang on 2016/2/25 0025.
 */
public interface QuestionTestPointDao {

    public int[] addQuestionTestPoint(List<QuestionTestPoint> list);
    public List<QuestionTestPoint> findQuestionTestPoints(String id);
}
