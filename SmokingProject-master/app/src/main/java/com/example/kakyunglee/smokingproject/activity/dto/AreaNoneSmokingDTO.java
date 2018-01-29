package com.example.kakyunglee.smokingproject.activity.dto;

import java.math.BigDecimal;

// 미정이라서 할 수 없음
public class AreaNoneSmokingDTO {
    private int id;				// primary key
    private String name;		// place name
    private String range;		// place range
    private String city;		// 어느 시인지
    private String gu;			// 어느 구인지
    private String detail_district;		// 장소분류
    private String basis;				// 기본 법령
    private String area;				// ?
    private String fine;				// 벌금
    private String phone;				// 전화번호
    private String address_road;		// 도로명 주소
    private String address_loc;			// 일반 주소
    private String management_agency;	// 관련 부처
    private String latitude;		// 위도
    private String longitude;		// 경도
    private String updated_at;			// 업데이트 된 날



    /////////// getter, setter /////////////

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRange() {
        return range;
    }
    public void setRange(String range) {
        this.range = range;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getGu() {
        return gu;
    }
    public void setGu(String gu) {
        this.gu = gu;
    }
    public String getDetail_district() {
        return detail_district;
    }
    public void setDetail_district(String detail_district) {
        this.detail_district = detail_district;
    }
    public String getBasis() {
        return basis;
    }
    public void setBasis(String basis) {
        this.basis = basis;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getFine() {
        return fine;
    }
    public void setFine(String fine) {
        this.fine = fine;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress_road() {
        return address_road;
    }
    public void setAddress_road(String address_road) {
        this.address_road = address_road;
    }
    public String getAddress_loc() {
        return address_loc;
    }
    public void setAddress_loc(String address_loc) {
        this.address_loc = address_loc;
    }
    public String getManagement_agency() {
        return management_agency;
    }
    public void setManagement_agency(String management_agency) {
        this.management_agency = management_agency;
    }
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getUpdated_at() {
        return updated_at;
    }
    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }


    ////////// toString ///////////

    @Override
    public String toString() {
        return "AreaSmokingDTO [id=" + id + ", name=" + name + ", range=" + range + ", city=" + city + ", gu=" + gu
                + ", detail_district=" + detail_district + ", basis=" + basis + ", area=" + area + ", fine=" + fine
                + ", phone=" + phone + ", address_road=" + address_road + ", address_loc=" + address_loc
                + ", management_agency=" + management_agency + ", latitude=" + latitude + ", longitude=" + longitude
                + ", updated_at=" + updated_at + "]";
    }


}
