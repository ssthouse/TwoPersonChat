package com.ssthouse.twopersonchat.style;


import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

public class TransparentStyle {

	public static void setAppToTransparentStyle(Context context, int color){
		//形成完整沉浸式的方法
		((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		SystemBarTintManager tintManager = new SystemBarTintManager((Activity) context);
		tintManager.setStatusBarTintEnabled(true);  
		// 设置一个颜色给系统栏   
		tintManager.setTintColor(color);   
		// 设置一个状态栏资源 
		tintManager.setStatusBarTintColor(color);


		//		((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		// 激活导航栏设置  
		//		tintManager.setNavigationBarTintEnabled(true);
		// 设置一个样式背景给导航栏  
		//		tintManager.setNavigationBarTintColor(color); 
	}
}
