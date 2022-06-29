
// note: i no longer own this domain
package net.codetojoy;

import java.util.*;
import java.util.stream.IntStream;

// Q: no tests?
// A: i will probably regret it but this http layer is meant to be quick & dirty. 
//    it may or may not be quick, but it is certainly dirty: achievement unlocked.
//
// Q: no framework?
// A: it is hard to say what is currently supported for JDK 19, so I'm rolling my own for now 
//    The real point here is to understand `sealed interface`/records in JDK 19, and this file is
//    just undifferentiated heavy-lifting.

record Query(int prizeCard, int maxCard, String mode, List<Integer> cards) {}

record Distance(Integer i, Integer distance) {}

public class StrategyService {
    private static final String CARDS = "cards";
    private static final String MAX_CARD = "max_card";
    private static final String MODE = "mode";
    private static final String PRIZE_CARD = "prize_card";

    // prize_card=10&max_card=12&mode=max&cards=4&cards=6&cards=2
    public String apply(List<Param> inParams) {
        var prizeCard = -1;
        var maxCard = -1;
        var mode = "";
        var cards = new ArrayList<Integer>();
        
        for (var param : inParams) {
            var key = param.key();
            var value = param.value();
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

        var query = new Query(prizeCard, maxCard, mode, cards); 
        var choice = applyStrategy(query);
        String responseFormat = "{choice: %d, time: '%s'}";
        String response = String.format(responseFormat, choice, new Date().toString());
        return response;
    }

    Strategy findStrategy(Query query) {
        return switch (query.mode().toLowerCase()) {
            case "max" -> new Strategy.MaxCard(query.cards());
            case "min" -> new Strategy.MinCard(query.cards());
            case "nearest" -> new Strategy.NearestCard(query.cards(), query.prizeCard());
            case "next" -> new Strategy.NextCard(query.cards());
            case null -> new Strategy.NextCard(query.cards());
            default -> new Strategy.NextCard(query.cards());
        };
    }

    Integer applyStrategy(Query query) { 
        return switch (findStrategy(query)) { 
            case Strategy.MaxCard(var cards) -> maxCard(cards);
            case Strategy.MinCard(var cards) -> minCard(cards);
            case Strategy.NearestCard(var cards, var prizeCard) -> nearestCard(cards, prizeCard);
            case Strategy.NextCard(var cards) -> nextCard(cards);
        };
    }

    IntStream intStream(List<Integer> cards) {
        return cards.stream().flatMapToInt(IntStream::of);
    }

    Integer maxCard(List<Integer> cards) {
        var optional = intStream(cards).max();
        return optional.getAsInt();
    }

    Integer minCard(List<Integer> cards) {
        var optional = intStream(cards).min();
        return optional.getAsInt();
    }

    Integer nearestCard(List<Integer> cards, Integer prizeCard) {
        var distances = intStream(cards).mapToObj(i -> new Distance(i, Math.abs(i - prizeCard)));
        var infoComparator = Comparator.comparing(Distance::distance);
        var result = distances.min(infoComparator).get();
        return result.i();
    }

    Integer nextCard(List<Integer> cards) {
        return cards.get(0);
    }
}
