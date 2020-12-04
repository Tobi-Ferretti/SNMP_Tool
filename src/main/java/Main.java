import org.soulwing.snmp.*;

public class Main {
    public static void main(String[] args){

        SimpleSnmpV2cTarget target = new SimpleSnmpV2cTarget();
        target.setAddress("10.0.0.1");
        target.setCommunity("public");

        SnmpContext context = SnmpFactory.getInstance().newContext(target);
        try {
            // perform some SNMP operations
        }
        finally {
            context.close();
        }
    }
}
