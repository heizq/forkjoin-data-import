package cn.lamppa.edu.platform.service;



/**
 * Created by liupd on 16-2-26.
 **/
public interface PointAndChoiceService {

    public void createSynQuestion()throws Exception;

    public void createSynQuestionMiddleTable()throws Exception;

    public void synAbility()throws Exception;

    public void synAbility2(int start,int end)throws Exception;

    public void synCognizelevel() throws Exception;

    public void synKnowledge()throws Exception;

    public void synKnowledgeRelation()throws Exception;

    public void synChoices(int start,int end) throws Exception;

    public String synChoiceByChoiceId(String id) throws Exception;

    public void synchoicedata()throws Exception;

    public Integer getCount()throws  Exception;


}
