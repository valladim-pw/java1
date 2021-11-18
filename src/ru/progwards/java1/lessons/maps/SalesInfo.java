package ru.progwards.java1.lessons.maps;
import java.io.*;
import java.util.*;
public class SalesInfo {
	List<String> orderList = new ArrayList<>();
	public static class FileLoadError extends RuntimeException {
		public String fileName = "";
		public String msg = "";
		public String className = "";
		public FileLoadError(String fileName, String msg){
			super(msg);
			this.msg = msg;
			this.fileName = fileName;
			className = this.getClass().getName();
		}
		@Override
		public String getMessage() {
			if(msg != null && msg.indexOf("(") != -1 && msg.indexOf("(O") == -1 ){
				msg = msg.substring(msg.indexOf("("), msg.indexOf(")"));
				msg = msg.substring(1);
			}
			if(className.indexOf("$") != -1 ){
				className = className.substring(className.indexOf("$") + 1);
			}
			return msg;
		}
		@Override
		public String toString() {
			String nullStr = "file name is null";
			if(msg == null)
				msg = nullStr;
			return className + ": " + "\"" + fileName + "\""  + " -> " + msg ;
		}
	}
	public int loadOrders(String fileName){
		int count = 0;
		try(FileReader reader = new FileReader(fileName);Scanner scanner = new Scanner(reader))	{
			while(scanner.hasNextLine()){
				String string = scanner.nextLine();
				List<String> list = Arrays.asList(string.split(","));
				boolean ok = false;
				if(!string.isBlank() && list.size() == 4){
					for(int i = 2; i < list.size(); i++){
						if(list.get(i).trim().charAt(0) == 48){
							ok = false;
							break;
						}
						for(Character c : list.get(i).trim().toCharArray()) {
							if (Character.isDigit(c)){
								ok = true;
							} else {
								ok = false;
								break;
							}
						}
						if(!ok)
							break;
					}
					if(ok){
						orderList.add(string + "\n");
						count++;
					}
				}
			}
			System.out.println(count);
			return count;
		} catch (Exception e){
			FileLoadError fle = new FileLoadError( fileName, e.getMessage());
			fle.getMessage();
			throw fle;
		}
	}
	public Map<String, Double> getGoods(){
		Map<String, Double> goods = new TreeMap<>();
		for(String good : orderList){
			List<String> goodList = Arrays.asList(good.split(","));
			String key = goodList.get(1).trim();
			Double sum = Double.parseDouble(goodList.get(3).trim());
			Double newSum = 0.0;
			Double oldSum = goods.putIfAbsent(key, sum);
			if(oldSum != null){
				newSum = oldSum + sum;
				goods.replace(key, newSum);
			} else {
				key = "";
				sum = 0.0;
			}
		}
		return goods;
	}
	public Map<String, AbstractMap.SimpleEntry<Double, Integer>> getCustomers(){
		Map<String, AbstractMap.SimpleEntry<Double, Integer>> customers = new TreeMap<>();
		for(String good : orderList){
			List<String> goodList = Arrays.asList(good.split(","));
			String key = goodList.get(0);
			Double sumKey = Double.parseDouble(goodList.get(3).trim());
			Integer numValue = Integer.parseInt(goodList.get(2).trim());
			var order = new AbstractMap.SimpleEntry<>(sumKey, numValue);
			var oldOrder = customers.putIfAbsent(key, order);
			if(oldOrder != null){
				Double newSumKey = Double.parseDouble(goodList.get(3).trim());
				Integer newNumValue = Integer.parseInt(goodList.get(2).trim());
				newSumKey += oldOrder.getKey();
				newNumValue += oldOrder.getValue();
				var newOrder = new AbstractMap.SimpleEntry<>(newSumKey, newNumValue);
				customers.replace(key, newOrder);
			} else {
				key = "";
				sumKey = 0.0;
				numValue = 0;
			}
		}
		return customers;
	}
	public static void main(String[] args) {
		SalesInfo salesInfo = new SalesInfo();
		try{
			salesInfo.loadOrders("orders.txt");
			System.out.println(salesInfo.getGoods());
			System.out.println(salesInfo.getCustomers());
		} catch(RuntimeException e){
			System.out.println(e);
		}
	}
}
