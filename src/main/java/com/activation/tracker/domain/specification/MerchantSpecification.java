package com.activation.tracker.domain.specification;

import com.activation.tracker.domain.entity.Merchant;
import com.activation.tracker.domain.enums.MerchantStatus;
import org.springframework.data.jpa.domain.Specification;

public class MerchantSpecification {

    public static Specification<Merchant> hasStatus(MerchantStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Merchant> nameContains(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
}
