import cn.lamppa.edu.platform.domain.QuestionBase;
import cn.lamppa.edu.platform.domain.QuestionCategory;
import cn.lamppa.edu.platform.domain.QuestionKnowledge;
import cn.lamppa.edu.platform.domain.QuestionTestPoint;
import cn.lamppa.edu.platform.enums.QuestionType;
import cn.lamppa.edu.platform.service.QuestionFillingService;
import cn.lamppa.edu.platform.util.IdSequence;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heizhiqiang on 2016/2/24 0024.
 */
public class DaoTest extends TestCase{

    private QuestionFillingService questionFillingService;

    protected void setUp(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:application-context.xml");
        //questionFillingService = (QuestionFillingService) ctx.getBean("questionFillingService");
        questionFillingService = ctx.getBean(QuestionFillingService.class);
    }

    @Test
    public void testCreateSyn()  throws Exception{
        String questionId = IdSequence.nextId();
        QuestionBase base = new QuestionBase();
        base.setAccuracy("123");
        base.setDifficulty(5);
        base.setId(questionId);
        base.setSource("kfajweoi啊是的哈开始了基督教if");
        base.setType(QuestionType.CHOICE.toString());
        base.setUseNum(1);
        base.setYear("2016");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("base",base);

        List<QuestionKnowledge> minorKnowledge = new ArrayList<QuestionKnowledge>();
        for(int i=1;i<3;i++){
            QuestionKnowledge v = new QuestionKnowledge();
            v.setId(IdSequence.nextId());
            v.setIsMain(false);
            v.setType(QuestionType.FILLING.toString());
            v.setQuestionId(questionId);
            v.setKnowledgeId("1231231");
            v.setCognizeLevelId("11111");
            v.setAbilityId("22222");
            v.setSort(i);
            minorKnowledge.add(v);
        }
        map.put("minorKnowledge",minorKnowledge);

        List<QuestionTestPoint> testPoints = new ArrayList<QuestionTestPoint>();
        for(int i=1;i<3;i++){
            QuestionTestPoint v = new QuestionTestPoint();
            v.setId(IdSequence.nextId());
            v.setQuestionId(questionId);
            v.setKnowledgeId("111");
            v.setQuestionType(QuestionType.FILLING.toString());
            v.setTestPointId("11112222");
            testPoints.add(v);
        }
        map.put("testpoint",testPoints);

        List<QuestionCategory> categorys = new ArrayList<QuestionCategory>();
        for(int i=1;i<2;i++){
            QuestionCategory v = new QuestionCategory();
            v.setQuestionId(questionId);
            v.setId(IdSequence.nextId());
            v.setCategroy("simulation");
            v.setCategroyName("模拟");
            categorys.add(v);
        }
        map.put("category",categorys);


    }



}
