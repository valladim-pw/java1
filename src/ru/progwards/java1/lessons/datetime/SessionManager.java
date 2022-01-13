package ru.progwards.java1.lessons.datetime;
import java.time.*;
import java.util.*;

public class SessionManager {
	private Set<UserSession> sessions;
	private int sessionValid;
	public SessionManager(int sessionValid){
		this.sessionValid = sessionValid;
		this.sessions = new LinkedHashSet();
	}
	public void add(UserSession userSession){
		sessions.add(userSession);
	}
	public UserSession find(String userName){
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime validTime;
		Iterator<UserSession> iter = sessions.iterator();
		UserSession next;
		while(iter.hasNext()){
			next = iter.next();
			validTime = next.getLastAccess().plusSeconds((long)sessionValid);
			int compare = now.compareTo(validTime);
			if(next.getUserName().contains(userName) && compare <= 0){
				next.updateLastAccess();
				return next;
			}
		}
		return null;
	}
	public UserSession get(int sessionHandle){
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime validTime;
		Iterator<UserSession> iter = sessions.iterator();
		UserSession next;
		while(iter.hasNext()){
			next = iter.next();
			validTime = next.getLastAccess().plusSeconds((long)sessionValid);
			int compare = now.compareTo(validTime);
			if(Integer.toString(next.getSessionHandle()).contains(Integer.toString(sessionHandle)) && compare <= 0){
				next.updateLastAccess();
				return next;
			}
		}
		return null;
	}
	public void delete(int sessionHandle){
		Iterator<UserSession> iter = sessions.iterator();
		UserSession next;
		while(iter.hasNext()){
			next = iter.next();
			if(Integer.toString(next.getSessionHandle()).contains(Integer.toString(sessionHandle))){
				iter.remove();
			}
		}
	}
	public void deleteExpired(){
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime validTime;
		Iterator<UserSession> iter = sessions.iterator();
		UserSession next;
		while(iter.hasNext()){
			next = iter.next();
			validTime = next.getLastAccess().plusSeconds((long)sessionValid);
			int compare = now.compareTo(validTime);
			if(compare > 0){
				iter.remove();
			}
		}
	}
	public static void main(String[] args) {
		SessionManager sm = new SessionManager(4);
		System.out.println("1->" + sm.find("Vadim"));
		UserSession us1 = new UserSession("Vadim");
		sm.add(us1);
		System.out.println("1->" + sm.find(us1.getUserName()));
		System.out.println("2->" + sm.get(us1.getSessionHandle()));
		System.out.println("2->" + sm.get(us1.getSessionHandle()));
		System.out.println("2->" + sm.get(us1.getSessionHandle()));
		long start = 0;
		long finish = 0;
		try{
			start = Instant.now().toEpochMilli();
			Thread.sleep(4000);
			finish = Instant.now().toEpochMilli();
		}catch (Exception e){
			System.out.println(e);
		}
		System.out.println("3->" + (finish - start));
		System.out.println("4->" + sm.get(us1.getSessionHandle()));
		UserSession us2 = new UserSession("Ivan");
		sm.add(us2);
		System.out.println("5->" + sm.find(us2.getUserName()));
		try{
			start = Instant.now().toEpochMilli();
			Thread.sleep(2000);
			finish = Instant.now().toEpochMilli();
		}catch (Exception e){
			System.out.println(e);
		}
		System.out.println("6->" + (finish - start));
		UserSession us3 = new UserSession("Dmitry");
		sm.add(us3);
		System.out.println("7->" + sm.find(us3.getUserName()));
		try{
			start = Instant.now().toEpochMilli();
			Thread.sleep(2000);
			finish = Instant.now().toEpochMilli();
		}catch (Exception e){
			System.out.println(e);
		}
		System.out.println("8->" + (finish - start));
		sm.deleteExpired();
		System.out.println("10->" + sm.sessions);
		sm.delete(us3.getSessionHandle());
		System.out.println("12->" + sm.sessions);
	}
}

