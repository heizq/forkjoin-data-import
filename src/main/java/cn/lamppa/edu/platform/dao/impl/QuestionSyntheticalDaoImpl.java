package cn.lamppa.edu.platform.dao.impl;

import cn.lamppa.edu.platform.dao.QuestionSyntheticalDao;
import cn.lamppa.edu.platform.domain.QuestionFillingAnswer;
import cn.lamppa.edu.platform.domain.QuestionSynthetical;
import cn.lamppa.edu.platform.domain.QuestionSyntheticalItem;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heizhiqiang on 2016/2/29 0029.
 */
@Configurable
@Repository(value="questionSyntheticalDao")
public class QuestionSyntheticalDaoImpl implements QuestionSyntheticalDao {

    @Resource
    private JdbcTemplate resJdbcTemplate;

    @Resource
    private JdbcTemplate platformJdbcTemplate;

    public String addQuestion(QuestionSynthetical model) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",model.getId());
        params.put("topic",model.getTopic());
        params.put("base_id",model.getBaseId());
        params.put("audit_status",model.getAuditStatus());
        params.put("status",model.getStatus());
        params.put("create_time",model.getCreateTime());
        params.put("create_user_id",model.getCreateUserId());
        params.put("create_user_name",model.getCreateUserName());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(platformJdbcTemplate);

        insert.withTableName("ks_question_synthetical");
        insert.execute(params);
        return model.getId();
    }

    public String addItem(QuestionSyntheticalItem model) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",model.getId());
        params.put("synthetical_id",model.getSyntheticalId());
        params.put("question_id",model.getQuestionId());
        params.put("TYPE",model.getType());
        params.put("sort",model.getSort());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(platformJdbcTemplate);

        insert.withTableName("ks_question_synthetical_item");
        insert.execute(params);
        return model.getId();
    }

    public List<QuestionSynthetical> findQuestionSynthetical() {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT   id,  topic,  base_id,  audit_status,  audit_time,  audit_user_id,  STATUS,  audit_opinion,  create_time,");
        sql.append("  create_user_id,  audit_user_name,  create_user_name FROM  ks_question_synthetical ");
        List<QuestionSynthetical> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{},
                new RowMapper<QuestionSynthetical>() {
                    public QuestionSynthetical mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionSynthetical model = new QuestionSynthetical();
                        model.setId(rs.getString("id"));
                        model.setTopic(rs.getString("topic"));
                        model.setBaseId(rs.getString("base_id"));
                        model.setAuditStatus(rs.getString("audit_status"));
                        model.setAuditTime(rs.getTime("audit_time"));
                        model.setAuditUserId(rs.getString("audit_user_id"));
                        model.setAuditUserName(rs.getString("audit_user_name"));
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

    public List<QuestionSyntheticalItem> findItems(String questionId) {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT   id,  synthetical_id,  question_id,  TYPE,  sort FROM ks_question_synthetical_item ");
        sql.append("  where synthetical_id=? ");
        List<QuestionSyntheticalItem> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{questionId},
                new RowMapper<QuestionSyntheticalItem>() {
                    public QuestionSyntheticalItem mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionSyntheticalItem model = new QuestionSyntheticalItem();
                        model.setId(rs.getString("id"));
                        model.setQuestionId(rs.getString("question_id"));
                        model.setSyntheticalId(rs.getString("synthetical_id"));
                        model.setType(rs.getString("type"));
                        model.setSort(rs.getString("sort"));
                        return model;
                    }
                });
        return list;
    }

    @Override
    public List<QuestionSynthetical> findQuestionByPage(int start, int end) {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT   id,  topic,  base_id,  audit_status,  audit_time,  audit_user_id,  STATUS,  audit_opinion,  create_time,");
        sql.append("  create_user_id,  audit_user_name,  create_user_name FROM  ks_question_synthetical limit ?,?");
        List<QuestionSynthetical> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{start,end},
                new RowMapper<QuestionSynthetical>() {
                    public QuestionSynthetical mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionSynthetical model = new QuestionSynthetical();
                        model.setId(rs.getString("id"));
                        model.setTopic(rs.getString("topic"));
                        model.setBaseId(rs.getString("base_id"));
                        model.setAuditStatus(rs.getString("audit_status"));
                        model.setAuditTime(rs.getTime("audit_time"));
                        model.setAuditUserId(rs.getString("audit_user_id"));
                        model.setAuditUserName(rs.getString("audit_user_name"));
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
    public int getCount() {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT  count(*) ");
        sql.append(" FROM  ks_question_synthetical ");

        int count = this.resJdbcTemplate.queryForObject(sql.toString(),Integer.class);
        return count;
    }


}
