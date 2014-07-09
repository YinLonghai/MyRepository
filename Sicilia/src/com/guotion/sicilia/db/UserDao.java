package com.guotion.sicilia.db;

import java.sql.SQLException;
import java.util.List;

import com.guotion.sicilia.bean.net.User;
import com.j256.ormlite.dao.Dao;

public class UserDao {

	private Dao<User, Integer> userDao = null;
	public UserDao(AccountDBHelper dbHelper) {
		try {
			userDao = dbHelper.getDao(User.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createIfNotExists(User user){
		try {
			if(userDao.queryForEq("_id", user._id).size() == 0){
				userDao.createIfNotExists(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<User> getAll(){
		try {
			return userDao.queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void deleteUser(int userId){
		try {
			userDao.deleteById(userId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
