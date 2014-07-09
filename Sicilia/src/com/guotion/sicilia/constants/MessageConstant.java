package com.guotion.sicilia.constants;

/**
 * 公共网络消息状态KEY及值的常量
 * @author 邱明月
 * @version 1.0
 */
public interface MessageConstant {
	
	
	
	
	/**
	 * 所处界面
	 * @author qmy
	 *
	 */
	public interface ManagerKey{
		public int KEY_LAUNCH = 0;
		public int KEY_LOGIN = 1;
		public int KEY_REGISTER = 2;
		public int KEY_REGISTER_CHECK_PHONENUMBER = 3;
		public int KEY_MY_USER_INFO = 4;
		public int KEY_MAIN = 5;
		public int KEY_SYS_CHAT = 6;
		public int KEY_FRIEND_USER_INFO = 7;
	}
	
	
	/**
	 * 发送Message的行为
	 * @author qmy
	 *
	 */
	public interface What{
		public int WHAT_TIME_OUT = 0;
		public int WHAT_LOGIN = 1;
		public int WHAT_REGISTER = 2;	
		public int WHAT_QUREST_REGISTER_CAPTCHA = 3;
		public int WHAT_QUREST_RESETPASSWORD_CAPTCHA = 4;
		public int WHAT_UPLOAD_USERINFO = 5;
		public int WHAT_UPDATE_USERINFO = 6;		
		public int WHAT_IM_LOGIN_ERROR = 7;
		public int WHAT_QUREST_USER_INFO = 8;
		/**
		 * 上传头像失败
		 */
		public int WHAT_UPLOAD_HEAD_ERROR = 9;
		/**
		 * 上传头像成功
		 */
		public int WHAT_UPLOAD_HEAD_SUCCESS = 10;
		/**
		 * 提交意见
		 */
		public int WHAT_SUGGEST = 11;
		/**
		 * 修改备注名
		 */
		public int WHAT_UPLOAD_REMARK_NAME = 12;
	}
	
	/**
	 * 返回消息的Key
	 * @author qmy
	 *
	 */
	public interface ResultKey{
		/**
		 * 返回数据的key
		 */
		public final String RESPONSE_DATA_KEY = "response_data_key";
		/**
		 * 附加消息的key
		 */
		public static final String ATTACH_TEXT_KEY = "attach_text";
		/**
		 * 存放用户资料的key
		 */
		public final String USER_INFO_KEY = "user_info_key";
		/**
		 * MapMessage中状态的key
		 */
		public final String KEY_STATE = "state";
		/**
		* 获取好友时，存放好友资料的key
		 */
		public final String FRIENDS_INFO_KEY = "friends_info_key";
		/**
		 * 修改时间的key
		 */
		public final String MODIFY_TIME = "modify_time";

	}
	
	
	/**
	 * 返回消息的值
	 * @author qmy
	 *
	 */
	public interface ResultValue{
		/**
		 * 操作成功
		 */
		public final int RESULT_SUCCESS = 0;
		/**
		 * 操作失败
		 */
		public final int RESULT_FAILURE = 1;
		/**
		 * 版本过低
		 */
		public final int RESULT_VERSION_LOW = 2;
		/**
		 * 网络异常
		 */
		public final int RESULT_NETWORK_ERROR = 8;
		
		/**
		 * 聊天账号登录失败
		 */
		public final int RESULT_IM_LOGIN_ERROR = 9;	
		
		/**
		 * 帐号或密码不正确
		 */
		public final int RESULT_PWD_ERROR = 102;
		
		/**
		 * 验证码不正确
		 */
		public final int RESULT_CAPTCHA_ERROE = 103;
		
		/**
		 * 已经发送过验证码
		 */
		public final int RESULT_CAPTCHA_SENT = 104;
		
		/**
		 * 手机号已经注册过
		 */
		public final int RESULT_TEL_REGISTERED = 105;
		/**
		 * 手机号没有注册
		 */
		public final int RESULT_RESET_PASSWORD_TEL_NOT_REGISTER = 106;
		/**
		 * 三方帐号未注册
		 */
		public final int RESULT_THRID_ACCOUNT_NOT_REGISTERED = 107;
		
		
		/**
		 * 用户没有备注的好友
		 */
		public final int NO_REMARK_FRIENDS = 203;
		/**
		 * 获取用户的权限不够
		 */
		public final int AUTHORITY_ERROR = 204;
		/**
		 * 用户资料不存在 
		 */
		public final int RESULT_USERINFO_NOT_EXIST = 205;
		/**
		 * 服务器保存的时间更近
		 */
		public final int RESULT_USERINFO_CHANGETIME_SERVER_LATELY = 206;
		/**
		 * 用户资料，客户端更近
		 */
		public final int RESULT_USERINFO_CHANGETIME_CILENT_LATELY = 207;
		
		/**
		 * 用户好友列表没有更新
		 */
		public int RESULT_FRIENDS_NO_UPDATE = 208;

		/**
		 * 没有找到合符条件的人
		 */
		public final int RESULT_NO_USER_FOUND = 302;

			
	}
	
	
	
	
	
}
