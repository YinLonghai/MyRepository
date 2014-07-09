package com.guotion.sicilia.bean.net;

import java.io.Serializable;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-4-20
 * Time: 下午2:38
 * To change this template use File | Settings | File Templates.
 */
@DatabaseTable(tableName="userInfo")
public class User implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -5288639174755764502L;
	@DatabaseField(generatedId=true)
	public int id;
	@DatabaseField
	public String _id = "";
    public String __v = "0";
    @DatabaseField
    public String userName = "";
    public String passWord = "";
    public String birthday = ""; //出生日期 格式:"1984-02-01"
    public String lunarBtd = ""; //农历生日 格式:"12-30","1-3"
    public String gender = "男";
    public String online = "0";//在先状态
    public String nickName = "";
    @DatabaseField
    public String headPhoto = ""; //头像地址
    public String authorized = "0"; //是否通过申请
    public String accountState = "0";//冻结时候为1，正常为0
    public String personalPreferences = "";//个人喜好
    public String attribution = "";//家庭
    public String vestingDepartment = "";//归属部门  移除
    public String job = "";//职位
    public String capacity = "1073741824";//个人空间容量  剩下
    public String level = "1";// 0 - Admin,1 - Normal
    public String mobile = "";//     新增，预留字段
    public String mail = "";// 新增
    /**
     *   SignatureHistory类型或者String类型
     */
    public Object signature;
    /**
     * ChatGroup类型或者是String类型
     */
    public List<Object> chatGroups;
    public User(){}

    public User(String _id, String __v, String userName, String passWord, String birthday, String lunarBtd,
                String gender, String online, String nickName, String headPhoto, String authorized,
                String accountState, String personalPreferences, String attribution, String vestingDepartment,
                String job, String capacity, String level, String mobile, String mail, Object signature, List<Object> chatGroups) {
        this._id = _id;
        this.__v = __v;
        this.userName = userName;
        this.passWord = passWord;
        this.birthday = birthday;
        this.lunarBtd = lunarBtd;
        this.gender = gender;
        this.online = online;
        this.nickName = nickName;
        this.headPhoto = headPhoto;
        this.authorized = authorized;
        this.accountState = accountState;
        this.personalPreferences = personalPreferences;
        this.attribution = attribution;
        this.vestingDepartment = vestingDepartment;
        this.job = job;
        this.capacity = capacity;
        this.level = level;
        this.mobile = mobile;
        this.mail = mail;
        this.signature = signature;
        this.chatGroups = chatGroups;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get__v() {
        return __v;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLunarBtd() {
        return lunarBtd;
    }

    public void setLunarBtd(String lunarBtd) {
        this.lunarBtd = lunarBtd;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(String headPhoto) {
        this.headPhoto = headPhoto;
    }

    public String getAuthorized() {
        return authorized;
    }

    public void setAuthorized(String authorized) {
        this.authorized = authorized;
    }

    public String getAccountState() {
        return accountState;
    }

    public void setAccountState(String accountState) {
        this.accountState = accountState;
    }

    public String getPersonalPreferences() {
        return personalPreferences;
    }

    public void setPersonalPreferences(String personalPreferences) {
        this.personalPreferences = personalPreferences;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public String getVestingDepartment() {
        return vestingDepartment;
    }

    public void setVestingDepartment(String vestingDepartment) {
        this.vestingDepartment = vestingDepartment;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Object getSignature() {
        return signature;
    }

    public void setSignature(Object signature) {
        this.signature = signature;
    }

    public List<Object> getChatGroups() {
        return chatGroups;
    }

    public void setChatGroups(List<Object> chatGroups) {
        this.chatGroups = chatGroups;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", __v='" + __v + '\'' +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", birthday='" + birthday + '\'' +
                ", lunarBtd='" + lunarBtd + '\'' +
                ", gender='" + gender + '\'' +
                ", online='" + online + '\'' +
                ", nickName='" + nickName + '\'' +
                ", headPhoto='" + headPhoto + '\'' +
                ", authorized='" + authorized + '\'' +
                ", accountState='" + accountState + '\'' +
                ", personalPreferences='" + personalPreferences + '\'' +
                ", attribution='" + attribution + '\'' +
                ", vestingDepartment='" + vestingDepartment + '\'' +
                ", job='" + job + '\'' +
                ", capacity='" + capacity + '\'' +
                ", level='" + level + '\'' +
                ", mobile='" + mobile + '\'' +
                ", mail='" + mail + '\'' +
                ", signature=" + signature +
                ", chatGroups=" + chatGroups +
                '}';
    }
}
