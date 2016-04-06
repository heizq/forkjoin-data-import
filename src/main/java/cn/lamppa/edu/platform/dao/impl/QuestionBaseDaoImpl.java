package cn.lamppa.edu.platform.dao.impl;

import cn.lamppa.edu.platform.dao.QuestionBaseDao;
import cn.lamppa.edu.platform.domain.QuestionBase;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Created by heizhiqiang on 2016/2/24 0024.
 */
@Configurable
@Repository(value="questionBaseDao")
public class QuestionBaseDaoImpl implements QuestionBaseDao{
    @Resource
    private JdbcTemplate resJdbcTemplate;

    @Resource
    private JdbcTemplate platformJdbcTemplate;

    public int addQuestionBase(QuestionBase model) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",model.getId());
        params.put("difficulty",model.getDifficulty());
        params.put("year",model.getYear());
        params.put("use_num",model.getUseNum());
        params.put("accuracy",model.getAccuracy());
        params.put("source",model.getSource());
        params.put("type",model.getType());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(platformJdbcTemplate);
        insert.withTableName("ks_question_base");
        int num =  insert.execute(params);

        return num;
    }

    public QuestionBase findQuestionBase(String id) {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT  id, difficulty, YEAR, source, use_num, accuracy, TYPE FROM  ks_question_base  where id = ?");
        List<QuestionBase> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{id},
                new RowMapper<QuestionBase>() {
                    public QuestionBase mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionBase model = new QuestionBase();
                        model.setId(rs.getString("id"));
                        model.setYear(rs.getString("year"));
                        model.setType(rs.getString("type"));
                        model.setUseNum(rs.getInt("use_num"));
                        model.setSource(rs.getString("source"));
                        model.setAccuracy(rs.getString("accuracy"));
                        model.setDifficulty(rs.getInt("difficulty"));
                        return model;
                    }
                });

        if(list!=null &&list.size()>0){
            return list.get(0);
        }else {
            return null;
        }
    }
}
