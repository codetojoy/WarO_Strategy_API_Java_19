
// note: i no longer own this domain
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

record Params(int prizeCard, int maxCard, String mode, List<Integer> cards) {}

public class StrategyService {
    private static final String CARDS = "cards";
    private static final String MAX_CARD = "max_card";
    private static final String MODE = "mode";
    private static final String PRIZE_CARD = "prize_card";

    // prize_card=10&max_card=12&mode=max&cards=4&cards=6&cards=2
    public String apply(List<String> inParams) {
        var prizeCard = -1;
        var maxCard = -1;
        var mode = "";
        var cards = new ArrayList<Integer>();
        
        for (var param : inParams) {
            var pair = param.split("=");
            var key = pair[0];
            var value = pair[1];
            if (key.equals(CARDS)) {
                var card = Integer.parseInt(value);
                cards.add(card);
            } else if (key.equals(MAX_CARD)) {
                maxCard = Integer.parseInt(value);
            } else if (key.equals(MODE)) {
                mode = value;
            } else if (key.equals(PRIZE_CARD)) {
                prizeCard = Integer.parseInt(value);
            }
        }

        var params = new Params(prizeCard, maxCard, mode, cards); 
        var choice = applyStrategy(params);
        String responseFormat = "{choice: %d, time: '%s'}";
        String response = String.format(responseFormat, choice, new Date().toString());
        return response;
    }

    Strategy findStrategy(Params params) {
        return switch (params.mode().toLowerCase()) {
            case "max" -> new Strategy.MaxCard(params.cards());
            case "min" -> new Strategy.MinCard(params.cards());
            case "nearest" -> new Strategy.NearestCard(params.cards(), params.prizeCard());
            case "next" -> new Strategy.NextCard(params.cards());
            case null -> new Strategy.NextCard(params.cards());
            default -> new Strategy.NextCard(params.cards());
        };
    }

    Integer applyStrategy(Params params) { 
        return switch (findStrategy(params)) { 
            case Strategy.MaxCard(var cards) -> maxCard(cards);
            case Strategy.MinCard(var cards) -> minCard(cards);
            case Strategy.NearestCard(var cards, var prizeCard) -> nearestCard(cards, prizeCard);
            case Strategy.NextCard(var cards) -> nextCard(cards);
        };
    }

    Integer maxCard(List<Integer> cards) {
        return 5150;
    }
    Integer minCard(List<Integer> cards) {
        return 5150;
    }
    Integer nearestCard(List<Integer> cards, Integer prizeCard) {
        return 5150;
    }
    Integer nextCard(List<Integer> cards) {
        return 5150;
    }
}
