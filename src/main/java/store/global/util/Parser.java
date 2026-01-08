package store.global.util;

import java.util.List;

public interface Parser<T> {
    List<T> parse(String input, String delimiter);
}