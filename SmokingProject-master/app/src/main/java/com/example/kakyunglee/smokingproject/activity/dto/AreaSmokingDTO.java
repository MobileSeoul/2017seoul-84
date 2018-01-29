package com.example.kakyunglee.smokingproject.activity.dto;

// 미정이라서 할 수 없음
public class AreaSmokingDTO {
    private int id;					// pk
    private String gu;				// 해당 구
    private String classification;	// 장소 구분 (개방형, 완전 폐쇄형...)
    private String detail_address;	// 상세 주소
    private double size;			// 장소 사이즈
    private String installation;	// 설치 장소
    private String administration;	// 관리처
    private String latitude;	// 위도
    private String logitude;	// 경도
    private String address;			// 상세 주소


    ///////////////// getter, setter ////////////////


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getGu() {
        return gu;
    }
    public void setGu(String gu) {
        this.gu = gu;
    }
    public String getClassification() {
        return classification;
    }
    public void setClassification(String classification) {
        this.classification = classification;
    }
    public String getDetail_address() {
        return detail_address;
    }
    public void setDetail_address(String detail_address) {
        this.detail_address = detail_address;
    }
    public double getSize() {
        return size;
    }
    public void setSize(double size) {
        this.size = size;
    }
    public String getInstallation() {
        return installation;
    }
    public void setInstallation(String installation) {
        this.installation = installation;
    }
    public String getAdministration() {
        return administration;
    }
    public void setAdministration(String administration) {
        this.administration = administration;
    }
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLogitude() {
        return logitude;
    }
    public void setLogitude(String logitude) {
        this.logitude = logitude;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    ////////////////// toString //////////////////


    @Override
    public String toString() {
        return "AreaSmokingDTO [id=" + id + ", gu=" + gu + ", classification=" + classification + ", detail_address="
                + detail_address + ", size=" + size + ", installation=" + installation + ", administration="
                + administration + ", latitude=" + latitude + ", logitude=" + logitude + ", address=" + address + "]";
    }



}
