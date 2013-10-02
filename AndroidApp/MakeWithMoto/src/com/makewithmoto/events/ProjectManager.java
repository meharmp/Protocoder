package com.makewithmoto.events;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.makewithmoto.base.BaseMainApp;
import com.makewithmoto.utils.FileIO;

//TODO take out the file reading to FileIO 
public class ProjectManager {
	public static final int PROJECT_USER_MADE = 0;
	public static final int PROJECT_EXAMPLE = 1;
	private static final String TAG = "ProjectManager";
	public static int type;

	private static ProjectManager INSTANCE;

	public static ProjectManager getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ProjectManager();

		return INSTANCE;
	}

	public String getCode(Project p) {
		String out = null;
		File f = new File(p.getUrl() + File.separator + "main.js");
		try {
			InputStream in = new FileInputStream(f);
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int i;
			try {
				i = in.read();
				while (i != -1) {
					buf.write(i);
					i = in.read();
				}
				in.close();
			} catch (IOException ex) {
			}
			out = buf.toString();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("Project", e.toString());
		}
		return out;
	}

	public void writeNewCode(Project p, String code) {
		writeNewFile(p.getUrl() + File.separator + "main.js", code);
	}

	public void writeNewFile(String file, String code) {
		File f = new File(file);

		try {
			if (!f.exists())
				f.createNewFile();
			FileOutputStream fo = new FileOutputStream(f);
			byte[] data = code.getBytes();
			fo.write(data);
			fo.flush();
			fo.close();
		} catch (FileNotFoundException ex) {
			Log.e("Project", ex.toString());
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("Project", e.toString());
		}
	}

	public JSONObject to_json(Project p) {
		JSONObject json = new JSONObject();
		try {
			json.put("name", p.getName());
			json.put("url", p.getUrl());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	public ArrayList<Project> list(int type) {
		ArrayList<Project> projects = new ArrayList<Project>();
		File dir = null;

		Log.d(TAG, "project type" + type + " " + PROJECT_USER_MADE + " "
				+ PROJECT_EXAMPLE);

		switch (type) {
		case PROJECT_USER_MADE:
			dir = new File(BaseMainApp.projectsDir);
			if (!dir.exists())
				dir.mkdir();

			break;

		case PROJECT_EXAMPLE:
			dir = new File(BaseMainApp.examplesDir);
			if (!dir.exists())
				dir.mkdir();

			break;
		default:
			break;
		}

		File[] all_projects = dir.listFiles();

		for (int i = 0; i < all_projects.length; i++) {
			File file = all_projects[i];
			String projectURL = file.getAbsolutePath();
			String projectName = file.getName();
			Log.d("PROJECT", "Adding project named " + projectName);
			projects.add(new Project(projectName, projectURL, type));
		}

		return projects;
	}

	public Project get(String name, int type) {
		Log.d(TAG, "looking for project types " + type);
		ArrayList<Project> projects = list(type);
		for (Project project : projects) {
			if (name.equals(project.getName())) {
				Log.d("UU", "" + name + " " + type + " " + project.getName());
				return project;
			}
		}
		return null;
	}

	public Project addNewProject(Context c, String newProjectName,
			String fileName, int type) {
		String newTemplateCode = FileIO.readAssetFile(c, "templates/new.js");

		if (newTemplateCode == null)
			newTemplateCode = "";
		String file = FileIO.writeStringToFile(BaseMainApp.projectsDir,
				newProjectName, newTemplateCode);

		Project newProject = new Project(newProjectName, file, type);

		return newProject;

	}

	public JSONArray listFilesInProject(Project p) {

		File f = new File(p.getUrl());
		File file[] = f.listFiles();
		Log.d("Files", "Size: " + file.length);

		JSONArray array = new JSONArray();
		for (int i = 0; i < file.length; i++) { 
			
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("file_name", file[i].getName());
				jsonObject.put("file_size", file[i].length() / 1024);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
			array.put(jsonObject);
			Log.d("Files", "FileName:" + file[i].getName());
		}
	
		return array;
	}

	// TODO fix this hack
	public String getProjectURL(Project p) {
		String projectURL = p.getUrl();

		return projectURL;

	}

}