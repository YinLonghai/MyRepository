package com.guotion.sicilia.im.util;

import com.google.gson.Gson;
import com.guotion.sicilia.bean.net.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: lizhengbin Date: 14-4-21 Time: 上午10:36 To
 * change this template use File | Settings | File Templates.
 */
public class GsonTransferUtil {

	public static Object transferToObject(String json, Object object)
			throws Exception {

		// System.out.println("-------------------------");
		// System.out.println("json_button=" + json);
		// System.out.println("-------------------------");
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(json);
			jsonObject.getString("response");
		} catch (Exception e) {
			Gson gson = new Gson();
			if (object instanceof Activity) {
				Activity activity = gson.fromJson(json, Activity.class);
				try {
					activity.setCloudFiles(getListFromString(jsonObject
							.getString("cloudFiles")));
				} catch (Exception e2) {
				}
				try {
					activity.setMembers(getListFromString(jsonObject
							.getString("members")));
				} catch (Exception e3) {
				}
				try {
					activity.setCreator(jsonObject.getString("creator"));
				} catch (Exception e4) {
				}
				return activity;
			}

			else if (object instanceof User) {
				User user = gson.fromJson(json, User.class);
				try {
					user.setChatGroups(getListFromString(jsonObject
							.getString("chatGroups")));
				} catch (Exception eu1) {
				}
				try {
					user.setSignature(jsonObject.getString("signature"));
				} catch (Exception eu2) {
				}
				return user;
			} else if (object instanceof ANotification) {
				ANotification note = gson.fromJson(json, ANotification.class);
				try {
					note.setEditBy(jsonObject.getString("editBy"));
				} catch (Exception en1) {
				}
				return note;
			} else if (object instanceof ChatGroup) {
				ChatGroup chatGroup = gson.fromJson(json, ChatGroup.class);
				try {
					chatGroup.setAdmins(getListFromString(jsonObject
							.getString("admins")));
				} catch (Exception ec1) {
				}
				try {
					chatGroup.setMembers(getListFromString(jsonObject
							.getString("members")));
				} catch (Exception ec2) {
				}
				try {
					chatGroup.setCreator(jsonObject.getString("creator"));
				} catch (Exception ec3) {
				}
				try {
					chatGroup.setChatHistory(jsonObject
							.getString("chatHistory"));
				} catch (Exception ec4) {
				}
				return chatGroup;
			} else if (object instanceof SystemEvent) {
				SystemEvent systemEvent = gson
						.fromJson(json, SystemEvent.class);
				try {
					systemEvent.setToActivity(jsonObject
							.getString("toActivity"));
				} catch (Exception es1) {
				}
				try {
					systemEvent.setToSingleUser(jsonObject
							.getString("toSingleUser"));
				} catch (Exception es2) {
				}
				return systemEvent;
			} else if (object instanceof ChatHistory) {
				ChatHistory chatHistory = gson
						.fromJson(json, ChatHistory.class);
				try {
					chatHistory.setChatItem(getListFromString(jsonObject
							.getString("chatItem")));
				} catch (Exception ech1) {
				}
				try {
					chatHistory.setChatGroup(jsonObject.getString("chatGroup"));
				} catch (Exception ech2) {
				}
				return chatHistory;
			} else if (object instanceof ChatItem) {
				ChatItem chatItem = gson.fromJson(json, ChatItem.class);
				try {
					chatItem.setChatGroup(jsonObject.getString("chatGroup"));
				} catch (Exception echa1) {
				}
				try {
					chatItem.setChatHistory(jsonObject.getString("chatHistory"));
				} catch (Exception echa2) {
				}
				try {
					chatItem.setCloud(jsonObject.getString("cloud"));
				} catch (Exception e1) {
				}
				try {
					chatItem.setToUser(jsonObject.getString("toUser"));
				} catch (Exception echa3) {
				}
				try {
					chatItem.setUser(jsonObject.getString("user"));
				} catch (Exception echa4) {
				}
				return chatItem;
			} else if (object instanceof CloudEvent) {
				CloudEvent cloudEvent = gson.fromJson(json, CloudEvent.class);
				try {
					cloudEvent.setFiles(getListFromString(jsonObject
							.getString("files")));
				} catch (Exception ecl1) {
				}
				try {
					cloudEvent.setActivity(jsonObject.getString("activity"));
				} catch (Exception ecl2) {
				}
				try {
					cloudEvent.setOwner(jsonObject.getString("owner"));
				} catch (Exception ecl3) {
				}

				return cloudEvent;
			} else if (object instanceof CloudItem) {
				CloudItem cloudItem = gson.fromJson(json, CloudItem.class);
				try {
					cloudItem.setOwner(jsonObject.getString("owner"));
				} catch (Exception ecl1) {
				}
				return cloudItem;
			} else if (object instanceof LastRead) {
				LastRead lastRead = gson.fromJson(json, LastRead.class);
				try {
					lastRead.setGroups(getListFromString(jsonObject
							.getString("groups")));
				} catch (Exception el1) {
				}
				try {
					lastRead.setUser(jsonObject.getString("user"));
				} catch (Exception el2) {
				}
				return lastRead;
			} else if (object instanceof SignatureHistory) {
				SignatureHistory signatureHistory = gson.fromJson(json,
						SignatureHistory.class);
				try {
					signatureHistory.setUser(jsonObject.getString("user"));
				} catch (Exception es1) {
				}
				return signatureHistory;
			} else if (object instanceof Tag) {
				return gson.fromJson(json, Tag.class);
			}
			Device device = gson.fromJson(json, Device.class);
			try {
				device.setUser(jsonObject.getString("user"));
			} catch (Exception ed1) {
			}
			return device;
		}
		throw new Exception(jsonObject.getString("response"));
	}

	public static List<Object> transferToArray(String json, Object object)
			throws Exception {
		// System.out.println("-------------------------");
		// System.out.println("json_button=" + json);
		// System.out.println("-------------------------");
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(json); // 出现错误，那么返回的是数组
			jsonObject.getString("response");
		} catch (Exception e) {
			Gson gson = new Gson();
			if (object instanceof Activity) {
				List<Object> spe = getListFromString(json);
				List<Object> objectList = new LinkedList<Object>();
				for (Object obj : spe) {
					objectList.add(transferToObject(obj + "", new Activity()));
				}
				return objectList;
			} else if (object instanceof User) {
				List<Object> spe = getListFromString(json);
				List<Object> objectList = new LinkedList<Object>();
				for (Object obj : spe) {
					objectList.add(transferToObject(obj + "", new User()));
				}
				return objectList;
			} else if (object instanceof ANotification) {
				List<Object> spe = getListFromString(json);
				List<Object> objectList = new LinkedList<Object>();
				for (Object obj : spe) {
					objectList.add(transferToObject(obj + "",
							new ANotification()));
				}
				return objectList;
			} else if (object instanceof ChatGroup) {
				List<Object> spe = getListFromString(json);
				List<Object> objectList = new LinkedList<Object>();
				for (Object obj : spe) {
					objectList.add(transferToObject(obj + "", new ChatGroup()));
				}
				return objectList;
			} else if (object instanceof SystemEvent) {
				List<Object> spe = getListFromString(json);
				List<Object> objectList = new LinkedList<Object>();
				for (Object obj : spe) {
					objectList
							.add(transferToObject(obj + "", new SystemEvent()));
				}
				return objectList;
			} else if (object instanceof ChatHistory) {
				List<Object> spe = getListFromString(json);
				List<Object> objectList = new LinkedList<Object>();
				for (Object obj : spe) {
					objectList
							.add(transferToObject(obj + "", new ChatHistory()));
				}
				return objectList;
			} else if (object instanceof ChatItem) {
				List<Object> spe = getListFromString(json);
				List<Object> objectList = new LinkedList<Object>();
				for (Object obj : spe) {
					objectList.add(transferToObject(obj + "", new ChatItem()));
				}
				return objectList;
			} else if (object instanceof CloudEvent) {
				List<Object> spe = getListFromString(json);
				List<Object> objectList = new LinkedList<Object>();
				for (Object obj : spe) {
					objectList
							.add(transferToObject(obj + "", new CloudEvent()));
				}
				return objectList;
			} else if (object instanceof CloudItem) {
				List<Object> spe = getListFromString(json);
				List<Object> objectList = new LinkedList<Object>();
				for (Object obj : spe) {
					objectList.add(transferToObject(obj + "", new User()));
				}
				return objectList;
			} else if (object instanceof LastRead) {
				List<Object> spe = getListFromString(json);
				List<Object> objectList = new LinkedList<Object>();
				for (Object obj : spe) {
					objectList.add(transferToObject(obj + "", new LastRead()));
				}
				return objectList;
			} else if (object instanceof SignatureHistory) {
				List<Object> spe = getListFromString(json);
				List<Object> objectList = new LinkedList<Object>();
				for (Object obj : spe) {
					objectList.add(transferToObject(obj + "",
							new SignatureHistory()));
				}
				return objectList;
			} else if (object instanceof Tag) {
				List<Object> spe = getListFromString(json);
				List<Object> objectList = new LinkedList<Object>();
				for (Object obj : spe) {
					objectList.add(transferToObject(obj + "", new Tag()));
				}
				return objectList;
			}
			List<Object> spe = getListFromString(json);
			List<Object> objectList = new LinkedList<Object>();
			for (Object obj : spe) {
				objectList.add(transferToObject(obj + "", new Device()));
			}
			return objectList;
		}
		throw new Exception(jsonObject.getString("response"));
	}

	public static boolean transferToBoolean(String json) {
		// System.out.println("-------------------------");
		// System.out.println("json_button=" + json);
		// System.out.println("-------------------------");
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (jsonObject.getString("response").equals("1"))
				return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	private static List<Object> getListFromString(String content)
			throws JSONException {
		List<Object> objectList = new LinkedList<Object>();
		JSONArray jsonArray = new JSONArray(content);
		for (int i = 0; i < jsonArray.length(); i++) {
			objectList.add(jsonArray.get(i));
		}
		return objectList;
	}
}
