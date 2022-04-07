package ru.progwards.java1.lessons.sets;
import java.util.*;

public class ProductAnalytics {
	private List<Shop> shops;
	private List<Product> products;
	public ProductAnalytics(List<Product> products, List<Shop> shops){
		this.products = products;
		this.shops = shops;
	}
	public Set<Product> existInAll(){
		Set<Product> allProducts = new HashSet<>(products);
		HashSet interSet = new HashSet(allProducts);
		Set<Shop> allShops = new HashSet<Shop>(shops);
		Iterator<Shop> iterShops = allShops.iterator();
		while (iterShops.hasNext())
			interSet.retainAll(iterShops.next().getProducts());
		return interSet;
	}
	Set<Product> existAtListInOne(){
		Set<Product> allProducts = new HashSet<>(products);
		HashSet interSet = new HashSet(allProducts);
		Set<Shop> allShops = new HashSet<Shop>(shops);
		HashSet compareSet = new HashSet();
		Iterator<Shop> iterShops = allShops.iterator();
		while (iterShops.hasNext())
			compareSet.addAll(iterShops.next().getProducts());
		compareSet.retainAll(allProducts);
		return compareSet;
	}
	Set<Product> notExistInShops(){
		Set<Product> allProducts = new HashSet<>(products);
		HashSet exclusSet = new HashSet(allProducts);
		Set<Shop> allShops = new HashSet<Shop>(shops);
		HashSet compareSet = new HashSet();
		Iterator<Shop> iterShops = allShops.iterator();
		while (iterShops.hasNext())
			compareSet.addAll(iterShops.next().getProducts());
		exclusSet.removeAll(compareSet);
		return exclusSet;
	}
	Set<Product> existOnlyInOne(){
		Set<Shop> allShops = new LinkedHashSet<Shop>(shops);
		HashSet shopSet1 = new HashSet();
		HashSet shopSet2 = new HashSet();
		HashSet compareSet1 = new HashSet();
		HashSet compareSet2 = new HashSet();
		HashSet resultSet = new HashSet();
		Iterator<Shop> iterShops = allShops.iterator();
		while (iterShops.hasNext()){
			shopSet1.addAll(iterShops.next().getProducts());
			resultSet.addAll(shopSet1);
			if(compareSet2.isEmpty()){
				shopSet2.addAll(iterShops.next().getProducts());
			} else {
				shopSet2.addAll(compareSet2);
			}
			resultSet.addAll(shopSet2);
			shopSet1.retainAll(shopSet2);
			compareSet1.addAll(shopSet1);
			resultSet.removeAll(compareSet1);
			compareSet2.addAll(resultSet);
			shopSet1.clear();
			shopSet2.clear();
		}
		return resultSet;
	}
	public static void main(String[] args) {
		Product p1 = new Product("Молоко");
		Product p2 = new Product("Кефир");
		Product p3 = new Product("Сметана");
		Product p4 = new Product("Хлеб");
		Product p5 = new Product("Мясо");
		Product p6 = new Product("Колбаса");
		Product p7 = new Product("Кофе");
		Product p8 = new Product("Чай");
		Product p9 = new Product("Конфеты");
		Product p10 = new Product("Торты");
		Product p11 = new Product("Шампунь");
		Product p12 = new Product("Мыло");		
		Product p13 = new Product("Газеты");
		Product p14 = new Product("Игрушки");
		Product p15 = new Product("Грудинка");
		Product p16 = new Product("Яйца");
		Product p17 = new Product("Варенье");
		Product p18 = new Product("Сахар");
		Product p19 = new Product("Соль");
		Product p20 = new Product("Канцтовары");
		Product p21 = new Product("Пиво");
		Product p22 = new Product("Вода");
		List<Product> range1 = new ArrayList<Product>();
		range1.add(p1);
		range1.add(p2);
		range1.add(p3);
		range1.add(p4);
		range1.add(p5);
		range1.add(p6);
		range1.add(p8);
		range1.add(p16);
		range1.add(p21);
		range1.add(p22);
		List<Product> range2 = new ArrayList<Product>();
		range2.add(p4);
		range2.add(p7);
		range2.add(p8);
		range2.add(p9);
		range2.add(p10);
		range2.add(p17);
		range2.add(p21);
		range2.add(p22);
		List<Product> range3 = new ArrayList<Product>();
		range3.add(p1);
		range3.add(p2);
		range3.add(p3);
		range3.add(p4);
		range3.add(p5);
		range3.add(p6);
		range3.add(p7);
		range3.add(p8);
		range3.add(p9);
		range3.add(p10);
		range3.add(p11);
		range3.add(p12);
		range3.add(p13);
		range3.add(p18);
		range3.add(p21);
		range3.add(p22);
		List<Product> range4 = new ArrayList<Product>();
		range4.add(p5);
		range4.add(p6);
		range4.add(p15);
		range4.add(p21);
		range4.add(p22);
		List<Product> range5 = new ArrayList<Product>();
		range5.add(p1);
		range5.add(p2);
		range5.add(p3);
		range5.add(p4);
		range5.add(p5);
		range5.add(p6);
		range5.add(p8);
		range5.add(p19);
		range5.add(p21);
		range5.add(p22);
		List<Product> sumRange = new ArrayList<Product>();
		sumRange.add(p1);
		sumRange.add(p2);
		sumRange.add(p3);
		sumRange.add(p4);
		sumRange.add(p5);
		sumRange.add(p6);
		sumRange.add(p7);
		sumRange.add(p8);
		sumRange.add(p9);
		sumRange.add(p10);
		sumRange.add(p11);
		sumRange.add(p12);
		sumRange.add(p13);
		sumRange.add(p14);
		sumRange.add(p15);
		sumRange.add(p16);
		sumRange.add(p17);
		sumRange.add(p18);
		sumRange.add(p19);
		sumRange.add(p20);
		sumRange.add(p21);
		sumRange.add(p22);
	 	Shop shop1 = new Shop(range1);
		Shop shop2 = new Shop(range2);
		Shop shop3 = new Shop(range3);
		Shop shop4 = new Shop(range4);
		Shop shop5 = new Shop(range5);
		List<Shop> shops = new ArrayList<Shop>();
		shops.add(shop1);
		shops.add(shop2);
		shops.add(shop3);
		shops.add(shop4);
		shops.add(shop5);
		ProductAnalytics review = new ProductAnalytics(sumRange, shops);
		System.out.println(review.existInAll());
		System.out.println(review.existAtListInOne());
		System.out.println(review.notExistInShops());
		System.out.println(review.existOnlyInOne());
	}
}
