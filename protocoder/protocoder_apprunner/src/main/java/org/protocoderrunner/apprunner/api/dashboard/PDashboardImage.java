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

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.protocoderrunner.apidoc.annotation.APIMethod;
import org.protocoderrunner.apidoc.annotation.APIParam;
import org.protocoderrunner.apprunner.PInterface;
import org.protocoderrunner.apprunner.ProtocoderScript;
import org.protocoderrunner.network.CustomWebsocketServer;
import org.protocoderrunner.utils.StrUtils;

import java.net.UnknownHostException;

public class PDashboardImage extends PInterface {

	private static final String TAG = "PDashboardImage";
	String id;

	public PDashboardImage(Context a) {
		super(a);
	}

	@ProtocoderScript
	@APIMethod(description = "", example = "")
	public void add(String url, int x, int y, int w, int h) throws UnknownHostException, JSONException {
		this.id = StrUtils.generateRandomString();

		JSONObject values = new JSONObject()
                .put("id", id)
                .put("url", url)
                .put("type", "image")
                .put("x", x)
                .put("y", y)
                .put("w", w)
                .put("h", h);

        JSONObject msg = new JSONObject()
                .put("type", "widget")
                .put("action", "add")
                .put("values", values);

		CustomWebsocketServer.getInstance(a.get()).send(msg);
	}

	@ProtocoderScript
	@APIMethod(description = "change image with a provided url", example = "")
    @APIParam(params = { "url"})
    public void changeImage(String url) throws JSONException, UnknownHostException {

		JSONObject values = new JSONObject()
                .put("id", id)
                .put("type", "label")
                .put("url", url);

        JSONObject msg = new JSONObject()
                .put("type", "widget")
                .put("action", "changeImage")
                .put("values", values);

		CustomWebsocketServer.getInstance(a.get()).send(msg);
	}
}
