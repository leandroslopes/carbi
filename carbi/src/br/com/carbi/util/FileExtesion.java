package br.com.carbi.util;

import java.io.File;

public class FileExtesion {

	public FileExtesion() {

	}

	public static String getFileExtension(File file) {
		String extension = "";

		try {
			if (file != null && file.exists()) {
				String name = file.getName();
				extension = name.substring(name.lastIndexOf("."));
			}
		} catch (Exception e) {
			extension = "";
		}

		return extension;

	}
}
