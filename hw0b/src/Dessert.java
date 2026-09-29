public class Dessert{
    /*
    Create a class called Dessert (you’ll need to create a new file and add it to Git)
    inside of the src/ folder. This class should have the following characteristics:

Two instance variables: int flavor and int price.
A constructor that takes two parameters int flavor and int price and sets the instance variables accordingly.
One static variable int numDesserts that keeps track of the number of desserts created so far.
A method public void printDessert() that prints the flavor and price of the dessert, along with the total
 number of desserts created so far, separated by a space.
For example, if we create a dessert with flavor 1 and price 2, and then call its printDessert() method,
 it should print 1 2 1.
If we then create a dessert with flavor 3 and price 4, and then call its printDessert() method, it should print 3 4 2.
Lastly, a method public static void main(String[] args) that only prints the line I love dessert! when executed.
     */
    int flavor;
    int price;
    public static int numDesserts = 0;

    public Dessert(int flavor, int price){
        this.flavor = flavor;
        this.price = price;
        numDesserts++;
    }

    public void printDessert(){
        System.out.println(flavor+" "+price+" "+numDesserts);
    }

    public static void main(String[] args){
        System.out.println("I love dessert!");
    }
}