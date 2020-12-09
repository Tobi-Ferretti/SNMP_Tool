import org.soulwing.snmp.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        Mib mib = MibFactory.getInstance().newMib();
        mib.load("SNMPv2-MIB");

        SimpleSnmpV2cTarget target = new SimpleSnmpV2cTarget();
        target.setAddress("10.0.0.1");
        target.setCommunity("public");

        SnmpContext context = SnmpFactory.getInstance().newContext(target, mib);
        try {
            VarbindCollection result = context.getNext("sysUpTime").get();
            System.out.println(result.get("sysUpTime"));
        }
        finally {
            context.close();
        }
    }
}
