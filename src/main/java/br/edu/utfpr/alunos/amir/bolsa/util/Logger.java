package br.edu.utfpr.alunos.amir.bolsa.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class Logger {
	public static synchronized void writeLog(String log) {
		PrintWriter logger = null;
		try {
			logger = new PrintWriter (new FileWriter ("c:/data/log.txt", true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.println(LocalDateTime.now().toString() + " " + log);
		logger.flush();
		logger.close();
	}
}
