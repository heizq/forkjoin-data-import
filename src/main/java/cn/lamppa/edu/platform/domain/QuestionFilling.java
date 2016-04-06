package cn.lamppa.edu.platform.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xufeng on 15-12-16.
 **/
public class QuestionFilling extends QuestionBase implements Serializable{

    //填空题Id
    private  String id;
    //填空题题干
    private  String topic;
    //填空题解析
    private  String analysis;
    //基础表Id
    private  String baseId;
    //是否小题
    private  Boolean isSmall;
    //审核状态
    private  String auditStatus;
    //审核时间
    private  Date auditTime;
    //审核人
    private  String auditUserId;
    //审核人名称
    private String auditUserName;
    //试题状态
    private  String   status;
    //录入时间
    private  Date createTime;
    //录入人Id
    private  String createUserId;
    //录入人名称
    private String createUserName;
    //审核意见
    private  String auditOpinion;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    public Boolean getIsSmall() {
        return isSmall;
    }

    public void setIsSmall(Boolean isSmall) {
        this.isSmall = isSmall;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditUserId() {
        return auditUserId;
    }

    public void setAuditUserId(String auditUserId) {
        this.auditUserId = auditUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion;
    }

    public String getAuditUserName() {
        return auditUserName;
    }

    public void setAuditUserName(String auditUserName) {
        this.auditUserName = auditUserName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
}
