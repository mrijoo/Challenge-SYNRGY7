
package com.ch.binarfud.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@NamedStoredProcedureQuery(
    name = "transfer_balance",
    procedureName = "transfer_balance",
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "sender_id", type = UUID.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "receiver_id", type = UUID.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "amount", type = Double.class)
    }
)
public class User extends BaseEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private double balance = 0;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Merchant merchant;
}
