package entities;
import javax.persistence.*;
/** Produit du TP, stocké dans MySQL. */
@Entity
public class Product {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private int id;
    @Column(nullable=false,unique=true) private String name;
    private double price;
    public Product(){}public Product(String name,double price){this.name=name;this.price=price;}
    public int getId(){return id;}public void setId(int id){this.id=id;}
    public String getName(){return name;}public void setName(String name){this.name=name;}
    public double getPrice(){return price;}public void setPrice(double price){this.price=price;}
    public String toString(){return "Product{id="+id+", name='"+name+"', price="+price+"}";}
}
