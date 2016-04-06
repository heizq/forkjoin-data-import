package cn.lamppa.edu.platform.dao;

import cn.lamppa.edu.platform.domain.ChapterSection;
import cn.lamppa.edu.platform.domain.ChapterSectionKnowledge;
import cn.lamppa.edu.platform.domain.QuestionJudge;

import java.util.List;

/**
 * Created by heizhiqiang on 2016/2/24 0024.
 */
public interface ChapterSectionDao {
    /**
     * add judge question
     * **/
    public String addChapterSection(ChapterSection model);

    public List<ChapterSection> findChapterSectionByTextbookAndParentId(ChapterSection model);

    public List<ChapterSectionKnowledge> findChapterSectionKnowledge(String chapterSectionId);

    public String addChapterSectionKnowledge(ChapterSectionKnowledge chapterSectionKnowledge);
}
