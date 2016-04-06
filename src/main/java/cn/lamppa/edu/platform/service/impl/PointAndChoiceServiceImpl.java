package cn.lamppa.edu.platform.service.impl;

import cn.lamppa.edu.platform.enums.QuestionsTables;
import cn.lamppa.edu.platform.service.PointAndChoiceService;
import cn.lamppa.edu.platform.util.Constants;
import cn.lamppa.edu.platform.util.IdSequence;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sun.util.logging.resources.logging;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liupd on 16-2-26.
 **/

@Configurable
@Repository(value="pointAndChoiceService")
public class PointAndChoiceServiceImpl implements PointAndChoiceService {

    private static Logger logger = LoggerFactory.getLogger(PointAndChoiceServiceImpl.class);

    public static final String choice="CHOICE";

    public static final String audit_status="APPROVED";

    public static final String status="SUBMITED";

    @Autowired
    private JdbcTemplate resJdbcTemplate;

    @Autowired
    private JdbcTemplate platformJdbcTemplate;

    //@Transactional
    @Override
    public void createSynQuestion() throws Exception {
        String tableName = QuestionsTables.syn_table.toString();
        Connection conn = resJdbcTemplate.getDataSource().getConnection();
        DatabaseMetaData dbMetaData = conn.getMetaData();
        String[]   types   =   { "TABLE" };
        ResultSet tabs = dbMetaData.getTables(null, null, tableName, types);
        logger.info("create syn_table start..");
        if (!tabs.next()) {
            StringBuffer sql = new StringBuffer();
            sql.append(" CREATE TABLE `" + tableName + "` (");
            sql.append(" `id`  bigint(20) NOT NULL AUTO_INCREMENT ,");
            sql.append("`table_name`  varchar(255) NULL DEFAULT NULL COMMENT '表名称' ,");
            sql.append("`source_id`  varchar(255) NULL DEFAULT NULL COMMENT '源数据id',");
            sql.append("`desc_id`  varchar(255) NULL DEFAULT NULL COMMENT '目标数据id',");
            sql.append(" PRIMARY KEY (`id`)");
            sql.append(") CHARACTER SET=utf8 COLLATE=utf8_general_ci;");
            resJdbcTemplate.update(sql.toString());
            resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.ks_knowledge_point.name(),"0","0"});
            resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.ks_knowledge_point_relation.name(),"0","0"});
            resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.ks_question_base.name(),"0","0"});
            resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.ks_question_choice.name(),"0","0"});
            resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.ks_question_choice_answer.name(),"0","0"});
            resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.ks_question_knowledge.name(),"0","0"});
            resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.ks_question_choice.name(),"0","0"});
            resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.bd_ability.name(),"0","0"});
            resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.bd_cognize_level.name(),"0","0"});
            logger.info("create syn_middle success!");
        }
        logger.info("syn_table exists");
        tabs.close();
        conn.close();
    }

    @Override
    public void createSynQuestionMiddleTable() throws Exception {
        String tableName = QuestionsTables.ks_question_flag.toString();
        Connection conn = platformJdbcTemplate.getDataSource().getConnection();
        DatabaseMetaData dbMetaData = conn.getMetaData();
        String[]   types   =   { "TABLE" };
        logger.info("create ks_question_flag start..");
        ResultSet tabs = dbMetaData.getTables(null, null, tableName, types);
        if (!tabs.next()) {
            StringBuffer sql = new StringBuffer();
            sql.append(" CREATE TABLE `" + tableName + "` (");
            sql.append(" `id`  bigint(20) NOT NULL AUTO_INCREMENT ,");
            sql.append("`question_id`  varchar(255) NULL DEFAULT NULL COMMENT '题id',");
            sql.append("`src_flag`  varchar(50) NULL DEFAULT NULL COMMENT '题来源标志说明',");
            sql.append("`data_bak`  varchar(50) NULL DEFAULT NULL COMMENT '预留字段备用',");
            sql.append(" PRIMARY KEY (`id`) ");
            sql.append(") CHARACTER SET=utf8 COLLATE=utf8_general_ci;");
            platformJdbcTemplate.update(sql.toString());
            logger.info("create ks_question_flag success!");
        }
        logger.info("syn_table exists");
        tabs.close();
        conn.close();
    }

    @Override
    public void synAbility() throws Exception {
        logger.info(".....bd_ability start import......");
        String srcSql="select source_id from syn_table where table_name='bd_ability' and id in(select max(id) from syn_table where table_name='bd_ability' )";
        String max_source_id = resJdbcTemplate.queryForObject(srcSql,String.class);
        Long start=System.currentTimeMillis();
        Integer i=0;
        List<Map<String,Object>> list=null;
        if(StringUtils.isNotBlank(max_source_id)&&!max_source_id.equals("0")){
            list = resJdbcTemplate.queryForList("select * from "+QuestionsTables.bd_ability.name()+" where id>? ",new Object[]{max_source_id});
        }else{
            list=resJdbcTemplate.queryForList("select * from "+QuestionsTables.bd_ability.name()+"");
        }
        System.out.println(list);
        if(list.size()>0){
            String errorIds="id contains";
            for(Map map:list){
                i++;
                String srcId=map.get("id").toString();
                String destId= IdSequence.nextId();
                logger.info("bd_ability.."+srcId+"正在导入");
                String name = map.get("name")==null?null:map.get("name").toString().trim();
                String phaseName=map.get("phases_business_key")==null?null:map.get("phases_business_key").toString().trim();
                String subjectName=map.get("subject_business_key")==null?null:map.get("subject_business_key").toString().trim();
                Date update_time =new Date();
                String phases_id=(String)platformJdbcTemplate.query("SELECT p.business_key FROM bd_phases p where p.name=?",new Object[]{phaseName},new ResultSetExtractor<Object>() {
                    @Override
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                        while(rs.next()){
                            return rs.getString("business_key");
                        }
                        return null;
                    }
                });
                String subjects_id=(String)platformJdbcTemplate.query("SELECT p.business_key FROM bd_subjects p where p.name=?",new Object[]{subjectName},new ResultSetExtractor<Object>() {
                    @Override
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                        while(rs.next()){
                            return rs.getString("business_key");
                        }
                        return null;
                    }
                });
                StringBuffer sql=new StringBuffer();
                sql.append(" insert into ").append(QuestionsTables.bd_ability.name()).append("  ");
                sql.append(" (id,version,business_key,create_time,is_delete,name,phases_business_key,subject_business_key,update_time) ");
                sql.append(" values(?,?,?,?,?,?,?,?,?)");
                Object[] param={destId,"0",destId,update_time,"N",name,phases_id,subjects_id,update_time};
                platformJdbcTemplate.update(sql.toString(), param);
                resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.bd_ability.name(),srcId,destId});
                logger.info("bd_ability .."+srcId+"导入成功");
            }
        }
        logger.info("用时:"+(System.currentTimeMillis()-start));
    }

    @Override
    public  void synAbility2(int start,int end) throws Exception {
        //logger.info(".....bd_ability start import......");
        Long starts=System.currentTimeMillis();
        Integer i=0;
        List<Map<String,Object>> list=null;
        String sqlStr="select * from "+QuestionsTables.bd_ability.name()+ "  limit   "+start+"  ,  "+end+"";
        list=resJdbcTemplate.queryForList(sqlStr);
       /* System.out.println("sql:"+sqlStr);
        System.out.println("start:"+start+","+"end:"+end+"size():"+list.size());*/
        //System.out.println(list.size());
        if(list.size()>0){
            String errorIds="id contains";
            for(Map map:list){
                i++;
                String srcId=map.get("id").toString();
                String destId= IdSequence.nextId();
                //logger.info("bd_ability.."+srcId+"正在导入");
                String name = map.get("name")==null?null:map.get("name").toString().trim();
                String phaseName=map.get("phases_business_key")==null?null:map.get("phases_business_key").toString().trim();
                String subjectName=map.get("subject_business_key")==null?null:map.get("subject_business_key").toString().trim();
                Date update_time =new Date();
                String phases_id=(String)platformJdbcTemplate.query("SELECT p.business_key FROM bd_phases p where p.name=?",new Object[]{phaseName},new ResultSetExtractor<Object>() {
                    @Override
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                        while(rs.next()){
                            return rs.getString("business_key");
                        }
                        return null;
                    }
                });
                String subjects_id=(String)platformJdbcTemplate.query("SELECT p.business_key FROM bd_subjects p where p.name=?",new Object[]{subjectName},new ResultSetExtractor<Object>() {
                    @Override
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                        while(rs.next()){
                            return rs.getString("business_key");
                        }
                        return null;
                    }
                });
                StringBuffer sql=new StringBuffer();
                sql.append(" insert into ").append(QuestionsTables.bd_ability.name()).append("  ");
                sql.append(" (id,version,business_key,create_time,is_delete,name,phases_business_key,subject_business_key,update_time) ");
                sql.append(" values(?,?,?,?,?,?,?,?,?)");
                Object[] param={destId,"0",destId,update_time,"N",name,phases_id,subjects_id,update_time};
                platformJdbcTemplate.update(sql.toString(), param);
                //resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.bd_ability.name(),srcId,destId});
                //logger.info("bd_ability .."+srcId+"导入成功");
            }
        }
        logger.info("用时:"+(System.currentTimeMillis()-starts));
    }

    @Override
    public void synCognizelevel() throws Exception {
        logger.info(".....bd_cognize_level start import......");
        String srcSql="select source_id from syn_table where table_name='bd_cognize_level' and id in(select max(id) from syn_table where table_name='bd_cognize_level' )";
        String max_source_id = resJdbcTemplate.queryForObject(srcSql,String.class);
        Long start=System.currentTimeMillis();
        Integer i=0;
        List<Map<String,Object>> list=null;
        if(StringUtils.isNotBlank(max_source_id)&&!max_source_id.equals("0")){
            list = resJdbcTemplate.queryForList("select * from "+QuestionsTables.bd_cognize_level.name()+" where id>? ",new Object[]{max_source_id});
        }else{
            list=resJdbcTemplate.queryForList("select * from "+QuestionsTables.bd_cognize_level.name()+"");
        }
        System.out.println(list);
        if(list.size()>0){
            String errorIds="id contains";
            for(Map map:list){
                i++;
                String srcId=map.get("id").toString();
                String destId= IdSequence.nextId();
                logger.info("bd_cognize_level.."+srcId+"正在导入");
                String name = map.get("name")==null?null:map.get("name").toString().trim();
                String phaseName=map.get("phases_business_key")==null?null:map.get("phases_business_key").toString().trim();
                String subjectName=map.get("subject_business_key")==null?null:map.get("subject_business_key").toString().trim();
                Date update_time =new Date();
                String phases_id=(String)platformJdbcTemplate.query("SELECT p.business_key FROM bd_phases p where p.name=?",new Object[]{phaseName},new ResultSetExtractor<Object>() {
                    @Override
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                        while(rs.next()){
                            return rs.getString("business_key");
                        }
                        return null;
                    }
                });
                String subjects_id=(String)platformJdbcTemplate.query("SELECT p.business_key FROM bd_subjects p where p.name=?",new Object[]{subjectName},new ResultSetExtractor<Object>() {
                    @Override
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                        while(rs.next()){
                            return rs.getString("business_key");
                        }
                        return null;
                    }
                });
                StringBuffer sql=new StringBuffer();
                sql.append(" insert into ").append(QuestionsTables.bd_cognize_level.name()).append("  ");
                sql.append(" (id,version,business_key,create_time,is_delete,name,phases_business_key,subject_business_key,update_time) ");
                sql.append(" values(?,?,?,?,?,?,?,?,?)");
                Object[] param={destId,"0",destId,update_time,"N",name,phases_id,subjects_id,update_time};
                platformJdbcTemplate.update(sql.toString(), param);
                resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.bd_cognize_level.name(),srcId,destId});
                logger.info("bd_cognize_level .."+srcId+"导入成功");
            }
        }
        logger.info("用时:"+(System.currentTimeMillis()-start));
    }


    @Override
    public void synKnowledge() throws Exception {
        logger.info(".....Knowledge point start import......");
        String srcSql="select source_id from syn_table where table_name='ks_knowledge_point' and id in(select max(id) from syn_table where table_name='ks_knowledge_point' )";
        String max_source_id = resJdbcTemplate.queryForObject(srcSql,String.class);
        Long start=System.currentTimeMillis();
        Integer i=0;
        List<Map<String,Object>> list=null;
        if(StringUtils.isNotBlank(max_source_id)&&!max_source_id.equals("0")){
            list = resJdbcTemplate.queryForList("select * from "+QuestionsTables.ks_knowledge_point.name()+" where id>? ",new Object[]{max_source_id});
        }else{
            list=resJdbcTemplate.queryForList("select * from "+QuestionsTables.ks_knowledge_point.name()+"");
        }
        System.out.println(list.size());
        if(list.size()>0){
            String errorIds="id contains";
            for(Map map:list){
                i++;
                String srcId=map.get("id").toString();
                String destId= IdSequence.nextId();
                logger.info("ks_knowledge_point.."+srcId+"正在导入");
                String name = map.get("name")==null?null:map.get("name").toString().trim();
                String phaseName=map.get("phase_name")==null?null:map.get("phase_name").toString().trim();
                String subjectName=map.get("subject_name")==null?null:map.get("subject_name").toString().trim();
                String instruction = map.get("instruction")==null?null:(String)map.get("instruction");
                String remark = map.get("remark")==null?null:map.get("remark").toString();
                Date update_time =new Date();
                String phases_id=(String)platformJdbcTemplate.query("SELECT p.business_key FROM bd_phases p where p.name=?",new Object[]{phaseName},new ResultSetExtractor<Object>() {
                        @Override
                        public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                            while(rs.next()){
                                return rs.getString("business_key");
                            }
                            return null;
                        }
                    });
                String subjects_id=(String)platformJdbcTemplate.query("SELECT p.business_key FROM bd_subjects p where p.name=?",new Object[]{subjectName},new ResultSetExtractor<Object>() {
                    @Override
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                        while(rs.next()){
                            return rs.getString("business_key");
                        }
                        return null;
                    }
                });
                StringBuffer sql=new StringBuffer();
                sql.append(" insert into ").append(QuestionsTables.ks_knowledge_point.name()).append("  ");
                sql.append(" (id,name,subject_id,subject_name,phase_id,phase_name,instruction,remark,update_time) ");
                sql.append(" values(?,?,?,?,?,?,?,?,?)");
                Object[] param={destId,name,subjects_id,subjectName,phases_id,phaseName,instruction,remark,update_time};
                platformJdbcTemplate.update(sql.toString(), param);
                resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.ks_knowledge_point.name(),srcId,destId});
                logger.info("ks_knowledge_point .."+srcId+"导入成功");
            }
        }
        logger.info("用时:"+(System.currentTimeMillis()-start));
    }

    @Override
    public void synKnowledgeRelation() throws Exception {

        logger.info(".....Knowledge point relation start import......");
        String srcSql="select source_id from syn_table where table_name='ks_knowledge_point_relation' and id in(select max(id) from syn_table where table_name='ks_knowledge_point_relation' )";
        String max_source_id = resJdbcTemplate.queryForObject(srcSql,String.class);
        Long start=System.currentTimeMillis();
        Integer i=0;
        List<Map<String,Object>> list=null;
        if(StringUtils.isNotBlank(max_source_id)&&!max_source_id.equals("0")){
            list = resJdbcTemplate.queryForList("select * from "+QuestionsTables.ks_knowledge_point_relation.name()+" where id>? and parent_id!=0 ",new Object[]{max_source_id});
        }else{
            list=resJdbcTemplate.queryForList("select * from "+QuestionsTables.ks_knowledge_point_relation.name()+" where parent_id!=0 ");
        }
        if(list.size()>0){
            String errorIds="id contains";
            for(Map map:list){
                i++;
                String srcId=map.get("id").toString();
                String destId= IdSequence.nextId();
                logger.info("ks_knowledge_point_relation.."+srcId+"正在导入");
                String knowledge_id = map.get("knowledge_id")==null?null:map.get("knowledge_id").toString().trim();
                String parent_id=map.get("parent_id")==null?null:map.get("parent_id").toString().trim();

                String knowledgeId=(String)resJdbcTemplate.query("SELECT desc_id FROM syn_table where source_id=? and table_name='ks_knowledge_point'",new Object[]{knowledge_id},new ResultSetExtractor<Object>() {
                    @Override
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                        while(rs.next()){
                            return rs.getString("desc_id");
                        }
                        return null;
                    }
                });

                String parentId=(String)resJdbcTemplate.query("SELECT desc_id FROM syn_table where source_id=? and table_name='ks_knowledge_point'",new Object[]{parent_id},new ResultSetExtractor<Object>() {
                    @Override
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                        while(rs.next()){
                            return rs.getString("desc_id");
                        }
                        return null;
                    }
                });
                StringBuffer sql=new StringBuffer();
                sql.append(" insert into ").append(QuestionsTables.ks_knowledge_point_relation.name()).append("  ");
                sql.append(" (id,knowledge_id,parent_id) ");
                sql.append(" values(?,?,?)");
                Object[] param={destId,knowledgeId,parentId};
                platformJdbcTemplate.update(sql.toString(), param);
                resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.ks_knowledge_point_relation.name(),srcId,destId});
                logger.info("ks_knowledge_point .."+srcId+"导入成功");
            }
        }
        logger.info("用时:"+(System.currentTimeMillis()-start));
    }



    @Override
    public void synChoices(int start,int end) throws Exception {
        //resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.ks_question_choice.name(),"0","0"});
        logger.info(".....choice start import......");
        /*String srcSql="select source_id from syn_table where table_name='ks_question_choice' and id in(select max(id) from syn_table where table_name='ks_question_choice' )";
        String max_source_id = resJdbcTemplate.queryForObject(srcSql, String.class);*/
        Long starts=System.currentTimeMillis();
        Integer i=0;
        String querySql="select c.id,c.base_id,c.topic,c.analysis,c.is_small,c.is_multi,e.year,e.source,e.difficulty from ks_question_base e,ks_question_choice c where c.base_id=e.id and c.is_small=0 limit "+ start +","+ end +"";
        List<Map<String,Object>> list=resJdbcTemplate.queryForList(querySql);
            if(list.size()>0){
                String errorIds="id contains";
                for(Map map:list){
                    i++;
                    String choiceId=map.get("id").toString();
                    String base_id=map.get("base_id")==null?null:map.get("base_id").toString();
                    String destchoiceId= IdSequence.nextId();
                    logger.info("ks_question_choice.."+choiceId+"正在导入");
                    String topic = map.get("topic")==null?null:map.get("topic").toString().trim();
                    String analysis=map.get("analysis")==null?null:map.get("analysis").toString().trim();
                    Boolean is_multi=map.get("is_multi")==null?null:(Boolean)map.get("is_multi");
                    String difficulty=map.get("difficulty")==null?null:map.get("difficulty").toString();
                    String year=map.get("year")==null?null:map.get("year").toString();
                    platformJdbcTemplate.update("insert into ks_question_base(id,difficulty,year,type)values(?,?,?,?)", new Object[]{destchoiceId, difficulty, year, choice});
                    String choiceSql="insert into ks_question_choice(id,topic,analysis,base_id,is_small,is_multi,audit_status,status,create_time,create_user_id,create_user_name)values(?,?,?,?,?,?,?,?,?,?,?)";
                    platformJdbcTemplate.update(choiceSql, new Object[]{destchoiceId, topic, analysis, destchoiceId, false, is_multi, audit_status, status, new Date(), Constants.user_key, Constants.user_name});
                    List<Map<String,Object>> choiceAnswers=resJdbcTemplate.queryForList("select * from "+QuestionsTables.ks_question_choice_answer.name()+" where choice_id=?",choiceId);
                    if(choiceAnswers.size()>0){
                        for(Map answer:choiceAnswers){
                            String destAnswerchoiceId= IdSequence.nextId();
                            String topicAnswer = answer.get("topic")==null?null:answer.get("topic").toString().trim();
                            String sort=answer.get("sort")==null?null:answer.get("sort").toString().trim();
                            Boolean correct_answer=answer.get("correct_answer")==null?null:(Boolean)answer.get("correct_answer");
                            StringBuffer answersql=new StringBuffer();
                            answersql.append(" insert into ").append(QuestionsTables.ks_question_choice_answer.name()).append("  ");
                            answersql.append(" (id,choice_id,topic,correct_answer,sort) ");
                            answersql.append(" values(?,?,?,?,?)");
                            Object[] param={destAnswerchoiceId,destchoiceId,topicAnswer,correct_answer,sort};
                            platformJdbcTemplate.update(answersql.toString(), param);
                        }
                    }
                    List<Map<String,Object>> choiceKnowledges=resJdbcTemplate.queryForList("select * from "+QuestionsTables.ks_question_knowledge.name()+" where question_id=?",choiceId);
                    if(choiceKnowledges.size()>0){
                        for(Map knowledge:choiceKnowledges){
                            String choiceknowledgeId = knowledge.get("knowledge_id")==null?null:knowledge.get("knowledge_id").toString().trim();
                            String destKnowledgechoiceId= IdSequence.nextId();
                            String knowledgeId=(String)resJdbcTemplate.query("SELECT desc_id FROM syn_table where source_id=? and table_name='ks_knowledge_point'",new Object[]{choiceknowledgeId},new ResultSetExtractor<Object>() {
                                @Override
                                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                                    while(rs.next()){
                                        return rs.getString("desc_id");
                                    }
                                    return null;
                                }
                            });
                            Boolean is_main=knowledge.get("is_main")==null?null:(Boolean)knowledge.get("is_main");
                            String cognize_level_id=knowledge.get("cognize_level_id")==null?null:knowledge.get("cognize_level_id").toString();
                            String ability_id=knowledge.get("ability_id")==null?null:knowledge.get("ability_id").toString();
                            if(StringUtils.isNotBlank(ability_id)){
                                ability_id=(String)resJdbcTemplate.query("SELECT desc_id FROM syn_table where table_name='bd_ability' and source_id=? ",new Object[]{ability_id},new ResultSetExtractor<Object>() {
                                    @Override
                                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                                        while(rs.next()){
                                            return rs.getString("desc_id");
                                        }
                                        return null;
                                    }
                                });
                            }
                            if(StringUtils.isNotBlank(cognize_level_id)){
                                cognize_level_id=(String)resJdbcTemplate.query("SELECT desc_id FROM syn_table where table_name='bd_cognize_level' and source_id=? ",new Object[]{cognize_level_id},new ResultSetExtractor<Object>() {
                                    @Override
                                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                                        while(rs.next()){
                                            return rs.getString("desc_id");
                                        }
                                        return null;
                                    }
                                });
                            }
                            StringBuffer answersql=new StringBuffer();
                            answersql.append(" insert into ").append(QuestionsTables.ks_question_knowledge.name()).append("  ");
                            answersql.append(" (id,question_id,type,knowledge_id,is_main,cognize_level_id,ability_id) ");
                            answersql.append(" values(?,?,?,?,?,?,?)");
                            Object[] param={destKnowledgechoiceId,destchoiceId,choice,knowledgeId,is_main,cognize_level_id,ability_id};
                            platformJdbcTemplate.update(answersql.toString(), param);
                        }
                        platformJdbcTemplate.update("insert into ks_question_flag(question_id,src_flag,data_bak)values(?,?,?)",new String[]{destchoiceId,Constants.src_flag,null});
                        resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.ks_question_choice.name(),base_id,destchoiceId});
                        logger.info("ks_question_choice .."+choiceId+"导入成功");
                    }
                }
            }
        logger.info("用时:"+(System.currentTimeMillis()-starts));
    }


    public void synchoicedata()throws Exception{
        logger.info(".....choice start import......");
        Long starts=System.currentTimeMillis();
        List<Map<String,Object>> list=null;
        Integer i=0;
        Integer pageNum=10000;
        Integer pageTotal=getPageNo(pageNum);
        logger.info("分页数:"+(pageTotal));
        for(int t=0;t<pageTotal;t++){
        String querySql="select c.id,c.base_id,c.topic,c.analysis,c.is_small,c.is_multi,e.year,e.source,e.difficulty from ks_question_base e,ks_question_choice c where c.base_id=e.id and c.is_small=0 limit "+ pageNum*((t+1)-1) +","+ pageNum +"";
        list=resJdbcTemplate.queryForList(querySql);
            if(list.size()>0){
                String errorIds="id contains";
                for(Map map:list){
                    i++;
                    String choiceId=map.get("id").toString();
                    String base_id=map.get("base_id")==null?null:map.get("base_id").toString();
                    String destchoiceId= IdSequence.nextId();
                    logger.info("ks_question_choice.."+choiceId+"正在导入");
                    String topic = map.get("topic")==null?null:map.get("topic").toString().trim();
                    String analysis=map.get("analysis")==null?null:map.get("analysis").toString().trim();
                    Boolean is_multi=map.get("is_multi")==null?null:(Boolean)map.get("is_multi");
                    String difficulty=map.get("difficulty")==null?null:map.get("difficulty").toString();
                    String year=map.get("year")==null?null:map.get("year").toString();
                    platformJdbcTemplate.update("insert into ks_question_base(id,difficulty,year,type)values(?,?,?,?)",new Object[]{destchoiceId,difficulty,year,choice});
                    String choiceSql="insert into ks_question_choice(id,topic,analysis,base_id,is_small,is_multi,audit_status,status,create_time,create_user_id,create_user_name)values(?,?,?,?,?,?,?,?,?,?,?)";
                    platformJdbcTemplate.update(choiceSql,new Object[]{destchoiceId,topic,analysis,destchoiceId,false,is_multi,audit_status,status,new Date(), Constants.user_key,Constants.user_name});
                    List<Map<String,Object>> choiceAnswers=resJdbcTemplate.queryForList("select * from "+QuestionsTables.ks_question_choice_answer.name()+" where choice_id=?",choiceId);
                    if(choiceAnswers.size()>0){
                        for(Map answer:choiceAnswers){
                            String destAnswerchoiceId= IdSequence.nextId();
                            String topicAnswer = answer.get("topic")==null?null:answer.get("topic").toString().trim();
                            String sort=answer.get("sort")==null?null:answer.get("sort").toString().trim();
                            Boolean correct_answer=answer.get("correct_answer")==null?null:(Boolean)answer.get("correct_answer");
                            StringBuffer answersql=new StringBuffer();
                            answersql.append(" insert into ").append(QuestionsTables.ks_question_choice_answer.name()).append("  ");
                            answersql.append(" (id,choice_id,topic,correct_answer,sort) ");
                            answersql.append(" values(?,?,?,?,?)");
                            Object[] param={destAnswerchoiceId,destchoiceId,topicAnswer,correct_answer,sort};
                            platformJdbcTemplate.update(answersql.toString(), param);
                        }
                    }
                    List<Map<String,Object>> choiceKnowledges=resJdbcTemplate.queryForList("select * from "+QuestionsTables.ks_question_knowledge.name()+" where question_id=?",choiceId);
                    if(choiceKnowledges.size()>0){
                        for(Map knowledge:choiceKnowledges){
                            String choiceknowledgeId = knowledge.get("knowledge_id")==null?null:knowledge.get("knowledge_id").toString().trim();
                            String destKnowledgechoiceId= IdSequence.nextId();
                            String knowledgeId=(String)resJdbcTemplate.query("SELECT desc_id FROM syn_table where source_id=? and table_name='ks_knowledge_point'",new Object[]{choiceknowledgeId},new ResultSetExtractor<Object>() {
                                @Override
                                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                                    while(rs.next()){
                                        return rs.getString("desc_id");
                                    }
                                    return null;
                                }
                            });
                            Boolean is_main=knowledge.get("is_main")==null?null:(Boolean)knowledge.get("is_main");
                            String cognize_level_id=knowledge.get("cognize_level_id")==null?null:knowledge.get("cognize_level_id").toString();
                            String ability_id=knowledge.get("ability_id")==null?null:knowledge.get("ability_id").toString();
                            if(StringUtils.isNotBlank(ability_id)){
                                ability_id=(String)resJdbcTemplate.query("SELECT desc_id FROM syn_table where table_name='bd_ability' and source_id=? ",new Object[]{ability_id},new ResultSetExtractor<Object>() {
                                    @Override
                                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                                        while(rs.next()){
                                            return rs.getString("desc_id");
                                        }
                                        return null;
                                    }
                                });
                            }
                            if(StringUtils.isNotBlank(cognize_level_id)){
                                cognize_level_id=(String)resJdbcTemplate.query("SELECT desc_id FROM syn_table where table_name='bd_cognize_level' and source_id=? ",new Object[]{cognize_level_id},new ResultSetExtractor<Object>() {
                                    @Override
                                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                                        while(rs.next()){
                                            return rs.getString("desc_id");
                                        }
                                        return null;
                                    }
                                });
                            }
                            StringBuffer answersql=new StringBuffer();
                            answersql.append(" insert into ").append(QuestionsTables.ks_question_knowledge.name()).append("  ");
                            answersql.append(" (id,question_id,type,knowledge_id,is_main,cognize_level_id,ability_id) ");
                            answersql.append(" values(?,?,?,?,?,?,?)");
                            Object[] param={destKnowledgechoiceId,destchoiceId,choice,knowledgeId,is_main,cognize_level_id,ability_id};
                            platformJdbcTemplate.update(answersql.toString(), param);
                        }
                        platformJdbcTemplate.update("insert into ks_question_flag(question_id,src_flag,data_bak)values(?,?,?)",new String[]{destchoiceId,Constants.src_flag,null});
                        resJdbcTemplate.update(" insert into "+QuestionsTables.syn_table.name()+"(table_name,source_id,desc_id) values(?,?,?)",new Object[]{QuestionsTables.ks_question_choice.name(),base_id,destchoiceId});
                        logger.info("ks_question_choice .."+choiceId+"导入成功");
                    }
                }
            }
        }
        logger.info("用时:"+(System.currentTimeMillis()-starts));
    }

    public Integer getPageNo(int pageSize){
       Integer total=resJdbcTemplate.queryForObject("select count(1) as total from ks_question_base e,ks_question_choice c where c.base_id=e.id and c.is_small=0 ",Integer.class);
       Integer pageTotal;
        if (total %  pageSize== 0) {
            pageTotal = total / pageSize;
        } else {
            pageTotal = total / pageSize + 1;
        }
        return pageTotal;
    }


    @Override
    public String synChoiceByChoiceId(String id) throws Exception {
        String result="";
        List<Map<String,Object>> list=null;
        if(StringUtils.isNotBlank(id)){
            String srcsql="select  c.id,c.base_id,c.topic,c.analysis,c.is_small,c.is_multi from ks_question_choice c where c.is_small=1 and c.id=?";
            list = resJdbcTemplate.queryForList(srcsql,new Object[]{id});
        }
        if(null!=list && list.size()>0){
            for(Map map:list){
                String choiceId=map.get("id").toString();
                String destchoiceId= IdSequence.nextId();
                result=destchoiceId;
                logger.info("ks_question_choice.."+choiceId+"正在导入");
                String topic = map.get("topic")==null?null:map.get("topic").toString().trim();
                String analysis=map.get("analysis")==null?null:map.get("analysis").toString().trim();
                Boolean is_multi=map.get("is_multi")==null?null:(Boolean)map.get("is_multi");
                String choiceSql="insert into ks_question_choice(id,topic,analysis,base_id,is_small,is_multi,audit_status,status,create_time,create_user_id,create_user_name)values(?,?,?,?,?,?,?,?,?,?,?)";
                platformJdbcTemplate.update(choiceSql,new Object[]{destchoiceId,topic,analysis,destchoiceId,true,is_multi,audit_status,status,new Date(), Constants.user_key,Constants.user_name});
                List<Map<String,Object>> choiceAnswers=resJdbcTemplate.queryForList("select * from "+QuestionsTables.ks_question_choice_answer.name()+" where choice_id=?",choiceId);
                if(choiceAnswers.size()>0){
                    for(Map answer:choiceAnswers){
                        String destAnswerchoiceId= IdSequence.nextId();
                        String topicAnswer = answer.get("topic")==null?null:answer.get("topic").toString().trim();
                        String sort=answer.get("sort")==null?null:answer.get("sort").toString().trim();
                        Boolean correct_answer=answer.get("correct_answer")==null?null:(Boolean)answer.get("correct_answer");
                        StringBuffer answersql=new StringBuffer();
                        answersql.append(" insert into ").append(QuestionsTables.ks_question_choice_answer.name()).append("  ");
                        answersql.append(" (id,choice_id,topic,correct_answer,sort) ");
                        answersql.append(" values(?,?,?,?,?)");
                        Object[] param={destAnswerchoiceId,destchoiceId,topicAnswer,correct_answer,sort};
                        platformJdbcTemplate.update(answersql.toString(), param);
                    }
                }
                logger.info("ks_question_choice small .."+choiceId+"导入成功");
                return result;
            }
        }else {
            logger.error("Synthetical item choice id={} not found",id);
        }
        return null;
    }

    @Override
    public Integer getCount() throws Exception {
        return resJdbcTemplate.queryForObject("select count(1) as total from ks_question_base e,ks_question_choice c where c.base_id=e.id and c.is_small=0 ",Integer.class);
    }

}
