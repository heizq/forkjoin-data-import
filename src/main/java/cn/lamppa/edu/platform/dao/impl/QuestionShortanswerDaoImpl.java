package cn.lamppa.edu.platform.dao.impl;

import cn.lamppa.edu.platform.dao.QuestionShortanswerDao;
import cn.lamppa.edu.platform.domain.QuestionFilling;
import cn.lamppa.edu.platform.domain.QuestionShortanswer;
import cn.lamppa.edu.platform.domain.Textbook;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/25.
 */
@Configurable
@Repository(value="questionShortanswerDao")
public class QuestionShortanswerDaoImpl implements QuestionShortanswerDao {
    @Resource
    private JdbcTemplate resJdbcTemplate;

    @Resource
    private JdbcTemplate platformJdbcTemplate;

    public String addQuestionShortanswer(QuestionShortanswer questionShortanswer) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",questionShortanswer.getId());
        params.put("topic",questionShortanswer.getTopic());
        params.put("analysis",questionShortanswer.getAnalysis());
        params.put("answer",questionShortanswer.getAnswer());
        params.put("base_id",questionShortanswer.getBaseId());
        params.put("is_small",questionShortanswer.getIsSmall());
        params.put("audit_status",questionShortanswer.getAuditStatus());
        params.put("audit_time",questionShortanswer.getAuditTime());
        params.put("audit_user_id",questionShortanswer.getAuditUserId());
        params.put("audit_opinion",questionShortanswer.getAuditOpinion());
        params.put("audit_user_name",questionShortanswer.getAuditUserName());
        params.put("status",questionShortanswer.getStatus());
        params.put("create_time",questionShortanswer.getCreateTime());
        params.put("create_user_id",questionShortanswer.getCreateUserId());
        params.put("create_user_name", questionShortanswer.getCreateUserName());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(platformJdbcTemplate);
        insert.withTableName("ks_question_shortanswer");
        insert.execute(params);
        return questionShortanswer.getId();

    }

    public List<QuestionShortanswer> findQuestionShortanswerById(String questionId){

        StringBuilder sql = new StringBuilder("");
        sql.append(" select id,topic,analysis,answer,base_id,is_small,audit_status,audit_time,audit_user_id,audit_opinion " );
        sql.append(" audit_user_name,status,create_time,create_user_id,create_user_name from ks_question_shortanswer ");
        sql.append(" where id = ? and  is_small = ?");
        List<QuestionShortanswer> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{questionId,false},
                new RowMapper<QuestionShortanswer>() {
                    public QuestionShortanswer mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionShortanswer model = new QuestionShortanswer();
                        model.setId(rs.getString("id"));
                        model.setTopic(rs.getString("topic"));
                        model.setAnalysis(rs.getString("analysis"));
                        model.setAnswer(rs.getString("answer"));
                        model.setBaseId(rs.getString("base_id"));
                        model.setIsSmall(rs.getBoolean("is_small"));
                        model.setAuditStatus(rs.getString("audit_status"));
                        model.setAuditTime(rs.getDate("audit_time"));
                        model.setAuditUserId(rs.getString("audit_user_id"));
                        model.setAuditUserName(rs.getString("audit_user_name"));
                        model.setStatus(rs.getString("status"));
                        model.setCreateTime(rs.getDate("create_time"));
                        model.setCreateUserId(rs.getString("create_user_id"));
                        model.setCreateUserName(rs.getString("create_user_name"));
                        return model;
                    }
                });
        if(list!=null &&list.size()>0){
            return list;
        }else {
            return null;
        }
    }

    public List<QuestionShortanswer> findQuestionShortanswer(String questionId,Boolean isSmall) {
        StringBuilder sql = new StringBuilder("");
        List<Object> params = new ArrayList<Object>();
        params.add(isSmall);
        sql.append(" select id,topic,analysis,answer,base_id,is_small,audit_status,audit_time,audit_user_id,audit_opinion " );
        sql.append(" audit_user_name,status,create_time,create_user_id,create_user_name from ks_question_shortanswer ");
        sql.append(" where is_small = ?");

        if(StringUtils.isNotEmpty(questionId)){
            sql.append(" and id=?");
            params.add(questionId);
        }
        List<QuestionShortanswer> list = this.resJdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                new RowMapper<QuestionShortanswer>() {
                    public QuestionShortanswer mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionShortanswer model = new QuestionShortanswer();
                        model.setId(rs.getString("id"));
                        model.setTopic(rs.getString("topic"));
                        model.setAnalysis(rs.getString("analysis"));
                        model.setAnswer(rs.getString("answer"));
                        model.setBaseId(rs.getString("base_id"));
                        model.setIsSmall(rs.getBoolean("is_small"));
                        model.setAuditStatus(rs.getString("audit_status"));
                        model.setAuditTime(rs.getDate("audit_time"));
                        model.setAuditUserId(rs.getString("audit_user_id"));
                        model.setAuditUserName(rs.getString("audit_user_name"));
                        model.setStatus(rs.getString("status"));
                        model.setCreateTime(rs.getDate("create_time"));
                        model.setCreateUserId(rs.getString("create_user_id"));
                        model.setCreateUserName(rs.getString("create_user_name"));
                        return model;
                    }
                });

        return list;
    }

    @Override
    public int getCount() {
        StringBuffer sql=new StringBuffer();
        sql.append(" SELECT  count(1) ");
        sql.append(" FROM  ks_question_shortanswer where is_small= false");
        return this.resJdbcTemplate.queryForObject(sql.toString(),Integer.class);
    }

    @Override
    public List<QuestionShortanswer> findShortanswerByPage(int start, int end) {
        StringBuilder sql = new StringBuilder("");
        sql.append(" select id,topic,analysis,answer,base_id,is_small,audit_status,audit_time,audit_user_id,audit_opinion " );
        sql.append(" audit_user_name,status,create_time,create_user_id,create_user_name from ks_question_shortanswer ");
        sql.append(" where is_small = false limit ?,? ");
        List<QuestionShortanswer> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{start,end},
                new RowMapper<QuestionShortanswer>() {
                    public QuestionShortanswer mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionShortanswer model = new QuestionShortanswer();
                        model.setId(rs.getString("id"));
                        model.setTopic(rs.getString("topic"));
                        model.setAnalysis(rs.getString("analysis"));
                        model.setAnswer(rs.getString("answer"));
                        model.setBaseId(rs.getString("base_id"));
                        model.setIsSmall(rs.getBoolean("is_small"));
                        model.setAuditStatus(rs.getString("audit_status"));
                        model.setAuditTime(rs.getDate("audit_time"));
                        model.setAuditUserId(rs.getString("audit_user_id"));
                        model.setAuditUserName(rs.getString("audit_user_name"));
                        model.setStatus(rs.getString("status"));
                        model.setCreateTime(rs.getDate("create_time"));
                        model.setCreateUserId(rs.getString("create_user_id"));
                        model.setCreateUserName(rs.getString("create_user_name"));
                        return model;
                    }
                });
        return list;
    }


}
