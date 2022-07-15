package ua.epma.paymentsspring.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {


    public static void main(String[] args) {

        String email = "vald@gmail.com";

        Pattern p = Pattern.compile("^[\\w]+[^@$]");
        Matcher m = p.matcher(email);
        if (m.find()) {

            System.out.println(m.group());

        }
    }
}
