package com.infix.phukiencongnghe.data.dto.response;

public class ReviewDTO {
    private Integer id;
    private String name;
    private Integer numStar;
    private String evolute;
    private String evaluateDate;

    public ReviewDTO(Integer id, String name, Integer numStar, String evolute, String evaluateDate) {
        this.id = id;
        this.name = name;
        this.numStar = numStar;
        this.evolute = evolute;
        this.evaluateDate = evaluateDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumStar() {
        return numStar;
    }

    public void setNumStar(Integer numStar) {
        this.numStar = numStar;
    }

    public String getEvolute() {
        return evolute;
    }

    public void setEvolute(String evolute) {
        this.evolute = evolute;
    }

    public String getEvaluateDate() {
        return evaluateDate;
    }

    public void setEvaluateDate(String evaluateDate) {
        this.evaluateDate = evaluateDate;
    }
}

