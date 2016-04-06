package cn.lamppa.edu.platform.service.impl;

import cn.lamppa.edu.platform.dao.*;
import cn.lamppa.edu.platform.domain.*;
import cn.lamppa.edu.platform.enums.QuestionAuditStatus;
import cn.lamppa.edu.platform.enums.QuestionStatus;
import cn.lamppa.edu.platform.enums.QuestionType;
import cn.lamppa.edu.platform.service.QuestionJudgeService;
import cn.lamppa.edu.platform.util.Constants;
import cn.lamppa.edu.platform.util.IdSequence;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/27.
 */
@Service
public class QuestionJudgeServiceImpl implements QuestionJudgeService {
    private static Logger logger = LoggerFactory.getLogger(QuestionJudgeServiceImpl.class);
    @Resource
    private QuestionBaseDao questionBaseDao;
    @Resource
    private QuestionJudgeDao questionJudgeDao;
    @Resource
    private QuestionKnowledgeDao questionKnowledgeDao;
    @Resource
    private QuestionTestPointDao questionTestPointDao;
    @Resource
    private QuestionCategoryDao questionCategoryDao;
    @Resource
    private CommonDao commonDao;

    public void syncQuestion() {
        Long starts = System.currentTimeMillis();
        List<QuestionJudge> resList = questionJudgeDao.findQuestionJudges(null, false);
        transferData(resList);
        logger.info ("transfer judge question  use {} minute",(System.currentTimeMillis() - starts)/(60*1000));
    }

    public String syncItemQuestion(String questionId) {
        logger.info("start transfer judge item  start");
        List<QuestionJudge> list = questionJudgeDao.findQuestionJudges(questionId,true);
        String tId = "";
        if(list != null && list.size() > 0){
            tId = transferItemData(list);
        }else {
            logger.error("Synthetical item judge id={} not found",questionId);
        }
        return tId;
    }

    @Override
    public List<QuestionJudge> findJudgeByPage(int start, int end) {
        return questionJudgeDao.findJudgeByPage(start, end);
    }

    public String  transferData(List<QuestionJudge> resList){
        String errerId = "";
        int successcount=0;
        try{
            for(QuestionJudge res:resList){
                errerId = res.getId();
                String tId = IdSequence.nextId();
                logger.info("start judge quesiton NO={} to new Id={}",res.getId(),tId);
                QuestionBase base = questionBaseDao.findQuestionBase(res.getBaseId());
                QuestionKnowledge major = questionKnowledgeDao.findMajorKnowledge(res.getId());
                List<QuestionKnowledge> minor = questionKnowledgeDao.findMinorKnowledges(res.getId());
                //List<QuestionTestPoint> testPoints = questionTestPointDao.findQuestionTestPoints(res.getId());
                List<QuestionCategory> categories = questionCategoryDao.findQuestionCategoryByQuestionId(res.getId());

                if(base == null){
                    logger.error("error on question No={} 缺少 base 信息",errerId);
                }else{
                    base.setId(tId);
                    base.setType(QuestionType.JUDGE.toString());
                    questionBaseDao.addQuestionBase(base);
                }

                if(major == null){
                    logger.error("error on question No={} 缺少 knowledge 信息",errerId);
                }else{
                    major.setId(IdSequence.nextId());
                    major.setQuestionId(tId);
                    major.setType(QuestionType.JUDGE.toString());
                    major.setKnowledgeId(commonDao.getKnowledgeId(major.getKnowledgeId()));
                    major.setCognizeLevelId(commonDao.getCognizeLevel(major.getCognizeLevelId()));
                    major.setAbilityId(commonDao.getAbliity(major.getAbilityId()));
                    if(StringUtils.isEmpty(major.getKnowledgeId()) || StringUtils.isEmpty(major.getCognizeLevelId()) || StringUtils.isEmpty(major.getAbilityId())){
                        logger.info("import knowledge quesiton NO={}   end ....",res.getId());
                    }
                    questionKnowledgeDao.addQuestionKnowledge(major);
                }


                for(QuestionKnowledge v:minor){
                    v.setId(IdSequence.nextId());
                    v.setQuestionId(tId);
                    v.setType(QuestionType.JUDGE.toString());
                    v.setKnowledgeId(commonDao.getKnowledgeId(v.getKnowledgeId()));
                    questionKnowledgeDao.addQuestionKnowledge(v);
                }

                //            for(QuestionTestPoint v: testPoints){
                //                v.setId(IdSequence.nextId());
                //                v.setQuestionId(tId);
                //                v.setQuestionType(QuestionType.JUDGE.toString());
                //                //v.setKnowledgeId();
                //            }
                //            questionTestPointDao.addQuestionTestPoint(testPoints);

                for(QuestionCategory v:categories){
                    v.setId(IdSequence.nextId());
                    v.setQuestionId(tId);
                }
                questionCategoryDao.addQuestionCategory(categories);
                res.setId(tId);
                res.setBaseId(tId);
                res.setStatus(QuestionStatus.SUBMITED.toString());
                res.setAuditStatus(QuestionAuditStatus.APPROVED.toString());

                res.setCreateTime(new Date());
                res.setCreateUserId(Constants.user_key);
                res.setCreateUserName(Constants.user_name);

                questionJudgeDao.addQuestion(res);
                commonDao.insertMiddleTable(tId);
                successcount++;
            }
        }catch (Exception e){
            logger.error("run question No={} errer",errerId);
            logger.error(e.getMessage());
        }
        return String.valueOf(successcount);
    }

    @Override
    public int getCount() {
       return questionJudgeDao.count();
    }

    private String transferItemData(List<QuestionJudge> resList){
        try{
            QuestionJudge res = resList.get(0);
            String tId = IdSequence.nextId();
            res.setId(tId);
            res.setBaseId(tId);
            res.setStatus(QuestionStatus.SUBMITED.toString());
            res.setAuditStatus(QuestionAuditStatus.APPROVED.toString());

            res.setCreateTime(new Date());
            res.setCreateUserId(Constants.user_key);
            res.setCreateUserName(Constants.user_name);

            questionJudgeDao.addQuestion(res);
            return tId;

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return "";
    }

}
