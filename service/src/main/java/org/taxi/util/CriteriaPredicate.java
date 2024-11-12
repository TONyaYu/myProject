package org.taxi.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@NoArgsConstructor
public class CriteriaPredicate {

    @Getter
    private final List<Predicate> predicates = new ArrayList<>();

    public static CriteriaPredicate builder() {
        return new CriteriaPredicate();
    }

    public <T> CriteriaPredicate add(T object, Function<T, Predicate> function) {
        if (object != null) {
            predicates.add(function.apply(object));
        }
        return this;
    }

    public <T, N> CriteriaPredicate addNested(T object, Function<T, N> nestedFunction, Function<N, Predicate> function) {
        if (object != null) {
            N nestedObject = nestedFunction.apply(object);
            if (nestedObject != null) {
                predicates.add(function.apply(nestedObject));
            }
        }
        return this;
    }

    public Predicate build(CriteriaBuilder criteriaBuilder) {
        if (predicates.isEmpty()) {
            return criteriaBuilder.conjunction();
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

}
