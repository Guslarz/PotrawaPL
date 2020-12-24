package potrawa.data.deliverer;

public class DelivererOrder {
  private int id_;
  private String restaurantName_;
  private String address_;
  private String paymentMethod_;
  private double price_;
  private String additionalInformation_;

  public int getId() {
    return id_;
  }

  public String getRestaurantName() {
    return restaurantName_;
  }

  public String getAddress() {
    return address_;
  }

  public String getPaymentMethod() {
    return paymentMethod_;
  }

  public double getPrice() {
    return price_;
  }

  public String getAdditionalInformation() {
    return additionalInformation_;
  }

  public void setId(int id) {
    id_ = id;
  }

  public void setRestaurantName(String restaurantName) {
    restaurantName_ = restaurantName;
  }

  public void setAddress(String address) {
    address_ = address;
  }

  public void setPaymentMethod(String paymentMethod) {
    paymentMethod_ = paymentMethod;
  }

  public void setPrice(double price) {
    price_ = price;
  }

  public void setAdditionalInformation(String additionalInformation) {
    additionalInformation_ = additionalInformation;
  }
}
