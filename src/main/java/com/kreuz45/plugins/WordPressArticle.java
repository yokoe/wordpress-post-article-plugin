package com.kreuz45.plugins;

import hudson.EnvVars;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class WordPressArticle {
	private String url, user, password, title, body, category, basic_user_name, basic_password;
	private Boolean publish;
	public WordPressArticle(String url, String user, String password,
			String title, String body, String category, String basic_user_name,
			String basic_password, Boolean publish) {
		super();
		this.url = url;
		this.user = user;
		this.password = password;
		this.title = title;
		this.body = body;
		this.category = category;
		this.basic_user_name = basic_user_name;
		this.basic_password = basic_password;
		this.publish = publish;
	}
	
	public boolean post(EnvVars vars) {
		try {
			// WordPress XML-RPC API wp.newPost
        	// http://codex.wordpress.org/XML-RPC_WordPress_API/Posts#wp.newPost
        	XmlRpcClient client = new XmlRpcClient();
            XmlRpcClientConfigImpl conf = new XmlRpcClientConfigImpl();
			conf.setServerURL(new URL(url));
			if (this.basic_user_name != null && this.basic_user_name.length() > 0 &&
					this.basic_password != null && this.basic_password.length() > 0) {
				conf.setBasicUserName(this.basic_user_name);
				conf.setBasicPassword(this.basic_password);
			}
			
			client.setConfig(conf);
			
			List params = new ArrayList();
			params.add(1);
			params.add(this.user);
			params.add(this.password);
			
			Hashtable content = new Hashtable();
			content.put("post_title", vars.expand(this.title));
			content.put("post_content", vars.expand(this.body));
			
			if (this.publish) {
				content.put("post_status", "publish");
			}
			if (this.category != null && this.category.length() > 0) {
				Hashtable terms = new Hashtable();
				
				String[] categories = this.category.split(",");
				terms.put("category", categories);
				content.put("terms", terms);
			}
			
			params.add(content);
			
			// 実行
			String ret = (String) client.execute("wp.newPost", params);
			// サーバからのレスポンスを出力
			System.out.println("ret=" + ret);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
