package ru.progwards.java1.lessons.datetime;
import java.time.*;
import java.time.format.*;

public class Insurance {
	public static enum FormatStyle{
		SHORT,
		LONG,
		FULL
	}
	private ZonedDateTime start;
	private Duration duration;
	public FormatStyle style;
	public ZoneId id;
	public int startOff;
	public String validStr;
	public Insurance(ZonedDateTime start){
		this.start = start;
		this.id = start.getZone();
		this.startOff = start.getOffset().getTotalSeconds() / 3600;
	}
	public Insurance(String strStart, FormatStyle style){
		if(strStart.indexOf("[") != -1)
			style = FormatStyle.FULL;
		else if(strStart.indexOf("T") != -1)
			style = FormatStyle.LONG;
		else
			style = FormatStyle.SHORT;
		this.style = style;
		switch(style){
			case SHORT:
				LocalDate ld = LocalDate.parse(strStart, DateTimeFormatter.ISO_LOCAL_DATE);
				this.start = ld.atStartOfDay(ZoneId.systemDefault());
				this.id = start.getZone();
				this.startOff = start.getOffset().getTotalSeconds() / 3600;
				break;
			case LONG:
				LocalDateTime ldt = LocalDateTime.parse(strStart, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
				this.start = ldt.atZone(ZoneId.systemDefault());
				this.id = start.getZone();
				this.startOff = start.getOffset().getTotalSeconds() / 3600;
				break;
			case FULL:
				this.start = ZonedDateTime.parse(strStart, DateTimeFormatter.ISO_ZONED_DATE_TIME);
				this.id = start.getZone();
				this.startOff = start.getOffset().getTotalSeconds() / 3600;
				break;
		}
	}
	@Override
	public String toString() {
		String strResult = "";
		if(duration != null){
			checkValid(start.plus(duration));
		} else {
			checkValid(start);
		}
		strResult = "Insurance issued on " + start + validStr;
		return strResult;
	}
	public void setDuration(Duration duration){
		this.duration = duration;
	}
	public void setDuration(ZonedDateTime expiration){
		if (!expiration.getZone().equals(id)){
			int expOff = expiration.getOffset().getTotalSeconds() / 3600;
			int timeDiff = Math.abs(startOff - expOff);
			if(expOff < startOff)
				expiration = expiration.plusHours((long)timeDiff).toLocalDateTime().atZone(id);
			else if(expOff > startOff)
				expiration = expiration.minusHours((long)timeDiff).toLocalDateTime().atZone(id);
			else
				expiration = expiration.toLocalDateTime().atZone(id);
		}
		this.duration = Duration.between(start, expiration);
	}
	public void setDuration(int months, int days, int hours){
		ZonedDateTime exp = start.plusMonths((long)months).plusDays((long)days).plusHours((long)hours);
		this.duration = Duration.between(start, exp);
	}
	public void setDuration(String strDuration, FormatStyle style){
		if(strDuration.indexOf("P") != -1)
			style = FormatStyle.FULL;
		else if(strDuration.indexOf(":") != -1)
			style = FormatStyle.LONG;
		else
			style = FormatStyle.SHORT;
		this.style = style;
		switch(style){
			case SHORT:
				long ms = Long.parseLong(strDuration);
				this.duration = Duration.ofMillis(ms);
				break;
			case LONG:
				String strDate = strDuration.substring(0, strDuration.indexOf("T"));
				String strTime = strDuration.substring(strDuration.indexOf("T") + 1);
				int y = Integer.parseInt(strDate.substring(0, 4));
				int m = Integer.parseInt(strDate.substring(5, 7));
				int d = Integer.parseInt(strDate.substring(8));
				Period period = Period.of(y, m, d);
				LocalTime lt = LocalTime.parse(strTime,DateTimeFormatter.ISO_LOCAL_TIME);
				Duration duration =
								Duration.ZERO.plusHours((long)lt.getHour()).plusMinutes((long)lt.getMinute()).plusSeconds((long)lt.getSecond()).plusNanos((long)lt.getNano());
				ZonedDateTime now = ZonedDateTime.now(id);
				ZonedDateTime exp = now.plus(period).plus(duration);
				this.duration = Duration.between(now, exp);
				break;
			case FULL:
				this.duration = Duration.parse(strDuration);
				break;
		}
	}
	public boolean checkValid(ZonedDateTime dateTime){
		boolean validate;
		ZonedDateTime checkDate = ZonedDateTime.now(id);
		if (!dateTime.getZone().equals(id)){
			int dateTimeOff = dateTime.getOffset().getTotalSeconds() / 3600;
			int timeDiff = Math.abs(startOff - dateTimeOff);
			if(dateTimeOff < startOff)
				dateTime = dateTime.plusHours((long)timeDiff).toLocalDateTime().atZone(id);
			else if(dateTimeOff > startOff)
				dateTime = dateTime.minusHours((long)timeDiff).toLocalDateTime().atZone(id);
			else
				dateTime = dateTime.toLocalDateTime().atZone(id);
		}
		if(duration == null){
			if(!dateTime.equals(start)){
				int compareDateTime = dateTime.toLocalDateTime().compareTo(start.toLocalDateTime());
				if(compareDateTime > 0)
					validate = true;
				else
					validate = false;
			} else {
				int compareStart = start.toLocalDateTime().compareTo(checkDate.toLocalDateTime());
				if(compareStart <= 0)
					validate = true;
				else
					validate = false;
			}
		} else {
			ZonedDateTime expZoned = start.plus(duration);
			if(!dateTime.equals(expZoned)){
				int compareStart = dateTime.toLocalDateTime().compareTo(start.toLocalDateTime());
				int compareExp = dateTime.toLocalDateTime().compareTo(expZoned.toLocalDateTime());
				if(compareStart > 0 && compareExp <= 0)
					validate = true;
				else
					validate = false;
			} else{
				int compareStart = checkDate.toLocalDateTime().compareTo(start.toLocalDateTime());
				int compareExp = checkDate.toLocalDateTime().compareTo(dateTime.toLocalDateTime());
				if(compareStart > 0 && compareExp <= 0)
					validate = true;
				else
					validate = false;
			}
		}
		if(validate)
			validStr = " is valid";
		else
			validStr = " is not valid";
		return validate;
	}
	public static void main(String[] args) {
		ZonedDateTime start1 = ZonedDateTime.now(ZoneId.systemDefault());
		ZonedDateTime start2 = ZonedDateTime.now(ZoneId.systemDefault());
		ZonedDateTime start3 = ZonedDateTime.now(ZoneId.systemDefault());
		Insurance ins = new Insurance(start1);
		System.out.println(ins);
		Insurance ins2 = new Insurance("" + start2, FormatStyle.LONG);
		ins2.setDuration(Duration.ofDays(2));
		System.out.println(ins2);
		Insurance ins3 = new Insurance(start3);
		ins3.setDuration(ZonedDateTime.parse("2021-12-19T14:26:12.819693+03:00[Europe/Moscow]"));
		System.out.println(ins3);
		ZonedDateTime start4 = ZonedDateTime.now(ZoneId.systemDefault());
		Insurance ins4 = new Insurance(start4);
		ins4.setDuration("1000000000", Insurance.FormatStyle.SHORT);
		System.out.println(ins4);
		ZonedDateTime start5 = ZonedDateTime.parse("2021-11-20T00:04:17.670357+03:00[Europe/Moscow]");
		Insurance ins5 = new Insurance(start5);
		ins4.setDuration("0000-01-04T00:00:00", Insurance.FormatStyle.LONG);
		System.out.println(ins5);
		ZonedDateTime start6 = ZonedDateTime.parse("2021-12-18T00:04:17.682490+03:00[Europe/Moscow]");
		Insurance ins6 = new Insurance(start6);
		ins6.setDuration(Duration.ofDays(3));
		System.out.println(ins6.checkValid(ZonedDateTime.parse("2021-12-20T00:04:17.682529+03:00[Europe/Moscow]")));
		System.out.println(ins6);
		Insurance ins7 = new Insurance("2021-12-19T00:00:00", FormatStyle.LONG);
		ins7.setDuration(Duration.ofDays(2));
		System.out.println(ins7);
	}
}
