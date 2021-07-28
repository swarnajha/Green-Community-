package com.laureen.expirydatetracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class DateComparator implements Comparator<Item> {
    @Override
    public int compare(Item i1, Item i2) {
        //return 1 if rhs should be before lhs
        //return -1 if lhs should be before rhs
        //return 0 otherwise (meaning the order stays the same)
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Date date1 = null, date2 = null;
        try {
            date1 = format.parse(i1.getDate());
            date2 = format.parse(i2.getDate());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        int result = date1.compareTo(date2);
        return Integer.compare(result, 0);
    }
}
