package com.example.ismailamrani.comptable.utils.parsing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 22/05/2016.
 */
public class ListComparison {
    public static boolean areEqual(List<?> l1, List<?> l2 ) {
        // make a copy of the list so the original list is not changed,
        // and remove() is supported
        ArrayList<?> cp = new ArrayList<>( l1 );
        for ( Object o : l2 ) {
            if ( !cp.remove( o ) ) {
                return false;
            }
        }
        return cp.isEmpty();
    }
}
