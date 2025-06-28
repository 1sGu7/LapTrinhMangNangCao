package server;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubdomainScanner {
    public static String scan(String domain) {
        System.out.println("[Scanner] Starting scan for: " + domain);

        // Test with a small set of subdomains
        List<String> testSubdomains = Arrays.asList(
                "www", "mail", "ftp", "webmail", "portal", "blog",
                "dev", "test", "api", "admin", "cdn", "shop"
        );

        List<String> foundSubdomains = new ArrayList<>();

        for (String sub : testSubdomains) {
            String fullDomain = sub + "." + domain;
            System.out.println("[Scanner] Checking: " + fullDomain);

            if (checkDNS(fullDomain)) {
                foundSubdomains.add(fullDomain);
                System.out.println("[Scanner] Found: " + fullDomain);
            }
        }

        return foundSubdomains.isEmpty() ?
                "No subdomains found" :
                "Found subdomains:\n" + String.join("\n", foundSubdomains);
    }

    private static boolean checkDNS(String domain) {
        try {
            InetAddress.getByName(domain);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}