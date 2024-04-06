
// Importing necessary modules for project
import java.util.ArrayList;
import java.util.List;

// Creating the store class that has the product details
class Store {
    private String product_name;
    private Double product_price;


    public Store(String product_name, Double product_price) {
        this.product_name = product_name;
        this.product_price = product_price;
    }

    // method that gets the product name
    public String FetchProductName() {
        return product_name;
    }

    // method that gets the product's price
    public double FetchProductPrice() {
        return product_price;
    }
}

//
class SellerAgent {
    private String product_name;
    private List<Store> Goods;

    public SellerAgent(String product_name) {
        this.product_name = product_name;
        this.Goods = new ArrayList<>();

    }

    public String FetchProductName() {
        return product_name;
    }

    public void addGoods(Store goods) {
        Goods.add(goods);

    }

    public List<Store> getGoods() {
        return Goods;
    }

    public boolean ableToSell(Store goods, int Quantity) {
        for (Store s : Goods) {
            if (s.FetchProductName().equals(goods.FetchProductName())
                    && s.FetchProductPrice() <= (goods.FetchProductPrice())) {
                return true;
            }

        }

        return false;
    }

    public double summedPrice(Store goods, int Quantity) {
        for (Store s : Goods) {
            if (s.FetchProductName().equals(goods.FetchProductName())) {
                return s.FetchProductPrice() * Quantity;
            }
        }
        return 0.0;
    }
}

class PurchasingAgent {
    private String purchaser_name;
    private BrokerAgent broker;

    public PurchasingAgent(String purchaser_name, BrokerAgent broker) {
        this.purchaser_name = purchaser_name;
        this.broker = broker;
    }

    public String FetchProductName() {
        return purchaser_name;
    }

    public void makeAnOrder(Store goods, int Quantity) {
        broker.processGoodsOrdered(this, goods, Quantity);

    }

    public void acknowledgeReceivedOrder(SellerAgent vendor, Store goods, double goodsPrice) {
        System.out.println(vendor.FetchProductName() + " is making an offer to sell " + goods.FetchProductName()
                + " at a price of " + goodsPrice);
    }

    public void purchasedOrder(SellerAgent vendor, Store goods, double goodsPrice) {
        System.out.println(purchaser_name + " purchased goods " + goods.FetchProductName() + " from the seller "
                + vendor.FetchProductName() + " at the selling price of " + goodsPrice);

    }

}

class BrokerAgent {
    private List<PurchasingAgent> purchasers;
    private List<SellerAgent> vendors;

    public BrokerAgent() {
        this.purchasers = new ArrayList<>();
        this.vendors = new ArrayList<>();
    }

    public void addToPurchasers(PurchasingAgent purchaser) {
        purchasers.add(purchaser);
    }

    public void addToVendors(SellerAgent vendor) {
        vendors.add(vendor);
    }

    public void processGoodsOrdered(PurchasingAgent purchaser, Store goods, int Quantity) {
        List<SellerAgent> invitedSellers = new ArrayList<>();
        for (SellerAgent vendor : vendors) {
            if (vendor.ableToSell(goods, Quantity)) {
                invitedSellers.add(vendor);
            }
        }

        if (invitedSellers.isEmpty()) {
            System.out.println(" No vendors available to fulfil goods demanded " + goods.FetchProductName());
        } else {
            for (SellerAgent vendor : invitedSellers) {
                double goodsPrice = vendor.summedPrice(goods, Quantity);
                purchaser.acknowledgeReceivedOrder(vendor, goods, goodsPrice);

            }
        }
    }

    public void purchaseRecommended(PurchasingAgent purchaser, SellerAgent vendor, Store goods, double goodsPrice) {
        purchaser.purchasedOrder(vendor, goods, goodsPrice);
    }
}

public class contract {
    public static void main(String[] args) {
        BrokerAgent broker = new BrokerAgent();

        SellerAgent first_vendor = new SellerAgent("FIRST VENDOR");
        first_vendor.addGoods(new Store("Product 1", 10.0));
        first_vendor.addGoods(new Store("Product 2", 20.0));
        broker.addToVendors(first_vendor);

        SellerAgent second_vendor = new SellerAgent("SECOND VENDOR");
        second_vendor.addGoods(new Store("Product 2", 18.0));
        second_vendor.addGoods(new Store("Product 3", 25.0));
        broker.addToVendors(second_vendor);

        PurchasingAgent purchaser = new PurchasingAgent("FIRST PURCHASER", broker);
        broker.addToPurchasers(purchaser);

        Store goods = new Store("Product 2", 27.0);
        int Quantity = 1;

        purchaser.makeAnOrder(goods, Quantity);
    }
}
