package cn.lamppa.edu.platform.dao;

import cn.lamppa.edu.platform.domain.QuestionCategory;

import java.util.List;

/**
 * Created by heizhiqiang on 2016/2/25 0025.
 */
public interface QuestionCategoryDao {
    public int[] addQuestionCategory(List<QuestionCategory> list);

    public List<QuestionCategory>  findQuestionCategoryByQuestionId(String questionId);
}
