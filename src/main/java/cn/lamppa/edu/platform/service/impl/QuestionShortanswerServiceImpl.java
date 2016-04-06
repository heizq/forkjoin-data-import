package cn.lamppa.edu.platform.service.impl;

import cn.lamppa.edu.platform.dao.*;
import cn.lamppa.edu.platform.domain.*;
import cn.lamppa.edu.platform.enums.QuestionAuditStatus;
import cn.lamppa.edu.platform.enums.QuestionStatus;
import cn.lamppa.edu.platform.enums.QuestionType;
import cn.lamppa.edu.platform.service.QuestionShortanswerService;
import cn.lamppa.edu.platform.util.Constants;
import cn.lamppa.edu.platform.util.IdSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/2/25.
 **/
@Service
public class QuestionShortanswerServiceImpl implements QuestionShortanswerService {

    private static Logger logger = LoggerFactory.getLogger(QuestionJudgeServiceImpl.class);

    @Resource
    private QuestionShortanswerDao questionShortanswerDao;

    @Resource
    private QuestionBaseDao questionBaseDao;

    @Resource
    private QuestionKnowledgeDao questionKnowledgeDao;

    @Resource
    private QuestionTestPointDao questionTestPointDao;

    @Resource
    private QuestionCategoryDao questionCategoryDao;

    @Resource
    private CommonDao commonDao;


    public void syncQuestion() {
        Long start = System.currentTimeMillis();
        logger.info("start transfer Shortanswer question start");
        List<QuestionShortanswer> resList = questionShortanswerDao.findQuestionShortanswer(null,false);

        transferData(resList);
        logger.info("end transfer Shortanswer question  ");
        logger.info ("transfer Shortanswer question  use {} minutes",(System.currentTimeMillis() - start)/(60*1000));
    }



    public List<QuestionShortanswer> findQuestionShortanswerById(String questionId){
       return  questionShortanswerDao.findQuestionShortanswerById(questionId);
    }


    public String syncItemQuestion(String questionId) {
        logger.info("start transfer Shortanswer item ");
        List<QuestionShortanswer> list = questionShortanswerDao.findQuestionShortanswer(questionId,true);

        String tId = "";
        if(list != null && list.size() > 0){
            tId = transferItemData(list);
        }else {
            logger.error("Synthetical item shortanswer id={} not found",questionId);
        }
        return tId;
    }

    @Override
    public int getCount() {
        return questionShortanswerDao.getCount();
    }

    @Override
    public List<QuestionShortanswer> findShortanswerByPage(int start, int end) {
        return  questionShortanswerDao.findShortanswerByPage(start,end);
    }


    public  String transferData(List<QuestionShortanswer> resList){
        int successcount=0;
        String errerId = "";
        try{
            for(QuestionShortanswer res:resList){
                errerId = res.getId();
                String tId = IdSequence.nextId();
                logger.info("start shortanswer quesiton NO={} to new Id={}",res.getId(),tId);
                QuestionBase base = questionBaseDao.findQuestionBase(res.getId());
                QuestionKnowledge major = questionKnowledgeDao.findMajorKnowledge(res.getId());
                List<QuestionKnowledge> minor = questionKnowledgeDao.findMinorKnowledges(res.getId());
                List<QuestionTestPoint> testPoints = questionTestPointDao.findQuestionTestPoints(res.getId());
                List<QuestionCategory> categories = questionCategoryDao.findQuestionCategoryByQuestionId(res.getId());
                List<QuestionShortanswer> answers = questionShortanswerDao.findQuestionShortanswerById(res.getId());

                if(base == null){
                    logger.error("error on question No={} 缺少 base 信息",errerId);
                }else{
                   base.setId(tId);
                   base.setType(QuestionType.SHORTANSWER.toString());
                   questionBaseDao.addQuestionBase(base);
               }
                if(major == null){
                    logger.error("error on question No={} 缺少 knowledge 信息",errerId);
                }else{
                    major.setId(IdSequence.nextId());
                    major.setQuestionId(tId);
                    major.setType(QuestionType.SHORTANSWER.toString());
                    major.setCognizeLevelId(commonDao.getCognizeLevel(major.getCognizeLevelId()));
                    major.setAbilityId(commonDao.getAbliity(major.getAbilityId()));
                    major.setKnowledgeId(commonDao.getKnowledgeId(major.getKnowledgeId()));
                    questionKnowledgeDao.addQuestionKnowledge(major);

                }

                for(QuestionKnowledge v:minor){
                    v.setId(IdSequence.nextId());
                    v.setQuestionId(tId);
                    v.setType(QuestionType.SHORTANSWER.toString());
                    v.setKnowledgeId(commonDao.getKnowledgeId(v.getKnowledgeId()));
                    questionKnowledgeDao.addQuestionKnowledge(v);
                }

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

                questionShortanswerDao.addQuestionShortanswer(res);

                commonDao.insertMiddleTable(tId);
                successcount++;
            }
        }catch (Exception e){
            logger.error("run question No={} errer",errerId);
            logger.error(e.getMessage());
        }
        return String.valueOf(successcount);
    }


    private String transferItemData(List<QuestionShortanswer> resList){
        try{
            QuestionShortanswer res = resList.get(0);
            String tId = IdSequence.nextId();

            res.setId(tId);
            res.setBaseId(tId);
            res.setStatus(QuestionStatus.SUBMITED.toString());
            res.setAuditStatus(QuestionAuditStatus.APPROVED.toString());

            res.setCreateTime(new Date());
            res.setCreateUserId(Constants.user_key);
            res.setCreateUserName(Constants.user_name);

            questionShortanswerDao.addQuestionShortanswer(res);
            return tId;

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return "";
    }


}
