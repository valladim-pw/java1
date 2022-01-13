package ru.progwards.java1.lessons.datetime;
import java.time.*;
import java.util.*;

public class UserSession {
	private int sessionHandle;
	private String userName;
	private LocalDateTime lastAccess;
	public UserSession(String userName){
		this.userName = userName;
		this.lastAccess = LocalDateTime.now();
		this.sessionHandle = Math.abs(new Random().nextInt());
	}
	public void updateLastAccess(){
		this.lastAccess = LocalDateTime.now();
	}
	public int getSessionHandle() {
		return sessionHandle;
	}
	public String getUserName() {
		return userName;
	}
	public LocalDateTime getLastAccess() {
		return lastAccess;
	}
	@Override
	public String toString() {
		return "userName:" + userName + "\n" +
						"number:" + sessionHandle + "\n" +
						"lastAccess:" + lastAccess + "\n";
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserSession userSession = (UserSession) o;
		return sessionHandle == userSession.sessionHandle &&
						Objects.equals(userName, userSession.userName) &&
						Objects.equals(lastAccess, userSession.lastAccess);
	}
	@Override
	public int hashCode() {
		return Objects.hash(sessionHandle, userName, lastAccess);
	}
}

