package cn.lamppa.edu.platform.domain;

import java.io.Serializable;

/**
 * Created by heizhiqiang on 2016/2/24 0024.
 */
public class QuestionBase implements Serializable {
    /**
     * primary key
     */
    private String  id;

    /**
     * 难度
     */
    private Integer difficulty;

    /**
     * 年份
     */
    private String year;

    /**
     * 来源
     */
    private String source;

    /**
     * 使用次数
     */
    private Integer useNum;

    /**
     * 正确率
     */
    private String accuracy;


    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getUseNum() {
        return useNum;
    }

    public void setUseNum(Integer useNum) {
        this.useNum = useNum;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}
}
