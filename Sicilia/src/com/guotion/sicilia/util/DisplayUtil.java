package com.guotion.sicilia.util;

import com.guotion.sicilia.config.DeviceConfig;

/**
 * Android大小单位转换工具类
 * 
 * @author qmy
 * 
 */
public class DisplayUtil {
 /**
  * 将px值转换为dip或dp值，保证尺寸大小不变
  * 
  * @param pxValue
  * @param scale（DisplayMetrics类中属性density）
  * @return
  */
 public static int px2dip(float pxValue) {
  return (int) (pxValue / DeviceConfig.scale + 0.5f);
 }

 /**
  * 将dip或dp值转换为px值，保证尺寸大小不变
  * 
  * @param dipValue
  * @param scale（DisplayMetrics类中属性density）
  * @return
  */
 public static int dip2px(float dipValue) {
  return (int) (dipValue * DeviceConfig.scale + 0.5f);
 }

 /**
  * 将px值转换为sp值，保证文字大小不变
  * 
  * @param pxValue
  * @param fontScale（DisplayMetrics类中属性scaledDensity）
  * @return
  */
 public static int px2sp(float pxValue) {
  return (int) (pxValue / DeviceConfig.scale + 0.5f);
 }

 /**
  * 将sp值转换为px值，保证文字大小不变
  * 
  * @param spValue
  * @param fontScale（DisplayMetrics类中属性scaledDensity）
  * @return
  */
 public static int sp2px(float spValue) {
  return (int) (spValue * DeviceConfig.scale + 0.5f);
 }
}

