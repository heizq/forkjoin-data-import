package cn.lamppa.edu.platform.dao.impl;

import cn.lamppa.edu.platform.dao.QuestionJudgeDao;
import cn.lamppa.edu.platform.dao.QuestionKnowledgeDao;
import cn.lamppa.edu.platform.domain.QuestionBase;
import cn.lamppa.edu.platform.domain.QuestionFilling;
import cn.lamppa.edu.platform.domain.QuestionJudge;
import cn.lamppa.edu.platform.domain.QuestionKnowledge;
import cn.lamppa.edu.platform.enums.QuestionAuditStatus;
import cn.lamppa.edu.platform.enums.QuestionStatus;
import cn.lamppa.edu.platform.enums.QuestionType;
import cn.lamppa.edu.platform.util.IdSequence;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by zhangyun on 2016/2/25 0024.
 */
@Configurable
@Repository(value="questionJudgeDao")
public class QuestionJudgeDaoImpl implements QuestionJudgeDao {
    @Resource
    private JdbcTemplate resJdbcTemplate;

    @Resource
    private JdbcTemplate platformJdbcTemplate;

    public int addQuestion(QuestionJudge model) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",model.getId());
        params.put("topic",model.getTopic());
        params.put("is_ok",model.getIsOk());
        params.put("analysis",model.getAnalysis());
        params.put("base_id",model.getBaseId());
        params.put("is_small",model.getIsSmall());
        params.put("audit_status",model.getAuditStatus());
        params.put("status",model.getStatus());
        params.put("create_time",model.getCreateTime());
        params.put("create_user_id",model.getCreateUserId());
        params.put("create_user_name",model.getCreateUserName());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(platformJdbcTemplate);

        insert.withTableName("ks_question_judge");
        int num =  insert.execute(params);
        return num;
    }

    public List<QuestionJudge> findQuestionJudges(String questionId,Boolean  is_small) {
        StringBuilder sql = new StringBuilder("");
        List<Object> params = new ArrayList<Object>();
        params.add(is_small);
        sql.append("SELECT  id,  topic,  analysis, is_ok, base_id,  is_small,  audit_status,  audit_time,  audit_user_id, ");
        sql.append(" audit_opinion,  STATUS,  create_time,  create_user_id,  audit_user_name,  create_user_name  ");
        sql.append(" FROM  ks_question_judge where is_small = ?");
        if(StringUtils.isNotEmpty(questionId)){
            sql.append(" and id=?");
            params.add(questionId);
        }
        List<QuestionJudge> list = this.resJdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                new RowMapper<QuestionJudge>() {
                    public QuestionJudge mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionJudge model = new QuestionJudge();
                        model.setId(rs.getString("id"));
                        model.setTopic(rs.getString("topic"));
                        model.setIsOk(rs.getBoolean("is_ok"));
                        model.setAnalysis(rs.getString("analysis"));
                        model.setBaseId(rs.getString("base_id"));
                        model.setIsSmall(rs.getBoolean("is_small"));
                        model.setAuditStatus(rs.getString("audit_status"));
                        model.setAuditTime(rs.getTime("audit_time"));
                        model.setAuditUserId(rs.getString("audit_user_id"));
                        model.setAuditOpinion(rs.getString("audit_opinion"));
                        model.setStatus(rs.getString("STATUS"));
                        model.setCreateTime(rs.getTime("create_time"));
                        model.setCreateUserId(rs.getString("create_user_id"));
                        model.setCreateUserName(rs.getString("create_user_name"));

                        return model;
                    }
                });

        return list;
    }

    @Override
    public List<QuestionJudge> findJudgeByPage(int start, int end) {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT  id,  topic,  analysis, is_ok, base_id,  is_small,  audit_status,  audit_time,  audit_user_id, ");
        sql.append(" audit_opinion,  STATUS,  create_time,  create_user_id,  audit_user_name,  create_user_name  ");
        sql.append(" FROM  ks_question_judge where is_small = false limit ?,? ");
        List<QuestionJudge> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{start,end},
                new RowMapper<QuestionJudge>() {
                    public QuestionJudge mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionJudge model = new QuestionJudge();
                        model.setId(rs.getString("id"));
                        model.setTopic(rs.getString("topic"));
                        model.setIsOk(rs.getBoolean("is_ok"));
                        model.setAnalysis(rs.getString("analysis"));
                        model.setBaseId(rs.getString("base_id"));
                        model.setIsSmall(rs.getBoolean("is_small"));
                        model.setAuditStatus(rs.getString("audit_status"));
                        model.setAuditTime(rs.getTime("audit_time"));
                        model.setAuditUserId(rs.getString("audit_user_id"));
                        model.setAuditOpinion(rs.getString("audit_opinion"));
                        model.setStatus(rs.getString("STATUS"));
                        model.setCreateTime(rs.getTime("create_time"));
                        model.setCreateUserId(rs.getString("create_user_id"));
                        model.setCreateUserName(rs.getString("create_user_name"));

                        return model;
                    }
                });

        return list;
    }

    @Override
    public int count() {
        StringBuffer sql=new StringBuffer();
        sql.append("SELECT  count(1) ");
        sql.append(" FROM  ks_question_judge where is_small= false");
        int count = this.resJdbcTemplate.queryForObject(sql.toString(),Integer.class);
        return count;
    }
}
