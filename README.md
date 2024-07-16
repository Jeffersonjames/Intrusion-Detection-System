# Intrusion-Detection-System


package adt;

/**
 *
 * @author admin
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Preprocess pr=new Preprocess();
        pr.readData();
        
        
        DecisionTree dt=new DecisionTree();
        dt.construct();
        
    }
    
}
