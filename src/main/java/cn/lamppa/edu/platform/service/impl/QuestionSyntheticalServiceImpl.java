package cn.lamppa.edu.platform.service.impl;

import cn.lamppa.edu.platform.dao.*;
import cn.lamppa.edu.platform.domain.*;
import cn.lamppa.edu.platform.enums.CategoryType;
import cn.lamppa.edu.platform.enums.QuestionAuditStatus;
import cn.lamppa.edu.platform.enums.QuestionStatus;
import cn.lamppa.edu.platform.enums.QuestionType;
import cn.lamppa.edu.platform.service.*;
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
 * Created by heizhiqiang on 2016/3/1 0001.
 */
@Service
public class QuestionSyntheticalServiceImpl implements QuestionSyntheticalService {

    private static Logger logger = LoggerFactory.getLogger(QuestionSyntheticalServiceImpl.class);

    @Resource
    private QuestionBaseDao questionBaseDao;

    @Resource
    private QuestionKnowledgeDao questionKnowledgeDao;

    @Resource
    private QuestionTestPointDao questionTestPointDao;

    @Resource
    private QuestionCategoryDao questionCategoryDao;

    @Resource
    private QuestionFillingService questionFillingService;

    @Resource
    private CommonDao commonDao;

    @Resource
    private QuestionJudgeService questionJudgeService;

    @Resource
    private QuestionShortanswerService questionShortanswerService;

    @Resource
    private QuestionSyntheticalDao questionSyntheticalDao;

    @Resource
    private PointAndChoiceService pointAndChoiceService;

    public void syncQuestion() {
        Long start = System.currentTimeMillis();
        String errerId = "";
        try{
            List<QuestionSynthetical> list =  questionSyntheticalDao.findQuestionSynthetical();
            for(QuestionSynthetical res:list){
                errerId = res.getId();
                String tId = IdSequence.nextId();
                logger.info("start transfer synthetical quesiton NO={} new Id={}",res.getId(), tId);


                List<QuestionSyntheticalItem> items = questionSyntheticalDao.findItems(res.getId());
                for(QuestionSyntheticalItem item: items){

                    if(QuestionType.FILLING.toString().equals(item.getType().toUpperCase())){
                       String itemId = questionFillingService.syncItemQuestion(item.getQuestionId());
                        item.setQuestionId(itemId);
                        item.setType(QuestionType.FILLING.toString());
                    }else if(QuestionType.JUDGE.toString().equals(item.getType().toUpperCase())){
                        String itemId =  questionJudgeService.syncItemQuestion(item.getQuestionId());
                        item.setQuestionId(itemId);
                        item.setType(QuestionType.JUDGE.toString());

                    }else if(QuestionType.SHORTANSWER.toString().equals(item.getType().toUpperCase())){
                        String itemId = questionShortanswerService.syncItemQuestion(item.getQuestionId());
                        item.setQuestionId(itemId);
                        item.setType(QuestionType.SHORTANSWER.toString());
                    }else if(QuestionType.CHOICE.toString().equals(item.getType().toUpperCase())){
                        String itemId = pointAndChoiceService.synChoiceByChoiceId(item.getQuestionId());
                        item.setQuestionId(itemId);
                        item.setType(QuestionType.CHOICE.toString());
                    }
                    item.setId(IdSequence.nextId());
                    item.setSyntheticalId(tId);
                    questionSyntheticalDao.addItem(item);
                }


                QuestionBase base = questionBaseDao.findQuestionBase(res.getId());
                List<QuestionKnowledge> major = questionKnowledgeDao.findMajorKnowledgeList(res.getId());
                List<QuestionKnowledge> minor = questionKnowledgeDao.findMinorKnowledges(res.getId());
                List<QuestionTestPoint> testPoints = questionTestPointDao.findQuestionTestPoints(res.getId());
                List<QuestionCategory> categories = questionCategoryDao.findQuestionCategoryByQuestionId(res.getId());


                base.setId(tId);
                base.setType(QuestionType.SYNTHETICAL.toString());
                questionBaseDao.addQuestionBase(base);

                for(QuestionKnowledge v:major){
                    v.setId(IdSequence.nextId());
                    v.setQuestionId(tId);
                    v.setType(QuestionType.SYNTHETICAL.toString());
                    v.setKnowledgeId(commonDao.getKnowledgeId(v.getKnowledgeId()));
                    v.setCognizeLevelId(commonDao.getCognizeLevel(v.getCognizeLevelId()));
                    v.setAbilityId(commonDao.getAbliity(v.getAbilityId()));
                    questionKnowledgeDao.addQuestionKnowledge(v);
                }

                for(QuestionKnowledge v:minor){
                    v.setId(IdSequence.nextId());
                    v.setQuestionId(tId);
                    v.setType(QuestionType.SYNTHETICAL.toString());
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
                    v.setCategroy(CategoryType.simulation.toString());
                    v.setCategroyName(CategoryType.simulation.getType());
                }
                questionCategoryDao.addQuestionCategory(categories);


                res.setId(tId);
                res.setBaseId(tId);
                res.setStatus(QuestionStatus.SUBMITED.toString());
                res.setAuditStatus(QuestionAuditStatus.PENDINGAUDIT.toString());

                res.setCreateTime(new Date());
                res.setCreateUserId(Constants.user_key);
                res.setCreateUserName(Constants.user_name);

                questionSyntheticalDao.addQuestion(res);

                commonDao.insertMiddleTable(tId);
            }

        }catch (Exception e){
            e.printStackTrace();
            logger.error("run question No={} errer",errerId);
            logger.error(e.getMessage());
        }

    }

    @Override
    public String transferData(List<QuestionSynthetical> resList){
        Long start = System.currentTimeMillis();
        String errerId = "";
        int successCount = 0;
        try{
            for(QuestionSynthetical res:resList){
                errerId = res.getId();
                String tId = IdSequence.nextId();
                logger.info("start transfer synthetical quesiton NO={}  new Id={}",res.getId(), tId);


                List<QuestionSyntheticalItem> items = questionSyntheticalDao.findItems(res.getId());
                for(QuestionSyntheticalItem item: items){

                    if(QuestionType.FILLING.toString().equals(item.getType().toUpperCase())){
                        String itemId = questionFillingService.syncItemQuestion(item.getQuestionId());
                        item.setQuestionId(itemId);
                        item.setType(QuestionType.FILLING.toString());
                    }else if(QuestionType.JUDGE.toString().equals(item.getType().toUpperCase())){
                        String itemId =  questionJudgeService.syncItemQuestion(item.getQuestionId());
                        item.setQuestionId(itemId);
                        item.setType(QuestionType.JUDGE.toString());

                    }else if(QuestionType.SHORTANSWER.toString().equals(item.getType().toUpperCase())){
                        String itemId = questionShortanswerService.syncItemQuestion(item.getQuestionId());
                        item.setQuestionId(itemId);
                        item.setType(QuestionType.SHORTANSWER.toString());
                    }else if(QuestionType.CHOICE.toString().equals(item.getType().toUpperCase())){
                        String itemId = pointAndChoiceService.synChoiceByChoiceId(item.getQuestionId());
                        item.setQuestionId(itemId);
                        item.setType(QuestionType.CHOICE.toString());
                    }
                    item.setId(IdSequence.nextId());
                    item.setSyntheticalId(tId);
                    questionSyntheticalDao.addItem(item);
                }


                QuestionBase base = questionBaseDao.findQuestionBase(res.getId());
                List<QuestionKnowledge> major = questionKnowledgeDao.findMajorKnowledgeList(res.getId());
                List<QuestionKnowledge> minor = questionKnowledgeDao.findMinorKnowledges(res.getId());
                List<QuestionTestPoint> testPoints = questionTestPointDao.findQuestionTestPoints(res.getId());
                List<QuestionCategory> categories = questionCategoryDao.findQuestionCategoryByQuestionId(res.getId());

                if(base == null){
                    logger.error("error on question No={} 缺少 base 信息",errerId);
                }else{
                    base.setId(tId);
                    base.setType(QuestionType.SYNTHETICAL.toString());
                    questionBaseDao.addQuestionBase(base);
                }

                if(major == null){
                    logger.error("error on question No={} 缺少 knowledge 信息",errerId);
                }else{
                    for(QuestionKnowledge v:major){
                        v.setId(IdSequence.nextId());
                        v.setQuestionId(tId);
                        v.setType(QuestionType.SYNTHETICAL.toString());
                        v.setKnowledgeId(commonDao.getKnowledgeId(v.getKnowledgeId()));
                        v.setCognizeLevelId(commonDao.getCognizeLevel(v.getCognizeLevelId()));
                        v.setAbilityId(commonDao.getAbliity(v.getAbilityId()));
                        questionKnowledgeDao.addQuestionKnowledge(v);
                    }
                }


                for(QuestionKnowledge v:minor){
                    v.setId(IdSequence.nextId());
                    v.setQuestionId(tId);
                    v.setType(QuestionType.SYNTHETICAL.toString());
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
                    v.setCategroy(CategoryType.simulation.toString());
                    v.setCategroyName(CategoryType.simulation.getType());
                }
                questionCategoryDao.addQuestionCategory(categories);


                res.setId(tId);
                res.setBaseId(tId);
                res.setStatus(QuestionStatus.SUBMITED.toString());
                res.setAuditStatus(QuestionAuditStatus.APPROVED.toString());

                res.setCreateTime(new Date());
                res.setCreateUserId(Constants.user_key);
                res.setCreateUserName(Constants.user_name);

                questionSyntheticalDao.addQuestion(res);

                commonDao.insertMiddleTable(tId);
                successCount++;
            }

        }catch (Exception e){
            logger.error("run question No={} errer",errerId);
            logger.error(e.getMessage());
        }
        return String.valueOf(successCount);
    }

    @Override
    public int getCount() {
        return questionSyntheticalDao.getCount();
    }

    @Override
    public List<QuestionSynthetical> findQuestionByPage(int start, int end) {
        return questionSyntheticalDao.findQuestionByPage(start, end);
    }


}
