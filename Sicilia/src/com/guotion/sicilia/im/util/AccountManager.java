package com.guotion.sicilia.im.util;

import android.annotation.SuppressLint;

import com.guotion.sicilia.bean.net.Device;
import com.guotion.sicilia.bean.net.User;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.util.LogUtil;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.EncodingUtils;

/**
 * Created with IntelliJ IDEA.
 * User: lizhengbin
 * Date: 14-4-16
 * Time: 下午5:46
 * To change this template use File | Settings | File Templates.
 */
@SuppressLint("NewApi")
public class AccountManager {
	private User userObjct = new User();
	private final String CHARSET = "ISO-8859-1";
	private String cusException = "N:01";
    /**
     * 创建帐号
     * @param userName
     * @param passWord
     * @param birthday
     * @param lundarBtd
     * @param gender
     * @param nickName
     * @param mail
     * @param attribution
     * @param mobile
     * @return 刚创建帐号 chatGroups是空数组
     * @throws Exception
     */
	public User createAccount(String userName, String passWord, String birthday, String lundarBtd,
                              String gender, String nickName, String mail, String attribution, String mobile) throws Exception{
        StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/User/Create");
        url.append("?");
        url.append("userName="+EncodingUtils.getString(userName.getBytes(), CHARSET)+"&");
        url.append("passWord="+URLEncoder.encode(passWord,CHARSET)+"&");
        url.append("birthday=" + birthday + "&");
        url.append("lunarBtd=" + lundarBtd + "&");
        url.append("gender=" + gender + "&");
        url.append("nickName=" +EncodingUtils.getString(nickName.getBytes(), CHARSET)+"&");
        url.append("mail=" + URLEncoder.encode(mail,CHARSET) + "&");
        url.append("attribution=" +EncodingUtils.getString(attribution.getBytes(), CHARSET)+"&");
        url.append("mobile=" + URLEncoder.encode(mobile,CHARSET));
        
        byte[] result = RequestSender.requestByGet(url.toString());
        if(result == null)
        	throw new Exception(cusException);
        return (User) GsonTransferUtil.transferToObject(new String(result), userObjct);
    }
    /**
     * 
     * @param userName
     * @param passWord
     * @return   1.chatGroups 是ChatGroup的对象数组,chatGroups中的members和admins是String的数组
     * 			  2.signatures是对象
     * @throws Exception
     */
    public User login(String userName, String passWord, OnGetUserListener l) throws Exception {
        StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/User/Login");
        url.append("?");
        url.append("userName=" +EncodingUtils.getString(userName.getBytes(), CHARSET)+"&");
        url.append("passWord=" + URLEncoder.encode(passWord, CHARSET));
        byte[] result = RequestSender.requestByGet(url.toString());
        if(result == null)
        	throw new Exception(cusException);
        String userJson = new String(result);
        LogUtil.i("login user=" + userJson);
        User user = (User) GsonTransferUtil.transferToObject(userJson, userObjct);
        if (l != null) {
			l.onGetUser(user, userJson);
		}
        return user;
    }

    /**
     * @param userName   要授权的用户帐号
     * @param passWord   要授权的用户帐号的密码
     * @param level      0为管理员权限 ，1为普通用户权限
     * @param authorized 0为不通过，1为通过
     * @param admin      管理员帐号
     * @return   刚授权通过，chatGroup为空数组
     * @throws Exception
     */
    public User authorized(String userName, String passWord, String level, String authorized, String admin) throws Exception {
        StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/User/Authorized");
        url.append("?");
        url.append("userName=" +EncodingUtils.getString(userName.getBytes(), CHARSET)+ "&");
        url.append("passWord=" +URLEncoder.encode(passWord, CHARSET)+ "&");
        url.append("level=" + level + "&");
        url.append("authorized=" + authorized + "&");
        url.append("admin=" +admin);
        byte[] result = RequestSender.requestByGet(url.toString());
        if(result == null)
        	throw new Exception(cusException);
        return (User) GsonTransferUtil.transferToObject(new String(result), userObjct);
    }

    /**
     * 获取用户信息
     *
     * @param userName 用户名
     * @return  1.chatGroups是String的数组
     * 			 2.signature是String
     * @throws Exception
     */
    public User getUser(String userName) throws Exception {
        StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/User/Forget");
        url.append("?");
        url.append("userName="+EncodingUtils.getString(userName.getBytes(), CHARSET));//EncodingUtils.getString(userName.getBytes(), CHARSET)
        byte[] result = RequestSender.requestByGet(url.toString());
        if(result == null)
        	throw new Exception(cusException);
        return (User) GsonTransferUtil.transferToObject(new String(result), userObjct);
    }

    /**
     * 获取所有用户列表
     *
     * @return  1.chatGroups 是String的数组
     * 	         2.signature是对象
     * @throws Exception
     */
    public List<User> getUserList() throws Exception {
        StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/User/List");
        byte[] result = RequestSender.requestByGet(url.toString());
        if(result == null)
        	throw new Exception(cusException);
        return (List) GsonTransferUtil.transferToArray(new String(result), userObjct);
    }

    /**
     * 删除用户
     *
     * @param id_
     * @param admin
     * @return
     * @throws Exception
     */
    public boolean deleteUser(String id_, String admin) throws Exception {
        StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/User/Delete");
        url.append("?");
        url.append("id_=" + id_ + "&");
        url.append("admin=" +admin);
        byte[] result = RequestSender.requestByGet(url.toString());
        if(result == null)
        	throw new Exception(cusException);
        return GsonTransferUtil.transferToBoolean(new String(result));
    }

    /**
     * 修改资料(要修改头像)
     *
     * @param userId
     * @param passWord 可选
     * @param birthday 可选
     * @param lunarBtd 可选
     * @param gender 可选
     * @param nickName 可选
     * @param signature 可选
     * @param attribution 可选
     * @param persionalPreferences 可选
     * @param vestingDepartment 可选
     * @param mediaType
     * @param media
     * @return   1.signature是对象
     */
    public User updateUserInfo(String userId, String passWord, String birthday, String lunarBtd, String gender, String nickName,
                               String signature, String attribution, String personalPreferences, String vestingDepartment,
                               String mediaType, String job,File media, OnGetUserListener l) throws Exception {
        StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/User/Update");
        Map<String, String> params = new HashMap<String, String>();
        Map<String, File> fileMap = new HashMap<String, File>();
        params.put("id_", userId);
        if(birthday!=null && !birthday.isEmpty())
        	params.put("birthday", birthday);
        if(lunarBtd!=null && !lunarBtd.isEmpty())
        	params.put("lunarBtd", lunarBtd);
        if(gender!=null && !gender.isEmpty())
        	params.put("gender", gender);
        if(nickName!=null && !nickName.isEmpty())
        	params.put("nickName", nickName);
        if(signature!=null && !signature.isEmpty())
        	params.put("signature",signature);
        if(attribution!=null && !attribution.isEmpty())
        	params.put("attribution", attribution);
        if(personalPreferences!=null && !personalPreferences.isEmpty())
        	params.put("personalPreferences", personalPreferences);
        if(vestingDepartment!=null && !vestingDepartment.isEmpty())
        	params.put("vestingDepartment", vestingDepartment);
        if(job!=null && !job.isEmpty())
        	params.put("job", job);
        params.put("mediaType", mediaType);
        fileMap.put("media", media);
        byte[] result = RequestSender.requestByPostWithFile(url.toString(), params, fileMap);
        if(result == null)
        	throw new Exception(cusException);
        String json = new String(result);
        LogUtil.i("update user=" + json);
        User user = (User) GsonTransferUtil.transferToObject(json,userObjct);
        if (l != null) {
			l.onGetUser(user, json);
		}
        return user;
    }

    /**
     * 修改资料(不修改头像)
     *
     * @param userId
     * @param passWord 可选
     * @param birthday 可选
     * @param lunarBtd 可选
     * @param gender 可选
     * @param nickName 可选
     * @param signature 可选
     * @param attribution 可选
     * @param persionalPreferences 可选
     * @param vestingDepartment 可选
     * @return
     */
    public User updateUserInfo(String userId, String passWord, String birthday, String lunarBtd, 
    		String gender, String nickName, String signature, String attribution,
    		String personalPreferences, String vestingDepartment,String job, OnGetUserListener l) throws Exception {
        StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/User/Update");
        Map<String, String> params = new HashMap<String, String>();
        params.put("id_", userId);
        if(birthday!=null && !birthday.isEmpty())
        	params.put("birthday", birthday);
        if(lunarBtd!=null && !lunarBtd.isEmpty())
        	params.put("lunarBtd", lunarBtd);
        if(gender!=null && !gender.isEmpty())
        	params.put("gender", gender);
        if(nickName!=null && !nickName.isEmpty())
        	params.put("nickName", nickName);
        if(signature!=null && !signature.isEmpty())
        	params.put("signature",signature);
        if(attribution!=null && !attribution.isEmpty())
        	params.put("attribution", attribution);
        if(personalPreferences!=null && !personalPreferences.isEmpty())
        	params.put("personalPreferences", personalPreferences);
        if(vestingDepartment!=null && !vestingDepartment.isEmpty())
        	params.put("vestingDepartment", vestingDepartment);
        if(job!=null && !job.isEmpty())
        	params.put("job", job);
        byte[] result = RequestSender.requestByPostNoFile(url.toString(), params, CHARSET);
        if(result == null)
        	throw new Exception(cusException);
        String json = new String(result);
        LogUtil.i("update simple =" + json);
        User user = (User) GsonTransferUtil.transferToObject(json, userObjct);
        if (l != null) {
			l.onGetUser(user, json);
		}
        return user;
    }

    /**
     * 冻结帐号
     *
     * @param frozenId
     * @param adminId
     * @return chatGroups是String的数组
     * @throws Exception
     */
    public User frozenAccount(String frozenId, String adminId) throws Exception {
        StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/User/Frozen");
        url.append("?");
        url.append("id_=" + frozenId + "&");
        url.append("admin=" + adminId);
        byte[] result = RequestSender.requestByGet(url.toString());
        if(result == null)
        	throw new Exception(cusException);
        return (User) GsonTransferUtil.transferToObject(new String(result), userObjct);
    }

    /**
     * 解冻帐号
     *
     * @param unFrozenId  要被解冻的帐号
     * @param admin       管理员帐号
     * @param newPassword 新密码
     * @return chatGroups是String的数组
     * @throws Exception
     */
    public User unFrozenAccount(String unFrozenId, String admin, String newPassword) throws Exception {
        StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST + "/User/UnFrozen");
        url.append("?");
        url.append("id_=" + unFrozenId + "&");
        url.append("admin=" + admin + "&");
        url.append("passWord=" + URLEncoder.encode(newPassword, CHARSET));
        byte[] result = RequestSender.requestByGet(url.toString());
        if(result == null)
        	throw new Exception(cusException);
        return (User) GsonTransferUtil.transferToObject(new String(result), userObjct);
    }

    /**
     * 使用用户名绑定用户注册的google Cloud messaging id
     * @param regId
     * @param name
     * @return
     * @throws Exception
     */
    public Device bindDeviceWithUsername(String regId,String name) throws Exception{
    	StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST+"/APNS/Bind");
    	url.append("?");
    	url.append("name="+name+"&");
    	url.append("uid=A:"+regId);
    	 byte[] result = RequestSender.requestByGet(url.toString());
         if(result == null)
         	throw new Exception(cusException);
         return (Device) GsonTransferUtil.transferToObject(new String(result), new Device());
    } 
    /**
     * 使用Id绑定用户注册的google Cloud messaging id
     * @param regId
     * @param id
     * @return
     * @throws Exception
     */
    public Device bindDeviceWithId(String regId,String id) throws Exception{
    	StringBuilder url = new StringBuilder(ChatServerConstant.URL.SERVER_HOST+"/APNS/Bind");
    	url.append("?");
    	url.append("id_="+id+"&");
    	url.append("uid=A:"+regId);
    	byte[] result = RequestSender.requestByGet(url.toString());
        if(result == null)
        	throw new Exception(cusException);
        return (Device) GsonTransferUtil.transferToObject(new String(result), new Device());
    } 
    
    public interface OnGetUserListener{
    	public void onGetUser(User user, String userJson);
    }
}















