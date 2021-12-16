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
	public String startFormat;
	public String durationFormat;
	public ZoneId id;
	public String validStr ;
	FormatStyle style;
	public int startOff;
	public Insurance(ZonedDateTime start){
		this.start = start;
		this.id = start.getZone();
		this.startOff = start.getOffset().getTotalSeconds() / 3600;
	}
	public Insurance(String strStart, FormatStyle style){
		this.style = style;
		String id = strStart.substring(strStart.indexOf("[") + 1, strStart.indexOf("]"));
		String check = strStart.substring(strStart.indexOf("T"));
		switch(style){
			case SHORT:
				strStart = strStart.substring(0, strStart.indexOf("T"));
				LocalDate ld = LocalDate.parse(strStart, DateTimeFormatter.ISO_LOCAL_DATE);
				start = ld.atStartOfDay(ZoneId.systemDefault());
				startFormat = start.format(DateTimeFormatter.ISO_LOCAL_DATE);
				break;
			case LONG:
				if(check.indexOf(".") != -1)
					strStart = strStart.substring(0, strStart.indexOf("T")) + check.substring(0, check.indexOf("."));
				else if(check.indexOf("-") != -1)
					strStart = strStart.substring(0, strStart.indexOf("T")) + check.substring(0, check.indexOf("-"));
				else
					strStart = strStart.substring(0, strStart.indexOf("+"));
				LocalDateTime ldt = LocalDateTime.parse(strStart, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
				start = ldt.atZone(ZoneId.of(id));
				startFormat = start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
				break;
			case FULL:
				start = ZonedDateTime.parse(strStart, DateTimeFormatter.ISO_ZONED_DATE_TIME);
				startFormat = start.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
				break;
		}
	}
	@Override
	public String toString() {
		String strResult = "";
		String strStart = start.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
		if(this.style != null){
			strResult = startFormat;
		} else{
			strResult = strStart;
		}
		if(duration != null){
			if(this.style != null){
				strResult = durationFormat;
			} else{
				strResult =  "" + duration;
			}
		}
		if(validStr != null)
			strResult = "Insurance issued on " + strStart + validStr;
		return strResult;
	}
	public void setDuration(Duration duration){
		validStr = null;
		durationFormat = null;
		style = null;
		this.duration = duration;
		if (duration.isZero())
			System.out.println("! Дата-время истечения страховки равна дате-времени начала:");
		if (duration.isNegative())
			System.out.println("! Дата-время истечения страховки меньше даты-времени начала:");
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
		this.setDuration(duration.between(start, expiration));
	}
	public void setDuration(int months, int days, int hours){
		this.setDuration(start.plusMonths((long)months).plusDays((long)days).plusHours((long)hours));
	}
	public void setDuration(String strDuration, FormatStyle style){
		this.style = style;
		this.duration = Duration.parse(strDuration);
		switch(style){
			case SHORT:
				long ms = duration.toMillis();
				durationFormat = "" + ms;
				break;
			case LONG:
				String strDate = "";
				String strTime = "";
				LocalDateTime exp = start.toLocalDateTime().plus(duration.abs());
				Period period = Period.between(start.toLocalDate(), exp.toLocalDate());
				String add1 = "0";
				String add2 = "00";
				String add3 = "000";
				String years = "" + period.getYears();
				String months = "" + period.getMonths();
				String days = "" + period.getDays();
				switch(years.length()){
					case 1:
						years = add3 + years;
						break;
					case 2:
						years = add2 + years;
						break;
					case 3:
						years = add1 + years;
						break;
				}
				if(months.length() == 1)
					months = add1 + months;
				if(days.length() == 1)
					days = add1 + days;
				strDate = years + "-" + months + "-" + days;
				LocalTime locTime =
					LocalTime.of(
									duration.abs().toHoursPart(),
									duration.abs().toMinutesPart(),
									duration.abs().toSecondsPart()
					);
				strTime = locTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
				if(duration.isNegative())
					durationFormat = "-" + strDate + "T" + strTime;
				else
					durationFormat = strDate + "T" + strTime;
				break;
			case FULL:
				durationFormat = duration.toString();
				break;
		}
	}
	public boolean checkValid(ZonedDateTime dateTime){
		boolean validate;
		if(duration == null){
			validate = true;
		} else {
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
			ZonedDateTime expZoned = start.plus(duration);
			int compareStart = dateTime.toLocalDateTime().compareTo(start.toLocalDateTime());
			int compareExp = dateTime.toLocalDateTime().compareTo(expZoned.toLocalDateTime());
			if(compareStart > 0 && compareExp <= 0)
				validate = true;
			else
				validate = false;
		}
		if(validate)
			validStr = "is valid";
		else
			validStr = "is not valid";
		return validate;
	}
	public static void main(String[] args) {
		LocalDateTime ldtStart = LocalDateTime.of(2021,12,21,16,0,0);
		ZonedDateTime zdtStart = ldtStart.atZone(ZoneId.of("Europe/Moscow"));
		LocalDateTime ldtExp = LocalDateTime.of(2021,11,21,15,0,0);
		ZonedDateTime zdtExp = ldtExp.atZone(ZoneId.of("America/New_York"));
		LocalDateTime ldtValid = LocalDateTime.of(2022,6,12,16,0,0);
		ZonedDateTime zdtValid = ldtValid.atZone(ZoneId.of("Europe/Dublin"));
		Insurance ins = new Insurance(zdtStart);
		System.out.println(ins);
		Insurance insFormat = new Insurance(ins.toString(), FormatStyle.LONG);
		System.out.println(insFormat);
		ins.setDuration(Duration.ofDays(0).plus(Duration.ofHours(0)));
		ins.setDuration(ins.toString(), FormatStyle.LONG);
		System.out.println(ins);
		ins.checkValid(zdtValid);
		System.out.println(ins);
		ins.setDuration(zdtExp);
		ins.setDuration(ins.toString(), FormatStyle.LONG);
		System.out.println(ins);
		ins.checkValid(zdtValid);
		System.out.println(ins);
		ins.setDuration(22, 35, 30);
		ins.setDuration(ins.toString(), FormatStyle.FULL);
		System.out.println(ins);
		ins.checkValid(zdtValid);
		System.out.println(ins);
	}
}
