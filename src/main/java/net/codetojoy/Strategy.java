
package net.codetojoy;

import java.util.List;

sealed interface Strategy { 
    record MaxCard(List<Integer> cards) implements Strategy {}
    record MinCard(List<Integer> cards) implements Strategy {}
    record NextCard(List<Integer> cards) implements Strategy {}
    record NearestCard(List<Integer> cards, Integer prizeCard) implements Strategy {}
}

