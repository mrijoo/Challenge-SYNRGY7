package com.ch.binarfud.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private transient List<OrderDetail> orderDetails;

    @Column(nullable = false)
    private String address;

    public enum Status {
        PENDING,
        PROCESS,
        SUCCESS,
        CANCELLED,
        EXPIRED
    }

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    public enum PaymentMethod {
        BALANCE,
        TRANSFER
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;
}
