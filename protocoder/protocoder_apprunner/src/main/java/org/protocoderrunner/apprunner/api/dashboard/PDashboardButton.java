/*
 * Protocoder 
 * A prototyping platform for Android devices 
 * 
 * Victor Diaz Barrales victormdb@gmail.com
 *
 * Copyright (C) 2014 Victor Diaz
 * Copyright (C) 2013 Motorola Mobility LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions: 
 * 
 * The above copyright notice and this permission notice shall be included in all 
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN 
 * THE SOFTWARE.
 * 
 */

package org.protocoderrunner.apprunner.api.dashboard;

import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;
import org.protocoderrunner.apidoc.annotation.APIMethod;
import org.protocoderrunner.apprunner.PInterface;
import org.protocoderrunner.apprunner.ProtocoderScript;
import org.protocoderrunner.network.CustomWebsocketServer;
import org.protocoderrunner.network.CustomWebsocketServer.WebSocketListener;
import org.protocoderrunner.utils.StrUtils;

import android.app.Activity;

public class PDashboardButton extends PInterface {

	private static final String TAG = "PDashboardButton";
	String id;
	String name;

	public PDashboardButton(Activity a) {
		super(a);
	}

	// --------- JDashboard add ---------//
	public interface jDashboardAddCB {
		void event();
	}

	public void add(String name, int x, int y, int w, int h, final jDashboardAddCB callbackfn) throws JSONException,
			UnknownHostException {
		this.id = StrUtils.generateRandomString();
		this.name = name;

		JSONObject values = new JSONObject()
                .put("id", id)
                .put("name", name)
                .put("type", "button")
                .put("x", x)
                .put("y", y)
                .put("w", w)
                .put("h", h);

        JSONObject msg = new JSONObject()
                .put("type", "widget")
                .put("action", "add")
                .put("values", values);

		CustomWebsocketServer.getInstance(a.get()).send(msg);
		CustomWebsocketServer.getInstance(a.get()).addListener(id, new WebSocketListener() {

			@Override
			public void onUpdated(JSONObject jsonObject) {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						callbackfn.event();
					}
				});
			}
		});

	}

	public void update(boolean pressed) throws JSONException, UnknownHostException {

		JSONObject values = new JSONObject()
                .put("name", name)
                .put("type", "button")
                .put("val", pressed);

        JSONObject msg = new JSONObject()
                .put("type", "widget")
                .put("action", "update")
                .put("values", values);

		CustomWebsocketServer.getInstance(a.get()).send(msg);
	}
}
