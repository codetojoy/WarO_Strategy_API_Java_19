
package net.codetojoy;

import java.util.*;

// Q: no tests?
// A: i will probably regret it but this http layer is meant to be quick & dirty. 
//    it may or may not be quick, but it is certainly dirty: achievement unlocked.
//
// Q: no framework?
// A: it is hard to say what is currently supported for JDK 19, so I'm rolling my own for now 
//    The real point here is to understand `sealed interface`/records in JDK 19, and this file is
//    just undifferentiated heavy-lifting.

public class StrategyService {
    // prize_card=10&max_card=12&mode=max&cards=4&cards=6&cards=2
    public String apply(List<String> params) {
        String response = "Hi from waro v2!";
        return response;
    }
}
