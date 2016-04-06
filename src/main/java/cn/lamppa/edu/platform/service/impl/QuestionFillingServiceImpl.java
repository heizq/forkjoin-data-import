package cn.lamppa.edu.platform.service.impl;

import cn.lamppa.edu.platform.dao.*;
import cn.lamppa.edu.platform.domain.*;
import cn.lamppa.edu.platform.enums.QuestionAuditStatus;
import cn.lamppa.edu.platform.enums.QuestionStatus;
import cn.lamppa.edu.platform.enums.QuestionType;
import cn.lamppa.edu.platform.service.QuestionFillingService;
import cn.lamppa.edu.platform.util.Constants;
import cn.lamppa.edu.platform.util.IdSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by heizhiqiang on 2016/3/28 0028.
 */

@Service
public class QuestionFillingServiceImpl implements QuestionFillingService {

    private static Logger logger = LoggerFactory.getLogger(QuestionFillingServiceImpl.class);

    @Resource
    private QuestionBaseDao questionBaseDao;

    @Resource
    private QuestionKnowledgeDao questionKnowledgeDao;

    @Resource
    private QuestionTestPointDao questionTestPointDao;

    @Resource
    private QuestionCategoryDao questionCategoryDao;

    @Resource
    private QuestionFillingDao questionFillingDao;

    @Resource
    private CommonDao commonDao;


    public void syncQuestion() {
        Long start = System.currentTimeMillis();
        logger.info(" transfer filling question start.....");
        List<QuestionFilling> resList = questionFillingDao.findQuestionFilling(null,false);

        transferData(resList);
        logger.info(" transfer filling question  end.......");
        logger.info ("transfer filling question  use {} minutes",(System.currentTimeMillis() - start)/(60*1000));
    }

    public String syncItemQuestion(String questionId) {
        logger.info("start transfer filling item  start");
        List<QuestionFilling> list = questionFillingDao.findQuestionFilling(questionId,true);

        String tId = "";
        if(list != null && list.size() > 0){
            tId = transferItemData(list);
        }else {
            logger.error("Synthetical item filling id={} not found",questionId);
        }
        return tId;
    }

    public String transferData(List<QuestionFilling> resList)  {
        String errerId = "";
        int successCount = 0;
        try{
            for(QuestionFilling res:resList){
                errerId = res.getId();
                String tId = IdSequence.nextId();
                logger.info("start filling quesiton NO={} to new Id={}",res.getId(),tId);

                QuestionBase base = questionBaseDao.findQuestionBase(res.getId());
                QuestionKnowledge major = questionKnowledgeDao.findMajorKnowledge(res.getId());
                List<QuestionKnowledge> minor = questionKnowledgeDao.findMinorKnowledges(res.getId());
                List<QuestionTestPoint> testPoints = questionTestPointDao.findQuestionTestPoints(res.getId());
                List<QuestionCategory> categories = questionCategoryDao.findQuestionCategoryByQuestionId(res.getId());
                List<QuestionFillingAnswer> answers = questionFillingDao.findQuestionFillingAnswer(res.getId());

                if(base == null){
                    logger.error("error on question No={} 缺少 base 信息",errerId);
                }else{
                    base.setId(tId);
                    base.setType(QuestionType.FILLING.toString());
                    questionBaseDao.addQuestionBase(base);
                }

                if(major == null){
                    logger.error("error on question No={} 缺少 knowledge 信息",errerId);
                }else{
                    major.setId(IdSequence.nextId());
                    major.setQuestionId(tId);
                    major.setType(QuestionType.FILLING.toString());
                    major.setKnowledgeId(commonDao.getKnowledgeId(major.getKnowledgeId()));
                    major.setCognizeLevelId(commonDao.getCognizeLevel(major.getCognizeLevelId()));
                    major.setAbilityId(commonDao.getAbliity(major.getAbilityId()));
                    questionKnowledgeDao.addQuestionKnowledge(major);
                }

                for(QuestionKnowledge v:minor){
                    v.setId(IdSequence.nextId());
                    v.setQuestionId(tId);
                    v.setType(QuestionType.FILLING.toString());
                    v.setKnowledgeId(commonDao.getKnowledgeId(v.getKnowledgeId()));
                    questionKnowledgeDao.addQuestionKnowledge(v);
                }

                //            for(QuestionTestPoint v: testPoints){
                //                v.setId(IdSequence.nextId());
                //                v.setQuestionId(tId);
                //                v.setQuestionType(QuestionType.FILLING.toString());
                //                //v.setKnowledgeId(commonDao.getKnowledgeId(v.getKnowledgeId()));
                //            }
                //            questionTestPointDao.addQuestionTestPoint(testPoints);

                for(QuestionCategory v:categories){
                    v.setId(IdSequence.nextId());
                    v.setQuestionId(tId);
                }
                questionCategoryDao.addQuestionCategory(categories);

                if(answers == null){
                    logger.error("error on question No={} 缺少 answer 信息",errerId);
                }else{
                    for(QuestionFillingAnswer v: answers){
                        v.setId(IdSequence.nextId());
                        v.setFillingId(tId);
                        questionFillingDao.addQuestionAnswer(v);
                    }
                }


                res.setId(tId);
                res.setBaseId(tId);
                res.setStatus(QuestionStatus.SUBMITED.toString());
                res.setAuditStatus(QuestionAuditStatus.APPROVED.toString());

                res.setCreateTime(new Date());
                res.setCreateUserId(Constants.user_key);
                res.setCreateUserName(Constants.user_name);

                questionFillingDao.addQuestion(res);

                commonDao.insertMiddleTable(tId);

                logger.info("end filling quesiton NO={} ",res.getId()+" ");
                successCount++;
            }
        }catch (Exception e){
            logger.error("error on question No={} ",errerId);
            logger.error(e.getMessage());

        }
        return String.valueOf(successCount);
    }

    public String transferItemData(List<QuestionFilling> resList){
        try{
            QuestionFilling res = resList.get(0);
            String tId = IdSequence.nextId();

            List<QuestionFillingAnswer> answers = questionFillingDao.findQuestionFillingAnswer(res.getId());

            for(QuestionFillingAnswer v: answers){
                v.setId(IdSequence.nextId());
                v.setFillingId(tId);
                questionFillingDao.addQuestionAnswer(v);
            }

            res.setId(tId);
            res.setBaseId(tId);
            res.setStatus(QuestionStatus.SUBMITED.toString());
            res.setAuditStatus(QuestionAuditStatus.APPROVED.toString());

            res.setCreateTime(new Date());
            res.setCreateUserId(Constants.user_key);
            res.setCreateUserName(Constants.user_name);

            questionFillingDao.addQuestion(res);
            return tId;

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return "";
    }

    public int getCount() {
        return questionFillingDao.getCount();
    }

    @Override
    public List<QuestionFilling> findQuestionByPage(int start, int end) {
        return questionFillingDao.findQuestionByPage(start, end);
    }

    @Override
    public List<QuestionFilling> findAll() {
        return questionFillingDao.findQuestionFilling(null,false);
    }
}