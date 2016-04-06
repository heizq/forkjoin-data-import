package cn.lamppa.edu.platform.app;

import cn.lamppa.edu.platform.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

/**
 * Created by heizhiqiang on 2016/3/1 0001.
 */
public class MainApp {

    private static Logger logger = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:application-context.xml");
        PointAndChoiceService pointAndChoiceService=ctx.getBean(PointAndChoiceService.class);
        QuestionFillingService questionFillingService = ctx.getBean(QuestionFillingService.class);
        QuestionSyntheticalService questionSyntheticalService = ctx.getBean(QuestionSyntheticalService.class);
        QuestionJudgeService questionJudgeService = ctx.getBean(QuestionJudgeService.class);
        QuestionShortanswerService questionShortanswerService = ctx.getBean(QuestionShortanswerService.class);
        TextbookService textbookService = ctx.getBean(TextbookService.class);

        logger.info("......... transfer  data   start.....");

        Long start = System.currentTimeMillis();

//        pointAndChoiceService.createSynQuestion();
//
//        pointAndChoiceService.createSynQuestionMiddleTable();
//
//        pointAndChoiceService.synKnowledge();
//
//        pointAndChoiceService.synKnowledgeRelation();
//
//        pointAndChoiceService.synChoices();

        textbookService.addTextbook();

//        questionJudgeService.syncQuestion();
//
//        questionShortanswerService.syncQuestion();

       // questionFillingService.syncQuestion();

//        questionSyntheticalService.syncQuestion();

        logger.info("......... transfer data   end.....");

        logger.info ("transfer data use time ={} minute",(System.currentTimeMillis() - start)/(60*1000));
    }



}
