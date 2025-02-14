package utils.predicate;

import java.lang.reflect.Proxy;
import java.util.List;

public class PredicateBuilder {
  public static <P> P createPredicate(List<Boolean> consultationArray) {
    return (P) Proxy.newProxyInstance(
        Predicate.class.getClassLoader(),
        new Class<?>[]{Predicate.class},
        (proxy, method, args) -> {
          return null;
        }
    );
  }
}
